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
public class FriendMessageEvent extends MessageEvent {
    private Contact room;

    public FriendMessageEvent(MetaEvent<?> event, Contact room) {
        super(event);
        this.room = room;
    }

    @Override
    public Contact getSender() {
        return getFrom();
    }

    @Override
    public Contact getSubject() {
        return getFrom();
    }

    @Override
    public String getContactType() {
        return "FRIEND";
    }

    public JSONObject sendMessage(MsgData... data) {
        if (data.length == 0) return null;
        try {
            JSONObject json = new JSONObject();
            json.put("to", getFrom().getPayLoad().getName());
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
            return JSON.parseObject(out);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
