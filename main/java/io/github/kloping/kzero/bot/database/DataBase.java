package io.github.kloping.kzero.bot.database;


import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import io.github.kloping.MySpringTool.annotations.AutoStand;
import io.github.kloping.MySpringTool.annotations.Entity;
import io.github.kloping.kzero.spring.dao.GroupConf;
import io.github.kloping.kzero.spring.dao.UserScore;
import io.github.kloping.kzero.spring.mapper.GroupConfMapper;
import io.github.kloping.kzero.spring.mapper.UserScoreMapper;
import io.github.kloping.kzero.rt.ResourceSet;

/**
 * @author github-kloping
 */
@Entity
public class DataBase {
    @AutoStand
    UserScoreMapper userScoreMapper;
    @AutoStand
    GroupConfMapper groupConfMapper;

    public synchronized GroupConf getConf(String id) {
        GroupConf groupConf = null;
        groupConf = groupConfMapper.selectById(id);
        if (groupConf == null) {
            groupConf = new GroupConf().setId(id);
            groupConfMapper.insert(groupConf);
        }
        return groupConf;
    }

    public synchronized GroupConf setConf(GroupConf conf) {
        getConf(conf.getId());
        UpdateWrapper<GroupConf> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", conf.getId());
        groupConfMapper.update(conf, updateWrapper);
        return conf;
    }

    public boolean exists(String sid) {
        return userScoreMapper.selectById(sid) != null;
    }

    public boolean regA(String sid) {
        return userScoreMapper.insert(new UserScore().setWho(sid)) > 0;
    }

    public synchronized UserScore getUserInfo(String sid) {
        if (!exists(sid)) regA(sid);
        UserScore uScore = null;
        try {
            uScore = userScoreMapper.selectById(sid);
            return uScore;
        } catch (Exception e) {
            return null;
        }
    }

    public void putInfo(UserScore score) {
        UpdateWrapper<UserScore> q = new UpdateWrapper<>();
        q.eq("who", score.getWho());
        userScoreMapper.update(score, q);
    }

    public boolean isMaxEarnings(String sid) {
        UserScore score = getUserInfo(sid);
        return score.getEarnings() + score.getDebuffs() >= ResourceSet.FinalValue.MAX_EARNINGS;
    }


    public long addScore(long l, String sid) {
        UserScore score = getUserInfo(sid);
        score.addScore(l);
        putInfo(score);
        return score.getScore();
    }

    public long addFz(long l, String sid) {
        UserScore score = getUserInfo(sid);
        score.setFz(score.getFz() + l);
        putInfo(score);
        return score.getScore();
    }

    public long addScore_(long l, String sid) {
        UserScore score = getUserInfo(sid);
        score.setSScore(score.getSScore() + l);
        putInfo(score);
        return score.getScore();
    }

}
