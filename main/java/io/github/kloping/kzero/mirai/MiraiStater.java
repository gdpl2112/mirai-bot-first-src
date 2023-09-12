package io.github.kloping.kzero.mirai;

import com.alibaba.fastjson.JSONArray;
import io.github.kloping.common.Public;
import io.github.kloping.kzero.gsuid.*;
import io.github.kloping.kzero.main.KZeroMainThreads;
import io.github.kloping.kzero.main.api.*;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.console.terminal.MiraiConsoleImplementationTerminal;
import net.mamoe.mirai.console.terminal.MiraiConsoleTerminalLoader;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.GlobalEventChannel;
import net.mamoe.mirai.event.ListenerHost;
import net.mamoe.mirai.event.events.*;
import net.mamoe.mirai.message.data.*;

import java.io.ByteArrayInputStream;
import java.nio.file.Paths;
import java.util.*;

/**
 * @author github.kloping
 */
public class MiraiStater implements KZeroStater, ListenerHost {
    private BotCreated listener;
    private Map<KZeroBot, BotMessageHandler> handlerMap = new HashMap<>();

    @Override
    public void setHandler(KZeroBot bot, BotMessageHandler handler) {
        handlerMap.put(bot, handler);
    }

    @Override
    public void setCreated(BotCreated listener) {
        this.listener = listener;
    }

    @Override
    public void run() {
        Public.EXECUTOR_SERVICE.submit(() -> {
            MiraiConsoleImplementationTerminal terminal = new MiraiConsoleImplementationTerminal(Paths.get("works"));
            MiraiConsoleTerminalLoader.INSTANCE.startAsDaemon(terminal);
        });
        GlobalEventChannel.INSTANCE.registerListenerHost(this);
    }

    public KZeroBot<MessageChain, Bot> create(String bid, Bot o, KZeroBotAdapter adapter, MessageSerializer<MessageChain> serializer) {
        KZeroBot<MessageChain, Bot> bot = new KZeroBot<MessageChain, Bot>() {
            @Override
            public String getId() {
                return bid;
            }

            @Override
            public KZeroBotAdapter getAdapter() {
                return adapter;
            }

            @Override
            public MessageSerializer<MessageChain> getSerializer() {
                return serializer;
            }

            @Override
            public Bot getSelf() {
                return o;
            }
        };
        return bot;
    }

    @EventHandler
    public void onBotOnline(BotOnlineEvent event) {
        String bid = String.valueOf(event.getBot().getId());
        if (KZeroMainThreads.BOT_MAP.containsKey(bid)) return;
        onBotOnlineFirst(event);
    }

    public void onBotOnlineFirst(BotOnlineEvent event) {
        System.out.format("==================%s(%s)-上线了=====================\n", event.getBot().getId(), event.getBot().getNick());
        MiraiSerializer miraiSerializer = new MiraiSerializer(event.getBot());
        KZeroBot<MessageChain, Bot> bot = create(String.valueOf(event.getBot().getId()), event.getBot(),
                new MiraiBotAdapter(event.getBot(), miraiSerializer), miraiSerializer);
        listener.created(this, bot);
        GsuidClient.INSTANCE.addListener(String.valueOf(event.getBot().getId()), new GsuidMessageListener() {

            @Override
            public void onMessage(MessageOut out) {
                MessageEvent raw = getMessage(out.getMsg_id());
                MessageChainBuilder builder = new MessageChainBuilder();
                builder.append(new QuoteReply(raw.getSource()));
                for (MessageData d0 : out.getContent()) {
                    if (d0.getType().equals("node")) {
                        try {
                            JSONArray array = (JSONArray) d0.getData();
                            for (MessageData d1 : array.toJavaList(MessageData.class)) {
                                builderAppend(builder, d1, event);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else builderAppend(builder, d0, event);
                }
                raw.getSubject().sendMessage(builder.build());
            }

            private void builderAppend(MessageChainBuilder builder, MessageData d0, BotOnlineEvent event) {
                if (d0.getType().equals("text")) {
                    builder.append(new PlainText(d0.getData().toString().trim())).append("\n");
                } else if (d0.getType().equals("image")) {
                    byte[] bytes = Base64.getDecoder().decode(d0.getData().toString().substring("base64://".length()));
                    Image image = Contact.uploadImage(event.getBot().getAsFriend(), new ByteArrayInputStream(bytes));
                    builder.append(image);
                }
            }
        });
    }

    @EventHandler
    public void onMessage(GroupMessageEvent event) {
        KZeroBot<MessageChain, Bot> bot = KZeroMainThreads.BOT_MAP.get(String.valueOf(event.getBot().getId()));
        if (handlerMap.containsKey(bot)) {
            String out = bot.getSerializer().serialize(event.getMessage());
            MessagePack pack = new MessagePack(MessageType.GROUP, String.valueOf(event.getSender().getId()), String.valueOf(event.getSubject().getId()), out);
            pack.setRaw(event);
            handlerMap.get(bot).onMessage(pack);
            offer(event);
            sendToGsuid(event);
        }
    }

    @EventHandler
    public void onMessage(FriendMessageEvent event) {
        offer(event);
        sendToGsuid(event);
    }

    @EventHandler
    public void onMessage(GroupTempMessageEvent event) {
        offer(event);
        sendToGsuid(event);
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
                message.setData(Image.queryUrl(image));
            } else return;
            list.add(message);
        });
        if (list.size() > 0) {
            MessageReceive receive = new MessageReceive();
            receive.setBot_id("mirai-client");
            receive.setBot_self_id(String.valueOf(event.getBot().getId()));
            receive.setUser_id(String.valueOf(event.getSender().getId()));
            receive.setMsg_id(getMessageEventId(event));
            receive.setUser_type("direct");
            receive.setGroup_id("");
            if (event instanceof GroupMessageEvent) {
                receive.setUser_type("group");
                receive.setGroup_id(String.valueOf(event.getSubject().getId()));
            }
            if (event.getSender().getId() == 3474006766L) receive.setUser_pm(0);
            else receive.setUser_pm(2);
            receive.setContent(list.toArray(new MessageData[0]));
            GsuidClient.INSTANCE.send(receive);
        }
    }

    //=============消息记录start
    public static final Integer MAX_E = 50;

    private Deque<MessageEvent> QUEUE = new LinkedList<>();

    private void offer(MessageEvent msg) {
        if (QUEUE.contains(msg)) return;
        if (QUEUE.size() >= MAX_E) QUEUE.pollLast();
        QUEUE.offerFirst(msg);
    }

    private MessageEvent temp0 = null;

    private MessageEvent getMessage(String id) {
        if (temp0 != null && getMessageEventId(temp0).equals(id)) return temp0;
        for (MessageEvent event : QUEUE) {
            if (getMessageEventId(event).equals(id)) return temp0 = event;
        }
        return null;
    }

    private String getMessageEventId(MessageEvent event) {
        if (event.getSource().getIds().length == 0) return "";
        else return String.valueOf(event.getSource().getIds()[0]);
    }
    //=============消息记录end

    @EventHandler
    public void onEvent(MemberLeaveEvent event) {
        KZeroBot<MessageChain, Bot> bot = KZeroMainThreads.BOT_MAP.get(String.valueOf(event.getBot().getId()));
        if (handlerMap.containsKey(bot)) {
            MessagePack pack = new MessagePack(MessageType.GROUP, String.valueOf(event.getMember().getId()),
                    String.valueOf(event.getGroupId()), "MemberLeaveEvent");
            pack.setRaw(event);
            handlerMap.get(bot).onMessage(pack);
        }
    }

    @EventHandler
    public void onEvent(MemberJoinEvent event) {
        KZeroBot<MessageChain, Bot> bot = KZeroMainThreads.BOT_MAP.get(String.valueOf(event.getBot().getId()));
        if (handlerMap.containsKey(bot)) {
            MessagePack pack = new MessagePack(MessageType.GROUP, String.valueOf(event.getMember().getId()),
                    String.valueOf(event.getGroupId()), "MemberJoinEvent");
            pack.setRaw(event);
            handlerMap.get(bot).onMessage(pack);
        }
    }
}
