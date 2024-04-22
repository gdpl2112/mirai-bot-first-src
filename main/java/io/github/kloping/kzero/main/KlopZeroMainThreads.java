package io.github.kloping.kzero.main;

import io.github.kloping.file.FileUtils;
import io.github.kloping.kzero.main.api.BotCreated;
import io.github.kloping.kzero.main.api.KZeroBot;
import io.github.kloping.kzero.main.api.KZeroStater;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author github.kloping
 */
public class KlopZeroMainThreads implements Runnable, BotCreated {
    public static ExecutorService EXECUTOR_SERVICE = new ThreadPoolExecutor(5, 5, 0L,
            TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(), r -> new Thread(r));
    public static ExecutorService ONE_EXECUTOR_SERVICE = new ThreadPoolExecutor(1, 1, 0L,
            TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(), r -> new Thread(r));

    public static final Map<String, KZeroBot> BOT_MAP = new HashMap<>();

    public static final Map<String, KlopZeroApplication> APPLICATION_MAP = new HashMap<>();

    @Override
    public void created(KZeroStater stater, KZeroBot kZeroBot) {
        if (BOT_MAP.containsKey(kZeroBot.getId())) return;
        if (APPLICATION_MAP.containsKey(kZeroBot.getId())) return;
        BOT_MAP.put(kZeroBot.getId(), kZeroBot);
        KlopZeroApplication application = new KlopZeroApplication(stater, kZeroBot);
        application.start();
        APPLICATION_MAP.put(kZeroBot.getId(), application);
    }

    @Override
    public void run() {
        String name = ManagementFactory.getRuntimeMXBean().getName();
        String pid = name.split("@")[0];
        FileUtils.putStringInFile(pid, new File("./bot.pid"));
        for (Runnable starter : runnableList) {
            ONE_EXECUTOR_SERVICE.submit(starter);
        }
    }

    public List<Runnable> runnableList = new ArrayList<>();

    public void add(Runnable runnable) {
        runnableList.add(runnable);
    }

    public void add(KZeroStater stater) {
        stater.setCreated(this);
        runnableList.add(stater);
    }
}
