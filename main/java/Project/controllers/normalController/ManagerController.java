package Project.controllers.normalController;


import Project.controllers.auto.ConfirmController;
import Project.controllers.auto.ControllerTool;
import Project.dataBases.DataBase;
import Project.dataBases.GameDataBase;
import Project.interfaces.Iservice.IManagerService;
import io.github.kloping.MySpringTool.annotations.*;
import io.github.kloping.MySpringTool.exceptions.NoRunException;
import io.github.kloping.mirai0.Entitys.Group;
import io.github.kloping.mirai0.Entitys.User;
import io.github.kloping.mirai0.Main.Handlers.CapHandler;
import io.github.kloping.mirai0.Main.Handlers.MyHandler;
import io.github.kloping.mirai0.Main.ITools.MessageTools;
import io.github.kloping.mirai0.Main.Resource;
import io.github.kloping.mirai0.unitls.Tools.Tool;
import net.mamoe.mirai.contact.Member;
import net.mamoe.mirai.contact.NormalMember;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import static Project.ResourceSet.FinalString.*;
import static io.github.kloping.mirai0.Main.ITools.MessageTools.getAtFromString;
import static io.github.kloping.mirai0.Main.Resource.*;

/**
 * @author github-kloping
 */
@Controller
public class ManagerController {
    public ManagerController() {
        println(this.getClass().getSimpleName() + "构建");
    }

    @AutoStand
    IManagerService managerService;

    @Before
    public void before(@AllMess String mess, Group group, User qq) throws NoRunException {
        if (qq.getId() == Long.parseLong(superQ)) {
            println("超级权限执行...");
            return;
        } else if (!DataBase.isFather(qq.getId())) {
            throw new NoRunException("无权限");
        }
    }

    @Action("跳过验证.+")
    public String o3(@AllMess String mess) {
        try {
            String numStr = Tool.findNumberFromString(mess);
            long qid = Long.parseLong(numStr);
            CapHandler.ok(qid);
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return "not found";
        }
    }

    @Action("跳过进入冷却.+")
    public String oo1(@AllMess String mess) {
        try {
            String numStr = Tool.findNumberFromString(mess);
            long qid = Long.parseLong(numStr);
            GameDataBase.getInfo(qid).setK2(-1L).apply();
            return "ok";
        } catch (Exception e) {
            e.printStackTrace();
            return "not found";
        }
    }

    @Action("通过")
    public Object ace(User user, Group group) {
        Member qq = Resource.bot.getGroup(group.getId()).get(user.getId());
        if (qq.getPermission().getLevel() >= 1 || DataBase.isFather(qq.getId()))
            if (MyHandler.joinRequestEvent != null) {
                MyHandler.joinRequestEvent.accept();
                MyHandler.joinRequestEvent = null;
                return "已通过!!";
            }
        throw new NoRunException();
    }

    @Action("不通过")
    public Object rej(User user, Group group) {
        Member qq = Resource.bot.getGroup(group.getId()).get(user.getId());
        if (qq.getPermission().getLevel() >= 1 || DataBase.isFather(qq.getId()))
            if (MyHandler.joinRequestEvent != null) {
                MyHandler.joinRequestEvent.reject();
                MyHandler.joinRequestEvent = null;
                return "已拒绝!!";
            }
        throw new NoRunException();
    }

    @Action(value = OPEN_STR, otherName = "说话")
    public String open(io.github.kloping.mirai0.Entitys.Group group) {
        ControllerTool.removeGroup(group.getId());
        return DataBase.openGroup(group.getId()) ? "已经开启" : "开启成功";
    }

    @Action(value = CLOSE_STR, otherName = "闭嘴")
    public String close(io.github.kloping.mirai0.Entitys.Group group) {
        ControllerTool.removeGroup(group.getId());
        return DataBase.closeGroup(group.getId()) ? "关闭成功" : "已经关闭";
    }

    @Action("开启验证")
    private String m1(Group group) {
        DataBase.setCap(group.getId(), true);
        return "开启完成";
    }

    @Action("关闭验证")
    private String m2(Group group) {
        DataBase.setCap(group.getId(), false);
        return "关闭完成";
    }

    @Action("添加管理.{1,}")
    public String addFather(@AllMess String message, User qq) throws NoRunException {
        if (qq.getId() != superQL)
            throw new NoRunException();
        long who = MessageTools.getAtFromString(message);
        if (who == -1)
            return "添加谁?";
        return managerService.addFather(qq.getId(), who);
    }

    @Action("移除管理.{1,}")
    public String removeFather(@AllMess String message, User qq) throws NoRunException {
        if (qq.getId() != superQL)
            throw new NoRunException();
        long who = MessageTools.getAtFromString(message);
        if (who == -1)
            return "移除谁?";
        return managerService.removeFather(qq.getId(), who);
    }

    @Action("开启闪照破解")
    public String openFlash(Group group) {
        return DataBase.openShow(group.getId()) ? "开启闪照破解成功" : "已经开启闪照破解";
    }

    @Action("关闭闪照破解")
    public String closeFlash(Group group) {
        return DataBase.closeShow(group.getId()) ? "关闭闪照破解成功" : "已经关闭闪照破解";
    }

    @Action("开启聊天")
    public String openTalk(Group group) {
        DataBase.setSpeak(group.getId(), true);
        return "来吧聊天";
    }

    @Action("关闭聊天")
    public String closeTalk(Group group) {
        DataBase.setSpeak(group.getId(), false);
        return "不想聊了";
    }

    private final static String[] sss = {"你好啊!", "嘿,老Baby", "在吗 ", "吃了没"};

    @Action(value = "与ta互动.{1,}", otherName = {"与他互动.{1,}", "与她互动.{1,}"})
    public String talkWith(User qq, @AllMess String chain) {
        long who = MessageTools.getAtFromString(chain);
        if (who == -1)
            return new StringBuilder().append("谁?").toString();
        return new StringBuilder().append(String.format("&[At:%s]", who)).append("\r\n").append(sss[Tool.rand.nextInt(sss.length - 1)]).toString();
    }

    @Action(value = "踢.{1,}", otherName = "T.{1,}")
    public String out(long q, Group gr, @AllMess String chain) {
        Number[] numbers = getAllAt(chain);
        if (numbers.length == 0) {
            return NOT_FOUND_AT;
        }
        if (numbers.length == 1) {
            long who = numbers[0].longValue();
            net.mamoe.mirai.contact.Group group = bot.getGroup(gr.getId());
            NormalMember m1 = group.get(Resource.qq.getQq());
            NormalMember m2 = group.get(who);
            if (m1.getPermission().getLevel() == 0) {
                return PERMISSION_DENIED;
            } else {
                if (m2.getPermission().getLevel() > m1.getPermission().getLevel()) {
                    return NO_PERMISSION_STR;
                }
                String name = m2.getNameCard();
                try {
                    m2.kick(String.valueOf(who));
                    return "踢出 " + name + " 成功";
                } catch (Exception e) {
                    return "踢出 " + name + " 失败";
                }
            }
        } else {
            try {
                Method method = this.getClass().getDeclaredMethod("kickNum", Number[].class, Group.class);
                Object[] objects = new Object[]{
                        method, this, new Object[]{numbers, gr}
                };
                ConfirmController.regConfirm(q, objects);
                return "批量踢,请确认";
            } catch (Exception e) {
                e.printStackTrace();
                return "异常";
            }
        }
    }

    private String kickNum(Number[] numbers, Group g) {
        try {
            net.mamoe.mirai.contact.Group group = bot.getGroup(g.getId());
            for (Number n : numbers) {
                try {
                    long q1 = n.longValue();
                    group.get(q1).kick(Long.toString(n.longValue()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return "执行完毕";
        } catch (Exception e) {
            e.printStackTrace();
            return "异常执行";
        }
    }

    private static Number[] getAllAt(String allMess) {
        Set<Number> numbers = new HashSet<>();
        while (true) {
            Long l1 = MessageTools.getAtFromString(allMess);
            allMess = allMess.replaceFirst("\\[@" + l1 + "\\]", "");
            if (l1 == -1) break;
            else numbers.add(l1);
        }
        return numbers.toArray(new Number[0]);
    }

    @Action("禁言<.{1,}=>str>")
    public String Ban(User qq, Group egroup, @Param("str") String str, @AllMess String chain) {
        long who = MessageTools.getAtFromString(chain);
        if (who == -1)
            return "谁？";
        net.mamoe.mirai.contact.Group group = bot.getGroup(egroup.getId());
        return managerService.notSpeak(group.get(who), str.replace(who + "", ""), group);
    }

    @Action("解除禁言.{1,}")
    public String UnBan(User qq, Group egroup, @AllMess String chain) {
        long who = MessageTools.getAtFromString(chain);
        if (who == -1)
            return "谁？";
        net.mamoe.mirai.contact.Group group = bot.getGroup(egroup.getId());
        String str = managerService.notSpeak(group.get(who), "0秒", group);
        return TRY_UNMUTE;
    }

    @Action("撤回.+")
    public String recall(@AllMess String str, Group group) {
        try {
            long at = getAtFromString(str);
            str = str.replace("[@" + at + "]", "").replace("撤回", "");
            if (str.trim().matches("最近\\d+条")) {
                int[] is;
                int i = Tool.getInteagerFromStr(str);
                i = i > 15 ? 15 : i;
                is = new int[i];
                for (int i1 = 0; i1 < i; i1++) {
                    is[i1] = i1;
                }
                return managerService.backMess(bot.getGroup(group.getId()), at, group.getId(), is);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ERR_TIPS;
    }

    @Action(value = "yousend.+", otherName = {"[@me]跟我说<.+=>str>"})
    public String isay(@AllMess String str, Group group) {
        return str.substring(7);
    }
}