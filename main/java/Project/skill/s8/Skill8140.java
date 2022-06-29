package Project.skill.s8;

import Project.skill.SkillTemplate;
import io.github.kloping.mirai0.commons.Skill;
import io.github.kloping.mirai0.commons.game.NormalWithWhoTagPack;
import io.github.kloping.mirai0.commons.gameEntitys.SkillInfo;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.dataBases.skill.SkillDataBase.TAG_EXTRA_DAMAGE;
import static Project.services.detailServices.GameDetailServiceUtils.attGhostOrMan;
import static Project.services.detailServices.GameSkillDetailService.*;
import static io.github.kloping.mirai0.commons.resouce_and_tool.CommonSource.percentTo;
import static io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet.FinalNormalString.ATTACK_BREAK;

/**
 * @author github.kloping
 */
public class Skill8140 extends SkillTemplate {

    private static final Integer F0 = 2;

    public Skill8140() {
        super(8140);
    }


    @Override
    public String getIntro() {
        return String.format("鬼魅第八魂技,令指定敌人受到的所有伤害额外增加%s%%,并在2秒后对其造成%s%%的伤害",
                getAddP(getJid(), getId()),
                getAddP(getJid(), getId()) * F0
        );
    }

    @Override
    public Skill create(SkillInfo info, Number who, Number... nums) {
        return new Skill(info, who, new CopyOnWriteArrayList<>(nums), "鬼魅第八魂技") {
            @Override
            public void before() {

            }

            @Override
            public void run() {
                super.run();
                if (nums.length <= 0) return;
                long qid = nums[0].longValue();
                NormalWithWhoTagPack tagPack = new NormalWithWhoTagPack(TAG_EXTRA_DAMAGE, getDuration(getJid()));
                tagPack.setWho(who.longValue()).setQ(qid).setValue(info.getAddPercent().longValue());
                addTagPack(tagPack);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    setTips(ATTACK_BREAK);
                }
                StringBuilder sb = new StringBuilder();
                long v = percentTo(info.getAddPercent() * F0, getPersonInfo().getAtt());
                attGhostOrMan(sb, who, qid, v);
                setTips(sb.toString());
            }
        };
    }
}
