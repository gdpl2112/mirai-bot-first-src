package Project.skill.s8;

import Project.skill.SkillTemplate;
import io.github.kloping.map.MapUtils;
import io.github.kloping.mirai0.commons.Skill;
import io.github.kloping.mirai0.commons.gameEntitys.SkillInfo;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.services.detailServices.GameDetailServiceUtils.attGhostOrMan;
import static Project.services.detailServices.GameSkillDetailService.ASYNCHRONOUS_THING_MAP;
import static io.github.kloping.mirai0.Main.ITools.MemberTools.getRecentSpeechesGid;
import static io.github.kloping.mirai0.commons.resouce_and_tool.CommonSource.RANDOM;
import static io.github.kloping.mirai0.commons.resouce_and_tool.CommonSource.percentTo;

/**
 * @author github.kloping
 */
public class Skill8130 extends SkillTemplate {

    public Skill8130() {
        super(8130);
    }


    @Override
    public Skill create(SkillInfo info, Number who, Number... nums) {
        return new Skill(info, who, new CopyOnWriteArrayList<>(nums), "奇茸通天菊第八魂技") {
            @Override
            public void before() {

            }

            @Override
            public void run() {
                super.run();
                if (nums.length == 0) return;
                long qid = nums[0].longValue();
                StringBuilder sb = new StringBuilder();
                long v;

                v = percentTo(info.getAddPercent(), getPersonInfo().att());
                attGhostOrMan(sb, who, qid, v);
                setTips(sb.toString());
                tryLiuXue();
                v = percentTo(info.getAddPercent(), getPersonInfo().att());
                attGhostOrMan(sb, who, qid, v);
                setTips(sb.toString());
                tryLiuXue();
                v = percentTo(info.getAddPercent(), getPersonInfo().att());
                attGhostOrMan(sb, who, qid, v);
                setTips(sb.toString());
                tryLiuXue();
                v = percentTo(info.getAddPercent(), getPersonInfo().att());
                attGhostOrMan(sb, who, qid, v);
                setTips(sb.toString());
                tryLiuXue();
            }

            private void tryLiuXue() {
                if (RANDOM.nextInt(4) != 0) return;

                int n = 3;
                int eve = 1000;
                Skill8110.AsynchronousAttack thing = new Skill8110.AsynchronousAttack(n, who.longValue()
                        , nums[0].longValue(), info.getAddPercent(), eve, getRecentSpeechesGid(who.longValue()));
                thing.v = percentTo(info.getAddPercent(), getPersonInfo().getAtt());
                thing.v /= 4;
                thing.start();
                MapUtils.append(ASYNCHRONOUS_THING_MAP, who.longValue(), thing);
            }
        };
    }
}
