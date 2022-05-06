package Project.skill.s8;

import Project.controllers.auto.ControllerSource;
import Project.skill.SkillTemplate;
import io.github.kloping.mirai0.commons.Skill;
import io.github.kloping.mirai0.commons.SkillIntro;
import io.github.kloping.mirai0.commons.gameEntitys.SkillInfo;
import io.github.kloping.mirai0.commons.resouce_and_tool.CommonSource;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.dataBases.GameDataBase.getInfo;
import static Project.services.detailServices.GameDetailServiceUtils.attGhostOrMan;
import static Project.services.detailServices.GameSkillDetailService.getAddP;
import static io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet.FinalNormalString.ATTACK_BREAK;
import static io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet.FinalNormalString.NOT_SELECT_STR;
import static io.github.kloping.mirai0.unitls.Tools.Tool.RANDOM;

/**
 * @author github.kloping
 */
public class Skill8080 extends SkillTemplate {

    public Skill8080() {
        super(8080);
    }

    @Override
    public SkillIntro.Type[] getTypes() {
        return new SkillIntro.Type[]{SkillIntro.Type.Att, SkillIntro.Type.ToOne};
    }

    @Override
    public String getIntro() {
        return String.format("邪火凤凰第八魂技,在两倍的攻击前摇之后,对指定敌人造成%s-+10%%的伤害", getAddP(getJid(), getId()));
    }

    @Override
    public Skill create(SkillInfo info, Number who, Number... nums) {
        return new Skill(info, who, new CopyOnWriteArrayList<>(nums), "邪火第八魂技") {
            private long qid = -1;

            @Override
            public void before() {
                if (nums.length <= 0) {
                    return;
                }
                qid = nums[0].longValue();
            }

            @Override
            public void run() {
                super.run();
                if (qid <= 0) {
                    setTips(NOT_SELECT_STR);
                    return;
                }
                int r = RANDOM.nextInt(20) - 10;
                int b = info.getAddPercent();
                b += r;
                setTips(String.format("将造成%s%%(%s)伤害", b, CommonSource.percentTo(b, getInfo(who).att())));
                long t = ControllerSource.playerBehavioralManager.getAttPre(who.longValue());
                t *= 2;
                try {
                    Thread.sleep(t);
                    long att = getInfo(who).att();
                    long v = CommonSource.percentTo(b, att);
                    StringBuilder sb = new StringBuilder();
                    attGhostOrMan(sb, who, qid, v);
                    setTips(sb.toString());
                } catch (InterruptedException e) {
                    setTips(ATTACK_BREAK);
                }
            }
        };
    }
}
