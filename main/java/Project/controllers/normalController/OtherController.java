package Project.controllers.normalController;


import Project.commons.SpGroup;
import Project.utils.VelocityUtils;
import io.github.kloping.MySpringTool.annotations.*;
import io.github.kloping.MySpringTool.exceptions.NoRunException;
import io.github.kloping.mirai.MiraiRunnable;

import static Project.commons.rt.ResourceSet.FinalString.CLOSE_STR;
import static Project.commons.rt.ResourceSet.FinalString.OPEN_STR;
import static Project.commons.rt.ResourceSet.FinalValue.NOT_OPEN_NO_RUN_EXCEPTION;
import static Project.controllers.auto.ControllerTool.opened;
import static io.github.kloping.mirai0.Main.BootstarpResource.println;

/**
 * @author github kloping
 */
@Controller
public class OtherController {
    private static final StringBuilder MENU_STR = new StringBuilder();
    private static final long CD = 3000;
    public static String[] E_MENUS = null;
    private static String[] BaseMenuStrings;

    static {
        MENU_STR.append("·管理菜单").append("\r\n");
        MENU_STR.append("·回话菜单").append("\r\n");
        MENU_STR.append("·点歌系统").append("\r\n");
        MENU_STR.append("·娱乐功能").append("\r\n");

        MENU_STR.append("·基本菜单 #游戏基本菜单").append("\r\n");
        MENU_STR.append("·暗器菜单").append("\r\n");
        MENU_STR.append("·魂骨菜单").append("\r\n");
        MENU_STR.append("·魂技菜单").append("\r\n");
        MENU_STR.append("·宗门系统").append("\r\n");
        MENU_STR.append("·交易市场").append("\r\n");
        MENU_STR.append("·活动列表").append("\r\n");

        MENU_STR.append("不用at我,直接说就行了哦");
    }

    static {
        BaseMenuStrings = VelocityUtils.getTemplateToString("menu").split("====");
    }

    static {
        E_MENUS = VelocityUtils.getTemplateToString("yl.menu").split("====");
    }

    @AutoStand
    private ManagerController controller;
    private long cd0 = 0;

    public OtherController() {
        println(this.getClass().getSimpleName() + "构建");
    }

    public static String getPermission(long le) {
        if (le == 0) return "群员";
        if (le == 1) return "管理员";
        if (le == 2) return "群主";
        return "未知";
    }

    @Before
    public void before(@AllMess String mess, SpGroup group) throws NoRunException {
        if (mess.contains(OPEN_STR) || mess.contains(CLOSE_STR)) {
            return;
        }
        if (!opened(group.getId(), this.getClass())) {
            throw NOT_OPEN_NO_RUN_EXCEPTION;
        }
    }

    @Action("菜单")
    public String menu() {
        return MENU_STR.toString();
    }

    @Action("娱乐功能.*?")
    public Object[] m0(@AllMess String m) {
        return E_MENUS;
    }

    @Action("基本菜单")
    public Object[] baseMenu() {
        return BaseMenuStrings;
    }

    @Action("获取<.+=>str>")
    public Object getAllInfo(SpGroup group, @Param("str") String str) {
        long qq = Project.utils.Utils.getAtFromString(str);
        if (qq == -1) throw new NoRunException();
        return MiraiRunnable.getMemberInfo(group.getId(), qq);
    }
}