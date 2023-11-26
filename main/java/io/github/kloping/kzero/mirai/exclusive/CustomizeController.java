package io.github.kloping.kzero.mirai.exclusive;

import io.github.kloping.common.Public;
import io.github.kloping.file.FileUtils;
import io.github.kloping.judge.Judge;
import io.github.kloping.kzero.main.api.MessageSerializer;
import io.github.kloping.kzero.mirai.exclusive.script.BaseMessageScriptContext;
import io.github.kloping.kzero.mirai.exclusive.script.BaseScriptUtils;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.event.events.MessagePostSendEvent;
import net.mamoe.mirai.event.events.MessagePreSendEvent;
import net.mamoe.mirai.message.data.MessageChain;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.client.RestTemplate;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.File;

/**
 * @author github.kloping
 */
public class CustomizeController extends SimpleListenerHost {

    private static final ScriptEngineManager SCRIPT_ENGINE_MANAGER = new ScriptEngineManager();


    private MessageSerializer<MessageChain> serializer;

    private RestTemplate template = new RestTemplate();

    public CustomizeController(MessageSerializer<MessageChain> serializer) {
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
                ScriptEngine javaScript = SCRIPT_ENGINE_MANAGER.getEngineByName("JavaScript");
                javaScript.put("context", new BaseMessageScriptContext(event, serializer));
                javaScript.put("utils", new BaseScriptUtils(event.getBot().getId(), template, serializer));
                String msg = serializer.serialize(event.getMessage());
                javaScript.put("msg", msg);
                javaScript.eval(code);
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
}
