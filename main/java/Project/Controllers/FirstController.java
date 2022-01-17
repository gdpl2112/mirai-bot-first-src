package Project.Controllers;

import Entitys.Group;
import io.github.kloping.MySpringTool.annotations.Action;
import io.github.kloping.MySpringTool.annotations.AllMess;
import io.github.kloping.MySpringTool.annotations.Controller;

/**
 * @author github-kloping
 */
@Controller
public class FirstController {

    @Action("测试.+")
    public Object a(@AllMess String mess, Group group) throws Exception {
        return System.currentTimeMillis();
    }

}
