package Project.skill.ghost;

import Project.services.detailServices.ac.GameJoinDetailService;
import Project.skill.SkillTemplate;
import io.github.kloping.mirai0.commons.GhostObj;
import io.github.kloping.mirai0.commons.Skill;
import io.github.kloping.mirai0.commons.gameEntitys.SkillInfo;
import io.github.kloping.mirai0.unitls.Tools.Tool;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.services.detailServices.ac.GameJoinDetailService.saveGhostObjIn;
import static Project.services.detailServices.GameSkillDetailService.getAddP;
import static io.github.kloping.mirai0.commons.resouce_and_tool.CommonSource.toPercent;

/**
 * @author github.kloping
 */
public class Skill1006 extends SkillTemplate {

    public Skill1006() {
        super(1006);
        setName("逃跑技能");
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
                if (Tool.tool.RANDOM.nextInt(101) < v) {
                    saveGhostObjIn(ghostObj.getWhoMeet(), null);
                    setTips("逃跑成功");
                } else {
                    setTips("逃跑失败");
                }
            }
        };
    }
}
