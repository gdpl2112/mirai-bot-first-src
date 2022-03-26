package Project.skill.s7;

import Project.services.detailServices.GameBoneDetailService;
import Project.skill.SkillTemplate;
import io.github.kloping.mirai0.commons.PersonInfo;
import io.github.kloping.mirai0.commons.Skill;
import io.github.kloping.mirai0.commons.SkillIntro;
import io.github.kloping.mirai0.commons.gameEntitys.SkillInfo;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.dataBases.GameDataBase.getInfo;
import static Project.dataBases.skill.SkillDataBase.*;
import static Project.services.detailServices.GameSkillDetailService.getAddP;

/**
 * @author github.kloping
 */
public class Skill721 extends SkillTemplate {


    public Skill721() {
        super(721);
    }

    @Override
    public SkillIntro.Type[] getTypes() {
        return new SkillIntro.Type[]{SkillIntro.Type.WHZs, SkillIntro.Type.HasTime, SkillIntro.Type.Special, SkillIntro.Type.Add};
    }

    @Override
    public String getIntro() {
        return String.format("释放幽冥真身,令自身 增加%s点闪避,并增加%s的攻击", getAddP(getJid(), getId()), getAddP(getJid(), getId()));
    }

    @Override
    public Skill create(SkillInfo info, Number who, Number... nums) {
        return new Skill(info, who, new CopyOnWriteArrayList<>(nums), "幽冥真身") {
            private long v;
            private Long v2;
            private Long q1;

            @Override
            public void before() {
                Long q = who.longValue();
                PersonInfo pInfo = getInfo(q);
                Long lon = pInfo.getAtt();
                v2 = percentTo(info.getAddPercent(), lon);
                addAttHasTime(who.longValue(), new HasTimeAdder(System.currentTimeMillis() + t721, who.longValue(), v2));
                q1 = Long.valueOf(who + "");
                v = Long.valueOf(info.getAddPercent());
                GameBoneDetailService.addForAttr(q1, v, GameBoneDetailService.Type.HIDE_PRO);
            }

            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(t721);
                    GameBoneDetailService.addForAttr(q1, -v, GameBoneDetailService.Type.HIDE_PRO);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }
}
