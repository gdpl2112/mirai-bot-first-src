package io.github.kloping.kzero.main;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.kloping.kzero.bot.controllers.fs.Fs;
import io.github.kloping.kzero.main.api.BotMessageHandler;
import io.github.kloping.kzero.main.api.KZeroBot;
import io.github.kloping.kzero.main.api.KZeroStater;
import io.github.kloping.kzero.main.api.MessagePack;
import io.github.kloping.kzero.spring.KZeroSpringStarter;
import io.github.kloping.spt.Setting;
import io.github.kloping.spt.StarterObjectApplication;
import io.github.kloping.spt.entity.interfaces.Runner;
import io.github.kloping.spt.exceptions.NoRunException;
import io.github.kloping.spt.impls.ActionManagerImpl;
import io.github.kloping.spt.impls.FieldManagerImpl;
import io.github.kloping.spt.interfaces.component.ContextManager;
import io.github.kloping.spt.interfaces.component.FieldManager;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @author github.kloping
 */
public class KlopZeroApplication implements BotMessageHandler {
    private KZeroStater stater;
    private KZeroBot bot;
    private Boolean state = false;

    public KlopZeroApplication(KZeroStater stater, KZeroBot bot) {
        this.stater = stater;
        this.bot = bot;
    }

    public static final String[] REQUIRED_PROPERTY = {"auth.pwd", "cmd.reboot", "cmd.update", "cmd.update-m"};

    public ConfigurableApplicationContext context;

    public StarterObjectApplication application0;

    public void start(Class<?> start) {
        stater.setHandler(bot, this);
        context = KZeroSpringStarter.run(bot.getId());
        start0(start);
        //spring to auto
        FieldManager fieldManager = application0.INSTANCE.getFieldManager();
        ContextManager contextManager = application0.INSTANCE.getContextManager();
        contextManager.append(context);
        for (String beanDefinitionName : context.getBeanDefinitionNames()) {
            Object obj = context.getBean(beanDefinitionName);
            if (obj == null) continue;
            if (obj instanceof BaseMapper) {
                contextManager.append(obj);
            } else if (obj instanceof Fs) {
                contextManager.append(obj);
            } else {
                if (obj.getClass().isAnnotationPresent(RestController.class)
                        || obj.getClass().isAnnotationPresent(Controller.class)
                        || obj.getClass().isAnnotationPresent(Service.class)
                ) {
                    contextManager.append(obj);
                }
            }
        }
        for (String s : REQUIRED_PROPERTY) {
            String v = context.getEnvironment().getProperty(s);
            if (v != null) contextManager.append(v, s);
        }

        if (fieldManager instanceof FieldManagerImpl) {
            FieldManagerImpl fm = (FieldManagerImpl) fieldManager;
            application0.logger.setLogLevel(3);
            fm.workStand();
            application0.logger.setLogLevel(0);
        }
        application0.logger.info(String.format("All services of the bot(%s) are started!", bot.getId()));
        state = true;
    }

    private void start0(Class<?> start) {
        application0 = new StarterObjectApplication(KlopZeroApplication.class);
        application0.logger.setPrefix(String.format("[bot-%s]", bot.getId()));
        application0.setMainKey(String.class);
        application0.setWaitTime(600000L);
        //========================sendId=========type
        application0.setAccessTypes(String.class, MessagePack.class, KZeroBot.class);
        application0.setAllAfter(new Runner() {
            @Override
            public void run(Method method, Object t, Object[] objects) throws NoRunException {
                if (t != null) {
                    MessagePack messagePack = (MessagePack) objects[3];
                    KZeroBot bot = (KZeroBot) objects[4];
                    bot.getAdapter().onResult(method, t, messagePack);
                }
            }
        });
        application0.addConfFile("./conf/conf.txt");
        application0.INSTANCE.getContextManager().append(bot);
        application0.INSTANCE.getContextManager().append(stater);
        application0.run0(start);
        ContextManager contextManager = application0.INSTANCE.getContextManager();
        application0.STARTED_RUNNABLE.add(() -> verify());
    }

    @Override
    public void onMessage(MessagePack pack) {
        if (!state) return;
        application0.executeMethod(bot.getId(), pack.getMsg(), pack.getSenderId(), pack, bot);
    }

    public void verify() throws RuntimeException {
    }
}
