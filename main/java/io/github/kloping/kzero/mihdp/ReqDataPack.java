package io.github.kloping.kzero.mihdp;

import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.HashMap;
import java.util.Map;

/**
 * @author github.kloping
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class ReqDataPack {
    private String action;

    private String id;

    private String bot_id;
    private String env_type;
    private String env_id = "";
    private String sender_id;
    private String content;

    private Long time = 0L;

    private Map<String, Object> args = new HashMap<>();

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
