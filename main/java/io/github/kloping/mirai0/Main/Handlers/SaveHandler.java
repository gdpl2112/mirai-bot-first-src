package io.github.kloping.mirai0.Main.Handlers;

import Project.aSpring.SaverSpringStarter;
import kotlin.coroutines.CoroutineContext;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.*;
import net.mamoe.mirai.internal.network.protocol.packet.chat.receive.MessageSvcPbSendMsg;
import net.mamoe.mirai.message.code.MiraiCode;
import net.mamoe.mirai.message.data.MessageChain;
import org.jetbrains.annotations.NotNull;

/**
 * @author github-kloping
 * @version 1.0
 */
public class SaveHandler extends SimpleListenerHost {

    public SaveHandler(String[] args) {
        SaverSpringStarter.main(args);
    }

    @Override
    public void handleException(@NotNull CoroutineContext context, @NotNull Throwable exception) {
        exception.printStackTrace();
    }

    @EventHandler
    public void onMessage(@NotNull GroupMessageEvent event) throws Exception {
        AllMessage.factory(event).save();
    }

    @EventHandler
    public void onMessage(@NotNull MessagePostSendEvent event) throws Exception {
        AllMessage.factory(event).save();
    }

    @EventHandler
    public void onMessage(@NotNull GroupMessageSyncEvent event) throws Exception {
        AllMessage.factory(event).save();
    }

    @EventHandler
    public void onMessage(@NotNull FriendMessageEvent event) throws Exception {
        AllMessage.factory(event).save();
    }

    @EventHandler
    public void onMessage(@NotNull FriendMessageSyncEvent event) throws Exception {
        AllMessage.factory(event).save();
    }

    @EventHandler
    public void onMessage(@NotNull StrangerMessageEvent event) throws Exception {
        AllMessage.factory(event).save();
    }

    @EventHandler
    public void onMessage(@NotNull StrangerMessageSyncEvent event) throws Exception {
        AllMessage.factory(event).save();
    }

}
