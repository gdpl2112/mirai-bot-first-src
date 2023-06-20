package Project.controllers.gameControllers;

import Project.commons.SpGroup;
import Project.commons.SpUser;
import Project.commons.gameEntitys.AchievementEntity;
import Project.dataBases.AchievementDataBase;
import io.github.kloping.MySpringTool.annotations.*;
import io.github.kloping.MySpringTool.exceptions.NoRunException;
import io.github.kloping.mirai0.Main.iutils.MessageUtils;
import Project.utils.Tools.Tool;

import static Project.commons.rt.ResourceSet.FinalNormalString.BG_TIPS;
import static Project.commons.rt.ResourceSet.FinalString.ERR_TIPS;
import static Project.commons.rt.ResourceSet.FinalString.NEWLINE;
import static Project.commons.rt.ResourceSet.FinalValue.NOT_OPEN_NO_RUN_EXCEPTION;
import static Project.controllers.auto.ControllerTool.opened;
import static Project.dataBases.GameDataBase.getInfo;

/**
 * @author github.kloping
 */
@Controller
public class GameAchievementController {

    @Before
    public void before(SpUser qq, SpGroup group, @AllMess String str) throws NoRunException {
        if (!opened(group.getId(), this.getClass())) {
            throw NOT_OPEN_NO_RUN_EXCEPTION;
        }
        if (getInfo(qq.getId()).isBg()) {
            MessageUtils.INSTANCE.sendMessageInGroupWithAt(BG_TIPS, group.getId(), qq.getId());
            throw new NoRunException(BG_TIPS);
        }
    }

    @Action("成就列表<.*?=>n>")
    public Object list(long qid, @Param("n") String s) {
        Integer n = Tool.INSTANCE.getInteagerFromStr(s);
        n = n == null ? 1 : n;
        n--;
        StringBuilder sb = new StringBuilder("成就列表\n\n");
        Integer finalN = n;
        AchievementDataBase.INSTANCE.entityMap.forEach((k, v) -> {
            if (v.getAid() > finalN * 10 && v.getAid() <= (finalN + 1) * 10) {
                sb.append(k).append(".").append(v.intro(qid))
                        .append(NEWLINE).append("\t\t  ").append(v.isFinish(qid) ? "已完成" : "未完成").append(NEWLINE);
            }
        });
        return sb.toString();
    }

    @Action("领取成就<.*?=>n>")
    public Object finish(long qid, @Param("n") String s) {
        try {
            Integer n = Integer.valueOf(s);
            if (AchievementDataBase.INSTANCE.entityMap.containsKey(n)) {
                AchievementEntity entity = AchievementDataBase.INSTANCE.entityMap.get(n);
                return entity.finish(qid);
            } else {
                return "成就不存在";
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return ERR_TIPS;
    }

}
