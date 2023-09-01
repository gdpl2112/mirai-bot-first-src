package io.github.kloping.kzero.spring.dao;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * @author github.kloping
 */
@TableName("raffle")
public class RaffleE {
    @TableId
    private Long qid;
    private Integer c1;
    private Integer c2;

    public RaffleE addC1() {
        c1++;
        return this;
    }

    public RaffleE addC2() {
        c2++;
        return this;
    }

    public Long getQid() {
        return qid;
    }

    public void setQid(Long qid) {
        this.qid = qid;
    }

    public Integer getC1() {
        return c1;
    }

    public void setC1(Integer c1) {
        this.c1 = c1;
    }

    public Integer getC2() {
        return c2;
    }

    public void setC2(Integer c2) {
        this.c2 = c2;
    }
}
