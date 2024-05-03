package io.github.kloping.kzero.hwxb.controller;

import com.alibaba.fastjson.JSONObject;
import io.github.kloping.kzero.hwxb.WxHookStarter;
import io.github.kloping.kzero.hwxb.dto.Source;
import io.github.kloping.kzero.hwxb.event.GroupMessageEvent;
import io.github.kloping.kzero.hwxb.event.MessageEvent;
import io.github.kloping.kzero.hwxb.event.MetaEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author github.kloping
 */
@RestController
public class HandlerController {

    @Value("${wxbot.token}")
    String token;

    @Value("${wxbot.url}")
    String url;

    @Value("${server.port}")
    Integer port;

    @Value("${server.self}")
    String self;

    private MetaEvent.Auth auth;

    @RequestMapping("recv")
    public Object recv(HttpServletRequest request) {
        synchronized (this) {
            if (auth == null) auth = new MetaEvent.Auth(token, url, self, port);
        }
        String source = request.getParameter("source");
        String type = request.getParameter("type");
        String content = request.getParameter("content");
        String isSystemEvent = request.getParameter("isSystemEvent");
        String isMsgFromSelf = request.getParameter("isMsgFromSelf");
        String isMentioned = request.getParameter("isMentioned");

        Source s0 = JSONObject.parseObject(source, Source.class);
        MetaEvent event = null;
        if (!s0.getRoom().isEmpty()) {
            event = new GroupMessageEvent(s0.getRoom());
        } else {
            event = new MetaEvent();
        }
        if (event instanceof MessageEvent) {
            ((MessageEvent) event).setTo(s0.getTo());
            ((MessageEvent) event).setFrom(s0.getFrom());
        }
        event.setContent(content);
        event.setType(type);
        event.setIsSystemEvent(Integer.valueOf(isSystemEvent) == 1);
        event.setIsMsgFromSelf(Integer.valueOf(isMsgFromSelf) == 1);
        event.setIsMentioned(Integer.valueOf(isMentioned) == 1);
        event.setRequest(request);
        event.setAuth(auth);
        if (WxHookStarter.INSTANCE != null) {
            WxBotEventRecv recv = WxHookStarter.RECVS.get(type);
            if (recv != null) {
                Object o = recv.recv(event);
                if (o != null) return o;
            }
        }
        return "{}";
    }
}
