package io.github.kloping.kzero.awxb;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.github.gdpl2112.onebot.v12.action.Action;
import io.github.gdpl2112.onebot.v12.action.MessageParams;
import io.github.gdpl2112.onebot.v12.contact.Member;
import io.github.gdpl2112.onebot.v12.data.Message;
import io.github.gdpl2112.onebot.v12.data.MessageChain;
import io.github.gdpl2112.onebot.v12.data.MessageChainBuilder;
import io.github.gdpl2112.onebot.v12.event.MessageEvent;
import io.github.gdpl2112.onebot.v12.event.MetaEvent;
import io.github.kloping.judge.Judge;
import io.github.kloping.kzero.main.api.KZeroBotAdapter;
import io.github.kloping.kzero.main.api.MessagePack;
import io.github.kloping.kzero.main.api.MessageType;
import io.github.kloping.kzero.qqpd.GuildBotAdapter;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author github.kloping
 */
public class WxAdapter implements KZeroBotAdapter {

    private MetaEvent metaEvent;
    private WxSerializer serializer;

    @Override
    public boolean sendMessage(MessageType type, String targetId, Object msg) {
        MessageChain messages = null;
        if (msg instanceof String) {
            messages = serializer.deserialize(msg.toString());
        } else if (msg instanceof MessageChain) {
            messages = (MessageChain) msg;
        }
        Action action = new Action();
        MessageParams params = new MessageParams();
        params.setUserId(targetId);
        params.setDetailType(type == MessageType.GROUP ? "group" : "private");
        params.setMessage(messages.getMessages());
        action.setParams(params.asMap());
        return metaEvent.send(action).getRetcode() == 0;
    }

    @Override
    public void sendMessageByForward(MessageType type, String targetId, Object... objects) {
        for (Object object : objects) {
            sendMessage(type, targetId, object);
        }
    }

    @Override
    public void onResult(Method method, Object data, MessagePack pack) {
        if (data == null || Judge.isEmpty(data.toString())) return;
        MessageEvent event = (MessageEvent) pack.getRaw();
        if (data.getClass().isArray()) {
            String targetId = pack.getSubjectId();
            Object[] objects = (Object[]) data;
            for (Object msg : objects) {
                sendElement(msg, event);
            }
        } else if (data instanceof MessageChain) {
            event.sendMessage((MessageChain) data);
        } else {
            sendElement(data.toString(), event);
        }
    }

    private void sendElement(Object msg, MessageEvent event) {
        MessageChainBuilder builder = new MessageChainBuilder();
        for (Message message : serializer.ARR_DE_SERIALIZER.deserializer(msg.toString())) {
            if (message != null) builder.append(message);
        }
        event.sendMessage(builder.build());
    }

    private Member getMemberInfo(String sid, String tid) {
        Action action = new Action();
        action.setAction("get_group_member_info");
        Map map = new HashMap<>();
        map.put("group_id", tid);
        map.put("user_id", sid);
        action.setParams(map);
        JSONObject data = metaEvent.send(action).getData(JSONObject.class);
        if (data == null) return null;
        return data.toJavaObject(Member.class);
    }

    @Override
    public String getNameCard(String sid, String tid) {
        return getMemberInfo(sid, tid).getUserName();
    }

    @Override
    public List<String> getMembers(String tid) {
        Action action = new Action();
        action.setAction("get_group_member_list");
        Map map = new HashMap<>();
        map.put("group_id", tid);
        action.setParams(map);
        JSONArray ar = metaEvent.send(action).getData(JSONArray.class);
        List<String> list = new LinkedList<>();
        for (Object o : ar) {
            JSONObject jo = (JSONObject) o;
            list.add(jo.getString("user_name"));
        }
        return list;
    }

    @Override
    public String getAvatarUrl(String sid) {
        Action action = new Action();
        action.setAction("get_group_list");
        action.setParams(new HashMap<>());
        JSONArray ar = metaEvent.send(action).getData(JSONArray.class);
        for (Object o : ar) {
            JSONObject e = (JSONObject) o;
            Member member = getMemberInfo(sid, e.getString("group_id"));
            if (member != null) {
                return member.getAvatar();
            }
        }
        return "http://kloping.top/icon.jpg";
    }


    @Override
    public String getNameCard(String sid) {
        Action action = new Action();
        action.setAction("get_group_list");
        action.setParams(new HashMap<>());
        JSONArray ar = metaEvent.send(action).getData(JSONArray.class);
        for (Object o : ar) {
            JSONObject e = (JSONObject) o;
            Member member = getMemberInfo(sid, e.getString("group_id"));
            if (member != null) {
                return member.getUserName();
            }
        }
        return GuildBotAdapter.DEFAULT_NAME;
    }

    public WxAdapter(MetaEvent metaEvent, WxSerializer serializer) {
        this.metaEvent = metaEvent;
        this.serializer = serializer;
    }
}
