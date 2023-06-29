package io.github.kloping.gb.services;

import io.github.kloping.MySpringTool.annotations.AutoStand;
import io.github.kloping.MySpringTool.annotations.Entity;
import io.github.kloping.date.DateUtils;
import io.github.kloping.gb.Utils;
import io.github.kloping.gb.alone.Mora;
import io.github.kloping.gb.drawers.Drawer;
import io.github.kloping.gb.finals.FinalConfig;
import io.github.kloping.gb.finals.FinalFormat;
import io.github.kloping.gb.finals.FinalStrings;
import io.github.kloping.gb.spring.dao.UserScore;
import io.github.kloping.gb.spring.mapper.SingListMapper;
import io.github.kloping.gb.spring.mapper.UserScoreMapper;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static io.github.kloping.gb.Utils.RANDOM;
import static io.github.kloping.gb.finals.FinalStrings.MAX_EARNINGS_TIPS;

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
                    long l = RANDOM.nextInt(20) + 40;
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
        if (user.isMaxEarnings()) return MAX_EARNINGS_TIPS;
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

    /**
     * 捐款
     *
     * @param qid
     * @return
     */
    public String donate(String qid) {
        UserScore user = getUserScore(qid);
        if (user.getScore() < 140) return "积分不足";
        if (user.isMaxEarnings()) return MAX_EARNINGS_TIPS;
        int l = RANDOM.nextInt(40) + 40;
        int r = RANDOM.nextInt(3) + 1;
        user.addScore(-l).record(-l).addFz(-r);
        apply(user);
        return String.format("捐款了%s积分,降低了%s点犯罪指数", l, r);
    }

    /**
     * 收益
     *
     * @param id
     * @return
     */
    public String earnings(String id) {
        UserScore score = getUserScore(id);
        return String.format(FinalFormat.EARNINGS_TIPS_FORMAT, score.getEarnings(), score.getDebuffs());
    }


    @AutoStand
    SingListMapper singListMapper;

    /**
     * 签到
     *
     * @param id
     * @return
     */
    public String sign(String id) {
        synchronized (id) {
            UserScore user = getUserScore(id);
            Integer day = DateUtils.getDay();
            if (user.getDay().intValue() == day) {
                return "签到失败,你今天已经签到过了!!";
            } else {
                user.setFz(0L);
                user.setDay(Long.valueOf(day));
                user.setDays((user.getDays().intValue() + 1L));
                user.addScore(100);
                apply(user);
                singListMapper.insert(id, day.toString(), System.currentTimeMillis());
                Object[] lines = regDay(id, day.toString());
                String line = lines[0].toString();
                Integer st = Integer.valueOf(lines[1].toString());
                if (line.isEmpty()) {
                    return "\n签到成功!\n增加100积分\n犯罪指数清除\n累计签到:" + user.getDays() + "次";
                } else {
                    return "\n签到成功!\n增加100积分\n犯罪指数清除\n累计签到:" + user.getDays() + "次\n"
                            + Drawer.drawLine("第" + Utils.trans(st) + "签") + "\n" + line;
                }
            }
        }
    }

    private Object[] regDay(String id, String day) {
        int r = singListMapper.selectCountByDay(day);
        switch (r) {
            case 1:
                apply(getUserScore(id).addScore(100));
                return new Object[]{"额外获得100积分", r};
            case 2:
                apply(getUserScore(id).addScore(50));
                return new Object[]{"额外获得50积分", r};
            case 3:
                apply(getUserScore(id).addScore(25));
                return new Object[]{"额外获得25积分", r};
            case 10:
                apply(getUserScore(id).addScore(150));
                return new Object[]{"额外获得150积分", r};
            default:
                return new Object[]{"", r};
        }
    }

}
