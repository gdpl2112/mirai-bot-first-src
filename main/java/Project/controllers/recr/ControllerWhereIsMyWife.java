package Project.controllers.recr;

import io.github.kloping.MySpringTool.annotations.Action;
import io.github.kloping.MySpringTool.annotations.AllMess;
import io.github.kloping.MySpringTool.annotations.Before;
import io.github.kloping.MySpringTool.annotations.Controller;
import io.github.kloping.MySpringTool.exceptions.NoRunException;
import io.github.kloping.map.MapUtils;
import io.github.kloping.mirai0.Main.iutils.MemberUtils;
import io.github.kloping.mirai0.commons.SpGroup;
import io.github.kloping.mirai0.commons.SpUser;
import io.github.kloping.mirai0.unitls.Tools.Tool;
import net.mamoe.mirai.contact.Member;

import java.util.HashMap;
import java.util.Map;

import static Project.controllers.auto.ControllerTool.opened;
import static Project.controllers.auto.TimerController.ZERO_RUNS;
import static io.github.kloping.mirai0.Main.BootstarpResource.BOT;
import static io.github.kloping.mirai0.Main.BootstarpResource.println;
import static io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet.FinalFormat.WHERE_MEMBER_IS_MY_WIFE;
import static io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet.FinalFormat.WHERE_MEMBER_IS_MY_WIFE1;
import static io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet.FinalString.CLOSE_STR;
import static io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet.FinalString.OPEN_STR;
import static io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet.FinalValue.NOT_OPEN_NO_RUN_EXCEPTION;

/**
 * @author github.kloping
 */
@Controller
public class ControllerWhereIsMyWife {

    /**
     * 群号 => qid => qid
     */
    public static final Map<Long, Map<Long, Long>> WIFE = new HashMap<>();

    static {
        ZERO_RUNS.add(() -> {
//            RedisOperate<Map<Long, Long>> operate = ControllerSource.firstController.redisOperate;
//            if (operate != null) {
//                operate.execute((e) -> {
//                    e.keys("*").forEach((k) -> {
//                        e.del(k);
//                    });
//                });
//            }
            WIFE.clear();
        });
    }

    public ControllerWhereIsMyWife() {
        println(this.getClass().getSimpleName() + "构建");
    }

    @Before
    public void before(@AllMess String mess, SpGroup group) throws NoRunException {
        if (mess.contains(OPEN_STR) || mess.contains(CLOSE_STR)) {
            return;
        }
        if (!opened(group.getId(), this.getClass())) {
            throw NOT_OPEN_NO_RUN_EXCEPTION;
        }
    }

    @Action(value = "哪个群友是我老婆", otherName = {"娶群友"})
    public String s0(SpGroup group, SpUser user) {
        Long qid = null;
        long uid = user.getId();
        long gid = group.getId();
        if (WIFE.containsKey(gid)) {
            Map<Long, Long> map = WIFE.get(gid);
            if (map.containsKey(uid)) {
                qid = WIFE.get(gid).get(uid);
            } else if (map.values().contains(uid)) {
                for (Map.Entry<Long, Long> e0 : map.entrySet()) {
                    if (e0.getValue() == uid) {
                        return toView1(e0.getKey(), e0.getKey(), group);
                    }
                }
            }
        }
        if (qid == null) {
            qid = getRandQid(uid, group);
            MapUtils.append(WIFE, gid, uid, qid);
        }
        String s0 = toView(uid, qid, group);
        return s0;
    }

    private String toView(long id, Long qid, SpGroup group) {
        String name0 = MemberUtils.getNameFromGroup(id, group);
        String name1 = MemberUtils.getNameFromGroup(qid, group);
        return String.format(WHERE_MEMBER_IS_MY_WIFE, qid, name1, qid);
    }

    private String toView1(long id, Long qid, SpGroup group) {
        String name0 = MemberUtils.getNameFromGroup(id, group);
        String name1 = MemberUtils.getNameFromGroup(qid, group);
        return String.format(WHERE_MEMBER_IS_MY_WIFE1, qid, name1, qid);
    }

    private long getRandQid(long mid, SpGroup group) {
        Member member;
        member = (Member) Tool.INSTANCE.getRandT(BOT.getGroup(group.getId()).getMembers());
        long qid = member.getId();
        if (WIFE.containsKey(group.getId())) {
            if (WIFE.get(group.getId()).keySet().contains(qid) || WIFE.get(group.getId()).values().contains(qid)) {
                return getRandQid(mid, group);
            }
        }
        if (qid == mid) return getRandQid(mid, group);
        return qid;
    }
}
