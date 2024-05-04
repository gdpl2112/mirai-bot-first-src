package io.github.kloping.kzero.hwxb;

import io.github.kloping.arr.ArrDeSerializer;
import io.github.kloping.kzero.hwxb.controller.WxBotEventRecv;
import io.github.kloping.kzero.hwxb.dto.Group;
import io.github.kloping.kzero.hwxb.dto.User;
import io.github.kloping.kzero.hwxb.dto.dao.MsgData;
import io.github.kloping.kzero.hwxb.dto.dao.MsgPack;
import io.github.kloping.kzero.hwxb.event.GroupMessageEvent;
import io.github.kloping.kzero.hwxb.event.MessageEvent;
import io.github.kloping.kzero.hwxb.event.MetaEvent;
import io.github.kloping.kzero.main.api.*;
import io.github.kloping.kzero.mihdp.MihdpClient;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author github.kloping
 */
public class WxHookStarter implements KZeroStater {
    public static final String ID = "wxh";

    public static WxHookStarter INSTANCE;

    public WxHookStarter() {
        INSTANCE = this;
    }

    private BotCreated botCreated;
    private BotMessageHandler handler;
    public static Map<String, WxBotEventRecv> RECVS = new HashMap<>();
    /**
     * gid 2 event
     */
    public static Map<String, MetaEvent> SID2EVENT = new HashMap<>();

    @Override
    public void setCreated(BotCreated listener) {
        this.botCreated = listener;
    }

    @Override
    public void setHandler(KZeroBot bot, BotMessageHandler handler) {
        this.handler = handler;
    }

    @Override
    public void run() {
        botCreated.created(this, new KZeroBot() {
            @Override
            public String getId() {
                return ID;
            }

            @Override
            public KZeroBotAdapter getAdapter() {
                return new KZeroBotAdapter() {
                    @Override
                    public boolean sendMessage(MessageType type, String targetId, Object obj) {
                        MsgPack msg = new MsgPack();
                        MessageEvent event = (MessageEvent) SID2EVENT.get(targetId);
                        msg.setTo(event.getSubject().getPayLoad().getName());
                        List<MsgData> list = (List) getSerializer().deserialize(obj.toString());
                        msg.setData(list.toArray(new MsgData[0]));
                        if (type == MessageType.GROUP) {
                            msg.setIsRoom(true);
                        }
                        event.getAuth().sendMessage(msg);
                        return true;
                    }

                    @Override
                    public void sendMessageByForward(MessageType type, String targetId, Object... objects) {
                        for (Object object : objects) {
                            sendMessage(type, targetId, object);
                        }
                    }

                    @Override
                    public void onResult(Method method, Object data, MessagePack pack) {
                        MessageEvent event = (MessageEvent) pack.getRaw();
                        MsgPack msg = new MsgPack();
                        msg.setTo(event.getSubject().getPayLoad().getName());
                        msg.setIsRoom(pack.getType() == MessageType.GROUP ? true : false);
                        List<MsgData> list = (List) getSerializer().deserialize(data.toString());
                        msg.setData(list.toArray(new MsgData[0]));
                        event.getAuth().sendMessage(msg);
                    }

                    @Override
                    public String getNameCard(String sid, String tid) {
                        GroupMessageEvent event = (GroupMessageEvent) SID2EVENT.get(tid);
                        Group group = (Group) event.getRoom().getPayLoad();
                        for (User user : group.getMemberList()) {
                            if (user.getId().equals(sid)) return user.getName();
                        }
                        return "默认昵称";
                    }

                    @Override
                    public List<String> getMembers(String tid) {
                        GroupMessageEvent event = (GroupMessageEvent) SID2EVENT.get(tid);
                        Group group = (Group) event.getRoom().getPayLoad();
                        List<String> list = new LinkedList<>();
                        for (User user : group.getMemberList()) {
                            list.add(user.getName());
                        }
                        return list;
                    }

                    @Override
                    public String getAvatarUrl(String sid) {
                        for (MetaEvent value : SID2EVENT.values()) {
                            if (value instanceof GroupMessageEvent) {
                                GroupMessageEvent event = (GroupMessageEvent) value;
                                Group group = (Group) event.getRoom().getPayLoad();
                                for (User user : group.getMemberList()) {
                                    return user.getAvatar() + "&token=" + value.getAuth().getWxAuth().getToken();
                                }
                            }
                        }
                        return KZeroBotAdapter.super.getAvatarUrl(sid);
                    }

                    @Override
                    public String getAvatarUrlConverted(String sid) {
                        return KZeroBotAdapter.super.getAvatarUrlConverted(sid);
                    }

                    @Override
                    public String getNameCard(String sid) {
                        return KZeroBotAdapter.super.getNameCard(sid);
                    }
                };
            }

            @Override
            public MessageSerializer getSerializer() {
                return new MessageSerializer() {
                    @Override
                    public String serialize(Object msg) {
                        return msg.toString();
                    }

                    private final ArrDeSerializer<MsgData> ARR_DE_SERIALIZER = new ArrDeSerializer<>();

                    {
                        ARR_DE_SERIALIZER.add(ArrDeSerializer.EMPTY_PATTERN, new ArrDeSerializer.Rule0<MsgData>() {
                            @Override
                            public MsgData deserializer(String s) {
                                return new MsgData(s, "text");
                            }
                        });
                        ARR_DE_SERIALIZER.add(PATTER_AT, new ArrDeSerializer.Rule0<MsgData>() {
                            @Override
                            public MsgData deserializer(String s) {
                                return new MsgData(s, "text");
                            }
                        });
                        ARR_DE_SERIALIZER.add(PATTER_PIC, new ArrDeSerializer.Rule0<MsgData>() {
                            @Override
                            public MsgData deserializer(String data) {
                                String path = data.substring(data.indexOf(":") + 1, data.length() - 1);
                                try {
                                    if (path.startsWith("http")) {
                                        return new MsgData(path, "fileUrl");
                                    } else {
                                        MetaEvent metaEvent = SID2EVENT.values().iterator().next();
                                        String u0 = String.format("%s:%s/", metaEvent.getAuth().getWxAuth().getSelf(), metaEvent.getAuth().getWxAuth().getPort());
                                        return new MsgData(path
                                                .replaceAll("\\\\", "/")
                                                .replace("./temp/", u0)
                                                , "fileUrl");
                                    }
                                } catch (Exception e) {
                                    System.err.println(path + "加载失败");
                                    e.printStackTrace();
                                }
                                return null;
                            }
                        });
                    }

                    @Override
                    public Object deserialize(String msg) {
                        return ARR_DE_SERIALIZER.deserializer(msg);
                    }
                };
            }

            @Override
            public Object getSelf() {
                return WxHookStarter.this;
            }
        });
        RECVS.put("text", r -> {
            MessageEvent event = (MessageEvent) r;
            MessagePack pack = new MessagePack();
            SID2EVENT.put(event.getSubject().getId(), event);
            pack.setSubjectId(event.getSubject().getId());
            pack.setSenderId(event.getFrom().getId());
            pack.setType(event.getContactType().equals("GROUP") ? MessageType.GROUP : MessageType.FRIEND);
            pack.setRaw(r);
            pack.setMsg(event.getContent());
            handler.onMessage(pack);
            WxHookExtend0.recv(event);
            return "{}";
        });
        RECVS.put("system_event_login", r -> {
            SID2EVENT.put("", r);
            return "{}";
        });
        MihdpClient.INSTANCE.listeners.put(ID, WxHookExtend0::onMessage);
    }
}
