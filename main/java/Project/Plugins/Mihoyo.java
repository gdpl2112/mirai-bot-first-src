package Project.Plugins;

import Entitys.apiEntitys.mihoyoYuanshen.MihoyoYuanshen;
import Entitys.apiEntitys.mihoyoYuanshenDetail.MihoyoYuanshenDetail;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static Project.Controllers.FirstController.mihoyo;
import static Project.Tools.Tool.unicodeToCn;

public class Mihoyo {

    public static MihoyoYuanshen getNews() {
        return JSON.parseObject(getJsonFromYs(mihoyo.news_index())).toJavaObject(MihoyoYuanshen.class);
    }

    public static String[] getNews(String cid) {
        Document doc = mihoyo.news_point(cid);
        String jsonStr = getJsonFromYs(doc);
        JSONObject jo = null;
        try {
            jo = JSON.parseObject(jsonStr);
        } catch (Exception e) {
            System.err.println(jsonStr);
            e.printStackTrace();
        }
        MihoyoYuanshenDetail detail = jo.toJavaObject(MihoyoYuanshenDetail.class);
        String html = detail.getData()[0].getArticle().getContent();
        String title = detail.getData()[0].getArticle().getTitle();
        String pic = detail.getData()[0].getArticle().getCover();
        String s = getMessageStringYs(Jsoup.parse(html).body());
        return new String[]{pic, title, s};
    }

    public static String getMessageStringYs(Element element) {
        StringBuilder sb = new StringBuilder();
        if (hasVideoTag(element)) {
            String s1 = element.getElementsByTag("video").get(0).attr("src");
            sb.append("相关视频:");
            sb.append(s1);
        }
        for (Element child : element.children()) {
            if (child.tagName().equals("p"))
                sb.append("\n").append(child.text());
        }
        return sb.toString();
    }

    private static boolean hasVideoTag(Element element) {
        if (element.children() != null && element.children().size() > 0)
            for (Element child : element.children()) {
                if (child.tagName().equals("video")) return true;
                else return hasVideoTag(child);
            }
        return false;
    }

    public static String getJsonFromYs(Document doc) {
        Element element = doc.body().getElementsByTag("script").get(0);
        final String oStr = element.toString();
        int i1 = oStr.lastIndexOf("(");
        int i2 = oStr.lastIndexOf(")") - 1;
        String eStr = oStr.substring(i1 + 1, i2);
        i1 = oStr.indexOf("return {") + "return ".length();
        i2 = oStr.lastIndexOf("}(");
        String jsonStr = oStr.substring(i1, i2);

        String[] args = eStr.split(",");
        Set<Character> set = new LinkedHashSet<>();
        for (int i = 97; i <= 122; i++) {
            set.add((char) i);
        }
        for (int i = 65; i <= 'Z'; i++) {
            set.add((char) i);
        }
        Character[] chars = set.toArray(new Character[0]);
        int c1 = 0;
        Map<String, String> maps = new ConcurrentHashMap<>();
        for (String arg : args) {
            try {
                if (!arg.startsWith("\""))
                    arg = "\"" + arg + "\"";
                arg = unicodeToCn(arg);
                arg = unicodeToCn(arg);
                String c = String.valueOf(chars[c1++]);
                String m1 = ":" + c;
                jsonStr = jsonStr.replaceAll(m1, ":" + arg);
                m1 = "\\[" + c;
                jsonStr = jsonStr.replaceAll(m1, "[" + arg);
                m1 = "," + c + "]";
                jsonStr = jsonStr.replaceAll(m1, "," + arg + "]");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return unicodeToCn(jsonStr);
    }
}
