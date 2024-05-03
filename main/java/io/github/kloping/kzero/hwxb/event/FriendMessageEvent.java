package io.github.kloping.kzero.hwxb.event;

import io.github.kloping.kzero.hwxb.dto.Contact;
import lombok.Data;

/**
 * @author github.kloping
 */
@Data
public class FriendMessageEvent extends MessageEvent {
    private Contact from;

    public FriendMessageEvent(Contact from) {
        this.from = from;
    }
}
