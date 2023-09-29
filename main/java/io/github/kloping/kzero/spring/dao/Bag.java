package io.github.kloping.kzero.spring.dao;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * @author github.kloping
 */
@Data
public class Bag {
    @TableId
    private Integer id;
    private Integer oid;
    private String qid;
    private Long time;
    private String desc = "无描述";
    private Integer state = 0;
}
