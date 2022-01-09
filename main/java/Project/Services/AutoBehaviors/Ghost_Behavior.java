package Project.Services.AutoBehaviors;

import Entitys.gameEntitys.AttributeBone;
import Entitys.gameEntitys.GhostObj;
import Entitys.Group;
import Entitys.gameEntitys.PersonInfo;
import Project.DataBases.skill.SkillDataBase;
import Project.Services.DetailServices.GameDetailService;
import Project.Services.DetailServices.GameJoinDetailService;
import Project.Services.Iservice.IGameBoneService;
import Project.Services.Iservice.IGameService;
import Project.Tools.Tool;
import io.github.kloping.Mirai.Main.ITools.MessageTools;
import io.github.kloping.MySpringTool.annotations.AutoStand;
import io.github.kloping.MySpringTool.annotations.Entity;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static Project.DataBases.GameDataBase.getInfo;
import static Project.DataBases.GameDataBase.putPerson;
import static Project.DataBases.skill.SkillDataBase.toPercent;
import static Project.Services.DetailServices.GameJoinDetailService.saveGhostObjIn;
import static Project.Tools.GameTool.getAllHHBL;

@Entity
public class Ghost_Behavior implements Runnable {
    public static final ExecutorService threads = Executors.newFixedThreadPool(20);
    public static Map<Long, Ghost_Behavior> ls = new HashMap<>();
    private Group group;
    private Long qq;

    @AutoStand
    static IGameBoneService gameBoneService;

    @AutoStand
    static IGameService gameService;

    public static void ExRun(Ghost_Behavior ghost_behavior) {
        ls.put(ghost_behavior.qq, ghost_behavior);
        threads.execute(ghost_behavior);
    }

    public Ghost_Behavior() {
    }

    public Ghost_Behavior(Long qq, Group group) {
        this.qq = qq;
        this.group = group;
    }

    private GhostObj ghostObj;
    private Integer level;

    @Override
    public void run() {
        try {
            Thread.sleep(4000);
            ghostObj = GameJoinDetailService.getGhostObjFrom(qq);
            if (ghostObj == null) return;
            int[] fs = getFindTime(ghostObj.getL());
            int findTime = Tool.rand.nextInt(fs[1] - fs[0]) + fs[0];
            Thread.sleep(findTime * 1000);
            if (!updateGhost()) return;
            Send("小心!!\n'" + ghostObj.getName() + "'发现了你!!!");
            boolean l = false;
            boolean led = false;
            while (true) {
                if (!updateGhost()) break;
                if ((!led || Tool.rand.nextInt(5) < 3) && needLock()) {
                    if (!startLock())
                        break;
                    l = true;
                }
                if (startAtt(l)) {
                    Thread.sleep(8500);
                    continue;
                } else break;
            }
            onDestroy(l);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean startAtt(boolean lock) throws InterruptedException {
        if (!updateGhost()) return false;
        Send(ghostObj.getName() + "准备对你,进行蓄力一击!");
        if (!updateGhost()) return false;
        int[] fs = getReadyTime(ghostObj.getL());
        int findTime = Tool.rand.nextInt(fs[1] - fs[0]) + fs[0];
        while (findTime > 0) {
            Thread.sleep(1600);
            findTime--;
            if (needAway()) {
                Send(ghostObj.getName() + "取消了蓄力,准备逃跑");
                startWay();
            }
            if (!updateGhost()) return false;
            if (needSay(findTime)) Send("蓄力倒计时!\r\n" + findTime);
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
        if (getInfo(qq).getHp() <= 0) {
            saveGhostObjIn(qq, null);
        }
        Send(builder.toString());
        if (!updateGhost()) return false;
        return true;
    }

    private void startWay() {
        try {
            PersonInfo info = getInfo(qq);
            int b1 = toPercent(ghostObj.getAtt(), info.getAtt());
            int r = 5;
            if (b1 < 70)
                r = 3;
            else if (b1 > 135)
                r = 7;
            boolean sendl = false;
            while (true) {
                Thread.sleep(3000);
                if (Tool.rand.nextInt(10) < r) {
                    saveGhostObjIn(qq, null);
                    Send(ghostObj.getName() + "拼尽全力逃跑了!");
                } else {
                    if (sendl) {
                        Send(ghostObj.getName() + "尝试逃跑");
                        sendl = true;
                    }
                }
                if (!updateGhost()) return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static boolean needSay(int time) {
        return (time == 1 || time == 3 | time == 8 || time == 10 || time == 15 || time == 20 || time == 25);
    }

    public void onDestroy(boolean is) {
        if (is)
            putPerson(getInfo(qq).eddTag(SkillDataBase.tag_CantHide, 0));
    }

    private boolean startLock() throws InterruptedException {
        Send("小心!" + ghostObj.getName() + " 开始 锁定你了");
        if (!updateGhost()) return false;
        Thread.sleep(1000);
        int[] fs = getLockTime(ghostObj.getL());
        int findTime = Tool.rand.nextInt(fs[1] - fs[0]) + fs[0];
        Thread.sleep(findTime * 1000);
        if (!updateGhost()) return false;
        putPerson(getInfo(qq).addTag(SkillDataBase.tag_CantHide, 0));
        Send(ghostObj.getName() + "已经锁定你了!");
        return true;
    }

    private boolean needLock() {
        PersonInfo info = getInfo(qq);
        AttributeBone attributeBone = gameBoneService.getAttribute(qq);
        boolean k1 = attributeBone.getHide_pro() >= 50;
        boolean k2 = ((double) info.getHpl() / (double) info.getHp()) > 0.5;
        boolean k3 = info.getAtt() * getAllHHBL(qq) >= ghostObj.getHp() / 2;
        return k1 && (k2 || k3);
    }

    private boolean updateGhost() {
        ghostObj = GameJoinDetailService.getGhostObjFrom(qq);
        return ghostObj != null;
    }

    private boolean needAway() {
        PersonInfo info = getInfo(qq);
        int b1 = toPercent(info.getHp(), info.getHpl());
        int b2 = toPercent(ghostObj.getHp(), ghostObj.getMaxHp());
        if (b1 >= 50 && b2 <= 50) {
            return Tool.rand.nextInt(10) < 2;
        } else {
            return false;
        }
    }

    private static final synchronized double getAttR(Integer level) {
        if (level < 200000) return 1.0;
        else {
            if (level < 300000) return 1.6;
            if (level < 1000000) return 2.0;
            if (level < 3000000) return 3.0;
            if (level <= 8000000) return 3.5;
            return 4;
        }
    }

    public static final synchronized int[] getFindTime(Integer level) {
        if (level <= 10000) return new int[]{14, 18};
        else if (level <= 30000) return new int[]{12, 14};
        else if (level <= 100000) return new int[]{8, 12};
        else if (level <= 500000) return new int[]{6, 8};
        else if (level <= 1000000) return new int[]{4, 6};
        else if (level <= 5000000) return new int[]{2, 3};
        else if (level <= 10000000) return new int[]{0, 1};
        return new int[]{9999, 9999};
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

    private void Send(String str) {
        MessageTools.sendMessageInGroupWithAt(str, group.getId(), qq);
//        group.sendMessage(new MessageChainBuilder().append(new At(qq)).append("\r\n").append(str).build());
    }
}
