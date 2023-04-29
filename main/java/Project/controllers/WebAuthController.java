package Project.controllers;

import Project.aSpring.SpringStarter;
import Project.aSpring.mcs.controllers.RestController0;
import Project.commons.SpGroup;
import Project.commons.SpUser;
import io.github.kloping.MySpringTool.annotations.*;
import io.github.kloping.MySpringTool.exceptions.NoRunException;

/**
 * @author github.kloping
 */
@Controller
public class WebAuthController {
    @Before
    public void before(@AllMess String mess, SpGroup group, SpUser qq) throws NoRunException {
        if (group.getId() != 570700910L) {
            throw new NoRunException();
        }
    }

    @Action("通过帖子<.+=>id>")
    public Object ok(@Param("id") Integer id) {
        RestController0 c0 = SpringStarter.configuration.getBean(RestController0.class);
        return c0.accept(id);
    }
}
