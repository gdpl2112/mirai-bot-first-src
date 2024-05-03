package io.github.kloping.kzero.hwxb.event;

import io.github.kloping.kzero.hwxb.dto.Contact;
import lombok.Data;

/**
 * @author github.kloping
 */
@Data
public class FriendMessageEvent extends MessageEvent {
    @Override
    public Contact getSubject() {
        return getFrom();
    }

    @Override
    public String getContactType() {
        return "FRIEND";
    }
}
