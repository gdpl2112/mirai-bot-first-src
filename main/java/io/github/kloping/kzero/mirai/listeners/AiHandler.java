package io.github.kloping.kzero.mirai.listeners;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.github.kloping.kzero.bot.controllers.AllController;
import io.github.kloping.kzero.spring.dao.GroupConf;
import io.github.kloping.url.UrlUtils;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.User;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.ListenerHost;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.*;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayInputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static io.github.kloping.kzero.mirai.listeners.SongASyncMethod.*;
/**
 * @author github.kloping
 */
public class AiHandler implements ListenerHost {
    public static final AiHandler INSTANCE = new AiHandler();

    @EventHandler
    public void pointOnly(GroupMessageEvent event) throws Exception {
        GroupConf groupConf = AllController.dataBase.getConf(String.valueOf(event.getSubject().getId()));
        if (groupConf != null) {
            if (!groupConf.getOpen()) return;
        }
        StringBuilder line = new StringBuilder();
        for (SingleMessage singleMessage : event.getMessage()) {
            if (singleMessage instanceof PlainText) {
                line.append(((PlainText) singleMessage).getContent().trim());
            }
        }
        String out = line.toString().trim();
        String name = null;
        String type = null;
        if (out.startsWith("酷狗点歌") && out.length() > 4) {
            name = out.substring(4);
            type = TYPE_KUGOU;
        } else if (out.startsWith("网易点歌") && out.length() > 4) {
            name = out.substring(4);
            type = TYPE_WY;
        } else if (out.startsWith("点歌") && out.length() > 2) {
            name = out.substring(2);
            type = TYPE_QQ;
        } else if (out.startsWith("QQ点歌") && out.length() > 4) {
            name = out.substring(4);
            type = TYPE_QQ;
        } else if (out.startsWith("取消点歌")||out.startsWith("取消选择")) {
            SongData o = QID2DATA.remove(event.getSender().getId());
            event.getSubject().sendMessage("已取消.\n" + o.name);
        } else if (out.matches("[+\\-\\d]+")) {
            Integer n = Integer.valueOf(out);
            SongData e = QID2DATA.get(event.getSender().getId());
            if (e != null) {
                if (n == 0) {
                    String r = listSongs(event.getSender().getId(), e.type, e.p + 1, e.name);
                    if (r == null) event.getSubject().sendMessage("翻页时异常!");
                    else event.getSubject().sendMessage(r);
                } else {
                    Message msg = pointSongs(e, n);
                    if (msg == null) event.getSubject().sendMessage("选择时异常!");
                    else event.getSubject().sendMessage(msg);
                }
            }
        }

        if (name != null && type != null) {
            String r = listSongs(event.getSender().getId(), type, 1, name);
            if (r == null) event.getSubject().sendMessage("点歌时异常!");
            else event.getSubject().sendMessage(r);
        }
    }

    public static final String regx = "(https?|http|ftp|file):\\/\\/[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|]";
    public static final Pattern pattern = Pattern.compile(regx);

    public static final String KS_LINK = "v.kuaishou.com";

    @EventHandler
    public void parseOnly(MessageEvent event) throws Exception {
        GroupConf groupConf = AllController.dataBase.getConf(String.valueOf(event.getSubject().getId()));
        if (groupConf != null) {
            if (!groupConf.getOpen()) return;
        }
        StringBuilder line = new StringBuilder();
        for (SingleMessage singleMessage : event.getMessage()) {
            if (singleMessage instanceof PlainText) {
                line.append(((PlainText) singleMessage).getContent().trim());
            }
        }
        String out = line.toString().trim();
        if (out.contains(KS_LINK)) {
            Matcher matcher = pattern.matcher(out);
            if (matcher.find()) parseKs(matcher.group(), event);
        }
    }

    public static final RestTemplate TEMPLATE = new RestTemplate();

    public void parseKs(String url, MessageEvent event) {
        String out = TEMPLATE.getForObject("https://api.xingzhige.com/API/kuaishou/?url=" + url, String.class);
        JSONObject jo = JSON.parseObject(out);
        Integer code = jo.getInteger("code");
        if (code == 0) {
            event.getSubject().sendMessage("解析成功\n正在发送请稍等..\n" + url);
            JSONObject data = jo.getJSONObject("data");
            JSONObject item = data.getJSONObject("item");
            JSONObject stat = data.getJSONObject("stat");

            MessageChainBuilder builder = new MessageChainBuilder();
            byte[] bytes = UrlUtils.getBytesFromHttpUrl(item.getString("cover"));
            builder.append(Contact.uploadImage(event.getSubject(), new ByteArrayInputStream(bytes), "jpg"));
            builder.append("\n").append(item.getString("title"))
                    .append("\n\uD83D\uDC97:").append(stat.getInteger("like").toString())
                    .append("\n\uD83D\uDC41\uFE0E:").append(stat.getInteger("view").toString())
                    .append("\n✉:").append(stat.getInteger("comment").toString());

            JSONArray images = item.getJSONArray("images");
            if (images != null) {
                builder.append("\n数量:").append(String.valueOf(images.size()));
            } else {
                event.getSubject().sendMessage(builder.build());
            }

            User sender = event.getBot().getAsFriend();
            ForwardMessageBuilder fbuilder = new ForwardMessageBuilder(event.getBot().getAsFriend());
            if (images == null) {
                fbuilder.add(sender, new PlainText("视频直链:" + item.getString("video")));
            } else {
                fbuilder.add(sender, new PlainText("音频直链:" + item.getString("music")));
                for (Object image : images) {
                    String u0 = image.toString();
                    bytes = UrlUtils.getBytesFromHttpUrl(u0);
                    fbuilder.add(sender, Contact.uploadImage(sender, new ByteArrayInputStream(bytes), "jpg"));
                }
            }

            event.getSubject().sendMessage(fbuilder.build());
        } else {
            event.getSubject().sendMessage("解析失败\n" + out);
        }
    }
}
