package io.github.kloping.kzero.hwxb.event;

import com.alibaba.fastjson.JSONObject;
import io.github.kloping.kzero.hwxb.dto.Contact;
import lombok.Data;

/**
 * @author github.kloping
 */
@Data
public abstract class MessageEvent extends MetaEvent<String> {
    private Contact from;
    private Contact to;

    public abstract Contact getSubject();

    public abstract String getContactType();
}
