package Project.controllers.gameControllers;

import io.github.kloping.MySpringTool.annotations.AllMess;
import io.github.kloping.MySpringTool.annotations.Before;
import io.github.kloping.MySpringTool.annotations.Controller;
import io.github.kloping.MySpringTool.exceptions.NoRunException;
import io.github.kloping.mirai0.commons.Group;
import io.github.kloping.mirai0.commons.User;
import io.github.kloping.mirai0.Main.ITools.MessageTools;

import java.util.ArrayList;
import java.util.List;

import static Project.controllers.auto.ControllerTool.opened;
import static Project.dataBases.GameDataBase.getInfo;
import static io.github.kloping.mirai0.Main.Resource.println;
import static io.github.kloping.mirai0.unitls.Tools.Tool.EveListStartWith;

/**
 * @author github.kloping
 */
@Controller
public class ChallengeController {
    public ChallengeController() {
        println(this.getClass().getSimpleName() + "构建");
    }


    private static List<String> listFx = new ArrayList<>();

    static {

    }

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
}
