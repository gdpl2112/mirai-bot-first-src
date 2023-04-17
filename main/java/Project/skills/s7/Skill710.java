package Project.skills.s7;

import Project.utils.VelocityUtils;
import Project.skills.SkillTemplate;
import io.github.kloping.mirai0.commons.Skill;
import Project.commons.gameEntitys.SkillInfo;
import Project.commons.resouce_and_tool.CommonSource;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.dataBases.GameDataBase.getInfo;
import static Project.services.detailServices.GameDetailServiceUtils.attGhostOrMan;
import static Project.services.detailServices.GameSkillDetailService.nearest;

/**
 * @author github.kloping
 */
public class Skill710 extends SkillTemplate {


    public Skill710() {
        super(710);
    }

    public String getIntro() {
        return VelocityUtils.getTemplateToString(String.format("skill/%s.intro", getJid()));
    }

    @Override
    public Skill create(SkillInfo info, Number who, Number... nums) {
        return new Skill(info, who, new CopyOnWriteArrayList<>(nums), "碧灵蛇皇") {
            @Override
            public void before() {
                StringBuilder sb = new StringBuilder();
                for (Long number : nearest(2, nums)) {
                    long v = CommonSource.percentTo(30, getInfo(who).att());
                    attGhostOrMan(sb, who, number, v);
                }
                setTips(sb.toString());
            }

            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(10 * 1000);
                    StringBuilder sb = new StringBuilder();
                    for (Long number : nearest(2, nums)) {
                        long v = CommonSource.percentTo(30, getInfo(who).att());
                        attGhostOrMan(sb, who, number, v);
                    }
                    setTips(sb.toString());
                    sb = new StringBuilder();
                    Thread.sleep(10 * 1000);
                    for (Long number : nearest(2, nums)) {
                        long v = CommonSource.percentTo(30, getInfo(who).att());
                        attGhostOrMan(sb, who, number, v);
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
