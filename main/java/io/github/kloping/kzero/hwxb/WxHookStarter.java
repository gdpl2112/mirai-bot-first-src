package io.github.kloping.kzero.hwxb;

import io.github.kloping.arr.ArrDeSerializer;
import io.github.kloping.common.Public;
import io.github.kloping.kzero.gsuid.GsuidClient;
import io.github.kloping.kzero.hwxb.controller.WxBotEventRecv;
import io.github.kloping.kzero.hwxb.dto.Group;
import io.github.kloping.kzero.hwxb.dto.User;
import io.github.kloping.kzero.hwxb.dto.dao.MsgData;
import io.github.kloping.kzero.hwxb.event.GroupMessageEvent;
import io.github.kloping.kzero.hwxb.event.MessageEvent;
import io.github.kloping.kzero.hwxb.event.MetaEvent;
import io.github.kloping.kzero.main.DevMain;
import io.github.kloping.kzero.main.api.*;
import io.github.kloping.kzero.mihdp.MihdpClient;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;
import java.util.*;

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
            public Class<?> getStartClass() {
                return DevMain.class;
            }

            @Override
            public String getId() {
                return ID;
            }

            @Override
            public KZeroBotAdapter getAdapter() {
                return new KZeroBotAdapter() {
                    private GroupMessageEvent ue;

                    private @Nullable GroupMessageEvent getGroupMessageEvent(String targetId) {
                        if (ue != null && ue.getSubject().getId().equalsIgnoreCase(targetId)) return ue;
                        GroupMessageEvent event = null;
                        for (MetaEvent value : SID2EVENT.values()) {
                            if (value instanceof GroupMessageEvent) {
                                event = (GroupMessageEvent) value;
                                if (event.getSubject().getId().equalsIgnoreCase(targetId)) {
                                    break;
                                }
                            }
                        }
                        return event;
                    }

                    @Override
                    public boolean sendMessage(MessageType type, String targetId, Object obj) {
                        GroupMessageEvent event = getGroupMessageEvent(targetId);
                        if (event == null) return false;
                        List<MsgData> list = (List) getSerializer().deserialize(obj.toString());
                        event.sendMessage(list.toArray(new MsgData[0]));
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
                        List<MsgData> list = (List) getSerializer().deserialize(data.toString());
                        event.sendMessage(list.toArray(new MsgData[0]));
                    }

                    @Override
                    public String getNameCard(String sid, String tid) {
                        try {
                            GroupMessageEvent event = getGroupMessageEvent(tid);
                            Group group = (Group) event.getRoom().getPayLoad();
                            for (User user : group.getMemberList()) {
                                if (user.getId().equals(sid)) return user.getName();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return "默认昵称";
                    }

                    @Override
                    public List<String> getMembers(String tid) {
                        GroupMessageEvent event = getGroupMessageEvent(tid);
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
                                    return user.getAvatar() + "&token=" + value.getAuth().getToken();
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
                                        String u0 = String.format("%s:%s/", metaEvent.getAuth().getSelf(),
                                                metaEvent.getAuth().getPort());
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
            MessageEvent<String> event = (MessageEvent) r;
            offer(event);

            MessagePack pack = new MessagePack();
            pack.setSubjectId(event.getSubject().getId());
            pack.setSenderId(event.getFrom().getId());
            pack.setType(event.getContactType().equals("GROUP") ? MessageType.GROUP : MessageType.FRIEND);
            pack.setRaw(r);
            pack.setMsg(event.getContent());
            handler.onMessage(pack);

            WxHookExtend0.recv(event);

            Public.EXECUTOR_SERVICE.submit(()-> ExtendServiceSa.handle(event));

            return "{}";
        });
        RECVS.put("system_event_login", r -> {
            SID2EVENT.put("", r);
            return "{}";
        });

        GsuidClient.INSTANCE.addListener(ID, WxHookExtend0::onMessageG);
        MihdpClient.INSTANCE.listeners.put(ID, WxHookExtend0::onMessage);
    }

    private final int max = 30;

    private Queue<String> queue = new ArrayDeque<>(max);

    private synchronized void offer(MessageEvent<String> event) {
        String id = event.getId();
        if (queue.size() >= max) {
            String oid = queue.poll();
            SID2EVENT.remove(oid);
        }
        queue.offer(id);
        SID2EVENT.put(event.getId(), event);
    }
}
