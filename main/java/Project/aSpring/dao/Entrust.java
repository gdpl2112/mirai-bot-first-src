package Project.aSpring.dao;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * 委托实体
 *
 * @author github.kloping
 */
@Data
public class Entrust {
    @TableId
    private Long qid;
    private Long gid;
    private Long t0;
    /**
     * 时间type
     */
    private Integer type0;
    /**
     * 物品type
     */
    private Integer type1;

    public String desc() {
        return String.format("%s的%s",
                type0 == 1 ? "12小时" :
                        type0 == 2 ? "一天" :
                                type0 == 4 ? "2天" : "未知时间"
                , type1 == 0 ? "金魂币" :
                        type1 == 1 ? "大瓶经验" :
                                type1 == 2 ? "时光胶囊" : "未知物品"
        );
    }
}
