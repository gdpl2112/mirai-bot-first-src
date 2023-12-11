package io.github.kloping.kzero.main;

import com.alibaba.fastjson.JSON;
import io.github.kloping.MySpringTool.annotations.CommentScan;
import io.github.kloping.MySpringTool.interfaces.component.HttpClientManager;
import io.github.kloping.kzero.gsuid.GsuidClient;
import io.github.kloping.kzero.guilds.GuildStater;
import io.github.kloping.kzero.mirai.MiraiStater;
import io.github.kloping.qqbot.api.Intents;
import io.github.kloping.qqbot.entities.ex.msg.MessageChain;
import io.github.kloping.qqbot.network.Events;
import jdk.jfr.Event;
import org.quartz.ee.servlet.QuartzInitializerListener;

/**
 * @author github.kloping
 */
@CommentScan(path = "io.github.kloping.kzero")
public class Main {
    public static void main(String[] args) {
        KZeroMainThreads threads = new KZeroMainThreads();
//        threads.add(GsuidClient.INSTANCE);
//        threads.add(new MiraiStater());
//        threads.add(new GuildStater("102057448", "v0uQvq74AZtFGTCCWcDnEpsOLNoszA2H",
//                Intents.PRIVATE_INTENTS.getCode()));
//        threads.add(new GuildStater("102005968", "SHxLuZlWTtqElfokFx6pNYX1qH9dFXN2",
//                Intents.PRIVATE_INTENTS.getCode()));
        threads.add(new GuildStater("102032364", "pzlH9hVZ7KmIHgOzzhFYZNpaQHgs5fEF", "Z2IK7fz4tTvAAvRi",
                Intents.PUBLIC_INTENTS.and(Intents.GROUP_INTENTS)));
        threads.run();
    }
}
