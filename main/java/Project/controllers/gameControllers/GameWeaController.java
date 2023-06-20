package Project.controllers.gameControllers;

import Project.commons.SpGroup;
import Project.commons.SpUser;
import Project.dataBases.GameDataBase;
import Project.interfaces.Iservice.IGameWeaService;
import io.github.kloping.MySpringTool.annotations.*;
import io.github.kloping.MySpringTool.exceptions.NoRunException;
import io.github.kloping.mirai0.Main.iutils.MessageUtils;
import Project.utils.Tools.Tool;

import static Project.commons.rt.ResourceSet.FinalNormalString.BG_TIPS;
import static Project.commons.rt.ResourceSet.FinalNormalString.EMPTY_STR;
import static Project.commons.rt.ResourceSet.FinalValue.NOT_OPEN_NO_RUN_EXCEPTION;
import static Project.controllers.auto.ControllerTool.opened;
import static Project.dataBases.GameDataBase.getInfo;
import static io.github.kloping.mirai0.Main.BootstarpResource.println;

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
    public void before(SpGroup group, SpUser qq) throws NoRunException {
        if (!opened(group.getId(), this.getClass())) {
            throw NOT_OPEN_NO_RUN_EXCEPTION;
        }
        if (getInfo(qq.getId()).isBg()) {
            MessageUtils.INSTANCE.sendMessageInGroupWithAt(BG_TIPS, group.getId(), qq.getId());
            throw new NoRunException(BG_TIPS);
        }
    }

    @Action("使用暗器<.{0,}=>name>")
    public String useWea(@Param("name") String name, SpGroup group, SpUser qq) {
        String sss = gameWeaService.useAq(name, qq.getId());
        return sss;
    }

    @Action("武器背包")
    public String aqBgs(SpUser qq, SpGroup group) {
        String str = gameWeaService.aqBgs(qq.getId());
        return str;
    }

    @Action("暗器制作表")
    public String listAq(SpUser qq, SpGroup group) {
        String str = gameWeaService.aqList();
        return str;
    }

    @Action("暗器菜单")
    public String aqMenu(SpGroup group, SpUser qq) {
        return gameWeaService.aqMeun();
    }

    @Action("制作暗器<.{1,}=>name>")
    public String makeAq(SpUser qq, @Param("name") String name, SpGroup group) {
        Integer id = GameDataBase.NAME_2_ID_MAPS.get(name.trim());
        if (id == null || !(isAnq(id))) return "系统找不到=>" + name;
        String str = gameWeaService.makeAq(qq.getId(), id);
        return str;
    }

    public boolean isAnq(int id) {
        return (id > 1000 && id < 1200);
    }

    @Action("分解<.+=>name>")
    public String decomposition(SpUser user, @Param("name") String name) {
        Integer num = Integer.valueOf(Tool.INSTANCE.findNumberFromString(name, "1"));
        name = name.replace(num.toString(), EMPTY_STR);
        Integer id = GameDataBase.NAME_2_ID_MAPS.get(name.trim());
        if (id == null) return "系统找不到=>" + name;
        if (!(isAnq(id)) && id != 120) return name + "不可分解";
        num = num > 15 ? 15 : num;
        return gameWeaService.decomposition(user.getId(), id, num);
    }
}
