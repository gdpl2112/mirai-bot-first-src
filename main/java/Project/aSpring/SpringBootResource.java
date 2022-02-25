package Project.aSpring;

import Project.aSpring.mcs.mapper.*;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.kloping.clasz.ClassUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

import java.io.IOException;
import java.lang.reflect.Field;

/**
 * @author github-kloping
 */
public class SpringBootResource {
    private static final String PUBLIC_IP = getPublicIp();
    public static String address;
    public static ConfigurableApplicationContext configuration;
    public static ConfigurableEnvironment environment;

    public static UserScoreMapper scoreMapper;
    public static PersonInfoMapper personInfoMapper;
    public static GInfoMapper gInfoMapper;
    public static AutoReplyMapper autoReplyMapper;
    public static GroupConfMapper groupConfMapper;
    public static FatherMapper fatherMapper;
    public static TradingRecordMapper tradingRecordMapper;
    public static ShopItemMapper shopItemMapper;
    public static WarpMapper warpMapper;
    public static KillGhostMapper killGhostMapper;
    public static BagMapper bagMapper;
    public static AqBagMapper aqBagMapper;
    public static SoulBoneMapper soulBoneMapper;
    public static SoulAttributeMapper soulAttributeMapper;
    public static HhpzMapper hhpzMapper;
    public static TaskPointMapper taskPointMapper;
    public static SingListMapper singListMapper;
    public static SkillInfoMapper skillInfoMapper;

    public static SkillInfoMapper getSkillInfoMapper() {
        return skillInfoMapper;
    }

    public static SingListMapper getSingListMapper() {
        return singListMapper;
    }

    public static TaskPointMapper getTaskPointMapper() {
        return taskPointMapper;
    }

    public static HhpzMapper getHhpzMapper() {
        return hhpzMapper;
    }

    public static SoulBoneMapper getSoulBoneMapper() {
        return soulBoneMapper;
    }

    public static SoulAttributeMapper getSoulAttributeMapper() {
        return soulAttributeMapper;
    }

    public static AqBagMapper getAqBagMapper() {
        return aqBagMapper;
    }

    public static BagMapper getBagMapper() {
        return bagMapper;
    }

    public static KillGhostMapper getKillGhostMapper() {
        return killGhostMapper;
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

    public static UserScoreMapper getScoreMapper() {
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
                            if (declaredField.get(null) != null) {
                                continue;
                            }
                            Class<? extends BaseMapper> c1 = c0;
                            Object o0 = configuration.getBean(c1);
                            declaredField.set(null, o0);
                        }
                    }
                }
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
}