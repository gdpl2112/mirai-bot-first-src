package io.github.kloping.kzero.main;

import io.github.kloping.MySpringTool.h1.impl.ConfigFileManagerImpl;
import io.github.kloping.MySpringTool.h1.impl.LoggerImpl;
import io.github.kloping.MySpringTool.h1.impl.component.ContextManagerWithEIImpl;
import io.github.kloping.MySpringTool.interfaces.Logger;
import io.github.kloping.MySpringTool.interfaces.component.ConfigFileManager;
import io.github.kloping.MySpringTool.interfaces.component.ContextManager;
import io.github.kloping.date.DateUtils;

import java.io.File;

/**
 * @author github.kloping
 */
public class DevPluginConfig implements Runnable {

    public static DevPluginConfig CONFIG;

    public DevPluginConfig() {
        CONFIG = this;
    }

    public ContextManager contextManager;
    public Logger logger;

    @Override
    public void run() {
        logger = new LoggerImpl();
        new File("./logs/plugins").mkdirs();
        logger.setOutFile(String.format("./logs/plugins/%s-%s-%s.log", DateUtils.getYear(), DateUtils.getMonth(), DateUtils.getDay()));
        contextManager = new ContextManagerWithEIImpl();
        ConfigFileManager configFileManager = new ConfigFileManagerImpl(contextManager);
        configFileManager.load("./conf/conf.txt");
        System.out.println("已加载 插件配置");
    }
}
