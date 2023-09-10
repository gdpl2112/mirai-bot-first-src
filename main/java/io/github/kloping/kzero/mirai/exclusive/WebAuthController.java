package io.github.kloping.kzero.mirai.exclusive;

import io.github.kloping.MySpringTool.annotations.*;
import io.github.kloping.MySpringTool.exceptions.NoRunException;
import io.github.kloping.kzero.main.api.MessagePack;
import io.github.kloping.kzero.spring.web.ExtendController;

/**
 * @author github.kloping
 */
@Controller
public class WebAuthController {
    @Before
    public void before(@AllMess String mess, MessagePack pack) throws NoRunException {
        if (pack.getSubjectId().equals("570700910")) {
            throw new NoRunException();
        }
    }

    @AutoStand
    ExtendController restController0;

    @Action("通过帖子<.+=>id>")
    public Object ok(@Param("id") Integer id) {
        return restController0.accept(id);
    }
}