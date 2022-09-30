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
import static io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet.FinalNormalString.ATTACK_BREAK;
import static io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet.FinalString.NEWLINE;

/**
 * @author github.kloping
 */
public class Skill1009 extends SkillTemplate {

    public Skill1009() {
        super(1009);
        setName("主动出击");
    }

    @Override
    public String getIntro() {
        return String.format("魂兽普通技能,主动出击,5秒后对所有玩家对自己有威胁的玩家造成%s%%的伤害(包括支援的玩家2秒后)", getAddP(getJid(), getId()));
    }

    @Override
    public Skill create(SkillInfo info, Number who, Number... nums) {
        return new Skill(info, who, new CopyOnWriteArrayList<>(nums), "") {
            @Override
            public void before() {
            }

            @Override
            public void run() {
                super.run();
                GhostObj ghostObj = GameJoinDetailService.getGhostObjFrom(-who.longValue());
                try {
                    Thread.sleep(5000);
                    int b = getAddP(getJid(), getId()).intValue();
                    long v = percentTo(b, ghostObj.getAtt());
                    StringBuilder sb = new StringBuilder();
                    sb.append("对你造成").append(v).append("伤害").append(NEWLINE);
                    sb.append(GameDetailService.beaten(-who.longValue(), -2, v));
                    sb.append(NEWLINE);
                    sb.append(GameController.gameService.info(-who.longValue()));
                    setTips(sb.toString());
                    for (Long with : ghostObj.getWiths()) {
                        if (ghostObj.getWiths().size() > 0) {
                            Thread.sleep(2000);
                            sb = new StringBuilder();
                            sb.append("对支援者造成").append(v).append("伤害").append(NEWLINE);
                            sb.append(GameDetailService.beaten(with, -2, v));
                            sb.append(NEWLINE);
                            sb.append(GameController.gameService.info(-who.longValue()));
                            setTips(sb.toString());
                        }
                    }
                } catch (InterruptedException e) {
                    setTips(ATTACK_BREAK);
                }
            }
        };
    }
}
