package Project.services.player;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @author github.kloping
 */
@Data
@Accessors(chain = true)
@ToString
@EqualsAndHashCode
public class Growth {
    /**
     * 直到time时间戳失效
     */
    private Long time;
    /**
     * 值
     */
    private Number value;
    /**
     * 作用于
     */
    private Long qid;
    /**
     * 类型
     */
    private String type;
}
