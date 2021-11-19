package Project.Plugins;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

import static Project.Plugins.NetMain.*;

public class SearchPic {

    public static String[] getPic(String name) throws IOException {
        String urlStr = String.format(rootPath + getPicNum, name, 15);
        Document doc = Jsoup.connect(urlStr).ignoreContentType(true).timeout(7000).get();
        String m1 = doc.body().text();
        JSONObject jsonObject = JSON.parseObject(m1);
        String[] wm = jsonObject.getJSONArray("data").toJavaObject(String[].class);
        return wm;
    }

    public static String[] getPicDt(String name) throws IOException {
        String urlStr = String.format(rootPath + getPicNoNumDt, name, 15);
        Document doc = Jsoup.connect(urlStr).ignoreContentType(true).timeout(7000).get();
        String m1 = doc.body().text();
        JSONObject jsonObject = JSON.parseObject(m1);
        String[] wm = jsonObject.getJSONArray("data").toJavaObject(String[].class);
        return wm;
    }

    public static String[] getPicM(String name) throws Exception {
        String urlStr = String.format(rootPath + getPicNum, name, 15);
        Document doc = Jsoup.connect(urlStr).ignoreContentType(true).timeout(7000).get();
        String m1 = doc.body().text();
        JSONObject jsonObject = JSON.parseObject(m1);
        String[] wm = jsonObject.getJSONArray("data").toJavaObject(String[].class);
        return wm;
    }

    public static String[] parseKsImgs(String urlStr) throws IOException {
        urlStr = String.format(rootPath + parsePic, urlStr, "ks");
        Document doc = Jsoup.connect(urlStr).ignoreContentType(true).timeout(7000).get();
        String m1 = doc.body().text();
        String[] wm = JSON.parseArray(m1).toJavaObject(String[].class);
        return wm;
    }

    public static String[] parseDyImgs(String urlStr) throws IOException {
        urlStr = String.format(rootPath + parsePic, urlStr, "dy");
        Document doc = Jsoup.connect(urlStr).ignoreContentType(true).timeout(7000).get();
        String m1 = doc.body().text();
        String[] wm = JSON.parseArray(m1).toJavaObject(String[].class);
        return wm;
    }
}