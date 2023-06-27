package io.github.kloping.gb.services;

import io.github.kloping.MySpringTool.annotations.AutoStand;
import io.github.kloping.MySpringTool.annotations.Entity;
import io.github.kloping.gb.Resources;
import io.github.kloping.gb.Utils;
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

    public UserService() {
    }

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
                return Resources.SCORE_NOT_ENOUGH;
            }
        }
        return Resources.SUCCESS;
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
                return Resources.SCORE_NOT_ENOUGH;
            }
        }
        return Resources.SUCCESS;
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
        if (score2 == null) return Resources.NOT_REGISTERED;
        if (score1.getScore() < m0) {
            return Resources.SCORE_NOT_ENOUGH;
        } else {
            score1.addScore(-m0);
            score2.addScore(m0);
            apply(score1);
            apply(score2);
        }
        return Resources.SUCCESS;
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
        return Resources.ROBBERY_FAILED;
    }
}
