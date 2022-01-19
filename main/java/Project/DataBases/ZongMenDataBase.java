package Project.DataBases;

import Entitys.gameEntitys.Zon;
import Entitys.gameEntitys.Zong;
import Project.Tools.JSONUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import static Project.ResourceSet.Final.NULL_LOW_STR;
import static Project.Tools.Tool.getStringFromFile;
import static Project.Tools.Tool.putStringInFile;

public class ZongMenDataBase {
    public static String path;
    public static Map<Long, Integer> qq2id = new HashMap<>();

    public ZongMenDataBase(String mainPath) {
        path = mainPath + "/dates/games" + "/dates/system/ZongMens";
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        if (!new File(path + "/table.t").exists())
            initFill();
        initMap();
    }

    private void initFill() {
        qq2id.put(0L, 1);
        putStringInFile(mapToString(qq2id), path + "/table.t", "utf-8");
    }

    private void initMap() {
        qq2id = parseMap(getStringFromFile(path + "/table.t", "utf-8"), Long.class, Integer.class);
    }

    public static <K extends Object, V extends Object> Map<K, V> parseMap(String lines, Class<K> cla1, Class<V> cla2) {
        return parseMap(lines.split("\\s+"), cla1, cla2);
    }

    public static <K extends Object, V extends Object> Map<K, V> parseMap(String[] lines, Class<K> kClass, Class<V> vClass) {
        Map<K, V> map = new HashMap<>();
        for (String line : lines) {
            String[] ss = line.split("=");
            if (ss.length <= 1) continue;
            K k = null;
            V v = null;
            try {
                if (kClass != String.class) {
                    Method method = kClass.getDeclaredMethod("valueOf", String.class);
                    k = (K) method.invoke(null, ss[0]);
                } else k = (K) ss[0];
                if (vClass != String.class) {
                    Method method = vClass.getDeclaredMethod("valueOf", String.class);
                    v = (V) method.invoke(null, ss[1]);
                } else v = (V) ss[1];
            } catch (Exception e) {
                e.printStackTrace();
            }
            map.put(k, v);
        }
        return map;
    }

    public static <K extends Object, V extends Object> String mapToString(Map<K, V> map) {
        StringBuilder sb = new StringBuilder();
        for (K k : map.keySet()) {
            sb.append(k + "=" + map.get(k)).append("\r\n");
        }
        return sb.toString();
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
            updateMap();
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
            String line = JSONUtils.ObjectToJsonString(zong);
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
            String line = JSONUtils.ObjectToJsonString(zon);
            putStringInFile(line, file.getPath(), "utf-8");
            qq2id.put(zon.getQq().longValue(), zon.getId());
            updateMap();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void updateMap() {
        putStringInFile(mapToString(qq2id), path + "/table.t", "utf-8");
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
            } else continue;
        }
        return sb.toString().split("\\s+");
    }
}




