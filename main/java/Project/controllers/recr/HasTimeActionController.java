package Project.controllers.recr;

import Project.dataBases.DataBase;
import Project.dataBases.GameDataBase;
import Project.dataBases.SourceDataBase;
import io.github.kloping.MySpringTool.annotations.AllMess;
import io.github.kloping.MySpringTool.annotations.Before;
import io.github.kloping.MySpringTool.annotations.Controller;
import io.github.kloping.MySpringTool.exceptions.NoRunException;
import io.github.kloping.mirai0.commons.Group;
import io.github.kloping.mirai0.commons.TradingRecord;
import io.github.kloping.mirai0.commons.broadcast.enums.ObjType;

import static Project.controllers.auto.ControllerTool.opened;
import static Project.dataBases.GameDataBase.addToBgs;
import static Project.dataBases.task.TaskCreator.getRandObj1000;
import static io.github.kloping.mirai0.Main.Resource.println;
import static io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet.FinalString.CLOSE_STR;
import static io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet.FinalString.OPEN_STR;
import static io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet.FinalValue.NOT_OPEN_NO_RUN_EXCEPTION;
import static io.github.kloping.mirai0.unitls.Tools.Tool.RANDOM;

/**
 * @author github.kloping
 */
@Controller
public class HasTimeActionController {
    public HasTimeActionController() {
        println(this.getClass().getSimpleName() + "构建");
    }


    /**
     * 使用粽子
     * 获得一个粽子
     * 获得100-1000积分
     * 获得100-1000金魂币
     * 获得随机道具
     * 获得100-1w经验
     * 获得十年-万年魂环
     *
     * @param who
     * @return
     */
    public static String use(long who) {
        int r = 0;
        switch (RANDOM.nextInt(10)) {
            case 0:
                GameDataBase.addToBgs(who, 7001, ObjType.got);
                return "使用成功获得一个粽子";
            case 1:
                r = RANDOM.nextInt(900) + 100;
                GameDataBase.getInfo(who).addGold((long) r, new TradingRecord()
                        .setFrom(-1)
                        .setMain(who).setDesc("从粽子获得")
                        .setTo(who)
                        .setMany(r)
                        .setType0(TradingRecord.Type0.gold)
                        .setType1(TradingRecord.Type1.add)).apply();
                return "获得" + r + "金魂币";
            case 3:
                int id = getRandObj1000();
                addToBgs(who, id, ObjType.got);
                return "获得" + SourceDataBase.getImgPathById(id);
            case 4:
                r = RANDOM.nextInt(9900) + 100;
                GameDataBase.getInfo(who).addXp((long) r).apply();
                return "获得" + r + "点经验";
            case 5:
                r = RANDOM.nextInt(4) + 201;
                addToBgs(who, r, ObjType.got);
                return "获得" + SourceDataBase.getImgPathById(r);
            default:
                r = RANDOM.nextInt(900) + 100;
                DataBase.addScore(r, who);
                return "获得" + r + "积分";
        }
    }

    @Before
    public void before(@AllMess String mess, Group group) throws NoRunException {
        if (mess.contains(OPEN_STR) || mess.contains(CLOSE_STR)) {
            return;
        }
        if (!opened(group.getId(), this.getClass())) {
            throw NOT_OPEN_NO_RUN_EXCEPTION;
        }
    }
//
//    public static Set<Long> received = new HashSet<>();
//
//    static {
//        TimerController.ZERO_RUNS.add(() -> received.clear());
//    }
//
//    @Action("领取粽子")
//    public String a0(User user) {
//        if (received.contains(user.getId())) {
//            return "您今天已经领取过了哦";
//        } else {
//            received.add(user.getId());
//            GameDataBase.addToBgs(user.getId(), 7001, ObjType.got);
//            return "领取成功,已发放至背包" + SourceDataBase.getImgPathById(7001);
//        }
//    }

}
