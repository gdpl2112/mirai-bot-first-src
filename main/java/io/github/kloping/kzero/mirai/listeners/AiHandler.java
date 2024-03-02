package io.github.kloping.kzero.mirai.listeners;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.ListenerHost;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

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


    public static final Map<Long, Map.Entry<String, Object>> QID_2_WORD = new HashMap<>();


    @EventHandler
    public void pointOnly(GroupMessageEvent event) {
        StringBuilder line = new StringBuilder();
        for (SingleMessage singleMessage : event.getMessage()) {
            if (singleMessage instanceof PlainText) {
                line.append(((PlainText) singleMessage).getContent().trim());
            }
        }
        String out = line.toString().trim();
        if (out.startsWith("酷狗点歌") && out.length() > 4) {
            String name = out.substring(4);
            QID_2_WORD.put(event.getSender().getId(), new AbstractMap.SimpleEntry<>("kg", name));
            event.getSubject().sendMessage(TEMPLATE.getForObject("https://xiaoapi.cn/API/yy.php?type=kg&msg=" + name, String.class));
            return;
        } else if (out.startsWith("网易点歌") && out.length() > 4) {
            String name = out.substring(4);
            QID_2_WORD.put(event.getSender().getId(), new AbstractMap.SimpleEntry<>("wy", name));
            event.getSubject().sendMessage(TEMPLATE.getForObject("https://xiaoapi.cn/API/yy.php?type=wy&msg=" + name, String.class));
            return;
        } else if (out.startsWith("点歌") && out.length() > 4) {
            String name = out.substring(2);
            pVip(event, name, 1);
        } else if (out.startsWith("QQ点歌") && out.length() > 4) {
            String name = out.substring(4);
            pVip(event, name, 1);
        } else if (out.matches("[\\d]+")) {
            Integer n = Integer.valueOf(out);
            Map.Entry<String, Object> e = QID_2_WORD.get(event.getSender().getId());
            if (e != null) {
                String type = e.getKey();
                if (type.equals("kg")) {
                    String lines = TEMPLATE.getForObject(String.format("https://xiaoapi.cn/API/yy.php?type=%s&msg=%s&n=%s", e.getKey(), e.getValue(), n), String.class);
                    String[] args = lines.split("\n");
                    MusicShare share = new MusicShare(MusicKind.KugouMusic, args[1].substring(3), args[2].substring(3), args[3].substring(5), args[0].substring(3), args[3].substring(5));
                    event.getSubject().sendMessage(share);
                } else if (type.equals("wy")) {
                    String lines = TEMPLATE.getForObject(String.format("https://xiaoapi.cn/API/yy.php?type=%s&msg=%s&n=%s", e.getKey(), e.getValue(), n), String.class);
                    String[] args = lines.split("\n");
                    MusicShare share = new MusicShare(MusicKind.NeteaseCloudMusic, args[1].substring(3), args[2].substring(3), args[3].substring(5), args[0].substring(3), args[3].substring(5));
                    event.getSubject().sendMessage(share);
                } else {
                    if (n == 0) {
                        String[] args = type.split("|");
                        Integer p = Integer.valueOf(args[1]);
                        String name = args[2];
                        pVip(event, name, p + 1);
                    }
                    JSONObject d0 = (JSONObject) e.getValue();
                    MusicShare share = new MusicShare(MusicKind.QQMusic, d0.getString("name"), d0.getString("singer"), d0.getString("url"), d0.getString("cover"), d0.getString("url"));
                    event.getSubject().sendMessage(share);
                }
            }
        }
    }

    private static void pVip(GroupMessageEvent event, String name, Integer p) {
        JSONObject jo0 = JSON.parseObject(TEMPLATE.getForObject("https://zj.v.api.aa1.cn/api/qqmusic/demo.php?type=1&n=9&p=" + p + "&q=" + name, String.class));
        QID_2_WORD.put(event.getSender().getId(), new AbstractMap.SimpleEntry<>(String.format("data|%s|%s", p, name), jo0));
        if (jo0.getInteger("code") == 200) {
            StringBuilder sb = new StringBuilder();
            int i = 1;
            for (Object o1 : jo0.getJSONArray("list")) {
                JSONObject e0 = (JSONObject) o1;
                sb.append(1).append(".").append(e0.getString("name")).append("--").append(e0.getString("name")).append("\n");
            }
            sb.append(jo0.getString("msg")).append("-tips:选择'0'可翻向下一页");
            event.getSubject().sendMessage(sb.toString());
        } else {
            event.getSubject().sendMessage(jo0.getString("msg"));
        }
        return;
    }

}
