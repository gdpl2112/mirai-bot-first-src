package io.github.kloping.kzero.hwxb;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.github.kloping.file.FileUtils;
import io.github.kloping.judge.Judge;
import io.github.kloping.kzero.hwxb.dto.dao.MsgData;
import io.github.kloping.kzero.hwxb.event.MessageEvent;
import io.github.kloping.kzero.mirai.listeners.AiHandler;
import io.github.kloping.kzero.utils.Utils;
import io.github.kloping.url.UrlUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;

import static io.github.kloping.kzero.mirai.listeners.AiHandler.TEMPLATE;

/**
 * @author github.kloping
 */
public class ExtendServiceSa {

    public static Object handle(MessageEvent r) {
        String text = r.getContent().toString();
        if (text.contains(AiHandler.DY_LINK)) {
            Matcher matcher = AiHandler.URLPATTERN.matcher(text);
            if (matcher.find()) return gotoDouyin(r, matcher.group());
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
