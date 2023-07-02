package io.github.kloping.gb.spring.dao;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author github-kloping
 */
@Data
@Accessors(chain = true)
public class TradingRecord {
    @TableField("`type0`")
    private Type0 type0;
    @TableField("`type1`")
    private Type1 type1;
    @TableField("`from`")
    private String from;
    @TableField("`to`")
    private String to;
    @TableField("`main`")
    private String main;
    @TableField("`now`")
    private Long now;
    @TableField("`many`")
    private Long many;
    @TableField("`desc`")
    private String desc;
    @TableField("`time`")
    private Long time = System.currentTimeMillis();

    @JSONField(name = "type0")
    public void setType0(String type0) {
        this.type0 = Type0.valueOf(type0);
    }

    @JSONField(name = "type1")
    public void setType1(String type1) {
        this.type1 = Type1.valueOf(type1);
    }

    public enum Type0 {
        gold("gold"), score("score");
        final String v;

        Type0(String v) {
            this.v = v;
        }
    }

    public enum Type1 {
        lost("lost"), add("add");
        final String v;

        Type1(String v) {
            this.v = v;
        }
    }
}
