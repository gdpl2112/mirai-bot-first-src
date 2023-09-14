package io.github.kloping.kzero.bot.controllers;

import io.github.kloping.MySpringTool.annotations.Action;
import io.github.kloping.MySpringTool.annotations.Controller;
import io.github.kloping.kzero.main.api.KZeroBot;
import io.github.kloping.kzero.main.api.MessagePack;

import java.util.HashMap;
import java.util.Map;

/**
 * @author github.kloping
 */
@Controller
public class InterceptController {
    public interface OnIntercept {
        Object Intercept(MessagePack pack, KZeroBot bot);
    }

    public Map<String, OnIntercept> interceptMap = new HashMap<>();

    @Action("<pic:.*?>")
    public void onPic(MessagePack pack, KZeroBot bot) {
        String sid = pack.getSenderId();
        if (interceptMap.containsKey(sid)) {
            OnIntercept intercept = interceptMap.get(sid);
            if (intercept.Intercept(pack, bot) != null)
                interceptMap.remove(sid);
        }
    }

    public void register(String sid, OnIntercept intercept) {
        interceptMap.put(sid, intercept);
    }
}
