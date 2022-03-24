package Project.controllers.gameControllers;

import Project.interfaces.Iservice.IChallengeService;
import io.github.kloping.MySpringTool.annotations.*;
import io.github.kloping.MySpringTool.exceptions.NoRunException;
import io.github.kloping.mirai0.Main.ITools.MessageTools;
import io.github.kloping.mirai0.commons.Group;
import io.github.kloping.mirai0.commons.User;
import io.github.kloping.mirai0.commons.game.ChallengeField;
import io.github.kloping.mirai0.commons.game.ChallengeSide;
import io.github.kloping.mirai0.unitls.Tools.Tool;
import io.github.kloping.mirai0.unitls.drawers.GameDrawer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static Project.controllers.auto.ControllerTool.opened;
import static Project.dataBases.GameDataBase.getInfo;
import static io.github.kloping.mirai0.Main.Resource.println;
import static io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet.FinalString.NOT_PARTICIPATION_STR;
import static io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet.FinalString.WAITING_STR;
import static io.github.kloping.mirai0.unitls.Tools.Tool.EveListStartWith;

/**
 * @author github.kloping
 */
@Controller
public class ChallengeController {
    private static final Object INTRO = "挑战说明:\n" +
            "1.挑战采用回合制地图式游戏\n" +
            "2.在挑战中死亡不会清除经验/降级\n" +
            "3.挑战胜利获得一星,失败扣除一星最底0星\n" +
            "4.排行中显示按星数量排行\n" +
            "5.挑战中时不可购买物品,不可被转让\n" +
            "6.挑战中时可以使用一次暗器\n" +
            "7.挑战中时所有攻击存在范围(特殊除外,普通攻击为周围一圈\n" +
            "8.挑战最大回合20回合(之后可能调整\n" +
            "9.";

    public ChallengeController() {
        println(this.getClass().getSimpleName() + "构建");
    }

    private static List<String> listFx = new ArrayList<>();

    @Before
    public void before(User qq, Group group, @AllMess String mess) throws NoRunException {
        if (!opened(group.getId(), this.getClass())) {
            throw new NoRunException("未开启");
        }
        if (getInfo(qq.getId()).getHp() <= 0) {
            if (EveListStartWith(listFx, mess) == -1) {
                MessageTools.sendMessageInGroupWithAt("无状态", group.getId(), qq.getId());
                throw new NoRunException("无状态");
            }
        }
    }

    @AutoStand
    IChallengeService service;

    @Action("人机挑战")
    private Object o1(User user, Group group) {
        return service.startWithBot(user.getId(),group.getId());
    }

    @Action("移动<.+=>str>")
    private Object o2(@Param("str") String str, Group group, User user) {
       return service.moveOnChallenge(user.getId(),str);
    }

    @Action("挑战说明")
    private Object o2() {
        return INTRO;
    }
}
