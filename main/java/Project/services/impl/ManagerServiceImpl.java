package Project.services.impl;


import Project.dataBases.DataBase;
import io.github.kloping.mirai0.unitls.Tools.Tool;
import Project.services.Iservice.IManagerService;
import io.github.kloping.mirai0.Main.ITools.Saver;
import io.github.kloping.mirai0.Main.Resource;
import io.github.kloping.MySpringTool.annotations.Entity;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.Member;
import net.mamoe.mirai.contact.NormalMember;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageSource;

import java.util.Arrays;
import java.util.List;

import static Project.ResourceSet.FinalFormat.TRY_MUTE_SECONDS;
import static Project.ResourceSet.FinalString.*;
import static io.github.kloping.mirai0.Main.Resource.superQL;

/**
 * @author github-kloping
 */
@Entity
public class ManagerServiceImpl implements IManagerService {
    @Override
    public String addFather(long father, long who) {
        if (father == superQL) {
            if (DataBase.addFather(Long.valueOf(who))) {
                return "添加完成";
            } else {
                return "他本来就是";
            }
        } else {
            return "你无权限";
        }
    }

    @Override
    public String removeFather(long father, long who) {
        if (father == superQL) {
            if (DataBase.removeFather(Long.valueOf(who))) {
                return "移除完成";
            } else {
                return "他本来就不是";
            }
        } else {
            return "你无权限";
        }
    }

    public static final String[] TIME_UNIT = new String[]{SECONDS, MINUTE, HOUR, DAY, MONTH};

    @Override
    public String notSpeak(Member who, String what, Group group) {
        String es = "";
        long t = 1;
        for (String s : TIME_UNIT) {
            if (what.contains(s)) {
                es = s;
            }
        }
        if (es.isEmpty()) {
            return NOT_FOUND_TIME_UNIT;
        }
        String s1 = Tool.findNumberFromString(what);
        if (s1 == null || s1.isEmpty()) {
            s1 = "1";
        }
        long t1 = Long.parseLong(s1);
        switch (es) {
            case SECONDS:
            default:
                if (t1 > 60) {
                    return SECONDS_TOO_MUCH;
                } else {
                    t = t1;
                }
                break;
            case MINUTE:
                if (t1 > 60) {
                    return MINUTE_TOO_MUCH;
                } else {
                    t = t1 * 60;
                }
                break;
            case HOUR:
                if (t1 > 24) {
                    return HOUR_TOO_MUCH;
                } else {
                    t = t1 * 60 * 60;
                }
                break;
            case DAY:
                if (t1 > 60) {
                    return DAY_TOO_MUCH;
                } else {
                    t = t1 * 60 * 60 * 24;
                }
                break;
            case MONTH:
                if (t1 > 1) {
                    return MONTH_TOO_MUCH;
                } else {
                    t = t1 * 60 * 60 * 24 * 30;
                }
                break;

        }
        if (group.get(Resource.qq.getQq()).getPermission().getLevel() == 0) {
            return NOT_MANAGER;
        }
        if (group.get(who.getId()).getPermission().getLevel() > group.get(Resource.qq.getQq()).getPermission().getLevel()) {
            return PERMISSION_DENIED;
        } else {
            NormalMember m1 = (NormalMember) who;
            if (t > 0) {
                m1.mute((int) t);
            } else {
                m1.unmute();
            }
        }
        return String.format(TRY_MUTE_SECONDS, t);
    }

    @Override
    public String backMess(Group group, long who, long g, int... ns) {
        if (group.get(Resource.qq.getQq()).getPermission().getLevel() == 0) {
            return NOT_MANAGER;
        }
        if (group.get(who).getPermission().getLevel() > group.get(Resource.qq.getQq()).getPermission().getLevel()) {
            return PERMISSION_DENIED;
        }
        try {
            List<String> strings = Arrays.asList(Saver.getTexts(g, who, ns));
            if (strings == null || strings.size() == 0) {
                return NOT_FOUND;
            }
            String m = RECALL_SUCCEED;
            for (String text : strings) {
                try {
                    MessageChain chain = MessageChain.deserializeFromJsonString(text);
                    MessageSource.recall(chain);
                } catch (Exception e) {
                    e.printStackTrace();
                    m = RECALL_FAIL;
                }
            }
            return m;
        } catch (Exception e) {
            e.printStackTrace();
            return RECALL_FAIL;
        }
    }
}