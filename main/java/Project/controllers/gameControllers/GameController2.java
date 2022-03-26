package Project.controllers.gameControllers;


import Project.dataBases.GameDataBase;
import Project.interfaces.Iservice.IGameObjService;
import Project.interfaces.Iservice.IGameService;
import io.github.kloping.MySpringTool.annotations.*;
import io.github.kloping.MySpringTool.exceptions.NoRunException;
import io.github.kloping.mirai0.commons.Group;
import io.github.kloping.mirai0.commons.User;
import io.github.kloping.mirai0.unitls.Tools.Tool;
import io.github.kloping.number.NumberUtils;

import static Project.controllers.auto.ControllerTool.opened;
import static io.github.kloping.mirai0.Main.Resource.println;

/**
 * @author github-kloping
 */
@Controller
public class GameController2 {
    @AutoStand
    IGameService service;
    @AutoStand
    IGameObjService gameObjService;

    public GameController2() {
        println(this.getClass().getSimpleName() + "构建");
    }

    @Before
    public void before(User qq, Group group, @AllMess String str) throws NoRunException {
        if (!opened(group.getId(), this.getClass())) {
            throw new NoRunException("未开启");
        }
    }

    @Action(value = "详细信息", otherName = {"详情信息"})
    public String m1(long q) {
        return service.detailInfo(q);
    }

    @Action("收徒<.+=>str>")
    public String m2(long q, @Param("str") String str) {
        long q2 = Long.parseLong(NumberUtils.findNumberFromString(str).trim());
        if (q == q2) return "scram";
        return service.shouTu(q, q2);
    }

    @Action(value = "出师")
    public String m3(long q) {
        return service.chuShi(q);
    }

    @Action("升级第<.+=>str>")
    public String upda(@Param("str") String str, long q) {
        if (str.contains("魂环")) {
            str = str.replace("魂环", "").replace("第", "");
            String s1 = Tool.findNumberZh(str);
            Integer st = Integer.valueOf(Tool.chineseNumber2Int(s1));
            return service.upHh(q, st);
        }
        throw new NoRunException();
    }

    @Action("合成<.+=>name>")
    public String m1(@Param("name") String name, long q) {
        try {
            int id = GameDataBase.NAME_2_ID_MAPS.get(name);
            return gameObjService.compound(q, id);
        } catch (Exception e) {
            return "未找到相关物品";
        }
    }
}
