package Project.skill.s7;

import Project.skill.SkillTemplate;
import io.github.kloping.mirai0.Entitys.gameEntitys.*;
import io.github.kloping.mirai0.unitls.Tools.Tool;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.dataBases.GameDataBase.*;
import static Project.dataBases.skill.SkillDataBase.*;
import static Project.services.detailServices.GameDetailServiceUtils.*;
import static Project.services.detailServices.GameJoinDetailService.*;
import static Project.services.detailServices.GameSkillDetailService.*;
import static io.github.kloping.mirai0.unitls.Tools.Tool.RANDOM;
import io.github.kloping.mirai0.Entitys.gameEntitys.base.BaseInfo;
import Project.services.detailServices.GameBoneDetailService;
/**
 * @author github.kloping
 */
public class Skill715 extends SkillTemplate {
    @Override
    public void before() {
    }

    public Skill715() {
        super(715);
    }

    @Override
    public SkillIntro.Type[] getTypes() {
        return  new SkillIntro.Type[]{SkillIntro.Type.WHZs, SkillIntro.Type.OneTime, SkillIntro.Type.Add, SkillIntro.Type.Shd, SkillIntro.Type.Mark};
    }

    @Override
    public String getIntro() {
        return  String.format("释放刺豚真身,为自己增加一个最大生命值的%s%%的护盾,并每10秒恢复2%的生命值,持续%s秒", getAddP(getJid(), getId()), getAddP(getJid(), getId()) / 2);
    }

    @Override
    public Skill create(SkillInfo info, Number who, Number... nums) {
        return  new Skill(info, who, new CopyOnWriteArrayList<>(nums), "刺豚真身") {
            private Long v = (long) (info.getAddPercent() / 2);
            private Long eveV;

            @Override
            public void before() {
                PersonInfo pInfo = getInfo(who);
                long v = pInfo.getHpL();
                int p = info.getAddPercent();
                long o = percentTo(p, v);
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
