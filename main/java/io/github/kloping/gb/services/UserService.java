package io.github.kloping.gb.services;

import io.github.kloping.MySpringTool.annotations.AutoStand;
import io.github.kloping.MySpringTool.annotations.Entity;
import io.github.kloping.gb.spring.dao.UserScore;
import io.github.kloping.gb.spring.mapper.UserScoreMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author github.kloping
 */
@Entity
public class UserService {
    public static final Integer MAX = 15;
    public static Integer INDEX = 0;
    public static final List<String> CLOSED = new CopyOnWriteArrayList<>();
    public Map<String, UserScore> temp = new HashMap<>();

    public UserService() {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                temp.forEach((id, us) -> {
                    mapper.updateById(us);
                });
            }
        });
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
        if (temp.containsKey(id)) return temp.get(id);
        UserScore score = mapper.selectById(id);
        if (force && score == null) {
            score = new UserScore();
            score.setId(id);
            mapper.insert(score);
        }
        temp.put(id, score);
        return score;
    }

    /**
     * 保存数据
     *
     * @param user
     * @return
     */
    public synchronized boolean apply(UserScore user) {
        if (INDEX++ % MAX == 0) {
            return mapper.updateById(user) > 0;
        }
        return temp.put(user.getId(), user) == null;
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
                return "积分不足!";
            }
        }
        return "取积分成功!";
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
                return "积分不足!";
            }
        }
        return "存积分成功!";
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
        if (score2 == null) return null;
        if (score1.getScore() < m0) {
            return "积分不足!";
        } else {
            score1.addScore(-m0);
            score2.addScore(m0);
            apply(score1);
            apply(score2);
        }
        return "转让完成!";
    }
}
