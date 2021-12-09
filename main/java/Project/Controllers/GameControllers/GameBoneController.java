package Project.Controllers.GameControllers;


import Entitys.Group;
import Entitys.User;
import Project.Services.Iservice.IGameBoneService;
import Project.Tools.Tool;
import io.github.kloping.MySpringTool.annotations.*;
import io.github.kloping.MySpringTool.exceptions.NoRunException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static Project.Controllers.ControllerTool.CanGroup;
import static Project.DataBases.GameDataBase.*;
import static Project.drawers.Drawer.getImageFromStrings;
import static Project.Tools.GameTool.upDateMan;
import static io.github.kloping.Mirai.Main.Resource.Switch.AllK;
import static io.github.kloping.Mirai.Main.Resource.println;

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
        if (!AllK)
            throw new NoRunException();

        if (!CanGroup(group.getId())) {
            throw new NoRunException();
        }
        upDateMan(qq.getId(), getInfo(qq.getId()).getLevel());
        if (getInfo(qq.getId()).getHp() <= 0) {
            if (Tool.EveListStartWith(listFx, str) == -1) {
//               group.sendString(new StringChainBuilder().append(new At(qq.getId())).append("\n 无状态!").build());
                throw new NoRunException();
            }
        }
    }

    @Action(value = "我的属性", otherName = "属性信息")
    public String MyAttribute(long qq, Group g) {
        return gameBoneService.getInfoAttributes(qq);
    }

    @Action("我的魂骨")
    public String MyBones(long qq, Group g) {
        Map<Integer, Map.Entry<String, Integer>> map = gameBoneService.getAttributeMap(qq, true);
        StringBuilder sb = new StringBuilder();
        for (Integer id : map.keySet()) {
            sb.append(getNameById(id)).append(getImgById(id));
        }
        return sb.toString().isEmpty() ? "没有魂骨!" : sb.toString();
    }

    @Action("魂骨菜单")
    public String BoneMenu(long qq, Group g) {
        return getImageFromStrings(
                "吸收魂骨 (魂骨名)",
                "我的属性 #查看属性",
                "我的魂骨 #展示魂骨",
                "卸掉魂骨 (卸掉之后无状态",
                "=====>经验清零)",
                "更多功能开发中..."
        );
    }

    @Action("吸收魂骨<.{1,}=>name>")
    public String ParseBone(@Param("name") String name, long qq, Group g) {
        int id = 0;
        try {
            id = Name2idMaps.get(name);
            if (!(id > 1500 && id < 1600))
                return new StringBuilder().append("系统没有找到==》").append(name).toString();
        } catch (Exception e) {
            return new StringBuilder().append("错误的==》").append(name).toString();
        }
        String str = gameBoneService.ParseBone(id, qq);
        return str;
    }

    @Action("卸掉魂骨<.{1,}=>name>")
    public String UnParseBone(@Param("name") String name, long qq, Group g) {
        int id = 0;
        try {
            id = Name2idMaps.get(name);
            if (!(id > 1500 && id < 1600))
                return new StringBuilder().append("系统没有找到==》").append(name).toString();
        } catch (Exception e) {
            return new StringBuilder().append("错误的==》").append(name).toString();
        }
        String str = gameBoneService.UnInstallBone(id, qq);
        return str;
    }
}
