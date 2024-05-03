package io.github.kloping.kzero.hwxb.event;

import com.alibaba.fastjson.annotation.JSONField;
import io.github.kloping.kzero.hwxb.dto.Contact;
import lombok.Data;

/**
 * @author github.kloping
 */
@Data
public class MessageEvent {
    private Contact to;
    private String type;
    private String content;
    private Boolean isSystemEvent;
    private Boolean isMentioned;
    private Boolean isMsgFromSelf;
}
