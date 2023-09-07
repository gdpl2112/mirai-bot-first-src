package io.github.kloping.kzero.spring.dao;

import com.baomidou.mybatisplus.annotation.TableId;
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
    @TableId
    private String id;
    private Long score = 1000L;
    private Long score0 = 200L;
    private Integer day = 0;
    private Integer days = 0;
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
}