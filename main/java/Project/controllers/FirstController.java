package Project.controllers;

import Project.aSpring.SpringStarter;
import Project.aSpring.mcs.controllers.RestController0;
import Project.dataBases.DataBase;
import Project.interfaces.Iservice.IGameJoinAcService;
import Project.interfaces.httpApi.KlopingWeb;
import io.github.kloping.MySpringTool.annotations.*;
import io.github.kloping.MySpringTool.exceptions.NoRunException;
import io.github.kloping.mirai0.Main.BotStarter;
import io.github.kloping.mirai0.commons.SpGroup;
import io.github.kloping.mirai0.commons.SpUser;

import static Project.controllers.auto.TimerController.ZERO_RUNS;

/**
 * @author github-kloping
 */
@Controller
public class FirstController {

    @Before
    public void before(@AllMess String mess, SpGroup group, SpUser qq) throws NoRunException {
        if (!BotStarter.test) {
            throw new NoRunException("not test");
        }
    }

    public static final String PWD = "bot-test-pwd0";
    public static final String C0 = "ToReceiveThe";

    @AutoStand
    static KlopingWeb klopingWeb;

    static {
        ZERO_RUNS.add(() -> {
            klopingWeb.del("", PWD);
        });
    }


    @Action("领取积分")
    public synchronized String a0(SpUser user) {
        String qid = String.valueOf(user.getId());
        String oid = klopingWeb.get(qid, PWD);
        if (oid.equals(C0)) {
            return "已领取";
        } else {
            klopingWeb.put(qid, C0, PWD);
            DataBase.addScore(10000000L, user.getId());
        }
        return "体验服专属,每日可领取一次,体验服数据随时可能删除";
    }

    @AutoStand
    IGameJoinAcService gameJoinAcService;

    @Action("测试")
    public Object c0(SpGroup group, long who, SpUser qq) {
        return null;
    }

}
