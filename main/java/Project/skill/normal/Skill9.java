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
public class Skill9 extends SkillTemplate {
    @Override
    public void before() {
    }

    public Skill9() {
        super(9);
    }

    @Override
    public SkillIntro.Type[] getTypes() {
        return  new SkillIntro.Type[]{SkillIntro.Type.Special, SkillIntro.Type.Add, SkillIntro.Type.ToOne, SkillIntro.Type.HasTime};
    }

    @Override
    public String getIntro() {
        return  String.format("给予指定一个人,在接下来的来两分钟内,每24秒恢复%s%%的生命值", getAddP(getJid(), getId()));
    }

    @Override
    public Skill create(SkillInfo info, Number who, Number... nums) {
        return  new Skill(info, who, new CopyOnWriteArrayList<>(nums), "__") {
            @Override
            public void before() {
                v = 1;
                q = oneNearest(who, nums);
                eve();
                setTips("作用于 " + Tool.At(q));
            }

            private Integer v;
            private long q;

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
                putPerson(getInfo(q).addHp(percentTo(info.getAddPercent(), l)));
                return true;
            }
        };
    }
}
