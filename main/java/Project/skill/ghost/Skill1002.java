package Project.skill.ghost;

import Project.controllers.gameControllers.GameController;
import Project.services.detailServices.GameDetailService;
import Project.services.detailServices.GameJoinDetailService;
import Project.skill.SkillTemplate;
import io.github.kloping.mirai0.commons.GhostObj;
import io.github.kloping.mirai0.commons.Skill;
import io.github.kloping.mirai0.commons.gameEntitys.SkillInfo;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.services.detailServices.GameSkillDetailService.getAddP;
import static io.github.kloping.mirai0.commons.resouce_and_tool.CommonSource.percentTo;
import static io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet.FinalString.NEWLINE;

/**
 * @author github.kloping
 */
public class Skill1002 extends SkillTemplate {

    public Skill1002() {
        super(1002);
        setName("蓄力重击");
    }

    @Override
    public String getIntro() {
        return String.format("魂兽普通技能,蓄力重击,蓄力倒计时结束后对玩家造成%s%%的伤害", getAddP(getJid(), getId()));
    }

    @Override
    public Skill create(SkillInfo info, Number who, Number... nums) {
        return new Skill(info, who, new CopyOnWriteArrayList<>(nums), "蓄力重击") {
            GhostObj ghostObj;

            @Override
            public void before() {
                ghostObj = GameJoinDetailService.getGhostObjFrom(-who.longValue());
            }

            @Override
            public void run() {
                super.run();
                int t0 = 16;
                try {
                    while (t0-- > 0) {
                        Thread.sleep(1000);
                        if (needSay(t0)) {
                            setTips("蓄力倒计时!\r\n" + t0);
                        }
                    }
                    ghostObj = GameJoinDetailService.getGhostObjFrom(-who.longValue());
                    if (ghostObj == null) return;
                    int b = getAddP(getJid(), getId()).intValue();
                    long v = percentTo(b, ghostObj.getAtt());
                    StringBuilder sb = new StringBuilder();
                    sb.append("对你造成").append(v).append("伤害").append(NEWLINE);
                    sb.append(GameDetailService.beaten(-who.longValue(), -2, v));
                    sb.append(NEWLINE);
                    sb.append(GameController.gameService.info(-who.longValue()));
                    setTips(sb.toString());
                } catch (InterruptedException e) {
                    setTips("技能被打断");
                }
            }
        };
    }

    private static boolean needSay(int time) {
        return (time == 1 || time == 3 | time == 8 || time == 10 || time == 15 || time == 20 || time == 25);
    }
}
