package Project.detailPlugin;

import Project.interfaces.IBaiduShitu;
import io.github.kloping.MySpringTool.annotations.AutoStand;
import io.github.kloping.MySpringTool.annotations.Entity;
import io.github.kloping.mirai0.Entitys.apiEntitys.baiduShitu.BaiduShitu;

import java.util.HashMap;
import java.util.Map;

/**
 * @author github-kloping
 * @version 1.0
 */
@Entity
public class BaiduShituDetail {
    public static final Map<String, String> HEADERS = new HashMap<>();

    static {
        HEADERS.put("host", "graph.baidu.com");
        HEADERS.put("content-length", "343");
        HEADERS.put("accept", "*/*");
        HEADERS.put("content-type", "multipart/form-data; boundary=----WebKitFormBoundaryhj2XGBn7j9a5lo36");
        HEADERS.put("origin", "https://image.baidu.com");
        HEADERS.put("x-requested-with", "idm.internet.download.manager.plus");
        HEADERS.put("sec-fetch-site", "same-site");
        HEADERS.put("sec-fetch-mode", "cors");
        HEADERS.put("sec-fetch-dest", "empty");
        HEADERS.put("referer", "https://image.baidu.com/?fr=shitu");
        HEADERS.put("accept-encoding", "gzip, deflate");
        HEADERS.put("accept-language", "zh-CN,zh;q=0.9,en-US;q=0.8,en;q=0.7");
    }

    @AutoStand
    static IBaiduShitu baiduShitu;

    public static BaiduShitu get(String imageUrl) {
        BaiduShitu shitu = baiduShitu.get(HEADERS, "------WebKitFormBoundaryhj2XGBn7j9a5lo36\n" +
                "Content-Disposition: form-data; name=\"sdkParams\"\n" +
                "\n" +
                "{\"data\":\"0480c357f7aed15dfa7baa673711cb2dc7707ef6505acfb346b2399f6cbd84b6f995b7df42fad5ee08d8e30a82248919754ab803ba733e5b6fe07ab03c901bb32844781b3dfda1f9105ca0f71fe4f2fc\",\"key_id\":\"23\",\"sign\":\"b1eb6966\"}\n" +
                "------WebKitFormBoundaryhj2XGBn7j9a5lo36--\n", null, null, null, null, imageUrl, System.currentTimeMillis());
        return shitu;
    }
}
