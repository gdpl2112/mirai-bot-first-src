package Project.controllers.normalController;


import Project.aSpring.SpringBootResource;
import Project.commons.SpGroup;
import Project.dataBases.DataBase;
import Project.interfaces.Iservice.IOtherService;
import Project.utils.Tools.Tool;
import Project.utils.VelocityUtils;
import io.github.kloping.MySpringTool.annotations.*;
import io.github.kloping.MySpringTool.exceptions.NoRunException;
import io.github.kloping.mirai.BotInstance;
import io.github.kloping.mirai.MiraiRunnable;

import static Project.commons.rt.ResourceSet.FinalString.*;
import static Project.commons.rt.ResourceSet.FinalValue.NOT_OPEN_NO_RUN_EXCEPTION;
import static Project.controllers.auto.ControllerTool.opened;
import static Project.controllers.plugins.PointSongController.sing;
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
    IOtherService otherService;
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
    public String m0(@AllMess String m) {
        Integer i1 = Tool.INSTANCE.getInteagerFromStr(m);
        int n = 1;
        n = i1 == null ? n : i1;
        n = n > E_MENUS.length ? 1 : n;
        return E_MENUS[n - 1].trim() + "\r\n##当前第" + n + "页,共" + E_MENUS.length + "页";
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

    @Action("金魂币消费记录")
    public String m0(long q) {
        return "点击=>" + String.format(SpringBootResource.address + "/record.html?qid=" + q);
    }

    @Action("\\[@me]<.{1,}=>str>")
    public Object atMe(long qq, SpGroup group, @Param("str") String str) {
        if (str.startsWith(SPEAK_STR)) {
            BotInstance.getInstance().speak(str.substring(1), group);
            return null;
        } else if (str.startsWith(SING_STR)) {
            sing(str.substring(1), group);
            return null;
        } else {
            if (OPEN_STR.equals(str)) {
                if (!DataBase.isFather(qq, group.getId())) {
                    return null;
                }
                return controller.open(group);
            } else if (CLOSE_STR.equals(str)) {
                if (!DataBase.isFather(qq, group.getId())) {
                    return null;
                }
                return controller.close(group);
            } else if (DataBase.canSpeak(group.getId())) {
                if (!Tool.INSTANCE.isIlleg(str)) {
                    if (cd0 < System.currentTimeMillis()) {
                        cd0 = System.currentTimeMillis() + CD;
                        String result = otherService.talk(str);
                        return result;
                    }
                }
            }
        }
        throw new NoRunException();
    }
}