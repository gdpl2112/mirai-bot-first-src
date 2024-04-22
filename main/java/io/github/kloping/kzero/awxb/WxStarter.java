package io.github.kloping.kzero.awxb;

import io.github.gdpl2112.onebot.v12.ListenerHost;
import io.github.gdpl2112.onebot.v12.WebChatClientWithOneBotV12;
import io.github.gdpl2112.onebot.v12.contact.Bot;
import io.github.gdpl2112.onebot.v12.data.MessageChain;
import io.github.gdpl2112.onebot.v12.event.EventReceiver;
import io.github.gdpl2112.onebot.v12.event.GroupMessageEvent;
import io.github.gdpl2112.onebot.v12.event.MetaEvent;
import io.github.kloping.MySpringTool.StarterObjectApplication;
import io.github.kloping.kzero.awxb.exclusive.Wx2Gsuid;
import io.github.kloping.kzero.awxb.exclusive.Wx2Mihdp;
import io.github.kloping.kzero.gsuid.GsuidClient;
import io.github.kloping.kzero.main.KlopZeroMainThreads;
import io.github.kloping.kzero.main.api.*;
import io.github.kloping.kzero.mihdp.MihdpClient;

import java.lang.reflect.Field;

/**
 * @author github.kloping
 */
public class WxStarter extends ListenerHost implements KZeroStater {
    private BotCreated listener;
    private BotMessageHandler handler;

    @Override
    public void setHandler(KZeroBot bot, BotMessageHandler handler) {
        if (bot.getSelf() instanceof MetaEvent)
            this.handler = handler;
    }

    @Override
    public void setCreated(BotCreated listener) {
        this.listener = listener;
    }

    @Override
    public void run() {
        WebChatClientWithOneBotV12 botV12 = new WebChatClientWithOneBotV12();
        botV12.setConfFile("./conf/conf.txt");
        WebChatClientWithOneBotV12.registerListenerHost(this);
        WebChatClientWithOneBotV12.registerListenerHost(Wx2Mihdp.INSTANCE);
        WebChatClientWithOneBotV12.registerListenerHost(Wx2Gsuid.INSTANCE);
        botV12.start();
        try {
            Field field = botV12.getClass().getDeclaredField("application");
            field.setAccessible(true);
            StarterObjectApplication application = (StarterObjectApplication) field.get(botV12);
            application.logger.setPrefix("[wxbot]");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handleException(Throwable e) {

    }

    public static final String STATUS_UPDATE = "status_update";

    @EventReceiver
    public void onEvent(MetaEvent metaEvent) {
        if (STATUS_UPDATE.equals(metaEvent.getDetailType())) {
            Bot bot = metaEvent.getStatus().getBots()[0];
            String bid = String.valueOf(bot.getSelf().getUserId());
            if (KlopZeroMainThreads.BOT_MAP.containsKey(bid)) {
                KlopZeroMainThreads.BOT_MAP.get(bid).setSelf(metaEvent);
                return;
            }
            System.out.format("==================%s(%s)-上线了=====================\n", bot.getSelf().getUserId(), bot.getSelf().getPlatform());
            WxSerializer serializer = new WxSerializer(metaEvent);
            KZeroBot<MessageChain, MetaEvent> kbot = create(bid, metaEvent, new WxAdapter(metaEvent, serializer), serializer);
            listener.created(this, kbot);
            if (MihdpClient.INSTANCE != null)
                MihdpClient.INSTANCE.listeners.put(kbot.getId(), Wx2Mihdp.INSTANCE);
            if (GsuidClient.INSTANCE != null)
                GsuidClient.INSTANCE.addListener(kbot.getId(), Wx2Gsuid.INSTANCE);
        }
    }

    private KZeroBot<MessageChain, MetaEvent> create(String s, MetaEvent event, WxAdapter adapter, WxSerializer serializer) {
        return new KZeroBot<MessageChain, MetaEvent>() {
            @Override
            public String getId() {
                return s;
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
            public MetaEvent getSelf() {
                return event;
            }
        };
    }


    @EventReceiver
    public void onEvent(GroupMessageEvent event) {
        MessageChain chain = event.getMessage();
//        Guild2Gsuid.INSTANCE.offer(event);
        if (handler != null) {
            KZeroBot<MessageChain, MetaEvent> kZeroBot = KlopZeroMainThreads.BOT_MAP.get(String.valueOf(event.getSelf().getUserId()));
            String outMsg = kZeroBot.getSerializer().serialize(chain);
            MessagePack pack = new MessagePack(MessageType.GROUP, event.getSender().getUserId(), event.getGroup().getGroupId(), outMsg);
            pack.setRaw(event);
            handler.onMessage(pack);
            //plugin to gsuid
//            Guild2Gsuid.INSTANCE.sendToGsuid(event);
//            MihdpConnect2.INSTANCE.sendToMihdp(null, event, kZeroBot);
        }
//        temp(event);
    }
}
