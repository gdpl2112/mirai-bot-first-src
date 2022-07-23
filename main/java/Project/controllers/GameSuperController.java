package Project.controllers;

import io.github.kloping.MySpringTool.annotations.AllMess;
import io.github.kloping.MySpringTool.annotations.Before;
import io.github.kloping.MySpringTool.annotations.Controller;
import io.github.kloping.MySpringTool.exceptions.NoRunException;
import io.github.kloping.mirai0.commons.Group;
import io.github.kloping.mirai0.commons.User;

import static io.github.kloping.mirai0.Main.Resource.isSuperQ;
import static io.github.kloping.mirai0.Main.Resource.println;

/**
 * @author github-kloping
 */
@Controller
public class GameSuperController {

    public long tempSuperL = -1;

    public GameSuperController() {
        println(this.getClass().getSimpleName() + "构建");
    }

    @Before
    public void before(@AllMess String mess, Group group, User qq) throws NoRunException {
        if (tempSuperL != -1L) {
            if (qq.getId() == tempSuperL) {
                tempSuperL = -1L;
                return;
            }
        }
        if (!isSuperQ(qq.getId())) {
            throw new NoRunException("can`t do this");
        }
    }

}
