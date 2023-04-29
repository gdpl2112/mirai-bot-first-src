package Project.skills.normal;

import Project.commons.gameEntitys.SkillInfo;
import Project.commons.rt.CommonSource;
import Project.skills.SkillTemplate;
import io.github.kloping.mirai0.commons.GhostObj;
import io.github.kloping.mirai0.commons.PersonInfo;
import io.github.kloping.mirai0.commons.Skill;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.commons.rt.CommonSource.toPercent;
import static Project.dataBases.GameDataBase.getInfo;
import static Project.services.detailServices.GameDetailServiceUtils.attGhostOrMan;
import static Project.services.detailServices.ac.GameJoinDetailService.getGhostObjFrom;

/**
 * @author github.kloping
 */
public class Skill18 extends SkillTemplate {

    public Skill18() {
        super(18);
    }


    @Override
    public Skill create(SkillInfo info, Number who, Number... nums) {
        return new Skill(info, who, new CopyOnWriteArrayList<>(nums), "斩杀技") {
            @Override
            public void before() {
                if (nums.length == 0) {
                    setTips("未选择");
                    return;
                }
                long maxTemp = 0, thTemp = 0;
                if (nums[0].longValue() < 0 || nums[0].longValue() == -2) {
                    GhostObj ghostObj = getGhostObjFrom(who.longValue());
                    if (ghostObj == null) {
                        setTips("未遇见魂兽");
                        return;
                    }
                    maxTemp = ghostObj.getMaxHp();
                    thTemp = ghostObj.getHp();
                } else {
                    PersonInfo in = getInfo(nums[0]);
                    maxTemp = in.getHpL();
                    thTemp = in.getHp();
                }
                final long max = maxTemp > 0 ? maxTemp : 0;
                final long th = thTemp > 0 ? thTemp : 0;
                int b = info.getAddPercent();
                long att = getInfo(who).att();
                int x = toPercent(th, max);
                x = 100 - x;
                int y = (int) (((float) x / 50f) * 100);
                long v = CommonSource.percentTo(b, att);
                v = CommonSource.percentTo(y, v);
                StringBuilder sb = new StringBuilder();
                v = v < 0 ? 0 : v;
                attGhostOrMan(sb, who, nums[0], v);
                setTips(sb.toString());
            }
        };
    }
}
