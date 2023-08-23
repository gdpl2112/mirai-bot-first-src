package Project.controllers;

import Project.commons.SpGroup;
import Project.commons.SpUser;
import Project.dataBases.DataBase;
import Project.interfaces.Iservice.IGameJoinAcService;
import Project.interfaces.httpApi.KlopingWeb;
import io.github.kloping.MySpringTool.annotations.*;
import io.github.kloping.MySpringTool.exceptions.NoRunException;
import io.github.kloping.mirai0.Main.BotStarter;
import Project.aSpring.dao.PersonInfo;
import Project.aSpring.dao.WhInfo;

import static Project.controllers.auto.TimerController.ZERO_RUNS;
import static Project.dataBases.GameDataBase.getInfo;

/**
 * @author github-kloping
 */
@Controller
public class FirstController {

    public static final String PWD = "bot-test-pwd0";
    public static final String C0 = "ToReceiveThe";
    @AutoStand
    static KlopingWeb klopingWeb;

    static {
        ZERO_RUNS.add(() -> {
            klopingWeb.del("", PWD);
        });
    }

    @AutoStand
    IGameJoinAcService gameJoinAcService;

    @Before
    public void before(@AllMess String mess, SpGroup group, SpUser qq) throws NoRunException {
        if (!BotStarter.test) {
            throw new NoRunException("not test");
        }
    }

    @Action("激活第二武魂1")
    private String active(long q) {
        PersonInfo pinfo = getInfo(q);
        if (pinfo.getWhc() == 1) {
            WhInfo whInfo = new WhInfo();
            whInfo.setQid(q);
            whInfo.setP(2);
            whInfo.create();
            return "激活成功!";
        } else {
            return "已经激活!";
        }
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

    @Action("测试")
    public Object c0(SpGroup group, long who, SpUser qq) {
        return null;
    }
}
