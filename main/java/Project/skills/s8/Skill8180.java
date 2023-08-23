package Project.skills.s8;

import Project.aSpring.dao.SkillInfo;
import Project.commons.gameEntitys.base.BaseInfo;
import Project.skills.SkillTemplate;
import Project.utils.VelocityUtils;
import io.github.kloping.mirai0.commons.Skill;
import io.github.kloping.mirai0.commons.game.NormalWithWhoTagPack;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.commons.rt.CommonSource.percentTo;
import static Project.dataBases.skill.SkillDataBase.TAG_818;
import static Project.services.detailServices.GameDetailServiceUtils.attGhostOrMan;
import static Project.services.detailServices.GameDetailServiceUtils.getBaseInfoFromAny;
import static Project.services.detailServices.GameSkillDetailService.addTagPack;
import static Project.services.detailServices.GameSkillDetailService.getAddP;

/**
 * @author github.kloping
 */
public class Skill8180 extends SkillTemplate {

    public Skill8180() {
        super(8180);
    }


    @Override
    public String getIntro() {
        return VelocityUtils.getTemplateToString(String.format("skill/%s.intro", getJid()),
                getAddP(getJid(), getId()),
                getAddP(getJid(), getId())
        );
    }

    @Override
    public Skill create(SkillInfo info, Number who, Number... nums) {
        return new Skill(info, who, new CopyOnWriteArrayList<>(nums), "蛇杖第八魂技") {

            @Override
            public void before() {
            }

            @Override
            public void run() {
                super.run();
                if (nums.length <= 0) return;
                long qid = nums[0].longValue();
                BaseInfo baseInfo = getBaseInfoFromAny(who.longValue(), qid);
                if (baseInfo.containsTag(TAG_818)) {
                    StringBuilder sb = new StringBuilder();
                    long v = percentTo(info.getAddPercent(), getPersonInfo().att());
                    v *= 2;
                    attGhostOrMan(sb, who, qid, v);
                    setTips(sb.toString());
                    baseInfo.eddTag(TAG_818);
                    baseInfo.apply();
                } else {
                    NormalWithWhoTagPack pack = new NormalWithWhoTagPack(TAG_818, 60000);
                    pack.setWho(who.longValue()).setQ(qid).setValue(1L);
                    addTagPack(pack);
                    StringBuilder sb = new StringBuilder();
                    long v = percentTo(info.getAddPercent(), getPersonInfo().att());
                    attGhostOrMan(sb, who, qid, v);
                    setTips(sb.toString());
                }
            }
        };
    }
}
