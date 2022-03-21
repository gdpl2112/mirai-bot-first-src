package Project.controllers.gameControllers.shoperController;


import Project.dataBases.GameDataBase;
import Project.interfaces.Iservice.IGameUseObjService;
import io.github.kloping.MySpringTool.annotations.*;
import io.github.kloping.MySpringTool.exceptions.NoRunException;
import io.github.kloping.mirai0.commons.Group;
import io.github.kloping.mirai0.commons.User;
import io.github.kloping.mirai0.Main.ITools.MessageTools;
import io.github.kloping.mirai0.unitls.Tools.Tool;

import java.io.File;

import static Project.controllers.auto.ControllerTool.opened;
import static Project.controllers.normalController.ScoreController.longs;
import static Project.services.impl.GameUseObjServiceImpl.maxSle;
import static io.github.kloping.mirai0.Main.Resource.println;
import static io.github.kloping.mirai0.unitls.drawers.Drawer.getImageFromStrings;

/**
 * @author github-kloping
 */
@Controller
public class GameObjController {
    public GameObjController() {
        println(this.getClass().getSimpleName() + "构建");
    }

    @AutoStand
    IGameUseObjService gameUseObiService;

    @Before
    public void before(Group group, User qq) throws NoRunException {
        if (!opened(group.getId(), this.getClass())) {
            throw new NoRunException("未开启");
        }
    }

    @Action("使用<.{1,}=>str>")
    public Object use(User qq, @Param("str") String str, Group g) {
        try {
            String what = str.replaceAll(",", "").replaceAll("个", "");
            Integer num = null;
            try {
                num = Integer.valueOf(Tool.findNumberFromString(what));
                what = what.replace(num + "", "");
            } catch (Exception e) {

            }
            String shopName = what.replace("使用", "").trim();
            Integer id = GameDataBase.NAME_2_ID_MAPS.get(shopName);
            String sss = null;
            if (num == null || num.intValue() == 1) {
                sss = gameUseObiService.useObj(qq.getId(), id);
            } else {
                sss = gameUseObiService.useObj(qq.getId(), id, num);
            }
            return sss;
        } catch (Exception e) {
            return "未发现相关物品或使用失败#" + str;
        }
    }

    @Action("说明<.{1,}=>str>")
    public Object intro(User qq, @Param("str") String str, Group group) {
        try {
            String what = str;
            what = what.replace("说明", "");
            Integer id = GameDataBase.NAME_2_ID_MAPS.get(what.trim());
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
    public Object shop(Group group) {
        if (upShopPath.isEmpty() || !new File(upShopPath).exists())
            upShopPath = getImageFromStrings(GameDataBase.getShop());
        return (upShopPath + "\r\n用=>出售=>来出售物品\r\n回收价为原价值的1/3但最高不会超过" + maxSle);
    }

    @Action("购买<.{1,}=>name>")
    public Object buy(User qq, @Param("name") String name, @AllMess String mess, Group group) {
        try {
            String what = name.replaceAll(",", "").replaceAll("个", "");
            Integer num = null;
            try {
                num = Integer.valueOf(Tool.findNumberFromString(what));
                what = what.replaceFirst(num + "", "");
            } catch (Exception e) {
            }
            String shopName = what.trim();
            Integer id = GameDataBase.NAME_2_ID_MAPS.get(shopName);
            String sss = null;
            if (num == null || num.intValue() == 1)
                sss = gameUseObiService.buyObj(qq.getId(), id);
            else
                sss = gameUseObiService.buyObj(qq.getId(), id, num);
            return sss;
        } catch (Exception e) {
            e.printStackTrace();
            return "未找到相关物品";
        }
    }

    @Action(value = "物品转让<.{1,}=>name>", otherName = {"转让物品<.{1,}=>name>", "转让<.{1,}=>name>"})
    public String transfer(User qq, @Param("name") String name, @AllMess String message) {
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
            } catch (Exception e) {
                num = null;
            }
            Integer id = GameDataBase.NAME_2_ID_MAPS.get(name);
            String s;
            if (num == null || num.intValue() == 1)
                s = gameUseObiService.objTo(qq.getId(), id, whos);
            else
                s = gameUseObiService.objTo(qq.getId(), id, whos, num);
            return s;
        } catch (Exception e) {
            return "未找到相关物品";
        }
    }


    @Action("出售<.{1,}=>name>")
    public Object sle(User qq, @Param("name") String name, Group group) {
        try {
            String what = name.trim().replaceAll(",", "").replaceAll("个", "");
            Integer num = null;
            try {
                num = Integer.valueOf(Tool.findNumberFromString(what));
                what = what.replaceFirst(num + "", "");
            } catch (Exception e) {
                num = null;
            }
            Integer id = GameDataBase.NAME_2_ID_MAPS.get(what);
            if (id == null) return "未知物品";
            String mess = "";
            if (num == null || num.intValue() == 1) mess = gameUseObiService.sleObj(qq.getId(), id);
            else mess = gameUseObiService.sleObj(qq.getId(), id, num);
            return mess;
        } catch (Exception e) {
            e.printStackTrace();
            return "未找到相关物品";
        }
    }
}