package io.github.kloping.kzero.mirai.listeners;

import com.alibaba.fastjson.JSONObject;
import io.github.kloping.MySpringTool.annotations.AutoStand;
import io.github.kloping.MySpringTool.annotations.AutoStandAfter;
import io.github.kloping.MySpringTool.annotations.Entity;
import io.github.kloping.common.Public;
import io.github.kloping.kzero.main.api.KZeroBot;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.GlobalEventChannel;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.QuoteReply;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

@Entity
public class LewisHandler extends SimpleListenerHost {
    public static final String ALLOW_BID = "291841860";
    public static final String ALLOW_BID1 = "930204019";

    @AutoStandAfter
    private void after(KZeroBot bot) {
        GlobalEventChannel.INSTANCE.registerListenerHost(this);
    }

    @AutoStand(id = "lewis.main")
    Number main;

    @AutoStand(id = "lewis.host")
    String host;

    @AutoStand(id = "lewis.bot")
    Long bid;


    @EventHandler
    public void onEvent(GroupMessageEvent event) {
        Public.EXECUTOR_SERVICE.execute(() -> {
            // 分群处理
            if (main.toString().equals(String.valueOf(event.getGroup().getId()))) {
                JSONObject param = new JSONObject();
                param.put("key", event.getMessage().serializeToMiraiCode());
                param.put("qq", event.getSender().getId());
                param.put("groupId", event.getGroup().getId());
                param.put("botQq", bid);
                String body = param.toJSONString();
                Document doc = null;
                try {
                    doc = Jsoup.connect(String.format("http://%s/api/getM2Result", host))
                            .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/111.0.0.0 Safari/537.36 Edg/111.0.1661.44")
                            .ignoreHttpErrors(true).ignoreContentType(true)
                            .header("Content-Type", "application/json")
                            .header("Authorization", null).requestBody(body).post();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                String m2ResultStr = doc.body().text();
                JSONObject resultJson = JSONObject.parseObject(m2ResultStr);
                MessageChainBuilder builder = new MessageChainBuilder().append(new QuoteReply(event.getMessage()));
                if (resultJson.getInteger("code") != null) {
                    if (resultJson.getInteger("code") == 200) {
                        if (null != resultJson.getString("result")) {
                            event.getSubject().sendMessage(builder.append(resultJson.getString("result")).build());
                        }
                    }
                    if (resultJson.getInteger("code") == 0) {
                        if (resultJson.getString("result") != null) {
                            event.getSubject().sendMessage(builder.append(resultJson.getString("result")).build());
                        }
                    }
                }
            }
        });
    }
}