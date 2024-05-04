package io.github.kloping.kzero.hwxb.controller;

import com.alibaba.fastjson.JSONObject;
import io.github.kloping.kzero.hwxb.WxAuth;
import io.github.kloping.kzero.hwxb.WxHookStarter;
import io.github.kloping.kzero.hwxb.dto.Source;
import io.github.kloping.kzero.hwxb.event.GroupMessageEvent;
import io.github.kloping.kzero.hwxb.event.MessageEvent;
import io.github.kloping.kzero.hwxb.event.MetaEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author github.kloping
 */
@RestController
@ConditionalOnProperty(name = "wxbot.token")
public class HandlerController {

    @Autowired
    WxAuth wxAuth;

    private MetaEvent.Auth auth;

    @RequestMapping("recv")
    public Object recv(HttpServletRequest request) {
        synchronized (this) {
            if (auth == null) auth = new MetaEvent.Auth(wxAuth);
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
        event.setContent(content);
        event.setType(type);
        event.setIsSystemEvent(Integer.valueOf(isSystemEvent) == 1);
        event.setIsMsgFromSelf(Integer.valueOf(isMsgFromSelf) == 1);
        event.setIsMentioned(Integer.valueOf(isMentioned) == 1);
        event.setRequest(request);
        event.setAuth(auth);
        if (event instanceof MessageEvent) {
            ((MessageEvent) event).setTo(s0.getTo());
            ((MessageEvent) event).setFrom(s0.getFrom());
            System.out.format("[wx.hook-log]%s.%s=>%s\n", event.getClass().getSimpleName(),
                    ((MessageEvent) event).getFrom().getPayLoad().getName(), event.getContent());
        } else {
            System.out.format("[wx.hook-log]%s.%s=>%s\n", event.getClass().getSimpleName(), event.getType(), event.getContent());
        }
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
