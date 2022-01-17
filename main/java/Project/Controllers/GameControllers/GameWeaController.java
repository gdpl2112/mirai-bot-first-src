package Project.Controllers.GameControllers;

import Entitys.Group;
import Entitys.User;
import Project.DataBases.GameDataBase;
import Project.services.Iservice.IGameWeaService;
import io.github.kloping.MySpringTool.annotations.*;
import io.github.kloping.MySpringTool.exceptions.NoRunException;

import static Project.Controllers.ControllerTool.opened;
import static io.github.kloping.Mirai.Main.Resource.println;

/**
 * @author github-kloping
 */
@Controller
public class GameWeaController {
    public GameWeaController() {
        println(this.getClass().getSimpleName() + "构建");
    }

    @AutoStand
    IGameWeaService gameWeaService;

    @Before
    public void before(Group group, User qq) throws NoRunException {
        if (!opened(group.getId(), this.getClass())) {
            throw new NoRunException("未开启");
        }
    }

    @Action("使用暗器<.{0,}=>name>")
    public String UseWea(@Param("name") String name, Group group, User qq) {
        String sss = gameWeaService.UseAq(name, qq.getId());
        return sss;
    }

    @Action("暗器背包")
    public String AqBgs(User qq, Group group) {
        String str = gameWeaService.AqBgs(qq.getId());
        return str;
    }

    @Action("暗器制作表")
    public String ListAq(User qq, Group group) {
        String str = gameWeaService.AqList();
        return str;
    }

    @Action("暗器菜单")
    public String AqMenu(Group group, User qq) {
        String str = gameWeaService.AqMeun();
        return str;
    }

    @Action("制作暗器<.{1,}=>name>")
    public String makeAq(User qq, @Param("name") String name, Group group) {
        int id = GameDataBase.Name2idMaps.get(name.trim());
        if (!(id > 1000 && id < 1200))
            return "系统找不到=>" + name;
        String str = gameWeaService.makeAq(qq.getId(), id);
        return str;
    }
}
