package io.github.kloping.kzero.main;

import io.github.kloping.kzero.gsuid.GsuidClient;
import io.github.kloping.kzero.hwxb.WxHookStarter;

import java.net.URISyntaxException;

/**
 * @author github.kloping
 */
public class Main {
    public static void main(String[] args) {
        new DevPluginConfig().run();

        KlopZeroMainThreads threads = new KlopZeroMainThreads();
//        try {
//            KlopZeroMainThreads.EXECUTOR_SERVICE.submit(new MihdpClient());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        try {
//            KlopZeroMainThreads.EXECUTOR_SERVICE.submit(new GsuidClient());
//        } catch (URISyntaxException e) {
//            e.printStackTrace();
//        }
//        threads.add(new WxStarter());
        threads.add(new WxHookStarter());
//        threads.add(new MiraiStater());
//        threads.add(new GuildStater("102057448", "v0uQvq74AZtFGTCCWcDnEpsOLNoszA2H", Intents.PRIVATE_INTENTS.getCode()));
//        threads.add(new GuildStater("102005968", "SHxLuZlWTtqElfokFx6pNYX1qH9dFXN2", "2yvspmjgdbZXVTRPONMLKJIHHHHHHHHI",
//                Intents.START.and(Intents.GROUP_INTENTS,Intents.MESSAGE_AUDIT)));
//        threads.add(new GuildStater("102032364", "pzlH9hVZ7KmIHgOzzhFYZNpaQHgs5fEF", "Z2IK7fz4tTvAAvRi",
//                Intents.PUBLIC_INTENTS.and(Intents.GROUP_INTENTS)));
        threads.run();
    }
}
