package Project.controllers;

import Project.dataBases.DataBase;
import Project.dataBases.GameDataBase;
import Project.dataBases.skill.SkillDataBase;
import Project.detailPlugin.CurfewScheduler;
import Project.interfaces.Iservice.IGameService;
import Project.interfaces.Iservice.IManagerService;
import Project.services.impl.ZongMenServiceImpl;
import io.github.kloping.MySpringTool.StarterApplication;
import io.github.kloping.MySpringTool.annotations.*;
import io.github.kloping.MySpringTool.exceptions.NoRunException;
import io.github.kloping.mirai0.Main.ITools.Client;
import io.github.kloping.mirai0.Main.ITools.MemberTools;
import io.github.kloping.mirai0.Main.ITools.MessageTools;
import io.github.kloping.mirai0.commons.*;
import io.github.kloping.mirai0.unitls.Tools.Tool;

import java.io.File;

import static Project.aSpring.SpringBootResource.getBagMapper;
import static Project.dataBases.DataBase.HIST_U_SCORE;
import static Project.dataBases.DataBase.putInfo;
import static Project.dataBases.GameDataBase.HIST_INFOS;
import static Project.dataBases.GameDataBase.getInfo;
import static io.github.kloping.mirai0.Main.ITools.MemberTools.getUser;
import static io.github.kloping.mirai0.Main.Resource.*;
import static io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet.FinalFormat.AT_FORMAT;
import static io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet.FinalString.*;
import static io.github.kloping.mirai0.unitls.Tools.Tool.findNumberFromString;
import static io.github.kloping.mirai0.unitls.Tools.Tool.getTime;

/**
 * @author github-kloping
 */
@Controller
public class SuperController {

    public long tempSuperL = -1;

    @AutoStand
    IGameService gameService;

    @AutoStand
    IManagerService managerService;

    @AutoStand
    private ZongMenServiceImpl zons;

    public SuperController() {
        println(this.getClass().getSimpleName() + "构建");
    }

    @Before
    public void before(@AllMess String mess, Group group, User qq) throws NoRunException {
        if (tempSuperL != -1L) {
            if (qq.getId() == tempSuperL) {
                tempSuperL = -1L;
                return;
            }
        }
        if (!isSuperQ(qq.getId())) {
            throw new NoRunException("can`t do this");
        }
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

    @Action(value = "addScore.{1,}", otherName = {"加积分.+"})
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
        HIST_U_SCORE.forEach((k, v) -> {
            v.addScore(num);
            putInfo(v);
        });
        return "完成!!";
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

    @Action("超级侦查<.+=>name>")
    public String select(User qq, @AllMess String chain, Group group) {
        long who = MessageTools.getAtFromString(chain);
        if (who == -1)
            return ("谁?");
        if (!GameDataBase.exist(who)) return (PLAYER_NOT_REGISTERED);
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

    @Action("添加管理.{1,}")
    public String addFather(@AllMess String message, User qq, Group group) throws NoRunException {
        if (!isSuperQ(qq.getId()))
            throw new NoRunException();
        long who = MessageTools.getAtFromString(message);
        if (who == -1)
            return "添加谁?";
        String perm = message.replace(String.format(AT_FORMAT, who), "");
        perm = perm.replace("添加管理", "");
        if (perm.equals(Father.ALL)) {
            return managerService.addFather(qq.getId(), who);
        } else {
            return managerService.addFather(qq.getId(), who, Long.toString(group.getId()));
        }
    }

    @Action("/c-ms")
    public String o2() {
        THREADS.submit(Client.INSTANCE);
        return "trying";
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
        return null;
    }

    @Action("移除管理.{1,}")
    public String removeFather(@AllMess String message, User qq) throws NoRunException {
        if (isSuperQ(qq.getId()))
            throw new NoRunException();
        long who = MessageTools.getAtFromString(message);
        if (who == -1)
            return "移除谁?";
        return managerService.removeFather(qq.getId(), who);
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
            SkillDataBase.reMap();
            Tool.deleteDir(new File("./temp"));
            MessageTools.HIST_IMAGES.clear();
        }
    }

    @Action("/添加物品<.+=>str>")
    public Object add0(@Param("str") String str) {
        Long who = MessageTools.getAtFromString(str);
        if (who == -1) {
            return NOT_FOUND_AT;
        }
        str = str.replace("[@" + who.toString() + "]", "");
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

    @Action("/跳过闭关冷却.+")
    public String o1(@AllMess String l) {
        long who = MessageTools.getAtFromString(l);
        if (who == -1) return ERR_TIPS;
        getInfo(who).setBgk(0L).apply();
        return "OK";
    }

    @Action("/跳过进入冷却.+")
    public String oo1(@AllMess String mess) {
        try {
            String numStr = findNumberFromString(mess);
            long qid = Long.parseLong(numStr);
            GameDataBase.getInfo(qid).setK2(-1L).apply();
            return "ok";
        } catch (Exception e) {
            e.printStackTrace();
            return "not found";
        }
    }
}
