package io.github.kloping.kzero.game.database;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import io.github.kloping.MySpringTool.annotations.AutoStand;
import io.github.kloping.MySpringTool.annotations.Entity;
import io.github.kloping.kzero.game.commons.GameUserInfo;
import io.github.kloping.kzero.game.commons.GameUserInfoOperate;
import io.github.kloping.kzero.spring.dao.PersonInfo;
import io.github.kloping.kzero.spring.dao.WhInfo;
import io.github.kloping.kzero.spring.mapper.PersonInfoMapper;
import io.github.kloping.kzero.spring.mapper.WhInfoMapper;

/**
 * @author github.kloping
 */
@Entity
public class GameDataBase {
    @AutoStand
    WhInfoMapper whInfoMapper;
    @AutoStand
    PersonInfoMapper personInfoMapper;

    public GameUserInfo getGameUserInfo(String sid) {
        PersonInfo personInfo = getPersonInfo(sid, true);
        int p = personInfo.getP();
        WhInfo winfo = getWhInfo(sid, p);
        if (winfo == null && p == 1) {
            winfo = new WhInfo();
            winfo.setSid(sid).setP(p);
            whInfoMapper.insert(winfo);
        }
        return new GameUserInfo(personInfo, winfo);
    }

    public WhInfo getWhInfo(String sid, int p) {
        QueryWrapper<WhInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("sid", sid);
        queryWrapper.eq("p", p);
        return whInfoMapper.selectOne(queryWrapper);
    }

    public PersonInfo getPersonInfo(String sid) {
        return getPersonInfo(sid, false);
    }

    public PersonInfo getPersonInfo(String sid, boolean compulsion) {
        PersonInfo personInfo = personInfoMapper.selectById(sid);
        if (!compulsion) return personInfo;
        else if (personInfo == null) {
            personInfo = new PersonInfo().setSid(sid);
            personInfoMapper.insert(personInfo);
        }
        return personInfo;
    }

    public int update(GameUserInfo info) {
        int i = 0;
        i += personInfoMapper.updateById(info.getPersonInfo());
        UpdateWrapper<WhInfo> wrapper = new UpdateWrapper<>();
        wrapper.eq("p", info.getWhInfo().getP());
        wrapper.eq("sid", info.getWhInfo().getSid());
        i += whInfoMapper.update(info.getWhInfo(), wrapper);
        return i;
    }

    public int operate(String sid, GameUserInfoOperate operate) {
        GameUserInfo gameUserInfo = getGameUserInfo(sid);
        operate.operate(gameUserInfo);
        return update(gameUserInfo);
    }
}
