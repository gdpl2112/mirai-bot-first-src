package Project.detailPlugin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.github.kloping.mirai0.commons.apiEntitys.ShiTu.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author github-kloping
 * @version 1.0
 */
public class All {
    public static Object sub(String str) {
        return str.substring(str.indexOf("{"), str.lastIndexOf("}") + 1);
    }


    public static Object getTalentDays(String json) {
        return JSON.parseObject(json).getJSONObject("data").getInteger("days");
    }

    public static String shiTuParse(Document document) {
        Response response = new Response();
        Element element = document.getElementsByClass("ULSxyf").get(2);
        Elements elements = element.getElementsByTag("script");
        for (int i = 0; i < 8; i++) {
            Element e1 = elements.get(i);
            String s1 = e1.toString();
            int i1 = s1.indexOf("(function(){var s='");
            int i2 = s1.lastIndexOf("';");
            if (i2 > i1 && i1 > 0) {
                response.getSimilar().add(s1.substring(i1 + "(function(){var s='".length(), i2));
            }
        }
        for (Element e : element.getElementsByClass("g tF2Cxc")) {
            String a = e.getElementsByTag("a").get(0).attr("href");
            response.getSources().add(a);
        }
        return JSON.toJSONString(response);
    }

    public static String getTitle(String url) throws IOException {
        return Jsoup.connect(url).ignoreContentType(true).userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/98.0.4758.80 Safari/537.36 Edg/98.0.1108.43")
                .get().title();
    }

    public static String sjc0(String json) {
        JSONObject jo = JSON.parseObject(json);
        JSONObject jo0 = jo.getJSONArray("vod_list").getJSONObject(0);
        AtomicReference<Object> data = new AtomicReference<>();
        AtomicReference<String> name = new AtomicReference<>();
        jo0.forEach((k, v) -> {
            name.set(k);
            data.set(v);
        });
        jo0.remove(name.get());
        jo0.put("data", data.get());
        json = jo.toJSONString();
        return json;
    }
}
