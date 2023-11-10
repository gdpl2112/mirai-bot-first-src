package io.github.kloping.kzero.bot.controllers;

import io.github.kloping.MySpringTool.annotations.*;
import io.github.kloping.MySpringTool.entity.interfaces.Runner;
import io.github.kloping.MySpringTool.exceptions.NoRunException;
import io.github.kloping.MySpringTool.interfaces.QueueExecutor;
import io.github.kloping.date.DateUtils;
import io.github.kloping.kzero.bot.database.DataBase;
import io.github.kloping.kzero.main.api.KZeroBot;
import io.github.kloping.kzero.main.api.MessagePack;
import io.github.kloping.kzero.mirai.exclusive.PluginManagerController;
import io.github.kloping.kzero.mirai.exclusive.WebAuthController;
import io.github.kloping.kzero.spring.dao.GroupConf;
import io.github.kloping.kzero.spring.dao.SweatherData;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author github.kloping
 */
@Controller
public class AllController implements Runner {

    public AllController(QueueExecutor queueExecutor) {
        queueExecutor.setBefore(this);
    }

    @AutoStand
    DataBase dataBase;

    @Override
    public void run(Method method, Object t, Object[] objects)  {
        Class cla = method.getDeclaringClass();
        if (cla == AdminController.class) return;
        if (cla == PluginManagerController.class) return;
        if (cla == WebAuthController.class) return;
        MessagePack pack = (MessagePack) objects[3];
        KZeroBot bot = (KZeroBot) objects[4];
        GroupConf groupConf = dataBase.getConf(pack.getSubjectId());
        if (groupConf != null) {
            if (!groupConf.getOpen()) throw new NoRunException("未开启");
        }
        intercept0(method, pack, bot);
    }

    @AutoStand
    SubscribeController subscribeController;

    @Action("测试")
    public Object test0(String sid, MessagePack pack, KZeroBot bot) throws Exception {
        return subscribeController.futureWeaNow(new SweatherData().setAddress("泗县"));
    }

    private List<String> wakes = new LinkedList<>();

    @CronSchedule("5 1 5 * * ? *")
    public void eveZero() {
        wakes.clear();
    }

    private Map<String, Long> map = new HashMap<>();

    protected boolean isWakeUp(String sid) {
        return wakes.contains(sid);
    }

    private static final int MIN_WAKE_TIME = 1000 * 60 * 60 * 5;
    private static final int MAX_WAKE_TIME = 1000 * 60 * 60 * 12;

    @DefAction
    public void intercept0(Method method, MessagePack pack, KZeroBot bot) {
        String sid = pack.getSenderId();
        if (!wakes.contains(sid)) wakes.add(sid);
        int hour = DateUtils.getHour();
        if (hour >= 5 && hour <= 11) {
            Long ut0 = map.get(sid);
            if (ut0 != null) {
                ut0 = System.currentTimeMillis() - ut0;
                if (ut0 > MIN_WAKE_TIME && ut0 < MAX_WAKE_TIME) {
                    int h = (int) (ut0 / (1000 * 60 * 60));
                    int m = (int) (ut0 % (1000 * 60 * 60)) / (1000 * 60);
                    bot.getAdapter().onResult(method, String.format("推测睡眠时长: %s时%s分", h, m), pack);
                }
            }
        }
        map.put(pack.getSenderId(), System.currentTimeMillis());
    }
}
