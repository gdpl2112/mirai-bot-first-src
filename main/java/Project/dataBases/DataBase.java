package Project.dataBases;


import io.github.kloping.mirai0.Entitys.GroupConf;
import io.github.kloping.mirai0.Entitys.UScore;
import com.alibaba.fastjson.JSON;
import io.github.kloping.file.FileUtils;
import io.github.kloping.initialize.FileInitializeValue;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static io.github.kloping.mirai0.unitls.Tools.Tool.*;
import static io.github.kloping.mirai0.Main.Resource.superQ;

public class DataBase {

    public static String path = ".";

    public DataBase(String mainPath) {
        try {
            path = mainPath + "/dates";
            File file = new File(path + "/mainfist");
            if (!file.exists()) {
                new File(path + "/mainfist/groups").mkdirs();
                new File(path + "/mainfist/menu").createNewFile();
                putStringInFile("————", path + "/mainfist/menu");
                new File(path + "/mainfist/groups/100").createNewFile();
                new File(path + "/mainfist/fathers").mkdirs();
                new File(path + "/mainfist/fathers/" + superQ).createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isFather(Long who) {
        return new File(path + "/mainfist/fathers/" + who).exists();
    }

    public static boolean canBackShow(Long where) {
        return getConf(where).isShow();
    }

    public static boolean canBack(Long where) {
        return getConf(where).isOpen();
    }

    public static boolean canSpeak(Long where) {
        return getConf(where).isSpeak();
    }

    public static boolean needCap(long where) {
        return getConf(where).isCap();
    }

    public static boolean setSpeak(Long where, boolean k) {
        return setConf(getConf(where).setSpeak(k)).isSpeak();
    }

    public static boolean setCap(long where, boolean k) {
        return setConf(getConf(where).setCap(k)).isCap();
    }

    public static boolean openShow(Long where) {
        return setConf(getConf(where).setShow(true)).isShow();
    }

    public static boolean closeShow(Long where) {
        return setConf(getConf(where).setShow(false)).isShow();
    }

    public static boolean openGroup(Long where) {
        return setConf(getConf(where).setOpen(true)).isOpen();
    }

    public static boolean closeGroup(Long where) {
        return setConf(getConf(where).setOpen(false)).isOpen();
    }

    public static synchronized GroupConf getConf(long id) {
        return FileInitializeValue.getValue(path + "/mainfist/groups/" + id + ".json", new GroupConf().setId(id), true);
    }

    public static synchronized GroupConf setConf(GroupConf conf) {
        return FileInitializeValue.putValues(path + "/mainfist/groups/" + conf.getId() + ".json", conf, true);
    }

    public static boolean addFather(Long who) {
        File file = new File(path + "/mainfist/fathers/" + who);
        if (file.exists()) {
            return false;
        } else {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    public static boolean removeFather(Long who) {
        File file = new File(path + "/mainfist/fathers/" + who);
        if (file.exists()) {
            return file.delete();
        }
        return false;
    }


    public static boolean RegA(Long who) {
        try {
            String pathN = path + "/users/" + who;
            File file = new File(pathN + "/infos");
            UScore score = new UScore();
            score.setWho(who);
            String jsonStr = JSON.toJSONString(score);
            putStringInFile(jsonStr, file.getPath());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean exists(Long who) {
        try {
            String pathN = path + "/users/" + who;
            File file = new File(pathN);
            boolean k = file.exists();
            return k;
        } catch (Exception e) {
            return false;
        }
    }

    public static long[] getAllInfoOld(Long who) {
        if (!exists(who)) {
            RegA(who);
        }
        try {
            System.out.println("查询 " + who + "的 信息");
            String pathN = path + "/users/" + who;
            long l1 = Long.parseLong(getStringFromFile(pathN + "/" + who + ".score").replaceAll("\r|\n", ""));
            long l2 = Long.parseLong(getStringFromFile(pathN + "/" + who + ".score_").replaceAll("\r|\n", ""));
            long l31 = Long.parseLong(getStringFromFile(pathN + "/" + who + ".times").split(":")[0]);
            long l32 = Long.parseLong(getStringFromFile(pathN + "/" + who + ".times").split(":")[1].replaceAll("\r|\n", ""));
            long l4 = Long.parseLong(getStringFromFile(pathN + "/" + who + ".times_").replaceAll("\r|\n", ""));
            long l5 = Long.parseLong(getStringFromFile(pathN + "/" + who + ".day").replaceAll("\r|\n", ""));
            long l6 = Long.parseLong(getStringFromFile(pathN + "/" + who + ".days").replaceAll("\r|\n", ""));
            long l7 = Long.parseLong(getStringFromFile(pathN + "/" + who + ".fz").replaceAll("\r|\n", ""));
            long l8 = Long.parseLong(getStringFromFile(pathN + "/" + who + ".k").replaceAll("\r|\n", ""));
            long[] ls = new long[]{l1, l2, l32, l32, l4, l5, l6, l7, l8};
            System.out.println(Arrays.toString(ls));
            return ls;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static final Map<Long, UScore> HIST_U_SCORE = new ConcurrentHashMap<>();

    public static UScore getAllInfo(Long who) {
        if (!exists(who)) {
            RegA(who);
        }
        UScore uScore = null;
        try {
            if (HIST_U_SCORE.containsKey(who.longValue())) {
                return HIST_U_SCORE.get(who.longValue());
            }
            String pathN = path + "/users/" + who;
            File file = new File(pathN + "/infos");
            if (file.exists()) {
                String jsonStr = getStringFromFile(file.getPath());
                uScore = JSON.parseObject(jsonStr, UScore.class);
                tryDeleteOld(who);
            } else {
                long[] ll = getAllInfoOld(who);
                uScore = new UScore();
                uScore.setScore(ll[0]);
                uScore.setSScore(ll[1]);
                uScore.setTimesDay(ll[2]);
                uScore.setTimes(ll[3]);
                uScore.setSTimes(ll[4]);
                uScore.setDay(ll[5]);
                uScore.setDays(ll[6]);
                uScore.setFz(ll[7]);
                uScore.setK(ll[8]);
                uScore.setWho(who);
                String jsonStr = JSON.toJSONString(uScore);
                putStringInFile(jsonStr, file.getPath());
            }
            HIST_U_SCORE.put(who.longValue(), uScore);
            return uScore;
        } catch (Exception e) {
            return null;
        }
    }

    private static void tryDeleteOld(long who) {
        String pathN = path + "/users/" + who;
        new File(pathN + "/" + who + ".score").delete();
        new File(pathN + "/" + who + ".score_").delete();
        new File(pathN + "/" + who + ".times").delete();
        new File(pathN + "/" + who + ".times_").delete();
        new File(pathN + "/" + who + ".day").delete();
        new File(pathN + "/" + who + ".days").delete();
        new File(pathN + "/" + who + ".fz").delete();
        new File(pathN + "/" + who + ".k").delete();
    }

    public static final int PUT_INFO_INDEX_MAX = 10;

    private static final Map<Long, Integer> PUT_INFO_INDEX = new ConcurrentHashMap<>();

    static {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                PUT_INFO_INDEX.forEach((k, v) -> {
                    putInfoTrue(HIST_U_SCORE.get(k));
                });
            }
        });
    }

    public static void putInfoTrue(UScore score) {
        long who = score.getWho().longValue();
        String pathN = path + "/users/" + who + "/infos";
        putStringInFile(JSON.toJSONString(score), pathN);
    }

    public static void putInfo(UScore score) {
        long who = score.getWho().longValue();
        if (PUT_INFO_INDEX.containsKey(who)) {
            if (PUT_INFO_INDEX.get(who) >= PUT_INFO_INDEX_MAX) {
                putInfoTrue(score);
                PUT_INFO_INDEX.remove(who);
            } else {
                PUT_INFO_INDEX.put(who, PUT_INFO_INDEX.get(who) + 1);
            }
        } else {
            PUT_INFO_INDEX.put(who, 1);
        }
    }

    public static long addScore(long l, Long who) {
        UScore score = getAllInfo(who);
        score.setScore(score.getScore() + l);
        putInfo(score);
        return score.getScore();
    }

    public static long addFz(long l, Long who) {
        UScore score = getAllInfo(who);
        score.setFz(score.getFz() + l);
        putInfo(score);
        return score.getScore();
    }

    public static long addScore_(long l, Long who) {
        UScore score = getAllInfo(who);
        score.setSScore(score.getSScore() + l);
        putInfo(score);
        return score.getScore();
    }

    public static long addTimes(long l, Long who) {
        if (!exists(who)) {
            RegA(who);
        }
        UScore score = getAllInfo(who);
        try {
            int today = Integer.parseInt(getToday());
            if (score.getTimesDay().intValue() != today) {
                score.setTimes(1L);
                score.setTimesDay((long) today);
            } else {
                score.setTimes((long) (score.getTimes().intValue() + 1));
            }
            putInfo(score);
            addTimes_(1, who);
        } catch (Exception e) {
            e.printStackTrace();
            String pathN = path + "/users/" + who;
            File file = new File(pathN + "/infos");
            file.delete();
            RegA(who);
        }
        return 1L;
    }

    private static long addTimes_(long l, Long who) {
        UScore score = getAllInfo(who);
        score.setSTimes(score.getSTimes() + l);
        putInfo(score);
        return score.getScore();
    }

    public static long getK(Long who) {
        long l = getAllInfo(who).getK();
        return l;
    }

    public static void setK(Long who, long l) {
        UScore score = getAllInfo(who);
        score.setK(l);
        putInfo(score);
    }

    public static void AddAllScore(long num) {

    }

    public static String getString(String fileName) {
        File f1 = new File(path, fileName);
        return FileUtils.getStringFromFile(f1.getAbsolutePath());
    }

    public static boolean setString(Object str, String fileName) {
        File f1 = new File(path, fileName);
        try {
            FileUtils.putStringInFile(str.toString(), f1);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
