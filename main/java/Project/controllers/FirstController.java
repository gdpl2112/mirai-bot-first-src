package Project.controllers;

import Project.interfaces.http_api.AiBaidu;
import io.github.kloping.MySpringTool.annotations.Action;
import io.github.kloping.MySpringTool.annotations.AllMess;
import io.github.kloping.MySpringTool.annotations.AutoStand;
import io.github.kloping.MySpringTool.annotations.Controller;
import io.github.kloping.mirai0.commons.Group;
import io.github.kloping.mirai0.commons.User;

/**
 * @author github-kloping
 */
@Controller
public class FirstController {
    @AutoStand
    AiBaidu aiBaidu;

    @Action("测试.+")
    public Object a(@AllMess String mess, Group group, User user) throws Exception {
        return null;
    }
}
