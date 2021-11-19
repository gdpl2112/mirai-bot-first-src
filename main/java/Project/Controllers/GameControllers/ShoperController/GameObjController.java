package Project.Controllers.GameControllers.ShoperController;


import Entitys.Group;
import Entitys.User;
import Project.DataBases.GameDataBase;
import Project.Services.IServer.IGameUseObjService;
import Project.Tools.Tool;
import io.github.kloping.Mirai.Main.ITools.MessageTools;
import io.github.kloping.MySpringTool.annotations.*;
import io.github.kloping.MySpringTool.exceptions.NoRunException;
import net.mamoe.mirai.message.data.PlainText;

import static Project.Controllers.ControllerTool.CanGroup;
import static Project.Controllers.NormalController.ScoreController.longs;
import static Project.Tools.Drawer.getImageFromStrings;
import static io.github.kloping.Mirai.Main.Resource.Switch.AllK;
import static io.github.kloping.Mirai.Main.Resource.println;

@Controller
public class GameObjController {
    public GameObjController() {
        println(this.getClass().getSimpleName() + "构建");
    }

    @AutoStand
    IGameUseObjService gameUseObiService;

    @Before
    public void before(Group group, User qq) throws NoRunException {
        if (!AllK)
            throw new NoRunException();

        if (!CanGroup(group.getId())) {
            throw new NoRunException();
        }
    }

    @Action("使用<.{1,}=>str>")
    public Object Use(User qq, @Param("str") String str, Group g) {
        try {
            String what = str.replaceAll(",", "").replaceAll("个", "");
            Integer num = null;
            try {
                num = Integer.valueOf(Tool.findNumberFromString(what));
                what = what.replace(num + "", "");
            } catch (Exception e) {

            }
            String shopName = what.replace("使用", "").trim();
            Integer id = GameDataBase.Name2idMaps.get(shopName);
            String sss = null;
            if (num == null)
                sss = gameUseObiService.useObj(qq.getId(), id);
            else
                sss = gameUseObiService.useObj(qq.getId(), id, num);
            return sss;
        } catch (Exception e) {
            e.printStackTrace();
            return "未发现相关物品或使用失败";
        }
    }

    @Action("说明<.{1,}=>str>")
    public Object Intro(User qq, @Param("str") String str, Group group) {
        try {
            String what = str;
            what = what.replace("说明", "");
            Integer id = GameDataBase.Name2idMaps.get(what.trim());
            if (id == null) {
                return "未找到相关物品";
            }
            return gameUseObiService.getIntro(id);
        } catch (Exception e) {
            e.printStackTrace();
            return "未发现相关物";
        }
    }

    private String upShopPath = "";

    @Action(value = "商城", otherName = {"商店", "商场"})
    public Object Shop(Group group) {
        if (upShopPath.isEmpty()) {
            upShopPath = getImageFromStrings(1, GameDataBase.getShop());
        }
        return (upShopPath + "\r\n用=>出售=>来出售物品\r\n回收价为原价值的1/3但最高不会超过3000");
    }

    @Action("购买<.{1,}=>name>")
    public Object Buy(User qq, @Param("name") String name, @AllMess String mess, Group group) {
        try {
            String what = name.replaceAll(",", "").replaceAll("个", "");
            Integer num = null;
            try {
                num = Integer.valueOf(Tool.findNumberFromString(what));
                what = what.replaceFirst(num + "", "");
            } catch (Exception e) {
            }
            String shopName = what.trim();
            Integer id = GameDataBase.Name2idMaps.get(shopName);
            String sss = null;
            if (num == null)
                sss = gameUseObiService.BuyObj(qq.getId(), id);
            else
                sss = gameUseObiService.BuyObj(qq.getId(), id, num);
            return sss;
        } catch (Exception e) {
            e.printStackTrace();
            return "未找到相关物品";
        }
    }

    @Action(value = "物品转让<.{1,}=>name>", otherName = "转让物品<.{1,}=>name>")
    public String Transfer(User qq, @Param("name") String name, @AllMess String message) {
        try {
            if (longs.contains(qq.getId())) return "Can't";
            long whos = MessageTools.getAtFromString(message);
            if (whos == -1)
                return "转给谁?";
            if (!GameDataBase.exist(whos)) return "该玩家尚未注册";
            name = name.replace("[@" + whos + "]", "").replace("[@me]", "");
            Integer num = null;
            try {
                num = Integer.valueOf(Tool.findNumberFromString(name));
                name = name.replaceFirst(num + "", "").replaceAll(",", "").replaceAll("个", "");
                ;
            } catch (Exception e) {
                num = null;
            }
            Integer id = GameDataBase.Name2idMaps.get(name);
            String s;
            if (num == null)
                s = gameUseObiService.ObjTo(qq.getId(), id, whos);
            else
                s = gameUseObiService.ObjTo(qq.getId(), id, whos, num);
            return s;
        } catch (Exception e) {
            return "未找到相关物品";
        }
    }


    @Action("出售<.{1,}=>name>")
    public Object Sle(User qq, @Param("name") String name, Group group) {
        try {
            String what = name.trim().replaceAll(",", "").replaceAll("个", "");
            Integer num = null;
            try {
                num = Integer.valueOf(Tool.findNumberFromString(what));
                what = what.replaceFirst(num + "", "");
            } catch (Exception e) {
                num = null;
            }
            Integer id = GameDataBase.Name2idMaps.get(what);
            if (id == null) {
                return new PlainText("商城中未发现此物品");
            }
            String mess = "";
            if (num == null)
                mess = gameUseObiService.SleObj(qq.getId(), id);
            else
                mess = gameUseObiService.SleObj(qq.getId(), id, num);
            return mess;
        } catch (Exception e) {
            e.printStackTrace();
            return "未找到相关物品";
        }
    }
}