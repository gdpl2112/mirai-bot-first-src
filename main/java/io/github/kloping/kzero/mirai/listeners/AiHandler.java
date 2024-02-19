package io.github.kloping.kzero.mirai.listeners;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.github.kloping.common.Public;
import net.mamoe.mirai.contact.NormalMember;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.ListenerHost;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.SingleMessage;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author github.kloping
 */
public class AiHandler implements ListenerHost {
    public static final AiHandler INSTANCE = new AiHandler();

    public static final RestTemplate TEMPLATE = new RestTemplate();

    private Map<Long, Queue<MessageEvent>> hist = new HashMap<>();

    @EventHandler
    public void onE1(GroupMessageEvent event) {
        Public.EXECUTOR_SERVICE.submit(() -> {
            try {
                for (SingleMessage singleMessage : event.getMessage()) {
                    if (singleMessage instanceof Image) {
                        String url0 = Image.queryUrl((Image) singleMessage);
                        try {
                            if (url0 != null) {
                                String data = TEMPLATE.getForObject("http://luck.klizi.cn/api/jianhuang.php?url=" + url0, String.class);
                                JSONObject jo0 = JSON.parseObject(data);
                                if (!jo0.getString("tips").equals("正常")) {
                                    event.getSubject().sendMessage(String.format("分数:%s\n不正常,请注意言行.", jo0.get("score")));
                                }
                            }
                        } catch (RestClientException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        Public.EXECUTOR_SERVICE.submit(() -> {
            if (event.getSubject().getBotAsMember().getPermission().getLevel() == 2) {
                Queue<MessageEvent> queue = hist.get(event.getSender().getId());
                if (queue == null) queue = new LinkedBlockingQueue<>(5);
                if (queue.size() > 3) {
                    String code = MessageChain.serializeToJsonString(event.getMessage()).trim();
                    int ac = 0;
                    for (int i = 0; i < queue.size(); i++) {
                        MessageEvent e1 = queue.peek();
                        if (Math.abs(e1.getTime() - event.getTime()) > 120) continue;
                        String c2 = MessageChain.serializeToJsonString(e1.getMessage());
                        if (code.equals(c2)) ac++;
                    }
                    if (ac == 3) {
                        event.getSubject().sendMessage("检测到可能存在刷屏行为,请注意发言.");
                    } else if (ac > 3) {
                        event.getSubject().sendMessage("多次刷屏...\n禁言20s以示警告");
                        NormalMember member = (NormalMember) event.getSender();
                        member.mute(20);
                    }
                }
                if (queue.size() > 5) queue.poll();
                queue.offer(event);
                hist.put(event.getSender().getId(), queue);
            }
        });
    }

}
