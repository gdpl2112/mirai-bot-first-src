package Project.controllers.auto;

import Project.controllers.normalController.ScoreController;
import Project.dataBases.DataBase;
import Project.interfaces.http_api.Fuyhi;
import Project.interfaces.http_api.KingApi;
import Project.interfaces.http_api.KlopingWeb;
import Project.interfaces.http_api.XiaoQianDTianYi;
import io.github.kloping.MySpringTool.annotations.*;
import io.github.kloping.MySpringTool.exceptions.NoRunException;
import io.github.kloping.date.FrameUtils;
import io.github.kloping.mirai0.Main.ITools.MessageTools;
import io.github.kloping.mirai0.Main.Resource;
import io.github.kloping.mirai0.commons.Group;
import io.github.kloping.mirai0.commons.User;
import io.github.kloping.mirai0.commons.entity.PayOut;
import io.github.kloping.mirai0.commons.entity.PayOutM;
import net.mamoe.mirai.event.events.GroupMessageEvent;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author github.kloping
 */
@Controller
public class Controller0 {

    private static final Set<Long> all = new HashSet<>();

    static {
        all.add(291841860L);
        all.add(3597552450L);
        all.add(392801250L);
        all.add(3565754729L);
    }

    @Before
    public void before(@AllMess String mess, Group group, User qq) throws NoRunException {
        if (!all.contains(Resource.BOT.getId())) {
            throw new NoRunException("not open");
        }
        if (!DataBase.canBack(group.getId()))
            throw new NoRunException("not open");
    }

    private static final Long BOT_ID = 930204019L;
    private static final Long SUPER_Q = 3474006766L;
    private static String PS_KEY;
    private static String SKEY;

    @AutoStand
    KlopingWeb kloping;

    private void tryGetPsKey() {
        String psKey = kloping.get("pskey-" + BOT_ID, "4432120");
        String sKey = kloping.get("skey-" + BOT_ID, "4432120");
        PS_KEY = psKey;
        SKEY = sKey;
    }

    @AutoStand
    public XiaoQianDTianYi api;

    public Map<Long, PayOutM> longPayOutMap = new ConcurrentHashMap<>();

    {
        FrameUtils.SERVICE.scheduleWithFixedDelay(() -> {
            Iterator<Long> iterator = longPayOutMap.keySet().iterator();
            while (iterator.hasNext()) {
                long qid = iterator.next();
                PayOutM outM = longPayOutMap.get(qid);
                if (outM.getTime() < System.currentTimeMillis()) {
                    test(outM);
                    longPayOutMap.remove(qid);
                    MessageTools.instance.sendMessageInGroupWithAt("订单过期", outM.getGid(), outM.getQid());
                }
            }
        }, 3, 3, TimeUnit.SECONDS);
    }

    public static final Map<Integer, Integer> Q2C = new HashMap<>();

    static {
        Q2C.put(1, 52000);
        Q2C.put(2, 110000);
        Q2C.put(3, 160000);
        Q2C.put(5, 315000);
        Q2C.put(10, 625000);
        Q2C.put(20, 1205000);
    }

    private void test(PayOutM outM) {
        try {
            test0(outM);
            test0(outM);
            test0(outM);
        } catch (Exception ignored) {
        }
    }

    private synchronized void test0(PayOutM outM) {
        if (api.pay("", PS_KEY, 0f,
                outM.getGid(), outM.getQid(), outM.getBid(), outM.getOut().getPay_id(), 2).getText().equals("已支付")) {
            DataBase.addScore(Q2C.get(outM.getValue()), outM.getQid());
            longPayOutMap.remove(outM.getQid());
            String s0 = outM.getQid() + "在群:" + outM.getGid() + "完成订单:" + outM.getValue();
            Resource.BOT.getFriend(SUPER_Q).sendMessage(s0);
            MessageTools.instance.sendMessageInGroupWithAt(scoreController.selectScore(outM.getQid()), outM.getGid(), outM.getQid());
            throw new RuntimeException();
        }
    }

    @Action("充值<.+=>str>")
    public synchronized Object pay0(@Param("str") String str, User user, io.github.kloping.mirai0.commons.Group group) {
        long senderId = user.getId();
        long groupId = group.getId();
        long botId = BOT_ID;
        Integer v = Integer.valueOf(str);
        tryGetPsKey();
        if (Q2C.containsKey(v)) {
            if (longPayOutMap.containsKey(senderId)) {
                return "\n订单处理中...";
            } else {
                PayOut out = api.pay("充值支付", PS_KEY, v.floatValue(), groupId,
                        senderId, botId, "", 1);
                PayOutM m = new PayOutM();
                m.setOut(out);
                m.setGid(groupId);
                m.setQid(senderId);
                m.setTime(System.currentTimeMillis() + 120000);
                m.setBid(botId);
                m.setValue(v);
                longPayOutMap.put(senderId, m);
                return "\n请在2分钟内完成订单\n完成支付请说: 完成订单\n取消订单请说: 取消订单";
            }
        } else {
            return "\n不支持该金额";
        }
    }

    @Action("取消订单")
    public synchronized String pay1(User user, io.github.kloping.mirai0.commons.Group group) {
        long senderId = user.getId();
        long groupId = group.getId();
        long botId = BOT_ID;
        if (longPayOutMap.containsKey(senderId)) {
            PayOutM m = longPayOutMap.get(senderId);
            PayOut out = api.pay("", PS_KEY, 0f,
                    groupId, senderId, botId, m.getOut().getPay_id(), 4);
            longPayOutMap.remove(senderId);
            return out.getText();
        } else {
            return "未发现订单";
        }
    }

    @AutoStand
    ScoreController scoreController;

    @Action("完成订单")
    public synchronized Object pay2(User user, io.github.kloping.mirai0.commons.Group group) {
        long senderId = user.getId();
        long groupId = group.getId();
        long botId = BOT_ID;
        if (longPayOutMap.containsKey(senderId)) {
            PayOutM m = longPayOutMap.get(senderId);
            PayOut out = api.pay("", PS_KEY, 0f,
                    groupId, senderId, botId, m.getOut().getPay_id(), 2);
            if ("已支付".equals(out.getText())) {
                DataBase.addScore(Q2C.get(m.getValue()), senderId);
                longPayOutMap.remove(senderId);
                String s0 = senderId + "在群:" + groupId + "完成订单:" + m.getValue();
                Resource.BOT.getFriend(SUPER_Q).sendMessage(s0);
                return scoreController.selectScore(senderId);
            } else {
                return out.getText();
            }
        } else {
            return "未发现订单";
        }
    }

    @AutoStand
    Fuyhi tianYi;

    @Action("余额")
    private synchronized Object yue(long senderId) {
        tryGetPsKey();
        if (senderId == SUPER_Q) return tianYi.yuE(BOT_ID, SKEY, PS_KEY);
        else return null;
    }
}