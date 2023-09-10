package io.github.kloping.kzero.main;

import io.github.kloping.MySpringTool.annotations.CommentScan;
import io.github.kloping.kzero.gsuid.GsuidClient;
import io.github.kloping.kzero.guilds.GuildStater;
import io.github.kloping.kzero.mirai.MiraiStater;
import io.github.kloping.qqbot.api.Intents;

/**
 * @author github.kloping
 */
@CommentScan(path = "io.github.kloping.kzero")
public class DevMain {
    public static void main(String[] args) {
        KZeroMainThreads threads = new KZeroMainThreads();
        threads.add(new MiraiStater());
        threads.add(new GuildStater("102005968", "pzlH9hVZ7KmIHgOzzhFYZNpaQHgs5fEF",
                Intents.PUBLIC_INTENTS.getCode()));
        threads.add(new GuildStater("102032364", "SHxLuZlWTtqElfokFx6pNYX1qH9dFXN2",
                Intents.PUBLIC_INTENTS.getCode()));
        threads.add(GsuidClient.INSTANCE);
        threads.run();
    }
}
