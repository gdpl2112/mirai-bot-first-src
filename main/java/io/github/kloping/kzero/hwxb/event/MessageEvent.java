package io.github.kloping.kzero.hwxb.event;

import com.alibaba.fastjson.JSONObject;
import io.github.kloping.kzero.hwxb.dto.Contact;
import io.github.kloping.kzero.hwxb.dto.dao.MsgData;
import lombok.Data;

/**
 * @author github.kloping
 */
@Data
public abstract class MessageEvent<T extends Object> extends MetaEvent<T> {
    private Contact from;
    private Contact to;

    public MessageEvent(MetaEvent<T> event) {
        super(event.getId());
        this.setContent(event.getContent());
        this.setType(event.getType());
        this.setIsSystemEvent(event.getIsSystemEvent());
        this.setIsMsgFromSelf(event.getIsMsgFromSelf());
        this.setIsMentioned(event.getIsMentioned());
        this.setRequest(event.getRequest());
        this.setAuth(event.getAuth());
    }

    public abstract Contact getSubject();

    public abstract String getContactType();

    public abstract JSONObject sendMessage(MsgData... data);

    public JSONObject sendMessage(String msg) {
        return sendMessage(new MsgData(msg, "text"));
    }
}
