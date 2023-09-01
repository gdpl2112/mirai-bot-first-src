package io.github.kloping.kzero.bot.controllers.normalController;


import io.github.kloping.MySpringTool.annotations.*;
import io.github.kloping.judge.Judge;
import io.github.kloping.kzero.bot.database.DataBase;
import io.github.kloping.kzero.bot.services.ScoreService;
import io.github.kloping.kzero.rt.ResourceSet;
import io.github.kloping.kzero.utils.Utils;
import io.github.kloping.number.NumberUtils;


/**
 * @author github-kloping
 */
@Controller
public class ScoreController {
    @AutoStand
    ScoreService scoreService;

    @Action(value = "积分查询", otherName = {"查询积分"})
    public String selectScore(String sid) {
        return scoreService.selectInfo(sid);
    }

    @Action("取积分<\\d{1,}=>str>")
    public String getScore(String sid, @Param("str") String str) {
        Long num = null;
        try {
            num = Long.valueOf(str);
            return scoreService.getScore(sid, num);
        } catch (NumberFormatException e) {
            return "取多少";
        }
    }

    @Action("存积分<\\d{1,}=>str>")
    public String putScore(String sid, @Param("str") String str) {
        Long num = null;
        try {
            num = Long.valueOf(str);
            return scoreService.putScore(sid, num);
        } catch (NumberFormatException e) {
            return "存多少";
        }
    }

    @AutoStand
    DataBase dataBase;

    @Action(value = "积分转让.{0,}", otherName = {"转让积分.{0,}"})
    public String transfer(String sid, @AllMess String str) {
        Long num = null;
        try {
            String tid = Utils.getAtFromString(str);
            if (Judge.isEmpty(tid)) return ResourceSet.FinalString.NOT_FOUND_AT;
            if (!dataBase.exists(sid)) return ResourceSet.FinalString.PLAYER_NOT_REGISTERED;
            str = str.replaceFirst(tid, "");
            num = Long.valueOf(NumberUtils.getIntegerFromString(str, 0));
            return scoreService.getScoreTo(sid, tid, num);
        } catch (NumberFormatException e) {
            return "格式错误(例: 积分转让 @我 10)";
        }
    }

/*
    @Action(value = "抢劫.+", otherName = {"打劫.+"})
    public String robbery(String sid, @AllMess String str) {
        try {
            long who = Utils.getAtFromString(str);
            if (who == -1) return ResourceSet.FinalString.NOT_FOUND_AT;
            String numStr = Tool.INSTANCE.findNumberFromString(str.replace(String.valueOf(who), ""));
            if (numStr != null && !numStr.trim().isEmpty()) {
                int n = Integer.parseInt(numStr);
                if (n > ResourceSet.FinalValue.MAX_ROBBERY_TIMES) {
                    return String.format(ResourceSet.FinalFormat.CANT_BIGGER, ResourceSet.FinalValue.MAX_ROBBERY_TIMES);
                } else {
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < n; i++) {
                        sb.append(scoreService.robbery(qq.getId(), who)).append("\n");
                    }
                    return sb.toString().trim();
                }
            }
            if (!DataBase.exists(who)) return ResourceSet.FinalString.PLAYER_NOT_REGISTERED;
            return scoreService.robbery(qq.getId(), who);
        } catch (NumberFormatException e) {
            return "格式错误(例: 打劫<At>)";
        }
    }

    @Action(value = "捐款")
    public String donate(String sid, @AllMess String str) {
        if (DataBase.getUserInfo(qq.getId()).getScore() < 140) return "积分不足";
        if (DataBase.isMaxEarnings(qq.getId())) {
            return ResourceSet.FinalString.MAX_EARNINGS_TIPS;
        }
        long l = Tool.INSTANCE.RANDOM.nextInt(40) + 40;
        long r = Tool.INSTANCE.RANDOM.nextInt(3) + 1;
        DataBase.addScore(-l, qq.getId());
        DataBase.putInfo(DataBase.getUserInfo(qq.getId()).record(-l));
        DataBase.addFz(-r, qq.getId());
        return String.format("捐款了%s积分,降低了%s点犯罪指数", l, r);
    }

    @Action(value = "签到", otherName = {"冒泡", "早安"})
    public String sign(String sid) {
        String str = scoreService.sign(qq.getId(), group);
        return str;
    }

    @Action(value = "今日签榜", otherName = {"签榜"})
    public String todayList(MessagePack pack) {
        return scoreService.todayList(pack.getSubjectId());
    }

    @Action("我的发言")
    public String getSpeaks(String sid) {
        UserScore ls = DataBase.getUserInfo(qq.getId());
        return "你今天发言了:" + ls.getTimes() + "次\n" + "累计发言:" + ls.getSTimes() + "次";
    }

    @Action(value = "积分侦查.{1,}", otherName = "侦查积分.{1,}")
    public String showScore(String sid, @AllMess String mess) {
        try {
            long who = Utils.getAtFromString(mess);
            if (!DataBase.exists(who)) return ResourceSet.FinalString.PLAYER_NOT_REGISTERED;
            UserScore ls = DataBase.getUserInfo(who);
            return "ta的积分剩余:" + ls.getScore();
        } catch (NumberFormatException e) {
            return "格式错误(例: 侦查积分 @我 )";
        }
    }

    @Action("发言排行.?")
    public String ph(@AllMess String s,) {
        Integer s0 = Tool.INSTANCE.getIntegerFromStr(s);
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
    public String ph0(@AllMess String s,) {
        Integer s0 = Tool.INSTANCE.getIntegerFromStr(s);
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
    public String scorePh(@AllMess String s,) {
        Integer s0 = Tool.INSTANCE.getIntegerFromStr(s);
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
    }*/
}
