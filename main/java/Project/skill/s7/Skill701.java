package Project.skill.s7;

import Project.skill.SkillTemplate;
import io.github.kloping.mirai0.commons.Skill;
import io.github.kloping.mirai0.commons.gameEntitys.SkillInfo;
import io.github.kloping.mirai0.commons.resouce_and_tool.CommonSource;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.dataBases.GameDataBase.getInfo;
import static Project.services.detailServices.GameDetailServiceUtils.attGhostOrMan;
import static Project.services.detailServices.GameSkillDetailService.getAddP;
import static Project.services.detailServices.GameSkillDetailService.nearest;

/**
 * @author github.kloping
 */
public class Skill701 extends SkillTemplate {

    public Skill701() {
        super(701);
    }


    @Override
    public String getIntro() {
        return String.format("释放雷霆之力,对指定2个敌人造成%s%%攻击的伤害,10秒后在造成30%%的伤害,10秒后造成10%%的伤害", getAddP(getJid(), getId()));
    }

    @Override
    public Skill create(SkillInfo info, Number who, Number... nums) {
        return new Skill(info, who, new CopyOnWriteArrayList<>(nums), "蓝电霸王龙武魂真身") {
            @Override
            public void before() {
                StringBuilder sb = new StringBuilder();
                for (Long q : nearest(2, nums)) {
                    long v = CommonSource.percentTo(60, getInfo(who).att());
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
                        long v = CommonSource.percentTo(30, getInfo(who).att());
                        attGhostOrMan(sb, who, q, v);
                    }
                    setTips(sb.toString());
                    sb = new StringBuilder();
                    Thread.sleep(10 * 1000);
                    for (Long q : nearest(2, nums)) {
                        long v = CommonSource.percentTo(10, getInfo(who).att());
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
