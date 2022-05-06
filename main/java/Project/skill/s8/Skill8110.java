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
import static io.github.kloping.mirai0.Main.ITools.MemberTools.getRecentSpeeches;

/**
 * @author github.kloping
 */
public class Skill8110 extends SkillTemplate {

    public Skill8110() {
        super(8110);
    }

    @Override
    public SkillIntro.Type[] getTypes() {
        return new SkillIntro.Type[]{SkillIntro.Type.Att, SkillIntro.Type.ToOne};
    }

    @Override
    public String getIntro() {
        return String.format("破魂枪第八魂技,以破魂枪贯穿指定敌人,造成%s%%的伤害,并造成持续的流血伤害", getAddP(getJid(), getId()));
    }

    @Override
    public Skill create(SkillInfo info, Number who, Number... nums) {
        return new Skill(info, who, new CopyOnWriteArrayList<>(nums), "破魂枪第八魂技") {
            @Override
            public void before() {
                if (nums.length == 0) return;
                StringBuilder sb = new StringBuilder();
                long v = CommonSource.percentTo(info.getAddPercent(), getPersonInfo().att());
                attGhostOrMan(sb, who, nums[0].longValue(), v);
                getPersonInfo().addHp(CommonSource.percentTo(15, v));
                setTips(sb.toString());
                int n = 4;
                int eve = 1000;
                AsynchronousAttack thing = new AsynchronousAttack(n, who.longValue()
                        , nums[0].longValue(), info.getAddPercent(), eve, getRecentSpeeches(who.longValue()));
                thing.v = CommonSource.percentTo(info.getAddPercent(), getPersonInfo().getAtt());
                thing.v /= 8;
                thing.start();
                MapUtils.append(ASYNCHRONOUS_THING_MAP, who.longValue(), thing);
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
                StringBuilder sb = new StringBuilder();
                attGhostOrMan(sb, q1, q2, v);
                if (!sb.toString().trim().isEmpty()) MessageTools.sendMessageInGroup(sb.toString(), gid);
            }
        }
    }
}
