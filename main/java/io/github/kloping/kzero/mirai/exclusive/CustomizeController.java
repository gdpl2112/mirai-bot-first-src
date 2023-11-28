package io.github.kloping.kzero.mirai.exclusive;

import io.github.kloping.common.Public;
import io.github.kloping.file.FileUtils;
import io.github.kloping.judge.Judge;
import io.github.kloping.kzero.main.api.MessageSerializer;
import io.github.kloping.kzero.mirai.exclusive.script.BaseMessageScriptContext;
import io.github.kloping.kzero.mirai.exclusive.script.BaseScriptUtils;
import io.github.kloping.kzero.mirai.exclusive.script.ScriptContext;
import io.github.kloping.url.UrlUtils;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.User;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.*;
import net.mamoe.mirai.message.data.ForwardMessageBuilder;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.Message;
import net.mamoe.mirai.message.data.MessageChain;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.client.RestTemplate;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.ByteArrayInputStream;
import java.io.File;

/**
 * @author github.kloping
 */
public class CustomizeController extends SimpleListenerHost {

    private static final ScriptEngineManager SCRIPT_ENGINE_MANAGER = new ScriptEngineManager();

    private MessageSerializer<Message> serializer;

    private RestTemplate template = new RestTemplate();

    public CustomizeController(MessageSerializer<Message> serializer) {
        this.serializer = serializer;
    }

    @EventHandler
    public void onMessage(@NotNull MessageEvent event) {
        if (event instanceof MessagePreSendEvent) return;
        if (event instanceof MessagePostSendEvent) return;
        final String code = getScriptCode(event.getBot().getId());
        if (Judge.isEmpty(code)) return;
        Public.EXECUTOR_SERVICE.submit(() -> {
            try {
                ScriptEngine engine = BaseScriptUtils.BID_2_SCRIPT_ENGINE.get(event.getBot().getId());
                if (engine == null) {
                    engine = SCRIPT_ENGINE_MANAGER.getEngineByName("JavaScript");
                    BaseScriptUtils.BID_2_SCRIPT_ENGINE.put(event.getBot().getId(), engine);
                }
                engine.eval("function importJ(c){context.imports(c)}");
                engine.put("context", new BaseMessageScriptContext(event, serializer));
                engine.put("utils", new BaseScriptUtils(event.getBot().getId(), template, serializer));
                String msg = serializer.serialize(event.getMessage());
                engine.put("msg", msg);
                engine.eval(code);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        });
    }

    @EventHandler
    public void onEvent(BotEvent event) {
        if (event instanceof MessageEvent) return;
        if (event instanceof MessagePreSendEvent) return;
        if (event instanceof MessagePostSendEvent) return;
        if (event instanceof BotOnlineEvent) return;
        if (event instanceof BotOfflineEvent) return;
        final String code = getScriptCode(event.getBot().getId());
        if (code == null) return;
        Public.EXECUTOR_SERVICE.submit(() -> {
            try {
                ScriptEngine engine = BaseScriptUtils.BID_2_SCRIPT_ENGINE.get(event.getBot().getId());
                if (engine == null) {
                    engine = SCRIPT_ENGINE_MANAGER.getEngineByName("JavaScript");
                    BaseScriptUtils.BID_2_SCRIPT_ENGINE.put(event.getBot().getId(), engine);
                }
                engine.eval("function importJ(c){context.imports(c)}");
                engine.put("context", new BasebBotEventScriptContext(event, serializer));
                engine.put("utils", new BaseScriptUtils(event.getBot().getId(), template, serializer));
                engine.put("msg", event.toString());
                engine.put("event", event);
                engine.eval(code);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        });
    }

    private String getScriptCode(long id) {
        File file = new File("./scripts/b-" + id + ".js");
        file.getParentFile().mkdirs();
        return FileUtils.getStringFromFile(file.getAbsolutePath());
    }


    public static class BasebBotEventScriptContext implements ScriptContext {
        private BotEvent event;
        private MessageSerializer<Message> serializer;

        public BasebBotEventScriptContext(BotEvent event, MessageSerializer<Message> serializer) {
            this.event = event;
            this.serializer = serializer;
        }

        @Override
        public MessageChain getRaw() {
            return null;
        }

        @Override
        public void send(String str) {

        }

        @Override
        public void send(Message message) {

        }

        @Override
        public Bot getBot() {
            return event.getBot();
        }

        @Override
        public User getSender() {
            return null;
        }

        @Override
        public Contact getSubject() {
            return null;
        }

        @Override
        public ForwardMessageBuilder forwardBuilder() {
            return new ForwardMessageBuilder(event.getBot().getAsFriend());
        }

        @Override
        public Message deSerialize(String msg) {
            return serializer.deserialize(msg);
        }

        @Override
        public Image uploadImage(String url) {
            try {
                byte[] bytes = UrlUtils.getBytesFromHttpUrl(url);
                Image image = Contact.uploadImage(event.getBot().getAsFriend(), new ByteArrayInputStream(bytes));
                return image;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        public String getType() {
            return event.getClass().getSimpleName();
        }
    }

}
