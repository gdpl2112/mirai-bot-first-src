package Project.listeners;

import io.github.kloping.MySpringTool.StarterObjectApplication;
import io.github.kloping.MySpringTool.annotations.CommentScan;
import io.github.kloping.mirai.MessageSerializer;
import io.github.kloping.mirai0.Main.iutils.MessageUtils;
import kotlin.coroutines.CoroutineContext;
import net.mamoe.mirai.contact.Friend;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.FriendMessageEvent;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.event.events.TempMessageEvent;
import net.mamoe.mirai.message.data.Message;
import net.mamoe.mirai.message.data.MessageChain;
import org.jetbrains.annotations.NotNull;

/**
 * @author github.kloping
 */
@CommentScan(path = "Project.controllers.friends")
public class NoGroupHandler extends SimpleListenerHost {
    public static final StarterObjectApplication APPLICATION = new StarterObjectApplication(NoGroupHandler.class);

    static {
        APPLICATION.setMainKey(Long.class);
        APPLICATION.setWaitTime(25000L);
        APPLICATION.setAccessTypes(MessageEvent.class, Friend.class, Long.class);
        APPLICATION.setAllAfter((t, objs) -> {
            MessageEvent event = (MessageEvent) objs[2];
            if (t != null) {
                if (t instanceof String) {
                    MessageChain message = MessageUtils.INSTANCE.getMessageFromString(t.toString(), event.getSubject());
                    event.getSubject().sendMessage(message);
                } else if (t instanceof Message) {
                    event.getSubject().sendMessage((Message) t);
                }
            }
        });
        APPLICATION.run0(NoGroupHandler.class);
    }

    public NoGroupHandler() {

    }

    public NoGroupHandler(@NotNull CoroutineContext coroutineContext) {
        super(coroutineContext);
    }

    @Override
    public void handleException(@NotNull CoroutineContext context, @NotNull Throwable exception) {
        super.handleException(context, exception);
    }

    @EventHandler
    public void friendEvent(FriendMessageEvent event) {
        long fid = event.getSender().getId();
        String content = MessageSerializer.messageChain2String(event.getMessage());
        APPLICATION.executeMethod(fid, content, event, event.getSender(), fid);
        talk(content, event);
    }

    @EventHandler
    public void friendEvent(TempMessageEvent event) {
        long fid = event.getSender().getId();
        String content = MessageSerializer.messageChain2String(event.getMessage());
        APPLICATION.executeMethod(fid, content, event, event.getSender(), fid);
        talk(content, event);
    }

    public void talk(String str, MessageEvent event) {
//        if (!DataBase.canSpeak(event.getSubject().getId())) return;
//        try {
//            StringBuilder data = new StringBuilder();
//            data.append("Human:").append(str);
//            String rqBody = "{\"prompt\":\"" + data.toString() + "\",\"tokensLength\":0}";
//            Connection connection = Jsoup.connect("https://api.forchange.cn")
//                    .timeout(240000).ignoreContentType(true).ignoreHttpErrors(true).method(Connection.Method.POST).requestBody(rqBody).userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/110.0.0.0 Safari/537.36 Edg/110.0.1587.41").header("Accept", "application/json, text/plain, */*").header("Accept-Encoding", "gzip, deflate, br").header("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8,en-GB;q=0.7,en-US;q=0.6").header("Access-Control-Allow-Origin", "*").header("Connection", "keep-alive").header("Content-Type", "application/json").header("Host", "api.forchange.cn").header("Origin", "https://chat.forchange.cn").header("Referer", "https://chat.forchange.cn/").header("Content-Length", String.valueOf(rqBody.getBytes().length));
//            Document doc0 = connection.post();
//            JSONObject result = JSON.parseObject(doc0.body().text());
//            String text = result.getJSONArray("choices").getJSONObject(0).getString("text");
//            data.append("\nAI:").append(text).append("\n");
//            event.getSubject().sendMessage(text.trim());
//        } catch (IOException e) {
//            e.printStackTrace();
//            event.getSubject().sendMessage(e.getMessage());
//        }
    }
}
