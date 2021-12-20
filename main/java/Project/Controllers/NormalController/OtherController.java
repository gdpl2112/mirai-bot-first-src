package Project.Controllers.NormalController;


import Entitys.Group;
import Project.Services.Iservice.IOtherService;
import Project.Tools.Tool;
import io.github.kloping.Mirai.Main.ITools.MemberTools;
import io.github.kloping.Mirai.Main.ITools.MessageTools;
import io.github.kloping.MySpringTool.annotations.*;
import io.github.kloping.MySpringTool.exceptions.NoRunException;
import net.mamoe.mirai.contact.NormalMember;

import static Project.Controllers.ControllerTool.CanGroup;
import static io.github.kloping.Mirai.Main.Resource.Switch.AllK;
import static io.github.kloping.Mirai.Main.Resource.bot;
import static io.github.kloping.Mirai.Main.Resource.println;

@Controller
public class OtherController {
    public OtherController() {
        println(this.getClass().getSimpleName() + "构建");
    }

    @AutoStand
    IOtherService otherService;

    @Before
    public void before(Group group) throws NoRunException {
        if (!AllK)
            throw new NoRunException();
        if (!CanGroup(group.getId())) {
            throw new NoRunException();
        }
    }

    private static final StringBuilder menuStr = new StringBuilder();

    static {
        menuStr.append("1.基本菜单").append("\r\n");
        menuStr.append("2.暗器菜单").append("\r\n");
        menuStr.append("3.魂骨菜单").append("\r\n");
        menuStr.append("4.魂技菜单").append("\r\n");
        menuStr.append("5.回话菜单").append("\r\n");
        menuStr.append("6.宗门系统").append("\r\n");
        menuStr.append("7.交易市场").append("\r\n");
        menuStr.append("8.点歌系统").append("\r\n");
        menuStr.append("9.开始会话 #在线运行代码").append("\r\n");
        menuStr.append("不用at我,直接说就行了哦");
        //.append("\r\n");
    }

    @Action("菜单")
    public String menu() {
        return menuStr.toString();
    }

    private static final StringBuilder BaseMenuStr = new StringBuilder();
    private static String BaseMenuString;
    private static String[] BaseMenuStrings;

    static {
        BaseMenuStr.append("修炼\n");
        BaseMenuStr.append("信息\n");
        BaseMenuStr.append("详细信息 #查看信息\n");
        BaseMenuStr.append("升级 #经验足够时\n");
        BaseMenuStr.append("觉醒 #2级时用来觉醒武魂\n");
        BaseMenuStr.append("转生 #重置信息\n");
        BaseMenuStr.append("商城/商店/商场\n");
        BaseMenuStr.append("背包/我的背包 #查看背包\n");
        BaseMenuStr.append("说明 #见商城)\n");
        BaseMenuStr.append("购买 #见商城)\n");
        BaseMenuStr.append("使用 #见背包)\n");
        BaseMenuStr.append("物品转让(名字)(at)\n");
        BaseMenuStr.append("列表\n");
        BaseMenuStr.append("进入 #见列表\n");
        BaseMenuStr.append("====");
        BaseMenuStr.append("请求支援(遇到魂兽时请求)\n");
        BaseMenuStr.append("支援(At)\n");
        BaseMenuStr.append("探查 #查看当前魂兽信息\n");
        BaseMenuStr.append("吸收(十/百/千..)年魂环\n");
        BaseMenuStr.append("魂环配置 #展示魂环\n");
        BaseMenuStr.append("购买金魂币(值) #2积分1个\n");
        BaseMenuStr.append("攻击(at)\n");
        BaseMenuStr.append("侦查(at)\n");
        BaseMenuStr.append("升级第<几>魂环\n");
        BaseMenuStr.append("取名封号<名字>\n");
        BaseMenuStr.append("====");
        BaseMenuStr.append("选择(攻击/逃跑) \r\n\t#当遇到魂兽时使用\n");
        BaseMenuStr.append("换积分(用金魂币换积分)\r\n\t#1金魂币1.5积分(多出500的金魂币才能换)\n");
        BaseMenuStr.append("等级排行\n");
        BaseMenuStr.append("称号 #(查看所有称号)\n");
        BaseMenuStr.append("武魂类型 #查看自己的武魂类型是什么)\n");
        BaseMenuStr.append("融合武魂 @xx  # 需要融合戒指\n");
        BaseMenuStr.append("魂兽击杀排行\n");
        BaseMenuStr.append("关系列表\n");
        BaseMenuStr.append("收徒 @xx\n");
        BaseMenuStr.append("出师\n");
        BaseMenuStr.append("====");
        BaseMenuStr.append("魂环吸收限制");
        BaseMenuStr.append("精神力作用\n");
        BaseMenuStr.append("新机制\n");
        BaseMenuStr.append("怎么获得名师点\n");
        BaseMenuStr.append("====");
        BaseMenuStr.append("#任务相关\n");
        BaseMenuStr.append("接徒弟任务\n");
        BaseMenuStr.append("接每周任务\n");
        BaseMenuStr.append("当前任务\n");
        BaseMenuStr.append("====");
        BaseMenuStr.append("#积分相关\n");
        BaseMenuStr.append("签到\n");
        BaseMenuStr.append("今日签榜\n");
        BaseMenuStr.append("签榜\n");
        BaseMenuStr.append("取积分(值)\n");
        BaseMenuStr.append("存积分(值)\n");
        BaseMenuStr.append("积分转让/转让积分(@xx)(值)\n");
        BaseMenuStr.append("积分侦查 @xx\n");
        BaseMenuStr.append("打工 #赚积分\n");
        BaseMenuStr.append("积分查询/查询积分\n");
        BaseMenuStr.append("猜拳(石头/剪刀/布)(值)\n");
        BaseMenuStr.append("抢劫@at\n");
        BaseMenuStr.append("我的发言\n");
        BaseMenuStr.append("====");
        BaseMenuStr.append("#管理相关 #需要权限\n");
        BaseMenuStr.append("禁言(@xx)(值)(单位,秒,分..)\n");
        BaseMenuStr.append("解除禁言(@xx)\n");
        BaseMenuStr.append("开启/说话\n");
        BaseMenuStr.append("关闭/闭嘴\n");
        BaseMenuStr.append("(开启/关闭)闪照破解\n");
        BaseMenuStr.append("获取 @xx \n");
        BaseMenuStr.append("撤回 <@> <index...> \n");
        BaseMenuStr.append("====");
        BaseMenuStr.append("#其他\n");
        BaseMenuStr.append("搜图 xx \n");
        BaseMenuStr.append("百度搜图 xx \n");
        BaseMenuStr.append("堆糖搜图 xx \n");
        BaseMenuStr.append("发张 xx \n");
        BaseMenuStr.append("掷骰子\n");
        BaseMenuStr.append("捡瓶子/捡漂流瓶\n");
        BaseMenuStr.append("扔瓶子/仍漂流瓶<内容>\n");
//        BaseMenuStr.append("哔哩搜索 xx \n");
//        BaseMenuStr.append("哔哩哔哩搜索 xx \n");
//        BaseMenuStr.append("快手短视频 #不稳定\n");
//        BaseMenuStr.append("快手搜索 #不稳定\n");
        BaseMenuStr.append("/推<@>\n");
        BaseMenuStr.append("/推[图片]\n");
        BaseMenuStr.append("/玩球<@>\n");
        BaseMenuStr.append("/玩球[图片]\n");
        BaseMenuStr.append("/丢<@>\n");
        BaseMenuStr.append("/丢[图片]\n");
        BaseMenuString = BaseMenuStr.toString();
        BaseMenuStrings = BaseMenuString.split("====");
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
        sb.append("身份:").append(getPermisi(member.getPermission().getLevel())).append("\r\n");
        sb.append("群内名:").append(MemberTools.getNameFromGroup(qq, group)).append("\r\n");
        sb.append("QQ名:").append(member.getNick()).append("\r\n");
        sb.append("加入时间:").append(Tool.getTimeM(member.getJoinTimestamp() * 1000L)).append("\r\n");
        sb.append("最后发言:").append(Tool.getTimeM(member.getLastSpeakTimestamp() * 1000L)).append("\r\n");
        sb.append("头衔:").append(member.getSpecialTitle()).append("\r\n");
        sb.append("禁言时长:").append(member.getMuteTimeRemaining()).append("\r\n");
        sb.append("头像链接:").append(member.getAvatarUrl()).append("\r\n");
        return sb.toString();
    }

    public static String getPermisi(long le) {
        if (le == 0) return "群员";
        if (le == 1) return "管理员";
        if (le == 2) return "群主";
        return "未知";
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