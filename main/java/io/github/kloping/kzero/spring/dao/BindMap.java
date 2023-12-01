package io.github.kloping.kzero.spring.dao;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author github.kloping
 */
@Data
@Accessors(chain = true)
public class BindMap {
    /**
     * bot id
     */
    private String bid;
    /**
     * bot id 下 的id
     */
    private String sid;
    /**
     * 映射为id
     */
    private String tid;
}
