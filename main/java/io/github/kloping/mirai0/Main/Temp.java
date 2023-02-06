package io.github.kloping.mirai0.Main;

import static io.github.kloping.mirai0.commons.uitls.FileDirUtils.copyEves;

/**
 * @author github.kloping
 */
public class Temp {
    public static void main(String[] args) {
        copyEves("D:\\Projects\\OwnProjects\\MyMirai_01\\src\\main\\resource",
                "D:\\Projects\\OwnProjects\\MyMirai_01\\target\\classes", true);
        BotStarter.main(new String[]{});
    }
}
