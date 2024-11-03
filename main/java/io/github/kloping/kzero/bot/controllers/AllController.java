package io.github.kloping.kzero.bot.controllers;

import io.github.kloping.common.Public;
import io.github.kloping.date.DateUtils;
import io.github.kloping.kzero.bot.database.DataBase;
import io.github.kloping.kzero.main.api.KZeroBot;
import io.github.kloping.kzero.main.api.MessagePack;
import io.github.kloping.kzero.mirai.exclusive.PluginManagerController;
import io.github.kloping.kzero.mirai.exclusive.WebAuthController;
import io.github.kloping.kzero.spring.dao.GroupConf;
import io.github.kloping.kzero.utils.ChatAi;
import io.github.kloping.spt.annotations.*;
import io.github.kloping.spt.entity.interfaces.Runner;
import io.github.kloping.spt.exceptions.NoRunException;
import io.github.kloping.spt.interfaces.Logger;
import io.github.kloping.spt.interfaces.QueueExecutor;
import io.github.kloping.spt.interfaces.component.ContextManager;
import io.github.kloping.url.UrlUtils;

import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.*;

/**
 * @author github.kloping
 */
@Controller
public class AllController implements Runner {

    public AllController(QueueExecutor queueExecutor) {
        queueExecutor.setBefore(this);
    }

    @DefAction
    public void run(Method method, MessagePack pack, KZeroBot bot) throws NoRunException {
        GroupConf groupConf = dataBase.getConf(pack.getSubjectId());
        if (groupConf != null) {
            if (!groupConf.getOpen()) {
                return;
            }
        }
        hand(bot, pack);
    }

    @AutoStand
    Logger log;

    public static final String REGX = "[我想问|为什么|为啥|什么].+|.+\\?|.+\\？|ai:.+|.+是什么";

    private String asid = null;

    private void hand(KZeroBot bot, MessagePack pack) {
        String msg = pack.getMsg();
        if (msg == null) return;
        if (asid == null) asid = String.format("<at:%s>", bot.getId());
        msg = msg.trim();
        if (msg.matches(REGX) || msg.startsWith(asid)) {
            if (msg.matches("(<.*?>)?\\s?[\\?|\\？]+")) {
                log.waring("忽略纯问号");
                return;
            }
            log.waring("匹配关键词:开始回话");
            if (msg.startsWith("ai:")) msg = msg.substring(3);
            String out = ChatAi.chat(pack.getSenderId(), msg);

            if (out != null) {
                bot.getAdapter().sendMessage(pack.getType(), pack.getSubjectId(), out);
            }
        }
    }

    @AutoStand
    public static DataBase dataBase;

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
        if (cla != AllController.class){
            GroupConf groupConf = dataBase.getConf(pack.getSubjectId());
            if (groupConf != null) {
                if (!groupConf.getOpen()) {
                    return;
                }
            }
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

    @Action("测试")
    public Object test0(@Param("name") String name, String sid, MessagePack pack, KZeroBot bot) throws Exception {
        return "测试消息";
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

    private Map<String, Queue<MessagePack>> hist = new HashMap<>();

    public List<WakeUpReception> receptions = new LinkedList<>();

    public interface WakeUpReception {
        boolean up(String id);
    }
}
