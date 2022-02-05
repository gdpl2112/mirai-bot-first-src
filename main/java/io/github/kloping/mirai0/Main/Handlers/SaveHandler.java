package io.github.kloping.mirai0.Main.Handlers;

import kotlin.coroutines.CoroutineContext;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.FriendMessageEvent;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.event.events.StrangerMessageEvent;
import net.mamoe.mirai.message.data.MessageSource;
import net.mamoe.mirai.message.data.MessageSourceBuilder;
import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 * @author github-kloping
 * @version 1.0
 */
public class SaveHandler extends SimpleListenerHost {
    private static File baseFile = new File("./message/data");
    private File groupDataFile = new File(baseFile.getAbsolutePath(), "group");
    private File friendDataFile = new File(baseFile.getAbsolutePath(), "friend");
    private File strangerDataFile = new File(baseFile.getAbsolutePath(), "stranger");

    public SaveHandler() {
        baseFile.mkdirs();
        groupDataFile.mkdirs();
        friendDataFile.mkdirs();
        strangerDataFile.mkdirs();
    }

    @Override
    public void handleException(@NotNull CoroutineContext context, @NotNull Throwable exception) {
        exception.printStackTrace();
    }

    @EventHandler
    public void onMessage(@NotNull GroupMessageEvent event) throws Exception {
    }

    @EventHandler
    public void onMessage(@NotNull FriendMessageEvent event) throws Exception {

    }

    @EventHandler
    public void onMessage(@NotNull StrangerMessageEvent event) throws Exception {

    }

}
