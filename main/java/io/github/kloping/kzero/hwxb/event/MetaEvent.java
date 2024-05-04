package io.github.kloping.kzero.hwxb.event;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import io.github.kloping.kzero.hwxb.WxAuth;
import io.github.kloping.kzero.hwxb.dto.dao.MsgPack;
import lombok.Data;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author github.kloping
 */
@Data
public class MetaEvent<T extends Object> {
    private T content;
    private String type;
    private Boolean isSystemEvent;
    private Boolean isMentioned;
    private Boolean isMsgFromSelf;

    @JSONField(serialize = false, deserialize = false)
    private HttpServletRequest request;
    @JSONField(serialize = false, deserialize = false)
    private Auth auth;

    @Data
    public static class Auth {
        private WxAuth wxAuth;

        public Auth(WxAuth wxAuth) {
            this.wxAuth = wxAuth;
        }

        public JSONObject sendMessage(MsgPack... packs) {
            if (packs.length == 0) return null;
            try {
                String body = JSON.toJSONString(packs, true);
                Document doc0 = Jsoup.connect(wxAuth.getUrl()).ignoreContentType(true)
                        .header("Content-Type", "application/json")
                        .header("Accept-Encoding", "br,deflate,gzip,x-gzip")
                        .header("Connection", "Keep-Alive")
                        .header("Content-Length", String.valueOf(body.length()))
                        .userAgent("Apache-HttpClient/4.5.14 (Java/17.0.8.1)")
                        .requestBody(body).post();
                return JSON.parseObject(doc0.body().text());
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
    }
}
