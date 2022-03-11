package Project.skill.normal;

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
public class Skill719 extends SkillTemplate {
    @Override
    public void before() {
    }

    public Skill719() {
        super(719);
    }

    @Override
    public SkillIntro.Type[] getTypes() {
        return  WhTypes.T72;
    }

    @Override
    public String getIntro() {
        return  String.format("蓝银草,释放蓝银草,每%s秒恢复%s%%的生命值", (t719 / 1000), getAddP(getJid(), getId()));
    }

    @Override
    public Skill create(SkillInfo info, Number who, Number... nums) {
        return  new Skill(info, who, new CopyOnWriteArrayList<>(nums), "蓝银草真身") {

            @Override
            public void before() {
                eve();
            }

            private int c = 1;

            @Override
            public void run() {
                super.run();
                try {
                    if (c++ > t79C) {
                        setTips("武魂真身失效");
                        return;
                    }
                    Thread.sleep(t79);
                    eve();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            public void eve() {
                addHp(who, who.longValue(), info.getAddPercent());
            }
        };
    }
}
