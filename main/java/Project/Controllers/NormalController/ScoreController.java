package Project.Controllers.NormalController;


import io.github.kloping.mirai0.Entitys.Group;
import io.github.kloping.mirai0.Entitys.UScore;
import io.github.kloping.mirai0.Entitys.User;
import Project.DataBases.DataBase;
import Project.services.Iservice.IOtherService;
import Project.services.Iservice.IScoreService;
import io.github.kloping.MySpringTool.annotations.*;
import io.github.kloping.MySpringTool.exceptions.NoRunException;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static Project.Controllers.ControllerTool.opened;
import static Project.ResourceSet.FinalFormat.CANT_BIGGER;
import static Project.ResourceSet.FinalString.*;
import static Project.ResourceSet.FinalValue.MAX_ROBBERY_TIMES;
import static io.github.kloping.mirai0.unitls.Tools.Tool.findNumberFromString;
import static io.github.kloping.mirai0.Main.ITools.MessageTools.getAtFromString;
import static io.github.kloping.mirai0.Main.Resource.println;

/**
 * @author github-kloping
 */
@Controller
public class ScoreController {
    public ScoreController() {
        println(this.getClass().getSimpleName() + "构建");
    }

    public static final List<Long> CLOSED = new CopyOnWriteArrayList<>();

    @AutoStand
    IOtherService otherService;
    @AutoStand
    IScoreService scoreService;

    @Before
    public void before(Group group) throws NoRunException {
        if (!opened(group.getId(), this.getClass())) {
            throw new NoRunException("未开启");
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
        if (CLOSED.contains(qq))
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
        if (CLOSED.contains(qq))
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
    public String transfer(User qq, @AllMess String str) {
        Long num = null;
        try {
            if (longs.contains(qq.getId())) return ILLEGAL_OPERATION;
            long who = getAtFromString(str);
            if (who == -1)
                return NOT_FOUND_AT;
            if (!DataBase.exists(who)) return PLAYER_NOT_REGISTERED;
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
    public String robbery(User qq, @AllMess String str) {
        try {
            long who = getAtFromString(str);
            if (who == -1)
                return NOT_FOUND_AT;
            String numStr = findNumberFromString(str.replace(String.valueOf(who), ""));
            if (numStr != null && !numStr.trim().isEmpty()) {
                int n = Integer.parseInt(numStr);
                if (n > MAX_ROBBERY_TIMES) {
                    return String.format(CANT_BIGGER, MAX_ROBBERY_TIMES);
                } else {
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < n; i++) {
                        sb.append(scoreService.robbery(qq.getId(), who)).append("\n");
                    }
                    return sb.toString().trim();
                }
            }
            if (!DataBase.exists(who)) return PLAYER_NOT_REGISTERED;
            return scoreService.robbery(qq.getId(), who);
        } catch (NumberFormatException e) {
            return "格式错误(例: 打劫 @我 )";
        }
    }

    @Action(value = "签到", otherName = {"冒泡", "早安"})
    public String sign(User qq, Group group) {
        String str = scoreService.sign(qq.getId());
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
    public String aJob(User qq, Group group) {
        return scoreService.workLong(qq.getId());
    }

    @Action(value = "他的发言.{1,}", otherName = {"她的发言.{1,}", "ta的发言.{1,}"})
    public String getSpeaks(User qq, @AllMess String str) {
        StringBuilder builder = new StringBuilder();
        try {
            long who = getAtFromString(str);
            if (who == -1)
                return builder.append("谁？").toString();
            if (!DataBase.exists(who)) return PLAYER_NOT_REGISTERED;
            UScore ls = DataBase.getAllInfo(who);
            return builder.append("ta").append("今天发言了:" + ls.getTimes() + "次\n" + "累计发言:" + ls.getSTimes() + "次").toString();
        } catch (NumberFormatException e) {
            return builder.append("格式错误(例: ta的发言 @我 )").toString();
        }
    }
}
