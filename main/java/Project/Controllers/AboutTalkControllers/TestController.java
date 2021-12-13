package Project.Controllers.AboutTalkControllers;

import Entitys.Group;
import io.github.kloping.Mirai.Main.BotStarter;
import io.github.kloping.MySpringTool.annotations.AllMess;
import io.github.kloping.MySpringTool.annotations.Before;
import io.github.kloping.MySpringTool.annotations.Controller;
import io.github.kloping.MySpringTool.exceptions.NoRunException;

import static Project.Controllers.ControllerTool.CanGroup;
import static io.github.kloping.Mirai.Main.Resource.Switch.AllK;
import static io.github.kloping.Mirai.Main.Resource.println;

@Controller
public class TestController {
    public TestController() {
        println(this.getClass().getSimpleName() + "构建");
    }


    @Before
    public void before(Group group, long qq, @AllMess String s) throws NoRunException {
        if (!AllK)
            throw new NoRunException();
        if (!CanGroup(group.getId())) {
            throw new NoRunException();
        }
        if (!BotStarter.test)
            throw new NoRunException();
    }
}
