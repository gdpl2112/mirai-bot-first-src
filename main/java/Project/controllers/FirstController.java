package Project.controllers;

import Project.controllers.auto.ControllerSource;
import Project.dataBases.DataBase;
import io.github.kloping.MySpringTool.annotations.*;
import io.github.kloping.MySpringTool.exceptions.NoRunException;
import io.github.kloping.mirai0.Main.BotStarter;
import io.github.kloping.mirai0.commons.Group;
import io.github.kloping.mirai0.commons.User;
import io.github.kloping.spt.RedisOperate;

import java.util.List;

import static Project.controllers.auto.TimerController.ZERO_RUNS;

/**
 * @author github-kloping
 */
@Controller
public class FirstController {

    @Before
    public void before(@AllMess String mess, Group group, User qq) throws NoRunException {
        if (!BotStarter.test) {
            throw new NoRunException("not test");
        }
    }

    public static final String KEY0 = "key0";

    static {
        ZERO_RUNS.add(() -> {
            List<Long> list = ControllerSource.firstController.redisOperate.getValue(KEY0);
            ControllerSource.firstController.redisOperate.delKey(KEY0);
        });
    }

    @AutoStand
    public RedisOperate<List<Long>> redisOperate;


    @Action("领取积分")
    public synchronized String a0(User user) {
        List<Long> list = redisOperate.getValue(KEY0);
        if (list.contains(user.getId())) {
            return "已领取";
        } else {
            list.add(user.getId());
            redisOperate.setValue(KEY0, list);
            DataBase.addScore(10000000L, user.getId());
        }
        return "体验服专属,每日可领取一次,体验服数据随时可能删除";
    }

}
