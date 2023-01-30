package Project.detailPlugin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.github.kloping.mirai0.commons.apiEntitys.ShiTu.Response;
import io.github.kloping.url.UrlUtils;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.Frame;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

import static io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet.FinalString.NEWLINE;

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

    public static String replaceDouble2normal(String json) {
        json = json.replaceAll("\":\\s?\"\"", "\": \"\\\\\"");
        json = json.replaceAll("\"\"", "\\\\\"\\\"");
        return json;
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

    public static ByteArrayOutputStream mp42mp3(InputStream is) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        FFmpegFrameGrabber frameGrabber1 = new FFmpegFrameGrabber(is);
        Frame frame = null;
        FFmpegFrameRecorder recorder = null;
        try {
            frameGrabber1.start();
            Random random = new Random();
            recorder = new FFmpegFrameRecorder(baos, frameGrabber1.getAudioChannels());
            recorder.setFormat("mp3");
            recorder.setSampleRate(frameGrabber1.getSampleRate());
            recorder.setTimestamp(frameGrabber1.getTimestamp());
            recorder.setAudioQuality(0);
            recorder.start();
            int index = 0;
            while (true) {
                frame = frameGrabber1.grab();
                if (frame == null) {
                    System.out.println("视频处理完成");
                    break;
                }
                if (frame.samples != null) {
                    recorder.recordSamples(frame.sampleRate, frame.audioChannels, frame.samples);
                }
                System.out.println("帧值=" + index);
                index++;
            }
            recorder.stop();
            recorder.release();
            frameGrabber1.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return baos;
    }


    public static final String getTextFromPic(String url) {
        try {
            Connection connection = Jsoup.connect("https://api.wer.plus/api/yocr")
                    .method(Connection.Method.POST)
                    .ignoreHttpErrors(true).ignoreContentType(true)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/108.0.0.0 Safari/537.36 Edg/108.0.1462.54")
                    .requestBody("{\"data\":\"" + url + "\"}");
            Connection.Response response = connection.execute();
            String json = response.body();
            JSONObject jo = JSON.parseObject(json);
            JSONObject data = jo.getJSONObject("data");
            if (!data.containsKey("comment")) return "未能识别出出文字";
            JSONArray comment = data.getJSONArray("comment");
            StringBuilder sb = new StringBuilder();
            for (Object oe : comment) {
                JSONArray e = (JSONArray) oe;
                sb.append("\"").append(e.get(1).toString()).append("\"可信度:").append(e.get(2).toString().substring(0, 4)).append(NEWLINE);
            }
            return sb.toString().trim();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
