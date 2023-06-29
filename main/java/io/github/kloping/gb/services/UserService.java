package io.github.kloping.gb.services;

import io.github.kloping.MySpringTool.annotations.AutoStand;
import io.github.kloping.MySpringTool.annotations.Entity;
import io.github.kloping.gb.FinalConfig;
import io.github.kloping.gb.FinalStrings;
import io.github.kloping.gb.Utils;
import io.github.kloping.gb.alone.Mora;
import io.github.kloping.gb.spring.dao.UserScore;
import io.github.kloping.gb.spring.mapper.UserScoreMapper;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author github.kloping
 */
@Entity
public class UserService {
    public static final List<String> CLOSED = new CopyOnWriteArrayList<>();

    @AutoStand
    UserScoreMapper mapper;

    /**
     * 获取 用户 信息
     *
     * @param id
     * @return
     */
    public UserScore getUserScore(String id) {
        return getUserScore(id, false);
    }

    /**
     * 获取用户信息 强制 #不为null
     *
     * @param id
     * @param force
     * @return
     */
    public synchronized UserScore getUserScore(String id, boolean force) {
        UserScore score = mapper.selectById(id);
        if (force && score == null) {
            score = new UserScore();
            score.setId(id);
            mapper.insert(score);
        }
        return score;
    }

    /**
     * 保存数据
     *
     * @param user
     * @return
     */
    public synchronized boolean apply(UserScore user) {
        return mapper.updateById(user) > 0;
    }

    /**
     * 取积分
     *
     * @param id
     * @param num
     * @return
     */
    public String getScore(String id, long num) {
        if (num > 0) {
            if (CLOSED.contains(id)) return "账户锁定中...\r\n退出客户端登录后重试";
            UserScore score = getUserScore(id, true);
            if (score.getSScore() >= num) {
                score.addSScore(-num);
                score.addScore(num);
                apply(score);
            } else {
                return FinalStrings.SCORE_NOT_ENOUGH;
            }
        }
        return FinalStrings.SUCCESS;
    }

    /**
     * 存积分
     *
     * @param id
     * @param num
     * @return
     */
    public String putScore(String id, long num) {
        if (num > 0) {
            if (CLOSED.contains(id)) return "账户锁定中...\r\n退出客户端登录后重试";
            UserScore score = getUserScore(id, true);
            if (score.getScore() >= num) {
                score.addScore(-num);
                score.addSScore(num);
                apply(score);
            } else {
                return FinalStrings.SCORE_NOT_ENOUGH;
            }
        }
        return FinalStrings.SUCCESS;
    }

    /**
     * 积分转让
     *
     * @param qid 转让者
     * @param m0  数量
     * @param tid 被转让者
     * @return
     */
    public String transfer(String qid, Long m0, String tid) {
        if (m0 <= 0) return null;
        UserScore score1 = getUserScore(qid, true);
        UserScore score2 = getUserScore(tid);
        if (score2 == null) return FinalStrings.NOT_REGISTERED;
        if (score1.getScore() < m0) {
            return FinalStrings.SCORE_NOT_ENOUGH;
        } else {
            score1.addScore(-m0);
            score2.addScore(m0);
            apply(score1);
            apply(score2);
        }
        return FinalStrings.SUCCESS;
    }

    /**
     * 打劫
     *
     * @param s1 打劫者
     * @param s2 被打劫者
     * @return
     */
    public String robbery(String s1, String s2) {
        UserScore score1 = getUserScore(s1);
        UserScore score2 = getUserScore(s2);
        long lI = score1.getScore();
        long lY = score2.getScore();
        long fI = score1.getFz();
        if (lI > 60) {
            if (lY > 60) {
                if (fI < 12) {
                    long l = Utils.RANDOM.nextInt(20) + 40;
                    score1.addScore(l);
                    score1.addFz(1);
                    score2.addScore(-l);
                    apply(score1);
                    apply(score2);
                    return "成功打劫了" + l + "积分!\n增加1指数";
                }
            }
        }
        return FinalStrings.ROBBERY_FAILED;
    }

    public String mora(String id, String what) {
        UserScore user = getUserScore(id);
        long li = user.getScore();
        long l1 = Utils.getInteger(what, 0);
        Mora mora1 = Mora.findMora(what, 0);
        if (mora1 == null || mora1.getValue().isEmpty()) return "猜拳格式错误=> 猜拳 (石头/剪刀/布) (积分)";
        if (li < l1) return FinalStrings.SCORE_NOT_ENOUGH;
        if (l1 < 5) return "积分最小值:5";
        if (l1 > 1500) return "积分最大值:1500";
        if (user.isMaxEarnings()) return FinalStrings.MAX_EARNINGS_TIPS;
        Mora i = Mora.getRc(FinalConfig.MORA_WIN, FinalConfig.MORA_P, mora1);
        int p = mora1.reff(i);
        if (p == 0) {
            return "平局 我出的是" + i.getValue();
        } else if (p == -1) {
            user.addScore(-l1).record(-l1);
            apply(user);
            return "你输了 我出的是" + i.getValue() + "\n你输掉了:" + l1 + "积分";
        } else if (p == 1) {
            user.addScore(l1).record(l1);
            apply(user);
            return "你赢了 我出的是" + i.getValue() + "\n你获得了:" + l1 + "积分";
        }
        return FinalStrings.ERR_TIPS;
    }
}
