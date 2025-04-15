package io.github.kloping.kzero.aigame;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author github kloping
 * @date 2025/4/15-10:19
 */
@Data
@Accessors(chain = true)
public class ResData {
    private Integer status;
    private String type;
    private String data;
}
