package Project.detailPlugin;

import io.github.kloping.MySpringTool.annotations.Entity;
import io.github.kloping.mirai0.Entitys.Curfew;
import io.github.kloping.mirai0.Main.Resource;
import lombok.SneakyThrows;

import java.io.File;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 宵禁调度器
 *
 * @author github-kloping
 * @version 1.0
 */
@Entity
public class CurfewScheduler extends TimerTask {
    public static final Map<Long, Curfew> CURFEW_MAP = new ConcurrentHashMap<>();

    @Override
    public void run() {
        CURFEW_MAP.forEach((k, v) -> {
            String dateStr = Curfew.FORMAT.format(new Date());
            for (String from : v.getFroms()) {
                if (from != null && from.equals(dateStr)) {
                    Resource.bot.getGroup(k).getSettings().setMuteAll(true);
                    Resource.bot.getGroup(k).sendMessage("宵禁开始");
                }
            }
            for (String to : v.getTos()) {
                if (to != null && to.equals(dateStr)) {
                    Resource.bot.getGroup(k).getSettings().setMuteAll(false);
                    Resource.bot.getGroup(k).sendMessage("宵禁结束");
                }
            }
        });
    }

    public CurfewScheduler() {
        new Timer().schedule(this, 16000, 60000);
        notify0();
    }

    public static void notify0() {
        File file = new File("./conf/");
        if (file.exists()) {
            for (File listFile : file.listFiles()) {
                Long gid = Long.parseLong(listFile.getName());
                update(Curfew.getInstance(gid));
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
