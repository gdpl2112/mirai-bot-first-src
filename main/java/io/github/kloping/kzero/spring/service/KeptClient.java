package io.github.kloping.kzero.spring.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import io.github.kloping.common.Public;
import io.github.kloping.kzero.bot.controllers.AllController;
import io.github.kloping.kzero.main.KlopZeroMainThreads;
import io.github.kloping.kzero.main.api.KZeroBot;
import io.github.kloping.kzero.main.api.KZeroBotAdapter;
import io.github.kloping.kzero.spring.dao.GroupConf;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.User;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.ListenerHost;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.QuoteReply;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author github kloping
 * @date 2024/12/4-13:22
 */
@Slf4j
public class KeptClient extends WebSocketClient implements ListenerHost {
    public KeptClient(URI uri, String base) {
        super(uri);
        this.base = base;
    }

    private String base;

    private String token;

    @Override
    public void onOpen(ServerHandshake a) {
        log.info("kpet client opened");
    }

    @Override
    public void onMessage(String message) {
        JSONObject jo = JSON.parseObject(message);
        String type = jo.getString("type");
        if ("auth".equals(type)) {
            token = jo.getString("token");
            log.info("kpet client auth success");
            toInitCommands();
        } else {
            switch (type) {
                case "send": {
                    String etype = jo.getString("etype");
                    String eid = jo.getString("eid");
                    String msg = jo.getString("msg");
                    for (KZeroBot value : KlopZeroMainThreads.BOT_MAP.values()) {
                        if (value.getSelf() instanceof Bot) {
                            Bot bot = (Bot) value.getSelf();
                            long e = Long.parseLong(eid);
                            if ("group".equals(etype)) {
                                Group group = bot.getGroupOrFail(e);
                                if (group != null) group.sendMessage(msg);
                            } else {
                                User user = bot.getFriend(e);
                                if (user != null) user.sendMessage(msg);
                            }
                        }
                    }

                }
                break;
            }
        }
    }

    public Map<String, Command> commandMap = new HashMap<>();

    private void toInitCommands() {
        JSONArray commands = reqGet(JSONArray.class, null, "/api/commands");
        if (commands != null && !commands.isEmpty()) {
            commandMap.clear();
        }
        for (Object command : commands) {
            JSONObject jo = (JSONObject) command;
            Command cmd = jo.toJavaObject(Command.class);
            commandMap.put(cmd.getName(), cmd);
        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        log.error("client closed,code:{},reason:{}", code, reason);
        Public.EXECUTOR_SERVICE.submit(() -> {
            try {
                TimeUnit.SECONDS.sleep(15);
                reconnect();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public void onError(Exception ex) {
        log.error("client error", ex);
    }

    private Map<String, Command.StepArg> id2step = new HashMap<>();

    @EventHandler
    public void onMessage(GroupMessageEvent event) throws Exception {
        if (AllController.isClosed(event.getSubject().getId())) return;
        String msg = event.getMessage().contentToString();
        String sid = String.valueOf(event.getSender().getId());
        if (id2step.containsKey(sid)) {
            Command.StepArg sa = id2step.get(sid);
            sa.setValue(msg);
            exc(event, sa.getNext());
            return;
        }
        Command command = commandMap.get(msg.trim());
        Command.StepArg sa = null;
        if (command != null) {
            if (!command.getArgs().isEmpty()) {
                for (Command.StepArg arg : command.getArgs()) {
                    if (sa == null) {
                        sa = arg;
                        sa.setCommand(command);
                        id2step.put(sid, sa);
                    } else {
                        sa.setNext(arg);
                        sa.setCommand(command);
                        sa = arg;
                    }
                }
                exc(event, id2step.get(sid));
                return;
            }
            exc(event, command);
        }
    }

    private void exc(GroupMessageEvent event, Command command) {
        JSONObject reqGet = reqGet(JSONObject.class, event, command.getPath());
        if (reqGet != null) {
            event.getSubject().sendMessage(reqGet.getString("msg"));
        }
    }


    private void exc(GroupMessageEvent event, Command.StepArg sa) {
        String sid = String.valueOf(event.getSender().getId());
        if (sa != null) {
            if (sa.action.equalsIgnoreCase("收集")) {
                MessageChainBuilder builder = new MessageChainBuilder();
                builder.append(new QuoteReply(event.getMessage()));
                builder.append(sa.getDesc());
                event.getSubject().sendMessage(builder.build());
                return;
            } else if (sa.action.equalsIgnoreCase("sid")) {
                sa.setValue(sid);
                exc(event, sa.getNext());
            }
        } else {
            Command.StepArg step = id2step.remove(sid);
            String api = step.getCommand().getPath();
            List<Map.Entry<String, String>> entries = new ArrayList<>();
            while (step != null) {
                entries.add(new AbstractMap.SimpleEntry<>(step.getName(), step.getValue()));
                step = step.getNext();
            }
            JSONObject reqGet = reqGet(JSONObject.class, event, api, entries.toArray(new Map.Entry[0]));
            if (reqGet != null) {
                event.getSubject().sendMessage(reqGet.getString("msg"));
            }
        }
    }

    private JSON reqGet(MessageEvent event, String api, Map.Entry<String, String>... entries) {
        StringBuilder ub = new StringBuilder(base + api + "?");
        for (Map.Entry<String, String> parm : entries) {
            ub.append(parm.getKey()).append("=").append(parm.getValue()).append("&");
        }
        Document doc0 = null;
        try {
            Connection connection = Jsoup.connect(ub.toString()).ignoreContentType(true).ignoreHttpErrors(true);
            connection.header("token", token).header("timestamp", String.valueOf(System.currentTimeMillis()));
            if (event != null) {
                connection.header("subject_id", String.valueOf(event.getSubject()))
                        .header("sender_id", String.valueOf(event.getSender()));
            }
            doc0 = connection.get();
            return (JSON) JSON.parse(doc0.body().wholeText());
        } catch (Exception e) {
            log.error("req get error", e);
            return JSON.parseObject("{\"status\":-1,\"msg\":\"请求异常,请联系管理员反馈!\"}");
        }
    }

    private <T extends JSON> T reqGet(Class<T> cls, MessageEvent event, String api, Map.Entry<String, String>... entries) {
        return (T) reqGet(event, api, entries);
    }

    @Data
    private static class Command {
        private String name;
        private String desc;
        private String path;
        private List<StepArg> args = new ArrayList<>();

        @Override
        public String toString() {
            return "Command{" +
                    "name='" + name + '\'' +
                    ", desc='" + desc + '\'' +
                    ", path='" + path + '\'' +
                    ", args=" + args.size() +
                    '}';
        }

        @Data
        private static class StepArg {
            //需要的参数名
            private String name;
            //前置描述
            private String desc;
            //参数采取动作
            private String action = "收集";

            private Command command;
            private StepArg next;
            private String value;
        }

    }
}
