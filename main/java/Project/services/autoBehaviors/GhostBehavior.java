package Project.services.autoBehaviors;

import Project.dataBases.GameDataBase;
import Project.dataBases.skill.SkillDataBase;
import Project.interfaces.Iservice.IGameBoneService;
import Project.interfaces.Iservice.IGameService;
import Project.services.detailServices.GameDetailService;
import Project.services.detailServices.GameJoinDetailService;
import io.github.kloping.MySpringTool.annotations.AutoStand;
import io.github.kloping.MySpringTool.annotations.Entity;
import io.github.kloping.mirai0.Main.ITools.MessageTools;
import io.github.kloping.mirai0.commons.GhostObj;
import io.github.kloping.mirai0.commons.Group;
import io.github.kloping.mirai0.commons.PersonInfo;
import io.github.kloping.mirai0.commons.gameEntitys.SoulAttribute;
import io.github.kloping.mirai0.commons.gameEntitys.base.BaseInfoTemp;
import io.github.kloping.mirai0.unitls.Tools.Tool;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import static Project.dataBases.GameDataBase.putPerson;
import static Project.services.detailServices.GameJoinDetailService.saveGhostObjIn;
import static io.github.kloping.mirai0.commons.resouce_and_tool.CommonSource.percentTo;
import static io.github.kloping.mirai0.commons.resouce_and_tool.CommonSource.toPercent;
import static io.github.kloping.mirai0.unitls.Tools.GameTool.getAllHHBL;

/**
 * @author github-kloping
 */
@Entity
public class GhostBehavior implements Runnable {
    public static final ExecutorService THREADS = Executors.newFixedThreadPool(20);
    public static Map<Long, GhostBehavior> ls = new HashMap<>();
    @AutoStand
    static IGameBoneService gameBoneService;
    @AutoStand
    static IGameService gameService;
    private Group group;
    private Long qq;
    private GhostObj ghostObj;
    private Integer level;

    public GhostBehavior() {
    }

    public GhostBehavior(Long qq, Group group) {
        this.qq = qq;
        this.group = group;
    }

    public static void exRun(GhostBehavior ghostBehavior) {
        ls.put(ghostBehavior.qq, ghostBehavior);
        THREADS.submit(ghostBehavior);
    }

    public static GhostBehavior create(long who, Group group, Integer level) {
        return new GhostBehavior(who, group) {
            @Override
            protected synchronized double getAttR(Integer level) {
                if (level < 200000) return 0.5;
                else {
                    if (level < 300000) return 0.75;
                    if (level < 1000000) return 0.9;
                    if (level < 3000000) return 1.18;
                    if (level <= 8000000) return 1.4;
                    return 1.7;
                }
            }
        };
    }

    private static boolean needSay(int time) {
        return (time == 1 || time == 3 | time == 8 || time == 10 || time == 15 || time == 20 || time == 25);
    }

    public static final synchronized int[] getFindTime(Integer level) {
        if (level <= 10000) return new int[]{14, 18};
        else if (level <= 30000) return new int[]{12, 14};
        else if (level <= 100000) return new int[]{8, 12};
        else if (level <= 500000) return new int[]{6, 8};
        else if (level <= 1000000) return new int[]{6, 8};
        else if (level <= 5000000) return new int[]{6, 8};
        else if (level <= 10000000) return new int[]{6, 8};
        return new int[]{6, 8};
    }

    public static final synchronized int[] getReadyTime(Integer level) {
        if (level <= 5000) return new int[]{25, 27};
        else if (level < 200000) return new int[]{21, 25};
        else if (level < 1000000) return new int[]{21, 24};
        else if (level < 2000000) return new int[]{18, 23};
        else if (level < 5000000) return new int[]{18, 22};
        else if (level < 9000000) return new int[]{18, 21};
        else return new int[]{16, 18};
    }

    public static final synchronized int[] getLockTime(Integer level) {
        if (level <= 10000) return new int[]{10, 15};
        else if (level <= 30000) return new int[]{10, 13};
        else if (level <= 100000) return new int[]{9, 10};
        else if (level <= 500000) return new int[]{6, 9};
        else if (level <= 1000000) return new int[]{5, 6};
        else if (level <= 5000000) return new int[]{4, 5};
        else if (level <= 10000000) return new int[]{2, 4};
        return new int[]{9999, 9999};
    }

    @Override
    public void run() {
        try {
            Thread.sleep(4000);
            ghostObj = GameJoinDetailService.getGhostObjFrom(qq);
            if (ghostObj == null) return;
            int[] fs = getFindTime(ghostObj.getL());
            int findTime =  Tool.tool.RANDOM.nextInt(fs[1] - fs[0]) + fs[0];
            Thread.sleep(findTime * 1000);
            if (!updateGhost()) return;
            send("小心!!\n'" + ghostObj.getName() + "'发现了你!!!");
            boolean l = false;
            boolean led = false;
            while (true) {
                if (!updateGhost()) {
                    break;
                }
                boolean k0 = (!led ||  Tool.tool.RANDOM.nextInt(5) < 3) && needLock();
                if (k0) {
                    if (!startLock()) {
                        break;
                    }
                    l = true;
                }
                boolean finalL = l;
                FutureTask future = new FutureTask(() -> {
                    try {
                        startAtt(finalL);
                    } catch (InterruptedException e) {
                        send(ghostObj.getName() + "因为眩晕被打断了");
                    }
                }, "");
                BaseInfoTemp.append(-qq, future);
                THREADS.submit(future).get();
                Thread.sleep(8500);
            }
            onDestroy(l);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean startAtt(boolean lock) throws InterruptedException {
        long lostAtt = percentTo(12, ghostObj.getAtt());
        try {
            ghostObj.setAtt(ghostObj.getAtt() - lostAtt);
            if (!updateGhost()) return false;
            send(ghostObj.getName() + "准备对你,进行蓄力一击!");
            if (!updateGhost()) return false;
            int[] fs = getReadyTime(ghostObj.getL());
            int findTime =  Tool.tool.RANDOM.nextInt(fs[1] - fs[0]) + fs[0];
            while (findTime > 0) {
                Thread.sleep(1200);
                findTime--;
                if (needAway()) {
                    send(ghostObj.getName() + "取消了蓄力,准备逃跑");
                    startWay();
                }
                if (!updateGhost()) return false;
                if (needSay(findTime)) send("蓄力倒计时!\r\n" + findTime);
            }
            if (!updateGhost()) return false;
            double attR = getAttR(ghostObj.getL());
            StringBuilder builder = new StringBuilder();
            long v = (long) (ghostObj.getAtt() * attR);
            if (lock) {
                builder.append(ghostObj.getName() + "锁定 并 蓄力 对你造成 无法躲避的  => " + v + "点伤害\r\n");
            } else {
                builder.append(ghostObj.getName() + "蓄力 对你造成了 => " + v + "点伤害\r\n");
            }
            builder.append(GameDetailService.beaten(qq, -2, v));
            String str = gameService.info(qq);
            builder.append("\n").append(str);
            if (GameDataBase.getInfo(qq).getHp() <= 0) {
                saveGhostObjIn(qq, null);
            }
            send(builder.toString());
            if (!updateGhost()) return false;
            return true;
        } finally {
            if (ghostObj != null) {
                ghostObj.setAtt(ghostObj.getAtt() + lostAtt);
            }
        }
    }

    private boolean brokenPaper() {
        return ghostObj.isVertigo();
    }

    private void startWay() {
        try {
            PersonInfo info = GameDataBase.getInfo(qq);
            int b1 = toPercent(ghostObj.getAtt(), info.att());
            int r = 5;
            if (b1 < 70) {
                r = 3;
            } else if (b1 > 135) {
                r = 7;
            }
            boolean sendl = false;
            while (true) {
                Thread.sleep(r * 1000);
                if (!updateGhost()) return;
                if ( Tool.tool.RANDOM.nextInt(10) < r) {
                    saveGhostObjIn(qq, null);
                    send(ghostObj.getName() + "拼尽全力逃跑了!");
                } else {
                    if (sendl) {
                        send(ghostObj.getName() + "尝试逃跑");
                        sendl = true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onDestroy(boolean is) {
        if (is) {
            putPerson(GameDataBase.getInfo(qq).eddTag(SkillDataBase.TAG_CANT_HIDE, 0));
        }
    }

    private boolean startLock() throws InterruptedException {
        send("小心!" + ghostObj.getName() + " 开始 锁定你了");
        if (!updateGhost()) return false;
        Thread.sleep(1000);
        int[] fs = getLockTime(ghostObj.getL());
        int findTime =  Tool.tool.RANDOM.nextInt(fs[1] - fs[0]) + fs[0];
        Thread.sleep(findTime * 1000);
        if (!updateGhost()) return false;
        putPerson(GameDataBase.getInfo(qq).addTag(SkillDataBase.TAG_CANT_HIDE, 1, 30000));
        send(ghostObj.getName() + "已经锁定你了!");
        return true;
    }

    private boolean needLock() {
        PersonInfo info = GameDataBase.getInfo(qq);
        SoulAttribute attributeBone = gameBoneService.getSoulAttribute(qq);
        boolean k1 = attributeBone.getHideChance() >= 50;
        boolean k2 = ((double) info.getHpL() / (double) info.getHp()) > 0.5;
        boolean k3 = info.att() * getAllHHBL(qq) >= ghostObj.getHp() / 2;
        return k1 && (k2 || k3);
    }

    private boolean updateGhost() {
        ghostObj = GameJoinDetailService.getGhostObjFrom(qq);
        return ghostObj != null;
    }

    private boolean needAway() {
        PersonInfo info = GameDataBase.getInfo(qq);
        int b1 = toPercent(info.getHp(), info.getHpL());
        int b2 = toPercent(ghostObj.getHp(), ghostObj.getMaxHp());
        if (b1 >= 50 && b2 <= 50) {
            return  Tool.tool.RANDOM.nextInt(10) < 2;
        } else {
            return false;
        }
    }

    protected synchronized double getAttR(Integer level) {
        if (level < 200000) return 0.7;
        else {
            if (level < 300000) return 0.85;
            if (level < 1000000) return 1.0;
            if (level < 3000000) return 1.2;
            if (level <= 8000000) return 1.5;
            return 2;
        }
    }

    private void send(String str) {
        MessageTools.instance.sendMessageInGroupWithAt(str, group.getId(), qq);
    }
}
