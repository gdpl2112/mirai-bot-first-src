package io.github.kloping.kzero.bot.controllers;

import io.github.kloping.MySpringTool.annotations.*;
import io.github.kloping.kzero.main.api.KZeroBot;
import io.github.kloping.kzero.main.api.KZeroBotAdapter;
import io.github.kloping.kzero.main.api.MessagePack;
import io.github.kloping.kzero.utils.Utils;
import io.github.kloping.rand.RandomUtils;
import io.github.kloping.spt.RedisOperate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author github.kloping
 */
@Controller
public class ControllerWhereIsMyWife {
    /**
     * 群号 => qid => qid
     */
    @AutoStand(id = "1")
    public RedisOperate<Map<String, String>> WIFIES;

    /**
     * 周一刷新
     */
    @CronSchedule("0 0 0 ? * 1 *")
    public void interest() {
        WIFIES.execute((e) -> {
            e.flushDB();
        });
    }

    public static final String WHERE_MEMBER_IS_MY_WIFE = "今天你的群友老婆是<pic:%s> 【%s】(%s) ";
    public static final String WHERE_MEMBER_IS_MY_WIFE0 = "强娶成功ta是你的了!\n今天你的群友老婆是<pic:%s> 【%s】(%s) ";
    public static final String WHERE_MEMBER_IS_MY_WIFE1 = "今天你被娶了!你的群友老公是<pic:%s> 【%s】(%s) ";

    @Action(value = "哪个群友是我老婆", otherName = {"娶群友"})
    public String s0(MessagePack pack, KZeroBot bot) {
        try {
            String aid = null;
            String sid = pack.getSenderId();
            String gid = pack.getSubjectId();
            Map<String, String> map = WIFIES.getValue(gid);
            if (map == null) map = new HashMap<>();
            if (map.containsKey(sid)) {
                aid = map.get(sid);
                return toView(aid, bot.getAdapter());
            } else if (map.values().contains(sid)) {
                for (Map.Entry<String, String> e0 : map.entrySet()) {
                    if (e0.getValue().equals(sid)) {
                        return toView1(e0.getKey(), bot.getAdapter());
                    }
                }
            } else {
                int size = bot.getAdapter().getMembers(pack.getSubjectId()).size();
                if (size <= 1) throw new RuntimeException("人数不足");
                if (map.size() * 2 >= size + 1)
                    throw new RuntimeException("群员不足");
                aid = getRandQid(sid, pack, bot);
                if (map.values().contains(aid)) return s0(pack, bot);
                map.put(sid, aid);
                WIFIES.setValue(gid, map);
                String s0 = toView(aid, bot.getAdapter());
                return s0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "娶群友失败!";
    }

    @Action("重娶群友")
    public String s1(MessagePack pack, KZeroBot bot) {
        Map map = WIFIES.getValue(pack.getSubjectId());
        if (map != null) map.remove(pack.getSenderId());
        return s0(pack, bot);
    }

    @Action("离婚")
    public String sout(MessagePack pack, KZeroBot bot) {
        String sid = pack.getSenderId();
        String gid = pack.getSubjectId();
        Map<String, String> map = WIFIES.getValue(gid);
        if (map == null) map = new HashMap<>();
        String asid = null;
        for (String s : map.keySet()) {
            if (map.get(s).equals(sid)) {
                asid = s;
                break;
            }
        }
        if (asid != null) {
            map.remove(asid);
            return "成功!";
        } else return "无需'离婚'或失败!";
    }

    @Action("强娶.*?")
    public String s1(@AllMess String msg, MessagePack pack, KZeroBot bot) {
        String aid = Utils.getAtFormat(msg);
        if (aid == null) return "娶群友说明:当未拥有群友老婆时首次娶群友必定成功!(๑*◡*๑)" +
                "\n但当已经拥有群友老婆时成功率大幅下降 当强娶对象已经被其他群友'娶'或'已娶'时成功率进一步下降￣へ￣" +
                "\n群友老婆最长生效一星期每周末刷新(〃'▽'〃)" +
                "\n其中有且仅'老婆'可随时使用'离婚'来取消'关系'╥﹏╥";
        String sid = pack.getSenderId();
        String gid = pack.getSubjectId();
        Map<String, String> map = WIFIES.getValue(gid);
        if (map == null) map = new HashMap<>();
        if (map.containsKey(sid) || map.values().contains(aid)) {
            map.remove(sid);
            if (RandomUtils.RANDOM.nextInt(3) == 0) {
                if (map.values().contains(aid)) {
                    if (RandomUtils.RANDOM.nextInt(2) == 0) {
                        String asid = null;
                        for (String s : map.keySet()) {
                            if (map.get(s).equals(aid)) {
                                asid = s;
                                break;
                            }
                        }
                        if (asid != null) {
                            map.remove(asid);
                        }
                        return qqNow(bot, map, sid, aid, gid);
                    }
                } else {
                    return qqNow(bot, map, sid, aid, gid);
                }
            }
        } else if (map.values().contains(sid)) {
            return s0(pack, bot);
        } else {
            return qqNow(bot, map, sid, aid, gid);
        }
        return "强娶失败!";
    }

    private String qqNow(KZeroBot bot, Map<String, String> map, String sid, String aid, String gid) {
        map.remove(aid);
        map.put(sid, aid);
        WIFIES.setValue(gid, map);
        KZeroBotAdapter adapter = bot.getAdapter();
        return String.format(WHERE_MEMBER_IS_MY_WIFE0, adapter.getAvatarUrl(aid), adapter.getNameCard(aid), aid);
    }

    private String toView(String aid, KZeroBotAdapter adapter) {
        return String.format(WHERE_MEMBER_IS_MY_WIFE, adapter.getAvatarUrl(aid), adapter.getNameCard(aid), aid);
    }

    private String toView1(String aid, KZeroBotAdapter adapter) {
        return String.format(WHERE_MEMBER_IS_MY_WIFE1, adapter.getAvatarUrl(aid), adapter.getNameCard(aid), aid);
    }

    private String getRandQid(String sid, MessagePack pack, KZeroBot bot) {
        List<String> list = bot.getAdapter().getMembers(pack.getSubjectId());
        String aid = null;
        Map<String, String> map = WIFIES.getValue(pack.getSubjectId());
        if (map == null) map = new HashMap<>();
        while (true) {
            aid = list.get(RandomUtils.RANDOM.nextInt(list.size()));
            if (map.containsValue(aid)) continue;
            if (map.containsKey(aid)) continue;
            else break;
        }
        return aid;
    }
}