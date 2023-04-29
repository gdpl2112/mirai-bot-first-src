package Project.skills.s8;

import Project.commons.gameEntitys.SkillInfo;
import Project.skills.SkillTemplate;
import io.github.kloping.mirai0.commons.Skill;
import io.github.kloping.mirai0.commons.game.NormalTagPack;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.commons.rt.CommonSource.percentTo;
import static Project.commons.rt.ResourceSet.FinalNormalString.ATTACK_BREAK;
import static Project.controllers.auto.ControllerSource.playerBehavioralManager;
import static Project.dataBases.skill.SkillDataBase.TAG_EXTRA_DAMAGE;
import static Project.services.detailServices.GameDetailServiceUtils.attGhostOrMan;
import static Project.services.detailServices.GameSkillDetailService.addTagPack;
import static Project.services.detailServices.GameSkillDetailService.getDuration;

/**
 * @author github.kloping
 */
public class Skill8290 extends SkillTemplate {

    public Skill8290() {
        super(8290);
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
                    tagPack.setQ(who.longValue()).setValue(20L);
                    addTagPack(tagPack);
                } catch (InterruptedException e) {
                    setTips(ATTACK_BREAK);
                }
            }
        };
    }
}
