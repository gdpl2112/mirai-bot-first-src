package Project.skills.normal;

import Project.commons.gameEntitys.SkillInfo;
import Project.services.detailServices.roles.v1.TagManagers;
import Project.skills.SkillTemplate;
import Project.utils.VelocityUtils;
import io.github.kloping.mirai0.commons.Skill;
import io.github.kloping.mirai0.commons.game.NormalWithWhoTagPack;
import io.github.kloping.mirai0.unitls.Tools.Tool;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.dataBases.skill.SkillDataBase.TAG_CANT_HIDE;
import static Project.services.detailServices.GameSkillDetailService.getDuration;

/**
 * @author github.kloping
 */
public class Skill14 extends SkillTemplate {


    public Skill14() {
        super(14);
    }


    @Override
    public String getIntro() {
        return VelocityUtils.getTemplateToString(String.format("skill/%s.intro", getJid()));
    }

    @Override
    public Skill create(SkillInfo info, Number who, Number... nums) {
        return new Skill(info, who, new CopyOnWriteArrayList<>(nums), "无法躲避") {
            private Long q;

            @Override
            public void before() {
                if (nums.length < 1) {
                    return;
                }
                q = nums[0].longValue();
                NormalWithWhoTagPack tagPack = new NormalWithWhoTagPack(TAG_CANT_HIDE, getDuration(getJid()));
                tagPack.setWho(who.longValue()).setQ(q).setValue(1L);
                long w = who.longValue();
                if (q < 0)
                    w = -w;
                TagManagers.getTagManager(w).addTag(tagPack);
                setTips("作用于" + Tool.INSTANCE.at(q));
            }
        };
    }
}
