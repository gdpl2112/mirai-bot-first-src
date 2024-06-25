package io.github.kloping.kzero.bot.database;


import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import io.github.kloping.spt.annotations.AutoStand;
import io.github.kloping.spt.annotations.Entity;
import io.github.kloping.kzero.spring.dao.Father;
import io.github.kloping.kzero.spring.dao.GroupConf;
import io.github.kloping.kzero.spring.mapper.FatherMapper;
import io.github.kloping.kzero.spring.mapper.GroupConfMapper;

/**
 * @author github-kloping
 */
@Entity
public class DataBase {

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
