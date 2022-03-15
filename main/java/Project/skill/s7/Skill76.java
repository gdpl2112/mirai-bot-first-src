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
public class Skill76 extends SkillTemplate {
    

    public Skill76() {
        super(76);
    }

    @Override
    public SkillIntro.Type[] getTypes() {
        return  new SkillIntro.Type[]{SkillIntro.Type.WHZs, SkillIntro.Type.Special, SkillIntro.Type.Mark};
    }

    @Override
    public String getIntro() {
        return  String.format("柔骨兔无敌真身,持续%s秒", getAddP(getJid(), getId()));
    }

    @Override
    public Skill create(SkillInfo info, Number who, Number... nums) {
        return  new Skill(info, who, new CopyOnWriteArrayList<>(nums), "柔骨兔真身") {
            @Override
            public void before() {
                putPerson(getInfo(who).addTag(TAG_WD, 0));
            }

            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(info.getAddPercent() * 1000);
                    putPerson(getInfo(who).eddTag(TAG_WD, 0));
                    setTips("无敌失效");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }
}
