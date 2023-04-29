package Project.controllers.gameControllers;

import Project.commons.SpGroup;
import Project.commons.SpUser;
import Project.dataBases.GameDataBase;
import Project.interfaces.Iservice.IChallengeService;
import Project.services.impl.GameServiceImpl;
import io.github.kloping.MySpringTool.annotations.*;
import io.github.kloping.MySpringTool.exceptions.NoRunException;
import io.github.kloping.mirai0.Main.iutils.MessageUtils;
import io.github.kloping.mirai0.unitls.Tools.Tool;

import java.util.ArrayList;
import java.util.List;

import static Project.commons.rt.ResourceSet.FinalFormat.ATT_WAIT_TIPS;
import static Project.commons.rt.ResourceSet.FinalString.NOT_FOUND_AT;
import static Project.commons.rt.ResourceSet.FinalValue.NOT_OPEN_NO_RUN_EXCEPTION;
import static Project.controllers.auto.ControllerSource.challengeDetailService;
import static Project.controllers.auto.ControllerTool.opened;
import static Project.dataBases.GameDataBase.getInfo;
import static io.github.kloping.mirai0.Main.BootstarpResource.println;

/**
 * @author github.kloping
 */
@Controller
public class ChallengeController {

    private static final Object INTRO =
            "挑战说明:\n" +
                    "\t1.挑战时所有魂技冷却为缩短40倍\n" +
                    "\t2.在挑战中死亡不会清除经验/降级\n" +
                    "\t3.挑战中时不可购买物品,不可被转让\n" +
                    "\t4.挑战者的信息将被回满,挑战结束,恢复原样\n" +
                    "\t5.挑战中时可使用背包物品,但使用冷却更长\n" +
                    "\t6.挑战中攻击减半\n" +
                    "\t7.挑战中普通攻击转为'选择攻击'\n" +
                    "命令:\n创建试炼挑战\n挑战<At>\n结束挑战";

    private static List<String> listFx = new ArrayList<>();

    @AutoStand
    IChallengeService service;

    @AutoStand
    GameServiceImpl gameService;

    public ChallengeController() {
        println(this.getClass().getSimpleName() + "构建");
    }

    @Before
    public void before(SpUser qq, SpGroup group, @AllMess String mess) throws NoRunException {
        if (!opened(group.getId(), this.getClass())) {
            throw NOT_OPEN_NO_RUN_EXCEPTION;
        }
        if (getInfo(qq.getId()).getHp() <= 0) {
            if (Tool.INSTANCE.EveListStartWith(listFx, mess) == -1) {
                MessageUtils.INSTANCE.sendMessageInGroupWithAt("无状态", group.getId(), qq.getId());
                throw new NoRunException("无状态");
            }
        }
    }

    @Action("创建试炼挑战")
    private Object o1(SpUser user, SpGroup group) {
        return service.createTrialChallenge(user.getId(), group.getId());
    }

    @Action("创建平衡挑战")
    private Object o2(SpUser user, SpGroup group) {
        return service.createTrial2Challenge(user.getId(), group.getId());
    }

    @Action("挑战.+")
    private Object o4(SpUser user, @AllMess String s, SpGroup group) {
        long qid = Project.utils.Utils.getAtFromString(s);
        if (qid <= 0) {
            s = s.replaceFirst("挑战", "");
            if (GameDataBase.NAME_2_ID_MAPS.containsKey(s)) {
                return service.joinChallenge(user.getId(), s, group);
            } else return NOT_FOUND_AT;
        }
        return service.joinChallenge(user.getId(), qid);
    }

    @Action("结束挑战")
    private Object o5(SpUser user) {
        return service.destroy(user.getId());
    }

    /**
     * 选择攻击的拦截
     *
     * @return
     */
    public Object o3(long qid) {
        long at = getInfo(qid).getAk1();
        if (at > System.currentTimeMillis())
            return String.format(ATT_WAIT_TIPS, Tool.INSTANCE.getTimeTips(at));
        return gameService.attNow(qid, challengeDetailService.challenges.Q2Q.get(qid),
                SpGroup.get(challengeDetailService.challenges.Q2C.get(qid).getGid()), 0);
    }

    @Action("挑战说明")
    private Object o2() {
        return INTRO;
    }
}
