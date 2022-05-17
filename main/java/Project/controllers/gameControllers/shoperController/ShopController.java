package Project.controllers.gameControllers.shoperController;


import Project.dataBases.GameDataBase;
import Project.interfaces.Iservice.IShoperService;
import io.github.kloping.MySpringTool.annotations.*;
import io.github.kloping.MySpringTool.exceptions.NoRunException;
import io.github.kloping.mirai0.commons.Group;
import io.github.kloping.mirai0.commons.User;
import io.github.kloping.mirai0.unitls.Tools.Tool;

import static Project.controllers.auto.ControllerTool.opened;
import static Project.controllers.normalController.ScoreController.longs;
import static io.github.kloping.mirai0.Main.Resource.println;
import static io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet.FinalValue.NOT_OPEN_NO_RUN_EXCEPTION;

/**
 * @author github-kloping
 */
@Controller
public class ShopController {
    @AutoStand
    IShoperService shoperService;

    public ShopController() {
        println(this.getClass().getSimpleName() + "构建");
    }

    private static Long[] getNumAndPrice(String str) {
        try {
            String[] ss = str.split("个");
            if (ss.length > 1) {
                String numStr = Tool.findNumberFromString(ss[0]);
                if (!numStr.isEmpty()) {
                    Long num = Long.valueOf(numStr + "");
                    str = str.replaceFirst(numStr, "");
                    String priceStr = Tool.findNumberFromString(str);
                    if (!priceStr.isEmpty()) {
                        Long price = Long.valueOf(priceStr);
                        return new Long[]{num, price};
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Before
    public void before(io.github.kloping.mirai0.commons.Group group, io.github.kloping.mirai0.commons.User qq) throws NoRunException {
        if (!opened(group.getId(), this.getClass())) {
            throw NOT_OPEN_NO_RUN_EXCEPTION;
        }
    }

    @Action("交易市场")
    public String allInfo(Group group) {
        return shoperService.allInfo(group);
    }

    @Action("市场上架.+")
    public String upItem(User qq, @AllMess String mess) {
        if (longs.contains(qq.getId())) {
            return "您不能上架物品";
        }
        Long[] ll = getNumAndPrice(mess);
        if (ll == null)
            return "格式错误 (市场上架 物品名字 (几)个 (多少金魂币))";
        String name = mess.replace("市场上架", "")
                .replace(ll[0] + "个", "")
                .replace(ll[1] + "", "");
        Integer id = GameDataBase.NAME_2_ID_MAPS.get(name);
        if (id == null)
            return "未发现相关物品";
        return shoperService.upItem(qq.getId(), id, ll[0], ll[1]);
    }

    @Action("市场下架<\\d+=>num>")
    public String downItem(@Param("num") String num, User qq) {
        return shoperService.downItem(qq.getId(), Integer.parseInt(num));
    }

    @Action("市场购买<\\d+=>ids>")
    public String getItem(@Param("ids") String ids, User qq) {
        if (longs.contains(qq.getId())) {
            return "您不能购买物品";
        }
        return shoperService.buy(qq.getId(), Integer.valueOf(ids));
    }

    @Action("市场说明<\\d+=>ids>")
    public String introItem(@Param("ids") String ids, User qq, Group group) {
        return shoperService.intro(qq.getId(), Integer.valueOf(ids), group);
    }
}
