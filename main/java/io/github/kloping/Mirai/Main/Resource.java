package io.github.kloping.Mirai.Main;

import Entitys.Group;
import Entitys.User;
import Project.DataBases.DataBase;
import Project.DataBases.GameDataBase;
import Project.DataBases.ShopDataBase;
import Project.DataBases.ZongMenDataBase;
import Project.DataBases.skill.SkillDataBase;
import Project.Network.NetWorkMain;
import Project.Plugins.NetMain;
import io.github.kloping.Mirai.Main.Handlers.MyHandler;
import io.github.kloping.Mirai.Main.ITools.MessageTools;
import io.github.kloping.MySpringTool.StarterApplication;
import io.github.kloping.MySpringTool.entity.interfaces.Runner;
import io.github.kloping.MySpringTool.exceptions.NoRunException;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.Message;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.net.URL;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.*;

import static Project.Controllers.GameControllers.GameH2LController.check;

public class Resource {
    public static final ExecutorService threads = Executors.newFixedThreadPool(20);
    public static final ExecutorService DaeThreads = new ThreadPoolExecutor(8, 10, 10, TimeUnit.SECONDS, new ArrayBlockingQueue<>(10));

    public static final String myMame = "0号";

    public static Bot bot;
    public static String superQ = "3474006766";
    public static Long superQL = Long.parseLong(superQ);
    public static String datePath = "";

    private static ABot bot1 = new ABot(291841860, "Han_Boot1");
    private static ABot bot2 = new ABot(392801250, "Han_Boot02");
    private static ABot bot3 = new ABot(930204019, "Han_443212");
    private static ABot bot4 = new ABot(3474006766L, "Heroes_20040");
    private static ABot bot5 = new ABot(2630059874L, "Z_123456");
    private static ABot bot6 = new ABot(3597552450L, "Han_Boot02");
    public static ABot qq = null;

    public static ABot get(int i) {
        switch (i) {
            case 1:
                return (qq = bot1);
            case 2:
                return (qq = bot2);
            case 3:
                return (qq = bot3);
            case 4:
                return (qq = bot4);
            case 5:
                return (qq = bot5);
            case 6:
                return (qq = bot6);

        }
        return null;
    }

    public static void Init() {
        dataBase = new DataBase(datePath);
        gameDataBase = new GameDataBase(datePath);
        zmDataBase = new ZongMenDataBase(datePath);
        shopDataBase = new ShopDataBase(datePath);
        skillDataBase = new SkillDataBase(datePath);
    }

    protected static void startTimer() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (timeIndex++ % 60 == 0) {
                    try {
                        for (File file : new File("./temp").listFiles()) {
                            if (file.isFile())
                                file.delete();
                            else continue;
                            System.out.println("==================删除=>" + file.getName() + "========>");
                        }
                    } catch (Exception e) {
                        System.err.println("==================删除失败========>" + e.getMessage());
                    }
                }
            }
        }, 10 * 1000, 10 * 1000);
    }

    private static int timeIndex = 60;

    protected static void SetterStarterApplication(Class<?> cla) {
        StarterApplication.setMainKey(Long.class);
        StarterApplication.setWaitTime(25 * 1000L);
        StarterApplication.setAccessTypes(Long.class, Entitys.User.class, Group.class, Integer.class);
        // StarterApplication.setAccPars(Long.class, Entitys.User.class, Group.class, Integer.class,MessageChain.class);
        StarterApplication.setAllAfter(new Runner() {
            @Override
            public void run(Object t, Object[] objects) throws NoRunException {
                if (t != null)
                    DaeThreads.submit(() -> onReturnResult(t, objects));
            }
        });
        StarterApplication.setAllBefore(new Runner() {
            @Override
            public void run(Object t, Object[] objects) throws NoRunException {
                onServerAddTimes();
                check(objects);
            }
        });
        StarterApplication.run(cla);
    }

    /*   static String url;
       static String driver;
       static String user;
       static String pwd;

       @Bean
       public SqlSessionFactory b1() {
           PooledDataSource dataSource = new PooledDataSource();
           dataSource.setDriver(driver);
           dataSource.setUsername(user);
           dataSource.setPassword(pwd);
           dataSource.setUrl(url);
           TransactionFactory transactionFactory = new JdbcTransactionFactory();
           Environment environment = new Environment("development", transactionFactory, dataSource);
           Configuration configuration = new Configuration(environment);
           SqlSessionFactory SqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);
           return SqlSessionFactory;
       }
    */

    public static void onReturnResult(Object o, Object[] objects) {
        long v1 = System.currentTimeMillis();
        MessageChainBuilder builder = new MessageChainBuilder();
        net.mamoe.mirai.contact.Group group = bot.getGroup(((Group) objects[4]).getId());
        Integer type = Integer.valueOf(objects[5].toString());
        if (type == 0) {
            if (o == null) return;
            if (o.getClass() == Object[].class) {
                Object[] objs = (Object[]) o;
                MessageTools.sendMessageByForward(group.getId(), objs);
                return;
            }
            if (o.toString().startsWith("&"))
                o = o.toString().replaceFirst("&", "");
            else
                builder.append(new At(Long.valueOf(((User) objects[3]).getId() + ""))).append("\r\n");
            if (o instanceof String) {
                MessageChain message = MessageTools.getMessageFromString(o.toString(), group);
                builder.append(message);
                MessageChain mc = builder.build();
                group.sendMessage(mc);
            } else if (o instanceof Message) {
                group.sendMessage(builder.append((Message) o).build());
            } else {
                System.err.println("未知的返回类型");
            }
        } else {
            builder.append("=======").append("\r\n");
            Contact contact = bot.getGroup(((Group) objects[4]).getId()).get(((Entitys.User) objects[3]).getId());
            if (o == null) return;
            if (o instanceof String) {
                contact.sendMessage(builder.append(o.toString().trim()).build());
            } else if (o instanceof Message) {
                contact.sendMessage(builder.append((Message) o).build());
            } else {
                System.err.println("未知的返回类型");
            }
        }
        MyHandler.upMessage = null;
    }

    public static final synchronized void onServerAddTimes() {
        DaeThreads.execute(runnableBefore);
    }

    public static final void SetOnErrInFIle(String path) {
        try {
            PrintStream oldPrintStream = System.err;
            new File(path).getParentFile().mkdirs();
            new File(path).createNewFile();
            FileOutputStream bos = new FileOutputStream(path, true);
            PrintStream printStream = new PrintStream(bos) {
                @Override
                public void write(int b) {
                    super.write(b);
                    oldPrintStream.write((int) b);
                }

                @Override
                public void write(byte[] buf, int off, int len) {
                    super.write(buf, off, len);
                    oldPrintStream.write(buf, off, len);
                }
            };
            System.setErr(printStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static final void SetOnOutInFIle(String path) {
        try {
            PrintStream oldPrintStream = System.out;
            new File(path).getParentFile().mkdirs();
            new File(path).createNewFile();
            FileOutputStream bos = new FileOutputStream(path, true);
            PrintStream printStream = new PrintStream(bos) {
                @Override
                public void write(int b) {
                    super.write(b);
                    oldPrintStream.write((int) b);
                }

                @Override
                public void write(byte[] buf, int off, int len) {
                    super.write(buf, off, len);
                    oldPrintStream.write(buf, off, len);
                }
            };
            System.setOut(printStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static final Runnable runnableBefore = new Runnable() {
        @Override
        public void run() {
            try {
                URL url = new URL(NetMain.rootPath + "/abo?id=" + bot.getId() + "&key=hrskloping");
                url.openStream();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
    public static final List<Runnable> StartOkRuns = new CopyOnWriteArrayList<>();

    protected static void StarterOk() {
        threads.execute(new Runnable() {
            @Override
            public void run() {
                for (Runnable runnable : StartOkRuns) {
                    try {
                        runnable.run();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Resource.bot.getFriend(superQL).sendMessage(e.getMessage());
                    }
                }
            }
        });
    }

    protected static void StarterOk(boolean k) {
        threads.execute(new Runnable() {
            @Override
            public void run() {
                for (Runnable runnable : StartOkRuns) {
                    try {
                        runnable.run();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Resource.bot.getFriend(superQL).sendMessage(e.getMessage());
                    }
                }
                if (!k) return;
                NetWorkMain.start();
            }
        });
    }


    //=======================
    public static DataBase dataBase = null;
    public static GameDataBase gameDataBase = null;
    public static ZongMenDataBase zmDataBase = null;
    public static ShopDataBase shopDataBase = null;
    public static SkillDataBase skillDataBase = null;
    //=================================
//    public static final IScoreService scoreService = new ScoreServiceImpl();
//    public static final IOtherService otherService = new OtherServiceImpl();
//    public static final IManagerService managerService = new ManagerServiceImpl();
//    ===========================================
//    public static final IGameService gameService = new GameServiceImpl();
//    public static final IGameWeaService gameWeaService = new GameWeaServiceImpl();
//    public static final IGameBoneService gameBoneService = new GameBoneServiceImpl();
//    public static final IGameJoinAcService gameJoinAcService = new GameJoinAcServiceImpl();
//    public static final IGameUseObjService gameUseObiService = new GameUseObjServiceImpl();
//    public static final IZongMenService zongMenService = new ZongMenServiceImpl();
//    public static final IShoperService shoperService = new ShoperServiceImpl();
//    public static final ISkillServer skillService = new GameSkillServiceImpl();
//    public static final GameDetailService gameDetailService = new GameDetailService();
//    public static final GameJoinDetailService gameJoinDetailService = new GameJoinDetailService();
//    public static final GameWeaDetailService gameWeaDetailService = new GameWeaDetailService();
//    public static final ZongDetailService zongDetailService = new ZongDetailService();

    //=======================
    public static void println(String line) {
        System.out.println("==========================" + line + "===================================");
    }

    public static void println(String line, boolean error) {
        System.err.println("==========================" + line + "===================================");
    }

    //================
    public static class Switch {
        public static boolean AllK = true;
        public static boolean isTalk = true;
        public static boolean isWelcome = true;
        public static boolean sendFlashToSuper = true;
    }

    public static final class ABot {
        private long qq;
        private String passWord;

        public ABot(long qq, String passWord) {
            this.qq = qq;
            this.passWord = passWord;
        }

        public long getQq() {
            return qq;
        }

        public String getPassWord() {
            return passWord;
        }
    }
}


