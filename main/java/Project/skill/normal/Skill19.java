package Project.skill.normal;

import Project.controllers.auto.ControllerSource;
import Project.skill.SkillTemplate;
import io.github.kloping.mirai0.commons.GhostObj;
import io.github.kloping.mirai0.commons.PersonInfo;
import io.github.kloping.mirai0.commons.Skill;
import io.github.kloping.mirai0.commons.gameEntitys.SkillInfo;
import io.github.kloping.mirai0.commons.resouce_and_tool.CommonSource;
import io.github.kloping.mirai0.unitls.Tools.Tool;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.dataBases.GameDataBase.getInfo;
import static Project.services.detailServices.GameDetailServiceUtils.attGhostOrMan;
import static Project.services.detailServices.ac.GameJoinDetailService.getGhostObjFrom;
import static Project.services.detailServices.GameSkillDetailService.getAddP;
import static io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet.FinalNormalString.ATTACK_BREAK;

/**
 * @author github.kloping
 */
public class Skill19 extends SkillTemplate {


    public Skill19() {
        super(19);
    }


    @Override
    public String getIntro() {
        return String.format("蓄力型技能,指定敌人,蓄力1.5倍攻击前摇秒后对其造成 攻击的%s +- 10%% 的 伤害", getAddP(getJid(), getId()));
    }

    @Override
    public Skill create(SkillInfo info, Number who, Number... nums) {
        return new Skill(info, who, new CopyOnWriteArrayList<>(nums), "蓄力1") {
            private int b;

            @Override
            public void before() {
                if (nums.length == 0) {
                    setTips("未选择");
                    return;
                }
                int r = Tool.tool.RANDOM.nextInt(20) - 10;
                if (nums[0].longValue() < 0) {
                    GhostObj ghostObj = getGhostObjFrom(who.longValue());
                    if (ghostObj == null) {
                        setTips("未遇见魂兽");
                    }
                } else {
                    PersonInfo in = getInfo(nums[0]);
                }
                b = info.getAddPercent() + r;
                setTips(String.format("将造成%s%%(%s)伤害", b, CommonSource.percentTo(b, getInfo(who).att())));
            }

            @Override
            public void run() {
                super.run();
                try {
                    long t = (long) (ControllerSource.playerBehavioralManager.getAttPre(who.longValue()) * 1.5f);
                    Thread.sleep(t);
                    long att = getInfo(who).att();
                    long v = CommonSource.percentTo(b, att);
                    StringBuilder sb = new StringBuilder();
                    attGhostOrMan(sb, who, nums[0], v);
                    setTips(sb.toString());
                } catch (InterruptedException e) {
                    setTips(ATTACK_BREAK);
                }
            }
        };
    }
}
