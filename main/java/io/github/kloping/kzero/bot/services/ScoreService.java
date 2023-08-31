package io.github.kzero.bot.services;


import io.github.kloping.MySpringTool.annotations.AutoStand;
import io.github.kloping.MySpringTool.annotations.Entity;
import io.github.kzero.bot.database.DataBase;
import io.github.kzero.spring.dao.UserScore;


/**
 * @author github-kloping
 */
@Entity
public class ScoreService {
    @AutoStand
    DataBase dataBase;

    public String selectInfo(String sid) {
        StringBuilder str = new StringBuilder();
        UserScore lll = dataBase.getUserInfo(sid);
        str.append("剩的积分:").append(lll.getScore()).append("\r\n");
        str.append("存的积分:").append(lll.getSScore());
        return str.toString();
    }


    public String getScore(String sid, long num) {
        long l1 = dataBase.getUserInfo(sid).getSScore().longValue();
        if (l1 >= num) {
            dataBase.addScore_(-num,sid);
            dataBase.addScore(num,sid);
            return "取积分成功";
        } else {
            return "存的积分不足:" + num + "\n 你存的积分:" + l1;
        }
    }


    public String putScore(String sid, long num) {
        long l1 = dataBase.getUserInfo(sid).getScore();
        if (l1 >= num) {
            dataBase.addScore_(num,sid);
            dataBase.addScore(-num,sid);
            return "存积分成功";
        } else {
            return "积分不足:" + num + "\n 你剩余积分:" + l1;
        }
    }


    public String getScoreTo(String sid, String tid, long num) {
        long l1 = dataBase.getUserInfo(sid).getScore();
        if (l1 >= num) {
            dataBase.addScore(-num,sid);
            dataBase.addScore(num,tid);
            return "积分转让成功";
        } else {
            return "存的积分不足:" + num + "\n 你存的积分:" + l1;
        }
    }

  /*


    public String robbery(Longsid, Longsids) {
        long lI = dataBase.getUserInfo(sid).getScore();
        long lY = dataBase.getUserInfo(sids).getScore();
        long fI = dataBase.getUserInfo(sid).getFz();
        if (lI > 60) {
            if (lY > 60) {
                if (fI < 12) {
                    long l = Tool.INSTANCE.RANDOM.nextInt(20) + 40;
                    dataBase.addScore(l,sid);
                    dataBase.addScore(-l,sids);
                    dataBase.putInfo(dataBase.getUserInfo(sid).record(l));
                    dataBase.putInfo(dataBase.getUserInfo(sids).record(-l));
                    dataBase.addFz(1,sid);
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


    public String sign(Long qid) {
        synchronized (qid) {
            UserScore ls = dataBase.getUserInfo(qid);
            int day = Tool.INSTANCE.getTodayInt();
            if (ls.getDay() == day) {
                return "签到失败,你今天已经签到过了!!";
            } else {
                ls.setFz(0L);
                ls.setDay(Long.valueOf(day));
                ls.setDays((ls.getDays().intValue() + 1L));
                ls.addScore(100);
                dataBase.putInfo(ls);
                SpringBootResource.getSingListMapper().insert(qid.longValue(), Tool.INSTANCE.getTodayDetialString(), System.currentTimeMillis());
                Object[] lines = regDay(qid);
                String line = lines[0].toString();
                Integer st = Integer.valueOf(lines[1].toString());
                if (line.isEmpty()) {
                    return Tool.INSTANCE.getTou(qid) + "\n签到成功!\n增加100积分\n犯罪指数清除\n累计签到:" + ls.getDays() + "次";
                } else {
                    return Tool.INSTANCE.getTou(qid) + "\n签到成功!\n增加100积分\n犯罪指数清除\n累计签到:" + ls.getDays() + "次\n"
                            + getImageFromFontString("第" + Tool.INSTANCE.trans(st) + "签")
                            + "\n" + line;
                }
            }
        }
    }


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

private static final Object[] regDay(Number l) {
        int r = SpringBootResource.getSingListMapper().selectCountByDay(Tool.INSTANCE.getTodayDetialString());
        switch (r) {
            case 1:
                dataBase.addScore(100, l.longValue());
                return new Object[]{"额外获得100积分", r};
            case 2:
                dataBase.addScore(50, l.longValue());
                return new Object[]{"额外获得50积分", r};
            case 3:
                dataBase.addScore(25, l.longValue());
                return new Object[]{"额外获得25积分", r};
            case 10:
                dataBase.addScore(150, l.longValue());
                return new Object[]{"额外获得150积分", r};
            default:
                return new Object[]{"", r};
        }
    }
    public String earnings(long id) {
        UserScore score = dataBase.getUserInfo(id);
        return String.format(ResourceSet.FinalFormat.EARNINGS_TIPS_FORMAT, score.getEarnings(), score.getDebuffs());
    }*/
}
