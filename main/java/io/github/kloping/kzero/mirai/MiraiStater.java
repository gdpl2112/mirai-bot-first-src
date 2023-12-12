package io.github.kloping.kzero.mirai;

import io.github.kloping.common.Public;
import io.github.kloping.kzero.main.KZeroMainThreads;
import io.github.kloping.kzero.main.api.*;
import io.github.kloping.kzero.mirai.exclusive.CustomizeController;
import io.github.kloping.kzero.mirai.listeners.GenshinUidConnect;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.console.terminal.MiraiConsoleImplementationTerminal;
import net.mamoe.mirai.console.terminal.MiraiConsoleTerminalLoader;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.GlobalEventChannel;
import net.mamoe.mirai.event.ListenerHost;
import net.mamoe.mirai.event.events.BotOnlineEvent;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.event.events.MemberJoinEvent;
import net.mamoe.mirai.event.events.MemberLeaveEvent;
import net.mamoe.mirai.message.data.Message;
import net.mamoe.mirai.message.data.MessageChain;

import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

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
        GlobalEventChannel.INSTANCE.registerListenerHost(new GenshinUidConnect());
    }

    public KZeroBot<Message , Bot> create(String bid, Bot o, KZeroBotAdapter adapter, MessageSerializer<Message > serializer) {
        KZeroBot<Message , Bot> bot = new KZeroBot<Message , Bot>() {
            @Override
            public String getId() {
                return bid;
            }

            @Override
            public KZeroBotAdapter getAdapter() {
                return adapter;
            }

            @Override
            public MessageSerializer<Message > getSerializer() {
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
        MiraiSerializer serializer = new MiraiSerializer(event.getBot());
        KZeroBot<Message, Bot> bot = create(String.valueOf(event.getBot().getId()), event.getBot(),
                new MiraiBotAdapter(event.getBot(), serializer), serializer);
        event.getBot().getEventChannel().registerListenerHost(new CustomizeController(serializer));
        listener.created(this, bot);
    }

    @EventHandler
    public void onMessage(GroupMessageEvent event) {
        KZeroBot<MessageChain, Bot> bot = KZeroMainThreads.BOT_MAP.get(String.valueOf(event.getBot().getId()));
        if (handlerMap.containsKey(bot)) {
            String out = bot.getSerializer().serialize(event.getMessage());
            MessagePack pack = new MessagePack(MessageType.GROUP, String.valueOf(event.getSender().getId()), String.valueOf(event.getSubject().getId()), out);
            pack.setRaw(event);
            handlerMap.get(bot).onMessage(pack);
        }
    }

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
