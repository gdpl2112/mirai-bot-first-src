package io.github.kloping.mirai0.Main;

import Project.dataBases.*;
import Project.dataBases.skill.SkillDataBase;
import Project.interfaces.http_api.Kloping;
import io.github.kloping.MySpringTool.StarterApplication;
import io.github.kloping.MySpringTool.entity.interfaces.Runner;
import io.github.kloping.MySpringTool.exceptions.NoRunException;
import io.github.kloping.MySpringTool.interfaces.component.ContextManager;
import io.github.kloping.initialize.FileInitializeValue;
import io.github.kloping.mirai0.Main.ITools.MessageTools;
import io.github.kloping.mirai0.commons.Group;
import io.github.kloping.mirai0.commons.User;
import io.github.kloping.object.ObjectUtils;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.console.terminal.MiraiConsoleImplementationTerminal;
import net.mamoe.mirai.console.terminal.MiraiConsoleTerminalLoader;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.message.data.Message;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;

import java.io.File;
import java.io.PrintStream;
import java.net.URL;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;

import static Project.controllers.auto.TimerController.ZERO_RUNS;
import static io.github.kloping.mirai0.Main.BotStarter.test;
import static io.github.kloping.mirai0.Main.ITools.MessageTools.getAt;
import static io.github.kloping.mirai0.Main.Parse.parseToLongList;

/**
 * @author github-kloping
 */
public class Resource {
    public static final ExecutorService THREADS =
            Executors.newFixedThreadPool(20);
    public static final ExecutorService DEA_THREADS =
            new ThreadPoolExecutor(8, 10, 10, TimeUnit.SECONDS, new ArrayBlockingQueue<>(10));
    public static final List<Runnable> START_AFTER = new CopyOnWriteArrayList<>();
    public static String MY_MAME;

    public static Bot BOT;

    public static Set<Long> superQL = new HashSet<>();
    public static String datePath = "";
    public static Bots bots = FileInitializeValue.getValue("./conf/bots.conf.json", new Bots());
    public static BotConf qq = null;
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
        if (!Boolean.valueOf(StarterApplication.Setting.INSTANCE.getContextManager().getContextEntity(Kloping.class).v0(code)))
            throw new RuntimeException("授权码过期或不可用(Authorization code expired or unavailable)");
        else {
            StarterApplication.logger.info("授权码验证成功√√√");
        }
    }

    public static boolean isSuperQ(long q) {
        return superQL.contains(q);
    }

    public static void pluginLoad() {
        PrintStream out = System.out;
        PrintStream err = System.err;

        MiraiConsoleTerminalLoader.INSTANCE.startAsDaemon(
                new MiraiConsoleImplementationTerminal(
                        Paths.get("./console")
                )
        );

        System.setOut(out);
        System.setErr(err);
    }

    public static BotConf get(int i) {
        switch (i) {
            case 1:
                return (qq = bots.getBots().get(0));
            case 2:
                return (qq = bots.getBots().get(1));
            case 3:
                return (qq = bots.getBots().get(2));
            case 4:
                return (qq = bots.getBots().get(3));
            case 5:
                return (qq = bots.getBots().get(4));
            case 6:
                return (qq = bots.getBots().get(5));
            default:
                return null;
        }
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
        StarterApplication.setAccessTypes(Long.class, io.github.kloping.mirai0.commons.User.class, Group.class, Integer.class);
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
        contextManager = StarterApplication.Setting.INSTANCE.getContextManager();
        StarterApplication.run(cla);
        //load conf
        superQL.addAll(parseToLongList(contextManager.getContextEntity(String.class, "superQL")));
        StarterApplication.logger.info("superQL=>" + superQL);
        MY_MAME = contextManager.getContextEntity(String.class, "bot.myName");
    }

    public static void onReturnResult(Object o, Object[] objects) {
        MessageChainBuilder builder = new MessageChainBuilder();
        Integer type = Integer.valueOf(objects[5].toString());
        if (type == 0) {
            net.mamoe.mirai.contact.Group group = BOT.getGroup(((Group) objects[4]).getId());
            if (o == null) {
                return;
            }
            //====
            if (o.getClass() == Object[].class) {
                Object[] objs = (Object[]) o;
                MessageTools.sendMessageByForward(group.getId(), objs);
                return;
            } else if (o instanceof Message) {
                group.sendMessage((Message) o);
                return;
            }
            //====
            if (o.toString().startsWith("&")) {
                o = o.toString().replaceFirst("&", "");
            } else {
                builder.append(getAt(((User) objects[3]).getId())).append("\r\n");
            }
            //====
            if (o instanceof String) {
                MessageChain message = MessageTools.getMessageFromString(o.toString(), group);
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
            Contact contact = BOT.getGroup(((Group) objects[4]).getId()).get(((io.github.kloping.mirai0.commons.User) objects[3]).getId());
            if (o == null) {
                return;
            }
            if (o.getClass() == Object[].class) {
                Object[] objs = (Object[]) o;
                MessageTools.sendMessageByForward(contact.getId(), objs);
                return;
            } else if (o instanceof Message) {
                contact.sendMessage((Message) o);
                return;
            }
            if (o instanceof String) {
                MessageChain message = MessageTools.getMessageFromString(o.toString(), contact);
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
        THREADS.execute(new Runnable() {
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

    public static final class BotConf {
        private long qq;
        private String passWord;

        public BotConf(long qq, String passWord) {
            this.qq = qq;
            this.passWord = passWord;
        }

        public long getQq() {
            return qq;
        }

        public void setQq(long qq) {
            this.qq = qq;
        }

        public String getPassWord() {
            return passWord;
        }

        public void setPassWord(String passWord) {
            this.passWord = passWord;
        }
    }

    private static final class Bots {
        List<BotConf> bots = new LinkedList<>();

        public List<BotConf> getBots() {
            return bots;
        }

        public void setBots(List<BotConf> bots) {
            this.bots = bots;
        }
    }
}


