package io.github.kloping.kzero.bot.database;


import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import io.github.kloping.MySpringTool.annotations.AutoStand;
import io.github.kloping.MySpringTool.annotations.Entity;
import io.github.kloping.kzero.spring.dao.Father;
import io.github.kloping.kzero.spring.dao.GroupConf;
import io.github.kloping.kzero.spring.dao.UserScore;
import io.github.kloping.kzero.spring.mapper.FatherMapper;
import io.github.kloping.kzero.spring.mapper.GroupConfMapper;
import io.github.kloping.kzero.spring.mapper.UserScoreMapper;

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
        return userScoreMapper.insert(new UserScore().setId(sid)) > 0;
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
        userScoreMapper.updateById(score);
    }

    public long addScore(int l, String sid) {
        UserScore score = getUserInfo(sid);
        score.setScore(score.getScore() + l);
        putInfo(score);
        return score.getScore();
    }

    public long addScore0(int l, String sid) {
        UserScore score = getUserInfo(sid);
        score.setScore0(score.getScore0() + l);
        putInfo(score);
        return score.getScore();
    }

    @AutoStand
    FatherMapper fatherMapper;

    public Father getFather(String sid) {
        return getFather(sid, false);
    }

    public Father getFather(String sid, boolean compulsion) {
        Father father = fatherMapper.selectById(sid);
        if (father == null && compulsion) {
            father = new Father();
            father.setSid(sid);
            fatherMapper.insert(father);
        }
        return father;
    }

}
