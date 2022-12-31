package Project.controllers.recr;

import io.github.kloping.MySpringTool.annotations.Action;
import io.github.kloping.MySpringTool.annotations.AllMess;
import io.github.kloping.MySpringTool.annotations.Before;
import io.github.kloping.MySpringTool.annotations.Controller;
import io.github.kloping.MySpringTool.exceptions.NoRunException;
import io.github.kloping.date.DateUtils;
import io.github.kloping.mirai0.commons.Group;
import io.github.kloping.mirai0.unitls.Tools.Tool;

import static Project.controllers.auto.ControllerTool.opened;
import static io.github.kloping.mirai0.Main.Resource.println;
import static io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet.FinalValue.NOT_OPEN_NO_RUN_EXCEPTION;

/**
 * @author github.kloping
 */
@Controller

public class TalkWithTimeController {
    public TalkWithTimeController() {
        println(this.getClass().getSimpleName() + "构建");

    }

    @Before
    public void before(@AllMess String mess, Group group) throws NoRunException {
        if (!opened(group.getId(), this.getClass())) {
            throw NOT_OPEN_NO_RUN_EXCEPTION;
        }
    }


    private static final String[] YD11 = {"元旦快乐!"
            , "元旦了，我送了一串礼物。快乐送给开心的人，幸福送给有情的人，希望送给等待的人，成功送给奋斗的人，祝福送给正看消息的人!"
            , "辞旧迎新元旦到，祝福赶忙报个到，快乐进入人生道，幸福惬意心里倒!"
            , "元旦快乐呦"
            , "元旦快乐呀~"
            , "元旦到了,祝您在新的年份里,更加进步,事事如意,步步高升,无忧无虑!"
            , "祝大家在新的年份里,运气超欧,抽奖不歪,要啥有啥!"
    };

    @Action(".*?元旦.*?")
    public String yuanDan11() {
        if (DateUtils.getMonth() == 1 && DateUtils.getDay() == 1) {
            return Tool.tool.getRandT(YD11);
        }
        return null;
    }
}
