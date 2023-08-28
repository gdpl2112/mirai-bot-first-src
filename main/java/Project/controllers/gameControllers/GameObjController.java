package Project.controllers.gameControllers;


import Project.commons.SpGroup;
import Project.commons.SpUser;
import Project.dataBases.GameDataBase;
import Project.dataBases.SourceDataBase;
import Project.interfaces.Iservice.IGameUseObjService;
import Project.interfaces.Iservice.IGameWeaService;
import Project.utils.VelocityUtils;
import io.github.kloping.MySpringTool.annotations.*;
import io.github.kloping.MySpringTool.exceptions.NoRunException;
import Project.utils.Tools.Tool;

import java.io.File;

import static Project.commons.rt.ResourceSet.FinalFormat.AT_FORMAT;
import static Project.commons.rt.ResourceSet.FinalNormalString.EMPTY_STR;
import static Project.commons.rt.ResourceSet.FinalString.*;
import static Project.commons.rt.ResourceSet.FinalValue.NOT_OPEN_NO_RUN_EXCEPTION;
import static Project.controllers.auto.ControllerSource.challengeDetailService;
import static Project.controllers.auto.ControllerTool.opened;
import static Project.controllers.normalController.ScoreController.longs;
import static Project.services.impl.GameUseObjServiceImpl.maxSle;
import static io.github.kloping.mirai0.Main.BootstarpResource.println;
import static Project.utils.drawers.Drawer.getImageFromStrings;

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
    public void before(SpGroup group, SpUser qq) throws NoRunException {
        if (!opened(group.getId(), this.getClass())) {
            throw NOT_OPEN_NO_RUN_EXCEPTION;
        }
    }

    @Action("使用<.{1,}=>str>")
    public Object use(SpUser qq, @Param("str") String str, SpGroup g) {
        String shopName = str.replaceAll(",", "").replaceAll("个", "").replaceAll("(\\[@\\d+]|#)", "");
        Integer num = Integer.valueOf(Tool.INSTANCE.findNumberFromString(shopName, 1));
        shopName = shopName.replace(num.toString(), "");
        Integer id = GameDataBase.NAME_2_ID_MAPS.get(shopName);
        if (id == null) return VelocityUtils.getTemplateToString("not.found.or.use.failed", str);
        if (id <= 127 && id >= 124) {
            return SourceDataBase.getImgPathById(id) + gameWeaService.useAq(str, qq.getId());
        }
        String result = ERR_TIPS;
        try {
            if (num == null || num.intValue() == 1) {
                result = gameUseObiService.useObj(qq.getId(), id);
            } else {
                if (challengeDetailService.isTemping(qq.getId())) {
                    return CHALLENGE_ING;
                }
                result = gameUseObiService.useObj(qq.getId(), id, num);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Action("说明<.{1,}=>str>")
    public Object intro(SpUser qq, @Param("str") String str, SpGroup group) {
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
    public Object shop(SpGroup group) {
        if (upShopPath.isEmpty() || !new File(upShopPath).exists())
            upShopPath = getImageFromStrings(GameDataBase.getShop());
        return VelocityUtils.getTemplateToString("tips.shop.0", upShopPath, maxSle);
    }

    @Action("购买<.{1,}=>name>")
    public Object buy(SpUser qq, @Param("name") String name, @AllMess String mess, SpGroup group) {
        try {
            String shopName = name.replaceAll(",", "").replaceAll("个", "");
            Integer num = null;
            num = Integer.valueOf(Tool.INSTANCE.findNumberFromString(shopName, 1));
            shopName = shopName.replaceFirst(num.toString(), "");
            Integer id = GameDataBase.NAME_2_ID_MAPS.get(shopName);
            String result = null;
            if (num == null || num.intValue() == 1) {
                result = gameUseObiService.buyObj(qq.getId(), id);
            } else {
                result = gameUseObiService.buyObj(qq.getId(), id, num);
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return NOT_FOUND_ABOUT_OBJ;
        }
    }

    @Action(value = "物品转让<.{1,}=>name>", otherName = {"转让物品<.{1,}=>name>", "转让<.{1,}=>name>"})
    public String transfer(SpUser qq, @Param("name") String name, @AllMess String message, SpGroup group) {
        try {
            if (longs.contains(qq.getId())) return ERR_TIPS;
            long q2 = Project.utils.Utils.getAtFromString(message);
            if (q2 == -1) return NOT_FOUND_AT;
            if (!GameDataBase.exist(q2)) return PLAYER_NOT_REGISTERED;
            if (challengeDetailService.isTemping(qq.getId())) return ILLEGAL_OPERATION;
            name = name.replace(String.format(AT_FORMAT, q2), "").replace("[@me]", "");
            Integer num = null;
            num = Integer.valueOf(Tool.INSTANCE.findNumberFromString(name, 1));
            name = name.replaceFirst(num.toString(), "").replaceAll(",", "").replaceAll("个", "");
            Integer id = GameDataBase.NAME_2_ID_MAPS.get(name);
            String result;
            if (challengeDetailService.isTemping(q2)) {
                return CHALLENGE_ING;
            }
            if (num == null || num.intValue() == 1) {
                result = gameUseObiService.objTo(qq.getId(), id, q2);
            } else {
                result = gameUseObiService.objTo(qq.getId(), id, q2, num, group);
            }
            return result;
        } catch (Exception e) {
            return ERR_TIPS;
        }
    }

    @Action("出售<.{1,}=>name>")
    public Object sle(SpUser qq, @Param("name") String name, SpGroup group) {
        try {
            if (challengeDetailService.isTemping(qq.getId())) {
                return CHALLENGE_ING;
            }
            name = name.trim().replaceAll(",", "").replaceAll("个", "");
            Integer num = null;
            num = Integer.valueOf(Tool.INSTANCE.findNumberFromString(name, 1));
            name = name.replaceFirst(num.toString(), "");
            Integer id = GameDataBase.NAME_2_ID_MAPS.get(name);
            if (id == null) return NOT_FOUND_ABOUT_OBJ;
            String result = EMPTY_STR;
            if (num == null || num.intValue() == 1) result = gameUseObiService.sleObj(qq.getId(), id);
            else result = gameUseObiService.sleObj(qq.getId(), id, num, group);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return NOT_FOUND_ABOUT_OBJ;
        }
    }
}