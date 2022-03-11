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
public class Skill11 extends SkillTemplate {
    @Override
    public void before() {
    }

    public Skill11() {
        super(11);
    }

    @Override
    public SkillIntro.Type[] getTypes() {
        return  new SkillIntro.Type[]{SkillIntro.Type.Special, SkillIntro.Type.Add, SkillIntro.Type.Edd, SkillIntro.Type.HasTime};
    }

    @Override
    public String getIntro() {
        return  String.format("狂热,让指定一人伤害变为真实伤害持续%s%%秒", getAddP(getJid(), getId()));
    }

    @Override
    public Skill create(SkillInfo info, Number who, Number... nums) {
        return  new Skill(info, who, new CopyOnWriteArrayList<>(nums), "狂热") {
            @Override
            public void before() {
                q = oneNearest(who, nums);
                putPerson(getInfo(q).addTag(TAG_TRUE, 1));
                setTips("作用于 " + Tool.At(q));
            }

            private Long q;

            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(t11);
                    putPerson(getInfo(q).eddTag(TAG_TRUE, 1));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }
}
