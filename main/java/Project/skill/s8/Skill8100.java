package Project.skill.s8;

import Project.skill.SkillTemplate;
import io.github.kloping.map.MapUtils;
import io.github.kloping.mirai0.Main.ITools.MessageTools;
import io.github.kloping.mirai0.commons.Skill;
import io.github.kloping.mirai0.commons.SkillIntro;
import io.github.kloping.mirai0.commons.game.AsynchronousThingType;
import io.github.kloping.mirai0.commons.gameEntitys.SkillInfo;
import io.github.kloping.mirai0.commons.resouce_and_tool.CommonSource;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ScheduledFuture;

import static Project.services.detailServices.GameDetailServiceUtils.attGhostOrMan;
import static Project.services.detailServices.GameSkillDetailService.ASYNCHRONOUS_THING_MAP;
import static Project.services.detailServices.GameSkillDetailService.getAddP;
import static io.github.kloping.mirai0.Main.ITools.MemberTools.getRecentSpeechesGid;

/**
 * @author github.kloping
 */
public class Skill8100 extends SkillTemplate {

    public Skill8100() {
        super(8100);
    }

    @Override
    public SkillIntro.Type[] getTypes() {
        return new SkillIntro.Type[]{SkillIntro.Type.Att, SkillIntro.Type.ToOne};
    }

    @Override
    public String getIntro() {
        return String.format("碧灵蛇皇毒第八魂技,使指定一个人受到%s%%的攻击,并在接下来的两分钟内持续受到递减的伤害,最小3%%的攻击", getAddP(getJid(), getId()));
    }

    @Override
    public Skill create(SkillInfo info, Number who, Number... nums) {
        return new Skill(info, who, new CopyOnWriteArrayList<>(nums), "七杀剑第八魂技") {
            @Override
            public void before() {
                if (nums.length == 0) return;
                int n = 12;
                int eve = 5000;
                AsynchronousAttack thing = new AsynchronousAttack(n, who.longValue()
                        , nums[0].longValue(), info.getAddPercent(), eve, getRecentSpeechesGid(who.longValue()));
                thing.v = CommonSource.percentTo(info.getAddPercent(), getPersonInfo().getAtt());
                thing.minV = CommonSource.percentTo(3, getPersonInfo().getAtt());
                thing.start();
                MapUtils.append(ASYNCHRONOUS_THING_MAP, who.longValue(), thing);
                setTips(nums[0].toString());
            }

            @Override
            public void run() {
                super.run();
            }
        };
    }

    public static class AsynchronousAttack extends io.github.kloping.mirai0.commons.game.AsynchronousAttack {
        private ScheduledFuture<?> future;
        private int i = 0;
        private long v = 0;
        private long minV = 0;

        public AsynchronousAttack(int n, long q1, long q2, long value, long eve, long gid) {
            super(n, q1, q2, value, eve, gid);
            setType(AsynchronousThingType.ATTACK);
        }

        @Override
        public void run() {
            if (i++ >= n) {
                future.cancel(true);
                over();
            } else {
                v = CommonSource.percentTo(80, v);
                v = v <= minV ? minV : v;
                StringBuilder sb = new StringBuilder();
                attGhostOrMan(sb, q1, q2, v);
                if (!sb.toString().trim().isEmpty()) MessageTools.sendMessageInGroup(sb.toString(), gid);
            }
        }
    }
}
