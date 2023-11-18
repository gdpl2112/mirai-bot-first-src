package io.github.kloping.kzero.guilds.exclusive;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.github.kloping.MySpringTool.annotations.Action;
import io.github.kloping.MySpringTool.annotations.AllMess;
import io.github.kloping.MySpringTool.annotations.Before;
import io.github.kloping.MySpringTool.annotations.Controller;
import io.github.kloping.MySpringTool.exceptions.NoRunException;
import io.github.kloping.kzero.main.api.KZeroBot;
import io.github.kloping.kzero.main.api.MessagePack;
import io.github.kloping.qqbot.entities.Bot;
import io.github.kloping.qqbot.entities.ex.Image;
import io.github.kloping.qqbot.entities.ex.MessageAsyncBuilder;
import io.github.kloping.reg.MatcherUtils;
import org.springframework.web.client.RestTemplate;

import java.net.URISyntaxException;

/**
 * @author github.kloping
 */
@Controller
public class ParseController {
    private RestTemplate template;

    public ParseController() {
        template = new RestTemplate();
    }

    @Before
    public void before(@AllMess String msg, KZeroBot kZeroBot, MessagePack pack) {
        if (!(kZeroBot.getSelf() instanceof Bot)) throw new NoRunException("mirai-bot专属扩展");
    }

    @Action("解析快手.+")
    public Object pk(@AllMess String msg) throws URISyntaxException {
        String[] urls = MatcherUtils.matcherAll(msg, "(https?|http|ftp|file):\\/\\/[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|]");
        if (urls.length == 0) {
            return "未发现链接!";
        }
        String url = urls[0];
        url = "http://ovoa.cc/api/kuaishou.php?url=" + url;
        String dataStr = template.getForObject(url, String.class);
        JSONObject jo = JSON.parseObject(dataStr);
        if (jo.getInteger("code") == 200) {
            if (jo.getString("msg").contains("视频")) {
                JSONObject data = jo.getJSONObject("data");
                MessageAsyncBuilder builder = new MessageAsyncBuilder();
                builder.append(new Image(data.getString("cover")))
                        .append("\n作者: ").append(data.getString("author"))
                        .append("\n标题: ").append(data.getString("title"));
                builder.append(new Image(data.getString("url"), 2));
                return builder.build();
            }
        }
        return "解析失败或不支持发送";
    }
}
