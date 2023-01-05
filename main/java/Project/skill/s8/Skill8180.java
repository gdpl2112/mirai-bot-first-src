package Project.skill.s8;

import Project.skill.SkillTemplate;
import io.github.kloping.mirai0.commons.Skill;
import io.github.kloping.mirai0.commons.game.NormalWithWhoTagPack;
import io.github.kloping.mirai0.commons.gameEntitys.SkillInfo;
import io.github.kloping.mirai0.commons.gameEntitys.base.BaseInfo;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.dataBases.skill.SkillDataBase.TAG_818;
import static Project.services.detailServices.GameDetailServiceUtils.attGhostOrMan;
import static Project.services.detailServices.GameDetailServiceUtils.getBaseInfoFromAny;
import static Project.services.detailServices.GameSkillDetailService.addTagPack;
import static Project.services.detailServices.GameSkillDetailService.getAddP;
import static io.github.kloping.mirai0.commons.resouce_and_tool.CommonSource.percentTo;

/**
 * @author github.kloping
 */
public class Skill8180 extends SkillTemplate {

    public Skill8180() {
        super(8180);
    }


    @Override
    public String getIntro() {
        return String.format(
                "蛇杖第八魂技,对指定敌人造成%s%%的伤害,并标记该敌人,标记存在1分钟,若该敌人已经存在该标记(蛇矛第八魂技),则额外造成%s%%的伤害"
                ,
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
