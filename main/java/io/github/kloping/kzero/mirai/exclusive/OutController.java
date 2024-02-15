package io.github.kloping.kzero.mirai.exclusive;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.github.kloping.MySpringTool.annotations.Controller;
import io.github.kloping.MySpringTool.annotations.CronSchedule;
import io.github.kloping.kzero.main.KZeroMainThreads;
import io.github.kloping.kzero.main.api.KZeroBot;
import io.github.kloping.url.UrlUtils;
import net.mamoe.mirai.message.data.ForwardMessageBuilder;
import net.mamoe.mirai.message.data.PlainText;
import net.mamoe.mirai.utils.ExternalResource;

/**
 * @author github.kloping
 */
@Controller
public class OutController {
    @CronSchedule("12 02 8 * * ? *")
    public void sixs() {
        Long sid = 291841860L;
        KZeroBot bot = KZeroMainThreads.BOT_MAP.get(sid.toString());
        if (bot != null) {
            JSONObject jd = JSON.parseObject(UrlUtils.getStringFromHttpUrl("http://api.suxun.site/api/sixs?type=json"));
            net.mamoe.mirai.Bot b = (net.mamoe.mirai.Bot) bot.getSelf();
            net.mamoe.mirai.contact.Group group = b.getGroup(278681553L);
            group.sendMessage("每日早报!\n" + jd.getString("weiyu"));
            ForwardMessageBuilder builder = new ForwardMessageBuilder(b.getAsFriend());
            for (Object news : jd.getJSONArray("news")) {
                String line = news.toString();
                builder.add(sid, "BOT", new PlainText(line));
            }
            builder.add(sid, "BOT", b.getAsFriend().uploadImage(ExternalResource.create(UrlUtils.getBytesFromHttpUrl(jd.getString("image")))));
            group.sendMessage(builder.build());
        }
    }
}
