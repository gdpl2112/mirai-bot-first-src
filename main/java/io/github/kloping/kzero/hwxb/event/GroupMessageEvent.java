package io.github.kloping.kzero.hwxb.event;

import io.github.kloping.kzero.hwxb.dto.Contact;
import lombok.Data;

/**
 * @author github.kloping
 */
@Data
public class GroupMessageEvent extends MessageEvent {
    private Contact room;

    public GroupMessageEvent(MetaEvent<?> event, Contact room) {
        super(event);
        this.room = room;
    }

    @Override
    public Contact getSubject() {
        return room;
    }

    @Override
    public String getContactType() {
        return "GROUP";
    }
}
