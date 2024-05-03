package io.github.kloping.kzero.hwxb;

import com.alibaba.fastjson.JSON;
import io.github.gdpl2112.onebot.v12.event.GroupMessageEvent;
import io.github.kloping.file.FileUtils;
import io.github.kloping.kzero.hwxb.dto.dao.MsgData;
import io.github.kloping.kzero.hwxb.dto.dao.MsgPack;
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
    public static void recv(MessageEvent event) {
        GeneralData.ResDataChain chain = new GeneralData.ResDataChain(new LinkedList<>());
        chain.getList().add(new GeneralData.ResDataText((event.getContent())));
        ReqDataPack req = new ReqDataPack();
        req.setAction("msg").setContent(JSON.toJSONString(chain))
                .setId(String.valueOf(event.hashCode()))
                .setBot_id("self")
                .setTime(System.currentTimeMillis()).setEnv_type(event instanceof GroupMessageEvent ? "group" : "friend")
                .setSender_id(event.getFrom().getId())
                .setEnv_id(event.getSubject().getPayLoad().getName());
        req.getArgs().put("icon", event.getFrom().getPayLoad().getAvatar() + "&token=" + event.getAuth().getToken());
        req.getArgs().put("name", event.getFrom().getPayLoad().getName());
        req.getArgs().put("draw", "true");
        MihdpClient.INSTANCE.send(JSON.toJSONString(req));
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
                String url = String.format("%s:%s/%s", event.getAuth().getSelf(), event.getAuth().getPort(), path.replace("./temp", ""));
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
