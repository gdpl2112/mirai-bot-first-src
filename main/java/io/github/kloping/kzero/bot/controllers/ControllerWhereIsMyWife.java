package io.github.kloping.kzero.bot.controllers;

import io.github.kloping.MySpringTool.annotations.Action;
import io.github.kloping.MySpringTool.annotations.AllMess;
import io.github.kloping.MySpringTool.annotations.Controller;
import io.github.kloping.MySpringTool.annotations.CronSchedule;
import io.github.kloping.kzero.main.api.KZeroBot;
import io.github.kloping.kzero.main.api.KZeroBotAdapter;
import io.github.kloping.kzero.main.api.MessagePack;
import io.github.kloping.kzero.utils.Utils;
import io.github.kloping.rand.RandomUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author github.kloping
 */
@Controller
public class ControllerWhereIsMyWife {

    @CronSchedule("0 1 0 * * ? ")
    public void interest() {
        WIFE.clear();
    }

    /**
     * 群号 => qid => qid
     */
    public static final Map<String, Map<String, String>> WIFE = new HashMap<>();
    public static final String WHERE_MEMBER_IS_MY_WIFE = "今天你的群友老婆是<pic:%s> 【%s】(%s) ";
    public static final String WHERE_MEMBER_IS_MY_WIFE0 = "强娶成功ta是你的了!\n今天你的群友老婆是<pic:%s> 【%s】(%s) ";
    public static final String WHERE_MEMBER_IS_MY_WIFE1 = "今天你被娶了!你的群友老公是<pic:%s> 【%s】(%s) ";

    @Action(value = "哪个群友是我老婆", otherName = {"娶群友"})
    public String s0(MessagePack pack, KZeroBot bot) {
        try {
            String aid = null;
            String sid = pack.getSenderId();
            String gid = pack.getSubjectId();
            Map<String, String> map = WIFE.getOrDefault(gid, new HashMap<>());
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
                map.put(sid, aid);
                WIFE.put(gid, map);
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
        WIFE.getOrDefault(pack.getSubjectId(), new HashMap<>()).remove(pack.getSenderId());
        return s0(pack, bot);
    }

    @Action("强娶.+")
    public String s1(@AllMess String msg, MessagePack pack, KZeroBot bot) {
        String aid = Utils.getAtFormat(msg);
        if (aid == null) return null;
        if (RandomUtils.RANDOM.nextInt(3) == 0) {
            String sid = pack.getSenderId();
            String gid = pack.getSubjectId();
            Map<String, String> map = WIFE.getOrDefault(gid, new HashMap<>());
            map.remove(sid);
            map.remove(aid);
            if (map.values().contains(sid)) {
                for (Map.Entry<String, String> e0 : map.entrySet()) {
                    if (e0.getValue().equals(sid)) {
                        map.remove(e0.getKey());
                    } else if (e0.getValue().equals(aid)) {
                        map.remove(e0.getKey());
                    }
                }
            }
            map.put(sid, aid);
            WIFE.put(gid, map);
            KZeroBotAdapter adapter = bot.getAdapter();
            return String.format(WHERE_MEMBER_IS_MY_WIFE0, adapter.getAvatarUrl(aid), adapter.getNameCard(aid), aid);
        } else return "强娶失败!";
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
        Map<String, String> map = WIFE.getOrDefault(pack.getSubjectId(), new HashMap<>());
        while (true) {
            aid = list.get(RandomUtils.RANDOM.nextInt(list.size()));
            if (map.containsValue(aid)) continue;
            if (map.containsKey(aid)) continue;
            else break;
        }
        return aid;
    }
}