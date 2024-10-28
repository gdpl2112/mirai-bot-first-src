package io.github.kloping.kzero.hwxb.event;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.github.kloping.kzero.hwxb.dto.Contact;
import io.github.kloping.kzero.hwxb.dto.dao.MsgData;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

/**
 * @author github.kloping
 */
@Data
@Slf4j
public class GroupMessageEvent extends MessageEvent {
    private Contact room;

    public GroupMessageEvent(MetaEvent<?> event, Contact room) {
        super(event);
        this.room = room;
    }

    @Override
    public Contact getSubject() {
        return room;
    }

    @Override
    public String getContactType() {
        return "GROUP";
    }

    public JSONObject sendMessage(MsgData... data) {
        if (data.length == 0) return null;
        try {
            JSONObject json = new JSONObject();
            json.put("to", getSubject().getPayLoad().getTopic());
            json.put("isRoom", true);
            if (data.length == 1) json.put("data", data[0]);
            else json.put("data", data);
            String body = json.toJSONString();
            Document doc0 = Jsoup.connect(getAuth().getUrl()).ignoreContentType(true)
                    .header("Content-Type", "application/json")
                    .header("Accept-Encoding", "br,deflate,gzip,x-gzip")
                    .header("Connection", "Keep-Alive")
                    .header("Content-Length", String.valueOf(body.length()))
                    .userAgent("Apache-HttpClient/4.5.14 (Java/17.0.8.1)")
                    .requestBody(body).post();
            String out = doc0.body().text();
            log.info("FriendMessageEvent.sendMessage:{}", out);
            return JSON.parseObject(doc0.body().text());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
