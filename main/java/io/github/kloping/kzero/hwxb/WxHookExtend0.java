package io.github.kloping.kzero.hwxb;

import com.alibaba.fastjson.JSON;
import io.github.kloping.file.FileUtils;
import io.github.kloping.kzero.gsuid.GsuidClient;
import io.github.kloping.kzero.gsuid.MessageData;
import io.github.kloping.kzero.gsuid.MessageOut;
import io.github.kloping.kzero.gsuid.MessageReceive;
import io.github.kloping.kzero.hwxb.dto.dao.MsgData;
import io.github.kloping.kzero.hwxb.dto.dao.MsgPack;
import io.github.kloping.kzero.hwxb.event.GroupMessageEvent;
import io.github.kloping.kzero.hwxb.event.MessageEvent;
import io.github.kloping.kzero.hwxb.event.MetaEvent;
import io.github.kloping.kzero.mihdp.GeneralData;
import io.github.kloping.kzero.mihdp.MihdpClient;
import io.github.kloping.kzero.mihdp.ReqDataPack;
import io.github.kloping.kzero.mihdp.ResDataPack;

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
                .setId(String.valueOf(event.hashCode()))
                .setBot_id(WxHookStarter.ID)
                .setTime(System.currentTimeMillis()).setEnv_type(event instanceof GroupMessageEvent ? "group" : "friend")
                .setSender_id(event.getFrom().getId())
                .setEnv_id(event.getSubject().getPayLoad().getName());
        String icon = event.getFrom().getPayLoad().getAvatar();
        icon = icon.replace("localhost", event.getAuth().getWxAuth().getIp());
        req.getArgs().put("icon", icon + "&token=" + event.getAuth().getWxAuth().getToken());
        req.getArgs().put("name", event.getFrom().getPayLoad().getName());
        req.getArgs().put("draw", "true");
        MihdpClient.INSTANCE.send(JSON.toJSONString(req));

        MessageReceive receive = new MessageReceive();
        receive.setUser_pm(event.getFrom().getPayLoad().getId().equalsIgnoreCase("@e3a27b8f580251935c485376bd8f6810b5aeee93b41b56b02e3eb10a849d3aee") ? 1 : 3);
        receive.setGroup_id(event.getSubject().getId());
        receive.setUser_type(event instanceof GroupMessageEvent ? "group" : "direct");
        receive.setContent(new MessageData[]{new MessageData(event.getContent().toString(), "text")});
        receive.setMsg_id(String.valueOf(event.hashCode()));
        receive.setBot_id(WxHookStarter.ID);
        receive.setBot_self_id(WxHookStarter.ID);
        receive.setUser_id(event.getFrom().getPayLoad().getId());
        GsuidClient.INSTANCE.send(receive);
    }

    public static void onMessage(ResDataPack pack) {
        MsgPack msgPack = new MsgPack();
        MsgData[] datas = asDatas(pack.getData());
        msgPack.setData(datas);
        msgPack.setIsRoom(pack.getEnv_type().equals("group") ? true : false);
        msgPack.setTo(pack.getEnv_id());
        MetaEvent event = WxHookStarter.SID2EVENT.values().iterator().next();
        event.getAuth().sendMessage(msgPack);
    }

    public static void onMessageG(MessageOut pack) {
        MsgPack msgPack = new MsgPack();
        MsgData[] datas = asDatas(pack.getContent());
        msgPack.setData(datas);
        msgPack.setIsRoom(pack.getTarget_type().equals("group") ? true : false);
        msgPack.setTo(pack.getTarget_id());
        MetaEvent event = WxHookStarter.SID2EVENT.values().iterator().next();
        event.getAuth().sendMessage(msgPack);
    }

    private static MsgData[] asDatas(MessageData[] content) {
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
                try {
                    FileUtils.writeBytesToFile(bytes, new File(path));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                MetaEvent event = WxHookStarter.SID2EVENT.values().iterator().next();
                String url = String.format("%s:%s/%s", event.getAuth().getWxAuth().getSelf(),
                        event.getAuth().getWxAuth().getPort(), path.replace("./temp/", ""));
                list.add(new MsgData(url, "fileUrl"));
            }
        }
        return list.toArray(new MsgData[0]);
    }

    private static MsgData[] asDatas(GeneralData data) {
        if (data instanceof GeneralData.ResDataText) {
            return new MsgData[]{new MsgData(((GeneralData.ResDataText) data).getContent(), "text")};
        } else if (data instanceof GeneralData.ResDataImage) {
            GeneralData.ResDataImage image = (GeneralData.ResDataImage) data;
            if (image.getP().equals("base64")) {
                String path = String.format("./temp/%s.jpg", UUID.randomUUID());
                try {
                    FileUtils.writeBytesToFile(Base64.getDecoder().decode(image.getData()), new File(path));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                MetaEvent event = WxHookStarter.SID2EVENT.values().iterator().next();
                String url = String.format("%s:%s/%s"
                        , event.getAuth().getWxAuth().getSelf(),
                        event.getAuth().getWxAuth().getPort(), path.replace("./temp/", ""));
                return new MsgData[]{new MsgData(url, "fileUrl")};
            }
        } else if (data instanceof GeneralData.ResDataChain) {
            GeneralData.ResDataChain chain = (GeneralData.ResDataChain) data;
            List<MsgData> list = new LinkedList<>();
            for (GeneralData generalData : chain.getList()) {
                list.addAll(Arrays.asList(asDatas(generalData)));
            }
            return list.toArray(new MsgData[0]);
        }
        return new MsgData[0];
    }
}
