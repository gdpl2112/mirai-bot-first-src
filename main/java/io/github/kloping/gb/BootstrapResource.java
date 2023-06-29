package io.github.kloping.gb;

import io.github.kloping.MySpringTool.StarterObjectApplication;
import io.github.kloping.MySpringTool.annotations.CommentScan;
import io.github.kloping.MySpringTool.entity.interfaces.Runner;
import io.github.kloping.MySpringTool.exceptions.NoRunException;
import io.github.kloping.arr.Class2OMap;
import io.github.kloping.gb.spring.MySpringApplication;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author github.kloping
 */
public class BootstrapResource implements Runnable {
    public static final ExecutorService THREADS = Executors.newFixedThreadPool(20);

    public static final BootstrapResource INSTANCE = new BootstrapResource();

    public StarterObjectApplication application;
    public GameStarter starter;

    public BootstrapResource() {
        THREADS.submit(this);
    }

    @Override
    public void run() {
        MySpringApplication.main(new String[]{});
        starter = new GameStarter() {
            @Override
            public void handler(@NotNull BotInterface bot, @NotNull MessageContext context) {
                StringBuilder sb = new StringBuilder();
                for (MessageData datum : context.getMsgs()) {
                    if (datum instanceof DataText) {
                        DataText text = (DataText) datum;
                        sb.append(text.getText().trim());
                    } else if (datum instanceof DataImage) {
                        sb.append("[图片]");
                    } else if (datum instanceof DataAt) {
                        DataAt at = (DataAt) datum;
                        sb.append(String.format("<@%s>", at.getId()));
                    }
                }
                application.executeMethod(context.getSid(), sb.toString(), context, bot, context.getSid());
            }
        };
        application = new StarterObjectApplication(BootstrapResource.class);
        application.setMainKey(String.class);
        application.setWaitTime(30 * 1000L);
        application.setAccessTypes(MessageContext.class, BotInterface.class, String.class);
        application.addConfFile("./conf/conf.txt");
        // 将 springboot bean 加入
        try {
            Class<?>[] classes = application.INSTANCE.getPackageScanner()
                    .scan(BootstrapResource.class, BootstrapResource.class.getClassLoader(),
                            "io.github.kloping.gb.spring.mapper");
            for (Class<?> aClass : classes) {
                Object bean = MySpringApplication.configuration.getBean(aClass);
                if (bean != null) {
                    application.INSTANCE.getContextManager().append(bean);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        application.setAllAfter(new Runner() {
            @Override
            public void run(Object t, Object[] objects) throws NoRunException {
                if (t != null) {
                    THREADS.submit(() -> {
                        MessageContext context = (MessageContext) objects[2];
                        BotInterface bot = (BotInterface) objects[3];
                        int r = bot.onReturn(context, objects[1].toString(), t);
                    });
                }
            }
        });
        application.run0(ApplicationStarterClass.class);
    }

    public void info(String line) {
        System.out.println("\033[32m" + line + "\033[m");
    }

    @CommentScan(path = "io.github.kloping.gb")
    public static class ApplicationStarterClass {
    }
}
