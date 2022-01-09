package Project.Services.DetailServices;


import Entitys.TradingRecord;
import Entitys.gameEntitys.PersonInfo;
import io.github.kloping.MySpringTool.annotations.Entity;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static Project.DataBases.GameDataBase.*;
import static Project.Services.DetailServices.GameJoinDetailService.AttGho;
import static Project.Tools.GameTool.isAlive;
import static Project.Tools.Tool.randLong;

@Entity
public class GameWeaDetailService {
    private final static Class cla = GameWeaDetailService.class;
    public static final List<String> Aqs = new ArrayList<>();

    public GameWeaDetailService() {
        if (Aqs.isEmpty()) {
            for (int id : id2NameMaps.keySet()) {
                if (id > 1000 && id < 1200)
                    Aqs.add(getNameById(id));
            }
        }
    }

    private synchronized void InitAqs() {
        if (Aqs.isEmpty())
            for (int id : id2NameMaps.keySet()) {
                if (id > 1000 && id < 1200)
                    Aqs.add(getNameById(id));
            }
    }

    public String UseAq(List<String> lps, long who, String name) {
        if (Aqs.isEmpty()) InitAqs();
        if (!Aqs.contains(name))
            return "系统未找到 此暗器";
        int id = Name2idMaps.get(name);
        if (!exitsO(id, who)) {
            return "你没有 " + name + "或已损坏";
        }
        try {
            Method method = cla.getMethod("use" + (id), List.class, long.class);
            String mes = (String) method.invoke(this, lps, who);
            used(who, id);
            return mes;
        } catch (Exception e) {
            e.printStackTrace();
            return "使用异常...";
        }
    }

    /**
     * 诸葛神弩
     *
     * @return
     */
    public String use1001(List<String> lps, long who) {
        if (lps.size() == 1) {
            long ar = (long) (500 + (getInfo(who).getAtt() * 0.9f));
            PersonInfo info_ = getInfo(who);
            ar = ar > 10000 ? 10000 : ar;
            if (lps.get(0).contains("#")) {
                Long l = Long.valueOf(ar);
                String ss = AttGho(who, l, true, false);
                return ss;
            } else {
                long whos = Long.parseLong(lps.get(0));
                String end = AttPer(who, whos, ar);
                return end;
            }
        } else {
            return "选择器,过多或过少";
        }
    }

    /**
     * 龙须针
     *
     * @return
     */
    public String use1002(List<String> lps, long who) {
        int num = lps.size();
        if (num < 4 && num > 0) {
            long ar = getInfo(who).getLevel() * 1000;
            ar = ar > 20000 ? 20000 : ar;
            Object[] os = startAtt(who, ar, lps);
            String sb = (String) os[0];
            boolean used = (boolean) os[1];
            return sb;
        } else {
            return "选择器,过多或过少";
        }
    }


    /**
     * 含沙射影
     *
     * @return
     */
    public String use1003(List<String> lps, long who) {
        int num = lps.size();
        if (num < 3 && num > 0) {
            long ar = (long) (getInfo(who).getAtt() * 0.6f);
            ar = ar > 80000 ? 80000 : ar;
            Object[] os = startAtt(who, ar, lps);
            String sb = (String) os[0];
            boolean used = (boolean) os[1];
            return sb;
        } else {
            return "选择器,过多或过少";
        }
    }


    /**
     * 字母追魂夺命胆
     *
     * @return
     */
    public String use1004(List<String> lps, long who) {
        int num = lps.size();
        if (num < 5 && num > 0) {
            long ar = (long) (1500 + getInfo(who).getAtt() * 0.45f);
            ar = ar > 500000 ? 500000 : ar;
            Object[] os = startAtt(who, ar, lps);
            String sb = (String) os[0];
            boolean used = (boolean) os[1];
            return sb;
        } else {
            return "选择器,过多或过少";
        }
    }

    /**
     * 孔雀翎
     *
     * @return
     */
    public String use1005(List<String> lps, long who) {
        int num = lps.size();
        if (num < 4 && num > 0) {
            long ar = (long) (getInfo(who).getAtt() * 0.65f);
            ar = ar > 1000000 ? 1000000 : ar;
            Object[] os = startAtt(who, ar, lps);
            String sb = (String) os[0];
            boolean used = (boolean) os[1];
            return sb;
        } else {
            return "选择器,过多或过少";
        }
    }

    /**
     * 暴雨梨花针
     *
     * @return
     */
    public String use1006(List<String> lps, long who) {
        if (lps.size() == 1) {
            long ar = (long) (3000 + (getInfo(who).getAtt() * 2.8f));
            ar = ar > 2500000 ? 2500000 : ar;
            if (lps.get(0).contains("#")) {
                Long l = Long.valueOf(ar);
                String ss = AttGho(who, l, true, false);
                return ss;
            } else {
                long whos = Long.parseLong(lps.get(0));
                String end = AttPer(who, whos, ar);
                return end;
            }
        } else {
            return "选择器,过多或过少";
        }
    }

    /**
     * 佛怒唐莲
     *
     * @return
     */
    public String use1007(List<String> lps, long who) {
        int num = lps.size();
        if (num < 4 && num > 0) {
            long ar = (long) (4500 + getInfo(who).getAtt() * 0.72f + getInfo(who).getLevel() * 10);
            ar = ar > 3000000 ? 3000000 : ar;
            Object[] os = startAtt(who, ar, lps);
            String sb = (String) os[0];
            boolean used = (boolean) os[1];
            return sb;
        } else {
            return "选择器,过多或过少";
        }
    }

    public void used(long who, int id) {
        Map<Integer, Map.Entry<Integer, Integer>> maps = getBgsw(Long.valueOf(who));
        for (Integer i : maps.keySet()) {
            if (maps.get(i).getKey() == id) {
                int c = maps.get(i).getValue();
                if (c > 1) {
                    removeFromAqBgs(who, id + ":" + c);
                    c--;
                    addToAqBgs(Long.valueOf(who), id + ":" + c);
                } else {
                    removeFromAqBgs(who, id + ":" + c);
                }
                return;
            }
        }
    }

    public boolean exitsO(int id, long who) {
        Map<Integer, Map.Entry<Integer, Integer>> maps = getBgsw(who);
        for (Integer i : maps.keySet()) {
            if (maps.get(i).getKey() == id) {
                return true;
            }
        }
        return false;
    }

    public Object[] startAtt(long who, long ar, List<String> lps) {
        Boolean used = true;
        int n = 1;
        StringBuilder sb = new StringBuilder();
        for (String whos : lps) {
            if (whos.equals("#")) {
                Long l = Long.valueOf(ar);
                String ss = AttGho(who, l, n++ == lps.size(), false);
                if (ss.startsWith("你对"))
                    used = true;
                sb.append(ss).append("\r\n").append("=======================\r\n");
            } else {
                String end = AttPer(who, Long.parseLong(whos), ar);
                if (end.equals(NoH))
                    used = false;
                sb.append(end).append("\r\n").append("======================\r\n");
            }
        }
        return new Object[]{sb.toString(), used};
    }

    private static final String NoH = "ta已经没有状态无需攻击";

    public String AttPer(long who, long whos, long ar) {
        StringBuilder sb = new StringBuilder();
        if (!isAlive(Long.valueOf(whos))) {
            return NoH;
        }
        long hps = getInfo(whos).getHp();

        if (hps <= ar)
            sb.append(GameDetailService.beaten(whos, who, hps));
        else
            sb.append(GameDetailService.beaten(whos, who, ar));

        if (!isAlive(Long.valueOf(whos))) {
            int L = (int) randLong(250, 0.7f, 1.0f);
            putPerson(getInfo(who).addGold((long) L
                    , new TradingRecord()
                            .setType1(TradingRecord.Type1.add)
                            .setType0(TradingRecord.Type0.gold)
                            .setTo(-1)
                            .setMain(who)
                            .setFrom(who)
                            .setDesc("击败" + whos)
                            .setMany(L)
            ));
            return "你对ta 造成了:" + ar + "点伤害" + sb + "\r\nta已经无状态,你从他身上摸到" + L + "个金魂币";
        } else {
            return "你对ta 造成了:" + ar + "点伤害" + sb;
        }
    }
}
