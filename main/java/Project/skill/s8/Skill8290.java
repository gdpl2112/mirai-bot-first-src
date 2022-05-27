package Project.skill.s8;

import Project.skill.SkillTemplate;
import io.github.kloping.mirai0.commons.Skill;
import io.github.kloping.mirai0.commons.SkillIntro;
import io.github.kloping.mirai0.commons.game.NormalTagPack;
import io.github.kloping.mirai0.commons.gameEntitys.SkillInfo;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.controllers.auto.ControllerSource.playerBehavioralManager;
import static Project.dataBases.skill.SkillDataBase.TAG_EXTRA_DAMAGE;
import static Project.services.detailServices.GameDetailServiceUtils.attGhostOrMan;
import static Project.services.detailServices.GameSkillDetailService.*;
import static io.github.kloping.mirai0.commons.resouce_and_tool.CommonSource.percentTo;
import static io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet.FinalNormalString.ATTACK_BREAK;

/**
 * @author github.kloping
 */
public class Skill8290 extends SkillTemplate {

    public Skill8290() {
        super(8290);
    }

    @Override
    public SkillIntro.Type[] getTypes() {
        return new SkillIntro.Type[]{SkillIntro.Type.Att, SkillIntro.Type.ToOne};
    }

    @Override
    public String getIntro() {
        return String.format("杀神昊天锤弓第八魂技,挥动巨大的昊天锤,在1.5倍攻击前摇之后,对指定敌人造成%s%%的巨大伤害,但之后使用者将额外受伤20%%",
                getAddP(getJid(), getId())
        );
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
                if (nums.length <= 0) return;
                long qid = nums[0].longValue();
                long v = percentTo(info.getAddPercent(), getPersonInfo().att());
                try {
                    Thread.sleep((long) (playerBehavioralManager.getAttPre(qid) * 1.5));
                    StringBuilder sb = new StringBuilder();
                    attGhostOrMan(sb, who, qid, v);
                    setTips(sb.toString());
                    NormalTagPack tagPack = new NormalTagPack(TAG_EXTRA_DAMAGE, getDuration(getJid()));
                    tagPack.setQ(who.longValue()).setValue(20L).setEffected(false);
                    addTagPack(tagPack);
                } catch (InterruptedException e) {
                    setTips(ATTACK_BREAK);
                }
            }
        };
    }
}
