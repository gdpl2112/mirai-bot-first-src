package io.github.kloping.kzero.bot.services;


import io.github.kloping.MySpringTool.annotations.AutoStand;
import io.github.kloping.MySpringTool.annotations.Entity;
import io.github.kloping.kzero.bot.database.DataBase;
import io.github.kloping.kzero.spring.dao.UserScore;
import io.github.kloping.kzero.spring.mapper.UserScoreMapper;
import io.github.kloping.rand.RandomUtils;

import java.util.List;


/**
 * @author github-kloping
 */
@Entity
public class UserService {
    @AutoStand
    DataBase dataBase;

    public String selectInfo(String sid) {
        StringBuilder str = new StringBuilder();
        UserScore lll = dataBase.getUserInfo(sid);
        str.append("剩的积分:").append(lll.getScore()).append("\r\n");
        str.append("存的积分:").append(lll.getScore0());
        return str.toString();
    }

    public String getScore(String sid, int num) {
        long l1 = dataBase.getUserInfo(sid).getScore0().longValue();
        if (l1 >= num) {
            dataBase.addScore0(-num, sid);
            dataBase.addScore(num, sid);
            return "取积分成功";
        } else {
            return "存的积分不足:" + num + "\n 你存的积分:" + l1;
        }
    }

    public String putScore(String sid, int num) {
        long l1 = dataBase.getUserInfo(sid).getScore();
        if (l1 >= num) {
            dataBase.addScore0(num, sid);
            dataBase.addScore(-num, sid);
            return "存积分成功";
        } else {
            return "积分不足:" + num + "\n 你剩余积分:" + l1;
        }
    }

    public String getScoreTo(String sid, String tid, int num) {
        long l1 = dataBase.getUserInfo(sid).getScore();
        if (l1 >= num) {
            dataBase.addScore(-num, sid);
            dataBase.addScore(num, tid);
            return "积分转让成功";
        } else {
            return "存的积分不足:" + num + "\n 你存的积分:" + l1;
        }
    }

    @AutoStand
    UserScoreMapper userScoreMapper;

    public String scorePh(Integer s0) {
        List<UserScore> list = userScoreMapper.phScore(s0);
        StringBuilder sb = new StringBuilder();
        int na = 0;
        for (UserScore score : list) {
            ++na;
            String id = score.getId();
            sb.append("第").append(na).append(": ").append(id).append("=>\n\t").append(score.getScore()).append("积分\n");
        }
        return sb.toString().isEmpty() ? "暂无记录" : sb.toString().trim();
    }

    public String robbery(String sid, String tid) {
        int lI = dataBase.getUserInfo(sid).getScore();
        int lY = dataBase.getUserInfo(sid).getScore();
        int fI = dataBase.getUserInfo(sid).getFz();
        if (lI > 60) {
            if (lY > 60) {
                if (fI < 12) {
                    int l = RandomUtils.RANDOM.nextInt(20) + 40;
                    dataBase.addScore(l, sid);
                    dataBase.addScore(-l, tid);
                    dataBase.addFz(1, sid);
                    return "成功打劫了" + l + "积分!\n增加1指数";
                } else {
                    return "打劫次数上限!";
                }
            } else {
                return "对方无积分!";
            }
        } else {
            return "积分不足!";
        }
    }
}
