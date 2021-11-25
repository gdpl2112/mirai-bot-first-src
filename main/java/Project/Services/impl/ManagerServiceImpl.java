package Project.Services.impl;


import Project.DataBases.DataBase;
import Project.Services.Iservice.IManagerService;
import Project.Tools.Tool;
import io.github.kloping.Mirai.Main.ITools.Saver;
import io.github.kloping.Mirai.Main.Resource;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.Member;
import net.mamoe.mirai.contact.NormalMember;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageSource;

import java.util.Arrays;
import java.util.List;

import static io.github.kloping.Mirai.Main.Resource.superQL;

import io.github.kloping.MySpringTool.annotations.Entity;

@Entity
public class ManagerServiceImpl implements IManagerService {

    @Override
    public String addFather(long father, long who) {
        if (father == superQL) {
            if (DataBase.addFather(Long.valueOf(who)))
                return "添加完成";
            else
                return "他本来就是";
        } else {
            return "你无权限";
        }
    }

    @Override
    public String removeFather(long father, long who) {
        if (father == superQL) {
            if (DataBase.removeFather(Long.valueOf(who)))
                return "移除完成";
            else
                return "他本来就不是";
        } else {
            return "你无权限";
        }
    }

    private static final String[] sss = new String[]{"秒", "分", "时", "天", "月"};

    @Override
    public String NotSpeak(Member who, String what, Group group) {
        String es = "";
        long t = 1;
        for (String s : sss) {
            if (what.contains(s)) {
                es = s;
            }
        }
        if (es.isEmpty()) return "没有单位(秒,分,时...)";
        String s1 = Tool.findNumberFromString(what);
        if (s1 == null || s1.isEmpty()) return "没有时长,多少";
        long t1 = Long.parseLong(s1);
        switch (es) {
            case "秒":
                if (t1 > 60)
                    return "超过60秒,请使用分钟";
                else
                    t = t1;
                break;
            case "分":
                if (t1 > 60)
                    return "超过60分,请使用小时";
                else
                    t = t1 * 60;
                break;
            case "时":
                if (t1 > 24)
                    return "超过24小时,请使用天";
                else
                    t = t1 * 60 * 60;
                break;
            case "天":
                if (t1 > 60)
                    return "超过30天,请使用月";
                else
                    t = t1 * 60 * 60 * 24;
                break;
            case "月":
                if (t1 > 1)
                    return "最大1个月";
                else
                    t = t1 * 60 * 60 * 24 * 30;
                break;
        }
        if (group.get(Resource.qq.getQq()).getPermission().getLevel() == 0) {
            return "我不是管理员啊";
        }
        if (group.get(who.getId()).getPermission().getLevel() > 0)
            return "他是管理员 除非我是群主";
        else {
            NormalMember m1 = (NormalMember) who;
            if (t > 0)
                m1.mute((int) t);
            else m1.unmute();
        }
        return "尝试禁言 ta " + t + "秒";
    }

    @Override
    public String BackMess(Group group, long whos, long g, int... ns) {
        if (group.get(Resource.qq.getQq()).getPermission().getLevel() == 0) {
            return "我不是管理员啊";
        }
        if (group.get(whos).getPermission().getLevel() > 0)
            return "他是管理员 无法禁言 除非我是群主";
        try {
//            List<String> strings = Arrays.asList(Saver.getTexts(g, whos));
            List<String> strings = Arrays.asList(Saver.getTexts(g, whos, ns));
            if (strings == null || strings.size() == 0) return "没有发现他ta的消息";
            String m = "撤回成功";
//            for (int n : ns) {
//                try {
//                    String text = strings.get(n);
//                    MessageChain chain = MessageChain.deserializeFromJsonString(text);
//                    MessageSource.recall(chain);
//                    Saver.saveRecalled(text, g, whos);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    m = "部分撤回失败";
//                }
//            }
            for (String text : strings) {
                try {
                    MessageChain chain = MessageChain.deserializeFromJsonString(text);
                    MessageSource.recall(chain);
                    Saver.saveRecalled(text, g, whos);
                } catch (Exception e) {
                    e.printStackTrace();
                    m = "部分撤回失败";
                }
            }
            return m;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "撤回失败";
    }

//    private static void Recall(long id,long st){
//        MessageChain source = MiraiCode.deserializeMiraiCode("[mirai:source:["+id+"],["+st+"]]");
//
//        MessageSource.recallIn(source,1);
//    }
}