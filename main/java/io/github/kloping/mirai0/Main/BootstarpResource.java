package io.github.kloping.mirai0.Main;

import Project.dataBases.*;
import Project.dataBases.skill.SkillDataBase;
import Project.interfaces.httpApi.KlopingWeb;
import Project.listeners.LittleHandler;
import Project.listeners.NbListener;
import Project.listeners.NoGroupHandler;
import Project.listeners.SaveHandler;
import io.github.kloping.MySpringTool.StarterApplication;
import io.github.kloping.MySpringTool.entity.interfaces.Runner;
import io.github.kloping.MySpringTool.exceptions.NoRunException;
import io.github.kloping.MySpringTool.interfaces.component.ContextManager;
import io.github.kloping.mirai0.Main.iutils.MessageUtils;
import io.github.kloping.mirai0.commons.SpGroup;
import io.github.kloping.mirai0.commons.SpUser;
import io.github.kloping.object.ObjectUtils;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.event.GlobalEventChannel;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.Message;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;

import static Project.controllers.auto.TimerController.ZERO_RUNS;
import static io.github.kloping.mirai0.Main.BotStarter.test;
import static io.github.kloping.mirai0.Main.Parse.parseToLongList;

/**
 * @author github-kloping
 */
public class BootstarpResource {
    public static final ExecutorService THREADS = Executors.newFixedThreadPool(20);
    public static final ExecutorService DEA_THREADS = new ThreadPoolExecutor(8, 10, 10, TimeUnit.SECONDS, new ArrayBlockingQueue<>(10));
    public static final List<Runnable> START_AFTER = new CopyOnWriteArrayList<>();
    public static String MY_MAME;

    public static Bot BOT;
    public static Set<Long> superQL = new HashSet<>();
    public static String datePath = "";
    public static DataBase dataBase = null;
    public static GameDataBase gameDataBase = null;
    public static ZongMenDataBase zmDataBase = null;
    public static ShopDataBase shopDataBase = null;
    public static SkillDataBase skillDataBase = null;
    public static GameTaskDatabase gameTaskDatabase = null;
    public static OtherDatabase otherDatabase = null;
    public static ContextManager contextManager;

    static {
        ZERO_RUNS.add(() -> {
            for (File file : new File("./temp").listFiles()) {
                if (file.isFile()) {
                    file.delete();
                } else {
                    continue;
                }
                System.out.println("==================删除=>" + file.getName() + "========>");
            }
        });
    }

    public static void verify() throws RuntimeException {
        String code = StarterApplication.Setting.INSTANCE.getContextManager().getContextEntity(String.class, "auth_code");
        if (code == null) throw new RuntimeException("没有配置授权码(Authorization not configured) auth_code");
        KlopingWeb kloping = StarterApplication.Setting.INSTANCE.getContextManager().getContextEntity(KlopingWeb.class);
        String r0 = kloping.verify0(code);
        if (!Boolean.valueOf(r0))
            throw new RuntimeException("授权码过期或不可用(Authorization code expired or unavailable)");
        else StarterApplication.logger.info("授权码验证成功√√√");
    }

    public static boolean isSuperQ(long q) {
        return superQL.contains(q);
    }

    public static void init() {
        dataBase = new DataBase(datePath);
        gameDataBase = new GameDataBase(datePath);
        zmDataBase = new ZongMenDataBase(datePath);
        shopDataBase = new ShopDataBase(datePath);
        skillDataBase = new SkillDataBase(datePath);
        gameTaskDatabase = new GameTaskDatabase(datePath);
        otherDatabase = new OtherDatabase(datePath);
    }

    protected static void setterStarterApplication(Class<?> cla) {
        StarterApplication.setMainKey(Long.class);
        StarterApplication.setWaitTime(test ? 600000L : 30 * 1000L);
        StarterApplication.setAccessTypes(Long.class, SpUser.class, SpGroup.class, Integer.class, MessageEvent.class);
        StarterApplication.setAllAfter(new Runner() {
            @Override
            public void run(Object t, Object[] objects) throws NoRunException {
                if (t != null) {
                    DEA_THREADS.submit(() -> onReturnResult(t, objects));
                }
            }
        });
        StarterApplication.setAllBefore(new Runner() {
            @Override
            public void run(Object t, Object[] objects) throws NoRunException {
            }
        });
        StarterApplication.addConfFile("./conf/conf.txt");
        StarterApplication.run(cla);
        contextManager = StarterApplication.Setting.INSTANCE.getContextManager();
        //load conf
        superQL.addAll(parseToLongList(contextManager.getContextEntity(String.class, "superQL")));
        StarterApplication.logger.info("superQL=>" + superQL);
        MY_MAME = contextManager.getContextEntity(String.class, "bot.myName");
        StarterApplication.STARTED_RUNNABLE.add(() -> verify());
    }

    public static void onReturnResult(Object o, Object[] objects) {
        MessageChainBuilder builder = new MessageChainBuilder();
        Integer type = Integer.valueOf(objects[5].toString());
        if (type == 0) {
            net.mamoe.mirai.contact.Group group = BOT.getGroup(((SpGroup) objects[4]).getId());
            if (o == null) {
                return;
            }
            //====
            if (o.getClass() == Object[].class) {
                Object[] objs = (Object[]) o;
                MessageUtils.INSTANCE.sendMessageByForward(group.getId(), objs);
                return;
            } else if (o instanceof Message) {
                group.sendMessage((Message) o);
                return;
            }
            //====
            if (o.toString().startsWith("&")) {
                o = o.toString().replaceFirst("&", "");
            } else {
                builder.append(MessageUtils.INSTANCE.getAt(((SpUser) objects[3]).getId())).append("\r\n");
            }
            //====
            if (o instanceof String) {
                MessageChain message = MessageUtils.INSTANCE.getMessageFromString(o.toString(), group);
                builder.append(message);
                MessageChain mc = builder.build();
                group.sendMessage(mc);
            } else if (o instanceof Message) {
                group.sendMessage(builder.append((Message) o).build());
            } else if (ObjectUtils.isBaseOrPack(o.getClass())) {
                group.sendMessage(o.toString());
            } else {
                System.err.println("未知的返回类型");
            }
            //====
        } else {
            Contact contact = BOT.getGroup(((SpGroup) objects[4]).getId()).get(((SpUser) objects[3]).getId());
            if (o == null) {
                return;
            }
            if (o.getClass() == Object[].class) {
                Object[] objs = (Object[]) o;
                MessageUtils.INSTANCE.sendMessageByForward(contact.getId(), objs);
                return;
            } else if (o instanceof Message) {
                contact.sendMessage((Message) o);
                return;
            }
            if (o instanceof String) {
                MessageChain message = MessageUtils.INSTANCE.getMessageFromString(o.toString(), contact);
                builder.append(message);
                MessageChain mc = builder.build();
                contact.sendMessage(mc);
            } else if (o instanceof Message) {
                contact.sendMessage(builder.append((Message) o).build());
            } else {
                System.err.println("未知的返回类型");
            }
        }
    }

    public static void startedAfter() {
        THREADS.submit(new Runnable() {
            @Override
            public void run() {
                for (Runnable runnable : START_AFTER) {
                    try {
                        runnable.run();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public static void println(String line) {
        System.out.println("==========================" + line + "===================================");
    }

    public static class Switch {
        public static boolean AllK = true;
        public static boolean sendFlashToSuper = true;
    }

    public static void startRegisterListenerHost(String[] args) {
        GlobalEventChannel.INSTANCE.registerListenerHost(LittleHandler.contextManager.getContextEntity(LittleHandler.class));
        GlobalEventChannel.INSTANCE.registerListenerHost(new SaveHandler(args));
        GlobalEventChannel.INSTANCE.registerListenerHost(new NoGroupHandler());
        StarterApplication.STARTED_RUNNABLE.add(() -> {
            GlobalEventChannel.INSTANCE.registerListenerHost(StarterApplication.Setting.INSTANCE.getContextManager().getContextEntity(NbListener.class));
        });
    }
}


