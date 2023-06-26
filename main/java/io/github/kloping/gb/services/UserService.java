package io.github.kloping.gb.services;

import io.github.kloping.MySpringTool.annotations.AutoStand;
import io.github.kloping.MySpringTool.annotations.Entity;
import io.github.kloping.gb.spring.dao.UserScore;
import io.github.kloping.gb.spring.mapper.UserScoreMapper;

/**
 * @author github.kloping
 */
@Entity
public class UserService {
    @AutoStand
    UserScoreMapper mapper;

    public UserScore getUserScore(String id) {
        return getUserScore(id, false);
    }

    public UserScore getUserScore(String id, boolean force) {
        UserScore score = mapper.selectById(id);
        if (force && score == null) {
            score = new UserScore();
            score.setId(id);
            mapper.insert(score);
        }
        return score;
    }
}
