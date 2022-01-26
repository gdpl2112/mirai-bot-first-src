package Project.services.impl;


import Entitys.Group;
import Entitys.gameEntitys.GhostObj;
import Project.services.DetailServices.GameJoinDetailService;
import Project.services.Iservice.IGameJoinAcService;
import io.github.kloping.MySpringTool.annotations.AutoStand;
import io.github.kloping.MySpringTool.annotations.Entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static Project.DataBases.GameDataBase.*;
import static Project.DataBases.skill.SkillDataBase.percentTo;
import static Project.DataBases.skill.SkillDataBase.toPercent;
import static Project.ResourceSet.FinalString.*;
import static Project.ResourceSet.FinalFormat.*;
import static Project.Tools.Tool.getTimeTips;
import static Project.services.DetailServices.GameJoinDetailService.getGhostObjFrom;
import static Project.services.DetailServices.GameJoinDetailService.saveGhostObjIn;
import static Project.Tools.GameTool.isATrue;
import static Project.drawers.Drawer.getImageFromStrings;

/**
 * @author github-kloping
 */
@Entity
public class GameJoinAcServiceImpl implements IGameJoinAcService {
    public static List<String> maps = new ArrayList<>();
    public static Map<String, String> dimMaps = new ConcurrentHashMap<>();
    public static List<String> decideMaps = new ArrayList<>();
    public static final Integer MAX_HELP_C = 5;
    public static final Integer MAX_HELP_TO_C = 3;

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
    static GameJoinDetailService service;

    @Override
    public String[] list() {
        return maps.toArray(new String[maps.size()]);
    }

    @Override
    public String join(long who, String name, Group group) {
        if (System.currentTimeMillis() < getK2(who)) {
            return String.format(ACTIVITY_WAIT_TIPS, getTimeTips(getK2(who)));
        }

        GhostObj ghostObj = getGhostObjFrom(who);
        if (ghostObj != null && ghostObj.getState() == GhostObj.HELPING) {
            if (isATrue(Long.valueOf(ghostObj.getForWhoStr()))) {
                return IN_SELECT;
            }
        }

        if (ghostObj != null) saveGhostObjIn(who, ghostObj);

        String what = name.trim();
        what = dimMaps.containsKey(what) ? dimMaps.get(what) : what;
        int id = maps.indexOf(what.trim());
        if (id < 0) return String.format(NOT_FOUND_SEE, what, LIST_STR);
        return service.run(id, who, group);
    }

    @Override
    public Object startAtt(long who, String select) {
        String what = select.trim();
        what = what.replace("选择", "").trim();
        int i = decideMaps.indexOf(what);
        if (i == -1) return NOT_FOUND_SELECT;
        if (!isATrue(who)) return NOT_IN_SELECT;
        GhostObj ghostObj = getGhostObjFrom(who);
        if (ghostObj != null) {
            if (ghostObj.getTime() > System.currentTimeMillis()) {
                return service.select(i, ghostObj, who);
            } else {
                saveGhostObjIn(who, null);
                return "已超过七分钟,超时无效!";
            }
        }
        return NOT_IN_SELECT;
    }

    @Override
    public String getHelp(long who) {
        GhostObj ghostObj = getGhostObjFrom(who);
        if (ghostObj != null) {
            if (ghostObj.getState() != GhostObj.HELPING) {
                if (!isATrue(who)) {
                    saveGhostObjIn(who, null);
                    return OVER_TIME_OR_DONT;
                }
            }
            if (ghostObj.getState() == GhostObj.HELPING) {
                if (isATrue(Long.valueOf(ghostObj.getForWhoStr()))) {
                    return YOU_HELPING;
                }
            } else {
                if (getInfo(who).getHelpC() >= MAX_HELP_C) {
                    return String.format(ONE_DAY_ONLY_HELP, MAX_HELP_C);
                } else {
                    switch (ghostObj.getState()) {
                        case GhostObj.NotNeed:
                            ghostObj.setState(GhostObj.NeedAndNo);
                            saveGhostObjIn(who, ghostObj);
                            putPerson(getInfo(who).addHelpC());
                            return REQUEST_HELP_SUCCEED;
                        case GhostObj.NeedAndNo:
                            return YOU_REQUEST_HELPING;
                        case GhostObj.NeedAndY:
                            return HELPED;
                        default:
                            return ERR_TIPS;
                    }
                }
            }
        } else {
            return NOT_IN_SELECT;
        }
        return ERR_TIPS;
    }

    @Override
    public String helpTo(long who, long whos) {
        if (who == whos) return CANT_HELP_YOURSELF;
        GhostObj ghostObj = getGhostObjFrom(who);
        GhostObj ghostObj1 = getGhostObjFrom(whos);
        if (ghostObj1 != null) {
            if (ghostObj != null && ghostObj.getTime() < System.currentTimeMillis()) {
                return IN_SELECT;
            } else {
                if (getInfo(who).getHelpToc() >= MAX_HELP_TO_C) {
                    return DAY_ONLY_HELP_TIPS;
                } else {
                    switch (ghostObj1.getState()) {
                        case GhostObj.NotNeed:
                            return NOT_NEED_HELP;
                        case GhostObj.NeedAndNo:
                            saveGhostObjIn(who, null);
                            ghostObj = new GhostObj(String.valueOf(whos));
                            ghostObj.setState(GhostObj.HELPING);
                            saveGhostObjIn(who, ghostObj);
                            ghostObj1.setState(GhostObj.NeedAndY);
                            ghostObj1.setWith(who);
                            saveGhostObjIn(whos, ghostObj1);
                            putPerson(getInfo(who).addHelpToC());
                            return HELP_SUCCEED;
                        case GhostObj.NeedAndY:
                            return HELPED;
                        default:
                            return ERR_TIPS;
                    }
                }
            }
        } else {
            return IT_NOT_IN_SELECT;
        }
    }

    @Override
    public String getIntro(long qq) {
        GhostObj ghostObj = getGhostObjFrom(qq);
        if (ghostObj == null || !isATrue(qq)) {
            return NOT_IN_SELECT;
        }
        if (ghostObj.getState() == GhostObj.HELPING) {
            ghostObj = getGhostObjFrom(Long.valueOf(ghostObj.getForWhoStr()));
        }
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
            if (getInfo(qq).getHj() < ev) {
                return HJ_NOT_ENOUGH;
            }
            putPerson(getInfo(qq).addHj(-ev));
            sb.append(String.format("探查成功,这消耗了你%s%%的精神力", bvc));
            sb.append(getImgById(ghostObj.getId()))
                    .append(getImageFromStrings(
                            "名字:" + ID_2_NAME_MAPS.get(ghostObj.getId()),
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
                            "名字:" + ID_2_NAME_MAPS.get(ghostObj.getId()),
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
