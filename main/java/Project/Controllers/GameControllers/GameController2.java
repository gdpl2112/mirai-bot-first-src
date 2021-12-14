package Project.Controllers.GameControllers;


import Entitys.Group;
import Entitys.User;
import Project.DataBases.GameDataBase;
import Project.Services.Iservice.IGameObjService;
import Project.Services.Iservice.IGameService;
import Project.Tools.Tool;
import io.github.kloping.MySpringTool.annotations.*;
import io.github.kloping.MySpringTool.exceptions.NoRunException;
import io.github.kloping.number.NumberUtils;

import static Project.Controllers.ControllerTool.CanGroup;
import static io.github.kloping.Mirai.Main.Resource.Switch.AllK;
import static io.github.kloping.Mirai.Main.Resource.println;

@Controller
public class GameController2 {
    public GameController2() {
        println(this.getClass().getSimpleName() + "构建");
    }


    @Before
    public void before(User qq, Group group, @AllMess String str) throws NoRunException {
        if (!AllK)
            throw new NoRunException();
        if (!CanGroup(group.getId())) {
            throw new NoRunException();
        }
    }

    @AutoStand
    IGameService service;

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

    @AutoStand
    IGameObjService gameObjService;

    @Action("合成<.+=>name>")
    public String m1(@Param("name") String name, long q) {
        try {
            int id = GameDataBase.Name2idMaps.get(name);
            return gameObjService.compound(q, id);
        } catch (Exception e) {
            return "未找到相关物品";
        }
    }
}
