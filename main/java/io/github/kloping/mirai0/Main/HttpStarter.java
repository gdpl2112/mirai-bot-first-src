package io.github.kloping.mirai0.Main;

import Project.aSpring.SpringStarter;
import com.github.kloping.qbot.myqq.MyBotApplication;
import com.github.kloping.qbot.myqq.v1.entity.Message;
import com.github.kloping.qbot.myqq.v1.sources.StaticSource;
import io.github.kloping.MySpringTool.StarterApplication;
import io.github.kloping.MySpringTool.entity.interfaces.Runner;
import io.github.kloping.MySpringTool.exceptions.NoRunException;
import io.github.kloping.mirai0.Main.Handlers.MyHandler;
import io.github.kloping.mirai0.commons.Group;
import io.github.kloping.mirai0.commons.User;
import io.github.kloping.mirai0.unitls.Tools.Tool;

import java.io.File;

import static io.github.kloping.mirai0.Main.BotStarter.test;
import static io.github.kloping.mirai0.Main.Parse.parseToLongList;
import static io.github.kloping.mirai0.Main.Resource.*;

/**
 * @author github.kloping
 */
public class HttpStarter {
    public static void main(String[] args) throws Exception {
        String abo = new File("").getAbsolutePath();
        abo = abo.replaceAll("\\\\", "\\\\\\\\");
        System.out.println(abo);
        MyBotApplication.main(args);
        StaticSource.RECEIVERS.add((e) -> {
            StarterApplication.executeMethod(e.getSenderId(), e.getContent(),
                    e.getSubjectId(), User.create(e.getSenderId(), e.getSubjectId(), "默认昵称", "默认昵称"),
                    Group.create(e.getSubjectId(), "默认群昵称", MyHandler.HIST_GROUP_MAP), 0, e);
        });
        long t = System.currentTimeMillis();
        StarterApplication.setWaitTime(test ? 600000L : 30 * 1000L);
        StarterApplication.setAccessTypes(Long.class, io.github.kloping.mirai0.commons.User.class, Group.class, Integer.class, Message.class);
        String finalAbo = abo;
        StarterApplication.setAllAfter(new Runner() {
            @Override
            public void run(Object t, Object[] objects) throws NoRunException {
                if (t != null) {
                    Object finalT = t;
                    DEA_THREADS.submit(() -> {
                        Message message = (Message) objects[6];
                        message.sendWithAt(finalT.toString());
                    });
                }
            }
        });
        StarterApplication.setAllBefore(new Runner() {
            @Override
            public void run(Object t, Object[] objects) throws NoRunException {

            }
        });
        StarterApplication.addConfFile("./conf/conf.txt");
        StarterApplication.run(BotStarter.class);
        contextManager = StarterApplication.Setting.INSTANCE.getContextManager();
        //load conf
        superQL.addAll(parseToLongList(contextManager.getContextEntity(String.class, "superQL")));
        StarterApplication.logger.info("superQL=>" + superQL);
        MY_MAME = contextManager.getContextEntity(String.class, "bot.myName");
        verify();
        init();
        String finalAbo1 = abo;
        Tool.tool = new Tool() {
            @Override
            public String pathToImg(String path) {
                if (path.startsWith(".")) {
                    path = finalAbo1 + path;
                }
                return "[pic=" + path + "]\n";
            }
        };
        Tool.tool.deleteDir(new File("./cache"));
        Tool.tool.deleteDir(new File("./cache1"));
        SpringStarter.main(args);
        startedAfter();
        Resource.println("运行的线程=》" + Thread.activeCount());
        System.out.println("耗时: " + (System.currentTimeMillis() - t) + "豪秒");
    }
}
