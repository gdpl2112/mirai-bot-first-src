package Project.DataBases;


import Entitys.UScore;
import com.alibaba.fastjson.JSON;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static Project.Tools.Tool.*;
import static io.github.kloping.Mirai.Main.Resource.superQ;

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
        File file = new File(path + "/mainfist/groups/" + where);
        return getPtoK(file, 1);
    }

    public static boolean canBack(Long where) {
        File file = new File(path + "/mainfist/groups/" + where);
        return getPtoK(file, 0);
    }

    public static boolean canSpeak(Long where) {
        File file = new File(path + "/mainfist/groups/" + where);
        return getPtoK(file, 2);
    }

    public static boolean setSpeak(Long where, boolean k) {
        File file = new File(path + "/mainfist/groups/" + where);
        return setPtoK(file, 2, k);
    }


    public static boolean openShow(Long where) {
        File file = new File(path + "/mainfist/groups/" + where);
        return setPtoK(file, 1, true);
    }

    public static boolean closeShow(Long where) {
        File file = new File(path + "/mainfist/groups/" + where);
        return setPtoK(file, 1, false);
    }

    public static boolean addGroup(Long where) {
        File file = new File(path + "/mainfist/groups/" + where);
        return setPtoK(file, 0, true);
    }

    public static boolean removeGroup(Long where) {
        File file = new File(path + "/mainfist/groups/" + where);
        return setPtoK(file, 0, false);
    }

    private static synchronized boolean setPtoK(File file, int i, boolean k) {
        testGroup(file);
        String[] sss = getStringsFromFile(file.getPath());
        Boolean k1 = Boolean.valueOf(sss[i]);
        sss[i] = k ? "true" : "false";
        putStringInFile(sss[0] + "\n" + sss[1] + "\n" + sss[2], file.getPath());
        return k1;
    }

    private static void testGroup(File file) {
        try {
            if (file.exists()) return;
            file.createNewFile();
            putStringInFile("false\r\ntrue\r\ntrue", file.getPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static synchronized boolean getPtoK(File file, int i) {
        testGroup(file);
        String[] sss = getStringsFromFile(file.getPath());
        Boolean k1 = Boolean.valueOf(sss[i]);
        return k1;
    }

    public static boolean addFather(Long who) {
        File file = new File(path + "/mainfist/fathers/" + who);
        if (file.exists())
            return false;
        else {
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
        if (file.exists())
            return file.delete();
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
        if (!exists(who)) RegA(who);
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

    public static final Map<Long, UScore> histUScore = new ConcurrentHashMap<>();

    public static UScore getAllInfo(Long who) {
        if (!exists(who)) RegA(who);
        UScore uScore = null;
        try {
            if (histUScore.containsKey(who.longValue())) return histUScore.get(who.longValue());
            String pathN = path + "/users/" + who;
            File file = new File(pathN + "/infos");
            if (file.exists()) {
                String jsonStr = getStringFromFile(file.getPath());
                uScore = JSON.parseObject(jsonStr, UScore.class);
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
            histUScore.put(who.longValue(), uScore);
            return uScore;
        } catch (Exception e) {
            return null;
        }
    }

    public static void putInfo(UScore score) {
        String pathN = path + "/users/" + score.getWho() + "/infos";
        putStringInFile(JSON.toJSONString(score), pathN);

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
        if (!exists(who)) RegA(who);
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
        return 1l;
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
        new Thread() {
            private File file = new File(path + "/users/");

            @Override
            public void run() {
                try {
                    for (File file : file.listFiles()) {
                        try {
                            addScore(num, Long.valueOf(file.getName()));
                        } catch (NumberFormatException e) {
                            System.out.println(file.getName());
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
