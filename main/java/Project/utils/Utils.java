package Project.utils;

import io.github.kloping.mirai.BotInstance;
import org.springframework.web.util.UriUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author github.kloping
 */
public class Utils {
    private static final String DEFAULT_CHARSET = "UTF-8";

    public static Long getAtFromString(String message) {
        int i0 = message.indexOf("[@");
        int ie = message.indexOf("]");
        if (i0 == -1 || ie == -1) return -1L;
        String sid = message.substring(i0 + 2, ie);
        if ("me".equals(sid)) return BotInstance.getInstance().getBotId();
        else return Long.parseLong(sid);
    }

    /**
     * 发起Get请求
     *
     * @param urlStr
     * @return
     */
    public final static byte[] doGetRequestForFile(String urlStr) {
        InputStream is = null;
        ByteArrayOutputStream os = null;
        byte[] buff = new byte[1024];
        int len = 0;
        try {
            URL url = new URL(UriUtils.encodePath(urlStr, DEFAULT_CHARSET));
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Content-Type", "plain/text;charset=" + DEFAULT_CHARSET);
            conn.setRequestProperty("charset", DEFAULT_CHARSET);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestMethod("GET");
            conn.setReadTimeout(15000);
            conn.connect();
            is = conn.getInputStream();
            os = new ByteArrayOutputStream();
            while ((len = is.read(buff)) != -1) {
                os.write(buff, 0, len);
            }
            return os.toByteArray();
        } catch (IOException e) {
            System.err.println("发起请求出现异常:" + e);
            return null;
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    System.err.println("【关闭流异常】");
                }
            }
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    System.err.println("【关闭流异常】");
                }
            }
        }
    }
}
