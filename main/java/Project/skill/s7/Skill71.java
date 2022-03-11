package Project.skill.s7;

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
public class Skill71 extends SkillTemplate {
    @Override
    public void before() {
    }

    public Skill71() {
        super(71);
    }

    @Override
    public SkillIntro.Type[] getTypes() {
        return  new SkillIntro.Type[]{SkillIntro.Type.WHZs, SkillIntro.Type.Att, SkillIntro.Type.HasTime, SkillIntro.Type.ToNum};
    }

    @Override
    public String getIntro() {
        return  String.format("释放雷霆之力,对指定2个敌人造成%s%%攻击的伤害,10秒后在造成30%的伤害,10秒后造成10%的伤害", getAddP(getJid(), getId()));
    }

    @Override
    public Skill create(SkillInfo info, Number who, Number... nums) {
        return  new Skill(info, who, new CopyOnWriteArrayList<>(nums), "蓝电霸王龙武魂真身") {
            @Override
            public void before() {
                StringBuilder sb = new StringBuilder();
                for (Long q : nearest(2, nums)) {
                    long v = percentTo(60, getInfo(who).getAtt());
                    attGhostOrMan(sb, who, q, v);
                }
                setTips(sb.toString());
            }

            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(10 * 1000);
                    StringBuilder sb = new StringBuilder();
                    for (Long q : nearest(2, nums)) {
                        long v = percentTo(30, getInfo(who).getAtt());
                        attGhostOrMan(sb, who, q, v);
                    }
                    setTips(sb.toString());
                    sb = new StringBuilder();
                    Thread.sleep(10 * 1000);
                    for (Long q : nearest(2, nums)) {
                        long v = percentTo(10, getInfo(who).getAtt());
                        attGhostOrMan(sb, who, q, v);
                    }
                    setTips(sb.toString());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                setTips("武魂真身失效");
            }
        };
    }
}
