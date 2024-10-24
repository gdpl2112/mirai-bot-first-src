package io.github.kloping.kzero.hwxb.event;

import io.github.kloping.kzero.hwxb.dto.Contact;
import lombok.Data;

/**
 * @author github.kloping
 */
@Data
public class FriendMessageEvent extends MessageEvent {
    private Contact room;

    public FriendMessageEvent(MetaEvent<?> event, Contact room) {
        super(event);
        this.room = room;
    }

    @Override
    public Contact getSubject() {
        return getFrom();
    }

    @Override
    public String getContactType() {
        return "FRIEND";
    }
}
