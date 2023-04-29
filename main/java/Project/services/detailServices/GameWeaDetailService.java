package Project.services.detailServices;


import Project.aSpring.SpringBootResource;
import Project.broadcast.game.GhostLostBroadcast;
import Project.commons.TradingRecord;
import Project.commons.rt.ResourceSet;
import Project.controllers.gameControllers.GameConditionController;
import Project.services.detailServices.roles.DamageType;
import io.github.kloping.MySpringTool.annotations.Entity;
import io.github.kloping.mirai0.commons.PersonInfo;
import io.github.kloping.mirai0.unitls.Tools.Tool;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static Project.commons.rt.CommonSource.percentTo;
import static Project.commons.rt.ResourceSet.FinalString.ERR_TIPS;
import static Project.controllers.auto.ControllerSource.challengeDetailService;
import static Project.controllers.auto.ControllerSource.playerBehavioralManager;
import static Project.dataBases.GameDataBase.*;
import static Project.services.detailServices.GameSkillDetailService.addShield;
import static Project.services.detailServices.ac.GameJoinDetailService.attGho;
import static io.github.kloping.mirai0.unitls.Tools.GameTool.isAlive;

/**
 * @author github-kloping
 */
@Entity
public class GameWeaDetailService {
    public static final List<String> WEAPONS = new ArrayList<>();
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
    }

    private synchronized void initAqs() {
        if (WEAPONS.isEmpty()) {
            for (int id : ID_2_NAME_MAPS.keySet()) {
                boolean k1 = (id > 1000 && id < 1200);
                boolean k2 = (id >= 124 && id <= 127);
                if (k1 || k2) {
                    WEAPONS.add(getNameById(id));
                }
            }
        }
    }

    public synchronized String useAq(List<String> lps, long who, String name) {
        if (WEAPONS.isEmpty()) initAqs();
        if (!WEAPONS.contains(name)) return ERR_TIPS;
        if (GameConditionController.CONDITIONING.containsKey(who))
            return "遇境中...";
        int id = NAME_2_ID_MAPS.get(name);
        if (!exitsO(id, who)) return "你没有'" + name + "'或已损坏";
        long at = getInfo(who).getAk1();
        if (at > System.currentTimeMillis()) {
            return String.format(ResourceSet.FinalFormat.ATT_WAIT_TIPS, Tool.INSTANCE.getTimeTips(at));
        }
        if (challengeDetailService.isTemping(who)) {
            getInfo(who).setAk1(System.currentTimeMillis() + playerBehavioralManager.getAttPost(who) * 2).apply();
        } else {
            getInfo(who).setAk1(System.currentTimeMillis() + playerBehavioralManager.getAttPost(who) / 3).apply();
        }
        try {
            Method method = CLA.getMethod("use" + (id), List.class, long.class);
            String mes = (String) method.invoke(this, lps, who);
            used(who, id);
            return mes;
        } catch (Exception e) {
            e.printStackTrace();
            return "使用失败";
        }
    }

    /**
     * 诸葛神弩
     *
     * @return
     */
    public String use1001(List<String> lps, long who) {
        if (lps.size() == 1) {
            long ar = (long) (500 + (getInfo(who).att() * 0.9f));
            PersonInfo pInfo = getInfo(who);
            int sid = 1001;
            ar = ar > MAX_DAMAGE.get(sid) ? MAX_DAMAGE.get(sid) : ar;
            if (lps.get(0).contains("#")) {
                Long l = Long.valueOf(ar);
                String ss = attGho(who, l, DamageType.AD, true, GhostLostBroadcast.KillType.ANQ_ATT);
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
            long ar = (long) (getInfo(who).att() * 0.6f);
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
            long ar = (long) (1500 + getInfo(who).att() * 0.45f);
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
            long ar = (long) (getInfo(who).att() * 0.65f);
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
            long ar = (long) (3000 + (getInfo(who).att() * 2.8f));
            int sid = 1006;
            ar = ar > MAX_DAMAGE.get(sid) ? MAX_DAMAGE.get(sid) : ar;
            if (lps.get(0).contains("#")) {
                Long l = Long.valueOf(ar);
                String ss = attGho(who, l, DamageType.AD, true, GhostLostBroadcast.KillType.ANQ_ATT);
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
            long ar = (long) (4500 + getInfo(who).att() * 0.72f + getInfo(who).getLevel() * 10);
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

    /**
     * 魂导护盾
     *
     * @return
     */
    public String use124(List<String> lps, long who) {
        long v2 = percentTo(50, getInfo(who).getHpL());
        addShield(who, v2);
        return "使用成功";
    }

    /**
     * 高级魂导护盾
     *
     * @return
     */
    public String use125(List<String> lps, long who) {
        long v2 = percentTo(100, getInfo(who).getHpL());
        addShield(who, v2);
        return "使用成功";
    }

    /**
     * 魂导炮
     *
     * @return
     */
    public String use126(List<String> lps, long who) {
        int num = lps.size();
        StringBuilder sb = new StringBuilder();
        if (num < 2 && num > 0) {
            long att = percentTo(50, getInfo(who).getAtt());
            for (String lp : lps) {
                Long w2 = lp.equals("#") ? -2L : Long.parseLong(lp);
                GameDetailServiceUtils.attGhostOrMan(sb, who, w2, att);
            }
        }
        return sb.toString();
    }

    /**
     * 高级魂导炮
     *
     * @return
     */
    public String use127(List<String> lps, long who) {
        int num = lps.size();
        StringBuilder sb = new StringBuilder();
        if (num < 2 && num > 0) {
            long att = percentTo(100, getInfo(who).getAtt());
            for (String lp : lps) {
                Long w2 = lp.equals("#") ? -2L : Long.parseLong(lp);
                GameDetailServiceUtils.attGhostOrMan(sb, who, w2, att);
            }
        }
        return sb.toString();
    }

    public void used(long who, int id) {
        for (Map<String, Integer> map : SpringBootResource.getAqBagMapper().selectAq(who)) {
            if (map.get("oid").intValue() == id) {
                Integer num = map.get("num");
                num--;
                if (num > 0) {
                    SpringBootResource.getAqBagMapper().update(num, 0, map.get("id"));
                } else {
                    SpringBootResource.getAqBagMapper().delete(map.get("id"));
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
                String ss = attGho(who, l, DamageType.AD, n++ == lps.size(), GhostLostBroadcast.KillType.ANQ_ATT);
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
            sb.append(GameDetailService.beaten(whos, who, hps, DamageType.AD));
        else
            sb.append(GameDetailService.beaten(whos, who, ar, DamageType.AD));

        if (!isAlive(Long.valueOf(whos))) {
            int l = (int) Tool.INSTANCE.randLong(250, 0.7f, 1.0f);
            getInfo(who).addGold((long) l
                    , new TradingRecord()
                            .setType1(TradingRecord.Type1.add)
                            .setType0(TradingRecord.Type0.gold)
                            .setTo(-1)
                            .setMain(who)
                            .setFrom(who)
                            .setDesc("击败" + whos)
                            .setMany(l)
            ).apply();
            return "你对ta 造成了:" + ar + "点伤害" + sb + "\r\nta已经无状态,你从他身上摸到" + l + "个金魂币";
        } else {
            return "你对ta 造成了:" + ar + "点伤害" + sb;
        }
    }
}
