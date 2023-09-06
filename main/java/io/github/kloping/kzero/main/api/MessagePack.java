package io.github.kloping.kzero.main.api;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author github.kloping
 */
@Data
@Accessors(chain = true)
public class MessagePack {
    public MessagePack() {
    }

    public MessagePack(MessageType type, String sendId, String subjectId, String msg) {
        this.type = type;
        this.senderId = sendId;
        this.subjectId = subjectId;
        this.msg = msg;
    }

    private MessageType type;
    private String senderId;
    private String subjectId;
    private String msg;
    private Object raw;
}
