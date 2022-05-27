package Project.services.impl;

import Project.broadcast.game.SkillUseBroadcast;
import Project.dataBases.GameDataBase;
import Project.dataBases.SourceDataBase;
import Project.dataBases.skill.SkillDataBase;
import Project.interfaces.Iservice.ISkillService;
import Project.services.detailServices.GameDetailService;
import Project.skill.SkillFactory;
import Project.skill.SkillTemplate;
import io.github.kloping.MySpringTool.annotations.Entity;
import io.github.kloping.mirai0.commons.Group;
import io.github.kloping.mirai0.commons.PersonInfo;
import io.github.kloping.mirai0.commons.Skill;
import io.github.kloping.mirai0.commons.broadcast.enums.ObjType;
import io.github.kloping.mirai0.commons.gameEntitys.SkillInfo;
import io.github.kloping.mirai0.commons.gameEntitys.base.BaseInfoTemp;
import io.github.kloping.mirai0.unitls.Tools.GameTool;
import io.github.kloping.mirai0.unitls.Tools.Tool;

import java.util.Map;
import java.util.concurrent.Future;

import static Project.dataBases.GameDataBase.getInfo;
import static Project.dataBases.GameDataBase.removeFromBgs;
import static Project.dataBases.skill.SkillDataBase.*;
import static Project.services.detailServices.GameSkillDetailService.*;
import static Project.skill.SkillFactory.factory8id;
import static Project.skill.SkillFactory.normalSkillNum;
import static io.github.kloping.mirai0.commons.resouce_and_tool.CommonSource.*;
import static io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet.FinalFormat.*;
import static io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet.FinalNormalString.CANT_USE_ING;
import static io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet.FinalNormalString.VERTIGO_ING;
import static io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet.FinalString.*;
import static io.github.kloping.mirai0.unitls.Tools.Tool.getTimeTips;

/**
 * @author github-kloping
 */
@Entity
public class GameSkillServiceImpl implements ISkillService {
    private static String getIntro(Integer id, Integer jid, Integer st, int wh) {
        try {
            SkillTemplate sk = SkillFactory.factory(jid);
            sk.setId(id);
            sk.setSt(st);
            sk.setHasTime(getDuration(jid).longValue());
            sk.setWh(wh);
            return sk.getContent();
        } catch (Exception e) {
            e.printStackTrace();
            return ERR_TIPS;
        }
    }

    private static void execute(Skill skill) throws Exception {
        Future future = SkillDataBase.threads.submit(skill);
        BaseInfoTemp.append(skill.getPersonInfo().getId().longValue(), future);
    }

    @Override
    public String initSkill(long qq, Group group, Integer st) {
        Integer[] is = GameDataBase.getHhs(qq);
        if (is.length < st || is[0] == 0) return ("你还没有获得对应的魂环");
        Map<Integer, SkillInfo> skinfo = getSkillInfo(qq);
        if (skinfo.containsKey(st)) {
            return ("已经激活了这个魂技");
        } else {
            if (st != 1) {
                if (!skinfo.containsKey(st - 1)) {
                    return ("请先激活前一个魂技");
                }
            }
        }
        Integer id = is[st - 1];
        Integer jid = null;
        if (st > 8) {
            return "更多魂技开发中...";
        } else if (st == 8) {
            jid = factory8id(getInfo(qq).getWh());
        } else if (st == 7) {
            jid = Integer.valueOf(7 + toStr(2, getInfo(qq).getWh()));
        } else {
            while (true) {
                jid = Tool.RANDOM.nextInt(normalSkillNum);
                boolean k = false;
                for (SkillInfo i : skinfo.values()) {
                    if (i.getJid().intValue() == jid.intValue()) {
                        k = true;
                        break;
                    }
                }
                if (k) {
                    continue;
                } else {
                    break;
                }
            }
        }

        SkillInfo info = new SkillInfo()
                .setId(id)
                .setJid(jid)
                .setQq(qq)
                .setTime(1L)
                .setTimeL(getCoolTime(id, jid, getInfo(qq).getWh(), st).longValue() * 60 * 1000L)
                .setSt(st)
                .setAddPercent((int) (getBasePercent(jid) * GameTool.getAHBl_(id)))
                .setUsePercent(getUserPercent(st, jid).intValue());

        SkillDataBase.saveSkillInfo(info);
        int id0 = is[st - 1] + 100;
        return SourceDataBase.getImgPathById(id0) + getIntro(id, jid, st, getInfo(qq).getWh());
    }

    @Override
    public String useSkill(long qq, Integer st, Number[] allAt, String arg, Group group) {
        Map<Integer, SkillInfo> infos = getSkillInfo(qq);
        if (!infos.containsKey(st)) return DONT_HAVE_SKILL;
        SkillInfo info = infos.get(st);
        if (info.getState() < 0) return THIS_SKILL_CANT_USE;
        if (System.currentTimeMillis() < info.getTime())
            return String.format(USE_SKILL_WAIT_TIPS, getTimeTips(info.getTime()));
        PersonInfo personInfo = getInfo(qq);
        if (personInfo.isVertigo()) return VERTIGO_ING;
        if (personInfo.containsTag(TAG_CANT_USE)) return CANT_USE_ING;
        long v = personInfo.getHll();
        long v1 = personInfo.getHl();
        Integer b = toPercent(v1, v);
        if (b < info.getUsePercent()) return String.format(HL_NOT_ENOUGH_TIPS, b);
        Skill skill = null;
        try {
            skill = get(qq, info, allAt);
            skill.setArgs(arg);
            skill.setGroup(group);
            execute(skill);
            SkillUseBroadcast.INSTANCE.broadcast(qq, info.getJid(), st, info);
        } catch (Exception e) {
            e.printStackTrace();
            return ERR_TIPS;
        }
        long uv = percentTo(info.getUsePercent(), personInfo.getHll());
        info.setTime(getCooling(qq, info));
        updateSkillInfo(info);
        String tips = skill.getTips() + GameDetailService.consumedHl(qq, uv);
        return SourceDataBase.getImgPathById(info.getId() + 100) +
                getIntro(info.getId(), info.getJid(), info.getSt(), getInfo(qq).getWh()) +
                "\r\n===================\r\n" + (((tips == null || tips.trim().isEmpty() || tips.equals(NULL_LOW_STR))) ? "" : tips);
    }

    @Override
    public String setName(long qq, Integer st, String str) {
        if (Tool.isIlleg(str)) return IS_ILLEGAL_TIPS_1;
        if (str.length() > 6) return STR_TOO_MUCH_LEN;
        Map<Integer, SkillInfo> infos = getSkillInfo(qq);
        if (infos.containsKey(st)) {
            SkillInfo info = infos.get(st);
            if (info.getMdTime() >= System.currentTimeMillis()) {
                return String.format(SKILL_INFO_WAIT_TIPS, getTimeTips(info.getMdTime()));
            } else {
                updateSkillInfo(info.setName(str).setMdTime(System.currentTimeMillis() + 1000 * 60 * 60 * 2));
            }
            if (info.getName() != null && !info.getName().isEmpty()) {
                info = infos.get(st);
                return "您的第" + Tool.trans(info.getSt()) + "魂技,名字是:" + info.getName();
            }
        } else {
            return DONT_HAVE_SKILL;
        }
        return ERR_TIPS;
    }

    @Override
    public String getIntro(long id, Integer st, String str) {
        Map<Integer, SkillInfo> infos = getSkillInfo(id);
        if (infos.containsKey(st)) {
            SkillInfo info = infos.get(st);
            info = infos.get(st);
            return "^您的第" + Tool.trans(info.getSt()) + "魂技:" +
                    ((info.getName() == null || info.getName().isEmpty()) ? "" : info.getName()) +
                    "\r\n===============\r\n介绍:" + getIntro(info.getId(), info.getJid(), info.getSt(), getInfo(id).getWh());
        } else {
            return "你没有这个魂技";
        }
    }

    @Override
    public String forget(long qq, Integer st) {
        if (!GameDataBase.containsInBg(113, qq)) return "您没有遗忘药水";
        removeFromBgs(qq, 113, ObjType.use);
        Map<Integer, SkillInfo> infos = getSkillInfo(qq);
        remove(infos.get(st));
        return "你忘掉了您的第" + Tool.cnArr[st - 1] + "魂技";
    }
}