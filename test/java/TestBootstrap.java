import Project.aSpring.SpringBootResource;
import Project.dataBases.DataBase;
import Project.dataBases.GameDataBase;
import io.github.kloping.mirai0.Entitys.UserScore;
import io.github.kloping.mirai0.Main.BotStarter;

import java.io.File;

/**
 * @author github.kloping
 */
public class TestBootstrap {
    public static void main(String[] args) throws Throwable {
        BotStarter.main(args);
        for (File file : new File(DataBase.path + "/users/").listFiles()) {
            try {
                Long q = Long.valueOf(file.getName());
                UserScore score = DataBase.getAllInfoFile(q);
                SpringBootResource.getScoreMapper().insert(score);
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("all is ok");
    }
}
