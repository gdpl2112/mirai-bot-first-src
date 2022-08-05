package Project.detailPlugin;

import Project.interfaces.http_api.KlopingWeb;
import com.alibaba.fastjson.JSONObject;
import io.github.kloping.MySpringTool.annotations.AutoStand;
import io.github.kloping.MySpringTool.annotations.Entity;
import io.github.kloping.file.FileUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @author github.kloping
 */
@Entity
public class KlopingDetail {
    @AutoStand
    KlopingWeb klopingWeb;

    public String uploadImg(File file) {
        byte[] bytes = FileUtils.getBytesFromFile(file.getAbsolutePath());
        JSONObject object = new JSONObject();
        object.put("data", bytes);
        Map<String, String> headers = new HashMap<>();
        headers.put("content-type", "application/json");
        String s0 = object.toString();
        headers.put("content-length", String.valueOf(s0.length()));
        headers.put("accept-encoding", "gzip,deflate");
        headers.put("host", "localhost");
       return klopingWeb.uploadImg(headers, "4432120", s0);
    }
}
