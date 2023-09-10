package io.github.kloping.kzero.bot.controllers;

import io.github.kloping.MySpringTool.annotations.Action;
import io.github.kloping.MySpringTool.annotations.AutoStand;
import io.github.kloping.MySpringTool.annotations.Controller;
import io.github.kloping.MySpringTool.entity.interfaces.Runner;
import io.github.kloping.MySpringTool.exceptions.NoRunException;
import io.github.kloping.MySpringTool.interfaces.QueueExecutor;
import io.github.kloping.kzero.bot.database.DataBase;
import io.github.kloping.kzero.main.api.MessagePack;
import io.github.kloping.kzero.mirai.exclusive.PluginManagerController;
import io.github.kloping.kzero.mirai.exclusive.WebAuthController;
import io.github.kloping.kzero.spring.dao.GroupConf;

import java.lang.reflect.Method;

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
    public void run(Method method, Object t, Object[] objects) throws NoRunException {
        Class cla = method.getDeclaringClass();
        if (cla == AdminController.class) return;
        if (cla == PluginManagerController.class) return;
        if (cla == WebAuthController.class) return;
        MessagePack pack = (MessagePack) objects[3];
        GroupConf groupConf = dataBase.getConf(pack.getSubjectId());
        if (groupConf != null) {
            if (!groupConf.getOpen()) throw new NoRunException("未开启");
        }
    }

    @Action("测试")
    public String test0() {
        return "<pic:http://kloping.top/temp/2023/9/6/ec32b4b0-3f40-4741-be39-f88d7b5a0a6a.jpg>";
    }
}
