package Project.skills.s8;

import Project.controllers.auto.ControllerSource;
import Project.skills.SkillTemplate;
import io.github.kloping.mirai0.commons.Skill;
import Project.commons.gameEntitys.SkillInfo;
import Project.commons.resouce_and_tool.CommonSource;
import io.github.kloping.mirai0.unitls.Tools.Tool;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.dataBases.GameDataBase.getInfo;
import static Project.services.detailServices.GameDetailServiceUtils.attGhostOrMan;
import static Project.commons.resouce_and_tool.ResourceSet.FinalNormalString.ATTACK_BREAK;

/**
 * @author github.kloping
 */
public class Skill8080 extends SkillTemplate {

    public Skill8080() {
        super(8080);
    }

    @Override
    public Skill create(SkillInfo info, Number who, Number... nums) {
        return new Skill(info, who, new CopyOnWriteArrayList<>(nums), "邪火第八魂技") {
            private long qid = -1;

            @Override
            public void before() {
            }

            @Override
            public void run() {
                super.run();
                if (nums.length == 0) return;
                qid = nums[0].longValue();
                int r = Tool.INSTANCE.RANDOM.nextInt(20) - 10;
                int b = info.getAddPercent();
                b += r;
                setTips(String.format("将造成%s%%(%s)伤害", b, CommonSource.percentTo(b, getInfo(who).att())));
                long t = ControllerSource.playerBehavioralManager.getAttPre(who.longValue());
                t *= 2;
                try {
                    Thread.sleep(t);
                    long att = getInfo(who).att();
                    long v = CommonSource.percentTo(b, att);
                    StringBuilder sb = new StringBuilder();
                    attGhostOrMan(sb, who, qid, v);
                    setTips(sb.toString());
                } catch (InterruptedException e) {
                    setTips(ATTACK_BREAK);
                }
            }
        };
    }
}
