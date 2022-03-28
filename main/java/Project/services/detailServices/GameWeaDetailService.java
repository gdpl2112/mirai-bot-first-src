package Project.services.detailServices;


import Project.aSpring.SpringBootResource;
import Project.broadcast.game.GhostLostBroadcast;
import io.github.kloping.MySpringTool.annotations.Entity;
import io.github.kloping.mirai0.commons.PersonInfo;
import io.github.kloping.mirai0.commons.TradingRecord;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static Project.dataBases.GameDataBase.*;
import static Project.services.detailServices.GameJoinDetailService.attGho;
import static io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet.FinalString.CHALLENGE_USED;
import static io.github.kloping.mirai0.unitls.Tools.GameTool.isAlive;
import static io.github.kloping.mirai0.unitls.Tools.Tool.randLong;

/**
 * @author github-kloping
 */
@Entity
public class GameWeaDetailService {
    public static final List<String> AQS = new ArrayList<>();
    public static final Map<Integer, Long> MAX_DAMAGE = new HashMap<>();
    private final static Class CLA = GameWeaDetailService.class;
    private static final String NO_H = "ta已经没有状态无需攻击";

    static {
        MAX_DAMAGE.put(1001, 10000L);
        MAX_DAMAGE.put(1002, 20000L);
        MAX_DAMAGE.put(1003, 80000L);
        MAX_DAMAGE.put(1004, 500000L);
        MAX_DAMAGE.put(1005, 1000000L);
        MAX_DAMAGE.put(1006, 2500000L);
        MAX_DAMAGE.put(1007, 3000000L);
    }

    public GameWeaDetailService() {
        if (AQS.isEmpty()) {
            for (int id : ID_2_NAME_MAPS.keySet()) {
                if (id > 1000 && id < 1200)
                    AQS.add(getNameById(id));
            }
        }
    }

    private synchronized void initAqs() {
        if (AQS.isEmpty())
            for (int id : ID_2_NAME_MAPS.keySet()) {
                if (id > 1000 && id < 1200)
                    AQS.add(getNameById(id));
            }
    }

    public String useAq(List<String> lps, long who, String name) {
        if (AQS.isEmpty()) initAqs();
        if (!AQS.contains(name))
            return "系统未找到 此暗器";
        int id = NAME_2_ID_MAPS.get(name);
        if (!exitsO(id, who)) {
            return "你没有 " + name + "或已损坏";
        }
        try {
            Method method = CLA.getMethod("use" + (id), List.class, long.class);
            String mes = (String) method.invoke(this, lps, who);
            used(who, id);
            if (ChallengeDetailService.USED.containsKey(who) && ChallengeDetailService.USED.get(who)) {
                return CHALLENGE_USED;
            } else {
                ChallengeDetailService.USED.put(who, true);
            }
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
            PersonInfo pInfo = getInfo(who);
            int sid = 1001;
            ar = ar > MAX_DAMAGE.get(sid) ? MAX_DAMAGE.get(sid) : ar;
            if (lps.get(0).contains("#")) {
                Long l = Long.valueOf(ar);
                String ss = attGho(who, l, true, false, GhostLostBroadcast.KillType.ANQ_ATT);
                return ss;
            } else {
                long whos = Long.parseLong(lps.get(0));
                String end = attPer(who, whos, ar);
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
            int sid = 1002;
            ar = ar > MAX_DAMAGE.get(sid) ? MAX_DAMAGE.get(sid) : ar;
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
            int sid = 1003;
            ar = ar > MAX_DAMAGE.get(sid) ? MAX_DAMAGE.get(sid) : ar;
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
            int sid = 1004;
            ar = ar > MAX_DAMAGE.get(sid) ? MAX_DAMAGE.get(sid) : ar;
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
            int sid = 1005;
            ar = ar > MAX_DAMAGE.get(sid) ? MAX_DAMAGE.get(sid) : ar;
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
            int sid = 1006;
            ar = ar > MAX_DAMAGE.get(sid) ? MAX_DAMAGE.get(sid) : ar;
            if (lps.get(0).contains("#")) {
                Long l = Long.valueOf(ar);
                String ss = attGho(who, l, true, false, GhostLostBroadcast.KillType.ANQ_ATT);
                return ss;
            } else {
                long whos = Long.parseLong(lps.get(0));
                String end = attPer(who, whos, ar);
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
            int sid = 1007;
            ar = ar > MAX_DAMAGE.get(sid) ? MAX_DAMAGE.get(sid) : ar;
            Object[] os = startAtt(who, ar, lps);
            String sb = (String) os[0];
            boolean used = (boolean) os[1];
            return sb;
        } else {
            return "选择器,过多或过少";
        }
    }

    public void used(long who, int id) {
        for (Map<String, Integer> map : SpringBootResource.getAqBagMapper().selectAq(who)) {
            if (map.get("oid").intValue() == id) {
                Integer num = map.get("num");
                num--;
                if (num > 0) {
                    SpringBootResource.getAqBagMapper().update(num, 0, map.get("id"));
                } else {
                    SpringBootResource.getAqBagMapper().update(num, 1, map.get("id"));
                }
                break;
            }
        }
    }

    public boolean exitsO(int id, long who) {
        for (Map<String, Integer> map : SpringBootResource.getAqBagMapper().selectAq(who)) {
            if (map.get("oid").intValue() == id) return true;
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
                String ss = attGho(who, l, n++ == lps.size(), false, GhostLostBroadcast.KillType.ANQ_ATT);
                if (ss.startsWith("你对"))
                    used = true;
                sb.append(ss).append("\r\n").append("=======================\r\n");
            } else {
                String end = attPer(who, Long.parseLong(whos), ar);
                if (end.equals(NO_H))
                    used = false;
                sb.append(end).append("\r\n").append("======================\r\n");
            }
        }
        return new Object[]{sb.toString(), used};
    }

    public String attPer(long who, long whos, long ar) {
        StringBuilder sb = new StringBuilder();
        if (!isAlive(Long.valueOf(whos))) {
            return NO_H;
        }
        long hps = getInfo(whos).getHp();

        if (hps <= ar)
            sb.append(GameDetailService.beaten(whos, who, hps));
        else
            sb.append(GameDetailService.beaten(whos, who, ar));

        if (!isAlive(Long.valueOf(whos))) {
            int l = (int) randLong(250, 0.7f, 1.0f);
            putPerson(getInfo(who).addGold((long) l
                    , new TradingRecord()
                            .setType1(TradingRecord.Type1.add)
                            .setType0(TradingRecord.Type0.gold)
                            .setTo(-1)
                            .setMain(who)
                            .setFrom(who)
                            .setDesc("击败" + whos)
                            .setMany(l)
            ));
            return "你对ta 造成了:" + ar + "点伤害" + sb + "\r\nta已经无状态,你从他身上摸到" + l + "个金魂币";
        } else {
            return "你对ta 造成了:" + ar + "点伤害" + sb;
        }
    }
}
