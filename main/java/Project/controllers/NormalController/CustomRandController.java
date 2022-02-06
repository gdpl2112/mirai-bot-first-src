package Project.controllers.NormalController;

import io.github.kloping.mirai0.Entitys.Group;
import io.github.kloping.mirai0.Entitys.User;
import io.github.kloping.mirai0.Entitys.eEntitys.CustomElement;
import io.github.kloping.mirai0.Entitys.eEntitys.CustomReplyGroup;
import Project.broadcast.PicBroadcast;
import Project.services.detailServices.CustomRandReplyService;
import io.github.kloping.mirai0.Main.ITools.MessageTools;
import io.github.kloping.MySpringTool.annotations.*;
import io.github.kloping.MySpringTool.exceptions.NoRunException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static io.github.kloping.mirai0.Main.Resource.println;

/**
 * @author github-kloping
 * @version 1.0
 */
@Controller
public class CustomRandController {

    @Before
    public void before(User qq) throws NoRunException {
    /*    if (qq.getId() == Long.parseLong(superQ)) {
            println("超级权限执行...");
            return;
        } else if (!DataBase.isFather(qq.getId())) {
            throw new NoRunException("无权限");
        }
    */
        throw new NoRunException("not Open");
    }

    private final Map<Long, CustomReplyGroup> rands = new ConcurrentHashMap<>();
    private final Map<Long, PicBroadcast.PicReceiver> recs = new ConcurrentHashMap<>();

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
                    MessageTools.sendMessageInGroupWithAt("appened", gid, qid);
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

    @AutoStand
    CustomRandReplyService service;

    @Action("/applyRand")
    public Object apply(User user) {
        return service.save(rands.get(user.getId())) ? "ok" : "err";
    }
}
