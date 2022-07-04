package Project.controllers.normalController;

import Project.broadcast.PicBroadcast;
import Project.services.detailServices.CustomRandReplyService;
import io.github.kloping.MySpringTool.annotations.*;
import io.github.kloping.MySpringTool.exceptions.NoRunException;
import io.github.kloping.mirai0.Main.ITools.MessageTools;
import io.github.kloping.mirai0.commons.Group;
import io.github.kloping.mirai0.commons.User;
import io.github.kloping.mirai0.commons.eEntitys.CustomElement;
import io.github.kloping.mirai0.commons.eEntitys.CustomReplyGroup;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author github-kloping
 * @version 1.0
 */
@Controller
public class CustomRandController {

    private final Map<Long, CustomReplyGroup> rands = new ConcurrentHashMap<>();
    private final Map<Long, PicBroadcast.PicReceiver> recs = new ConcurrentHashMap<>();
    @AutoStand
    CustomRandReplyService service;

    @Before
    public void before(User qq) throws NoRunException {
        throw new NoRunException("not Open");
    /*    if (qq.getId() == Long.parseLong(superQ)) {
            println("超级权限执行...");
            return;
        } else if (!DataBase.isFather(qq.getId())) {
            throw new NoRunException("无权限");
        }
    */
    }

    @Action("/createRand")
    public Object create(Group group, User user) {
        CustomReplyGroup crg = new CustomReplyGroup();
        crg.setGid(group.getId());
        crg.setQid(user.getId());
        crg.setVisible(true);
        return "create: " + rands.put(user.getId(), crg);
    }

    @Action("/setRandKey<.+=>str>")
    public Object setKey(@Param("str") String str, User user) {
        try {
            CustomReplyGroup crg = rands.get(user.getId());
            CustomElement ce = new CustomElement();
            ce.setTime(System.currentTimeMillis());
            ce.setQid(user.getId());
            ce.setContext(str);
            ce.setWeight(1);
            crg.getKeys().add(ce);
            return "setKey: " + str;
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @Action("/startAppendRandV")
    public Object append(User user) {
        PicBroadcast.PicReceiver picReceiver = new PicBroadcast.PicReceiverOnce() {
            @Override
            public Object onReceive(long qid, long gid, String pic, Object[] objects) {
                if (user.getId() == qid) {
                    CustomReplyGroup crg = rands.get(user.getId());
                    CustomElement ce = new CustomElement();
                    ce.setTime(System.currentTimeMillis());
                    ce.setQid(user.getId());
                    ce.setContext(pic);
                    ce.setWeight(1);
                    crg.getValues().add(ce);
                    MessageTools.instance.sendMessageInGroupWithAt("appened", gid, qid);
                }
                return null;
            }
        };
        recs.put(user.getId(), picReceiver);
        PicBroadcast.INSTANCE.add(picReceiver);
        return "started";
    }

    @Action("/endAppendRandV")
    public Object end(User user) {
        PicBroadcast.INSTANCE.remove(recs.get(user.getId()));
        return "ended";
    }

    @Action("/applyRand")
    public Object apply(User user) {
        return service.save(rands.get(user.getId())) ? "ok" : "err";
    }
}
