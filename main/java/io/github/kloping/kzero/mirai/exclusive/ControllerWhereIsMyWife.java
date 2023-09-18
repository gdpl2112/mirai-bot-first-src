package io.github.kloping.kzero.mirai.exclusive;

import io.github.kloping.MySpringTool.annotations.*;
import io.github.kloping.MySpringTool.exceptions.NoRunException;
import io.github.kloping.kzero.main.ResourceSet;
import io.github.kloping.kzero.main.api.KZeroBot;
import io.github.kloping.kzero.main.api.KZeroBotAdapter;
import io.github.kloping.kzero.main.api.MessagePack;
import io.github.kloping.map.MapUtils;
import io.github.kloping.rand.RandomUtils;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.Member;
import net.mamoe.mirai.event.events.GroupMessageEvent;

import java.util.HashMap;
import java.util.Map;

/**
 * @author github.kloping
 */
@Controller
public class ControllerWhereIsMyWife {
    @Before
    public void before(@AllMess String msg, KZeroBot kZeroBot, MessagePack pack) {
        if (!(kZeroBot.getSelf() instanceof Bot)) throw new NoRunException("mirai-bot专属扩展");
    }

    /**
     * 群号 => qid => qid
     */
    public static final Map<String, Map<String, String>> WIFE = new HashMap<>();

    @CronSchedule("0 1 0 * * ? ")
    public void interest() {
        WIFE.clear();
    }

    @Action(value = "哪个群友是我老婆", otherName = {"娶群友"})
    public String s0(MessagePack pack, KZeroBot bot) {
        String qid = null;
        String uid = pack.getSenderId();
        String gid = pack.getSubjectId();
        if (WIFE.containsKey(gid)) {
            Map<String, String> map = WIFE.get(gid);
            if (map.containsKey(uid)) {
                qid = WIFE.get(gid).get(uid);
            } else if (map.values().contains(uid)) {
                for (Map.Entry<String, String> e0 : map.entrySet()) {
                    if (e0.getValue() == uid) {
                        return toView1(e0.getKey(), e0.getKey(), bot.getAdapter());
                    }
                }
            }
        }
        if (qid == null) {
            qid = getRandQid(uid, pack);
            MapUtils.append(WIFE, gid, uid, qid);
        }
        String s0 = toView(uid, qid, bot.getAdapter());
        return s0;
    }

    @Action("重娶群友")
    public String s1(MessagePack pack, KZeroBot bot) {
        WIFE.get(pack.getSubjectId()).remove(pack.getSenderId());
        return s0(pack, bot);
    }

    private String toView(String id, String qid, KZeroBotAdapter adapter) {
        String name0 = adapter.getNameCard(id);
        String name1 = adapter.getNameCard(qid);
        return String.format(ResourceSet.FinalFormat.WHERE_MEMBER_IS_MY_WIFE, qid, name1, qid);
    }

    private String toView1(String id, String qid, KZeroBotAdapter adapter) {
        String name0 = adapter.getNameCard(id);
        String name1 = adapter.getNameCard(qid);
        return String.format(ResourceSet.FinalFormat.WHERE_MEMBER_IS_MY_WIFE1, qid, name1, qid);
    }

    private String getRandQid(String mid, MessagePack pack) {
        GroupMessageEvent event = (GroupMessageEvent) pack.getRaw();
        Member member;
        member = (Member) RandomUtils.getRand(event.getGroup().getMembers().toArray(new Member[0]));
        String qid = String.valueOf(member.getId());
        if (WIFE.containsKey(pack.getSubjectId())) {
            if (WIFE.get(pack.getSubjectId()).keySet().contains(qid) || WIFE.get(pack.getSubjectId()).values().contains(qid)) {
                return getRandQid(mid, pack);
            }
        }
        if (qid == mid) return getRandQid(mid, pack);
        return qid;
    }
}