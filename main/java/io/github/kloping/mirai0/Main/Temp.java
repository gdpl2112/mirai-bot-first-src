package io.github.kloping.mirai0.Main;

import static io.github.kloping.mirai0.commons.uitls.FileDirUtils.copyEves;

/**
 * @author github.kloping
 */
public class Temp {
    public static void main(String[] args) {
        copyEves("D:\\Projects\\TeamProjects\\gdpl2112\\main-bot\\src\\main\\resource",
                "D:\\Projects\\TeamProjects\\gdpl2112\\main-bot\\target\\classes", true);
        BotStarter.main(new String[]{});
    }
}
