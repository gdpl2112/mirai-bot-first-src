package Project.controllers.recr;

import io.github.kloping.MySpringTool.annotations.Action;
import io.github.kloping.MySpringTool.annotations.AllMess;
import io.github.kloping.MySpringTool.annotations.Before;
import io.github.kloping.MySpringTool.annotations.Controller;
import io.github.kloping.MySpringTool.exceptions.NoRunException;
import io.github.kloping.map.MapUtils;
import io.github.kloping.mirai0.Main.ITools.MemberTools;
import io.github.kloping.mirai0.commons.Group;
import io.github.kloping.mirai0.commons.User;
import net.mamoe.mirai.contact.Member;

import java.util.HashMap;
import java.util.Map;

import static Project.controllers.auto.ControllerTool.opened;
import static Project.controllers.auto.TimerController.ZERO_RUNS;
import static io.github.kloping.mirai0.Main.Resource.bot;
import static io.github.kloping.mirai0.Main.Resource.println;
import static io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet.FinalFormat.WHERE_MEMBER_IS_MY_WIFE;
import static io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet.FinalString.CLOSE_STR;
import static io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet.FinalString.OPEN_STR;
import static io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet.FinalValue.NOT_OPEN_NO_RUN_EXCEPTION;
import static io.github.kloping.mirai0.unitls.Tools.Tool.getRandT;

/**
 * @author github.kloping
 */
@Controller
public class Controller0 {

    public static final Map<Long, Map<Long, Long>> WIFE = new HashMap<>();

    static {
        ZERO_RUNS.add(() -> WIFE.clear());
    }

    public Controller0() {
        println(this.getClass().getSimpleName() + "构建");
    }

    @Before
    public void before(@AllMess String mess, Group group) throws NoRunException {
        if (mess.contains(OPEN_STR) || mess.contains(CLOSE_STR)) {
            return;
        }
        if (!opened(group.getId(), this.getClass())) {
            throw NOT_OPEN_NO_RUN_EXCEPTION;
        }
    }

    @Action("哪个群友是我老婆")
    public String s0(Group group, User user) {
        Long qid = null;
        if (WIFE.containsKey(group.getId())) {
            if (WIFE.get(group.getId()).containsKey(user.getId())) {
                qid = WIFE.get(group.getId()).get(user.getId());
            }
        }
        if (qid == null) {
            qid = getRandQid(group);
            MapUtils.append(WIFE, group.getId(), user.getId(), qid);
        }
        String s0 = toView(user.getId(), qid, group);
        return s0;
    }

    private String toView(long id, Long qid, Group group) {
        String name0 = MemberTools.getNameFromGroup(id, group);
        String name1 = MemberTools.getNameFromGroup(qid, group);
        return String.format(WHERE_MEMBER_IS_MY_WIFE,
                qid, name1, qid
        );
    }

    private long getRandQid(Group group) {
        Member member;
        member = (Member) getRandT(bot.getGroup(group.getId()).getMembers().toArray(new Member[0]));
        long qid = member.getId();
        if (WIFE.containsKey(group.getId())) {
            if (WIFE.get(group.getId()).values().contains(qid)) {
                return getRandQid(group);
            }
        }
        return qid;
    }
}
