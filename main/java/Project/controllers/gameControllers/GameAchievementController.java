package Project.controllers.gameControllers;

import Project.dataBases.AchievementDataBase;
import io.github.kloping.MySpringTool.annotations.*;
import io.github.kloping.MySpringTool.exceptions.NoRunException;
import io.github.kloping.mirai0.Main.ITools.MessageTools;
import io.github.kloping.mirai0.commons.Group;
import io.github.kloping.mirai0.commons.User;
import io.github.kloping.mirai0.commons.gameEntitys.AchievementEntity;

import static Project.controllers.auto.ControllerTool.opened;
import static Project.dataBases.GameDataBase.getInfo;
import static io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet.FinalNormalString.BG_TIPS;
import static io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet.FinalString.ERR_TIPS;
import static io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet.FinalString.NEWLINE;
import static io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet.FinalValue.NOT_OPEN_NO_RUN_EXCEPTION;

/**
 * @author github.kloping
 */
@Controller
public class GameAchievementController {

    @Before
    public void before(User qq, Group group, @AllMess String str) throws NoRunException {
        if (!opened(group.getId(), this.getClass())) {
            throw NOT_OPEN_NO_RUN_EXCEPTION;
        }
        if (getInfo(qq.getId()).isBg()) {
            MessageTools.instance.sendMessageInGroupWithAt(BG_TIPS, group.getId(), qq.getId());
            throw new NoRunException(BG_TIPS);
        }
    }

    @Action("成就列表<.*?=>n>")
    public Object list(long qid, @Param("n") String s) {
        StringBuilder sb = new StringBuilder();
        AchievementDataBase.INSTANCE.entityMap.forEach((k, v) -> {
            sb.append(k).append(".").append(v.intro(qid))
                    .append(NEWLINE).append("\t\t  ").append(v.isFinish(qid) ? "已完成" : "未完成").append(NEWLINE);
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
