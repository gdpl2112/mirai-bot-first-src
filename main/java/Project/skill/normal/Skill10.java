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
public class Skill10 extends SkillTemplate {
    

    public Skill10() {
        super(10);
    }

    @Override
    public SkillIntro.Type[] getTypes() {
        return  WhTypes.T6;
    }

    @Override
    public String getIntro() {
        return  String.format("在接下来的来%s秒内,免疫一次死亡", getAddP(getJid(), getId()));
    }

    @Override
    public Skill create(SkillInfo info, Number who, Number... nums) {
        return  new Skill(info, who, new CopyOnWriteArrayList<>(nums), "免死") {
            @Override
            public void before() {
                putPerson(getInfo(who).addTag(TAG_MS, 0));
                setTips("作用于 " + Tool.At(who.longValue()));
            }

            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(info.getAddPercent() * 1000);
                    putPerson(getInfo(who).eddTag(TAG_MS, 0));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }
}
