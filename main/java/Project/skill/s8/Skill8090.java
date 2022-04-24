
package Project.skill.s8;

import Project.skill.SkillTemplate;
import io.github.kloping.mirai0.commons.Skill;
import io.github.kloping.mirai0.commons.SkillIntro;
import io.github.kloping.mirai0.commons.gameEntitys.SkillInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static Project.services.detailServices.GameDetailServiceUtils.attGhostOrMan;
import static Project.services.detailServices.GameSkillDetailService.getAddP;
import static Project.services.detailServices.GameSkillDetailService.nearest;
import static io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet.FinalNormalString.ATTACK_BREAK;
import static io.github.kloping.mirai0.unitls.Tools.Tool.percentTo;

/**
 * @author github.kloping
 */
public class Skill8090 extends SkillTemplate {

    public Skill8090() {
        super(8090);
    }

    @Override
    public SkillIntro.Type[] getTypes() {
        return new SkillIntro.Type[]{SkillIntro.Type.Att, SkillIntro.Type.ToOne};
    }

    @Override
    public String getIntro() {
        return String.format("七杀剑第八魂技,2秒内对敌人(最多3个造成%s次的伤害,每次造成攻击的2%%伤害", getAddP(getJid(), getId()));
    }

    public static final long t0 = 2000;

    @Override
    public Skill create(SkillInfo info, Number who, Number... nums) {
        return new Skill(info, who, new CopyOnWriteArrayList<>(nums), "七杀剑第八魂技") {
            private List<Long> ms = new ArrayList<>();
            private int c = 0;
            private long v = 0;
            private long v0 = 0;
            private long e = 0;

            @Override
            public void before() {
                ms.addAll(Arrays.asList(nearest(3, nums)));
                c = info.getAddPercent();
            }

            @Override
            public void run() {
                super.run();
                v = percentTo(2, getPersonInfo().att());
                StringBuilder sb = new StringBuilder();
                e = (t0 / c);
                try {
                    while (c-- > 0) {
                        eve(sb);
                    }
                } catch (InterruptedException ex) {
                    setTips(ATTACK_BREAK);
                }
                setTips(String.format("累计造成%s点伤害", v0));
            }

            private void eve(StringBuilder sb) throws InterruptedException {
                Thread.sleep(e);
                for (Long m : ms) {
                    v0 += v;
                    attGhostOrMan(sb, who, m, v);
                }
            }
        };
    }
}
