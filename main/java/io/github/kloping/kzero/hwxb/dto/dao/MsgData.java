package io.github.kloping.kzero.hwxb.dto.dao;

import lombok.Data;

/**
 * @author github.kloping
 */
@Data
public class MsgData {
    private String content;
    private String type;

    public MsgData(String content, String type) {
        this.content = content;
        this.type = type;
    }
}
