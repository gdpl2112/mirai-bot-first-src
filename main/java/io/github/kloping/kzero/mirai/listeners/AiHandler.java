package io.github.kloping.kzero.mirai.listeners;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.ListenerHost;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.SingleMessage;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * @author github.kloping
 */
public class AiHandler implements ListenerHost {
    public static final AiHandler INSTANCE = new AiHandler();

    public static final RestTemplate TEMPLATE = new RestTemplate();

    @EventHandler
    public void onE1(GroupMessageEvent event) {
        try {
            for (SingleMessage singleMessage : event.getMessage()) {
                if (singleMessage instanceof Image) {
                    String url0 = Image.queryUrl((Image) singleMessage);
                    try {
                        if (url0 != null) {
                            String data = TEMPLATE.getForObject("http://luck.klizi.cn/api/jianhuang.php?url=" + url0, String.class);
                            JSONObject jo0 = JSON.parseObject(data);
                            if (!jo0.getString("tips").equals("正常")) {
                                MessageChainBuilder builder = new MessageChainBuilder();
                                builder.append(String.format("分数:%s\n可能不正常\n来源:%s(%s)",
                                                jo0.get("score"), event.getGroup().getName(), event.getGroup().getId()))
                                        .append(singleMessage);
                                event.getBot().getGroup(570700910L).sendMessage(builder.build());
                            }
                        }
                    } catch (RestClientException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
