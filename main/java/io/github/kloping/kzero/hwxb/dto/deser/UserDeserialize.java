package io.github.kloping.kzero.hwxb.dto.deser;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import io.github.kloping.kzero.hwxb.dto.FriendOrMember;
import io.github.kloping.kzero.hwxb.dto.Group;

import java.lang.reflect.Type;

/**
 * @author github.kloping
 */
public class UserDeserialize implements ObjectDeserializer {
    @Override
    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        String value = parser.parseObject(String.class);
        JSONObject jo = JSON.parseObject(value);
        if (jo.containsKey("topic")) {
            return (T) jo.toJavaObject(Group.class);
        } else return (T) jo.toJavaObject(FriendOrMember.class);
    }

    @Override
    public int getFastMatchToken() {
        return 0;
    }
}
