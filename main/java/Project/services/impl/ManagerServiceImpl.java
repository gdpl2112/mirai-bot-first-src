package Project.services.impl;


import Project.aSpring.SaverSpringStarter;
import Project.dataBases.DataBase;
import Project.interfaces.Iservice.IManagerService;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import io.github.kloping.MySpringTool.annotations.Entity;
import io.github.kloping.mirai0.Main.Handlers.AllMessage;
import io.github.kloping.mirai0.Main.Resource;
import io.github.kloping.mirai0.commons.Father;
import io.github.kloping.mirai0.unitls.Tools.Tool;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.Member;
import net.mamoe.mirai.contact.NormalMember;
import net.mamoe.mirai.message.data.MessageSource;
import net.mamoe.mirai.message.data.MessageSourceBuilder;
import net.mamoe.mirai.message.data.MessageSourceKind;

import java.util.List;

import static io.github.kloping.mirai0.Main.Resource.isSuperQ;
import static io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet.FinalFormat.TRY_MUTE_SECONDS;
import static io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet.FinalString.*;

/**
 * @author github-kloping
 */
@Entity
public class ManagerServiceImpl implements IManagerService {
    public static final String[] TIME_UNIT = new String[]{SECONDS, MINUTE, HOUR, DAY, MONTH};

    @Override
    public String addFather(long father, long who) {
        return addFather(father, who, Father.ALL);
    }

    @Override
    public String addFather(long father, long who, String perm) {
        if (isSuperQ(father)) {
            if (DataBase.addFather(Long.valueOf(who), perm)) {
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
        if (isSuperQ(father)) {
            if (DataBase.removeFather(Long.valueOf(who))) {
                return "移除完成";
            } else {
                return "他本来就不是";
            }
        } else {
            return "你无权限";
        }
    }

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
        String tips = RECALL_SUCCEED;
        List<AllMessage> messages = SaverSpringStarter.saveMapper.selectMessage(g, who, ns.length);
        for (AllMessage message : messages) {
            try {
                MessageSourceBuilder builder = new MessageSourceBuilder();
                builder.setInternalIds(new int[]{message.getInternalId()});
                builder.setIds(new int[]{message.getId()});
                builder.setFromId(message.getSenderId());
                builder.setTime(message.getIntTime());
                builder.setTargetId(message.getFromId());
                MessageSource source = builder.build(message.getBotId(), MessageSourceKind.GROUP);
                MessageSource.recall(source);
                UpdateWrapper<AllMessage> updateWrapper = new UpdateWrapper<>();
                updateWrapper.eq("internal_id", message.getInternalId());
                message.setRecalled(1);
                SaverSpringStarter.saveMapper.update(message, updateWrapper);
            } catch (Exception e) {
                e.printStackTrace();
                tips = RECALL_FAIL;
            }
        }
        return tips;
    }
}