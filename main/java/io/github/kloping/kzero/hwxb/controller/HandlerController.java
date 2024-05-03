package io.github.kloping.kzero.hwxb.controller;

import com.alibaba.fastjson.JSONObject;
import io.github.kloping.kzero.hwxb.dto.Source;
import io.github.kloping.kzero.hwxb.event.FriendMessageEvent;
import io.github.kloping.kzero.hwxb.event.GroupMessageEvent;
import io.github.kloping.kzero.hwxb.event.MessageEvent;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author github.kloping
 */
@RestController
public class HandlerController {
    @RequestMapping("recv")
    public Object recv(HttpServletRequest request) {
        String source = request.getParameter("source");
        String type = request.getParameter("type");
        String content = request.getParameter("content");
        String isSystemEvent = request.getParameter("isSystemEvent");
        String isMsgFromSelf = request.getParameter("isMsgFromSelf");
        String isMentioned = request.getParameter("isMentioned");
        Source s0 = JSONObject.parseObject(source, Source.class);
        MessageEvent event = null;
        if (s0.getFrom().isEmpty()) {
            event = new GroupMessageEvent(s0.getRoom());
        } else {
            event = new FriendMessageEvent(s0.getFrom());
        }
        event.setTo(s0.getTo());
        event.setContent(content);
        event.setType(type);
        event.setIsSystemEvent(Integer.valueOf(isSystemEvent) == 1);
        event.setIsMsgFromSelf(Integer.valueOf(isMsgFromSelf) == 1);
        event.setIsMentioned(Integer.valueOf(isMentioned) == 1);
        return " {\n" +
                "    \"success\": true,\n" +
                "    \"data\": {\n" +
                "      \"type\": \"text\",\n" +
                "      \"content\": \"hello world！\"\n" +
                "    }\n" +
                "  }";
    }
}
