package io.github.kloping.kzero.main;

import io.github.kloping.MySpringTool.annotations.CommentScan;
import io.github.kloping.kzero.gsuid.GsuidClient;
import io.github.kloping.kzero.guilds.GuildStater;
import io.github.kloping.kzero.mirai.MiraiStater;
import io.github.kloping.qqbot.api.Intents;
import org.quartz.ee.servlet.QuartzInitializerListener;

/**
 * @author github.kloping
 */
@CommentScan(path = "io.github.kloping.kzero")
public class Main {
    public static void main(String[] args) {
        KZeroMainThreads threads = new KZeroMainThreads();
//        threads.add(new MiraiStater());
        threads.add(new GuildStater("102057448", "v0uQvq74AZtFGTCCWcDnEpsOLNoszA2H",
                Intents.PRIVATE_INTENTS.getCode()));
        threads.add(new GuildStater("102005968", "SHxLuZlWTtqElfokFx6pNYX1qH9dFXN2",
                Intents.PRIVATE_INTENTS.getCode()));
        threads.add(GsuidClient.INSTANCE);
        threads.run();
    }
}
