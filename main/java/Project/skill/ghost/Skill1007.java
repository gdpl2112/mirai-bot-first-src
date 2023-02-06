package Project.skill.ghost;

import Project.services.detailServices.ac.GameJoinDetailService;
import Project.skill.SkillTemplate;
import io.github.kloping.mirai0.commons.GhostObj;
import io.github.kloping.mirai0.commons.Skill;
import io.github.kloping.mirai0.commons.gameEntitys.SkillInfo;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.services.detailServices.GameSkillDetailService.getAddP;
import static io.github.kloping.mirai0.commons.resouce_and_tool.CommonSource.percentTo;
import static io.github.kloping.mirai0.commons.resouce_and_tool.CommonSource.toPercent;

/**
 * @author github.kloping
 */
public class Skill1007 extends SkillTemplate {

    public Skill1007() {
        super(1007);
        setName("恢复技能");
    }

    @Override
    public Skill create(SkillInfo info, Number who, Number... nums) {
        return new Skill(info, who, new CopyOnWriteArrayList<>(nums), "") {
            @Override
            public void before() {
                ghostObj = GameJoinDetailService.getGhostObjFrom(-who.longValue());
            }

            GhostObj ghostObj;

            @Override
            public void run() {
                super.run();
                long v = getAddP(getJid(), getId());
                int b = toPercent(ghostObj.getHp(), ghostObj.getMaxHp());
                b = 100 - b;
                v += b / 2;
                ghostObj.setHp(ghostObj.getHp() + percentTo((int) v, ghostObj.getAtt()));
                ghostObj.apply();
                setTips("加血" + v + "%");
            }
        };
    }
}
