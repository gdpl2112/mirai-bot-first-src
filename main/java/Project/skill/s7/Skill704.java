package Project.skill.s7;

import Project.dataBases.skill.SkillDataBase;
import Project.skill.SkillTemplate;
import io.github.kloping.mirai0.commons.Skill;
import io.github.kloping.mirai0.commons.gameEntitys.SkillInfo;
import io.github.kloping.mirai0.commons.gameEntitys.base.BaseInfo;
import io.github.kloping.mirai0.commons.resouce_and_tool.CommonSource;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.dataBases.GameDataBase.getInfo;
import static Project.dataBases.GameDataBase.putPerson;
import static Project.dataBases.skill.SkillDataBase.addAttHasTime;
import static Project.services.detailServices.GameDetailServiceUtils.getAttFromAny;
import static Project.services.detailServices.GameDetailServiceUtils.getBaseInfoFromAny;
import static Project.services.detailServices.GameSkillDetailService.getAddP;
import static Project.services.detailServices.GameSkillDetailService.getDuration;

/**
 * @author github.kloping
 */
public class Skill704 extends SkillTemplate {


    public Skill704() {
        super(704);
    }


    @Override
    public String getIntro() {
        return String.format("释放噬魂真身,吸取敌人%s%%的攻击力,恢复 吸取值得一半 的生命值", getAddP(getJid(), getId()));
    }

    @Override
    public Skill create(SkillInfo info, Number who, Number... nums) {
        return new Skill(info, who, new CopyOnWriteArrayList<>(nums), "噬魂蛛皇真身") {
            long v;

            @Override
            public void before() {
                if (nums.length == 0) {
                    return;
                }
                long v1 = getAttFromAny(who, nums[0]);
                if (v1 == 0) {
                    setTips("该玩家未注册");
                    return;
                }
                long id = nums[0].longValue();
                v = CommonSource.percentTo(info.getAddPercent(), v1);
                if (id > 0) {
                    addAttHasTime(id, new SkillDataBase.HasTimeAdder(System.currentTimeMillis() + getDuration(getJid())
                            , id, -v, getJid()));
                } else {
                    BaseInfo baseInfo = getBaseInfoFromAny(who, id);
                    baseInfo.setAtt(baseInfo.getAtt() - v).apply();
                }
                putPerson(getInfo(who).addHp(v / 2));
            }

            @Override
            public void run() {
                super.run();
            }
        };
    }
}
