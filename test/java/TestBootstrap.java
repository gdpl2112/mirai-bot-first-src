import Project.broadcast.normal.MemberJoinedBroadcast;
import io.github.kloping.mirai0.Main.BotStarter;

import static Project.services.detailServices.GameSkillDetailService.getDuration;

/**
 * @author github.kloping
 */
public class TestBootstrap {
    public static void main(String[] args) throws Throwable {
        BotStarter.main(args);
        System.err.println("all is ok");
        System.out.println(getDuration(9));
        Thread.sleep(2000L);
        MemberJoinedBroadcast.INSTANCE.broadcast(3474006766L, 759590727L);
    }
}
