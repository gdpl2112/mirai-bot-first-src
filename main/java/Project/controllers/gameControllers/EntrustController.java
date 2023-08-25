package Project.controllers.gameControllers;

import Project.aSpring.SpringBootResource;
import Project.aSpring.SpringStarter;
import Project.aSpring.dao.Entrust;
import Project.aSpring.dao.TradingRecord;
import Project.aSpring.mcs.mapper.EntrustMapper;
import Project.commons.SpGroup;
import Project.commons.SpUser;
import Project.commons.broadcast.enums.ObjType;
import Project.controllers.BaseController;
import Project.dataBases.GameDataBase;
import Project.utils.Tools.Tool;
import io.github.kloping.MySpringTool.annotations.Action;
import io.github.kloping.MySpringTool.annotations.AllMess;
import io.github.kloping.MySpringTool.annotations.Controller;
import io.github.kloping.date.CronJob;
import io.github.kloping.date.CronUtils;
import io.github.kloping.mirai0.Main.iutils.MessageUtils;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * @author github.kloping
 */
@Controller
public class EntrustController extends BaseController implements Runnable {
    public EntrustController() {
        System.out.println("=====================EntrustController==============================");
        SpringStarter.STARTED_RUNNABLE.add(this);
    }

    /***
     *
     * @param date
     * @param dateFormat : e.g:yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String formatDateByPattern(Date date, String dateFormat) {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        String formatTimeStr = null;
        if (date != null) {
            formatTimeStr = sdf.format(date);
        }
        return formatTimeStr;
    }

    /***
     * convert Date to cron ,eg.  "0 06 10 15 1 ? 2014"
     * @param date  : 时间点
     * @return
     */
    public static String getCron(java.util.Date date) {
        String dateFormat = "ss mm HH dd MM ? yyyy";
        return formatDateByPattern(date, dateFormat);
    }

    private Map<Long, Integer> qid2cid = new HashMap<>();

    @Override
    public void run() {
        entrustMapper = SpringBootResource.getMapper(EntrustMapper.class);
        for (Entrust entrust : entrustMapper.selectList(null)) {
            Entrust finalEntrust = entrust;
            if (entrust.getT0() < System.currentTimeMillis()) continue;
            Date date = new Date(entrust.getT0());
            int id = CronUtils.INSTANCE.addCronJob(getCron(date), new CronJob() {
                @Override
                public void execute(JobExecutionContext context) throws JobExecutionException {
                    MessageUtils.INSTANCE.sendMessageInGroupWithAt(
                            String.format("完成派遣:%s\n%s", finalEntrust.desc(), finish(finalEntrust)),
                            finalEntrust.getGid(), finalEntrust.getQid());
                    qid2cid.remove(entrust.getQid());
                    entrustMapper.deleteById(finalEntrust.getQid());
                }
            });
            qid2cid.put(entrust.getQid(), id);
        }
    }

    public static EntrustMapper entrustMapper;

    @Action(value = "委托.*?", otherName = {"派遣.*?"})
    public Object entrust(SpGroup group, SpUser user, @AllMess String msg) {
        Entrust entrust = entrustMapper.selectById(user.getId());
        if (entrust != null) {
            return String.format("委托/派遣尚未结束\n%s\n大约等待%s", entrust.desc(), Tool.INSTANCE.getTimeTips(entrust.getT0()));
        }
        long t = System.currentTimeMillis();
        entrust = new Entrust();
        if (msg.contains("1天")) {
            entrust.setType0(2);
            t += 24 * 3600000;
        } else if (msg.contains("2天")) {
            entrust.setType0(4);
            t += 24 * 3600000 * 2;
        } else {
            //否则12小时
            entrust.setType0(1);
            t += 12 * 3600000;
        }
        if (msg.contains("大瓶经验")) {
            entrust.setType1(1);
        } else if (msg.contains("时光胶囊")) {
            entrust.setType1(2);
        } else {
            entrust.setType1(0);
        }
        entrust.setQid(user.getId());
        entrust.setGid(group.getId());
        entrust.setT0(t);
        Entrust finalEntrust = entrust;
        Date date = new Date(t);
        int id = CronUtils.INSTANCE.addCronJob(getCron(date), new CronJob() {
            @Override
            public void execute(JobExecutionContext context) throws JobExecutionException {
                MessageUtils.INSTANCE.sendMessageInGroupWithAt(
                        String.format("完成派遣:%s\n%s", finalEntrust.desc(), finish(finalEntrust)),
                        finalEntrust.getGid(), finalEntrust.getQid());
                qid2cid.remove(user.getId());
                entrustMapper.deleteById(finalEntrust.getQid());
            }
        });
        qid2cid.put(entrust.getQid(), id);
        entrustMapper.insert(entrust);
        return "开始派遣: " + entrust.desc();
    }

    @Action(value = "取消委托", otherName = {"取消派遣"})
    public Object unEntrust(SpGroup group, SpUser user, @AllMess String msg) {
        Entrust entrust = entrustMapper.selectById(user.getId());
        if (entrust == null) {
            return "未委托/派遣";
        }
        entrustMapper.deleteById(user.getId());
        qid2cid.remove(user.getId());
        return "已取消";
    }

    public String finish(Entrust entrust) {
        switch (entrust.getType1()) {
            case 0:
                int gold = Tool.INSTANCE.randB(560, 620) * entrust.getType0();
                GameDataBase.getInfo(entrust.getQid()).addGold((long) gold,
                        new TradingRecord().setFrom(-1).setMain(entrust.getQid()).setDesc("派遣获得")
                                .setTo(entrust.getQid()).setMany(gold).setType0(TradingRecord.Type0.gold).setType1(TradingRecord.Type1.add));
                return String.format("获取%s金魂币", gold);
            case 1:
                int n1 = Tool.INSTANCE.randB(3, 5) * entrust.getType0();
                GameDataBase.addToBgs(entrust.getQid(), 103, n1, ObjType.got);
                return String.format("获取%s大瓶经验", n1);
            case 2:
                int n2 = Tool.INSTANCE.randB(3, 5) * entrust.getType0();
                GameDataBase.addToBgs(entrust.getQid(), 101, n2, ObjType.got);
                return String.format("获取%s时光胶囊", n2);
        }
        return "error";
    }
}
