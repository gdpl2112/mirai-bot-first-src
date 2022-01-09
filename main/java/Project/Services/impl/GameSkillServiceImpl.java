package Project.Services.impl;

import Entitys.Group;
import Entitys.gameEntitys.PersonInfo;
import Entitys.gameEntitys.Skill;
import Entitys.gameEntitys.SkillInfo;
import Entitys.gameEntitys.SkillIntro;
import Project.DataBases.GameDataBase;
import Project.DataBases.skill.SkillDataBase;
import Project.Services.DetailServices.GameDetailService;
import Project.Services.Iservice.ISkillService;
import Project.Tools.GameTool;
import Project.Tools.Tool;
import Project.broadcast.game.SkillUseBroadcast;
import Project.broadcast.enums.ObjType;
import io.github.kloping.MySpringTool.annotations.Entity;

import java.util.Map;

import static Project.DataBases.GameDataBase.*;
import static Project.DataBases.skill.SkillDataBase.*;
import static Project.Services.DetailServices.GameSkillDetailService.*;

@Entity
public class GameSkillServiceImpl implements ISkillService {
    @Override
    public String InitSkill(long qq, Group group, Integer st) {
        Integer[] is = GameDataBase.getHhs(qq);
        if (is[0] == 0 || is.length < st) return ("你还没有获得对应的魂环");
        Map<Integer, SkillInfo> skinfo = getSkillInfo(qq);
        if (skinfo.containsKey(st)) {
            return ("已经激活了这个魂技");
        } else {
            if (st != 1)
                if (!skinfo.containsKey(st - 1))
                    return ("请先激活前一个魂技");
            if (st > 7)
                return ("更多魂技开发中...");
        }
        Integer id = is[st - 1];
        Integer id2;
        while (true) {
            id2 = Tool.rand.nextInt(20);
            boolean k = false;
            for (SkillInfo i : skinfo.values())
                if (i.getJid().intValue() == id2.intValue()) {
                    k = true;
                    break;
                }
            if (k) continue;
            else
                break;
        }

        if (st == 7) {
            id2 = Integer.valueOf(7 + "" + getInfo(qq).getWh());
        }

        SkillInfo info = new SkillInfo()
                .setId(id)
                .setJid(id2)
                .setQq(qq)
                .setTime(1L)
                .setTimeL(getCoolTime(id, id2, getInfo(qq).getWh(), st).longValue() * 60 * 1000L)
                .setSt(st)
                .setAddPercent((int) (getBasePercent(id2) * GameTool.getAHBl_(id)))
                .setUsePercent(getUserPercent(st, id2).intValue());

        SkillDataBase.saveSkillInfo(info);
        int id_ = is[st - 1] + 100;
        return getImgById(id_) + getIntro(id, id2, st, getInfo(qq).getWh());
    }

    private static String getIntro(Integer id, Integer jid, Integer st, int wh) {
        try {
            SkillIntro intro = new SkillIntro();
            intro.setId(id);
            intro.setJid(jid);
            intro.setSt(st);
            intro.setHasTime(getDuration(jid).longValue());
            intro.setTypes(getTypesFromJid(jid));
            intro.setWh(wh);
            return intro.getContent();
        } catch (Exception e) {
            e.printStackTrace();
            return "魂技介绍获取异常";
        }
    }

    @Override
    public String UseSkill(long qq, Integer st, Number[] allAt, String name, Group group) {
        Map<Integer, SkillInfo> infos = getSkillInfo(qq);
        if (!infos.containsKey(st)) return "你没有这个魂技";
        SkillInfo info = infos.get(st);
        /*
        if (info.getName() != null) {
            if (!(name.contains(info.getName()) || name.equals(info.getName()))) {
                return "您的第" + Tool.trans(info.getSt()) + "魂技,名字是:" + info.getName();
            }
        }*/
        if (info.getState() < 0) return "该魂技处于不可用状态";
        if (System.currentTimeMillis() < info.getTime()) return "魂技冷却中...\r\n" + Tool.getTimeHHMM(info.getTime());
        PersonInfo personInfo = getInfo(qq);
        long v = personInfo.getHll();
        long v1 = personInfo.getHl();
        Integer b = toPercent(v1, v);
        if (b < info.getUsePercent()) return "魂力不足:您当前的魂力值=>" + b + "%";
        Skill skill = null;
        try {
            skill = get(qq, info, allAt);
            execute(skill);
            SkillUseBroadcast.INSTANCE.broadcast(qq, info.getJid(), st, info);
            skill.setGroup(group);
        } catch (Exception e) {
            e.printStackTrace();
            return "魂技 选择器 异常";
        }
        long uv = percentTo(info.getUsePercent(), personInfo.getHll());
        info.setTime(System.currentTimeMillis() + info.getTimeL());
        saveSkillInfo(info);
        String tips = skill.getTips() + GameDetailService.consumedHl(qq, uv);
        return getImgById(info.getId() + 100) +
                getIntro(info.getId(), info.getJid(), info.getSt(), getInfo(qq).getWh()) +
                "\r\n===================\r\n" + (((tips == null || tips.trim().isEmpty() || tips.equals("null"))) ? "" : tips);
    }

    private static void execute(Skill skill) throws Exception {
        SkillDataBase.threads.execute(skill);
        Thread.currentThread().sleep(250);
    }

    @Override
    public String setName(long qq, Integer st, String str) {
        if (Tool.isIlleg(str)) return "名字包含敏感字节";
        if (str.length() > 6) return "名字过长";
        Map<Integer, SkillInfo> infos = getSkillInfo(qq);
        if (infos.containsKey(st)) {
            SkillInfo info = infos.get(st);
            if (info.getMd_time() >= System.currentTimeMillis()) {
                return "魂技修改信息冷却中...";
            } else saveSkillInfo(info.setName(str).setMd_time(System.currentTimeMillis() + 1000 * 60 * 60 * 2));
            if (info.getName() != null && !info.getName().isEmpty()) {
                info = infos.get(st);
                return "您的第" + Tool.trans(info.getSt()) + "魂技,名字是:" + info.getName();
            }
        } else {
            return "你没有这个魂技";
        }
        return "修改失败";
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