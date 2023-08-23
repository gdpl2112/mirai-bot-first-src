package Project.skills.s8;

import Project.aSpring.dao.SkillInfo;
import Project.skills.SkillTemplate;
import Project.utils.VelocityUtils;
import io.github.kloping.mirai0.commons.Skill;
import io.github.kloping.mirai0.commons.game.NormalTagPack;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.commons.rt.CommonSource.percentTo;
import static Project.dataBases.skill.SkillDataBase.TAG_EDD_ATT;
import static Project.services.detailServices.GameDetailServiceUtils.attGhostOrMan;
import static Project.services.detailServices.GameSkillDetailService.*;

/**
 * @author github.kloping
 */
public class Skill8230 extends SkillTemplate {

    public static final int V0 = 3;


    public Skill8230() {
        super(8230);
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

                NormalTagPack tagPack = new NormalTagPack(TAG_EDD_ATT, getDuration(getJid()));
                tagPack.setQ(qid).setValue(Long.valueOf(info.getAddPercent()));
                addTagPack(tagPack);
                StringBuilder sb = new StringBuilder();
                attGhostOrMan(sb, who.longValue(), qid, percentTo(info.getAddPercent() * V0, getPersonInfo().att()));
                setTips(sb.toString());
            }
        };
    }
}
