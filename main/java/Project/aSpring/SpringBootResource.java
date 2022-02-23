package Project.aSpring;

import Project.aSpring.mcs.mapper.*;
import Project.dataBases.DataBase;
import Project.dataBases.GameDataBase;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.kloping.clasz.ClassUtils;
import io.github.kloping.mirai0.Entitys.UScore;
import io.github.kloping.mirai0.Entitys.gameEntitys.PersonInfo;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;

import static Project.dataBases.DataBase.getAllInfo;
import static Project.dataBases.GameDataBase.getInfo;

/**
 * @author github-kloping
 */
public class SpringBootResource {
    private static final String PUBLIC_IP = getPublicIp();
    public static String address;

    public static ConfigurableApplicationContext configuration;
    public static ConfigurableEnvironment environment;

    public static UScoreMapper scoreMapper;
    public static PersonInfoMapper personInfoMapper;
    public static GInfoMapper gInfoMapper;
    public static AutoReplyMapper autoReplyMapper;
    public static GroupConfMapper groupConfMapper;
    public static FatherMapper fatherMapper;
    public static TradingRecordMapper tradingRecordMapper;
    public static ShopItemMapper shopItemMapper;
    public static WarpMapper warpMapper;
    public static KillGhostMapper normalMapper;

    public static KillGhostMapper getKillGhostMapper() {
        return normalMapper;
    }

    public static WarpMapper getWarpMapper() {
        return warpMapper;
    }

    public static ConfigurableApplicationContext getConfiguration() {
        return configuration;
    }

    public static ConfigurableEnvironment getEnvironment() {
        return environment;
    }

    public static UScoreMapper getScoreMapper() {
        return scoreMapper;
    }

    public static PersonInfoMapper getPersonInfoMapper() {
        return personInfoMapper;
    }

    public static GInfoMapper getgInfoMapper() {
        return gInfoMapper;
    }

    public static AutoReplyMapper getAutoReplyMapper() {
        return autoReplyMapper;
    }

    public static GroupConfMapper getGroupConfMapper() {
        return groupConfMapper;
    }

    public static FatherMapper getFatherMapper() {
        return fatherMapper;
    }

    public static TradingRecordMapper getTradingRecordMapper() {
        return tradingRecordMapper;
    }

    public static ShopItemMapper getShopItemMapper() {
        return shopItemMapper;
    }

    public static String getAddress() {
        return address;
    }

    public static void init() {
        try {
            try {
                for (Field declaredField : SpringBootResource.class.getDeclaredFields()) {
                    if (ClassUtils.isStatic(declaredField)) {
                        Class c0 = declaredField.getType();
                        if (c0.isInterface()) {
                            declaredField.setAccessible(true);
                            if (declaredField.get(null)!=null){continue;}
                            Class<? extends BaseMapper> c1 = c0;
                            Object o0 = configuration.getBean(c1);
                            declaredField.set(null, o0);
                        }
                    }
                }
//                tradingRecordMapper = configuration.getBean(TradingRecordMapper.class);
//                fatherMapper = configuration.getBean(FatherMapper.class);
//                groupConfMapper = configuration.getBean(GroupConfMapper.class);
//                scoreMapper = configuration.getBean(UScoreMapper.class);
//                personInfoMapper = configuration.getBean(PersonInfoMapper.class);
//                gInfoMapper = configuration.getBean(GInfoMapper.class);
//                autoReplyMapper = configuration.getBean(AutoReplyMapper.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("==============spring papered=================");
            address = "http://" + PUBLIC_IP + ":" + configuration.getEnvironment().getProperty("server.port");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getPublicIp() {
        try {
            Document document = Jsoup.connect("https://2021.ip138.com/")
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/97.0.4692.71 Safari/537.36 Edg/97.0.1072.62")
                    .ignoreContentType(true)
                    .get();
            String ip = document.getElementsByTag("a").get(0).text();
            return ip;
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
                    getScoreMapper().insert(score);
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
                    getPersonInfoMapper().insert(info);
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
