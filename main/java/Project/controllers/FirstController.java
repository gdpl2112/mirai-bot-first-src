package Project.controllers;

import Project.dataBases.DataBase;
import Project.interfaces.Iservice.IGameJoinAcService;
import Project.interfaces.http_api.KlopingWeb;
import io.github.kloping.MySpringTool.annotations.*;
import io.github.kloping.MySpringTool.exceptions.NoRunException;
import io.github.kloping.mirai0.Main.BotStarter;
import io.github.kloping.mirai0.Main.Resource;
import io.github.kloping.mirai0.commons.Group;
import io.github.kloping.mirai0.commons.User;
import io.github.kloping.mirai0.unitls.Tools.Tool;

import static Project.controllers.auto.TimerController.ZERO_RUNS;
import static Project.skill.SkillFactory.ghostSkillNum;
import static io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet.FinalString.NEWLINE;

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

    public static final String PWD = "bot-test-pwd0";
    public static final String C0 = "ToReceiveThe";

    @AutoStand
    static KlopingWeb klopingWeb;

    static {
        Resource.START_AFTER.add(() -> {
            klopingWeb.del("", PWD);
        });
        ZERO_RUNS.add(() -> {
            klopingWeb.del("", PWD);
        });
    }


    @Action("领取积分")
    public synchronized String a0(User user) {
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
    public Object c0(Group group, long who, User qq) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 100; i++) {
            int id0 = Tool.tool.RANDOM.nextInt(ghostSkillNum - 3);
            sb.append(id0).append(NEWLINE);
        }
        return sb.toString();
    }

}
