package Project.detailPlugin;

import io.github.kloping.MySpringTool.annotations.Entity;
import io.github.kloping.date.FrameUtils;
import io.github.kloping.mirai0.commons.Curfew;
import io.github.kloping.mirai0.Main.Resource;

import java.io.File;
import java.util.Date;
import java.util.Map;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * 宵禁调度器
 *
 * @author github-kloping
 * @version 1.0
 */
@Entity
public class CurfewScheduler extends TimerTask implements Runnable {
    public static final Map<Long, Curfew> CURFEW_MAP = new ConcurrentHashMap<>();
    private int index = 0;

    @Override
    public void run() {
        CURFEW_MAP.forEach((k, v) -> {
            String dateStr = Curfew.FORMAT.format(new Date());
            for (String from : v.getFroms()) {
                if (from != null && from.equals(dateStr)) {
                    try {
                        Resource.bot.getGroup(k).getSettings().setMuteAll(true);
                        Resource.bot.getGroup(k).sendMessage("宵禁开始");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            for (String to : v.getTos()) {
                if (to != null && to.equals(dateStr)) {
                    try {
                        Resource.bot.getGroup(k).getSettings().setMuteAll(false);
                        Resource.bot.getGroup(k).sendMessage("宵禁结束");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public CurfewScheduler() {
        FrameUtils.SERVICE.scheduleAtFixedRate(this, 20, 60, TimeUnit.SECONDS);
        notify0();
    }

    public static void notify0() {
        File file = new File("./conf/");
        if (file.exists()) {
            for (File listFile : file.listFiles()) {
                try {
                    Long gid = Long.parseLong(listFile.getName());
                    update(Curfew.getInstance(gid));
                } catch (NumberFormatException e) {
                }
            }
        }
    }

    public static void add(long gid, String from, String to) {
        Curfew curfew = Curfew.getInstance(gid);
        curfew.getFroms().add(from);
        curfew.getTos().add(to);
        curfew.save();
        CURFEW_MAP.put(gid, curfew);
    }

    public static void update(Curfew curfew) {
        CURFEW_MAP.put(curfew.getGid(), curfew);
    }

    public static void delete(long gid) {
        CURFEW_MAP.get(gid).delete();
    }
}
