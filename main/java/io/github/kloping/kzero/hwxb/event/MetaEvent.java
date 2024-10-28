package io.github.kloping.kzero.hwxb.event;

import com.alibaba.fastjson.annotation.JSONField;
import io.github.kloping.kzero.hwxb.WxAuth;
import lombok.Data;

import javax.servlet.http.HttpServletRequest;

/**
 * @author github.kloping
 */
@Data
public class MetaEvent<T extends Object> {
    private final String id;

    public MetaEvent(String id) {
        this.id = id;
    }

    private T content;
    private String type;
    private Boolean isSystemEvent;
    private Boolean isMentioned;
    private Boolean isMsgFromSelf;

    @JSONField(serialize = false, deserialize = false)
    private HttpServletRequest request;
    @JSONField(serialize = false, deserialize = false)
    private WxAuth auth;
}
