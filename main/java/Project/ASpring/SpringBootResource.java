package Project.ASpring;

import Entitys.UScore;
import Entitys.gameEntitys.PersonInfo;
import Project.ASpring.mapper.GInfoMapper;
import Project.ASpring.mapper.PersonInfoMapper;
import Project.ASpring.mapper.UScoreMapper;
import Project.DataBases.DataBase;
import Project.DataBases.GameDataBase;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

import java.io.File;

import static Project.DataBases.DataBase.getAllInfo;
import static Project.DataBases.GameDataBase.getInfo;

public class SpringBootResource {
    public static ConfigurableApplicationContext configuration;
    public static ConfigurableEnvironment environment;
    public static UScoreMapper scoreMapper;
    public static PersonInfoMapper personInfoMapper;
    public static GInfoMapper gInfoMapper;

    public static void init() {
        scoreMapper = configuration.getBean(UScoreMapper.class);
        personInfoMapper = configuration.getBean(PersonInfoMapper.class);
        gInfoMapper = configuration.getBean(GInfoMapper.class);
        System.out.println("==============spring papered=================");
    }

    public static void move0() {
        File file = new File(DataBase.path + "/users/");
        try {
            for (File f : file.listFiles()) {
                try {
                    long q = Long.valueOf(f.getName());
                    UScore score = getAllInfo(q);
                    scoreMapper.insert(score);
                    System.out.println("moved sc: " + q);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.err.println(f);
                }
            }
            System.out.println("moved sc : all");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void move1() {
        File file = new File(GameDataBase.path + "/dates/users/");
        try {
            for (File f : file.listFiles()) {
                try {
                    long q = Long.valueOf(f.getName());
                    PersonInfo info = getInfo(q);
                    personInfoMapper.insert(info);
                    System.out.println("moved pi: " + q);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.err.println(f);
                }
            }
            System.out.println("moved pi : all");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
