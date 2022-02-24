package Project.services.impl;


import Project.dataBases.GameDataBase;
import io.github.kloping.mirai0.Entitys.Group;
import io.github.kloping.mirai0.Entitys.TradingRecord;
import io.github.kloping.mirai0.Entitys.UserScore;
import Project.dataBases.DataBase;
import Project.interfaces.Iservice.IScoreService;
import io.github.kloping.mirai0.Main.ITools.MemberTools;
import io.github.kloping.MySpringTool.annotations.Entity;

import static Project.dataBases.DataBase.*;
import static Project.dataBases.GameDataBase.getInfo;
import static Project.dataBases.GameDataBase.putPerson;
import static Project.ResourceSet.FinalFormat.WORK_WAIT_TIPS;
import static io.github.kloping.mirai0.unitls.Tools.Tool.*;
import static io.github.kloping.mirai0.unitls.drawers.Drawer.getImageFromFontString;
import static io.github.kloping.mirai0.unitls.drawers.Drawer.getImageFromStrings;

/**
 * @author github-kloping
 */
@Entity
public class ScoreServiceImpl implements IScoreService {

    @Override
    public String selectInfo(Long who) {
        StringBuilder str = new StringBuilder();
        UserScore lll = DataBase.getAllInfo(who);
        str.append("剩的积分:").append(lll.getScore()).append("\r\n");
        str.append("存的积分:").append(lll.getSScore());
        return str.toString();
    }

    @Override
    public String getScore(Long who, long num) {
        long l1 = DataBase.getAllInfo(who).getSScore().longValue();
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
        long l1 = DataBase.getAllInfo(who).getScore();
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
        long l1 = DataBase.getAllInfo(who).getScore();
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
        long lI = DataBase.getAllInfo(who).getScore();
        long lY = DataBase.getAllInfo(whos).getScore();
        long fI = DataBase.getAllInfo(who).getFz();
        if (lI > 60) {
            if (lY > 60) {
                if (fI < 12) {
                    long l = rand.nextInt(20) + 40;
                    addScore(l, who);
                    addScore(-l, whos);
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
    public String sign(Long who) {
        UserScore ls = DataBase.getAllInfo(who);
        long day = Long.parseLong(getToday());
        if (ls.getDay() == day) {
            return "签到失败,你今天已经签到过了!!";
        } else {
            ls.setFz(0L);
            ls.setDay(Long.valueOf(getToday()));
            ls.setDays((long) (ls.getDays().intValue() + 1));
            ls.setScore(ls.getScore() + 100);
            putInfo(ls);
            Object[] lines = regDay(who);
            String line = lines[0].toString();
            Integer st = Integer.valueOf(lines[1].toString());
            if (line.isEmpty())
                return getTou(who) + "\n签到成功!\n增加100积分\n犯罪指数清除\n累计签到:" + ls.getDays() + "次";
            else
                return getTou(who) + "\n签到成功!\n增加100积分\n犯罪指数清除\n累计签到:" + ls.getDays() + "次\n"
                        + getImageFromFontString("第" + trans(st + 1) + "签")
                        + "\n" + line;
        }
    }

    private static synchronized final Object[] regDay(Number l) {
        int r = regToday(l);
        switch (r) {
            case 0:
                addScore(100, l.longValue());
                return new Object[]{"额外获得100积分", r};
            case 1:
                addScore(50, l.longValue());
                return new Object[]{"额外获得50积分", r};
            case 2:
                addScore(25, l.longValue());
                return new Object[]{"额外获得25积分", r};
            case 9:
                addScore(150, l.longValue());
                return new Object[]{"额外获得150积分", r};
            default:
                return new Object[]{"", r};
        }
    }

    @Override
    public String workLong(Long who) {
        setK(who, getK(who));
        if (getK(who) <= System.currentTimeMillis()) {
            int tr = rand.nextInt(26) + 12;
            int nr = rand.nextInt(45) + 25;
            int s = tr * 5;
            addScore(s, who);
            putPerson(GameDataBase.getInfo(who).addGold((long) nr
                    , new TradingRecord()
                            .setType1(TradingRecord.Type1.add)
                            .setType0(TradingRecord.Type0.gold)
                            .setTo(-1)
                            .setMain(who)
                            .setFrom(who)
                            .setDesc("打工")
                            .setMany(nr)
            ));
            setK(who, System.currentTimeMillis() + tr * 1000 * 60);
            return getImageFromStrings("你花费了" + tr + "分钟", "打工赚了" + s + "积分", "赚了" + nr + "个金魂币");
        } else {
            return String.format(WORK_WAIT_TIPS, getTimeTips(getK(who)));
        }
    }

    @Override
    public String todayList(Group group) {
        String[] ss = getDayList();
        int n = 1;
        StringBuilder sb = new StringBuilder();
        sb.append("今日" + getToday() + "号:VV\r\n");
        for (String s : ss) {
            String name = null;
            try {
                name = MemberTools.getNameFromGroup(Long.parseLong(s.trim()), group);
            } catch (Exception e) {
                name = s;
            }
            sb.append("第").append(trans(n++)).append(":\r\n=>").append(name).append("\r\n");
        }
        return sb.toString();
    }
}
