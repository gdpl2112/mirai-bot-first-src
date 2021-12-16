package Project.Services.impl;


import Entitys.Group;
import Entitys.gameEntitys.GhostObj;
import Project.Services.DetailServices.GameJoinDetailService;
import Project.Services.Iservice.IGameJoinAcService;
import io.github.kloping.MySpringTool.annotations.AutoStand;
import io.github.kloping.MySpringTool.annotations.Entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static Project.DataBases.GameDataBase.*;
import static Project.DataBases.skill.SkillDataBase.percentTo;
import static Project.DataBases.skill.SkillDataBase.toPercent;
import static Project.Services.DetailServices.GameJoinDetailService.getGhostObjFrom;
import static Project.Services.DetailServices.GameJoinDetailService.saveGhostObjIn;
import static Project.Tools.GameTool.isATrue;
import static Project.Tools.Tool.getTimeHHMM;
import static Project.Tools.Tool.rand;
import static Project.drawers.Drawer.getImageFromStrings;

@Entity
public class GameJoinAcServiceImpl implements IGameJoinAcService {
    public static List<String> maps = new ArrayList<>();
    public static Map<String, String> dimMaps = new ConcurrentHashMap<>();
    public static List<String> decideMaps = new ArrayList<>();
    public static final Integer MaxHelpC = 5;
    public static final Integer MaxHelpToC = 3;

    static {
        maps.add("星斗森林");
        maps.add("极北之地");
        maps.add("落日森林");

        dimMaps.put("星斗大森林", "星斗森林");
        dimMaps.put("星斗", "星斗森林");
        dimMaps.put("极北", "极北之地");
        dimMaps.put("落日", "落日森林");

        decideMaps.add("攻击");
        decideMaps.add("逃跑");
    }


    @AutoStand
    GameJoinDetailService service;

    @Override
    public String[] list() {
        return maps.toArray(new String[maps.size()]);
    }

    @Override
    public String join(long who, String name, Group group) {
        if (System.currentTimeMillis() < getK2(who))
            return "活动时间未到(冷却中..)=>" + getTimeHHMM(getK2(who));

        GhostObj ghostObj = getGhostObjFrom(who);
        if (ghostObj != null && ghostObj.getState() == GhostObj.HELPING)
            if (isATrue(Long.valueOf(ghostObj.getForWhoStr())))
                return "你正在选择状态中...";

        if (ghostObj != null) saveGhostObjIn(who, ghostObj);

        String what = name.trim();
        what = dimMaps.containsKey(what) ? dimMaps.get(what) : what;
        int id = maps.indexOf(what.trim());
        if (id < 0) return "没有找到 " + what + "见 列表";
        return service.run(id, who, group);
    }

    @Override
    public Object startAtt(long who, String select) {
        String what = select.trim();
        what = what.replace("选择", "").trim();
        int i = decideMaps.indexOf(what);
        if (i == -1) return "没有此选项";
        if (!isATrue(who)) return "没有选择状态";
        GhostObj ghostObj = getGhostObjFrom(who);
        if (ghostObj != null) {
            if (ghostObj.getTime() > System.currentTimeMillis()) {
                return service.Select(i, ghostObj, who);
            } else {
                saveGhostObjIn(who, null);
                return "已超过七分钟,超时无效!";
            }
        }
        return "没有处于选择状态....";
    }

    @Override
    public String getHelp(long who) {
        GhostObj ghostObj =
                getGhostObjFrom(who);
        if (ghostObj != null) {
            if (ghostObj.getState() != GhostObj.HELPING) {
                if (!isATrue(who)) {
                    saveGhostObjIn(who, null);
                    return "对战已超时或无效";
                }
            }
            if (ghostObj.getState() == GhostObj.HELPING) {
                if (isATrue(Long.valueOf(ghostObj.getForWhoStr()))) {
                    return "你正在帮帮忙中...";
                }
            } else {
                if (getInfo(who).getHelpC() >= MaxHelpC) {
                    return "一天 仅能 请求支援 5次";
                } else {
                    switch (ghostObj.getState()) {
                        case GhostObj.NotNeed:
                            ghostObj.setState(GhostObj.NeedAndNo);
                            saveGhostObjIn(who, ghostObj);
                            putPerson(getInfo(who).addHelpC());
                            return "请求支援成功(其他玩家使用=>支援@ta>来支援ta)";
                        case GhostObj.NeedAndNo:
                            return "正在请求支援中....";
                        case GhostObj.NeedAndY:
                            return "已经被支援";
                    }
                }
            }
        } else {
            return "你没有在选择状态";
        }
        return "错误";
    }

    @Override
    public String HelpTo(long who, long whos) {
        if (who == whos) return "不能帮助自己";
        GhostObj ghostObj = getGhostObjFrom(who);
        GhostObj ghostObj1 = getGhostObjFrom(whos);
        if (ghostObj1 != null) {
            if (ghostObj != null && ghostObj.getTime() < System.currentTimeMillis()) {
                return "你正在处于选择状态....";
            } else {
                if (getInfo(who).getHelpToc() >= MaxHelpToC) {
                    return "一天 仅可 支援 3次";
                } else {
                    switch (ghostObj1.getState()) {
                        case GhostObj.NotNeed:
                            return "ta不需要支援";
                        case GhostObj.NeedAndNo:
                            saveGhostObjIn(who, null);
                            ghostObj = new GhostObj(String.valueOf(whos));
                            ghostObj.setState(GhostObj.HELPING);
                            saveGhostObjIn(who, ghostObj);
                            ghostObj1.setState(GhostObj.NeedAndY);
                            ghostObj1.setWith(who);
                            saveGhostObjIn(whos, ghostObj1);
                            putPerson(getInfo(who).addHelpToC());
                            return "支援成功";
                        case GhostObj.NeedAndY:
                            return "ta已经被支援";
                    }
                }
            }
        } else {
            return "ta没有在选择状态(为遇到魂兽)";
        }
        return "???";
    }

    public static Integer createRand() {
        Integer r = new Integer(rand.nextInt());
        return r;
    }

    @Override
    public String getIntro(long qq) {
        GhostObj ghostObj = getGhostObjFrom(qq);

        if (ghostObj == null || !isATrue(qq))
            return "您没有遇到魂兽,或已过期";

        if (ghostObj.getState() == GhostObj.HELPING) {
            ghostObj = getGhostObjFrom(Long.valueOf(ghostObj.getForWhoStr()));
        }

        if (ghostObj == null || !isATrue(qq))
            return "您没有遇到魂兽,或已过期";

        int id = ghostObj.getId();
        long v1 = getInfo(qq).getHj();
        long v2 = ghostObj.getHj();
        int bv = toPercent(v1, v2);
        StringBuilder sb = new StringBuilder();
        int maxLose = 16;
        if (bv < 100) {
            int bvc = 100 - bv;
            bvc = bvc > maxLose ? maxLose : bvc < 1 ? 1 : bvc;
            long ev = percentTo(bvc, getInfo(qq).getHjL());
            if (getInfo(qq).getHj() < ev)
                return "精神力不足!";
            putPerson(getInfo(qq).addHj(-ev));
            sb.append(String.format("探查成功,这消耗了你%s%%的精神力", bvc));
            sb.append(getImgById(ghostObj.getId()))
                    .append(getImageFromStrings(
                            "名字:" + id2NameMaps.get(ghostObj.getId()),
                            "等级:" + ghostObj.getL(),
                            "攻击:" + ghostObj.getAtt(),
                            "生命:" + ghostObj.getHp(),
                            "经验:" + ghostObj.getXp(),
                            "精神力:" + ghostObj.getHj()
                    ));
        } else {
            sb.append("探查成功,这消耗了你0%的精神力");
            sb.append(getImgById(ghostObj.getId()))
                    .append(getImageFromStrings(
                            "名字:" + id2NameMaps.get(ghostObj.getId()),
                            "等级:" + ghostObj.getL(),
                            "攻击:" + ghostObj.getAtt(),
                            "生命:" + ghostObj.getHp(),
                            "经验:" + ghostObj.getXp(),
                            "精神力:" + ghostObj.getHj()
                    ));
        }
        return sb.toString();
    }
}
