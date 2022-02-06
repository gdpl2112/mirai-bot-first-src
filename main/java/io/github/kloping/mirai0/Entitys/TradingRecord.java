package io.github.kloping.mirai0.Entitys;

import Project.dataBases.OtherDatabase;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class TradingRecord {
    public enum Type0 {
        gold("gold"), score("score");
        String v;

        Type0(String v) {
            this.v = v;
        }
    }

    public enum Type1 {
        lost("lost"), add("add");
        String v;

        Type1(String v) {
            this.v = v;
        }
    }

    @JSONField(name = "type0")
    public void setType0(String type0) {
        this.type0 = Type0.valueOf(type0);
    }
    @JSONField(name = "type1")
    public void setType1(String type1) {
        this.type1 = Type1.valueOf(type1);
    }

    public TradingRecord setType0(Type0 type0) {
        this.type0 = type0;
        return this;
    }

    public TradingRecord setType1(Type1 type1) {
        this.type1 = type1;
        return this;
    }

    public Type0 type0;
    public Type1 type1;

    public long from;
    public long to;
    public long main;
    public Number now;
    public Number many;
    public String desc;

    public long time = System.currentTimeMillis();


    public static final synchronized List<TradingRecord> getInstance(long q) {
        return OtherDatabase.getList(q);
    }
}
