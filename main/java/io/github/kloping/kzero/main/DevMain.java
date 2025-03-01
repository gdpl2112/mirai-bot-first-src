package io.github.kloping.kzero.main;

import io.github.kloping.kzero.gsuid.GsuidClient;
import io.github.kloping.kzero.hwxb.WxHookStarter;
import io.github.kloping.kzero.mihdp.MihdpClient;
import io.github.kloping.kzero.mirai.MiraiStater;
import io.github.kloping.kzero.qqpd.GuildStater;
import io.github.kloping.qqbot.api.Intents;
import io.github.kloping.qqbot.entities.ex.Markdown;
import io.github.kloping.spt.annotations.ComponentScan;

import java.net.URISyntaxException;

/**
 * @author github.kloping
 */
@ComponentScan("io.github.kloping.kzero")
public class DevMain {
    public static void main(String[] args) {
        System.err.println("build time 2025/3/1");
        new DevPluginConfig().run();
        KlopZeroMainThreads threads = new KlopZeroMainThreads();
        threads.add(new MiraiStater());
        threads.add(new GuildStater("102032364", "pzlH9hVZ7KmIHgOzzhFYZNpaQHgs5fEF", "Z2IK7fz4tTvAAvRi",
                Intents.PUBLIC_INTENTS.and(Intents.GROUP_INTENTS)));
//        threads.add(new GuildStater("102005968", "SHxLuZlWTtqElfokFx6pNYX1qH9dFXN2",
//                Intents.PRIVATE_INTENTS.getCode()));
        threads.add(new WxHookStarter());
        try {
            KlopZeroMainThreads.EXECUTOR_SERVICE.submit(new MihdpClient());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        try {
            KlopZeroMainThreads.EXECUTOR_SERVICE.submit(new GsuidClient());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        threads.run();
    }
}
