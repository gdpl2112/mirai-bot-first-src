package io.github.kloping.kzero.guilds;

import com.alibaba.fastjson.JSONArray;
import io.github.kloping.kzero.gsuid.*;
import io.github.kloping.kzero.main.KZeroMainThreads;
import io.github.kloping.kzero.main.api.*;
import io.github.kloping.qqbot.Starter;
import io.github.kloping.qqbot.api.SendAble;
import io.github.kloping.qqbot.api.event.ConnectedEvent;
import io.github.kloping.qqbot.api.message.MessageChannelReceiveEvent;
import io.github.kloping.qqbot.api.message.MessageDirectReceiveEvent;
import io.github.kloping.qqbot.api.message.MessageEvent;
import io.github.kloping.qqbot.entities.Bot;
import io.github.kloping.qqbot.entities.ex.At;
import io.github.kloping.qqbot.entities.ex.Image;
import io.github.kloping.qqbot.entities.ex.MessageAsyncBuilder;
import io.github.kloping.qqbot.entities.ex.PlainText;
import io.github.kloping.qqbot.entities.ex.msg.MessageChain;
import io.github.kloping.qqbot.impl.EventReceiver;
import io.github.kloping.qqbot.impl.ListenerHost;

import java.util.*;

/**
 * @author github.kloping
 */
public class GuildStater extends ListenerHost implements KZeroStater {
    private BotCreated listener;
    private BotMessageHandler handler;

    @Override
    public void setHandler(KZeroBot bot, BotMessageHandler handler) {
        this.handler = handler;
    }

    @Override
    public void setCreated(BotCreated listener) {
        this.listener = listener;
    }

    private String appid;
    private String token;
    private Integer code;

    public GuildStater(String appid, String token, Integer code) {
        this.appid = appid;
        this.token = token;
        this.code = code;
    }

    @Override
    public void run() {
        Starter starter = new Starter(appid,token);
        starter.getConfig().setCode(code);
        starter.registerListenerHost(this);
        starter.run();
    }

    public KZeroBot<SendAble, Bot> create(String bid, Bot o, KZeroBotAdapter adapter, MessageSerializer<SendAble> serializer) {
        KZeroBot<SendAble, Bot> bot = new KZeroBot<SendAble, Bot>() {
            @Override
            public String getId() {
                return bid;
            }

            @Override
            public KZeroBotAdapter getAdapter() {
                return adapter;
            }

            @Override
            public MessageSerializer<SendAble> getSerializer() {
                return serializer;
            }

            @Override
            public Bot getSelf() {
                return o;
            }
        };
        return bot;
    }

    @Override
    public void handleException(Throwable e) {
        e.printStackTrace();
    }

    @EventReceiver
    public void onConnectedEvent(ConnectedEvent event) {
        String bid = event.getBot().getId();
        GsuidClient.INSTANCE.addListener(bid, new GsuidMessageListener() {

            @Override
            public void onMessage(MessageOut out) {
                MessageEvent raw = getMessage(out.getMsg_id());
                MessageAsyncBuilder builder = new MessageAsyncBuilder();
                if (raw instanceof MessageChannelReceiveEvent) {
                    builder.append(new At(At.MEMBER_TYPE, raw.getSender().getUser().getId()));
                    builder.append(new PlainText("\n"));
                }
                for (MessageData d0 : out.getContent()) {
                    if (d0.getType().equals("node")) {
                        try {
                            JSONArray array = (JSONArray) d0.getData();
                            for (MessageData d1 : array.toJavaList(MessageData.class)) {
                                builderAppend(builder, d1);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else builderAppend(builder, d0);
                }
                builder.reply(raw.getRawMessage());
                raw.send(builder.build());
            }

            private void builderAppend(MessageAsyncBuilder builder, MessageData d0) {
                if (d0.getType().equals("text")) {
                    builder.append(new PlainText(d0.getData().toString().trim()));
                } else if (d0.getType().equals("image")) {
                    byte[] bytes = Base64.getDecoder().decode(d0.getData().toString().substring("base64://".length()));
                    builder.append(new Image(bytes));
                }
            }
        });
        if (KZeroMainThreads.BOT_MAP.containsKey(bid)) return;
        onConnectedEventFirst(event);
    }

    private void onConnectedEventFirst(ConnectedEvent event) {
        Bot bot = event.getBot();
        GuildSerializer guildSerializer = new GuildSerializer();
        KZeroBot<SendAble, Bot> kZeroBot = create(bot.getId(), bot, new GuildBotAdapter(bot, guildSerializer), guildSerializer);
        listener.created(this, kZeroBot);
    }

    @EventReceiver
    public void onEvent(MessageChannelReceiveEvent event) {
        MessageChain chain = event.getMessage();
        offer(event);
        if (handler != null) {
            KZeroBot<SendAble, Bot> kZeroBot = KZeroMainThreads.BOT_MAP.get(String.valueOf(event.getBot().getId()));
            MessagePack pack = new MessagePack(MessageType.GROUP, event.getSender().getUser().getId(),
                    event.getChannelId(), kZeroBot.getSerializer().serialize(event.getMessage()));
            pack.setRaw(event);
            handler.onMessage(pack);
            //plugin to gsuid
            sendToGsuid(event);
        }
    }

    @EventReceiver
    public void onEvent(MessageDirectReceiveEvent event) {
        MessageChain chain = event.getMessage();
        offer(event);
        if (handler != null) {
            KZeroBot<SendAble, Bot> kZeroBot = KZeroMainThreads.BOT_MAP.get(String.valueOf(event.getBot().getId()));
            MessagePack pack = new MessagePack(MessageType.GROUP, event.getSender().getUser().getId(),
                    event.getSrcGuildId(), kZeroBot.getSerializer().serialize(event.getMessage()));
            pack.setRaw(event);
            handler.onMessage(pack);
            //plugin to gsuid
            sendToGsuid(event);
        }
    }

    private void sendToGsuid(MessageEvent event) {
        MessageChain chain = event.getMessage();
        List<MessageData> list = new ArrayList<>();
        chain.forEach(e -> {
            MessageData message = new MessageData();
            if (e instanceof PlainText) {
                message.setType("text");
                String data = e.toString().trim();
                if (data.equalsIgnoreCase("/gs帮助")) data = "gs帮助";
                message.setData(data);
            } else if (e instanceof Image) {
                Image image = (Image) e;
                message.setType("image");
                message.setData(image.getUrl().startsWith("http") ? image.getUrl() : "https://" + image.getUrl());
            } else return;
            list.add(message);
        });
        if (list.size() > 0) {
            MessageReceive receive = new MessageReceive();
            receive.setBot_id("pd-client");
            receive.setBot_self_id(event.getBot().getId());
            receive.setUser_id(event.getSender().getUser().getId());
            receive.setMsg_id(event.getRawMessage().getId());
            receive.setUser_type("direct");
            receive.setGroup_id("");
            if (event instanceof MessageChannelReceiveEvent) {
                receive.setUser_type("group");
                receive.setGroup_id(((MessageChannelReceiveEvent) event).getChannel().getId());
            }
            if ("7749068863541459083".equals(event.getSender().getUser().getId())) receive.setUser_pm(0);
            else receive.setUser_pm(2);
            receive.setContent(list.toArray(new MessageData[0]));
            GsuidClient.INSTANCE.send(receive);
        }
    }

    //=============消息记录start
    private static final Integer MAX_E = 50;

    private Deque<MessageEvent> QUEUE = new LinkedList<>();

    private void offer(MessageEvent msg) {
        if (QUEUE.contains(msg)) return;
        if (QUEUE.size() >= MAX_E) QUEUE.pollLast();
        QUEUE.offerFirst(msg);
    }

    private MessageEvent temp0 = null;

    private MessageEvent getMessage(String id) {
        if (temp0 != null && temp0.getRawMessage().getId().equals(id)) return temp0;
        for (MessageEvent event : QUEUE) {
            if (event.getRawMessage().getId().equals(id)) return temp0 = event;
        }
        return null;
    }
    //=============消息记录end
}
