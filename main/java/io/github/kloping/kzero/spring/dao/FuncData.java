package io.github.kloping.kzero.spring.dao;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * @author github.kloping
 */
@Data
public class FuncData {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String bid;
    private String tid;
    private String type;
    private Integer ftype;
}
