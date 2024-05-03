package io.github.kloping.kzero.hwxb.dto.dao;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author github.kloping
 */
@Data
@Accessors(chain = true)
public class MsgPack {
    private String to;
    private Boolean isRoom = false;
    private MsgData[] data;
}
