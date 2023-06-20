package Project.skills.normal;

import Project.commons.gameEntitys.SkillInfo;
import Project.skills.SkillTemplate;
import io.github.kloping.mirai0.commons.Skill;
import Project.utils.Tools.Tool;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.dataBases.GameDataBase.exist;
import static Project.dataBases.GameDataBase.getInfo;
import static Project.dataBases.skill.SkillDataBase.TAG_FJ;
import static Project.services.detailServices.GameSkillDetailService.getDuration;
import static Project.services.detailServices.GameSkillDetailService.oneNearest;

/**
 * @author github.kloping
 */
public class Skill7 extends SkillTemplate {


    public Skill7() {
        super(7);
    }


    @Override
    public Skill create(SkillInfo info, Number who, Number... nums) {
        return new Skill(info, who, new CopyOnWriteArrayList<>(nums), "反甲") {
            @Override
            public void before() {
                Long q = oneNearest(who.longValue(), nums);
                if (q < 0 || !exist(q)) {
                    return;
                }
                (getInfo(q).addTag(TAG_FJ, info.getAddPercent(), getDuration(getJid()))).apply();
                setTips("作用于 " + Tool.INSTANCE.at(who.longValue()));
            }

            @Override
            public void run() {
                super.run();
            }
        };
    }
}
