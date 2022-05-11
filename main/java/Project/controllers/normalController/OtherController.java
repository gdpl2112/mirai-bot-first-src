package Project.controllers.normalController;


import Project.aSpring.SpringBootResource;
import Project.dataBases.DataBase;
import Project.interfaces.Iservice.IOtherService;
import io.github.kloping.MySpringTool.annotations.*;
import io.github.kloping.MySpringTool.exceptions.NoRunException;
import io.github.kloping.mirai0.Main.ITools.MemberTools;
import io.github.kloping.mirai0.Main.ITools.MessageTools;
import io.github.kloping.mirai0.commons.Group;
import io.github.kloping.mirai0.unitls.Tools.Tool;
import net.mamoe.mirai.contact.NormalMember;

import static Project.controllers.auto.ControllerTool.opened;
import static Project.controllers.plugins.PointSongController.sing;
import static io.github.kloping.mirai0.Main.ITools.MessageTools.speak;
import static io.github.kloping.mirai0.Main.Resource.bot;
import static io.github.kloping.mirai0.Main.Resource.println;
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
        final StringBuilder baseMenuStr = new StringBuilder();
        baseMenuStr.append("修炼\n");
        baseMenuStr.append("信息\n");
        baseMenuStr.append("闭关/取消闭关\n");
        baseMenuStr.append("详细信息 #查看信息\n");
        baseMenuStr.append("升级 #经验足够时\n");
        baseMenuStr.append("觉醒 #2级时用来觉醒武魂\n");
        baseMenuStr.append("转生 #重置信息\n");
        baseMenuStr.append("商城/商店/商场\n");
        baseMenuStr.append("背包/我的背包 #查看背包\n");
        baseMenuStr.append("说明 #见商城)\n");
        baseMenuStr.append("购买 #见商城)\n");
        baseMenuStr.append("使用 #见背包)\n");
        baseMenuStr.append("物品转让(名字)(at)\n");
        baseMenuStr.append("列表\n");
        baseMenuStr.append("进入 #见列表\n");
        baseMenuStr.append("====");
        baseMenuStr.append("请求支援(遇到魂兽时请求)\n");
        baseMenuStr.append("支援<At>\n");
        baseMenuStr.append("探查 #查看当前魂兽信息\n");
        baseMenuStr.append("吸收(十/百/千..)年魂环\n");
        baseMenuStr.append("魂环配置 #展示魂环\n");
        baseMenuStr.append("购买金魂币(值) #2积分1个\n");
        baseMenuStr.append("攻击<At>\n");
        baseMenuStr.append("精神冲击<Any>\n");
        baseMenuStr.append("侦查<At>\n");
        baseMenuStr.append("升级第<几>魂环\n");
        baseMenuStr.append("取名封号<名字>\n");
        baseMenuStr.append("====");
        baseMenuStr.append("选择<攻击/逃跑>\r\n\t#当遇到魂兽时使用\n");
        baseMenuStr.append("换积分<value>\r\n\t#(用金魂币换积分)1金魂币1.5积分(多出500的金魂币才能换)\n");
        baseMenuStr.append("等级排行\n");
        baseMenuStr.append("称号 #(查看所有称号)\n");
        baseMenuStr.append("武魂类型 #查看自己的武魂类型是什么)\n");
        baseMenuStr.append("融合武魂 <At>  # 需要融合戒指\n");
        baseMenuStr.append("魂兽击杀排行\n");
        baseMenuStr.append("关系列表\n");
        baseMenuStr.append("收徒<At>\n");
        baseMenuStr.append("出师\n");
        baseMenuStr.append("出徒\n");
        baseMenuStr.append("====");
        baseMenuStr.append("魂环吸收限制");
        baseMenuStr.append("精神力作用\n");
        baseMenuStr.append("新机制\n");
        baseMenuStr.append("怎么获得名师点\n");
        baseMenuStr.append("====");
        baseMenuStr.append("#任务相关\n");
        baseMenuStr.append("接徒弟任务\n");
        baseMenuStr.append("接每周任务\n");
        baseMenuStr.append("当前任务\n");
        baseMenuStr.append("====");
        baseMenuStr.append("#积分相关\n");
        baseMenuStr.append("签到\n");
        baseMenuStr.append("今日签榜\n");
        baseMenuStr.append("签榜\n");
        baseMenuStr.append("取积分<value>\n");
        baseMenuStr.append("存积分<value>\n");
        baseMenuStr.append("积分转让/转让积分<At><value>\n");
        baseMenuStr.append("积分侦查<At>\n");
        baseMenuStr.append("打工 #赚积分\n");
        baseMenuStr.append("积分查询/查询积分\n");
        baseMenuStr.append("猜拳<石头/剪刀/布><value>\n");
        baseMenuStr.append("抢劫<At>\n");
        baseMenuStr.append("我的发言\n");
        baseMenuStr.append("我的收益\n");
        baseMenuStr.append("====");
        baseMenuStr.append("#管理相关 #需要权限\n");
        baseMenuStr.append("禁言(@xx)(值)(单位,秒,分..)\n");
        baseMenuStr.append("解除禁言(@xx)\n");
        baseMenuStr.append("开启/说话\n");
        baseMenuStr.append("关闭/闭嘴\n");
        baseMenuStr.append("(开启/关闭)闪照破解\n");
        baseMenuStr.append("获取 @xx \n");
        baseMenuStr.append("撤回 <@> <index...> \n");
        String baseMenuString = baseMenuStr.toString();
        BaseMenuStrings = baseMenuString.split("====");
    }

    static {
        final StringBuilder m0 = new StringBuilder();
        m0.append("开始成语接龙\n");
        m0.append("搜图 xx \n");
        m0.append("百度搜图 xx \n");
        m0.append("堆糖搜图 xx \n");
        m0.append("发张 xx \n");
        m0.append("掷骰子\n");
        m0.append("随机头像\n");
        m0.append("随机男头像\n");
        m0.append("随机女头像\n");
        m0.append("随机情侣头像\n");
        m0.append("王者公告\n");
        m0.append("原神公告\n");
        m0.append("捡瓶子/捡漂流瓶\n");
        m0.append("扔瓶子/仍漂流瓶<内容>\n");
        m0.append("王者语音<英雄名><序号>\n");
        m0.append("王者图片<英雄名>\n");
        m0.append("====");
        m0.append("/推<@>\n");
        m0.append("/推[图片]\n");
        m0.append("/玩球<@>\n");
        m0.append("/玩球[图片]\n");
        m0.append("/丢<@>\n");
        m0.append("/丢[图片]\n");
        m0.append("/滚草<At>\n");
        m0.append("/滚草[图片]\n");
        m0.append("/爬<At>\n");
        m0.append("/举牌子<Text>\n");
        m0.append("/赞<At>\n");
        m0.append("====");
        m0.append("解析快手图片<url>\n");
        m0.append("解析抖音图片<url>\n");
        m0.append("QQ信息<qq>\n");
        m0.append("QQ群信息<qq>\n");
        m0.append("/搜图<图片>\n");
        E_MENUS = m0.toString().split("====");
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
    public void before(@AllMess String mess, Group group) throws NoRunException {
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
        Integer i1 = Tool.getInteagerFromStr(m);
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
    public Object getAllInfo(Group group, @Param("str") String str) {
        long qq = MessageTools.getAtFromString(str);
        if (qq == -1) throw new NoRunException();
        NormalMember member = bot.getGroup(group.getId()).get(qq);
        StringBuilder sb = new StringBuilder();
        sb.append("QQ:").append(qq).append("\r\n");
        sb.append("身份:").append(getPermission(member.getPermission().getLevel())).append("\r\n");
        sb.append("群内名:").append(MemberTools.getNameFromGroup(qq, group)).append("\r\n");
        sb.append("QQ名:").append(member.getNick()).append("\r\n");
        sb.append("加入时间:").append(Tool.getTimeYMdhms(member.getJoinTimestamp() * 1000L)).append("\r\n");
        sb.append("最后发言:").append(Tool.getTimeYMdhms(member.getLastSpeakTimestamp() * 1000L)).append("\r\n");
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
    public Object atMe(long qq, Group group, @Param("str") String str) {
        if (str.startsWith(SPEAK_STR)) {
            speak(str.substring(1), group);
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
                if (!Tool.isIlleg(str)) {
                    if (cd0 < System.currentTimeMillis()) {
                        cd0 = System.currentTimeMillis() + CD;
                        String talk = otherService.talk(str);
                        return talk;
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
//            return Tool.pathToImg(url);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "生成失败";
//        }
//    }
}