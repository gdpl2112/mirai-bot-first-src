package io.github.kloping.kzero.hwxb;

import io.github.kloping.kzero.main.api.KZeroBot;
import io.github.kloping.spt.annotations.Constructor;
import io.github.kloping.spt.annotations.Controller;
import io.github.kloping.spt.exceptions.NoRunException;

/**
 * @author github.kloping
 */
@Controller
public class SaController {
    @Constructor(value = 1)
    public SaController(KZeroBot bot) {
        if (!(bot.getSelf() instanceof WxHookStarter)) throw new NoRunException("wxhook专属");
    }
}
