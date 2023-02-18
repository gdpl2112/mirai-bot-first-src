package Project.listeners;

import Project.interfaces.httpApi.Magiconch;
import io.github.kloping.MySpringTool.annotations.AutoStand;
import io.github.kloping.MySpringTool.annotations.Entity;
import io.github.kloping.mirai0.Main.iutils.EventUtils;
import io.github.kloping.mirai0.commons.apiEntitys.magiconch.MagiconchNbnhhshRequest;
import io.github.kloping.mirai0.commons.apiEntitys.magiconch.MagiconchNbnhhshResponse;
import kotlin.coroutines.CoroutineContext;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.FriendMessageEvent;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.QuoteReply;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

import static io.github.kloping.mirai0.Main.iutils.EventUtils.messageEvent2String;

/**
 * @author github.kloping
 */
@Entity
public class NbListener extends SimpleListenerHost {
    private static final Map<String, String> HEADER = new HashMap<>();

    static {
        HEADER.put("content-type", "application/json");
    }

    @AutoStand
    Magiconch magiconch;
    private long guessCd = System.currentTimeMillis();

    public NbListener() {
    }

    @Override
    public void handleException(@NotNull CoroutineContext context, @NotNull Throwable exception) {
        super.handleException(context, exception);
    }

    @EventHandler
    public void onMessage(@NotNull GroupMessageEvent event) throws Exception {
//        if (!ControllerTool.canGroup(event.getSubject().getId())) {
//            return;
//        }
//        touch(event);
    }

    private void touch(MessageEvent event) {
        if (guessCd > System.currentTimeMillis()) {
            return;
        }
        String a = EventUtils.messageEvent2String(event);
        if (a.matches("[a-z]+")) {
            MagiconchNbnhhshResponse[] responses = magiconch.trans(new MagiconchNbnhhshRequest(a), HEADER);
            guessCd = System.currentTimeMillis() + 10000L;
            if (responses != null && responses.length >= 1 && responses[0].getTrans() != null) {
                if (!responses[0].getTrans()[0].equalsIgnoreCase(a)) {
                    MessageChainBuilder mcb = new MessageChainBuilder();
                    mcb.append(new QuoteReply(event.getSource()))
                            .append(new At(event.getSender().getId())).append("\n");
                    for (MagiconchNbnhhshResponse response : responses) {
                        mcb.append(response.getName()).append("\n-释义结果:");
                        for (String tran : response.getTrans()) {
                            mcb.append(tran).append("; ");
                        }
                        mcb.append("\n");
                    }
                    event.getSubject().sendMessage(mcb.build());
                }
            }
        }
    }

    @EventHandler
    public void onMessage(@NotNull FriendMessageEvent event) throws Exception {
        touch(event);
    }
}
