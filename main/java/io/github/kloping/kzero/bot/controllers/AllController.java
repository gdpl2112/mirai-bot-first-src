package io.github.kloping.kzero.bot.controllers;

import io.github.kloping.MySpringTool.annotations.*;
import io.github.kloping.MySpringTool.entity.interfaces.Runner;
import io.github.kloping.MySpringTool.exceptions.NoRunException;
import io.github.kloping.MySpringTool.interfaces.QueueExecutor;
import io.github.kloping.MySpringTool.interfaces.component.ContextManager;
import io.github.kloping.common.Public;
import io.github.kloping.date.DateUtils;
import io.github.kloping.kzero.bot.database.DataBase;
import io.github.kloping.kzero.main.api.KZeroBot;
import io.github.kloping.kzero.main.api.MessagePack;
import io.github.kloping.kzero.main.api.MessageType;
import io.github.kloping.kzero.mirai.exclusive.PluginManagerController;
import io.github.kloping.kzero.mirai.exclusive.WebAuthController;
import io.github.kloping.kzero.spring.dao.GroupConf;
import io.github.kloping.url.UrlUtils;
import net.mamoe.mirai.contact.NormalMember;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.MessageChain;

import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author github.kloping
 */
@Controller
public class AllController implements Runner {

    public AllController(QueueExecutor queueExecutor) {
        queueExecutor.setBefore(this);
    }

    @AutoStand
    DataBase dataBase;

    @AutoStand(id = "upload.url")
    public static String UPLOAD_URL;

    @Override
    public void run(Method method, Object t, Object[] objects)  {
        Class cla = method.getDeclaringClass();
        if (cla == AdminController.class) return;
        if (cla == PluginManagerController.class) return;
        if (cla == WebAuthController.class) return;
        MessagePack pack = (MessagePack) objects[3];
        KZeroBot bot = (KZeroBot) objects[4];
        GroupConf groupConf = dataBase.getConf(pack.getSubjectId());
        if (groupConf != null) {
            if (!groupConf.getOpen()) throw new NoRunException("未开启");
        }
        String sid = pack.getSenderId();
        if (!wakes.contains(sid)) wakes.add(sid);
        int hour = DateUtils.getHour();
        if (hour >= 5 && hour <= 11) {
            Long ut0 = map.get(sid);
            if (ut0 != null) {
                ut0 = System.currentTimeMillis() - ut0;
                if (ut0 > MIN_WAKE_TIME && ut0 < MAX_WAKE_TIME) {
                    int h = (int) (ut0 / (1000 * 60 * 60));
                    int m = (int) (ut0 % (1000 * 60 * 60)) / (1000 * 60);
                    bot.getAdapter().onResult(method, String.format("推测睡眠时长: %s时%s分", h, m), pack);
                    Public.EXECUTOR_SERVICE.submit(() -> {
                        synchronized (receptions) {
                            Iterator<WakeUpReception> iterator = receptions.iterator();
                            while (iterator.hasNext()) {
                                if (iterator.next().up(sid)) iterator.remove();
                            }
                        }
                    });
                }
            }
        }
        map.put(pack.getSenderId(), System.currentTimeMillis());
    }

    @AutoStand
    ContextManager contextManager;

    @Action("测试<.+=>name>")
    public Object test0(@Param("name") String name, String sid, MessagePack pack, KZeroBot bot) throws Exception {
        return null;
    }

    @AutoStand(id = "super_id")
    String superId;

    @AutoStand(id = "auth.pwd")
    String pwd;

    @AutoStand(id = "cmd.reboot")
    String reboot;


    @Action("强制重启")
    public Object reboot(MessagePack pack, KZeroBot bot) throws Exception {
        if (superId.equals(pack.getSenderId())) {
            try {
                bot.getAdapter().sendMessage(pack.getType(), pack.getSubjectId(), "任务即将提交\n提交之后可能造成短暂的不可用\n请耐心等待");
            } catch (Throwable e) {
                e.printStackTrace();
            }
            String url = String.format("http://localhost/exec?pwd=%s&cmd=%s&out=true", pwd, URLEncoder.encode(reboot));
            Object o = UrlUtils.getStringFromHttpUrl(url);
            return "Task has been submitted\nout:" + o;
        } else return "permission denied";
    }

    @AutoStand(id = "cmd.update")
    String update;

    @Action("强制更新")
    public Object update(MessagePack pack, KZeroBot bot) throws Exception {
        if (superId.equals(pack.getSenderId())) {
            try {
                bot.getAdapter().sendMessage(pack.getType(), pack.getSubjectId(), "任务即将提交\n提交之后可能造成短暂的不可用\n请耐心等待");
            } catch (Throwable e) {
                e.printStackTrace();
            }
            String url = String.format("http://localhost/exec?pwd=%s&cmd=%s&out=true", pwd, URLEncoder.encode(update));
            Object o = UrlUtils.getStringFromHttpUrl(url);
            return "Task has been submitted\nout:" + o;
        } else return "permission denied";
    }

    @AutoStand(id = "cmd.update-m")
    String updateM;

    @Action("强制更新M")
    public Object updateM(MessagePack pack, KZeroBot bot) throws Exception {
        if (superId.equals(pack.getSenderId())) {
            try {
                bot.getAdapter().sendMessage(pack.getType(), pack.getSubjectId(), "任务即将提交\n提交之后可能造成短暂的不可用\n请耐心等待");
            } catch (Throwable e) {
                e.printStackTrace();
            }
            String url = String.format("http://localhost/exec?pwd=%s&cmd=%s&out=true", pwd, URLEncoder.encode(updateM));
            Object o = UrlUtils.getStringFromHttpUrl(url);
            return "Task has been submitted\nout:" + o;
        } else return "permission denied";
    }

    @AutoStand(id = "cmd.reboot-m")
    String rebootM;

    @Action("强制重启M")
    public Object rebootM(MessagePack pack, KZeroBot bot) throws Exception {
        if (superId.equals(pack.getSenderId())) {
            try {
                bot.getAdapter().sendMessage(pack.getType(), pack.getSubjectId(), "任务即将提交\n提交之后可能造成短暂的不可用\n请耐心等待");
            } catch (Throwable e) {
                e.printStackTrace();
            }
            String url = String.format("http://localhost/exec?pwd=%s&cmd=%s&out=true", pwd, URLEncoder.encode(rebootM));
            Object o = UrlUtils.getStringFromHttpUrl(url);
            return "Task has been submitted\nout:" + o;
        } else return "permission denied";
    }

    private List<String> wakes = new LinkedList<>();

    @CronSchedule("5 1 5 * * ? *")
    public void eveZero() {
        wakes.clear();
    }

    private Map<String, Long> map = new HashMap<>();

    protected boolean isWakeUp(String sid) {
        return wakes.contains(sid);
    }

    private static final int MIN_WAKE_TIME = 1000 * 60 * 60 * 5;
    private static final int MAX_WAKE_TIME = 1000 * 60 * 60 * 12;

    private Map<Long, Queue<MessageEvent>> hist = new HashMap<>();

    @DefAction
    public void intercept0(Method method, MessagePack pack, KZeroBot bot) {
        if (pack.getType() == MessageType.GROUP) {
            GroupMessageEvent event = (GroupMessageEvent) pack.getRaw();
            if (event.getSubject().getBotAsMember().getPermission().getLevel() == 2) {
                Queue<MessageEvent> queue = hist.get(event.getSender().getId());
                if (queue == null) queue = new LinkedBlockingQueue<>(5);
                if (queue.size() > 3) {
                    String code = MessageChain.serializeToJsonString(event.getMessage()).trim();
                    int ac = 0;
                    for (int i = 0; i < queue.size(); i++) {
                        MessageEvent e1 = queue.peek();
                        if (Math.abs(e1.getTime() - event.getTime()) > 120) continue;
                        String c2 = MessageChain.serializeToJsonString(e1.getMessage());
                        if (code.equals(c2)) ac++;
                    }
                    if (ac == 3) {
                        event.getSubject().sendMessage("检测到可能存在刷屏行为,请注意发言.");
                    } else if (ac > 3) {
                        event.getSubject().sendMessage("多次刷屏...\n禁言20s以示警告");
                        NormalMember member = (NormalMember) event.getSender();
                        member.mute(20);
                    }
                }
                if (queue.size() > 5) queue.poll();
                queue.offer(event);
                hist.put(event.getSender().getId(), queue);
            }
        }
    }

    public List<WakeUpReception> receptions = new LinkedList<>();

    public interface WakeUpReception {
        boolean up(String id);
    }
}
