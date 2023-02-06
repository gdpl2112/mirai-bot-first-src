package Project.controllers.gameControllers.shoperController;


import Project.dataBases.GameDataBase;
import Project.dataBases.SourceDataBase;
import Project.e0.VelocityUtils;
import Project.interfaces.Iservice.IGameUseObjService;
import Project.interfaces.Iservice.IGameWeaService;
import io.github.kloping.MySpringTool.annotations.*;
import io.github.kloping.MySpringTool.exceptions.NoRunException;
import io.github.kloping.mirai0.Main.ITools.MessageTools;
import io.github.kloping.mirai0.commons.Group;
import io.github.kloping.mirai0.commons.User;
import io.github.kloping.mirai0.unitls.Tools.Tool;

import java.io.File;

import static Project.controllers.auto.ControllerSource.challengeDetailService;
import static Project.controllers.auto.ControllerTool.opened;
import static Project.controllers.normalController.ScoreController.longs;
import static Project.services.impl.GameUseObjServiceImpl.maxSle;
import static io.github.kloping.mirai0.Main.Resource.println;
import static io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet.FinalFormat.AT_FORMAT;
import static io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet.FinalNormalString.EMPTY_STR;
import static io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet.FinalString.*;
import static io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet.FinalValue.NOT_OPEN_NO_RUN_EXCEPTION;
import static io.github.kloping.mirai0.unitls.drawers.Drawer.getImageFromStrings;

/**
 * @author github-kloping
 */
@Controller
public class GameObjController {
    @AutoStand
    IGameUseObjService gameUseObiService;
    @AutoStand
    IGameWeaService gameWeaService;

    private String upShopPath = "";

    public GameObjController() {
        println(this.getClass().getSimpleName() + "构建");
    }

    @Before
    public void before(Group group, User qq) throws NoRunException {
        if (!opened(group.getId(), this.getClass())) {
            throw NOT_OPEN_NO_RUN_EXCEPTION;
        }
    }

    @Action("使用<.{1,}=>str>")
    public Object use(User qq, @Param("str") String str, Group g) {
        try {
            String what = str.replaceAll(",", "").replaceAll("个", "")
                    .replaceAll("(\\[@\\d+]|#)", "");
            Integer num = null;
            try {
                num = Integer.valueOf(Tool.tool.findNumberFromString(what));
                what = what.replace(num.toString(), "");
            } catch (Exception e) {
            }
            String shopName = what.replace("使用", "").trim();
            Integer id = GameDataBase.NAME_2_ID_MAPS.get(shopName);
            if (id <= 127 && id >= 124) {
                return SourceDataBase.getImgPathById(id) + gameWeaService.useAq(str, qq.getId());
            }
            String sss = null;
            if (num == null) {
                sss = gameUseObiService.useObj(qq.getId(), id);
            } else {
                if (challengeDetailService.isTemping(qq.getId())) {
                    return CHALLENGE_ING;
                }
                sss = gameUseObiService.useObj(qq.getId(), id, num);
            }
            return sss;
        } catch (Exception e) {
            e.printStackTrace();
            return VelocityUtils.getTemplateToString("not.found.or.use.failed", str);
        }
    }

    @Action("说明<.{1,}=>str>")
    public Object intro(User qq, @Param("str") String str, Group group) {
        try {
            String what = str;
            Integer id = GameDataBase.NAME_2_ID_MAPS.get(what.trim());
            if (id == null) {
                return NOT_FOUND_ABOUT_OBJ;
            }
            return gameUseObiService.getIntro(id);
        } catch (Exception e) {
            e.printStackTrace();
            return NOT_FOUND_ABOUT_OBJ;
        }
    }

    @Action(value = "商城", otherName = {"商店", "商场"})
    public Object shop(Group group) {
        if (upShopPath.isEmpty() || !new File(upShopPath).exists())
            upShopPath = getImageFromStrings(GameDataBase.getShop());
        return VelocityUtils.getTemplateToString("tips.shop.0", upShopPath, maxSle);
    }

    @Action("购买<.{1,}=>name>")
    public Object buy(User qq, @Param("name") String name, @AllMess String mess, Group group) {
        try {
            String what = name.replaceAll(",", "").replaceAll("个", "");
            Integer num = null;
            try {
                num = Integer.valueOf(Tool.tool.findNumberFromString(what));
                what = what.replaceFirst(num + "", "");
            } catch (Exception e) {
            }
            String shopName = what.trim();
            Integer id = GameDataBase.NAME_2_ID_MAPS.get(shopName);
            String sss = null;
            if (num == null || num.intValue() == 1) {
                sss = gameUseObiService.buyObj(qq.getId(), id);
            } else {
                sss = gameUseObiService.buyObj(qq.getId(), id, num);
            }
            return sss;
        } catch (Exception e) {
            e.printStackTrace();
            return NOT_FOUND_ABOUT_OBJ;
        }
    }

    @Action(value = "物品转让<.{1,}=>name>", otherName = {"转让物品<.{1,}=>name>", "转让<.{1,}=>name>"})
    public String transfer(User qq, @Param("name") String name, @AllMess String message, Group group) {
        try {
            if (longs.contains(qq.getId())) return ERR_TIPS;
            long whos = MessageTools.instance.getAtFromString(message);
            if (whos == -1)
                return NOT_FOUND_AT;
            if (!GameDataBase.exist(whos)) return PLAYER_NOT_REGISTERED;
            if (challengeDetailService.isTemping(qq.getId())) return ILLEGAL_OPERATION;
            name = name.replace(String.format(AT_FORMAT, whos), "").replace("[@me]", "");
            Integer num = null;
            try {
                num = Integer.valueOf(Tool.tool.findNumberFromString(name));
                name = name.replaceFirst(String.valueOf(num), "").replaceAll(",", "").replaceAll("个", "");
            } catch (Exception e) {
                num = null;
            }
            Integer id = GameDataBase.NAME_2_ID_MAPS.get(name);
            String s;
            if (challengeDetailService.isTemping(whos)) {
                return CHALLENGE_ING;
            }
            if (num == null || num.intValue() == 1) {
                s = gameUseObiService.objTo(qq.getId(), id, whos);
            } else {
                s = gameUseObiService.objTo(qq.getId(), id, whos, num, group);
            }
            return s;
        } catch (Exception e) {
            return ERR_TIPS;
        }
    }

    @Action("出售<.{1,}=>name>")
    public Object sle(User qq, @Param("name") String name, Group group) {
        try {
            if (challengeDetailService.isTemping(qq.getId())) {
                return CHALLENGE_ING;
            }
            String what = name.trim().replaceAll(",", "").replaceAll("个", "");
            Integer num = null;
            try {
                num = Integer.valueOf(Tool.tool.findNumberFromString(what));
                what = what.replaceFirst(num + "", "");
            } catch (Exception e) {
                num = null;
            }
            Integer id = GameDataBase.NAME_2_ID_MAPS.get(what);
            if (id == null) return NOT_FOUND_ABOUT_OBJ;
            String mess = EMPTY_STR;
            if (num == null || num.intValue() == 1)
                mess = gameUseObiService.sleObj(qq.getId(), id);
            else mess = gameUseObiService.sleObj(qq.getId(), id, num, group);
            return mess;
        } catch (Exception e) {
            e.printStackTrace();
            return NOT_FOUND_ABOUT_OBJ;
        }
    }
}