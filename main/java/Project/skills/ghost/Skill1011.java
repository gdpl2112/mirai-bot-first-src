package Project.skills.ghost;

import Project.controllers.gameControllers.GameController;
import Project.services.detailServices.GameDetailService;
import Project.services.detailServices.ac.GameJoinDetailService;
import Project.services.detailServices.roles.DamageType;
import Project.skills.SkillTemplate;
import io.github.kloping.map.MapUtils;
import io.github.kloping.mirai0.Main.iutils.MessageUtils;
import io.github.kloping.mirai0.commons.GhostObj;
import io.github.kloping.mirai0.commons.Skill;
import io.github.kloping.mirai0.commons.game.AsynchronousThingType;
import io.github.kloping.mirai0.commons.gameEntitys.SkillInfo;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ScheduledFuture;

import static Project.services.detailServices.GameSkillDetailService.ASYNCHRONOUS_THING_MAP;
import static Project.services.detailServices.GameSkillDetailService.getAddP;
import static io.github.kloping.mirai0.Main.iutils.MemberUtils.getRecentSpeechesGid;
import static io.github.kloping.mirai0.commons.resouce_and_tool.CommonSource.percentTo;
import static io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet.FinalString.NEWLINE;

/**
 * @author github.kloping
 */
public class Skill1011 extends SkillTemplate {

    public Skill1011() {
        super(1011);
        setName("连续攻击");
    }

    @Override
    public Skill create(SkillInfo info, Number who, Number... nums) {
        return new Skill(info, who, new CopyOnWriteArrayList<>(nums), "") {
            @Override
            public void before() {
            }

            @Override
            public void run() {
                super.run();
                GhostObj ghostObj = GameJoinDetailService.getGhostObjFrom(-who.longValue());
                int b = getAddP(getJid(), getId()).intValue();
                long v = percentTo(b, ghostObj.getAtt());
                AsynchronousAttack thing = new AsynchronousAttack(10, ghostObj.getId(), -who.longValue(), 0, 1000, getRecentSpeechesGid(-who.longValue()));
                thing.setB(b);
                thing.setV(v);
                thing.start();
                MapUtils.append(ASYNCHRONOUS_THING_MAP, who.longValue(), thing);
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

        int b;
        long v;

        public void setB(int b) {
            this.b = b;
        }

        public void setV(long v) {
            this.v = v;
        }

        @Override
        public void run() {
            if (i++ >= n) {
                future.cancel(true);
                over();
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append("对你造成").append(v).append("伤害").append(NEWLINE);
                sb.append(GameDetailService.beaten(q2, -2, v, DamageType.AD));
                sb.append(NEWLINE);
                sb.append(GameController.gameService.info(q2));
                MessageUtils.INSTANCE.sendMessageInGroup(sb.toString(), gid);
            }
        }
    }
}
