package Project.ASpring;

import Entitys.UScore;
import Entitys.gameEntitys.PersonInfo;
import Project.ASpring.mcs.mapper.GInfoMapper;
import Project.ASpring.mcs.mapper.PersonInfoMapper;
import Project.ASpring.mcs.mapper.UScoreMapper;
import Project.DataBases.DataBase;
import Project.DataBases.GameDataBase;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.jsoup.Jsoup;
import org.springframework.beans.BeansException;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

import java.io.File;
import java.io.IOException;

import static Project.DataBases.DataBase.getAllInfo;
import static Project.DataBases.GameDataBase.getInfo;

/**
 * @author github-kloping
 */
public class SpringBootResource {
    private static final String PUBLIC_IP = getPublicIp();

    public static ConfigurableApplicationContext configuration;
    public static ConfigurableEnvironment environment;
    public static UScoreMapper scoreMapper;
    public static PersonInfoMapper personInfoMapper;
    public static GInfoMapper gInfoMapper;
    public static String address;

    public static void init() {
        try {
            scoreMapper = configuration.getBean(UScoreMapper.class);
            personInfoMapper = configuration.getBean(PersonInfoMapper.class);
            gInfoMapper = configuration.getBean(GInfoMapper.class);
            System.out.println("==============spring papered=================");
            System.err.println(configuration.getEnvironment().getProperty("spring.resources.static-locations"));
            address = "http://" + PUBLIC_IP + ":" + configuration.getEnvironment().getProperty("server.port");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getPublicIp() {
        try {
            String jsonStr = Jsoup.connect("https://ip.cn/api/index?ip=&type=0").ignoreContentType(true).get().body().text();
            JSONObject jo = JSON.parseObject(jsonStr);
            return jo.getString("ip");
        } catch (IOException e) {
            e.printStackTrace();
            return "localhost";
        }
    }

    public static void main(String[] args) {
        System.out.println(PUBLIC_IP);
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
