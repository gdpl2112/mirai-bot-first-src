package Project.dataBases;

import io.github.kloping.mirai0.Main.Resource;
import io.github.kloping.mirai0.commons.Zong;
import io.github.kloping.mirai0.commons.gameEntitys.Zon;
import io.github.kloping.mirai0.unitls.Tools.JsonUtils;
import io.github.kloping.mirai0.unitls.Tools.Tool;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import static Project.aSpring.SpringBootResource.getZonMapper;
import static Project.aSpring.SpringBootResource.getZongMapper;
import static io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet.FinalString.NULL_LOW_STR;

/**
 * @author github-kloping
 */
public class ZongMenDataBase {
    public static String path;
    public static Map<Long, Integer> qq2id = new HashMap<>();
    private static int idx = 0;

    public ZongMenDataBase(String mainPath) {
        path = mainPath + "/dates/games/dates/system/ZongMens";
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        Resource.START_AFTER.add(() -> {
            reInitMap();
        });
    }

    public static int getIdx() {
        return ++idx;
    }

    public static boolean createNewZong(Long qq, String name) {
        try {
            if (name == null || name.isEmpty() || NULL_LOW_STR.equals(name))
                return false;
            Zong zong = new Zong();
            Integer id = Integer.valueOf(getIdx());
            zong.setId(id)
                    .setName(name)
                    .setLevel(1)
                    .setState(1)
                    .setXp(0L)
                    .setElders(0)
                    .setMembers(1)
                    .setMember(new HashSet<>())
                    .setElder(new HashSet<>())
                    .setIcon("")
                    .setMain(qq);
            Zon zon = new Zon().setId(id).setXper(0).setQq(qq).setTimes(1).setLevel(2).setActive(0);
            zong.getMember().add(qq);
            getZongMapper().insert(zong);
            getZonMapper().insert(zon);
            qq2id.put(qq, id);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Zong getZongInfo(Long qq) {
        return getZongInfo(qq2id.get(qq));
    }

    public static Zong getZongInfoFromFile(Integer id) {
        try {
            File file = new File(path + "/" + id + "/main.json");
            String line = Tool.tool.getStringFromFile(file.getPath());
            Zong zong = JsonUtils.jsonStringToObject(line, Zong.class);
            System.out.println(zong);
            return zong;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Zong getZongInfo(Integer id) {
        try {
            return getZongMapper().selectById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Zon getZonInfo(Long qq) {
        try {
            return getZonMapper().selectById(qq);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Zon getZonInfoFromFile(Long qq) {
        try {
            Integer id = qq2id.get(qq);
            File file = new File(path + "/" + qq2id.get(qq) + "/" + qq + ".json");
            String line = Tool.tool.getStringFromFile(file.getPath());
            Zon zon = JsonUtils.jsonStringToObject(line, Zon.class);
            return zon;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean putZongInfo(Zong zong) {
        return getZongMapper().updateById(zong) > 0;
    }

    public static boolean putZonInfo(Zon zon) {
        if (getZonMapper().updateById(zon) > 0) {
            return true;
        } else {
            return getZonMapper().insert(zon) > 0;
        }
    }

    public static boolean addPer(Zong zong, Long qq) {
        Zon zon = new Zon()
                .setTimes(0)
                .setQq(qq)
                .setXper(0)
                .setLevel(0)
                .setId(zong.getId());
        qq2id.put(qq, zong.getId());
        return putZonInfo(zon);
    }

    public static String[] getAllZongNames() {
        StringBuilder sb = new StringBuilder();
        for (Zong zong : getZongMapper().selectAll()) {
            sb.append(zong.getId()).append(":").append(zong.getName()).append("\r\n");
        }
        return sb.toString().split("\\s+");
    }

    private void reInitMap() {
        try {
            for (Zong zong : getZongMapper().selectAll()) {
                for (Number number : zong.getMember()) {
                    qq2id.put(number.longValue(), zong.getId());
                    idx = idx >= zong.getId() ? idx : zong.getId() + 1;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}




