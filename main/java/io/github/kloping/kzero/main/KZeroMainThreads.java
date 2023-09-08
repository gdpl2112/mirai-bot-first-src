package io.github.kloping.kzero.main;

import io.github.kloping.kzero.main.api.BotCreated;
import io.github.kloping.kzero.main.api.KZeroBot;
import io.github.kloping.kzero.main.api.KZeroStater;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author github.kloping
 */
public class KZeroMainThreads implements Runnable, BotCreated {
    public static ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(10);
    public static final Map<String, KZeroBot> BOT_MAP = new HashMap<>();
    public static final Map<String, KZeroApplication> APPLICATION_MAP = new HashMap<>();
    @Override
    public void created(KZeroStater stater, KZeroBot kZeroBot) {
        if (BOT_MAP.containsKey(kZeroBot.getId())) return;
        if (APPLICATION_MAP.containsKey(kZeroBot.getId())) return;
        BOT_MAP.put(kZeroBot.getId(), kZeroBot);
        KZeroApplication application = new KZeroApplication(stater, kZeroBot);
        application.start();
        APPLICATION_MAP.put(kZeroBot.getId(), application);
    }

    @Override
    public void run() {
        for (Runnable starter : runnableList) {
            EXECUTOR_SERVICE.submit(starter);
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
