package io.github.kloping.mirai0.Main;

import Project.aSpring.SpringStarter;
import io.github.kloping.MySpringTool.StarterApplication;
import io.github.kloping.MySpringTool.annotations.CommentScan;
import io.github.kloping.mirai.MiraiStarter;

import java.io.IOException;

import static io.github.kloping.mirai0.Main.BootstarpResource.*;

/**
 * @author github-kloping
 */
@CommentScan(path = "Project")
public class BotStarter {

    public static boolean test = false;

    public static void main(String[] args) throws IOException {
        long t = System.currentTimeMillis();
        MiraiStarter.main(new String[]{"./works", "/console1", "true"});
        setterStarterApplication(BotStarter.class);
        startRegisterListenerHost(args);
        Boolean t0 = StarterApplication.Setting.INSTANCE.getContextManager().getContextEntity(Boolean.class, "env.test");
        test = t0 == null ? false : t0;
        System.out.println(test ? "=============测试=============" : "长运行....................");
        datePath = "./Libs";
        init();
        SpringStarter.main(args);
        startedAfter();
        System.out.println("耗时: " + (System.currentTimeMillis() - t) + "豪秒");
    }
}