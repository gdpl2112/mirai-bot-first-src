package Project.skill.s7;

import Project.skill.SkillTemplate;
import io.github.kloping.mirai0.commons.PersonInfo;
import io.github.kloping.mirai0.commons.Skill;
import io.github.kloping.mirai0.commons.gameEntitys.SkillInfo;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.dataBases.GameDataBase.getInfo;
import static Project.dataBases.GameDataBase.putPerson;
import static Project.dataBases.skill.SkillDataBase.HasTimeAdder;
import static Project.dataBases.skill.SkillDataBase.addAttHasTime;
import static Project.services.detailServices.GameDetailServiceUtils.getHjFromAny;
import static Project.services.detailServices.GameSkillDetailService.getAddP;
import static Project.services.detailServices.GameSkillDetailService.getDuration;
import static io.github.kloping.mirai0.commons.resouce_and_tool.CommonSource.percentTo;
import static io.github.kloping.mirai0.commons.resouce_and_tool.CommonSource.toPercent;

/**
 * @author github.kloping
 */
public class Skill730 extends SkillTemplate {

    public Skill730() {
        super(730);
    }


    

    @Override
    public Skill create(SkillInfo info, Number who, Number... nums) {
        return new Skill(info, who, new CopyOnWriteArrayList<>(nums), "魔神剑") {
            @Override
            public void before() {
                if (nums.length == 0) {
                    setTips("未选择任何..");
                }
                long v = getHjFromAny(who, nums[0].longValue());
                PersonInfo in = getInfo(who);
                long vv = percentTo(info.getAddPercent(), in.att());
                addAttHasTime(who.longValue(), new HasTimeAdder(System.currentTimeMillis() + getDuration(getJid()), who.longValue(), vv, getJid()));
                int b = toPercent(v, in.getHjL());
                b = b > 15 ? 15 : b <= 2 ? 3 : b;
                long v1 = percentTo(b, in.getHpL());
                long v2 = percentTo(b, in.getHll());
                long v3 = percentTo(b, in.getHjL());
                in.addHp(v1);
                in.addHl(v2);
                in.addHj(v3);
                putPerson(in);
            }
        };
    }
}
