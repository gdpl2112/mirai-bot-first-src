package Project.services.impl;


import Project.aSpring.SpringBootResource;
import Project.aSpring.dao.TradingRecord;
import Project.aSpring.dao.UserScore;
import Project.commons.SpGroup;
import Project.dataBases.DataBase;
import Project.dataBases.GameDataBase;
import Project.interfaces.Iservice.IScoreService;
import Project.utils.Tools.Tool;
import io.github.kloping.MySpringTool.annotations.Entity;
import io.github.kloping.mirai0.Main.iutils.MemberUtils;

import java.util.List;

import static Project.commons.rt.ResourceSet.FinalFormat.EARNINGS_TIPS_FORMAT;
import static Project.commons.rt.ResourceSet.FinalFormat.WORK_WAIT_TIPS;
import static Project.commons.rt.ResourceSet.FinalString.WORK_LONG_STR;
import static Project.dataBases.DataBase.*;
import static Project.utils.drawers.Drawer.getImageFromFontString;
import static Project.utils.drawers.Drawer.getImageFromStrings;

/**
 * @author github-kloping
 */
@Entity
public class ScoreServiceImpl implements IScoreService {
    private static final Object[] regDay(Number l) {
        int r = SpringBootResource.getSingListMapper().selectCountByDay(Tool.INSTANCE.getTodayDetialString());
        switch (r) {
            case 1:
                addScore(100, l.longValue());
                return new Object[]{"额外获得100积分", r};
            case 2:
                addScore(50, l.longValue());
                return new Object[]{"额外获得50积分", r};
            case 3:
                addScore(25, l.longValue());
                return new Object[]{"额外获得25积分", r};
            case 10:
                addScore(150, l.longValue());
                return new Object[]{"额外获得150积分", r};
            default:
                return new Object[]{"", r};
        }
    }

    @Override
    public String selectInfo(Long who) {
        StringBuilder str = new StringBuilder();
        UserScore lll = DataBase.getUserInfo(who);
        str.append("剩的积分:").append(lll.getScore()).append("\r\n");
        str.append("存的积分:").append(lll.getSScore());
        return str.toString();
    }

    @Override
    public String getScore(Long who, long num) {
        long l1 = DataBase.getUserInfo(who).getSScore().longValue();
        if (l1 >= num) {
            DataBase.addScore_(-num, who);
            addScore(num, who);
            return "取积分成功";
        } else {
            return "存的积分不足:" + num + "\n 你存的积分:" + l1;
        }
    }

    @Override
    public String putScore(Long who, long num) {
        long l1 = DataBase.getUserInfo(who).getScore();
        if (l1 >= num) {
            addScore_(num, who);
            addScore(-num, who);
            return "存积分成功";
        } else {
            return "积分不足:" + num + "\n 你剩余积分:" + l1;
        }
    }

    @Override
    public String getScoreTo(Long who, Long whos, long num) {
        long l1 = DataBase.getUserInfo(who).getScore();
        if (l1 >= num) {
            addScore(-num, who);
            addScore(num, whos);
            return "积分转让成功";
        } else {
            return "存的积分不足:" + num + "\n 你存的积分:" + l1;
        }
    }

    @Override
    public String robbery(Long who, Long whos) {
        long lI = DataBase.getUserInfo(who).getScore();
        long lY = DataBase.getUserInfo(whos).getScore();
        long fI = DataBase.getUserInfo(who).getFz();
        if (lI > 60) {
            if (lY > 60) {
                if (fI < 12) {
                    long l = Tool.INSTANCE.RANDOM.nextInt(20) + 40;
                    addScore(l, who);
                    addScore(-l, whos);
                    putInfo(getUserInfo(who).record(l));
                    putInfo(getUserInfo(whos).record(-l));
                    DataBase.addFz(1, who);
                    return "成功抢劫了" + l + "积分!\n增加1指数";
                } else {
                    return "犯罪指数最大";
                }
            } else {
                return "你竟然认为,抢一个穷光蛋能抢到钱!!";
            }
        } else {
            return "没钱还敢出来??";
        }
    }

    @Override
    public String sign(Long qid, SpGroup group) {
        synchronized (qid) {
            UserScore ls = DataBase.getUserInfo(qid);
            int day = Tool.INSTANCE.getTodayInt();
            if (ls.getDay() == day) {
                return "签到失败,你今天已经签到过了!!";
            } else {
                ls.setFz(0L);
                ls.setDay(Long.valueOf(day));
                ls.setDays((ls.getDays().intValue() + 1L));
                ls.addScore(100);
                putInfo(ls);
                SpringBootResource.getSingListMapper().insert(qid.longValue(), Tool.INSTANCE.getTodayDetialString(), System.currentTimeMillis());
                Object[] lines = regDay(qid);
                String line = lines[0].toString();
                Integer st = Integer.valueOf(lines[1].toString());
//                try {
//                    Integer s0 = 100;
//                    s0 += Tool.INSTANCE.getInteagerFromStr(line, 0);
//                    JSONObject jo = yiMin.qian(
//                            URLEncoder.encode(Tool.INSTANCE.getTouUrl(qid), "UTF-8"),
//                            URLEncoder.encode(MemberUtils.getNameFromGroup(qid, group), "UTF-8"),
//                            s0 + "积分",
//                            "累计签到:" + ls.getDays() + "次",
//                            "0号",
//                            "第" + Tool.INSTANCE.trans(st) + "签"
//                    );
//                    String url = jo.getString("data");
//                    return Tool.INSTANCE.pathToImg(url);
//                } catch (Exception e) {
//                    System.err.println("API sign call error");
                    if (line.isEmpty()) {
                        return Tool.INSTANCE.getTou(qid) + "\n签到成功!\n增加100积分\n犯罪指数清除\n累计签到:" + ls.getDays() + "次";
                    } else {
                        return Tool.INSTANCE.getTou(qid) + "\n签到成功!\n增加100积分\n犯罪指数清除\n累计签到:" + ls.getDays() + "次\n"
                                + getImageFromFontString("第" + Tool.INSTANCE.trans(st) + "签")
                                + "\n" + line;
                    }
//                }
            }
        }
    }

    @Override
    public String workLong(Long who) {
        setK(who, getK(who));
        if (getK(who) <= System.currentTimeMillis()) {
            int tr = Tool.INSTANCE.RANDOM.nextInt(26) + 12;
            int nr = Tool.INSTANCE.RANDOM.nextInt(45) + 25;
            int s = tr * 5;
            addScore(s, who);
            GameDataBase.getInfo(who).addGold((long) nr
                    , new TradingRecord()
                            .setType1(TradingRecord.Type1.add)
                            .setType0(TradingRecord.Type0.gold)
                            .setTo(-1)
                            .setMain(who)
                            .setFrom(who)
                            .setDesc(WORK_LONG_STR)
                            .setMany(nr)
            ).apply();
            putInfo(getUserInfo(who).record(s));
            setK(who, System.currentTimeMillis() + tr * 1000 * 60);
            return getImageFromStrings("你花费了" + tr + "分钟", "打工赚了" + s + "积分", "赚了" + nr + "个金魂币");
        } else {
            return String.format(WORK_WAIT_TIPS, Tool.INSTANCE.getTimeTips(getK(who)));
        }
    }

    @Override
    public String todayList(SpGroup group) {
        List<Long> list = SpringBootResource.getSingListMapper().selectDay(Tool.INSTANCE.getTodayDetialString());
        int n = 1;
        StringBuilder sb = new StringBuilder();
        sb.append("今日" + Tool.INSTANCE.getToday() + "号:VV\r\n");
        for (Long aLong : list) {
            String name = null;
            try {
                name = MemberUtils.getNameFromGroup(aLong, group);
            } catch (Exception e) {
                name = aLong.toString();
            }
            sb.append("第").append(Tool.INSTANCE.trans(n++)).append(":\r\n=>").append(name).append("\r\n");
        }
        return sb.toString();
    }

    @Override
    public String earnings(long id) {
        UserScore score = getUserInfo(id);
        return String.format(EARNINGS_TIPS_FORMAT, score.getEarnings(), score.getDebuffs());
    }
}
