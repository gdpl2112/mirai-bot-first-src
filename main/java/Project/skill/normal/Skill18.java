package Project.skill.normal;

import Project.skill.SkillTemplate;
import io.github.kloping.mirai0.Entitys.gameEntitys.*;
import io.github.kloping.mirai0.unitls.Tools.Tool;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.dataBases.GameDataBase.*;
import static Project.dataBases.skill.SkillDataBase.*;
import static Project.services.detailServices.GameDetailServiceUtils.*;
import static Project.services.detailServices.GameJoinDetailService.*;
import static Project.services.detailServices.GameSkillDetailService.*;
import static io.github.kloping.mirai0.unitls.Tools.Tool.RANDOM;
import io.github.kloping.mirai0.Entitys.gameEntitys.base.BaseInfo;
import Project.services.detailServices.GameBoneDetailService;
/**
 * @author github.kloping
 */
public class Skill18 extends SkillTemplate {
    @Override
    public void before() {
    }

    public Skill18() {
        super(18);
    }

    @Override
    public SkillIntro.Type[] getTypes() {
        return  new SkillIntro.Type[]{SkillIntro.Type.Att, SkillIntro.Type.Special};
    }

    @Override
    public String getIntro() {
        return  String.format("攻击指定敌人,对血量越少的敌人造成的伤害越高 已损失50%%时加成为攻击x%s%%", getAddP(getJid(), getId()));
    }

    @Override
    public Skill create(SkillInfo info, Number who, Number... nums) {
        return  new Skill(info, who, new CopyOnWriteArrayList<>(nums), "斩杀技") {
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
                long att = getInfo(who).getAtt();
                int x = toPercent(th, max);
                x = 100 - x;
                int y = (int) (((float) x / 50f) * 100);
                long v = percentTo(b, att);
                v = percentTo(y, v);
                StringBuilder sb = new StringBuilder();
                v = v < 0 ? 0 : v;
                attGhostOrMan(sb, who, nums[0], v);
                setTips(sb.toString());
            }
        };
    }
}
