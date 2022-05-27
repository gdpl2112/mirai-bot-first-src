package Project.skill.s8;

import Project.services.detailServices.GameDetailService;
import Project.skill.SkillTemplate;
import io.github.kloping.mirai0.commons.Skill;
import io.github.kloping.mirai0.commons.SkillIntro;
import io.github.kloping.mirai0.commons.game.NormalTagPack;
import io.github.kloping.mirai0.commons.gameEntitys.SkillInfo;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.dataBases.skill.SkillDataBase.TAG_ADD_ATT;
import static Project.services.detailServices.GameSkillDetailService.*;
import static io.github.kloping.mirai0.commons.resouce_and_tool.CommonSource.percentTo;

/**
 * @author github.kloping
 */
public class Skill8220 extends SkillTemplate {

    public Skill8220() {
        super(8220);
    }

    @Override
    public SkillIntro.Type[] getTypes() {
        return new SkillIntro.Type[]{SkillIntro.Type.Att, SkillIntro.Type.ToOne};
    }


    public static final int V0 = 3;

    @Override
    public String getIntro() {
        return String.format("光明圣龙第八魂技,增加指定敌人%s%%的输出,并对其恢复%s%%的生命",
                getAddP(getJid(), getId()),
                getAddP(getJid(), getId()) * V0
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
                NormalTagPack tagPack = new NormalTagPack(TAG_ADD_ATT, getDuration(getJid()));
                tagPack.setQ(qid).setValue(Long.valueOf(info.getAddPercent())).setEffected(false);
                addTagPack(tagPack);
                setTips(GameDetailService.addHp(qid, percentTo(info.getAddPercent() * V0, getPersonInfo().att())));
            }
        };
    }
}
