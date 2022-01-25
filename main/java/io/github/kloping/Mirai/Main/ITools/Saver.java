package io.github.kloping.Mirai.Main.ITools;

import Entitys.DataC;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import io.github.kloping.Mirai.Main.BotStarter;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import static io.github.kloping.Mirai.Main.Resource.contextManager;

/**
 * @author github-kloping
 */
public class Saver {

    public static String path = "./messages/";
    public static String pathRecall = "./messages/recalled/";

    public static final String ROOT_PATH = contextManager.getContextEntity(String.class, "SaverRootUrl");

    public static String savePath = "/save";

    public static String savePath2 = "/save2";

    public static String saveRecallPath = "/saveRecall";

    public static String getPath = "/get";

    public static String getPath2 = "/get2";

    public static String loginPath = "/login?password=kloping_";

    private static String token = "kloping_";

    private static synchronized Object request(String path, boolean needReturn, Object... objects) {
        try {
            Connection connection = Jsoup.connect(ROOT_PATH + path)
                    .ignoreContentType(true)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/95.0.4638.54 Safari/537.36 Edg/95.0.1020.30")
                    .header("Accept", "*/*")
                    .header("Content-Type", "application/json");
            DataC o = (DataC) objects[0];
            String jsonStr = JSON.toJSONString(o);
            connection.requestBody(jsonStr);
            Document document = connection.post();
            if (needReturn) {
                return document.body().text();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void saveMessage(String text, long group, long q) throws Exception {
        if (ROOT_PATH == null) return;
        if (BotStarter.test) return;
        DataC dataC = new DataC();
        dataC.setContent(text);
        dataC.setGroupId(group);
        dataC.setqId(q);
        dataC.setType(0);
        dataC.setToken(token);
        request(savePath, false, dataC);
    }

    public static void saveMessage2(String text, long iq, long q) throws Exception {
        if (ROOT_PATH == null) return;
        DataC dataC = new DataC();
        dataC.setContent(text);
        dataC.setGroupId(iq);
        dataC.setqId(q);
        dataC.setType(0);
        dataC.setToken(token);
        request(savePath2, false, dataC);
    }

    public static String[] getTexts(long group, long q, int[] ints) throws Exception {
        if (ROOT_PATH == null) return null;
        DataC dataC = new DataC();
        StringBuilder sb = new StringBuilder();
        for (int n : ints) {
            sb.append(n).append(",");
        }
        dataC.setContent(sb.toString());
        dataC.setGroupId(group);
        dataC.setqId(q);
        dataC.setType(0);
        dataC.setToken(token);
        String jsonStr = request(getPath, true, dataC).toString();
        DataC data = JSON.parseObject(jsonStr, DataC.class);
        JSONArray array = (JSONArray) data.getContent();
        return array.toArray(new String[0]);
    }

    public static String[] getTexts2(long group, long q, int[] ints) throws Exception {
        if (ROOT_PATH == null) return null;
        DataC dataC = new DataC();
        StringBuilder sb = new StringBuilder();
        for (int n : ints) sb.append(n).append(",");
        dataC.setContent(sb.toString());
        dataC.setGroupId(group);
        dataC.setqId(q);
        dataC.setType(0);
        dataC.setToken(token);
        String jsonStr = request(getPath2, true, dataC).toString();
        DataC data = JSON.parseObject(jsonStr, DataC.class);
        JSONArray array = (JSONArray) data.getContent();
        return array.toArray(new String[0]);
    }
}
