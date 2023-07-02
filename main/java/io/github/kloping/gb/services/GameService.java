package io.github.kloping.gb.services;

import io.github.kloping.MySpringTool.annotations.AutoStand;
import io.github.kloping.MySpringTool.annotations.Entity;
import io.github.kloping.gb.ImageManager;
import io.github.kloping.gb.drawers.Drawer;
import io.github.kloping.gb.finals.FinalFormat;
import io.github.kloping.gb.game.GameConfig;
import io.github.kloping.gb.spring.dao.PersonInfo;
import io.github.kloping.gb.spring.dao.WhInfo;
import io.github.kloping.gb.spring.mapper.PersonInfoMapper;
import io.github.kloping.gb.spring.mapper.WhInfoMapper;

/**
 * @author github.kloping
 */
@Entity
public class GameService {

    @AutoStand
    PersonInfoMapper personInfoMapper;

    @AutoStand
    WhInfoMapper whInfoMapper;

    public WhInfo getWhInfo(String sid, Integer p) {
        WhInfo info = whInfoMapper.selectById(sid);
        if (info == null) {
            info = new WhInfo();
            info.setQid(sid);
            info.setP(p);
            whInfoMapper.insert(info);
        }
        return info;
    }

    public PersonInfo getPersonInfo(String sid) {
        PersonInfo info = personInfoMapper.selectById(sid);
        if (info == null) {
            info = new PersonInfo();
            info.setName(sid);
            info.setP(1);
            personInfoMapper.insert(info);
        }
        return info;
    }

    @AutoStand
    GameConfig config;

    public String info(String sid) {
        PersonInfo is = getPersonInfo(sid);
        WhInfo wi = getWhInfo(sid, is.getP());
        StringBuilder sb = new StringBuilder();
        if (!is.getSname().isEmpty() && wi.getLevel() >= 90) {
            sb.append(String.format(FinalFormat.FORMAT_IMAGE,
                    Drawer.createFont(is.getSname() + GameConfig.getFH(wi.getLevel())
                    )));
        }
        long n = wi.getWh();
        if (n <= 0) {
            sb.append("你的武魂:暂未获得").append("\r\n");
        } else {
            sb.append("你的武魂:" + config.ID_2_NAME_MAPS.get(n)).append("\r\n");
            sb.append(ImageManager.getImgPathById((int) n)).append("\r\n");
        }
        return sb + String.format(FinalFormat.FORMAT_IMAGE, Drawer.drawInfo(wi, is));
    }
}
