package io.github.kloping.kzero.spring.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import io.github.kloping.common.Public;
import io.github.kloping.kzero.bot.controllers.AllController;
import io.github.kloping.kzero.spring.dao.GroupConf;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.ListenerHost;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

/**
 * @author github kloping
 * @date 2024/12/4-13:22
 */
@Slf4j
public class KeptClient extends WebSocketClient implements ListenerHost {
    public KeptClient(URI uri) {
        super(uri);
    }

    @Value("${kpet.api}")
    private String base;

    private String token;

    @Override
    public void onOpen(ServerHandshake handshakedata) {
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
        }
    }

    public Map<String, Command> commandMap = new HashMap<>();

    private void toInitCommands() {
        JSONArray commands = (JSONArray) reqGet("/api/commands");
        for (Object command : commands) {
            JSONObject jo = (JSONObject) command;
            Command cmd = jo.toJavaObject(Command.class);
            commandMap.put(cmd.getName(), cmd);
        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        log.error("client closed,code:{},reason:{}", code, reason);
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
        Command command = commandMap.get(msg.trim());
        if (command.getArgs() != null && !command.getArgs().isEmpty()) {
            Command.StepArg sa = null;
            for (Command.StepArg arg : command.getArgs()) {

            }
        }
    }

    private JSON reqGet(String api, Map.Entry<String, String>... entries) {
        StringBuilder ub = new StringBuilder(base + api + "?");
        for (Map.Entry<String, String> parm : entries) {
            ub.append(parm.getKey()).append("=").append(parm.getValue()).append("&");
        }
        Document doc0 = null;
        try {
            doc0 = Jsoup.connect(ub.toString())
                    .header("token", token)
                    .ignoreContentType(true)
                    .ignoreHttpErrors(true)
                    .get();
        } catch (IOException e) {
            log.error("req get error", e);
            return null;
        }
        return (JSON) JSON.parse(doc0.body().text());
    }

    @Data
    private static class Command {
        private String name;
        private String desc;
        private String path;
        private List<StepArg> args = new ArrayList<>();

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
        }
    }
}
