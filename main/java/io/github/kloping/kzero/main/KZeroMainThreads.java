package io.github.kzero.main;

import io.github.kzero.main.api.BotCreated;
import io.github.kzero.main.api.KZeroBot;
import io.github.kzero.main.api.KZeroStater;

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
    public List<KZeroStater> starters = new ArrayList<>();
    public Map<String, KZeroBot> botMap = new HashMap<>();
    public Map<String, KZeroApplication> applicationMap = new HashMap<>();

    @Override
    public void created(KZeroStater stater, KZeroBot kZeroBot) {
        botMap.put(kZeroBot.getId(), kZeroBot);
        KZeroApplication application = new KZeroApplication(stater, kZeroBot);
        application.start();
        applicationMap.put(kZeroBot.getId(), application);
    }

    @Override
    public void run() {
        for (KZeroStater starter : starters) {
            EXECUTOR_SERVICE.submit(starter);
        }
    }

    public void add(KZeroStater stater) {
        stater.setCreated(this);
        starters.add(stater);
    }
}
