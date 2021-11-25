package Project.Controllers.NormalController;


import Entitys.Group;
import Entitys.UScore;
import Entitys.User;
import Project.DataBases.DataBase;
import Project.Services.Iservice.IOtherService;
import Project.Services.Iservice.IScoreService;
import io.github.kloping.MySpringTool.annotations.*;
import io.github.kloping.MySpringTool.exceptions.NoRunException;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static Project.Controllers.ControllerTool.CanGroup;
import static Project.Tools.Tool.findNumberFromString;
import static io.github.kloping.Mirai.Main.ITools.MessageTools.getAtFromString;
import static io.github.kloping.Mirai.Main.Resource.Switch.AllK;
import static io.github.kloping.Mirai.Main.Resource.println;

@Controller
public class ScoreController {
    public ScoreController() {
        println(this.getClass().getSimpleName() + "构建");
    }

    public static final List<Long> closeings = new CopyOnWriteArrayList<>();
    @AutoStand
    IOtherService otherService;
    @AutoStand
    IScoreService scoreService;

    @Before
    public void before(Group group) throws NoRunException {
        if (!AllK)
            throw new NoRunException();
        if (!CanGroup(group.getId())) {
            throw new NoRunException();
        }
    }

    @Action("猜拳<.+=>str>")
    public String mora(User qq, @Param("str") String str) {
        return otherService.mora(qq.getId(), str);
    }

    @Action(value = "积分查询", otherName = {"查询积分"})
    public String selectScore(long qq) {
        return scoreService.selectInfo(qq);
    }

    @Action("取积分<\\d{1,}=>str>")
    public String getScore(long qq, @Param("str") String str) {
        if (closeings.contains(qq))
            return "账户锁定中...\r\n退出客户端登录后重试";
        Long num = null;
        try {
            num = Long.valueOf(str);
            return scoreService.getScore(qq, num);
        } catch (NumberFormatException e) {
            return "取多少";
        }
    }

    @Action("存积分<\\d{1,}=>str>")
    public String putScore(long qq, @Param("str") String str) {
        if (closeings.contains(qq))
            return "账户锁定中...\r\n退出客户端登录后重试";
        Long num = null;
        try {
            num = Long.valueOf(str);
            return scoreService.putScore(qq, num);
        } catch (NumberFormatException e) {
            return "存多少";
        }
    }

    @Action(value = "积分转让.{0,}", otherName = {"转让积分.{0,}"})
    public String Transfer(User qq, @AllMess String str) {
        Long num = null;
        try {
            if (longs.contains(qq.getId())) return "Can't";
            long who = getAtFromString(str);
            if (who == -1)
                return "转给谁啊";
            if (!DataBase.exists(who)) return "该玩家尚未注册";
            str = str.replaceFirst(Long.toString(who), "");
            num = Long.valueOf(findNumberFromString(str));
            num = num <= 0 ? 0L : num;
            return scoreService.getScoreTo(qq.getId(), who, num);
        } catch (NumberFormatException e) {
            return "格式错误(例: 积分转让 @我 10)";
        }
    }

    public static List<Long> longs = Arrays.asList((new Long[]{291841860L, 392801250L}));

    @Action(value = "抢劫.+", otherName = {"打劫.+"})
    public String Robbery(User qq, @AllMess String str) {
        try {
            long who = getAtFromString(str);
            if (who == -1)
                return "抢谁？";
            if (!DataBase.exists(who)) return "该玩家尚未注册";

            return scoreService.Robbery(qq.getId(), who);
        } catch (NumberFormatException e) {
            return "格式错误(例: 打劫 @我 )";
        }
    }

    @Action(value = "签到", otherName = {"冒泡", "早安"})
    public String Sign(User qq, Group group) {
        String str = scoreService.Sign(qq.getId());
        return str;
    }

    @Action(value = "今日签榜", otherName = {"签榜"})
    public String todayList(Group group) {
        return scoreService.todayList(group);
    }

    @Action("我的发言")
    public String getSpeaks(User qq) {
        UScore ls = DataBase.getAllInfo(qq.getId());
        return "你今天发言了:" + ls.getTimes() + "次\n" + "累计发言:" + ls.getSTimes() + "次";
    }

    @Action(value = "积分侦查.{1,}", otherName = "侦查积分.{1,}")
    public String showScore(User qq, @AllMess String mess) {
        try {
            long who = getAtFromString(mess);
            if (!DataBase.exists(who)) return "该玩家尚未注册";
            UScore ls = DataBase.getAllInfo(who);
            return "ta的积分剩余:" + ls.getScore();
        } catch (NumberFormatException e) {
            return "格式错误(例: 侦查积分 @我 )";
        }
    }

    @Action("打工")
    public String AJob(User qq, Group group) {
        return scoreService.WorkLong(qq.getId());
    }

    @Action(value = "他的发言.{1,}", otherName = {"她的发言.{1,}", "ta的发言.{1,}"})
    public String getSpeaks(User qq, @AllMess String str) {
        StringBuilder builder = new StringBuilder();
        try {
            long who = getAtFromString(str);
            if (who == -1)
                return builder.append("谁？").toString();
            if (!DataBase.exists(who)) return ("该玩家尚未注册");
            UScore ls = DataBase.getAllInfo(who);
            return builder.append("ta的:").append("今天发言了:" + ls.getTimes() + "次\n" + "累计发言:" + ls.getSTimes() + "次").toString();
        } catch (NumberFormatException e) {
            return builder.append("格式错误(例: ta的发言 @我 )").toString();
        }
    }
}
