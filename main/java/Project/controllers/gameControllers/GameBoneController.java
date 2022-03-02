package Project.controllers.gameControllers;


import Project.dataBases.SourceDataBase;
import io.github.kloping.mirai0.Entitys.Group;
import io.github.kloping.mirai0.Entitys.User;
import Project.interfaces.Iservice.IGameBoneService;
import io.github.kloping.mirai0.Entitys.gameEntitys.SoulBone;
import io.github.kloping.mirai0.unitls.Tools.Tool;
import io.github.kloping.MySpringTool.annotations.*;
import io.github.kloping.MySpringTool.exceptions.NoRunException;

import java.util.ArrayList;
import java.util.List;

import static Project.controllers.auto.ControllerTool.opened;
import static Project.dataBases.GameDataBase.*;
import static Project.dataBases.SourceDataBase.getImgPathById;
import static io.github.kloping.mirai0.unitls.drawers.Drawer.getImageFromStrings;
import static io.github.kloping.mirai0.Main.Resource.println;

/**
 * @author github-kloping
 */
@Controller
public class GameBoneController {
    public GameBoneController() {
        println(this.getClass().getSimpleName() + "构建");
    }

    private static List<String> listFx = new ArrayList<>();

    static {
        listFx.add("我的属性");
        listFx.add("我的魂骨");
    }

    @AutoStand
    IGameBoneService gameBoneService;

    @Before
    public void before(User qq, Group group, @AllMess String str) throws NoRunException {
        if (!opened(group.getId(), this.getClass())) {
            throw new NoRunException("未开启");
        }
        if (getInfo(qq.getId()).getHp() <= 0) {
            if (Tool.EveListStartWith(listFx, str) == -1) {
                throw new NoRunException();
            }
        }
    }

    @Action("魂骨菜单")
    public String boneMenu(long qq, Group g) {
        return getImageFromStrings(
                "吸收魂骨 (魂骨名)",
                "我的属性 #查看属性",
                "我的魂骨 #展示魂骨",
                "卸掉魂骨 (卸掉之后无状态",
                "=====>经验清零)",
                "更多功能开发中..."
        );
    }

    @Action(value = "我的属性", otherName = "属性信息")
    public String myAttribute(long qq, Group g) {
        return gameBoneService.getInfoAttributes(qq);
    }

    @Action("我的魂骨")
    public String myBones(long qq, Group g) {
        List<SoulBone> list = gameBoneService.getSoulBones(qq);
        StringBuilder sb = new StringBuilder();
        for (SoulBone soulBone : list) {
            Integer id = soulBone.getOid();
            sb.append(getNameById(id)).append(SourceDataBase.getImgPathById(id));
        }
        return sb.toString().isEmpty() ? "没有魂骨!" : sb.toString();
    }

    @Action("吸收魂骨<.{1,}=>name>")
    public String parseBone(@Param("name") String name, long qq, Group g) {
        int id = 0;
        try {
            id = NAME_2_ID_MAPS.get(name);
            if (!(id > 1500 && id < 1600))
                return new StringBuilder().append("系统没有找到==》").append(name).toString();
        } catch (Exception e) {
            return new StringBuilder().append("错误的==》").append(name).toString();
        }
        String str = gameBoneService.parseBone(id, qq);
        return str;
    }

    @Action("卸掉魂骨<.{1,}=>name>")
    public String unParseBone(@Param("name") String name, long qq, Group g) {
        int id = 0;
        try {
            id = NAME_2_ID_MAPS.get(name);
            if (!(id > 1500 && id < 1600))
                return new StringBuilder().append("系统没有找到==》").append(name).toString();
        } catch (Exception e) {
            return new StringBuilder().append("错误的==》").append(name).toString();
        }
        String str = gameBoneService.unInstallBone(id, qq);
        return str;
    }
}
