package Project.services.autoBehaviors;

import Project.broadcast.game.PlayerLostBroadcast;
import Project.dataBases.skill.SkillDataBase;
import Project.services.detailServices.GameJoinDetailService;
import Project.skill.SkillFactory;
import Project.skill.SkillTemplate;
import io.github.kloping.date.FrameUtils;
import io.github.kloping.mirai0.Main.ITools.MemberTools;
import io.github.kloping.mirai0.Main.ITools.MessageTools;
import io.github.kloping.mirai0.commons.GhostObj;
import io.github.kloping.mirai0.commons.Group;
import io.github.kloping.mirai0.commons.Skill;
import io.github.kloping.mirai0.commons.gameEntitys.base.BaseInfoTemp;
import io.github.kloping.mirai0.unitls.Tools.Tool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;

import static Project.skill.SkillFactory.ghostSkillNum;
import static io.github.kloping.mirai0.unitls.Tools.GameTool.getHhByGh;

/**
 * @author github-kloping
 */
public class GhostBehavior implements Runnable {
    public static final ExecutorService THREADS = Executors.newFixedThreadPool(20);
    public static final Map<Long, GhostBehavior> MAP = new HashMap<>();
    private Group group;
    private Long qq;
    private GhostObj ghostObj;

    static {
        PlayerLostBroadcast.INSTANCE.add(new PlayerLostBroadcast.PlayerLostReceiver() {
            @Override
            public void onReceive(long who, long from, LostType type) {
                if (type == LostType.att) {
                    if (MAP.containsKey(who)) {
                        MAP.get(who).thisOver();
                    }
                }
            }
        });
    }

    public GhostBehavior(Long qq, Group group) {
        this.qq = qq;
        this.group = group;
        MAP.put(qq, this);
    }

    public static void exRun(GhostBehavior ghostBehavior) {
        THREADS.submit(ghostBehavior);
    }

    public static GhostBehavior create(long who, Group group, Integer level) {
        return new GhostBehavior(who, group);
    }

    private int jid = -1;

    public Runnable r0 = new Runnable() {
        @Override
        public void run() {
            if (!updateGhost()) {
                thisOver();
                return;
            }
            if (atomicReference != null && atomicReference.get() != null) {
                try {
                    atomicReference.get().get(15, TimeUnit.SECONDS);
                } catch (Exception e) {
                    System.err.println(e.getMessage() + " jump");
                }
            }
            if (!updateGhost()) {
                thisOver();
                return;
            }
            SkillTemplate template;
            while (true) {
                template = jid2skill.get(Tool.tool.getRandT(list));
                if (jid != template.getJid()) break;
            }
            jid = template.getJid();
            if (!updateGhost()) {
                thisOver();
                return;
            }
            send("释放魂技:\n" + template.getIntro());
            Skill skill = template.create(null, -ghostObj.getWhoMeet());
            skill.setGroup(Group.get(MemberTools.getRecentSpeechesGid(ghostObj.getWhoMeet())));
            Future f0 = SkillDataBase.threads.submit(skill);
            atomicReference.set(f0);
            if (jid == 1001 || jid == 1002) {
                BaseInfoTemp.append(-ghostObj.getWhoMeet(), f0, true, ghostObj.getWhoMeet());
            } else {
                BaseInfoTemp.append(-ghostObj.getWhoMeet(), f0, true);
            }
        }
    };

    AtomicReference<Future> atomicReference = new AtomicReference<>(null);
    ScheduledFuture future = null;
    Map<Integer, SkillTemplate> jid2skill = new HashMap<>();
    List<Integer> list = new ArrayList<>();

    @Override
    public void run() {
        if (!updateGhost()) return;
        int num = getSkillNum(ghostObj.getLevel());
        while (jid2skill.size() < num) {
            int id0 = Tool.tool.RANDOM.nextInt(ghostSkillNum);
            int jid = 1001 + id0;
            if (jid2skill.containsKey(jid)) continue;
            SkillTemplate template = SkillFactory.factory100(jid, getHhByGh(ghostObj.getLevel()));
            jid2skill.put(jid, template);
        }

//        jid2skill.put(1001,SkillFactory.factory100(1001, getHhByGh(ghostObj.getLevel())));

        StringBuilder sb = new StringBuilder("魂兽魂技:\n");
        int i = 1;
        for (SkillTemplate value : jid2skill.values()) {
            sb.append(i++).append(",").append(value.getName()).append("\n");
        }
        list = new ArrayList<>(jid2skill.keySet());
        send(sb.toString().trim());
        future = FrameUtils.SERVICE.scheduleWithFixedDelay(r0, 4, 14, TimeUnit.SECONDS);
        while (updateGhost()) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        thisOver();
    }

    public void thisOver() {
        try {
            GameJoinDetailService.saveGhostObjIn(qq, null);
            if (future != null) {
                future.cancel(true);
                if (!future.isCancelled()) {
                    future.cancel(true);
                }
            }
            if (atomicReference != null) {
                atomicReference.get().cancel(true);
                if (!atomicReference.get().isCancelled()) {
                    atomicReference.get().cancel(true);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        MAP.remove(qq);
        forceOver = false;
    }

    private void send(String str) {
        MessageTools.instance.sendMessageInGroupWithAt(str, group.getId(), qq);
    }

    public static int getSkillNum(int level) {
        if (level > 1000 * 10000) {
            return 5;
        } else if (level > 100 * 10000) {
            return 4;
        } else if (level > 10 * 10000) {
            return 3;
        } else if (level > 5 * 0000) {
            return 2;
        }
        return 1;
    }

    private boolean forceOver = true;

    private boolean updateGhost() {
        ghostObj = GameJoinDetailService.getGhostObjFrom(qq);
        return (forceOver && ghostObj != null && ghostObj.getHp() > 0);
    }
}
