package Project.listeners;

import com.alibaba.fastjson.JSONObject;
import io.github.kloping.mirai0.Main.BootstarpResource;
import io.github.kloping.mirai0.Main.BotStarter;
import kotlin.coroutines.CoroutineContext;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.QuoteReply;
import org.jetbrains.annotations.NotNull;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

import static io.github.kloping.common.Public.EXECUTOR_SERVICE;

/**
 * @author github.kloping
 */
public class LewisHandler extends SimpleListenerHost {
    private String host = null;
    private Number bid = null;

    @Override
    public void handleException(@NotNull CoroutineContext context, @NotNull Throwable exception) {
        super.handleException(context, exception);
    }

    @EventHandler
    public void onEvent(GroupMessageEvent event) {
        EXECUTOR_SERVICE.execute(() -> {
            if (host == null) {
                host = BootstarpResource.contextManager.getContextEntity(String.class, "lewis.host");
            }
            if (bid == null) {
                bid = BootstarpResource.contextManager.getContextEntity(Number.class, "lewis.bot");
            }
            // 分群处理
            if (isAllowGroupId(event.getGroup().getId(), "main")) {
                JSONObject param = new JSONObject();
                param.put("key", event.getMessage().serializeToMiraiCode());
                param.put("qq", event.getSender().getId());
                param.put("groupId", event.getGroup().getId());
                param.put("botQq", bid);
                String body = param.toJSONString();
                Document doc = null;
                try {
                    doc = Jsoup.connect(String.format("http://%s/api/getM2Result", host)).userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/111.0.0.0 Safari/537.36 Edg/111.0.1661.44").ignoreHttpErrors(true).ignoreContentType(true).header("Content-Type", "application/json").header("Authorization", null).requestBody(body).post();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                String m2ResultStr = doc.body().text();
                JSONObject resultJson = JSONObject.parseObject(m2ResultStr);
                MessageChainBuilder builder = new MessageChainBuilder().append(new QuoteReply(event.getMessage()));
                if (resultJson.getInteger("code") != null) {
                    if (resultJson.getInteger("code") == 200) {
                        if (null != resultJson.getString("result")) {
                            event.getSubject().sendMessage(builder.append(resultJson.getString("result")).build());
                        }
                    }
                    if (resultJson.getInteger("code") == 0) {
                        if (resultJson.getString("result") != null) {
                            event.getSubject().sendMessage(builder.append(resultJson.getString("result")).build());
                        }
                    }
                }
            }
        });
    }

    private boolean isAllowGroupId(Long id, String main) {
        if (BotStarter.test) return true;
        Number gid = BootstarpResource.contextManager.getContextEntity(Number.class, "lewis." + main);
        return gid.longValue() == id;
    }
}
