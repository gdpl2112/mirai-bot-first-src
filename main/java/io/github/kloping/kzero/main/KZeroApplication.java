package io.github.kloping.kzero.main;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.kloping.MySpringTool.StarterApplication;
import io.github.kloping.MySpringTool.StarterObjectApplication;
import io.github.kloping.MySpringTool.entity.interfaces.Runner;
import io.github.kloping.MySpringTool.exceptions.NoRunException;
import io.github.kloping.MySpringTool.h1.impl.component.FieldManagerImpl;
import io.github.kloping.MySpringTool.interfaces.component.ContextManager;
import io.github.kloping.MySpringTool.interfaces.component.FieldManager;
import io.github.kloping.kzero.bot.interfaces.httpApi.KlopingWeb;
import io.github.kloping.kzero.main.api.BotMessageHandler;
import io.github.kloping.kzero.main.api.KZeroBot;
import io.github.kloping.kzero.main.api.KZeroStater;
import io.github.kloping.kzero.main.api.MessagePack;
import io.github.kloping.kzero.spring.KZeroSpringStarter;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Method;

/**
 * @author github.kloping
 */
public class KZeroApplication implements BotMessageHandler {
    private KZeroStater stater;
    private KZeroBot bot;

    public KZeroApplication(KZeroStater stater, KZeroBot bot) {
        this.stater = stater;
        this.bot = bot;
    }

    public void start() {
        stater.setHandler(this);
        context = KZeroSpringStarter.run(bot.getId());
        start0();
        //spring to auto
        FieldManager fieldManager = application0.INSTANCE.getFieldManager();
        ContextManager contextManager = application0.INSTANCE.getContextManager();
        for (String beanDefinitionName : context.getBeanDefinitionNames()) {
            Object obj = context.getBean(beanDefinitionName);
            if (obj == null) continue;
            if (obj instanceof BaseMapper) {
                contextManager.append(obj);
            } else {
                if (obj.getClass().isAnnotationPresent(RestController.class) ||
                        obj.getClass().isAnnotationPresent(Controller.class)) {
                    contextManager.append(obj);
                }
            }
        }
        if (fieldManager instanceof FieldManagerImpl) {
            FieldManagerImpl fm = (FieldManagerImpl) fieldManager;
//            application0.logger.setLogLevel(3);
            fm.workStand();
//            application0.logger.setLogLevel(0);
        }
    }

    private void start0() {
        application0 = new StarterObjectApplication(KZeroApplication.class);
        application0.setMainKey(String.class);
        application0.setWaitTime(600000L);
        //========================sendId=========type
        application0.setAccessTypes(String.class, MessagePack.class, KZeroBot.class);
        application0.setAllAfter(new Runner() {
            @Override
            public void run(Method method, Object t, Object[] objects) throws NoRunException {
                if (t != null) {
                    MessagePack messagePack = (MessagePack) objects[3];
                    bot.getAdapter().onResult(method, t, messagePack);
                }
            }
        });
        application0.addConfFile("./conf/conf.txt");
        application0.run0(Main.class);
        ContextManager contextManager = application0.INSTANCE.getContextManager();
        application0.STARTED_RUNNABLE.add(() -> verify());
    }

    @Override
    public void onMessage(MessagePack messagePack) {
        application0.executeMethod(bot.getId(), messagePack.getMsg(),
                messagePack.getSenderId(), messagePack, bot);
    }

    private ConfigurableApplicationContext context;

    private StarterObjectApplication application0;

    public void verify() throws RuntimeException {
        String code = application0.INSTANCE.getContextManager().getContextEntity(String.class, "auth_code");
        if (code == null) throw new RuntimeException("没有配置授权码(Authorization not configured) auth_code");
        KlopingWeb kloping = application0.INSTANCE.getContextManager().getContextEntity(KlopingWeb.class);
        String r0 = kloping.verify0(code);
        if (!Boolean.valueOf(false)) {
            try {
                throw new RuntimeException("授权码过期或不可用(Authorization code expired or unavailable)");
            } finally {
                System.exit(0);
            }
        } else StarterApplication.logger.info("授权码验证成功√√√");
    }
}
