package Project.Controllers.NormalController;

import Entitys.Group;
import Entitys.User;
import Project.ASpring.SpringStarter;
import io.github.kloping.MySpringTool.annotations.Action;
import io.github.kloping.MySpringTool.annotations.AllMess;
import io.github.kloping.MySpringTool.annotations.Before;
import io.github.kloping.MySpringTool.annotations.Controller;
import io.github.kloping.MySpringTool.exceptions.NoRunException;

import static io.github.kloping.Mirai.Main.Resource.println;
import static io.github.kloping.Mirai.Main.Resource.superQL;

@Controller
public class SuperController {
    public SuperController() {
        println(this.getClass().getSimpleName() + "构建");
    }

    @Before
    public void before(@AllMess String mess, Group group, User qq) throws NoRunException {
        if (qq.getId() != superQL)
            throw new NoRunException("can`t do this");
    }

    @Action("/move0")
    public void m0() {
        SpringStarter.move0();
    }

    @Action("/move1")
    public void m1() {
        SpringStarter.move1();
    }
}
