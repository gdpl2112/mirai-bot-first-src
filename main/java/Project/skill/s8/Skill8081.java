package Project.skill.s8;

import Project.skill.SkillTemplate;
import io.github.kloping.map.MapUtils;
import io.github.kloping.mirai0.Main.ITools.MessageTools;
import io.github.kloping.mirai0.commons.Skill;
import io.github.kloping.mirai0.commons.SkillIntro;
import io.github.kloping.mirai0.commons.game.AsynchronousThing;
import io.github.kloping.mirai0.commons.game.AsynchronousThingType;
import io.github.kloping.mirai0.commons.gameEntitys.SkillInfo;
import io.github.kloping.mirai0.commons.gameEntitys.base.BaseInfo;
import io.github.kloping.mirai0.commons.resouce_and_tool.CommonSource;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ScheduledFuture;

import static Project.dataBases.GameDataBase.getInfo;
import static Project.services.detailServices.GameDetailServiceUtils.attGhostOrMan;
import static Project.services.detailServices.GameDetailServiceUtils.getBaseInfoFromAny;
import static Project.services.detailServices.GameSkillDetailService.ASYNCHRONOUS_THING_MAP;
import static Project.services.detailServices.GameSkillDetailService.getAddP;
import static io.github.kloping.mirai0.Main.ITools.MemberTools.getRecentSpeechesGid;

/**
 * @author github.kloping
 */
public class Skill8081 extends SkillTemplate {

    public Skill8081() {
        super(8081);
    }

    @Override
    public SkillIntro.Type[] getTypes() {
        return new SkillIntro.Type[]{SkillIntro.Type.Att, SkillIntro.Type.ToOne};
    }

    @Override
    public String getIntro() {
        return String.format("邪火凤凰第八魂技,对指定敌人每5秒造成%s%%+指定敌人已损失7%%生命值的伤害", getAddP(getJid(), getId()));
    }

    @Override
    public Skill create(SkillInfo info, Number who, Number... nums) {
        return new Skill(info, who, new CopyOnWriteArrayList<>(nums), "邪火第八魂技") {
            @Override
            public void before() {
                if (nums.length == 0) return;
                int n = 12;
                int eve = 5000;
                AsynchronousThing thing = new AsynchronousAttack(n, who.longValue(), nums[0].longValue(),
                        info.getAddPercent(), eve, getRecentSpeechesGid(who.longValue()));
                thing.start();
                MapUtils.append(ASYNCHRONOUS_THING_MAP, who.longValue(), thing);
                setTips(nums[0].toString());
            }
        };
    }

    public static class AsynchronousAttack extends io.github.kloping.mirai0.commons.game.AsynchronousAttack {
        private ScheduledFuture<?> future;
        private int i = 0;

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
                Long att = getInfo(q1).att();
                long v = CommonSource.percentTo((int) value, att);
                BaseInfo baseInfo = getBaseInfoFromAny(q1, q2);
                v += CommonSource.percentTo(7, baseInfo.getHpL() - baseInfo.getHp());
                StringBuilder sb = new StringBuilder();
                attGhostOrMan(sb, q1, q2, v);
                if (!sb.toString().trim().isEmpty()) MessageTools.sendMessageInGroup(sb.toString(), gid);
            }
        }
    }
}
