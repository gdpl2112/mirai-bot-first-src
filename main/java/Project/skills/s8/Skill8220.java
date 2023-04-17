package Project.skills.s8;

import Project.utils.VelocityUtils;
import Project.services.detailServices.GameDetailService;
import Project.skills.SkillTemplate;
import io.github.kloping.mirai0.commons.Skill;
import io.github.kloping.mirai0.commons.game.NormalTagPack;
import Project.commons.gameEntitys.SkillInfo;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.dataBases.skill.SkillDataBase.TAG_ADD_ATT;
import static Project.services.detailServices.GameSkillDetailService.*;
import static Project.commons.resouce_and_tool.CommonSource.percentTo;

/**
 * @author github.kloping
 */
public class Skill8220 extends SkillTemplate {

    public static final int V0 = 3;


    public Skill8220() {
        super(8220);
    }

    @Override
    public String getIntro() {
        return VelocityUtils.getTemplateToString(String.format("skill/%s.intro", getJid()),
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
                tagPack.setQ(qid).setValue(Long.valueOf(info.getAddPercent()));
                addTagPack(tagPack);
                setTips(GameDetailService.addHp(qid, percentTo(info.getAddPercent() * V0, getPersonInfo().att())));
            }
        };
    }
}
