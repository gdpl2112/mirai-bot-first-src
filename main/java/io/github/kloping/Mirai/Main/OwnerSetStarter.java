package io.github.kloping.Mirai.Main;

import io.github.kloping.Mirai.Main.Handlers.LittleHandler;
import io.github.kloping.MySpringTool.StarterApplication;
import io.github.kloping.MySpringTool.h1.impl.ConfigFileManagerImpl;
import io.github.kloping.MySpringTool.h1.impl.LoggerImpl;
import io.github.kloping.MySpringTool.interfaces.component.*;
import io.github.kloping.MySpringTool.interfaces.entitys.MatherResult;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.BotFactory;
import net.mamoe.mirai.utils.BotConfiguration;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Scanner;

import Project.Tools.Tool;
import io.github.kloping.Mirai.Main.ITools.EventTools;
import io.github.kloping.Mirai.Main.ITools.Saver;
import io.github.kloping.Mirai.Main.Resource;
import io.github.kloping.MySpringTool.annotations.Action;
import io.github.kloping.MySpringTool.annotations.Controller;
import io.github.kloping.MySpringTool.h1.impl.AutomaticWiringParamsImpl;
import io.github.kloping.MySpringTool.h1.impl.InstanceCraterImpl;
import io.github.kloping.MySpringTool.h1.impl.component.ActionManagerImpl;
import io.github.kloping.MySpringTool.h1.impl.component.ClassManagerImpl;
import io.github.kloping.MySpringTool.h1.impl.component.ContextManagerImpl;
import io.github.kloping.MySpringTool.h1.impl.component.MethodManagerImpl;
import io.github.kloping.MySpringTool.interfaces.entitys.MatherResult;
import io.github.kloping.arr.Class2OMap;
import kotlin.coroutines.CoroutineContext;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.AnonymousMember;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.MemberPermission;
import net.mamoe.mirai.contact.NormalMember;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.FriendMessageEvent;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.event.events.GroupMessageSyncEvent;
import net.mamoe.mirai.message.data.*;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import static io.github.kloping.Mirai.Main.ITools.Saver.saveMessage;
import static io.github.kloping.MySpringTool.StarterApplication.Setting.INSTANCE;

public class OwnerSetStarter  {

    public static void main(String[] args) {
        BotConfiguration botConfiguration = new BotConfiguration();
        botConfiguration.setProtocol(BotConfiguration.MiraiProtocol.ANDROID_PAD);
        botConfiguration.setHeartbeatStrategy(BotConfiguration.HeartbeatStrategy.NONE);
        botConfiguration.setCacheDir(new File("./cache"));
        botConfiguration.fileBasedDeviceInfo("./devices/device2.json");
        Resource.ABot abot = Resource.get(4);
        Bot bot = BotFactory.INSTANCE.newBot(abot.getQq(), abot.getPassWord(), botConfiguration);
        bot.login();
        LittleHandler.contextManager.append(Bot.class, bot, "0");
        StarterApplication.logger = new LoggerImpl();
        LittleHandler.init();
        ConfigFileManager configFileManager = new ConfigFileManagerImpl(LittleHandler.contextManager);
        configFileManager.load("./conf/conf.txt");
        LittleHandler handler = LittleHandler.contextManager.getContextEntity(LittleHandler.class);
        bot.getEventChannel().registerListenerHost(handler);

//        Scanner sc = new Scanner(System.in);
//        String line = null;
//        while ((line = sc.nextLine()) != null) {
//            try {
//                // sendAllGroup ?
//                MatherResult result = LittleHandler.actionManager.mather(line);
//                if (result != null) {
//                    for (Method method : result.getMethods()) {
//                        try {
//                            method.invoke(handler, line);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
    }
}
