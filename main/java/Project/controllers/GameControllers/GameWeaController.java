package Project.controllers.GameControllers;

import io.github.kloping.mirai0.Entitys.Group;
import io.github.kloping.mirai0.Entitys.User;
import Project.dataBases.GameDataBase;
import Project.interfaces.Iservice.IGameWeaService;
import io.github.kloping.MySpringTool.annotations.*;
import io.github.kloping.MySpringTool.exceptions.NoRunException;

import static Project.controllers.auto.ControllerTool.opened;
import static io.github.kloping.mirai0.Main.Resource.println;

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
        String sss = gameWeaService.useAq(name, qq.getId());
        return sss;
    }

    @Action("暗器背包")
    public String AqBgs(User qq, Group group) {
        String str = gameWeaService.aqBgs(qq.getId());
        return str;
    }

    @Action("暗器制作表")
    public String ListAq(User qq, Group group) {
        String str = gameWeaService.aqList();
        return str;
    }

    @Action("暗器菜单")
    public String AqMenu(Group group, User qq) {
        String str = gameWeaService.aqMeun();
        return str;
    }

    @Action("制作暗器<.{1,}=>name>")
    public String makeAq(User qq, @Param("name") String name, Group group) {
        Integer id = GameDataBase.NAME_2_ID_MAPS.get(name.trim());
        if (id == null || !(id > 1000 && id < 1200)) {
            return "系统找不到=>" + name;
        }
        String str = gameWeaService.makeAq(qq.getId(), id);
        return str;
    }
}
