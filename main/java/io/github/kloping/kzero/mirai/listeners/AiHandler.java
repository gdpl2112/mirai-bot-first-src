package io.github.kloping.kzero.mirai.listeners;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.github.kloping.judge.Judge;
import io.github.kloping.kzero.bot.controllers.AllController;
import io.github.kloping.kzero.spring.dao.GroupConf;
import io.github.kloping.kzero.utils.Utils;
import io.github.kloping.url.UrlUtils;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.Contact;
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
    public static final Pattern URLPATTERN = Pattern.compile(regx);

    public static final String KS_LINK = "v.kuaishou.com";
    public static final String DY_LINK = "v.douyin.com";

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
            Matcher matcher = URLPATTERN.matcher(out);
            if (matcher.find()) parseKs(matcher.group(), event);
        } else if (out.contains(DY_LINK)) {
            Matcher matcher = URLPATTERN.matcher(out);
            if (matcher.find()) parseDy(matcher.group(), event);
        }
    }

    public static final RestTemplate TEMPLATE = new RestTemplate();

    private void parseDy(final String url, MessageEvent event) {
        String out = TEMPLATE.getForObject("https://www.hhlqilongzhu.cn/api/sp_jx/sp.php?url=" + url, String.class);
        JSONObject result = JSON.parseObject(out);
        if (result.getInteger("code") < 0) {
            event.getSubject().sendMessage("解析异常!\n若链接无误请反馈.");
            return;
        }
        Utils.Gt gt = new Utils.Gt(out);

        Bot bot = event.getBot();

        var builder = new MessageChainBuilder();
        byte[] bytes = UrlUtils.getBytesFromHttpUrl(gt.gt("data.cover", String.class));
        Image image = Contact.uploadImage(event.getBot().getAsFriend(), new ByteArrayInputStream(bytes), "jpg");
        builder.append(image)
                .append(gt.gt("data.title").toString())
                .append(gt.gt("text.time"));
        String u0 = gt.gt("data.url", String.class);
        var fbuilder = new ForwardMessageBuilder(bot.getAsFriend());
        if (Judge.isEmpty(u0)) {
            out = TEMPLATE.getForObject("https://www.hhlqilongzhu.cn/api/sp_jx/tuji.php?url=" + url, String.class);
            gt = new Utils.Gt(out);
            JSONArray array = gt.gt("data.images", JSONArray.class);
            builder.append("\n图集数量:").append(String.valueOf(array.size())).append("/正在发送请稍等..");
            try {
                event.getSubject().sendMessage(builder.build());
            } catch (Exception e) {
                e.printStackTrace();
            }
            fbuilder.add(bot.getId(), "AI", new PlainText("音频直链: " + gt.gt("data.music")));
            for (Object o : array) {
                bytes = UrlUtils.getBytesFromHttpUrl(o.toString());
                image = Contact.uploadImage(event.getBot().getAsFriend(), new ByteArrayInputStream(bytes), "jpg");
                fbuilder.add(bot.getId(), "AI", image);
            }
        } else {
            event.getSubject().sendMessage(builder.build());
            fbuilder.add(bot.getId(), "AI", new PlainText("视频直链: " + gt.gt("data.url")));
            fbuilder.add(bot.getId(), "AI", new PlainText("音频直链: " + gt.gt("data.music_url")));
        }
        event.getSubject().sendMessage(fbuilder.build());
    }

    public void parseKs(String url, MessageEvent event) {
        String out = TEMPLATE.getForObject("http://localhost/api/cre/jxvv?url=" + url, String.class);
        JSONObject result = JSON.parseObject(out);
        if (!result.containsKey("result")){
            sendToAsVideo(event,result);
            return;
        }
        if (result.getInteger("result") < 0) {
            event.getSubject().sendMessage("解析异常!\n若链接无误请反馈.");
            return;
        }
        Utils.Gt gt = new Utils.Gt(out);

        Bot bot = event.getBot();

        var builder = new MessageChainBuilder();
        byte[] bytes = UrlUtils.getBytesFromHttpUrl(gt.gt("photo.coverUrls[0].url", String.class));
        Image image = Contact.uploadImage(event.getSubject(), new ByteArrayInputStream(bytes), "jpg");
        builder.append(image)
                .append(gt.gt("photo.caption").toString())
                .append("作者").append(gt.gt("photo.userName")).append("/").append(gt.gt("photo.userSex"))
                .append("\n粉丝:").append(gt.gt("counts.fanCount"))
                .append("\n💗 ").append(gt.gt("photo.likeCount"))
                .append("\n👁︎︎ ").append(gt.gt("photo.viewCount"))
                .append("\n✉️ ").append(gt.gt("photo.commentCount"));

        ForwardMessageBuilder author = null;
        JSONArray array = gt.gt("shareUserPhotos", JSONArray.class);

        if (array != null && !array.isEmpty()) {
            author = new ForwardMessageBuilder(bot.getAsFriend());
            bytes = UrlUtils.getBytesFromHttpUrl(gt.gt("shareUserPhotos[0].headUrl", String.class));
            image = Contact.uploadImage(event.getSubject(), new ByteArrayInputStream(bytes), "jpg");
            author.add(bot.getId(), "AI", image);
            author.add(bot.getId(), "AI", new PlainText("sharer," + gt.gt("shareUserPhotos[0].userName")
                    + "/" + gt.gt("shareUserPhotos[0].userSex")));
        }

        JSONObject atlas = gt.gt("atlas", JSONObject.class);

        if (atlas == null) {
            builder.append("\n视频时长:" + (gt.gt("photo.duration", Integer.class) / 1000) + "s");
            event.getSubject().sendMessage(builder.build());

            var de0 = new ForwardMessageBuilder(bot.getAsFriend());
            de0.add(bot.getId(), "AI", new PlainText("视频直链: " + gt.gt("photo.mainMvUrls[0].url")));
            de0.add(bot.getId(), "AI", new PlainText("音频直链: " + gt.gt("photo.soundTrack.audioUrls[0].url")));
            event.getSubject().sendMessage(de0.build());
        } else {
            builder.append("\n图集数量:" + gt.gt("atlas.list", JSONArray.class).size() + "/正在发送,请稍等...");
            event.getSubject().sendMessage(builder.build());

            var fbuilder = new ForwardMessageBuilder(bot.getAsFriend());
            if (author != null) fbuilder.add(bot.getId(), "AI", author.build());
            fbuilder.add(bot.getId(), "AI", new PlainText("音频直链: https://" + gt.gt("atlas.musicCdnList[0].cdn")
                    + gt.gt("atlas.music")));
            var arr = gt.gt("atlas.list", JSONArray.class);
            var host = "https://" + gt.gt("atlas.cdn[0]");
            for (var i = 0; i < arr.size(); i++) {
                var e = arr.get(i);
                try {
                    bytes = UrlUtils.getBytesFromHttpUrl(host + e);
                    image = Contact.uploadImage(event.getSubject(), new ByteArrayInputStream(bytes), "jpg");
                    fbuilder.add(bot.getId(), "AI", image);
                } catch (Exception ex) {
                    fbuilder.add(bot.getId(), "AI", new PlainText("[图片加载失败;" + host + e + "]"));
                }
            }
            event.getSubject().sendMessage(fbuilder.build());
        }
    }

    private void sendToAsVideo(MessageEvent event, JSONObject result) {
        Bot bot = event.getBot();

        var builder = new MessageChainBuilder();
        byte[] bytes = UrlUtils.getBytesFromHttpUrl(result.getString("coverUrl"));
        Image image = Contact.uploadImage(event.getSubject(), new ByteArrayInputStream(bytes), "jpg");
        builder.append(image)
                .append(result.getString("caption").toString())
                .append("\n💗 ").append(result.getString("likeCount"))
                .append("\n👁︎︎ ").append(result.getString("viewCount"));
        builder.append("\n视频时长:" + (result.getInteger("duration") / 1000) + "s");
        event.getSubject().sendMessage(builder.build());

        var de0 = new ForwardMessageBuilder(bot.getAsFriend());
        de0.add(bot.getId(), "AI", new PlainText("视频直链: " + result.getString("photoUrl")));
        event.getSubject().sendMessage(de0.build());
    }
}
