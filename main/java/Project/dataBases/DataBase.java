package Project.dataBases;


import Project.aSpring.SpringBootResource;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import io.github.kloping.file.FileUtils;
import io.github.kloping.initialize.FileInitializeValue;
import io.github.kloping.mirai0.commons.Father;
import io.github.kloping.mirai0.commons.GroupConf;
import io.github.kloping.mirai0.commons.UserScore;
import io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet;
import io.github.kloping.mirai0.unitls.Tools.Tool;

import java.io.File;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static Project.aSpring.SpringBootResource.getFatherMapper;
import static Project.aSpring.SpringBootResource.getGroupConfMapper;
import static io.github.kloping.mirai0.commons.Father.ALL;

/**
 * @author github-kloping
 */
public class DataBase {

    public static final Map<Long, UserScore> HIST_U_SCORE = new ConcurrentHashMap<>();
    public static String path = ".";

    public DataBase(String mainPath) {
        try {
            path = mainPath + "/dates";
             /*
            File file = new File(path + "/mainfist");
            if (!file.exists()) {
                new File(path + "/mainfist/groups").mkdirs();
                new File(path + "/mainfist/menu").createNewFile();
                putStringInFile("————", path + "/mainfist/menu");
                new File(path + "/mainfist/groups/100").createNewFile();
                new File(path + "/mainfist/fathers").mkdirs();
                new File(path + "/mainfist/fathers/" + superQ).createNewFile();
            }
            */
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean canBackShow(Long where) {
        return getConf(where).getShow();
    }

    public static boolean canBack(Long where) {
        return getConf(where).getOpen();
    }

    public static boolean canSpeak(Long where) {
        return getConf(where).getSpeak();
    }

    public static boolean needCap(long where) {
        return getConf(where).getCap();
    }

    public static boolean setSpeak(Long where, boolean k) {
        return setConf(getConf(where).setSpeak(k)).getSpeak();
    }

    public static boolean setCap(long where, boolean k) {
        return setConf(getConf(where).setCap(k)).getCap();
    }

    public static boolean openShow(Long where) {
        return setConf(getConf(where).setShow(true)).getShow();
    }

    public static boolean closeShow(Long where) {
        return setConf(getConf(where).setShow(false)).getShow();
    }

    public static boolean openGroup(Long where) {
        return setConf(getConf(where).setOpen(true)).getOpen();
    }

    public static boolean closeGroup(Long where) {
        return setConf(getConf(where).setOpen(false)).getOpen();
    }

    public static synchronized GroupConf getConf(long id) {
        GroupConf groupConf;
        if (getGroupConfMapper() != null) {
            groupConf = getGroupConfMapper().selectById(id);
            if (groupConf == null) {
                groupConf = new GroupConf().setId(id);
                getGroupConfMapper().insert(groupConf);
            }
        } else {
            groupConf = FileInitializeValue.getValue(path + "/mainfist/groups/" + id + ".json", new GroupConf().setId(id), true);
        }
        return groupConf;
    }

    public static synchronized GroupConf setConf(GroupConf conf) {
        if (getGroupConfMapper() != null) {
            getConf(conf.getId());
            UpdateWrapper<GroupConf> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("id", conf.getId());
            getGroupConfMapper().update(conf, updateWrapper);
            return conf;
        }
        return FileInitializeValue.putValues(path + "/mainfist/groups/" + conf.getId() + ".json", conf, true);
    }

//    public static boolean isFather(Long who) {
//        if (getFatherMapper() != null) {
//            Father father = null;
//            try {
//                father = getFatherMapper().selectById(who);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return father.getPermission().equals(ALL);
//        }
//        return new File(path + "/mainfist/fathers/" + who).exists();
//    }

    public static boolean isFather(Long who, Long gid) {
        if (getFatherMapper() != null) {
            Father father = null;
            try {
                father = getFatherMapper().selectById(who);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return father != null && (father.getPermission().equals(ALL) || father.hasPermission(gid.longValue()));
        }
        return new File(path + "/mainfist/fathers/" + who).exists();
    }

    public static boolean addFather(Long who) {
        return addFather(who, ALL);
    }

    public static boolean addFather(Long who, String perm) {
        if (getFatherMapper() != null) {
            Father father;
            if ((father = getFatherMapper().selectById(who)) == null) {
                father = new Father();
                father.setId(who.longValue());
                father.addPermission(Long.parseLong(perm));
                return getFatherMapper().insert(father) > 0;
            }
            father.setId(who.longValue());
            father.addPermission(Long.parseLong(perm));
            father.getPermission();
            return getFatherMapper().updateById(father) > 0;
        }
        File file = new File(path + "/mainfist/fathers/" + who);
        if (file.exists()) {
            return false;
        } else {
            Tool.tool.testFile(file.getAbsolutePath());
        }
        return true;
    }

    public static boolean removeFather(Long who) {
        if (getFatherMapper() != null) {
            return getFatherMapper().deleteById(who.longValue()) > 0;
        }
        File file = new File(path + "/mainfist/fathers/" + who);
        if (file.exists()) {
            return file.delete();
        }
        return false;
    }

    public static boolean regA(Long who) {
        return SpringBootResource.getScoreMapper().insert(new UserScore().setWho(who.longValue())) > 0;
    }

    public static boolean exists(Long who) {
        return SpringBootResource.getScoreMapper().selectById(who.longValue()) != null;
    }

    public static UserScore getAllInfo(Long who) {
        if (!exists(who)) regA(who);
        UserScore uScore = null;
        try {
            if (HIST_U_SCORE.containsKey(who.longValue())) return HIST_U_SCORE.get(who.longValue());
            uScore = SpringBootResource.getScoreMapper().selectById(who.longValue());
            HIST_U_SCORE.put(who.longValue(), uScore);
            return uScore;
        } catch (Exception e) {
            return null;
        }
    }

    public static UserScore getAllInfoFile(Long who) {
        UserScore uScore = null;
        try {
            if (HIST_U_SCORE.containsKey(who.longValue())) {
                return HIST_U_SCORE.get(who.longValue());
            }
            String pathN = path + "/users/" + who;
            File file = new File(pathN + "/infos");
            if (file.exists()) {
                String jsonStr =
                        Tool.tool.getStringFromFile(file.getPath());
                uScore = JSON.parseObject(jsonStr, UserScore.class);
                tryDeleteOld(who);
            } else {
                long[] ll = getAllInfoOld(who);
                uScore = new UserScore();
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
                Tool.tool.putStringInFile(jsonStr, file.getPath());
            }
            HIST_U_SCORE.put(who.longValue(), uScore);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return uScore;
    }

    public static void putInfo(UserScore score) {
        HIST_U_SCORE.put(score.getWho().longValue(), score);
        long who = score.getWho().longValue();
        UpdateWrapper<UserScore> q = new UpdateWrapper<>();
        q.eq("who", who);
        SpringBootResource.getScoreMapper().update(score, q);
    }

    public static long[] getAllInfoOld(Long who) {
        try {
            System.out.println("查询 " + who + "的 信息");
            String pathN = path + "/users/" + who;
            long l1 = Long.parseLong(Tool.tool.getStringFromFile(pathN + "/" + who + ".score", 0L).replaceAll("\r|\n", ""));
            long l2 = Long.parseLong(Tool.tool.getStringFromFile(pathN + "/" + who + ".score_", 0L).replaceAll("\r|\n", ""));
            long l31 = Long.parseLong(Tool.tool.getStringFromFile(pathN + "/" + who + ".times", 0L).split(":")[0]);
            long l32 = Long.parseLong(Tool.tool.getStringFromFile(pathN + "/" + who + ".times", 0L).split(":")[1].replaceAll("\r|\n", ""));
            long l4 = Long.parseLong(Tool.tool.getStringFromFile(pathN + "/" + who + ".times_", 0L).replaceAll("\r|\n", ""));
            long l5 = Long.parseLong(Tool.tool.getStringFromFile(pathN + "/" + who + ".day", 0L).replaceAll("\r|\n", ""));
            long l6 = Long.parseLong(Tool.tool.getStringFromFile(pathN + "/" + who + ".days", 0L).replaceAll("\r|\n", ""));
            long l7 = Long.parseLong(Tool.tool.getStringFromFile(pathN + "/" + who + ".fz", 0L).replaceAll("\r|\n", ""));
            long l8 = Long.parseLong(Tool.tool.getStringFromFile(pathN + "/" + who + ".k", 0L).replaceAll("\r|\n", ""));
            long[] ls = new long[]{l1, l2, l32, l32, l4, l5, l6, l7, l8};
            System.out.println(Arrays.toString(ls));
            return ls;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return null;
        } finally {
            tryDeleteOld(who);
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

    public static boolean isMaxEarnings(long who) {
        UserScore score = getAllInfo(who);
        return score.getEarnings() + score.getDebuffs() >= ResourceSet.FinalValue.MAX_EARNINGS;
    }

    public static long addScore(long l, Long who) {
        UserScore score = getAllInfo(who);
        score.addScore(l);
        putInfo(score);
        return score.getScore();
    }

    public static long addFz(long l, Long who) {
        UserScore score = getAllInfo(who);
        score.setFz(score.getFz() + l);
        putInfo(score);
        return score.getScore();
    }

    public static long addScore_(long l, Long who) {
        UserScore score = getAllInfo(who);
        score.setSScore(score.getSScore() + l);
        putInfo(score);
        return score.getScore();
    }

    public static long addTimes(long l, Long who) {
        UserScore score = getAllInfo(who);
        try {
            int today = Integer.parseInt(Tool.tool.getToday());
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
        }
        return 1L;
    }

    private static long addTimes_(long l, Long who) {
        UserScore score = getAllInfo(who);
        score.setSTimes(score.getSTimes() + l);
        putInfo(score);
        return score.getScore();
    }

    public static long getK(Long who) {
        long l = getAllInfo(who).getK();
        return l;
    }

    public static void setK(Long who, long l) {
        UserScore score = getAllInfo(who);
        score.setK(l);
        putInfo(score);
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
