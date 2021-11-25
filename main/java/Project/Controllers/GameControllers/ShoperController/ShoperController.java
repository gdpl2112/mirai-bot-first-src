package Project.Controllers.GameControllers.ShoperController;


import Entitys.Group;
import Entitys.User;
import Project.DataBases.GameDataBase;
import Project.Services.Iservice.IShoperService;
import Project.Tools.Tool;
import io.github.kloping.MySpringTool.annotations.*;
import io.github.kloping.MySpringTool.exceptions.NoRunException;

import static Project.Controllers.ControllerTool.CanGroup;
import static Project.Controllers.NormalController.ScoreController.longs;
import static io.github.kloping.Mirai.Main.Resource.Switch.AllK;
import static io.github.kloping.Mirai.Main.Resource.println;

@Controller
public class ShoperController {
    public ShoperController() {
        println(this.getClass().getSimpleName() + "构建");
    }

    @AutoStand
    IShoperService shoperService;

    @Before
    public void before(Entitys.Group group, Entitys.User qq) throws NoRunException {
        if (!AllK)
            throw new NoRunException();
        if (!CanGroup(group.getId())) {
            throw new NoRunException();
        }
    }

    @Action("交易市场")
    public String AllInfo(Group group) {
        return shoperService.AllInfo(group);
    }

    @Action("市场上架.+")
    public String UpItem(User qq, @AllMess String mess) {
        if (longs.contains(qq.getId())) {
            return "您不能上架物品";
        }
        Long[] ll = getNumAndPrice(mess);
        if (ll == null)
            return "格式错误 (市场上架 物品名字 (几)个 (多少金魂币))";
        String name = mess.replace("市场上架", "")
                .replace(ll[0] + "个", "")
                .replace(ll[1] + "", "");
        Integer id = GameDataBase.Name2idMaps.get(name);
        if (id == null)
            return "未发现相关物品";
        return shoperService.UpItem(qq.getId(), id, ll[0], ll[1]);
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

    @Action("市场下架<\\d+=>num>")
    public String DownItem(@Param("num") String num, User qq) {
        return shoperService.DownItem(qq.getId(), Integer.parseInt(num));
    }

    @Action("市场购买<\\d+=>ids>")
    public String getItem(@Param("ids") String ids, User qq) {
        if (longs.contains(qq.getId())) {
            return "您不能购买物品";
        }
        return shoperService.Buy(qq.getId(), Integer.valueOf(ids));
    }

    @Action("市场说明<\\d+=>ids>")
    public String IntroItem(@Param("ids") String ids, User qq, Group group) {
        return shoperService.Intro(qq.getId(), Integer.valueOf(ids), group);
    }
}
