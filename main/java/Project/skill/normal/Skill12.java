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
public class Skill12 extends SkillTemplate {
    

    public Skill12() {
        super(12);
    }

    @Override
    public SkillIntro.Type[] getTypes() {
        return  new SkillIntro.Type[]{SkillIntro.Type.Add, SkillIntro.Type.HasTime, SkillIntro.Type.ToOne};
    }

    @Override
    public String getIntro() {
        return  String.format("令自身 增加%s点闪避", getAddP(getJid(), getId()));
    }

    @Override
    public Skill create(SkillInfo info, Number who, Number... nums) {
        return  new Skill(info, who, new CopyOnWriteArrayList<>(nums), "增速闪避") {

            @Override
            public void before() {
                q = oneNearest(who, nums);
                v = Long.valueOf(info.getAddPercent());
                GameBoneDetailService.addForAttr(q, v, GameBoneDetailService.Type.HIDE_PRO);
                setTips("作用于 " + Tool.At(q));
            }

            private Long q, v;

            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(t12);
                    GameBoneDetailService.addForAttr(q, -v, GameBoneDetailService.Type.HIDE_PRO);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }
}
