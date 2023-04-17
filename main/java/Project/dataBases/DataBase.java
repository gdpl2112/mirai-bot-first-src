package Project.dataBases;


import Project.aSpring.SpringBootResource;
import Project.commons.Father;
import Project.commons.GroupConf;
import Project.commons.UserScore;
import Project.commons.resouce_and_tool.ResourceSet;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import io.github.kloping.mirai0.unitls.Tools.Tool;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static Project.aSpring.SpringBootResource.getFatherMapper;
import static Project.aSpring.SpringBootResource.getGroupConfMapper;
import static Project.commons.Father.ALL;

/**
 * @author github-kloping
 */
public class DataBase {

    public static final Map<Long, UserScore> HIST_U_SCORE = new ConcurrentHashMap<>();

    public DataBase() {
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
        GroupConf groupConf = null;
        if (getGroupConfMapper() != null) {
            groupConf = getGroupConfMapper().selectById(id);
            if (groupConf == null) {
                groupConf = new GroupConf().setId(id);
                getGroupConfMapper().insert(groupConf);
            }
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
        return conf;
    }

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
        return false;
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
        return true;
    }

    public static boolean removeFather(Long who) {
        if (getFatherMapper() != null) {
            return getFatherMapper().deleteById(who.longValue()) > 0;
        }
        return false;
    }

    public static boolean regA(Long who) {
        return SpringBootResource.getScoreMapper().insert(new UserScore().setWho(who.longValue())) > 0;
    }

    public static boolean exists(Long who) {
        return SpringBootResource.getScoreMapper().selectById(who.longValue()) != null;
    }

    public synchronized static UserScore getUserInfo(Long who) {
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

    public synchronized static void putInfo(UserScore score) {
        HIST_U_SCORE.put(score.getWho().longValue(), score);
        long who = score.getWho().longValue();
        UpdateWrapper<UserScore> q = new UpdateWrapper<>();
        q.eq("who", who);
        SpringBootResource.getScoreMapper().update(score, q);
    }

    public static boolean isMaxEarnings(long who) {
        UserScore score = getUserInfo(who);
        return score.getEarnings() + score.getDebuffs() >= ResourceSet.FinalValue.MAX_EARNINGS;
    }

    public static long addScore(long l, Long who) {
        UserScore score = getUserInfo(who);
        score.addScore(l);
        putInfo(score);
        return score.getScore();
    }

    public static long addFz(long l, Long who) {
        UserScore score = getUserInfo(who);
        score.setFz(score.getFz() + l);
        putInfo(score);
        return score.getScore();
    }

    public static long addScore_(long l, Long who) {
        UserScore score = getUserInfo(who);
        score.setSScore(score.getSScore() + l);
        putInfo(score);
        return score.getScore();
    }

    public static long addTimes(long l, Long who) {
        UserScore score = getUserInfo(who);
        try {
            int today = Integer.parseInt(Tool.INSTANCE.getToday());
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
        UserScore score = getUserInfo(who);
        score.setSTimes(score.getSTimes() + l);
        putInfo(score);
        return score.getScore();
    }

    public static long getK(Long who) {
        long l = getUserInfo(who).getK();
        return l;
    }

    public static void setK(Long who, long l) {
        UserScore score = getUserInfo(who);
        score.setK(l);
        putInfo(score);
    }
}
