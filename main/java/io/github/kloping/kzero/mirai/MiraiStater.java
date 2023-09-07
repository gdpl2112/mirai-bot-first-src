package io.github.kloping.kzero.mirai;

import io.github.kloping.common.Public;
import io.github.kloping.kzero.main.KZeroMainThreads;
import io.github.kloping.kzero.main.api.*;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.console.terminal.MiraiConsoleImplementationTerminal;
import net.mamoe.mirai.console.terminal.MiraiConsoleTerminalLoader;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.GlobalEventChannel;
import net.mamoe.mirai.event.ListenerHost;
import net.mamoe.mirai.event.events.BotOnlineEvent;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.MessageChain;

import java.nio.file.Paths;

/**
 * @author github.kloping
 */
public class MiraiStater implements KZeroStater, ListenerHost {
    private BotCreated listener;
    private BotMessageHandler handler;

    @Override
    public void setHandler(BotMessageHandler handler) {
        this.handler = handler;
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
        if (listener != null) {
            MiraiSerializer miraiSerializer = new MiraiSerializer(event.getBot());
            KZeroBot<MessageChain, Bot> bot = create(String.valueOf(event.getBot().getId()), event.getBot(),
                    new MiraiBotAdapter(event.getBot(), miraiSerializer), miraiSerializer);
            listener.created(this, bot);
        }
    }

    @EventHandler
    public void onMessage(GroupMessageEvent event) {
        if (handler != null) {
            KZeroBot<MessageChain, Bot> bot = KZeroMainThreads.BOT_MAP.get(String.valueOf(event.getBot().getId()));
            String out = bot.getSerializer().serialize(event.getMessage());
            MessagePack pack = new MessagePack(MessageType.GROUP, String.valueOf(event.getSender().getId()), String.valueOf(event.getSubject().getId()), out);
            pack.setRaw(event);
            handler.onMessage(pack);
        }
    }
}
