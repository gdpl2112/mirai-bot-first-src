package io.github.kloping.kzero.hwxb;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import io.github.kloping.file.FileUtils;
import io.github.kloping.kzero.gsuid.GsuidClient;
import io.github.kloping.kzero.gsuid.MessageData;
import io.github.kloping.kzero.gsuid.MessageOut;
import io.github.kloping.kzero.gsuid.MessageReceive;
import io.github.kloping.kzero.hwxb.controller.HandlerController;
import io.github.kloping.kzero.hwxb.dto.dao.MsgData;
import io.github.kloping.kzero.hwxb.event.GroupMessageEvent;
import io.github.kloping.kzero.hwxb.event.MessageEvent;
import io.github.kloping.kzero.hwxb.event.MetaEvent;
import io.github.kloping.kzero.mihdp.GeneralData;
import io.github.kloping.kzero.mihdp.MihdpClient;
import io.github.kloping.kzero.mihdp.ReqDataPack;
import io.github.kloping.kzero.mihdp.ResDataPack;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * @author github.kloping
 */
public class WxHookExtend0 {
    public static void recv(MessageEvent<String> event) {
        GeneralData.ResDataChain chain = new GeneralData.ResDataChain(new LinkedList<>());
        chain.getList().add(new GeneralData.ResDataText((event.getContent())));
        ReqDataPack req = new ReqDataPack();
        req.setAction("msg").setContent(JSON.toJSONString(chain))
                .setId(event.getId().toString())
                .setBot_id(WxHookStarter.ID)
                .setTime(System.currentTimeMillis())
                .setEnv_type(event instanceof GroupMessageEvent ? "group" : "friend")
                .setSender_id(event.getFrom().getId())
                .setEnv_id(event.getSubject().getId());
        String icon = event.getFrom().getPayLoad().getAvatar();
        icon = icon.replace("localhost", event.getAuth().getIp());
        req.getArgs().put("icon", icon + "&token=" + event.getAuth().getToken());
        req.getArgs().put("name", event.getFrom().getPayLoad().getName());
        req.getArgs().put("draw", "true");
        if (MihdpClient.INSTANCE != null) MihdpClient.INSTANCE.send(JSON.toJSONString(req));

        MessageReceive receive = getMessageReceive(event);
        if (GsuidClient.INSTANCE != null) GsuidClient.INSTANCE.send(receive);
    }

    @NotNull
    private static MessageReceive getMessageReceive(MessageEvent<String> event) {
        MessageReceive receive = new MessageReceive();
        receive.setUser_pm(3);
        String gid = event.getSubject().getId();
        receive.setGroup_id(gid.substring(gid.length() - 10, gid.length()));
        receive.setUser_type(event instanceof GroupMessageEvent ? "group" : "direct");
        receive.setContent(new MessageData[]{
                new MessageData("text", event.getContent().toString()),
                new MessageData("icon", event.getFrom().getPayLoad().getAvatar() + "&token=" + event.getAuth().getToken()),
        });
        receive.setMsg_id(event.getId().toString());
        receive.setBot_id(WxHookStarter.ID);
        receive.setBot_self_id(WxHookStarter.ID);
        String fid = event.getFrom().getId();
        receive.setUser_id(fid.substring(fid.length() - 10, fid.length()));
        return receive;
    }

    public static void onMessage(ResDataPack pack) {
        try {
            MsgData[] data = asDatagram(pack.getData());
            MetaEvent event = WxHookStarter.SID2EVENT.get(pack.getId());
            MessageEvent messageEvent = (MessageEvent) event;
            messageEvent.sendMessage(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void onMessageG(MessageOut pack) {
        try {
            MsgData[] data = asDatagram(pack.getContent());
            MetaEvent event = WxHookStarter.SID2EVENT.get(pack.getMsg_id());
            MessageEvent messageEvent = (MessageEvent) event;
            messageEvent.sendMessage(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static MsgData[] asDatagram(MessageData[] content) throws IOException {
        List<MsgData> list = new ArrayList<>();
        for (MessageData d0 : content) {
            if (d0.getType().equals("text")) {
                list.add(new MsgData(d0.getData().toString(), "text"));
            } else if (d0.getType().equals("image")) {
                byte[] bytes;
                if (d0.getData().toString().startsWith("base64://")) {
                    bytes = Base64.getDecoder().decode(d0.getData().toString().substring("base64://".length()));
                } else {
                    bytes = Base64.getDecoder().decode(d0.getData().toString());
                }
                String path = String.format("./temp/%s.jpg", UUID.randomUUID());
                FileUtils.writeBytesToFile(bytes, new File(path));
                MetaEvent event = HandlerController.getLeast();
                String url = String.format("%s:%s/%s", event.getAuth().getSelf(), event.getAuth().getPort(), path.replace("./temp/", ""));
                list.add(new MsgData(url, "fileUrl"));
            } else if (d0.getType().equals("node")) {
                JSONArray arr = (JSONArray) d0.getData();
                MessageData[] datas = arr.toJavaList(MessageData.class).toArray(new MessageData[0]);
                MsgData[] msgData = asDatagram(datas);
                list.addAll(Arrays.asList(msgData));
            }
        }
        return list.toArray(new MsgData[0]);
    }

    private static MsgData[] asDatagram(GeneralData data) throws IOException {
        if (data instanceof GeneralData.ResDataText) {
            return new MsgData[]{new MsgData(((GeneralData.ResDataText) data).getContent(), "text")};
        } else if (data instanceof GeneralData.ResDataImage) {
            GeneralData.ResDataImage image = (GeneralData.ResDataImage) data;
            if (image.getP().equals("base64")) {
                String path = String.format("./temp/%s.jpg", UUID.randomUUID());
                FileUtils.writeBytesToFile(Base64.getDecoder().decode(image.getData()), new File(path));
                MetaEvent event = WxHookStarter.SID2EVENT.values().iterator().next();
                String url = String.format("%s:%s/%s", event.getAuth().getSelf(), event.getAuth().getPort(), path.replace("./temp/", ""));
                return new MsgData[]{new MsgData(url, "fileUrl")};
            }
        } else if (data instanceof GeneralData.ResDataChain) {
            GeneralData.ResDataChain chain = (GeneralData.ResDataChain) data;
            List<MsgData> list = new LinkedList<>();
            for (GeneralData generalData : chain.getList()) {
                list.addAll(Arrays.asList(asDatagram(generalData)));
            }
            return list.toArray(new MsgData[0]);
        }
        return new MsgData[0];
    }
}
