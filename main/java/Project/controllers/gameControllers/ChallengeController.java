package Project.controllers.gameControllers;

import Project.interfaces.Iservice.IChallengeService;
import Project.services.impl.GameServiceImpl;
import io.github.kloping.MySpringTool.annotations.*;
import io.github.kloping.MySpringTool.exceptions.NoRunException;
import io.github.kloping.mirai0.Main.ITools.MessageTools;
import io.github.kloping.mirai0.commons.Group;
import io.github.kloping.mirai0.commons.User;

import java.util.ArrayList;
import java.util.List;

import static Project.controllers.auto.ControllerTool.opened;
import static Project.dataBases.GameDataBase.getInfo;
import static Project.services.detailServices.ChallengeDetailService.A2R;
import static Project.services.detailServices.ChallengeDetailService.WILL_GO;
import static io.github.kloping.mirai0.Main.BotStarter.test;
import static io.github.kloping.mirai0.Main.Resource.println;
import static io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet.FinalFormat.ATT_WAIT_TIPS;
import static io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet.FinalString.NOT_FOUND_AT;
import static io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet.FinalValue.NOT_OPEN_NO_RUN_EXCEPTION;
import static io.github.kloping.mirai0.unitls.Tools.Tool.EveListStartWith;
import static io.github.kloping.mirai0.unitls.Tools.Tool.getTimeTips;

/**
 * @author github.kloping
 */
@Controller
public class ChallengeController {

    private static final Object INTRO = "挑战说明:\n" +
            "1.挑战时所有魂技冷却为缩短40倍\n" +
            "2.在挑战中死亡不会清除经验/降级\n" +
            "3.挑战胜利获得一星,失败扣除一星最底0星\n" +
            "4.排行中显示按星数量排行\n" +
            "5.挑战中时不可购买物品,不可被转让\n" +
            "6.挑战中时攻击值减半\n" +
            "7.挑战者的攻击将被平均,挑战结束,恢复原样\n" +
            "8.挑战中时可使用背包物品,但使用冷却更长\n"
            ;

    private static List<String> listFx = new ArrayList<>();

    @AutoStand
    IChallengeService service;

    @AutoStand
    GameServiceImpl gameService;

    public ChallengeController() {
        println(this.getClass().getSimpleName() + "构建");
    }

    @Before
    public void before(User qq, Group group, @AllMess String mess) throws NoRunException {
        if (!test) {
            throw new NoRunException("未开放");
        }
        if (!opened(group.getId(), this.getClass())) {
            throw NOT_OPEN_NO_RUN_EXCEPTION;
        }
        if (getInfo(qq.getId()).getHp() <= 0) {
            if (EveListStartWith(listFx, mess) == -1) {
                MessageTools.sendMessageInGroupWithAt("无状态", group.getId(), qq.getId());
                throw new NoRunException("无状态");
            }
        }
    }

    @Action("创建挑战")
    private Object o1(User user, Group group) {
        return service.createChallenge(user.getId(), group.getId());
    }

    @Action("挑战.+")
    private Object o4(User user, @AllMess String s) {
        long qid = MessageTools.getAtFromString(s);
        if (qid <= 0) {
            return NOT_FOUND_AT;
        }
        return service.joinChallenge(user.getId(), qid);
    }

    /**
     * 选择攻击的拦截
     *
     * @return
     */
    public Object o3(long qid) {
        long at = getInfo(qid).getAk1();
        if (at > System.currentTimeMillis())
            return String.format(ATT_WAIT_TIPS, getTimeTips(at));
        return gameService.attNow(qid, A2R.get(qid), Group.get(WILL_GO.get(qid)), 0);
    }

    @Action("挑战说明")
    private Object o2() {
        return INTRO;
    }
}
