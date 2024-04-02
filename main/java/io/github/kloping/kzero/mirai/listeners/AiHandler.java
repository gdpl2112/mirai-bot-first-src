package io.github.kloping.kzero.mirai.listeners;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.github.kloping.date.FrameUtils;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.ListenerHost;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.MusicKind;
import net.mamoe.mirai.message.data.MusicShare;
import net.mamoe.mirai.message.data.PlainText;
import net.mamoe.mirai.message.data.SingleMessage;
import org.jetbrains.annotations.NotNull;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author github.kloping
 */
public class AiHandler implements ListenerHost {
    public static final AiHandler INSTANCE = new AiHandler();

    public static final RestTemplate TEMPLATE = new RestTemplate();

    // id <type,data>
    public static final Map<Long, SongData> QID2DATA = new HashMap<>();

    public static final long MAX_CD = 1000 * 60 * 30;

    static {
        FrameUtils.SERVICE.scheduleWithFixedDelay(() -> {
            Iterator<Long> iterator = QID2DATA.keySet().iterator();
            while (iterator.hasNext()) {
                Long qid = iterator.next();
                SongData data = QID2DATA.get(qid);
                if ((System.currentTimeMillis() - data.time) > MAX_CD) {
                    QID2DATA.remove(qid);
                }
            }
        }, 20, 30, TimeUnit.MINUTES);
    }

    public static final String TYPE_KUGOU = "KG";
    public static final String TYPE_QQ = "qq";

    @EventHandler
    public void pointOnly(GroupMessageEvent event) throws Exception {
        StringBuilder line = new StringBuilder();
        for (SingleMessage singleMessage : event.getMessage()) {
            if (singleMessage instanceof PlainText) {
                line.append(((PlainText) singleMessage).getContent().trim());
            }
        }
        String out = line.toString().trim();
        if (out.startsWith("酷狗点歌") && out.length() > 4) {
            String name = out.substring(4);
            kugouVip(event, name, 1);
            return;
        } else if (out.startsWith("网易点歌") && out.length() > 4) {
            String name = out.substring(4);
            QID2DATA.put(event.getSender().getId(), new SongData("wy", name, event.getSender().getId(), System.currentTimeMillis()));
            event.getSubject().sendMessage(
                    TEMPLATE.getForObject("https://xiaoapi.cn/API/yy.php?type=wy&msg=" + name, String.class) + "\n使用'取消点歌'/'取消选择'来取消选择");
            return;
        } else if (out.startsWith("点歌") && out.length() > 2) {
            String name = out.substring(2);
            qqvip(event, name, 1);
        } else if (out.startsWith("QQ点歌") && out.length() > 4) {
            String name = out.substring(4);
            qqvip(event, name, 1);
        } else if (out.startsWith("取消点歌")||out.startsWith("取消选择")) {
            SongData o = QID2DATA.remove(event.getSender().getId());
            event.getSubject().sendMessage("已取消.\n" + o.name);
        } else if (out.matches("[\\d]+")) {
            Integer n = Integer.valueOf(out);
            SongData e = QID2DATA.get(event.getSender().getId());
            if (e != null) {
                MusicShare share = null;
                String type = e.type;
                if (type.equals(TYPE_KUGOU)) {
                    if (n == 0) {
                        kugouVip(event, e.name, e.p + 1);
                    } else {
                        JSONObject d0 = (JSONObject) e.data;
                        d0 = d0.getJSONObject("data").getJSONArray("name").getJSONObject(n - 1);
                                                        //http://www.dreamling.top/API/kugou/android/music/api.php?keyword=%E7%A8%BB%E9%A6%99&pagenum=10&format=json&flag=format
                        Document doc0 = getDocument("http://www.dreamling.top/API/kugou/web/music/api.php?&pagenum=9&format=json&flag=format&page=" + e.p + "&keyword=" + e.name + "&n=" + n);
                        JSONObject data = JSON.parseObject(doc0.body().text());
                        String url = data.getJSONObject("data").getString("url");
                        share = new MusicShare(MusicKind.KugouMusic, d0.getString("SongName"), d0.getString("SingerName"), url, d0.getString("Image"), url);
                    }
                } else if (type.equals("wy")) {
                    String lines = TEMPLATE.getForObject(String.format("https://xiaoapi.cn/API/yy.php?type=%s&msg=%s&n=%s", e.type, e.data, n), String.class);
                    String[] args = lines.split("\n");
                    share = new MusicShare(MusicKind.NeteaseCloudMusic, args[1].substring(3), args[2].substring(3), args[3].substring(5), args[0].substring(3), args[3].substring(5));
                    event.getSubject().sendMessage(share);
                } else if (type.equals(TYPE_QQ)) {
                    if (n == 0) {
                        qqvip(event, e.name, e.p + 1);
                    } else {
                        JSONObject d0 = (JSONObject) e.data;
                        d0 = d0.getJSONArray("list").getJSONObject(n - 1);
                        String url = getRedirectUrl(d0.getString("url"));
                        share = new MusicShare(MusicKind.QQMusic, d0.getString("name"), d0.getString("singer"), url, d0.getString("cover"), url);
                    }
                }
                if (share != null) {
                    event.getSubject().sendMessage(share);
                }
            }
        }
    }

    private static void qqvip(GroupMessageEvent event, String name, Integer p) throws Exception {
        Document doc0 = getDocument("https://zj.v.api.aa1.cn/api/qqmusic/demo.php?type=1&n=9&p=" + p + "&q=" + name);
        JSONObject jo0 = JSON.parseObject(doc0.body().text());
        QID2DATA.put(event.getSender().getId(), new SongData(p, name, TYPE_QQ, jo0, event.getSender().getId(), System.currentTimeMillis()));
        if (jo0.getInteger("code") == 200) {
            StringBuilder sb = new StringBuilder();
            int i = 1;
            for (Object o1 : jo0.getJSONArray("list")) {
                JSONObject e0 = (JSONObject) o1;
                sb.append(i++).append(".").append(e0.getString("name")).append("--").append(e0.getString("singer")).append("\n");
            }
            sb.append(jo0.getString("msg")).append("\ntips:选择'0'可翻向下一页\n使用'取消点歌'/'取消选择'来取消选择");
            event.getSubject().sendMessage(sb.toString());
        } else {
            event.getSubject().sendMessage(jo0.getString("msg") + "\n使用'取消点歌'/'取消选择'来取消选择");
        }
        return;
    }

    private static void kugouVip(GroupMessageEvent event, String name, Integer p) throws Exception {
        Document doc0 = getDocument("http://www.dreamling.top/API/kugou/web/music/api.php?&pagenum=9&format=json&flag=format&page=" + p + "&keyword=" + name);
        JSONObject jo0 = JSON.parseObject(doc0.body().text());
        QID2DATA.put(event.getSender().getId(), new SongData(p, name, TYPE_KUGOU, jo0, event.getSender().getId(), System.currentTimeMillis()));
        if (jo0.getInteger("code") == 200) {
            JSONObject data = jo0.getJSONObject("data");
            StringBuilder sb = new StringBuilder();
            sb.append("共搜索到'").append(data.getInteger("total")).append("'个结果").append(";当前第'").append(data.getInteger("page")).append("'页\n");
            int i = 1;
            for (Object o1 : data.getJSONArray("name")) {
                JSONObject e0 = (JSONObject) o1;
                sb.append(i++).append(".").append(e0.getString("Name")).append("\n");
            }
            sb.append("tips:选择'0'可翻向下一页\n使用'取消点歌'/'取消选择'来取消选择");
            event.getSubject().sendMessage(sb.toString());
        } else {
            event.getSubject().sendMessage(jo0.getString("搜索异常啦o(╥﹏╥)o"));
        }
        return;
    }

    @NotNull
    private static Document getDocument(String url) throws IOException {
        Document doc0 = Jsoup.connect(url)
                .ignoreHttpErrors(true).ignoreContentType(true)
                .header("Connection", "Keep-Alive")
                .header("User-Agent", "Apache-HttpClient/4.5.14 (Java/17.0.8.1)")
                .header("Accept-Encoding", "br,deflate,gzip,x-gzip").get();
        return doc0;
    }

    /**
     * 获取重定向地址
     *
     * @param path
     * @return
     * @throws Exception
     */
    private static String getRedirectUrl(String path) throws Exception {
        Document doc0 = getDocument(path);
        return doc0.location();
    }

    public static class SongData {
        public SongData(String type, Object data, Long qid, Long time) {
            this.type = type;
            this.data = data;
            this.qid = qid;
            this.time = time;
        }

        public SongData(Integer p, String name, String type, Object data, Long qid, Long time) {
            this.p = p;
            this.name = name;
            this.type = type;
            this.data = data;
            this.qid = qid;
            this.time = time;
        }

        private Integer p;
        private String name;
        private String type;
        private Object data;
        private Long qid;
        private Long time;
    }
}
