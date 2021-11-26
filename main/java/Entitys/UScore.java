package Entitys;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;


@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class UScore implements Serializable {
    // private Number score=1000;
    //    private Number times=0;
    //    private Number fz=0;
    //    private Number times_=0;
    //    private Number days=0;
    //    private Number score_=200;
    //    private Number k=-1;
    //    private Number day=-1;
    //    private Number timesDay=-1;
    //    private Number who;
    private Long score = Long.valueOf(1000);
    private Long times = 0l;
    private Long fz = 0l;
    @JSONField(name = "times_")
    private Long sTimes = 0l;
    private Long days = 0L;
    @JSONField(name = "score_")
    private Long sScore = 200L;
    private Long K = -1L;
    private Long day = -1L;
    private Long timesDay = -1L;
    private Long who;
}