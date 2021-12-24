package Project.Plugins;

import Entitys.apiEntitys.mihoyoYuanshen.MihoyoYuanshen;
import Entitys.apiEntitys.mihoyoYuanshenDetail.MihoyoYuanshenDetail;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import static Project.Controllers.FirstController.mihoyo;
import static Project.Tools.Tool.unicodeToCn;

public class Mihoyo {

    public static MihoyoYuanshen getNews() {
        String jsonStr = getJsonFromYs(mihoyo.news_index());
        return JSON.parseObject(jsonStr).toJavaObject(MihoyoYuanshen.class);
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
        pic = pic.substring(1,pic.length()-1);
        return new String[]{unicodeToCn(pic), title, s};
    }

    public static String getMessageStringYs(Element element) {
        StringBuilder sb = new StringBuilder();
        if (hasVideoTag(element)) {
            String s1 = element.getElementsByTag("video").get(0).attr("src");
            sb.append("相关视频:");
            sb.append(unicodeToCn(s1));
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
        String[] args = eStr.split(",");
        String m = "function";
        i1 = oStr.indexOf(m) + m.length();
        i2 = oStr.lastIndexOf("}(");
        String jsMethod = oStr.substring(i1, i2);
        jsMethod = "function m0" + jsMethod + "}";
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("javascript");
        try {
            engine.eval("function m1(o){return JSON.stringify(o);}");
            engine.eval(jsMethod);
            if (engine instanceof Invocable) {
                Invocable in = (Invocable) engine;
                Object o = in.invokeFunction("m0", args);
                return in.invokeFunction("m1", o).toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "{}";
    }
}
