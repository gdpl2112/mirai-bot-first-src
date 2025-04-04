package io.github.kloping.kzero.hwxb;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.github.kloping.common.Public;
import io.github.kloping.file.FileUtils;
import io.github.kloping.judge.Judge;
import io.github.kloping.kzero.hwxb.dto.dao.MsgData;
import io.github.kloping.kzero.hwxb.event.MessageEvent;
import io.github.kloping.kzero.mirai.listeners.PoiSongHandler;
import io.github.kloping.kzero.utils.Utils;
import io.github.kloping.url.UrlUtils;
import net.mamoe.mirai.message.data.Message;
import net.mamoe.mirai.message.data.MusicShare;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;

import static io.github.kloping.kzero.mirai.listeners.PoiSongHandler.TEMPLATE;
import static io.github.kloping.kzero.mirai.listeners.SongASyncMethod.*;

/**
 * @author github.kloping
 */
public class ExtendServiceSa {

    public static Object handle(MessageEvent event) {
        String text = event.getContent().toString();
        if (text.contains(PoiSongHandler.DY_LINK)) {
            Matcher matcher = PoiSongHandler.URLPATTERN.matcher(text);
            if (matcher.find()) return gotoDouyin(event, matcher.group());
        }

        String out = text.trim();
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
        } else if (out.startsWith("取消点歌") || out.startsWith("取消选择")) {
            SongData o = QID2DATA.remove(event.getSender().getId());
            event.sendMessage("已取消.\n" + o.name);
        } else if (out.matches("[+\\-\\d]+")) {
            Integer n = Integer.valueOf(out);
            SongData e = QID2DATA.get(event.getSender().getId());
            if (e != null) {
                if (n == 0) {
                    String r = listSongs(event.getSender().getId(), e.type, e.p + 1, e.name);
                    if (r == null) event.sendMessage("翻页时异常!");
                    else event.sendMessage(r);
                } else {
                    Message msg = pointSongs(e, n);
                    if (msg == null) event.sendMessage("选择时异常!");
                    else {
                        MusicShare musicShare = (MusicShare) msg;
                        StringBuilder sb = new StringBuilder();
                        sb.append("曲名:").append(musicShare.getTitle()).append("\n");
                        sb.append("作者:").append(musicShare.getSummary()).append("\n");
                        sb.append("直链:").append(musicShare.getMusicUrl()).append("\n");
                        event.sendMessage(sb.toString());
                        Public.EXECUTOR_SERVICE.submit(()->{
                            event.sendMessage(new MsgData(musicShare.getMusicUrl() , "fileUrl"));
                        });
                    }
                }
            }
        }

        if (name != null && type != null) {
            String r = listSongs(event.getSender().getId(), type, 1, name);
            if (r == null) event.sendMessage("点歌时异常!");
            else event.sendMessage(r);
        }
        return null;
    }

    private static Object gotoDouyin(MessageEvent r, String url) {
        String out = TEMPLATE.getForObject("https://www.hhlqilongzhu.cn/api/sp_jx/sp.php?url=" + url, String.class);
        JSONObject result = JSON.parseObject(out);
        if (result.getInteger("code") < 0) {
            r.sendMessage("解析异常!\n若链接无误请反馈.");
            return null;
        }
        Utils.Gt gt = new Utils.Gt(out);
        String u0 = gt.gt("data.url", String.class);
        if (Judge.isEmpty(u0)) {
            out = TEMPLATE.getForObject("https://www.hhlqilongzhu.cn/api/sp_jx/tuji.php?url=" + url, String.class);
            gt = new Utils.Gt(out);
            JSONArray array = gt.gt("data.images", JSONArray.class);
            r.sendMessage("图集数量:" + array.size() + "/正在发送请稍等..");
            r.sendMessage(("音频直链: " + gt.gt("data.music")));
            List<MsgData> list = new ArrayList<>();
            for (Object o : array) {
                byte[] bytes = UrlUtils.getBytesFromHttpUrl(o.toString());
                File file = new File("temp", UUID.randomUUID() + ".jpg");
                try {
                    FileUtils.writeBytesToFile(bytes, file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                list.add(new MsgData(String.format("http://%s:20049/", r.getAuth().getIp()) + file.getName(), "fileUrl"));
            }
            r.sendMessage(list.toArray(new MsgData[0]));
        } else {
            r.sendMessage(("视频直链: " + gt.gt("data.url")));
            r.sendMessage(("音频直链: " + gt.gt("data.music_url")));
        }
        return null;
    }
}
