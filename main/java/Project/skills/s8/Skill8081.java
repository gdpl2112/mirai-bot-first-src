package Project.skills.s8;

import Project.commons.gameEntitys.SkillInfo;
import Project.commons.gameEntitys.base.BaseInfo;
import Project.commons.rt.CommonSource;
import Project.skills.SkillTemplate;
import io.github.kloping.map.MapUtils;
import io.github.kloping.mirai0.Main.iutils.MessageUtils;
import io.github.kloping.mirai0.commons.Skill;
import io.github.kloping.mirai0.commons.game.AsynchronousThing;
import io.github.kloping.mirai0.commons.game.AsynchronousThingType;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ScheduledFuture;

import static Project.dataBases.GameDataBase.getInfo;
import static Project.services.detailServices.GameDetailServiceUtils.attGhostOrMan;
import static Project.services.detailServices.GameDetailServiceUtils.getBaseInfoFromAny;
import static Project.services.detailServices.GameSkillDetailService.ASYNCHRONOUS_THING_MAP;
import static io.github.kloping.mirai0.Main.iutils.MemberUtils.getRecentSpeechesGid;

/**
 * @author github.kloping
 */
public class Skill8081 extends SkillTemplate {

    public Skill8081() {
        super(8081);
    }

    @Override
    public Skill create(SkillInfo info, Number who, Number... nums) {
        return new Skill(info, who, new CopyOnWriteArrayList<>(nums), "邪火第八魂技") {
            @Override
            public void before() {
                if (nums.length == 0) return;
                Long qid = nums[0].longValue();
                int n = 12;
                int eve = 5000;
                AsynchronousThing thing = new AsynchronousAttack(n, who.longValue(), qid,
                        info.getAddPercent(), eve, getRecentSpeechesGid(who.longValue()));
                thing.start();
                MapUtils.append(ASYNCHRONOUS_THING_MAP, who.longValue(), thing);
                setTips(qid.toString());
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
                if (!sb.toString().trim().isEmpty()) MessageUtils.INSTANCE.sendMessageInGroup(sb.toString(), gid);
            }
        }
    }
}
