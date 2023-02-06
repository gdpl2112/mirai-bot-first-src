package Project.skill.s7;

import Project.dataBases.skill.SkillDataBase;
import Project.skill.SkillTemplate;
import io.github.kloping.mirai0.commons.PersonInfo;
import io.github.kloping.mirai0.commons.Skill;
import io.github.kloping.mirai0.commons.gameEntitys.SkillInfo;
import io.github.kloping.mirai0.commons.resouce_and_tool.CommonSource;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.dataBases.GameDataBase.getInfo;
import static Project.dataBases.GameDataBase.putPerson;
import static Project.dataBases.skill.SkillDataBase.HasTimeAdder;
import static Project.dataBases.skill.SkillDataBase.addAttHasTime;
import static Project.services.detailServices.GameSkillDetailService.getAddP;
import static Project.services.detailServices.GameSkillDetailService.getDuration;

/**
 * @author github.kloping
 */
public class Skill713 extends SkillTemplate {


    public Skill713() {
        super(713);
    }


    

    @Override
    public Skill create(SkillInfo info, Number who, Number... nums) {
        return new Skill(info, who, new CopyOnWriteArrayList<>(nums), "奇茸通天菊真身") {
            @Override
            public void before() {
                Long q = who.longValue();
                PersonInfo pInfo = getInfo(q);
                Long lon = pInfo.att();
                long v = CommonSource.percentTo(info.getAddPercent(), lon);
                addAttHasTime(who.longValue(), new HasTimeAdder(System.currentTimeMillis() + getDuration(getJid()),
                        who.longValue(), v, getJid()));
                try {
                    if (nums.length != 0) {
                        putPerson(getInfo(nums[0]).addTag(SkillDataBase.TAG_CANT_HIDE, 1, getDuration(getJid())));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }
}
