package Project.controllers.normalController;


import Project.aSpring.SpringBootResource;
import Project.dataBases.DataBase;
import Project.utils.VelocityUtils;
import Project.interfaces.Iservice.IOtherService;
import io.github.kloping.MySpringTool.annotations.*;
import io.github.kloping.MySpringTool.exceptions.NoRunException;
import io.github.kloping.mirai0.Main.iutils.MemberUtils;
import io.github.kloping.mirai0.Main.iutils.MessageUtils;
import io.github.kloping.mirai0.commons.SpGroup;
import io.github.kloping.mirai0.unitls.Tools.Tool;
import net.mamoe.mirai.contact.NormalMember;

import static Project.controllers.auto.ControllerTool.opened;
import static Project.controllers.plugins.PointSongController.sing;
import static io.github.kloping.mirai0.Main.BootstarpResource.BOT;
import static io.github.kloping.mirai0.Main.BootstarpResource.println;
import static io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet.FinalString.*;
import static io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet.FinalValue.NOT_OPEN_NO_RUN_EXCEPTION;

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
        MENU_STR.append("1.基本菜单").append("\r\n");
        MENU_STR.append("2.暗器菜单").append("\r\n");
        MENU_STR.append("3.魂骨菜单").append("\r\n");
        MENU_STR.append("4.魂技菜单").append("\r\n");
        MENU_STR.append("5.回话菜单").append("\r\n");
        MENU_STR.append("6.宗门系统").append("\r\n");
        MENU_STR.append("7.交易市场").append("\r\n");
        MENU_STR.append("8.点歌系统").append("\r\n");
        MENU_STR.append("9.娱乐功能").append("\r\n");
        MENU_STR.append("-1.开始会话 #在线运行代码").append("\r\n");
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
    public String baseMenu() {
        return BaseMenuStrings[0].trim() + "\r\n 当前第1页,共" + BaseMenuStrings.length + "页#基本菜单2 以查看第二页";
    }

    @Action("基本菜单<\\d=>num>")
    public String baseMenuN(@Param("num") String num) {
        int n = 1;
        try {
            n = Integer.parseInt(num);
        } catch (Exception e) {
        }
        n = n > BaseMenuStrings.length ? 1 : n;
        return BaseMenuStrings[n - 1].trim() + "\r\n 当前第" + n + "页,共" + BaseMenuStrings.length + "页";
    }

    @Action("获取<.+=>str>")
    public Object getAllInfo(SpGroup group, @Param("str") String str) {
        long qq = MessageUtils.INSTANCE.getAtFromString(str);
        if (qq == -1) throw new NoRunException();
        NormalMember member = BOT.getGroup(group.getId()).get(qq);
        StringBuilder sb = new StringBuilder();
        sb.append("QQ:").append(qq).append("\r\n");
        sb.append("身份:").append(getPermission(member.getPermission().getLevel())).append("\r\n");
        sb.append("群内名:").append(MemberUtils.getNameFromGroup(qq, group)).append("\r\n");
        sb.append("QQ名:").append(member.getNick()).append("\r\n");
        sb.append("加入时间:").append(Tool.INSTANCE.getTimeYMdhms(member.getJoinTimestamp() * 1000L)).append("\r\n");
        sb.append("最后发言:").append(Tool.INSTANCE.getTimeYMdhms(member.getLastSpeakTimestamp() * 1000L)).append("\r\n");
        sb.append("头衔:").append(member.getSpecialTitle()).append("\r\n");
        sb.append("禁言时长:").append(member.getMuteTimeRemaining()).append("\r\n");
        sb.append("头像链接:").append(member.getAvatarUrl()).append("\r\n");
        return sb.toString();
    }

    @Action("金魂币消费记录")
    public String m0(long q) {
        return "点击=>" + String.format(SpringBootResource.address + "/record.html?qid=" + q);
    }

    @Action("\\[@me]<.{1,}=>str>")
    public Object atMe(long qq, SpGroup group, @Param("str") String str) {
        if (str.startsWith(SPEAK_STR)) {
            MessageUtils.INSTANCE.speak(str.substring(1), group);
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

    //    @Action("生成国旗渐变头像<.+=>par>")
//    public String par(@AllMess String all) {
//        try {
//            int i1 = all.indexOf("[");
//            int i2 = all.indexOf("]");
//            String url = all.substring(i1 + 5, i2);
//            String endU = Image.queryUrl(Image.fromId(url));
//            url = Drawer.bundler_0(endU);
//            return  Tool.tool.pathToImg(url);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "生成失败";
//        }
//    }
}