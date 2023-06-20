package Project.services.autoBehaviors;

import Project.broadcast.game.PlayerLostBroadcast;
import Project.commons.SpGroup;
import Project.commons.gameEntitys.base.BaseInfoTemp;
import Project.dataBases.GameDataBase;
import Project.dataBases.skill.SkillDataBase;
import Project.services.detailServices.GameDetailService;
import Project.services.detailServices.ac.GameJoinDetailService;
import Project.services.detailServices.roles.DamageType;
import Project.skills.SkillFactory;
import Project.skills.SkillTemplate;
import io.github.kloping.common.Public;
import io.github.kloping.date.FrameUtils;
import io.github.kloping.mirai0.Main.iutils.MemberUtils;
import io.github.kloping.mirai0.Main.iutils.MessageUtils;
import io.github.kloping.mirai0.commons.GhostObj;
import io.github.kloping.mirai0.commons.Skill;
import Project.utils.Tools.Tool;
import io.github.kloping.number.NumberUtils;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;

import static Project.skills.SkillFactory.ghostSkillNum;
import static Project.utils.Tools.GameTool.getHhByGh;

/**
 * @author github-kloping
 */
public class GhostBehavior implements Runnable {
    public static final ExecutorService THREADS = new ThreadPoolExecutor(
            15, 20, 7, TimeUnit.MINUTES, new ArrayBlockingQueue<>(20));
    public static final Map<Long, GhostBehavior> MAP = new HashMap<>();

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

    AtomicReference<Future> atomicReference = new AtomicReference<>(null);
    ScheduledFuture future0 = null;
    Future future1 = null;
    Map<Integer, SkillTemplate> jid2skill = new HashMap<>();
    List<Integer> list = new ArrayList<>();
    private SpGroup group;
    private Long qq;
    private GhostObj ghostObj;
    public Runnable r1 = new Runnable() {
        @Override
        public void run() {
            try {
                while (true) {
                    int r = (int) Tool.INSTANCE.randA(4, 8);
                    long at2 = Tool.INSTANCE.randLong(ghostObj.getAtt(), 0.25f, 0.42f);
                    at2 = NumberUtils.percentTo(ghostObj.getTagValueOrDefault(SkillDataBase.TAG_STRENGTHEN_ATT, 100).intValue(), at2);
                    while (r >= 0) {
                        Thread.sleep(1000);
                        r--;
                    }
                    long qid = -1L;
                    if (ghostObj.getWiths().size() > 0) {
                        if (Tool.INSTANCE.RANDOM.nextBoolean()) {
                            qid = Tool.INSTANCE.getRandT(ghostObj.getWiths());
                        } else {
                            qid = qq;
                        }
                    }
                    StringBuilder sb = new StringBuilder();
                    sb.append(GameDataBase.getNameById(ghostObj.getId())).append("对").append(Tool.INSTANCE.at(qid)).append("造成").append(at2).append("点伤害\n").append(GameDetailService.beaten(qid, -2, at2, DamageType.AD));
                    Public.EXECUTOR_SERVICE.submit(() -> send(sb.toString()));
                }
            } catch (InterruptedException e) {
                System.err.println("结束");
            }
        }
    };
    private List<Integer> nowAllowJid = new LinkedList<>();
    private int jid = -1;
    private boolean forceOver = true;
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
                template = jid2skill.get(Tool.INSTANCE.getRandT(list));
                if (jid != template.getJid()) break;
            }
            jid = template.getJid();
            if (!updateGhost()) {
                thisOver();
                return;
            }
            send("释放魂技:\n" + template.getIntro());
            Skill skill = template.create(null, -ghostObj.getWhoMeet());
            skill.setGroup(SpGroup.get(MemberUtils.getRecentSpeechesGid(ghostObj.getWhoMeet())));
            Future f0 = SkillDataBase.threads.submit(skill);
            atomicReference.set(f0);
            if (jid == 1001 || jid == 1002) {
                BaseInfoTemp.append(-ghostObj.getWhoMeet(), f0, true, ghostObj.getWhoMeet());
            } else {
                BaseInfoTemp.append(-ghostObj.getWhoMeet(), f0, true);
            }
        }
    };

    public GhostBehavior(Long qq, SpGroup group) {
        this.qq = qq;
        this.group = group;
        MAP.put(qq, this);
    }

    public static void exRun(GhostBehavior ghostBehavior) {
        THREADS.submit(ghostBehavior);
    }

    public static GhostBehavior create(long who, SpGroup group, Integer level) {
        return new GhostBehavior(who, group);
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

    @Override
    public void run() {
        if (!updateGhost()) return;
        int num = getSkillNum(ghostObj.getLevel());
        while (jid2skill.size() < num) {
            int id0 = Tool.INSTANCE.RANDOM.nextInt(ghostSkillNum - 3);
            int jid = 1001 + id0;
            if (jid2skill.containsKey(jid)) continue;
            if (getNowAllowJid().contains(jid)) continue;
            System.out.println("factory <=" + jid);
            SkillTemplate template = SkillFactory.factory100(jid, getHhByGh(ghostObj.getLevel()));
            System.out.println("factory ok =>" + jid);
            jid2skill.put(jid, template);
        }

        if (ghostObj.getId() > 600 && ghostObj.getId() < 700) {
            if (ghostObj.getLevel().intValue() > 10000) {
                int j0 = 1101 + Tool.INSTANCE.RANDOM.nextInt(3);
                SkillTemplate template = SkillFactory.factory100(j0, getHhByGh(ghostObj.getLevel()));
                jid2skill.put(j0, template);
            }
        }

        StringBuilder sb = new StringBuilder("魂兽魂技:\n");
        int i = 1;
        for (SkillTemplate value : jid2skill.values()) {
            sb.append(i++).append(",").append(value.getName()).append("\n");
        }
        list = new ArrayList<>(jid2skill.keySet());
        send(sb.toString().trim());
        future0 = FrameUtils.SERVICE.scheduleWithFixedDelay(r0, 4, 14, TimeUnit.SECONDS);
        future1 = Public.EXECUTOR_SERVICE.submit(r1);
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
            if (future0 != null) {
                future0.cancel(true);
                if (!future0.isCancelled()) {
                    future0.cancel(true);
                }
            }
            if (future1 != null) {
                future1.cancel(true);
                if (!future1.isCancelled()) {
                    future1.cancel(true);
                }
            }
            if (atomicReference != null && atomicReference.get() != null) {
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
        MessageUtils.INSTANCE.sendMessageInGroupWithAt(str, group.getId(), qq);
    }

    private boolean updateGhost() {
        ghostObj = GameJoinDetailService.getGhostObjFrom(qq);
        return (forceOver && ghostObj != null && ghostObj.getHp() > 0);
    }

    public List<Integer> getNowAllowJid() {
        return nowAllowJid;
    }
}
