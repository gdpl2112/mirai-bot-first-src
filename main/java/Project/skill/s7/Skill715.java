package Project.skill.s7;

import Project.skill.SkillTemplate;
import io.github.kloping.mirai0.commons.PersonInfo;
import io.github.kloping.mirai0.commons.Skill;
import io.github.kloping.mirai0.commons.SkillIntro;
import io.github.kloping.mirai0.commons.gameEntitys.SkillInfo;
import io.github.kloping.mirai0.commons.resouce_and_tool.CommonSource;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.dataBases.GameDataBase.getInfo;
import static Project.dataBases.GameDataBase.putPerson;
import static Project.services.detailServices.GameSkillDetailService.addShield;
import static Project.services.detailServices.GameSkillDetailService.getAddP;

/**
 * @author github.kloping
 */
public class Skill715 extends SkillTemplate {

    public Skill715() {
        super(715);
    }

    @Override
    public SkillIntro.Type[] getTypes() {
        return new SkillIntro.Type[]{SkillIntro.Type.WHZs, SkillIntro.Type.OneTime, SkillIntro.Type.Add, SkillIntro.Type.Shd, SkillIntro.Type.Mark};
    }

    @Override
    public String getIntro() {
        return String.format("释放刺豚真身,为自己增加一个最大生命值的%s%%的护盾,并每10秒恢复2%的生命值,持续%s秒", getAddP(getJid(), getId()), getAddP(getJid(), getId()) / 2);
    }

    @Override
    public Skill create(SkillInfo info, Number who, Number... nums) {
        return new Skill(info, who, new CopyOnWriteArrayList<>(nums), "刺豚真身") {
            private Long v = (long) (info.getAddPercent() / 2);
            private Long eveV;

            @Override
            public void before() {
                PersonInfo pInfo = getInfo(who);
                long v = pInfo.getHpL();
                int p = info.getAddPercent();
                long o = CommonSource.percentTo(p, v);
                addShield(who.longValue(), o);
                eveV = pInfo.getHpL() / 50;
            }

            @Override
            public void run() {
                super.run();
                try {
                    if (v < 10) {
                        setTips("武魂真身失效.");
                        return;
                    }
                    Thread.sleep(10 * 1000);
                    v -= 10;
                    putPerson(getInfo(who).addHp(eveV));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
    }
}
