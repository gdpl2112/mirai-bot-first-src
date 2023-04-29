package Project.controllers.auto;

import Project.commons.SpGroup;
import Project.commons.SpUser;
import Project.controllers.normalController.ScoreController;
import Project.dataBases.DataBase;
import Project.interfaces.httpApi.Fuyhi;
import Project.interfaces.httpApi.KlopingWeb;
import io.github.kloping.MySpringTool.annotations.*;
import io.github.kloping.MySpringTool.exceptions.NoRunException;
import io.github.kloping.date.FrameUtils;
import io.github.kloping.mirai0.Main.BootstarpResource;
import io.github.kloping.mirai0.Main.iutils.MessageUtils;
import io.github.kloping.mirai0.commons.entity.PayOut;
import io.github.kloping.mirai0.commons.entity.PayOutM;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author github.kloping
 */
@Controller
public class Controller0 {

    public static final Map<Integer, Integer> Q2C = new HashMap<>();
    /**
     * 允许被充值的机器人
     */
    private static final Set<Long> ALL = new HashSet<>();
    private static final Long BOT_ID = 930204019L;
    private static final Long SUPER_Q = 3474006766L;
    private static String PS_KEY;
    private static String SKEY;

    static {
        ALL.add(291841860L);
        ALL.add(3597552450L);
        ALL.add(392801250L);
        ALL.add(3565754729L);
        ALL.add(930204019L);
    }

    static {
        Q2C.put(1, 52000);
        Q2C.put(2, 110000);
        Q2C.put(3, 160000);
        Q2C.put(5, 325000);
        Q2C.put(10, 628000);
        Q2C.put(20, 1215000);
    }

    @AutoStand
    public KlopingWeb api;
    public Map<Long, PayOutM> longPayOutMap = new ConcurrentHashMap<>();
    @AutoStand
    KlopingWeb kloping;
    @AutoStand
    ScoreController scoreController;
    @AutoStand
    Fuyhi tianYi;

    {
        FrameUtils.SERVICE.scheduleWithFixedDelay(() -> {
            Iterator<Long> iterator = longPayOutMap.keySet().iterator();
            while (iterator.hasNext()) {
                long qid = iterator.next();
                PayOutM outM = longPayOutMap.get(qid);
                if (outM.getTime() < System.currentTimeMillis()) {
                    test(outM);
                    longPayOutMap.remove(qid);
                    MessageUtils.INSTANCE.sendMessageInGroupWithAt("订单过期", outM.getGid(), outM.getQid());
                }
            }
        }, 3, 3, TimeUnit.SECONDS);
    }

    @Before
    public void before(@AllMess String mess, SpGroup group, SpUser qq) throws NoRunException {
        if (!ALL.contains(BootstarpResource.BOT.getId())) {
            throw new NoRunException("not open");
        }
        if (!DataBase.canBack(group.getId())) throw new NoRunException("not open");
    }

    private void tryGetPsKey() {
        String psKey = kloping.get("pskey-" + BOT_ID, "4432120");
        String sKey = kloping.get("skey-" + BOT_ID, "4432120");
        PS_KEY = psKey;
        SKEY = sKey;
    }

    private void test(PayOutM outM) {
        try {
            test0(outM);
            test0(outM);
            test0(outM);
        } catch (Exception ignored) {
        }
    }

    private synchronized void test0(PayOutM m) {
        if (api.pay(SKEY, PS_KEY, m.getQid(), BOT_ID, 2, m.getValue().floatValue(), "充值支付", m.getOut().getPayid(), m.getGid()).getText().equals("已支付")) {
            DataBase.addScore(Q2C.get(m.getValue()), m.getQid());
            longPayOutMap.remove(m.getQid());
            String s0 = m.getQid() + "在群:" + m.getGid() + "完成订单:" + m.getValue();
            BootstarpResource.BOT.getFriend(SUPER_Q).sendMessage(s0);
            MessageUtils.INSTANCE.sendMessageInGroupWithAt(scoreController.selectScore(m.getQid()), m.getGid(), m.getQid());
            throw new RuntimeException();
        }
    }

    @Action("充值<.+=>str>")
    public synchronized Object pay0(@Param("str") String str, SpUser user, SpGroup group) {
        long senderId = user.getId();
        long groupId = group.getId();
        long botId = BOT_ID;
        Integer v = Integer.valueOf(str);
        tryGetPsKey();
        if (Q2C.containsKey(v)) {
            if (longPayOutMap.containsKey(senderId)) {
                return "\n订单处理中...";
            } else {
                PayOut out = api.pay(SKEY, PS_KEY, senderId, BOT_ID, 1, v.floatValue(), "充值支付", "", groupId);
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
    public synchronized String pay1(SpUser user, SpGroup group) {
        long senderId = user.getId();
        long groupId = group.getId();
        long botId = BOT_ID;
        if (longPayOutMap.containsKey(senderId)) {
            PayOutM m = longPayOutMap.get(senderId);
            PayOut out = api.pay(SKEY, PS_KEY, senderId, BOT_ID, 3, m.getValue().floatValue(), "充值支付", m.getOut().getPayid(), groupId);
            longPayOutMap.remove(senderId);
            return out.getText();
        } else {
            return "未发现订单";
        }
    }

    @Action("完成订单")
    public synchronized Object pay2(SpUser user, SpGroup group) {
        long senderId = user.getId();
        long groupId = group.getId();
        long botId = BOT_ID;
        if (longPayOutMap.containsKey(senderId)) {
            PayOutM m = longPayOutMap.get(senderId);
            PayOut out = api.pay(SKEY, PS_KEY, senderId, BOT_ID, 2, m.getValue().floatValue(), "充值支付", m.getOut().getPayid(), groupId);
            if ("支付成功".equals(out.getText())) {
                DataBase.addScore(Q2C.get(m.getValue()), senderId);
                longPayOutMap.remove(senderId);
                String s0 = senderId + "在群:" + groupId + "完成订单:" + m.getValue();
                BootstarpResource.BOT.getFriend(SUPER_Q).sendMessage(s0);
                return scoreController.selectScore(senderId);
            } else {
                return out.getText();
            }
        } else {
            return "未发现订单";
        }
    }

    @Action("余额")
    private synchronized Object yue(long senderId) {
        tryGetPsKey();
        if (senderId == SUPER_Q) return tianYi.yuE(BOT_ID, SKEY, PS_KEY);
        else return null;
    }
}