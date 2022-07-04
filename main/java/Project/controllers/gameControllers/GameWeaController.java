package Project.controllers.gameControllers;

import Project.dataBases.GameDataBase;
import Project.interfaces.Iservice.IGameWeaService;
import io.github.kloping.MySpringTool.annotations.*;
import io.github.kloping.MySpringTool.exceptions.NoRunException;
import io.github.kloping.mirai0.Main.ITools.MessageTools;
import io.github.kloping.mirai0.commons.Group;
import io.github.kloping.mirai0.commons.User;

import static Project.controllers.auto.ControllerTool.opened;
import static Project.dataBases.GameDataBase.getInfo;
import static io.github.kloping.mirai0.Main.Resource.println;
import static io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet.FinalNormalString.BG_TIPS;
import static io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet.FinalValue.NOT_OPEN_NO_RUN_EXCEPTION;

/**
 * @author github-kloping
 */
@Controller
public class GameWeaController {
    @AutoStand
    IGameWeaService gameWeaService;

    public GameWeaController() {
        println(this.getClass().getSimpleName() + "构建");
    }

    @Before
    public void before(Group group, User qq) throws NoRunException {
        if (!opened(group.getId(), this.getClass())) {
            throw NOT_OPEN_NO_RUN_EXCEPTION;
        }
        if (getInfo(qq.getId()).isBg()) {
            MessageTools.instance.sendMessageInGroupWithAt(BG_TIPS, group.getId(), qq.getId());
            throw new NoRunException(BG_TIPS);
        }
    }

    @Action("使用暗器<.{0,}=>name>")
    public String useWea(@Param("name") String name, Group group, User qq) {
        String sss = gameWeaService.useAq(name, qq.getId());
        return sss;
    }

    @Action("暗器背包")
    public String aqBgs(User qq, Group group) {
        String str = gameWeaService.aqBgs(qq.getId());
        return str;
    }

    @Action("暗器制作表")
    public String listAq(User qq, Group group) {
        String str = gameWeaService.aqList();
        return str;
    }

    @Action("暗器菜单")
    public String aqMenu(Group group, User qq) {
        String str = gameWeaService.aqMeun();
        return str;
    }

    @Action("制作暗器<.{1,}=>name>")
    public String makeAq(User qq, @Param("name") String name, Group group) {
        Integer id = GameDataBase.NAME_2_ID_MAPS.get(name.trim());
        if (id == null || !(isAnq(id))) {
            return "系统找不到=>" + name;
        }
        String str = gameWeaService.makeAq(qq.getId(), id);
        return str;
    }

    public boolean isAnq(int id) {
        return (id > 1000 && id < 1200);
    }

    @Action("分解<.+=>name>")
    public String decomposition(User user, @Param("name") String name) {
        Integer id = GameDataBase.NAME_2_ID_MAPS.get(name.trim());
        if (id == null) {
            return "系统找不到=>" + name;
        }
        if (!(isAnq(id))) {
            return name + "不可分解";
        }
        return gameWeaService.decomposition(user.getId(), id);
    }
}
