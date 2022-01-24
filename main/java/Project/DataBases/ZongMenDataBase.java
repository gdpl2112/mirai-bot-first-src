package Project.DataBases;

import Entitys.gameEntitys.Zon;
import Entitys.gameEntitys.Zong;
import Project.Tools.JSONUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import static Project.ResourceSet.FinalString.NULL_LOW_STR;
import static Project.Tools.Tool.getStringFromFile;
import static Project.Tools.Tool.putStringInFile;

/**
 * @author github-kloping
 */
public class ZongMenDataBase {
    public static String path;
    public static Map<Long, Integer> qq2id = new HashMap<>();

    public ZongMenDataBase(String mainPath) {
        path = mainPath + "/dates/games" + "/dates/system/ZongMens";
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        initMap();
    }

    private void initMap() {
        File[] files = new File(path).listFiles((f) -> f.isDirectory());
        for (File file : files) {
            Integer id = Integer.valueOf(file.getName());
            for (Number r : getZongInfo(id).getMember()) {
                qq2id.put(r.longValue(), id);
            }
        }
    }

    public static boolean createNewZong(Long qq, String name) {
        try {
            if (name == null || name.isEmpty() || NULL_LOW_STR.equals(name))
                return false;
            Zong zong = new Zong();
            Integer id = Integer.valueOf(qq2id.get(0L) + "");
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
            Zon zon = new Zon().setId(id).setXper(0).setQq(qq).setTimes(1).setLevel(2);
            zong.getMember().add(qq);
            boolean k1 = putZongInfo(zong);
            boolean k2 = putZonInfo(zon);
            qq2id.put(qq, id++);
            qq2id.put(0L, id);
            return k1 && k2;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Zong getZongInfo(Long qq) {
        return getZongInfo(qq2id.get(qq));
    }

    public static Zong getZongInfo(Integer id) {
        try {
            File file = new File(path + "/" + id + "/main.json");
            String line = getStringFromFile(file.getPath());
            Zong zong = JSONUtils.jsonStringToObject(line, Zong.class);
            System.out.println(zong);
            return zong;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Zon getZonInfo(Long qq) {
        try {
            Integer id = qq2id.get(qq);
            File file = new File(path + "/" + qq2id.get(qq) + "/" + qq + ".json");
            String line = getStringFromFile(file.getPath());
            Zon zon = JSONUtils.jsonStringToObject(line, Zon.class);
            return zon;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean putZongInfo(Zong zong) {
        try {
            File file = new File(path + "/" + zong.getId() + "/main.json");
            file.getParentFile().mkdirs();
            file.createNewFile();
            String line = JSONUtils.objectToJsonString(zong);
            putStringInFile(line, file.getPath(), "utf-8");
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean putZonInfo(Zon zon) {
        try {
            File file = new File(path + "/" + zon.getId() + "/" + zon.getQq() + ".json");
            file.getParentFile().mkdirs();
            file.createNewFile();
            String line = JSONUtils.objectToJsonString(zon);
            putStringInFile(line, file.getPath(), "utf-8");
            qq2id.put(zon.getQq().longValue(), zon.getId());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean addPer(Zong zong, Long qq) {
        zong.getMember().add(qq);
        zong.setMembers(zong.getMembers() + 1);
        Zon zon = new Zon()
                .setTimes(0)
                .setQq(qq)
                .setXper(0)
                .setLevel(0)
                .setId(zong.getId());
        return putZongInfo(zong) && putZonInfo(zon);
    }

    public static String[] getAllZongNames() {
        StringBuilder sb = new StringBuilder();
        File[] files = new File(path).listFiles();
        for (File file1 : files) {
            if (file1.isDirectory()) {
                sb.append(file1.getName()).append(":").append(getZongInfo(Integer.valueOf(file1.getName())).getName()).append("\r\n");
            } else {
                continue;
            }
        }
        return sb.toString().split("\\s+");
    }
}




