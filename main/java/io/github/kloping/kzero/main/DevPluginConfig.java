package io.github.kloping.kzero.main;

import io.github.kloping.spt.impls.ConfigFileManagerImpl;
import io.github.kloping.spt.impls.LoggerImpl;
import io.github.kloping.spt.impls.ContextManagerWithEIImpl;
import io.github.kloping.spt.interfaces.Logger;
import io.github.kloping.spt.interfaces.component.ConfigFileManager;
import io.github.kloping.spt.interfaces.component.ContextManager;
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
        logger.setLogLevel(1);
        new File("./logs/plugins").mkdirs();
        logger.setOutFile(String.format("./logs/plugins/%s-%s-%s.log", DateUtils.getYear(), DateUtils.getMonth(), DateUtils.getDay()));
        contextManager = new ContextManagerWithEIImpl();
        ConfigFileManager configFileManager = new ConfigFileManagerImpl(contextManager);
        configFileManager.load("./conf/conf.txt");
        System.out.println("已加载 插件配置");
    }
}
