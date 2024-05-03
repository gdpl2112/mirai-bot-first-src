package io.github.kloping.kzero.hwxb.event;

import io.github.kloping.kzero.hwxb.dto.Contact;
import lombok.Data;

/**
 * @author github.kloping
 */
@Data
public class GroupMessageEvent extends MessageEvent {
    private Contact room;

    @Override
    public Contact getSubject() {
        return room;
    }

    public GroupMessageEvent(Contact room) {
        this.room = room;
    }

    @Override
    public String getContactType() {
        return "GROUP";
    }
}
