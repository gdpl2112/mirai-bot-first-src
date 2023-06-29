package io.github.kloping.gb.spring.dao;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.github.kloping.gb.finals.FinalConfig;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;


/**
 * @author github-kloping
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class UserScore implements Serializable {
    private Long score = 1000L;
    private Long times = 0L;
    private Long fz = 0L;
    @JSONField(name = "times_")
    private Long sTimes = 0L;
    private Long days = 0L;
    @JSONField(name = "score_")
    private Long sScore = 200L;
    private Long K = -1L;
    private Long day = -1L;
    private Long timesDay = -1L;
    @TableId
    private String id;
    private Long earnings = 0L;
    private Long debuffs = 0L;

    public UserScore record(Number s0) {
        if (s0.longValue() > 0) {
            earnings += s0.longValue();
        } else {
            debuffs += s0.longValue();
        }
        return this;
    }

    public UserScore addScore(Number s0) {
        this.score += s0.longValue();
        return this;
    }

    public UserScore addSScore(Number s0) {
        this.sScore += s0.longValue();
        return this;
    }

    public void addFz(int i) {
        fz += i;
    }

    public boolean isMaxEarnings() {
        return getEarnings() + getDebuffs() >= FinalConfig.MAX_EARNINGS;
    }
}