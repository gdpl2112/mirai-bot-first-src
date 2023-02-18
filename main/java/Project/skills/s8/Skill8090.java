package Project.skills.s8;

import Project.skills.SkillTemplate;
import io.github.kloping.mirai0.commons.Skill;
import io.github.kloping.mirai0.commons.gameEntitys.SkillInfo;
import io.github.kloping.mirai0.commons.resouce_and_tool.CommonSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static Project.services.detailServices.GameDetailServiceUtils.attGhostOrMan;
import static Project.services.detailServices.GameSkillDetailService.nearest;
import static io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet.FinalNormalString.ATTACK_BREAK;

/**
 * @author github.kloping
 */
public class Skill8090 extends SkillTemplate {

    public static final long T_0 = 2000;

    public Skill8090() {
        super(8090);
    }

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
                v = CommonSource.percentTo(2, getPersonInfo().att());
                StringBuilder sb = new StringBuilder();
                e = (T_0 / c);
                try {
                    while (c-- > 0) {
                        eve(sb);
                    }
                } catch (InterruptedException ex) {
                    setTips(ATTACK_BREAK);
                }
                setTips(sb.toString().trim());
                setTips(String.format("累计造成%s点伤害", v0));
            }

            private void eve(StringBuilder sb) throws InterruptedException {
                Thread.sleep(e);
                for (Long m : ms) {
                    v0 += v;
                    attGhostOrMan(sb, who, m, v, false);
                }
            }
        };
    }
}
