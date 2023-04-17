package Project.skills.normal;

import Project.skills.SkillTemplate;
import io.github.kloping.mirai0.commons.Skill;
import Project.commons.gameEntitys.SkillInfo;
import Project.commons.resouce_and_tool.CommonSource;
import io.github.kloping.mirai0.unitls.Tools.Tool;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.dataBases.GameDataBase.getInfo;
import static Project.dataBases.GameDataBase.putPerson;
import static Project.services.detailServices.GameSkillDetailService.oneNearest;

/**
 * @author github.kloping
 */
public class Skill9 extends SkillTemplate {


    public Skill9() {
        super(9);
    }




    @Override
    public Skill create(SkillInfo info, Number who, Number... nums) {
        return new Skill(info, who, new CopyOnWriteArrayList<>(nums), "__") {
            private Integer v;
            private long q;

            @Override
            public void before() {
                v = 1;
                q = oneNearest(who, nums);
                eve();
                setTips("作用于 " +  Tool.INSTANCE.at(q));
            }

            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(24 * 1000);
                    while (eve()) {
                        continue;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            private boolean eve() {
                if (v++ >= 5) {
                    return false;
                }
                long l = getInfo(q).getHpL();
                putPerson(getInfo(q).addHp(CommonSource.percentTo(info.getAddPercent(), l)));
                return true;
            }
        };
    }
}
