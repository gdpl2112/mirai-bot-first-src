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
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.springframework.web.client.RestTemplate;

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
    public Object pk(@AllMess String msg) throws Exception {
        String[] urls = MatcherUtils.matcherAll(msg, "(https?|http|ftp|file):\\/\\/[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|]");
        if (urls.length == 0) {
            return "未发现链接!";
        }
        String url = urls[0];
        String dataStr = Jsoup.connect("https://tools.qzxdp.cn/api/video_spider/query")
                .header("Accept", "application/json, text/javascript, */*; q=0.01")
                .header("Accept-Encoding", "gzip, deflate, br")
                .header("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8,en-GB;q=0.7,en-US;q=0.6")
                .header("Connection", "keep-alive")
                .header("Host", "tools.qzxdp.cn")
                .header("Origin", "https://tools.qzxdp.cn")
                .header("Referer", "https://tools.qzxdp.cn/video_spider")
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Safari/537.36 Edg/114.0.1823.67")
                .ignoreContentType(true)
                .data("video_url", url).method(Connection.Method.POST).execute().body();
        JSONObject jo = JSON.parseObject(dataStr);
        if (jo == null) return "解析失败或不支持发送";
        JSONObject data = jo.getJSONObject("data");
        if (data == null) return "解析失败或不支持发送";
        MessageAsyncBuilder builder = new MessageAsyncBuilder();
        builder.append("\n标题: ").append(data.getString("title"));
        builder.append(new Image(data.getString("url"), 2));
        return builder.build();

    }
}
