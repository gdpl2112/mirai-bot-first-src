package io.github.kzero.bot.controllers;

import io.github.kloping.MySpringTool.annotations.AutoStand;
import io.github.kloping.MySpringTool.annotations.Controller;
import io.github.kloping.MySpringTool.entity.interfaces.Runner;
import io.github.kloping.MySpringTool.exceptions.NoRunException;
import io.github.kloping.MySpringTool.interfaces.QueueExecutor;
import io.github.kzero.bot.database.DataBase;
import io.github.kzero.main.api.KZeroBot;
import io.github.kzero.main.api.MessagePack;
import io.github.kzero.spring.dao.GroupConf;

import java.lang.reflect.Method;

/**
 * @author github.kloping
 */
@Controller
public class AllController implements Runner {
    private QueueExecutor queueExecutor;

    public AllController(QueueExecutor queueExecutor, KZeroBot bot) {
        this.queueExecutor = queueExecutor;
        queueExecutor.setBefore(this);
    }

    @AutoStand
    DataBase dataBase;

    @Override
    public void run(Method method, Object t, Object[] objects) throws NoRunException {
        Class cla = method.getDeclaringClass();
        MessagePack pack = (MessagePack) objects[3];
        GroupConf groupConf = dataBase.getConf(pack.getSubjectId());
        if (groupConf != null) {
            if (!groupConf.getOpen()) throw new NoRunException("未开启");
        }
    }
}
