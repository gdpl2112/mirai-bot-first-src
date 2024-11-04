package io.github.kloping.kzero.mirai.exclusive;

import io.github.kloping.kzero.main.api.KZeroBot;
import io.github.kloping.kzero.main.api.MessagePack;
import io.github.kloping.kzero.spring.web.ExtendController;
import io.github.kloping.spt.annotations.*;
import io.github.kloping.spt.exceptions.NoRunException;
import net.mamoe.mirai.Bot;

/**
 * @author github.kloping
 */
@Controller
public class WebAuthController {
    @Constructor(value = 1)
    public WebAuthController(KZeroBot kZeroBot) {
        if (!(kZeroBot.getSelf() instanceof Bot)) throw new NoRunException("mirai-bot专属扩展");
    }

    @Before
    public void before(@AllMess String mess, MessagePack pack) throws NoRunException {
        if (!pack.getSubjectId().equals("570700910")) {
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