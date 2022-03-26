package Project.controllers.gameControllers;

import Project.interfaces.Iservice.IChallengeService;
import io.github.kloping.MySpringTool.annotations.*;
import io.github.kloping.MySpringTool.exceptions.NoRunException;
import io.github.kloping.mirai0.Main.ITools.MessageTools;
import io.github.kloping.mirai0.commons.Group;
import io.github.kloping.mirai0.commons.User;

import java.util.ArrayList;
import java.util.List;

import static Project.controllers.auto.ControllerTool.opened;
import static Project.dataBases.GameDataBase.getInfo;
import static io.github.kloping.mirai0.Main.BotStarter.test;
import static io.github.kloping.mirai0.Main.Resource.println;
import static io.github.kloping.mirai0.unitls.Tools.Tool.EveListStartWith;

/**
 * @author github.kloping
 */
@Controller
public class ChallengeController {
    private static final Object INTRO = "挑战说明:\n" +
            "1.挑战时所有魂技冷却为缩短60倍\n" +
            "2.在挑战中死亡不会清除经验/降级\n" +
            "3.挑战胜利获得一星,失败扣除一星最底0星\n" +
            "4.排行中显示按星数量排行\n" +
            "5.挑战中时不可购买物品,不可被转让\n" +
            "6.挑战中时攻击值减半\n" +
            "9.";

    public ChallengeController() {
        println(this.getClass().getSimpleName() + "构建");
    }

    private static List<String> listFx = new ArrayList<>();

    @Before
    public void before(User qq, Group group, @AllMess String mess) throws NoRunException {
        if (!test) {
            throw new NoRunException("未开放");
        }
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
        return service.startWithBot(user.getId(), group.getId());
    }
    
    /**
     * 选择攻击的拦截
     *
     * @return
     */
    public Object o3(long qid) {
        return "";
    }

    @Action("挑战说明")
    private Object o2() {
        return INTRO;
    }
}
