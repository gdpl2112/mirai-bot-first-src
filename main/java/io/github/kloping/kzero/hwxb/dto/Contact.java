package io.github.kloping.kzero.hwxb.dto;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import io.github.kloping.kzero.hwxb.dto.deser.UserDeserialize;
import lombok.Data;

/**
 * @author github.kloping
 */
@Data
public class Contact {
    @JSONField(name = "_events")
    private JSONObject events;
    @JSONField(name = "_eventsCount")
    private Integer eventsCount;
    private String id;
    @JSONField(deserializeUsing = UserDeserialize.class)
    private User payLoad;

    public boolean isEmpty() {
        return events == null || eventsCount == null || payLoad == null || id == null;
    }
}
