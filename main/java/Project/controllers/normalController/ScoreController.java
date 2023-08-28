package Project.controllers.normalController;


import Project.aSpring.SpringBootResource;
import Project.commons.SpGroup;
import Project.commons.SpUser;
import Project.aSpring.dao.UserScore;
import Project.dataBases.DataBase;
import Project.interfaces.Iservice.IScoreService;
import io.github.kloping.MySpringTool.annotations.*;
import io.github.kloping.MySpringTool.exceptions.NoRunException;
import io.github.kloping.mirai0.Main.iutils.MemberUtils;
import Project.utils.Tools.Tool;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static Project.commons.rt.ResourceSet.FinalFormat.CANT_BIGGER;
import static Project.commons.rt.ResourceSet.FinalString.*;
import static Project.commons.rt.ResourceSet.FinalValue.MAX_ROBBERY_TIMES;
import static Project.commons.rt.ResourceSet.FinalValue.NOT_OPEN_NO_RUN_EXCEPTION;
import static Project.controllers.auto.ControllerSource.challengeDetailService;
import static Project.controllers.auto.ControllerTool.opened;
import static Project.dataBases.DataBase.*;
import static io.github.kloping.mirai0.Main.BootstarpResource.println;

/**
 * @author github-kloping
 */
@Controller
public class ScoreController {
    public static final List<Long> CLOSED = new CopyOnWriteArrayList<>();
    public static List<Long> longs = Arrays.asList((new Long[]{291841860L, 392801250L, 392801250L}));
    @AutoStand
    IScoreService scoreService;

    public ScoreController() {
        println(this.getClass().getSimpleName() + "构建");
    }

    @Before
    public void before(SpGroup group) throws NoRunException {
        if (!opened(group.getId(), this.getClass())) {
            throw NOT_OPEN_NO_RUN_EXCEPTION;
        }
    }

    @Action(value = "积分查询", otherName = {"查询积分"})
    public String selectScore(long qq) {
        return scoreService.selectInfo(qq);
    }

    @Action("取积分<\\d{1,}=>str>")
    public String getScore(long qq, @Param("str") String str) {
        if (CLOSED.contains(qq)) return "账户锁定中...\r\n退出客户端登录后重试";
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
        if (CLOSED.contains(qq)) return "账户锁定中...\r\n退出客户端登录后重试";
        Long num = null;
        try {
            num = Long.valueOf(str);
            return scoreService.putScore(qq, num);
        } catch (NumberFormatException e) {
            return "存多少";
        }
    }

    @Action(value = "积分转让.{0,}", otherName = {"转让积分.{0,}"})
    public String transfer(SpUser qq, @AllMess String str) {
        Long num = null;
        try {
            if (longs.contains(qq.getId())) return ILLEGAL_OPERATION;
            long who = Project.utils.Utils.getAtFromString(str);
            if (who == -1) return NOT_FOUND_AT;
            if (!DataBase.exists(who)) return PLAYER_NOT_REGISTERED;
            str = str.replaceFirst(Long.toString(who), "");
            num = Long.valueOf(Tool.INSTANCE.findNumberFromString(str));
            num = num <= 0 ? 0L : num;
            return scoreService.getScoreTo(qq.getId(), who, num);
        } catch (NumberFormatException e) {
            return "格式错误(例: 积分转让 @我 10)";
        }
    }

    @Action(value = "抢劫.+", otherName = {"打劫.+"})
    public String robbery(SpUser qq, @AllMess String str) {
        try {
            long who = Project.utils.Utils.getAtFromString(str);
            if (who == -1) return NOT_FOUND_AT;
            String numStr = Tool.INSTANCE.findNumberFromString(str.replace(String.valueOf(who), ""));
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
            return "格式错误(例: 打劫<At>)";
        }
    }

    @Action(value = "捐款")
    public String donate(SpUser qq, @AllMess String str) {
        if (DataBase.getUserInfo(qq.getId()).getScore() < 140) return "积分不足";
        if (DataBase.isMaxEarnings(qq.getId())) {
            return MAX_EARNINGS_TIPS;
        }
        long l = Tool.INSTANCE.RANDOM.nextInt(40) + 40;
        long r = Tool.INSTANCE.RANDOM.nextInt(3) + 1;
        addScore(-l, qq.getId());
        putInfo(getUserInfo(qq.getId()).record(-l));
        DataBase.addFz(-r, qq.getId());
        return String.format("捐款了%s积分,降低了%s点犯罪指数", l, r);
    }

    @Action(value = "签到", otherName = {"冒泡", "早安"})
    public String sign(SpUser qq, SpGroup group) {
        String str = scoreService.sign(qq.getId(), group);
        return str;
    }

    @Action(value = "今日签榜", otherName = {"签榜"})
    public String todayList(SpGroup group) {
        return scoreService.todayList(group);
    }

    @Action("我的发言")
    public String getSpeaks(SpUser qq) {
        UserScore ls = DataBase.getUserInfo(qq.getId());
        return "你今天发言了:" + ls.getTimes() + "次\n" + "累计发言:" + ls.getSTimes() + "次";
    }

    @Action(value = "积分侦查.{1,}", otherName = "侦查积分.{1,}")
    public String showScore(SpUser qq, @AllMess String mess) {
        try {
            long who = Project.utils.Utils.getAtFromString(mess);
            if (!DataBase.exists(who)) return PLAYER_NOT_REGISTERED;
            UserScore ls = DataBase.getUserInfo(who);
            return "ta的积分剩余:" + ls.getScore();
        } catch (NumberFormatException e) {
            return "格式错误(例: 侦查积分 @我 )";
        }
    }

    @Action(WORK_LONG_STR)
    public String aJob(SpUser qq, SpGroup group) {
        if (challengeDetailService.isTemping(qq.getId())) {
            return CHALLENGE_ING;
        }
        return scoreService.workLong(qq.getId());
    }

    @Action(value = "他的发言.{1,}", otherName = {"她的发言.{1,}", "ta的发言.{1,}"})
    public String getSpeaks(SpUser qq, @AllMess String str) {
        StringBuilder builder = new StringBuilder();
        try {
            long who = Project.utils.Utils.getAtFromString(str);
            if (who == -1) return builder.append("谁？").toString();
            if (!DataBase.exists(who)) return PLAYER_NOT_REGISTERED;
            UserScore ls = DataBase.getUserInfo(who);
            return builder.append("ta").append("今天发言了:" + ls.getTimes() + "次\n" + "累计发言:" + ls.getSTimes() + "次").toString();
        } catch (NumberFormatException e) {
            return builder.append("格式错误(例: ta的发言 @我 )").toString();
        }
    }

    @Action("发言排行.?")
    public String ph(@AllMess String s, SpGroup group) {
        Integer s0 = Tool.INSTANCE.getInteagerFromStr(s);
        s0 = s0 == null ? 10 : s0;
        s0 = s0 > 50 ? 50 : s0;
        List<UserScore> list = SpringBootResource.getScoreMapper().ph(s0);
        StringBuilder sb = new StringBuilder();
        int na = 0;
        for (UserScore score : list) {
            ++na;
            Long qid = score.getWho();
            Integer num = score.getSTimes().intValue();
            sb.append("第").append(na).append(": ").append(MemberUtils.getNameFromGroup(qid, group)).append("=>").append("累计发言了").append(num).append("次\n");
        }
        return sb.toString().isEmpty() ? "暂无记录" : sb.toString().trim();
    }

    @Action("今日发言排行.?")
    public String ph0(@AllMess String s, SpGroup group) {
        Integer s0 = Tool.INSTANCE.getInteagerFromStr(s);
        s0 = s0 == null ? 10 : s0;
        s0 = s0 > 50 ? 50 : s0;
        List<UserScore> list = SpringBootResource.getScoreMapper().toDay(Tool.INSTANCE.getTodayInt(), s0);
        StringBuilder sb = new StringBuilder();
        int na = 0;
        for (UserScore score : list) {
            ++na;
            Long qid = score.getWho();
            Integer num = score.getTimes().intValue();
            sb.append("第").append(na).append(": ").append(MemberUtils.getNameFromGroup(qid, group)).append("=>").append("今日发言了").append(num).append("次\n");
        }
        return sb.toString().isEmpty() ? "暂无记录" : sb.toString().trim();
    }

    @Action(value = "我的收益", otherName = {"收益详情", "积分收益"})
    public String earnings(SpUser user) {
        return scoreService.earnings(user.getId());
    }

    @Action("积分排行.*?")
    public String scorePh(@AllMess String s, SpGroup group) {
        Integer s0 = Tool.INSTANCE.getInteagerFromStr(s);
        s0 = s0 == null ? 10 : s0;
        s0 = s0 > 20 ? 20 : s0;
        List<UserScore> list = SpringBootResource.getScoreMapper().phScore(s0);
        StringBuilder sb = new StringBuilder();
        int na = 0;
        for (UserScore score : list) {
            ++na;
            Long qid = score.getWho();
            sb.append("第").append(na).append(": ").append(MemberUtils.getNameFromGroup(qid, group)).append("=>\n\t").append(score.getScore()).append("积分\n");
        }
        return sb.toString().isEmpty() ? "暂无记录" : sb.toString().trim();
    }
}
