package Project.skills.s7;

import Project.utils.VelocityUtils;
import Project.services.detailServices.GameBoneDetailService;
import Project.skills.SkillTemplate;
import io.github.kloping.mirai0.commons.PersonInfo;
import io.github.kloping.mirai0.commons.Skill;
import Project.commons.gameEntitys.SkillInfo;
import Project.commons.resouce_and_tool.CommonSource;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.dataBases.GameDataBase.getInfo;
import static Project.dataBases.skill.SkillDataBase.HasTimeAdder;
import static Project.dataBases.skill.SkillDataBase.addAttHasTime;
import static Project.services.detailServices.GameSkillDetailService.getAddP;
import static Project.services.detailServices.GameSkillDetailService.getDuration;

/**
 * @author github.kloping
 */
public class Skill721 extends SkillTemplate {


    public Skill721() {
        super(721);
    }


    public String getIntro() {
        return VelocityUtils.getTemplateToString(String.format("skill/%s.intro", getJid()), getAddP(getJid(), getId()), getAddP(getJid(), getId()));
    }

    @Override
    public Skill create(SkillInfo info, Number who, Number... nums) {
        return new Skill(info, who, new CopyOnWriteArrayList<>(nums), "幽冥真身") {
            private long v;
            private Long v2;
            private Long q1;

            @Override
            public void before() {
                Long q = who.longValue();
                PersonInfo pInfo = getInfo(q);
                Long lon = pInfo.att();
                v2 = CommonSource.percentTo(info.getAddPercent(), lon);
                addAttHasTime(who.longValue(), new HasTimeAdder(System.currentTimeMillis() + getDuration(getJid()), who.longValue(), v2, getJid()));
                q1 = who.longValue();
                v = Long.valueOf(info.getAddPercent());
                GameBoneDetailService.addForAttr(q1, v, GameBoneDetailService.Type.HIDE_PRO, getDuration(getJid()));
            }

            @Override
            public void run() {
                super.run();
            }
        };
    }
}
