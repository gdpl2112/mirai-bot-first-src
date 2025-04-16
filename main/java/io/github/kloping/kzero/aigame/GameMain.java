package io.github.kloping.kzero.aigame;

import com.alibaba.fastjson.JSONObject;
import io.github.kloping.common.Public;
import io.github.kloping.url.UrlUtils;
import net.mamoe.mirai.console.terminal.MiraiConsoleImplementationTerminal;
import net.mamoe.mirai.console.terminal.MiraiConsoleTerminalLoader;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.GlobalEventChannel;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.QuoteReply;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayInputStream;
import java.nio.file.Paths;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

/**
 * @author github kloping
 * @date 2025/4/10-15:30
 */
public class GameMain extends SimpleListenerHost {
    public static final GameMain INSTANCE = new GameMain();

    public static void main(String[] args) {
        Public.EXECUTOR_SERVICE.submit(() -> {
            MiraiConsoleImplementationTerminal terminal = new MiraiConsoleImplementationTerminal(Paths.get("works"));
            MiraiConsoleTerminalLoader.INSTANCE.startAsDaemon(terminal);
        });
        GlobalEventChannel.INSTANCE.registerListenerHost(INSTANCE);

        GlobalEventChannel.INSTANCE.subscribeAlways(GroupMessageEvent.class, e -> {
            String reply = e.getMessage().contentToString().trim();
            if ("1".equals(reply)) {
                Consumer<Boolean> consumer = confirmActionMap.get(e.getSender().getId());
                if (consumer != null) {
                    confirmActionMap.remove(e.getSender().getId());
                    consumer.accept(true);
                }
            } else if ("0".equals(reply)) {
                Consumer<Boolean> consumer = confirmActionMap.get(e.getSender().getId());
                if (consumer != null) {
                    confirmActionMap.remove(e.getSender().getId());
                    consumer.accept(false);
                    e.getSubject().sendMessage("已取消");
                }
            }
        });

        GlobalEventChannel.INSTANCE.subscribeAlways(GroupMessageEvent.class, e -> {
            String reply = e.getMessage().contentToString().trim();
            Integer n = null;
            try {
                n = Integer.valueOf(reply);
                if (n >= 0 && n <= 9) {
                    Consumer<Integer> consumer = selectActionMap.get(e.getSender().getId());
                    if (consumer == null) return;
                    selectActionMap.remove(e.getSender().getId());
                    consumer.accept(n);
                }
            } catch (NumberFormatException ex) {
            }
        });

        PetModule.load();
    }

    public static final Map<Long, java.util.function.Consumer<Boolean>> confirmActionMap = new java.util.HashMap<>();
    public static final Map<Long, java.util.function.Consumer<Integer>> selectActionMap = new java.util.HashMap<>();

    public static final String URL = "http://127.0.0.1:920";

    public static final RestTemplate TEMPLATE = new RestTemplate();

    public static Set<MatchBean> GLOBAL_MATCHES = new LinkedHashSet<>();

    static {
        TEMPLATE.setErrorHandler(new IgnoreBadRequestErrorHandler());

        GLOBAL_MATCHES.add(MatchBean.create("注册", "使用‘注册{昵称}’来注册\n名字不可空或超过8个字符", (m, s) -> {
            toResult(m, "确定注册名为:'" + s + "'吗? \n后续可使用'改名{新名}'修改\n回复 1 确认 0 取消");
            confirmActionMap.put(m.getSender().getId(), (confirmed) -> {
                if (confirmed) {
                    ResponseEntity<String> data =
                            TEMPLATE.getForEntity(String.format("%s/players/register?id=%s&name=%s", URL, m.getSender().getId(), s), String.class);
                    if (data.getStatusCodeValue() == 200) {
                        toResult(m, "注册成功,请使用`信息`查看");
                    } else {
                        toResult(m, data.getBody());
                    }
                }
            });
        }));

        GLOBAL_MATCHES.add(MatchBean.create("改名", "使用'改名{名字}'来改名,名字不可空或超过8个字符", (m, s) -> {
            toResult(m, "确定改名为:'" + s + "'吗? \n下次修改时间将是14天后\n回复 1 确认 0 取消");
            confirmActionMap.put(m.getSender().getId(), (confirmed) -> {
                if (confirmed) {
                    ResponseEntity<String> data = TEMPLATE.postForEntity(URL + "/players/rename"
                            , MultiValueMapUtils.of("id", m.getSender().getId(), "newName", s), String.class);
                    if (data.getStatusCodeValue() != 200) toResult(m, data.getBody());
                    else showInfo(m, data.getBody());
                }
            });
        }));

        GLOBAL_MATCHES.add(MatchBean.create("信息", (m, s) -> {
            ResponseEntity<String> data = TEMPLATE.getForEntity(
                    String.format("%s/players/show?id=%s", URL, m.getSender().getId()), String.class);
            if (data.getStatusCodeValue() != 200) toResult(m, data.getBody());
            else showInfo(m, data.getBody());
        }));

        GLOBAL_MATCHES.add(MatchBean.create("打工", (m, s) -> {
            ResponseEntity<String> data = TEMPLATE.postForEntity(URL + "/players/work",
                    MultiValueMapUtils.of("id", m.getSender().getId()), String.class);
            if (data.getStatusCodeValue() == 200) {
                JSONObject jo = JSONObject.parseObject(data.getBody());
                toResult(m, jo.getString("data") + "\ntips:" + jo.getString("tips"));
            } else toResult(m, data.getBody());
        }));

    }

    private static void showInfo(MessageEvent m, String data) {
        JSONObject jo = JSONObject.parseObject(data);
        StringBuilder sb = new StringBuilder();
        int exp = jo.getInteger("experience");
        int maxexp = jo.getInteger("requiredExp");
        final int len = 10;
        int progress = (int) ((double) exp / maxexp * len);
        sb.append("👤 名字: ").append(jo.getString("name")).append("\n")
                .append("📊 等级: ").append(jo.getInteger("level")).append("\n")
                .append("🌟 经验: ").append(exp)
                .append("/").append(maxexp).append("\n")
                .append("📈 进度: [");
        for (int i = 0; i < len; i++) {
            if (i < progress) sb.append("█");
            else sb.append("░");
        }
        sb.append("]\n").append("💰 金币: ").append(jo.getInteger("gold"));
        MessageChainBuilder builder = new MessageChainBuilder();
        builder.append(new QuoteReply(m.getMessage()));
        builder.append(Contact.uploadImage(m.getSubject()
                , new ByteArrayInputStream(UrlUtils.getBytesFromHttpUrl(m.getSender().getAvatarUrl()))));
        builder.append(sb.toString());
        m.getSubject().sendMessage(builder.build());
    }

    public static void toResult(MessageEvent m, String string) {
        MessageChainBuilder builder = new MessageChainBuilder();
        builder.append(new QuoteReply(m.getMessage()));
        builder.append(string);
        m.getSubject().sendMessage(builder.build());
    }


    @EventHandler
    public void on(GroupMessageEvent event) {
        if (event.getGroup().getId() == 764663035L) {
            String text = event.getMessage().serializeToMiraiCode();
            if (text != null) {
                text = text.trim();
            }
            for (MatchBean matchBean : GLOBAL_MATCHES) {
                try {
                    Exception operation = MatchBean.operation(text, matchBean, event);
                    if (operation instanceof MatchBean.RuleException) {
                        MessageChainBuilder builder = new MessageChainBuilder();
                        builder.append(new QuoteReply(event.getMessage()));
                        builder.append(operation.getMessage());
                        event.getSubject().sendMessage(builder.build());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
