package Entitys;

import com.alibaba.fastjson.annotation.JSONField;
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
public class UScore implements Serializable {
    private Long score = Long.valueOf(1000);
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
    private Long who;
}