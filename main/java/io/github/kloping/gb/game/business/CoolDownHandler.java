package io.github.kloping.gb.game.business;

import io.github.kloping.gb.Utils;
import io.github.kloping.gb.finals.FinalFormat;
import io.github.kloping.gb.game.e0.GameDataContext;

/**
 * @author github-kloping
 * @date 2023-07-04
 */
public class CoolDownHandler extends BusinessHandler {
    private Long t0;
    private String format = FinalFormat.WAIT_TIPS;

    public CoolDownHandler(BusinessHandler next, Long t0) {
        super(next);
        this.t0 = t0;
    }

    public CoolDownHandler(BusinessHandler next, Long t0, String format) {
        super(next);
        this.t0 = t0;
        this.format = format;
    }

    @Override
    public String progress(GameDataContext context) {
        if (t0 < System.currentTimeMillis())
            return super.progress(context);
        else {
            return String.format(format, Utils.getTimeTips(t0));
        }
    }
}
