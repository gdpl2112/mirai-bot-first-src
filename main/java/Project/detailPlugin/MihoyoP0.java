package Project.detailPlugin;

import Entitys.apiEntitys.mihoyoYuanshen.MihoyoYuanshen;
import Entitys.apiEntitys.mihoyoYuanshenDetail.MihoyoYuanshenDetail;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import static Project.Controllers.ControllerSource.mihoyo;
import static Project.ResourceSet.FinalString.IMG_TAG_STR;
import static Project.ResourceSet.FinalString.VIDEO_TAG_STR;
import static Project.Tools.Tool.pathToImg;
import static Project.Tools.Tool.unicodeToCn;

/**
 * @author github-kloping
 */
public class MihoyoP0 {
    public static String c0(Document arg) {
        return getJsonFromYs(arg);
    }

    public static MihoyoYuanshen getNews() {
        return mihoyo.newsIndex();
    }

    public static String[] getNews(String cid) {
        MihoyoYuanshenDetail detail = mihoyo.newsPoint(cid);
        String html = detail.getData()[0].getArticle().getContent();
        String title = detail.getData()[0].getArticle().getTitle();
        String pic = detail.getData()[0].getArticle().getCover();
        String s = getMessageStringYs(Jsoup.parse(html).body());
        pic = pic.substring(1, pic.length() - 1);
        return new String[]{unicodeToCn(pic), title, s};
    }

    public static String getMessageStringYs(Element element) {
        StringBuilder sb = new StringBuilder();
        if (hasVideoTag(element)) {
            String s1 = element.getElementsByTag(VIDEO_TAG_STR).get(0).attr("src");
            sb.append("相关视频:");
            sb.append(unicodeToCn(s1));
        }
        for (Element child : element.children()) {
            String s = child.text();
            if (!s.startsWith("关于《原神》") && !s.isEmpty()) {
                sb.append("\n").append(child.text());
            }
            if (hasImgTag(child)) {
                sb.append("\n").append(pathToImg(child.getElementsByTag(IMG_TAG_STR).get(0).attr("src")));
            }
        }
        return sb.toString();
    }

    private static boolean hasImgTag(Element element) {
        if (element.children() != null && element.children().size() > 0) {
            for (Element child : element.children()) {
                if (IMG_TAG_STR.equals(child.tagName())) {
                    return true;
                } else {
                    return hasImgTag(child);
                }
            }
        }
        return false;
    }

    private static boolean hasVideoTag(Element element) {
        if (element.children() != null && element.children().size() > 0) {
            for (Element child : element.children()) {
                if (child.tagName().equals(VIDEO_TAG_STR)) {
                    return true;
                } else {
                    return hasVideoTag(child);
                }
            }
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
