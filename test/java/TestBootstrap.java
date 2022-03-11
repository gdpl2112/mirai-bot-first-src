import Project.dataBases.ZongMenDataBase;
import io.github.kloping.mirai0.Entitys.gameEntitys.Zon;
import io.github.kloping.mirai0.Entitys.gameEntitys.Zong;
import io.github.kloping.mirai0.Main.BotStarter;

import java.io.File;

import static Project.aSpring.SpringBootResource.getZonMapper;
import static Project.aSpring.SpringBootResource.getZongMapper;
import static Project.dataBases.ZongMenDataBase.getZonInfo;
import static Project.dataBases.ZongMenDataBase.getZongInfo;

/**
 * @author github.kloping
 */
public class TestBootstrap {
    public static void main(String[] args) throws Throwable {
        BotStarter.main(args);
        System.err.println("all is ok");
    }
}
