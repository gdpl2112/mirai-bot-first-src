package Project.controllers;

import Project.dataBases.DataBase;
import Project.dataBases.GameDataBase;
import Project.detailPlugin.CurfewScheduler;
import Project.interfaces.Iservice.IGameService;
import Project.services.impl.ZongMenServiceImpl;
import io.github.kloping.MySpringTool.StarterApplication;
import io.github.kloping.MySpringTool.annotations.*;
import io.github.kloping.MySpringTool.exceptions.NoRunException;
import io.github.kloping.mirai0.Entitys.Curfew;
import io.github.kloping.mirai0.Entitys.Group;
import io.github.kloping.mirai0.Entitys.User;
import io.github.kloping.mirai0.Entitys.gameEntitys.PersonInfo;
import io.github.kloping.mirai0.Main.ITools.MemberTools;
import io.github.kloping.mirai0.Main.ITools.MessageTools;
import io.github.kloping.mirai0.Main.Resource;
import io.github.kloping.mirai0.unitls.Tools.Tool;

import java.io.File;
import java.text.ParseException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static Project.ResourceSet.FinalString.*;
import static Project.aSpring.SpringBootResource.getBagMapper;
import static Project.dataBases.DataBase.HIST_U_SCORE;
import static Project.dataBases.GameDataBase.HIST_INFOS;
import static io.github.kloping.mirai0.Main.ITools.MemberTools.getUser;
import static io.github.kloping.mirai0.Main.Resource.*;
import static io.github.kloping.mirai0.unitls.Tools.Tool.getRandString;

/**
 * @author github-kloping
 */
@Controller
public class SuperController {
    public SuperController() {
        println(this.getClass().getSimpleName() + "构建");
    }

    public long tempSuperL = -1;

    @Before
    public void before(@AllMess String mess, Group group, User qq) throws NoRunException {
        if (tempSuperL != -1L) {
            if (qq.getId() == tempSuperL) {
                tempSuperL = -1L;
                return;
            }
        }
        if (qq.getId() != superQL) {
            throw new NoRunException("can`t do this");
        }
    }

    @AutoStand
    private ZongMenServiceImpl zons;

    @Action("/test")
    public String o2(User qq, Group group) {
        String tips = "===>";
        return tips;
    }

    @Action("/execute.+")
    public String o1(@AllMess String str, Group group) {
        long q = MessageTools.getAtFromString(str);
        if (q == -1) {
            throw new NoRunException("");
        }
        String qStr = q == bot.getId() ? "me" : String.valueOf(q);
        str = str.replaceFirst("/execute\\[@" + qStr + "]", "");
        StarterApplication.executeMethod(q, str, q, getUser(q), Group.get(group.getId()), 0);
        return "executing";
    }

    @Action("赋予一次超级权限.+")
    public String f0(@AllMess String s) {
        long q = MessageTools.getAtFromString(s);
        if (q == -1) {
            throw new NoRunException("");
        }
        tempSuperL = q;
        return "ok";
    }

    @Action("addScore.{1,}")
    public String addScore(@AllMess String messages, User qq, Group gr) throws NoRunException {
        long who = MessageTools.getAtFromString(messages);
        messages = messages.replace(Long.toString(who), "");
        if (who == -1) {
            return ("Are You True??");
        }
        long num = Long.parseLong(Tool.findNumberFromString(messages));
        DataBase.addScore(num, who);
        return new StringBuilder().append("给 =》 ").append(MemberTools.getNameFromGroup(who, gr)).append("增加了\r\n=>").append(num + "").append("积分").toString();
    }

    @Action("全体加积分.{1,}")
    public String addAllScore(@AllMess String messages, User qq) throws NoRunException {
        long num = Long.parseLong(Tool.findNumberFromString(messages));
        return "不可用";
    }

    private static final Map<Long, Long> UP_AKS = new ConcurrentHashMap<>();
    public static final String[] IS_ADMINISTRATOR_TIPS = new String[]
            {"应该是", "是的", "是吧", "是", "你猜", "yeah", "is", "1"};
    public static final String[] IST_ADMINISTRATOR_TIPS = new String[]
            {"你觉得呢", "不是", "?你猜", "问我呢?", "no", "222", "你确定?", "-1"};

    @Action(value = "ta是管理<.+=>par>", otherName = {"他是管理<.+=>par>", "她是管理<.+=>par>"})
    public Object isIAdministrator(@Param("par") String par, User qq) {
        Number q2 = null;
        try {
            q2 = Long.valueOf(Tool.findNumberFromString(par));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        if (q2 == null || q2.longValue() == -1) {
            return "谁?";
        }
        UP_AKS.put(qq.getId(), q2.longValue());
        if (DataBase.isFather(q2.longValue())) {
            return IS_ADMINISTRATOR_TIPS[Tool.RANDOM.nextInt(IS_ADMINISTRATOR_TIPS.length)];
        } else {
            return IST_ADMINISTRATOR_TIPS[Tool.RANDOM.nextInt(IST_ADMINISTRATOR_TIPS.length)];
        }
    }

    public static final String[] ADD_FATHER_TIPS = new String[]
            {"好的", "听你的", "OK", "嗯嗯", "真好"};
    public static final String[] NDD_FATHER_TIPS = new String[]
            {"不要", "为啥", "凭什么", "阿巴巴", "就不"};

    @Action("现在是了")
    public Object isAdministratorNow(User qq) {
        Long q2 = UP_AKS.get(qq.getId());
        if (q2 == null || q2 == 0) {
            return "是什么?";
        }
        if (DataBase.isFather(q2)) {
            return "ta本来就是好吧...";
        }
        if (qq.getId() == Resource.superQL) {
            DataBase.addFather(q2);
            return getRandString(ADD_FATHER_TIPS);
        } else {
            return getRandString(NDD_FATHER_TIPS);
        }
    }

    public static final String[] EDD_FATHER_TIPS =
            new String[]{"好的", "收到", "OK", "完成", "弄好了"};

    @Action("现在不是了")
    public Object istAdministratorNow(User qq) {
        Long q2 = UP_AKS.get(qq.getId());
        if (q2 == null || q2 == 0) {
            return "不是什么?";
        }
        if (!DataBase.isFather(q2)) {
            return "ta本来就不是好吧...";
        }
        if (qq.getId() == Resource.superQL) {
            DataBase.removeFather(q2);
            return getRandString(EDD_FATHER_TIPS);
        } else {
            return getRandString(NDD_FATHER_TIPS);
        }
    }

    @Action("关机")
    public String k0() {
        Switch.AllK = false;
        return "已关机..";
    }

    @Action("开机")
    public String k1() {
        Switch.AllK = true;
        return "已开机..";
    }

    @AutoStand
    IGameService gameService;

    @Action("超级侦查<.+=>name>")
    public String select(User qq, @AllMess String chain, Group group) {
        long who = MessageTools.getAtFromString(chain);
        if (who == -1)
            return ("谁?");
        if (!GameDataBase.exist(who)) return ("该玩家尚未注册");
        PersonInfo I = GameDataBase.getInfo(qq.getId());
        PersonInfo Y = GameDataBase.getInfo(who);
        StringBuilder m1 = new StringBuilder();
        m1.append("ta的信息\n");
        String sss = gameService.info(who);
        m1.append(sss);
        return m1.toString();
    }

    @Action("更新宵禁<.+=>str>")
    public String a0(@Param("str") String str, Group group) {
        String[] ss = getTime(str);
        if (ss == null) {
            return "格式错误!!";
        } else {
            Curfew curfew = Curfew.getInstance(group.getId());
            curfew.getFroms().clear();
            curfew.getFroms().add(ss[0]);
            curfew.getTos().clear();
            curfew.getTos().add(ss[1]);
            curfew.save();
            CurfewScheduler.update(curfew);
        }
        return "ok";
    }

    public static String[] getTime(String mess) {
        String[] ss = mess.split("-");
        try {
            Curfew.FORMAT.parse(ss[0]);
            Curfew.FORMAT.parse(ss[1]);
            return ss;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Action("新增宵禁<.+=>str>")
    public String a1(@Param("str") String str, Group group) {
        String[] ss = getTime(str);
        if (ss == null) {
            return "格式错误!!";
        } else {
            CurfewScheduler.add(group.getId(), ss[0], ss[1]);
        }
        return "ok";
    }

    @Action("/即时公告.+")
    public String announcement(@AllMess String str) {
        for (net.mamoe.mirai.contact.Group group : bot.getGroups()) {
            group.sendMessage(str);
        }
        return "ok";
    }

    @Action("/clearCache")
    public Object m4() {
        try {
            return HIST_U_SCORE.size() + " will clear\n" + HIST_INFOS.size() + " will clear";
        } finally {
            HIST_U_SCORE.clear();
            HIST_INFOS.clear();
            Tool.deleteDir(new File("./temp"));
        }
    }

    @Action("/添加物品<.+=>str>")
    public Object add0(@Param("str") String str) {
        Long who = MessageTools.getAtFromString(str);
        if (who == -1) {
            return NOT_FOUND_AT;
        }
        str = str.replace(who.toString(), "");
        String what = str.trim().replaceAll(",", "").replaceAll("个", "");
        Integer num = null;
        try {
            num = Integer.valueOf(Tool.findNumberFromString(what));
            what = what.replaceFirst(num + "", "");
        } catch (Exception e) {
            num = null;
        }
        Integer id = GameDataBase.NAME_2_ID_MAPS.get(what);
        if (id == null) return ERR_TIPS;
        for (Integer integer = 0; integer < num; integer++) {
            getBagMapper().insertWithDesc(id, who.longValue(), System.currentTimeMillis(), "补偿添加");
        }
        return OK_TIPS;
    }
}
