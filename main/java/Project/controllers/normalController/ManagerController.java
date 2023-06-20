package Project.controllers.normalController;


import Project.commons.SpGroup;
import Project.commons.SpUser;
import Project.controllers.auto.ConfirmController;
import Project.controllers.auto.ControllerTool;
import Project.dataBases.DataBase;
import Project.interfaces.Iservice.IManagerService;
import Project.listeners.CapHandler;
import Project.listeners.DefaultHandler;
import io.github.kloping.MySpringTool.annotations.*;
import io.github.kloping.MySpringTool.exceptions.NoRunException;
import io.github.kloping.mirai.MiraiRunnable;
import io.github.kloping.mirai0.Main.BootstarpResource;
import io.github.kloping.mirai0.commons.Quiz;
import Project.utils.Tools.Tool;
import io.github.kloping.number.NumberUtils;
import net.mamoe.mirai.contact.Member;
import net.mamoe.mirai.contact.NormalMember;
import net.mamoe.mirai.event.events.BotEvent;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import static Project.commons.rt.ResourceSet.FinalString.*;
import static io.github.kloping.mirai0.Main.BootstarpResource.*;

/**
 * @author github-kloping
 */
@Controller
public class ManagerController {
    @AutoStand
    IManagerService managerService;

    public ManagerController() {
        println(this.getClass().getSimpleName() + "构建");
    }

    public static Number[] getAllAt(String allMess) {
        Set<Number> numbers = new HashSet<>();
        while (true) {
            Long l1 = Project.utils.Utils.getAtFromString(allMess);
            allMess = allMess.replaceFirst("\\[@" + l1 + "]", "");
            if (l1 == -1) break;
            else numbers.add(l1);
        }
        return numbers.toArray(new Number[0]);
    }

    @Before
    public void before(@AllMess String mess, SpGroup group, SpUser qq) throws NoRunException {
        if (isSuperQ(qq.getId())) {
            println("超级权限执行...");
            return;
        } else if (DataBase.isFather(qq.getId(), group.getId())) {
            return;
        } else if (MiraiRunnable.isAdmin(group.getId(), qq.getId())) {
            return;
        }
        throw new NoRunException("无权限");
    }

    @Action("跳过验证.+")
    public String o3(@AllMess String mess) {
        try {
            String numStr = Tool.INSTANCE.findNumberFromString(mess);
            long qid = Long.parseLong(numStr);
            CapHandler.ok(qid);
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return "not found";
        }
    }

    @Action("通过")
    public Object ace(SpUser user, SpGroup group) {
        Member qq = BootstarpResource.BOT.getGroup(group.getId()).get(user.getId());
        if (qq.getPermission().getLevel() >= 1 || DataBase.isFather(qq.getId(), group.getId()))
            if (DefaultHandler.joinRequestEvent != null) {
                DefaultHandler.joinRequestEvent.accept();
                DefaultHandler.joinRequestEvent = null;
                return "已通过!!";
            }
        throw new NoRunException();
    }

    @Action("不通过")
    public Object rej(SpUser user, SpGroup group) {
        Member qq = BootstarpResource.BOT.getGroup(group.getId()).get(user.getId());
        if (qq.getPermission().getLevel() >= 1 || DataBase.isFather(qq.getId(), group.getId()))
            if (DefaultHandler.joinRequestEvent != null) {
                DefaultHandler.joinRequestEvent.reject();
                DefaultHandler.joinRequestEvent = null;
                return "已拒绝!!";
            }
        throw new NoRunException();
    }

    @Action(value = OPEN_STR, otherName = "说话")
    public String open(SpGroup group) {
        ControllerTool.removeGroup(group.getId());
        return DataBase.openGroup(group.getId()) ? "已经开启" : "开启成功";
    }

    @Action(value = CLOSE_STR, otherName = "闭嘴")
    public String close(SpGroup group) {
        ControllerTool.removeGroup(group.getId());
        return DataBase.closeGroup(group.getId()) ? "关闭成功" : "已经关闭";
    }

    @Action("开启验证")
    private String m1(SpGroup group) {
        DataBase.setCap(group.getId(), true);
        return "开启完成";
    }

    @Action("关闭验证")
    private String m2(SpGroup group) {
        DataBase.setCap(group.getId(), false);
        return "关闭完成";
    }

    @Action("开启闪照破解")
    public String openFlash(SpGroup group) {
        return DataBase.openShow(group.getId()) ? "开启闪照破解成功" : "已经开启闪照破解";
    }

    @Action("关闭闪照破解")
    public String closeFlash(SpGroup group) {
        return DataBase.closeShow(group.getId()) ? "关闭闪照破解成功" : "已经关闭闪照破解";
    }

    @Action("开启聊天")
    public String openTalk(SpGroup group) {
        DataBase.setSpeak(group.getId(), true);
        return "来吧聊天";
    }

    @Action("关闭聊天")
    public String closeTalk(SpGroup group) {
        DataBase.setSpeak(group.getId(), false);
        return "不想聊了";
    }

    @Action(value = "踢.{1,}", otherName = "T.{1,}")
    public String out(long q, SpGroup gr, @AllMess String chain, BotEvent event) {
        Number[] numbers = getAllAt(chain);
        if (numbers.length == 0) {
            return NOT_FOUND_AT;
        }
        if (numbers.length == 1) {
            long who = numbers[0].longValue();
            net.mamoe.mirai.contact.Group group = BOT.getGroup(gr.getId());
            NormalMember m1 = group.get(event.getBot().getId());
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
                Method method = this.getClass().getDeclaredMethod("kickNum", Number[].class, SpGroup.class);
                ConfirmController.regConfirm(q, method, this, new Object[]{numbers, gr});
                return "批量踢,请确认";
            } catch (Exception e) {
                e.printStackTrace();
                return "异常";
            }
        }
    }

    private String kickNum(Number[] numbers, SpGroup g) {
        try {
            net.mamoe.mirai.contact.Group group = BOT.getGroup(g.getId());
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

    @Action("禁言<.{1,}=>str>")
    public String Ban(SpUser qq, SpGroup egroup, @Param("str") String str, @AllMess String chain) {
        long who = Project.utils.Utils.getAtFromString(chain);
        if (who == -1) return NOT_FOUND_AT;
        net.mamoe.mirai.contact.Group group = BOT.getGroup(egroup.getId());
        return managerService.notSpeak(group.get(who), str.replace(who + "", ""), group);
    }

    @Action("解除禁言.{1,}")
    public String UnBan(SpUser qq, SpGroup egroup, @AllMess String chain) {
        long who = Project.utils.Utils.getAtFromString(chain);
        if (who == -1) return "谁？";
        net.mamoe.mirai.contact.Group group = BOT.getGroup(egroup.getId());
        String str = managerService.notSpeak(group.get(who), "0秒", group);
        return TRY_UNMUTE;
    }

    @Action("撤回.+")
    public String recall(@AllMess String str, SpGroup group) {
        try {
            long at = Project.utils.Utils.getAtFromString(str);
            str = str.replace("[@" + at + "]", "").replace("撤回", "");
            if (str.trim().matches("最近\\d+条")) {
                int[] is;
                int i = Tool.INSTANCE.getInteagerFromStr(str);
                i = i > 15 ? 15 : i;
                is = new int[i];
                for (int i1 = 0; i1 < i; i1++) {
                    is[i1] = i1;
                }
                return managerService.backMess(BOT.getGroup(group.getId()), at, group.getId(), is);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ERR_TIPS;
    }

    @Action(value = "yousend.+")
    public String isay(@AllMess String str, SpGroup group) {
        return str.substring(7);
    }

    @Action("创建竞猜.+")
    public String s3(@AllMess String all) {
        if (Quiz.quiz != null) return "竞猜中...";
        Quiz.quiz = new Quiz();
        String[] sss = all.substring(4).split(";");
        int i = 0;
        for (String s : sss) {
            if (i == 0) {
                Quiz.quiz.setTitle(s);
            } else {
                Quiz.quiz.getQuizData().put(i, s);
            }
            i++;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("创建成功\n").append(Quiz.quiz.getTitle()).append(NEWLINE);
        Quiz.quiz.getQuizData().forEach((k, v) -> {
            sb.append(k).append(".").append(v).append(NEWLINE);
        });
        sb.append("发'竞猜1,100' 来猜1并投注100积分");
        return sb.toString();
    }

    @Action("结束竞猜.+")
    public String s4(@AllMess String all) {
        if (Quiz.quiz == null) return "未开始竞猜";
        Integer index = Tool.INSTANCE.getInteagerFromStr(all);
        index = index == null ? 0 : index;
        if (index == 0) {
            for (Quiz.QuizSon quizSon : Quiz.quiz.getQuizSons()) {
                DataBase.getUserInfo(quizSon.getQid()).addScore(quizSon.getSc());
            }
            Quiz.quiz = null;
            return "强制结束";
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("竞猜结束\n").append(Quiz.quiz.getTitle()).append(NEWLINE).append("结果为:").append(NEWLINE).append(index).append(".").append(Quiz.quiz.getQuizData().get(index)).append("==>>\n");
            long win = 0;
            long a0 = 0;
            for (Quiz.QuizSon quizSon : Quiz.getQuiz().getQuizSons()) {
                if (quizSon.getIndex() == index) {
                    win += quizSon.getSc();
                }
                a0 += quizSon.getSc();
            }

            for (Quiz.QuizSon quizSon : Quiz.getQuiz().getQuizSons()) {
                if (quizSon.getIndex() == index) {
                    long s0 = quizSon.getSc();
                    int b0 = NumberUtils.toPercent(s0, win);
                    long wg = NumberUtils.percentTo(b0, a0);
                    DataBase.getUserInfo(quizSon.getQid()).addScore(wg);
                    sb.append(quizSon.getQid()).append("获得").append(wg).append(NEWLINE);
                }
            }
            Quiz.quiz = null;
            return sb.toString();
        }
    }

    @Action("停止竞猜.+")
    public String s5() {
        if (Quiz.quiz == null) return "未开始竞猜";
        Quiz.getQuiz().setProcessing(false);
        return "已停止";
    }
}