package Project.Controllers.NormalController;


import Entitys.Group;
import Entitys.User;
import Project.Controllers.ConfirmController;
import Project.Controllers.ControllerTool;
import Project.DataBases.DataBase;
import Project.Services.Iservice.IManagerService;
import Project.Tools.Tool;
import io.github.kloping.Mirai.Main.Handlers.MyHandler;
import io.github.kloping.Mirai.Main.ITools.MemberTools;
import io.github.kloping.Mirai.Main.ITools.MessageTools;
import io.github.kloping.Mirai.Main.ITools.Saver;
import io.github.kloping.Mirai.Main.Resource;
import io.github.kloping.MySpringTool.annotations.*;
import io.github.kloping.MySpringTool.exceptions.NoRunException;
import net.mamoe.mirai.contact.Member;
import net.mamoe.mirai.contact.NormalMember;
import net.mamoe.mirai.message.data.MessageChain;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static io.github.kloping.Mirai.Main.ITools.MessageTools.getAtFromString;
import static io.github.kloping.Mirai.Main.Resource.Switch.AllK;
import static io.github.kloping.Mirai.Main.Resource.*;

@Controller
public class ManagerController {
    public ManagerController() {
        println(this.getClass().getSimpleName() + "构建");
    }

    @AutoStand
    IManagerService managerService;

    @Before
    public void before(@AllMess String mess, Group group, User qq) throws NoRunException {
        if (!AllK)
            throw new NoRunException();
        if (qq.getId() == Long.parseLong(superQ)) {
            println("超级权限执行...");
            return;
        }
        if (!DataBase.isFather(qq.getId())) {
            throw new NoRunException("无权限");
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

    @Action("好友请求")
    public String A1() {
        MyHandler.autoAcceptFriend = !MyHandler.autoAcceptFriend;
        return MyHandler.autoAcceptFriend ? "当前开启" : "当前关闭";
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

    @Action(value = "开启", otherName = "说话")
    public String open(Entitys.Group group) {
        ControllerTool.removeGroup(group.getId());
        return DataBase.openGroup(group.getId()) ? "已经开启" : "开启成功";
    }

    @Action(value = "关闭", otherName = "闭嘴")
    public String close(Entitys.Group group) {
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
    public String Out(long q, Group gr, @AllMess String chain) {
        Number[] numbers = getAllAt(chain);
        if (numbers.length == 0) return "谁?";
        if (numbers.length == 1) {
            long who = numbers[0].longValue();
            net.mamoe.mirai.contact.Group group = bot.getGroup(gr.getId());
            NormalMember m1 = group.get(Resource.qq.getQq());
            NormalMember m2 = group.get(who);
            if (m1.getPermission().getLevel() == 0) {
                return "我不是管理员，抱歉了。。";
            } else {
                if (m2.getPermission().getLevel() > 0) {
                    return "ta也是管理员啊。。";
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
                Method method = this.getClass().getDeclaredMethod("KickNum", Number[].class, Group.class);
                Object[] objects = new Object[]{
                        method, this, new Object[]{numbers, gr}
                };
                ConfirmController.RegConfirm(q, objects);
                return "批量踢,请确认";
            } catch (Exception e) {
                e.printStackTrace();
                return "异常";
            }
        }
    }

    private String KickNum(Number[] numbers, Group g) {
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
        return managerService.NotSpeak(group.get(who), str.replace(who + "", ""), group);
    }

    @Action("解除禁言.{1,}")
    public String UnBan(User qq, Group egroup, @AllMess String chain) {
        long who = MessageTools.getAtFromString(chain);
        if (who == -1)
            return "谁？";
        net.mamoe.mirai.contact.Group group = bot.getGroup(egroup.getId());
        String str = managerService.NotSpeak(group.get(who), "0秒", group);
        if ("尝试禁言 ta 0秒".equals(str)) {
            return "解除成功";
        } else {
            return str;
        }
    }

    @Action("撤回.+")
    public String Recall(@AllMess String str, Group group) {
        try {
            long at = getAtFromString(str);
            str = str.replace("[@" + at + "]", "").replace("撤回", "");
            int[] is = Tool.StringToInts(str, 1);
            is = is == null || is.length == 0 ? new int[]{0} : is;
            return managerService.BackMess(bot.getGroup(group.getId()), at, group.getId(), is);
        } catch (Exception e) {
            e.printStackTrace();
            return "未知异常";
        }
    }

    @Action(value = "yousend.+", otherName = {"[@me]跟我说<.+=>str>"})
    public String Isay(@AllMess String str, Group group) {
        return str.substring(7);
    }

    @Action("eddScore<.+=>n>")
    public String eddScore(@AllMess String messages, User qq, Group gr) throws NoRunException {
        if (qq.getId() == superQL) {
            long who = MessageTools.getAtFromString(messages);
            if (who == -1) return ("Are You True??");
            long num = Long.parseLong(Tool.findNumberFromString(messages));
            DataBase.addScore(-num, who);
            return new StringBuilder().append("给 =》 ").append(MemberTools.getNameFromGroup(who, gr)).append("增加了\r\n=>").append(-num + "").append("积分").toString();
        } else throw new NoRunException();
    }

    @Action("/superGet<.+=>str>")
    public void mn2(@Param("str") String str, Group group) {
        try {
            String[] ss = str.split(":");
            long gid = Long.parseLong(ss[0]);
            long q = Long.parseLong(ss[1]);
            int[] ints = Tool.StringToInts(ss[2]);
            List<String> strings = Arrays.asList(Saver.getTexts(gid, q, ints));
            for (String s1 : strings) {
                Object o = MessageChain.deserializeFromJsonString(s1);
                MessageTools.sendMessageInGroup(o, group.getId());
            }
//            List<String> strings = Arrays.asList(Saver.getTexts(group.getId(), q));
//            for (int n : ints) {
//                String s1 = strings.get(n);
//                Object o = MessageChain.deserializeFromJsonString(s1);
//                MessageTools.sendMessageInGroup(o, group.getId());
//            }
        } catch (Exception e) {
            e.printStackTrace();
            MessageTools.sendMessageInGroup("Not Found", group.getId());
            throw new NoRunException();
        }
    }

    @Action("/superGetJson<.+=>str>")
    public void mn3(@Param("str") String str, Group group) {
        try {
            String[] ss = str.split(":");
            long gid = Long.parseLong(ss[0]);
            long q = Long.parseLong(ss[1]);
            int[] ints = Tool.StringToInts(ss[2]);
            ints = new int[]{ints[0]};
            List<String> strings = Arrays.asList(Saver.getTexts(gid, q, ints));
            for (String s1 : strings) {
                MessageTools.sendStringInGroup(s1, group.getId());
            }
//            List<String> strings = Arrays.asList(Saver.getTexts(group.getId(), q));
//            for (int n : ints) {
//                String s1 = strings.get(n);
//                Object o = MessageChain.deserializeFromJsonString(s1);
//                MessageTools.sendMessageInGroup(o, group.getId());
//            }
        } catch (Exception e) {
            e.printStackTrace();
            MessageTools.sendMessageInGroup("Not Found", group.getId());
            throw new NoRunException();
        }
    }

    @Action("/get<.+=>str>")
    public void mn1(@Param("str") String str, Group group) {
        try {
            String[] ss = str.split(":");
            long q = Long.parseLong(ss[0]);
            int[] ints = Tool.StringToInts(ss[1]);
            List<String> strings = Arrays.asList(Saver.getTexts(group.getId(), q, ints));
            for (String s1 : strings) {
                Object o = MessageChain.deserializeFromJsonString(s1);
                MessageTools.sendMessageInGroup(o, group.getId());
            }
//            List<String> strings = Arrays.asList(Saver.getTexts(group.getId(), q));
//            for (int n : ints) {
//                String s1 = strings.get(n);
//                Object o = MessageChain.deserializeFromJsonString(s1);
//                MessageTools.sendMessageInGroup(o, group.getId());
//            }
        } catch (Exception e) {
            e.printStackTrace();
            MessageTools.sendMessageInGroup("Not Found", group.getId());
            throw new NoRunException();
        }
    }

    @Action("设置成语接龙最大失败次数<\\d=>s>")
    public String m1(@Param("s") Integer s) {
        if (s != null && s > 0) {
            s = s > 10 ? 10 : s;
            EntertainmentController.maxFail = s;
            return "设置最大次数为: " + s;
        }
        return "&error";
    }
}