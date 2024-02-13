package io.github.kloping.kzero.bot.controllers;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.github.kloping.MySpringTool.annotations.Action;
import io.github.kloping.MySpringTool.annotations.Controller;
import io.github.kloping.common.Public;
import io.github.kloping.kzero.main.api.KZeroBot;
import io.github.kloping.kzero.main.api.MessagePack;
import io.github.kloping.kzero.utils.Utils;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * @author github.kloping
 */
@Controller
public class InterceptController {
    public interface OnIntercept {
        Object intercept(MessagePack pack, KZeroBot bot);
    }

    public Map<String, OnIntercept> interceptMap = new HashMap<>();

    public static final RestTemplate TEMPLATE = new RestTemplate();

    @Action("<pic:.*?>")
    public Object onPic(MessagePack pack, KZeroBot bot) {
        Public.EXECUTOR_SERVICE.submit(() -> {
            String url0 = Utils.getFormat(pack.getMsg(), "pic");
            if (url0 != null) {
                String data = TEMPLATE.getForObject("http://luck.klizi.cn/api/jianhuang.php?url=" + url0, String.class);
                JSONObject jo0 = JSON.parseObject(data);
                if (!jo0.getString("tips").equals("正常")) {
                    bot.getAdapter().sendMessage(pack.getType(), pack.getSubjectId(), String.format("分数:%s\n不正常,请注意言行.",jo0.get("score")));
                }
            }
        });
        String sid = pack.getSenderId();
        if (interceptMap.containsKey("")) {
            Object o = interceptMap.get("").intercept(pack, bot);
            if (o != null) return o;
        }
        if (interceptMap.containsKey(sid)) {
            OnIntercept intercept = interceptMap.get(sid);
            Object o = intercept.intercept(pack, bot);
            if (o != null)
                interceptMap.remove(sid);
            return o;
        }
        return null;
    }

    @Action("<at:.*?>")
    public Object onAt(MessagePack pack, KZeroBot bot) {
        String sid = pack.getSenderId();
        if (interceptMap.containsKey(sid)) {
            OnIntercept intercept = interceptMap.get(sid);
            Object o = intercept.intercept(pack, bot);
            if (o != null)
                interceptMap.remove(sid);
            return o;
        }
        return null;
    }

    public void register(String sid, OnIntercept intercept) {
        interceptMap.put(sid, intercept);
    }
}
