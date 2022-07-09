package Project.services.autoBehaviors;

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

import static Project.dataBases.GameDataBase.getInfo;
import static Project.skill.SkillFactory.ghostSkillNum;
import static io.github.kloping.mirai0.unitls.Tools.GameTool.getHhByGh;

/**
 * @author github-kloping
 */
public class GhostBehavior implements Runnable {
    public static final ExecutorService THREADS = Executors.newFixedThreadPool(20);

    private Group group;
    private Long qq;
    private GhostObj ghostObj;

    public GhostBehavior() {
    }

    public GhostBehavior(Long qq, Group group) {
        this.qq = qq;
        this.group = group;
    }

    public static void exRun(GhostBehavior ghostBehavior) {
        THREADS.submit(ghostBehavior);
    }

    public static GhostBehavior create(long who, Group group, Integer level) {
        return new GhostBehavior(who, group);
    }

    @Override
    public void run() {
        if (!updateGhost()) return;
        Map<Integer, SkillTemplate> jid2skill = new HashMap<>();
        int num = getSkillNum(ghostObj.getLevel());

        while (jid2skill.size() < num) {
            int id0 = Tool.tool.RANDOM.nextInt(ghostSkillNum);
            int jid = 1001 + id0;
            if (jid2skill.containsKey(jid)) continue;
            SkillTemplate template = SkillFactory.factory100(jid, getHhByGh(ghostObj.getLevel()));
            jid2skill.put(jid, template);
        }

        StringBuilder sb = new StringBuilder("魂兽魂技:\n");
        int i = 1;
        for (SkillTemplate value : jid2skill.values()) {
            sb.append(i++).append(",").append(value.getName()).append("\n");
        }
        List<Integer> list = new ArrayList<>(jid2skill.keySet());
        send(sb.toString().trim());

        AtomicReference<Future> atomicReference = new AtomicReference<>();

        ScheduledFuture future = FrameUtils.SERVICE.scheduleWithFixedDelay(() -> {
            if (atomicReference.get() != null) {
                try {
                    atomicReference.get().get(15, TimeUnit.SECONDS);
                } catch (Exception e) {
                    System.err.println(e.getMessage() + " jump");
                }
            }
            SkillTemplate template = jid2skill.get(Tool.tool.getRandT(list));
            send("释放魂技:\n" + template.getIntro());
            Skill skill = template.create(null, -ghostObj.getWhoMeet());
            skill.setGroup(Group.get(MemberTools.getRecentSpeechesGid(ghostObj.getWhoMeet())));
            Future f0 = SkillDataBase.threads.submit(skill);
            atomicReference.set(f0);
            BaseInfoTemp.append(-ghostObj.getWhoMeet(), f0, true);
        }, 4, 14, TimeUnit.SECONDS);
        while (updateGhost() && getInfo(ghostObj.getWhoMeet()).getHp() > 0) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        atomicReference.get().cancel(true);
        if (!atomicReference.get().isCancelled()) {
            atomicReference.get().cancel(true);
        }
        future.cancel(true);
        if (!future.isCancelled()) {
            future.cancel(true);
        }
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

    private boolean updateGhost() {
        ghostObj = GameJoinDetailService.getGhostObjFrom(qq);
        return ghostObj != null && ghostObj.getHp() > 0;
    }
}
