package io.github.kloping.kzero.spring.dao;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author github.kloping
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class SweatherData {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String bid;
    private String sid;
    private String type;
    private String gid;
    private String address;
    /**
     * 上次的数据
     */
    private String d0;
}
