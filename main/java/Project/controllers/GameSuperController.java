package Project.controllers;

import Project.commons.SpGroup;
import Project.commons.SpUser;
import io.github.kloping.MySpringTool.annotations.AllMess;
import io.github.kloping.MySpringTool.annotations.Before;
import io.github.kloping.MySpringTool.annotations.Controller;
import io.github.kloping.MySpringTool.exceptions.NoRunException;

import static io.github.kloping.mirai0.Main.BootstarpResource.isSuperQ;
import static io.github.kloping.mirai0.Main.BootstarpResource.println;

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
    public void before(@AllMess String mess, SpGroup group, SpUser qq) throws NoRunException {
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
