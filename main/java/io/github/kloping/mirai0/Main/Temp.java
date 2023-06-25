package io.github.kloping.mirai0.Main;

import static Project.commons.uitls.FileDirUtils.copyEves;

/**
 * @author github.kloping
 */
public class Temp {
    public static void main(String[] args) throws Exception{
        copyEves("D:\\Projects\\TeamProjects\\gdpl2112\\main-bot\\src\\main\\resources",
                "D:\\Projects\\TeamProjects\\gdpl2112\\main-bot\\target\\classes", true);
        BotStarter.main(new String[]{});
    }
}
