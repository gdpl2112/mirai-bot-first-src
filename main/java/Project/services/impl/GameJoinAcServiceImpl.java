package Project.services.impl;


import Project.controllers.gameControllers.ChallengeController;
import Project.dataBases.SourceDataBase;
import Project.dataBases.skill.SkillDataBase;
import Project.interfaces.Iservice.IGameJoinAcService;
import Project.services.autoBehaviors.GhostBehavior;
import Project.services.detailServices.GameJoinDetailService;
import io.github.kloping.MySpringTool.annotations.AutoStand;
import io.github.kloping.MySpringTool.annotations.Entity;
import io.github.kloping.mirai0.commons.GInfo;
import io.github.kloping.mirai0.commons.GhostObj;
import io.github.kloping.mirai0.commons.Group;
import io.github.kloping.mirai0.unitls.Tools.GameTool;
import io.github.kloping.mirai0.unitls.Tools.Tool;
import io.github.kloping.mirai0.unitls.drawers.Drawer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static Project.controllers.auto.ControllerSource.challengeDetailService;
import static Project.dataBases.GameDataBase.*;
import static Project.dataBases.skill.SkillDataBase.NEGATIVE_TAGS;
import static Project.services.detailServices.GameJoinDetailService.getGhostObjFrom;
import static Project.services.detailServices.GameJoinDetailService.saveGhostObjIn;
import static io.github.kloping.mirai0.commons.resouce_and_tool.CommonSource.percentTo;
import static io.github.kloping.mirai0.commons.resouce_and_tool.CommonSource.toPercent;
import static io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet.FinalFormat.*;
import static io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet.FinalString.*;
import static io.github.kloping.mirai0.unitls.Tools.GameTool.isATrue;
import static io.github.kloping.mirai0.unitls.drawers.Drawer.getImageFromStrings;

/**
 * @author github-kloping
 */
@Entity
public class GameJoinAcServiceImpl implements IGameJoinAcService {
    public static final Integer MAX_HELP_C = 5;
    public static final Integer MAX_HELP_TO_C = 3;
    public static List<String> maps = new ArrayList<>();
    public static Map<String, String> dimMaps = new ConcurrentHashMap<>();
    public static List<String> decideMaps = new ArrayList<>();
    @AutoStand
    static GameJoinDetailService service;

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
    ChallengeController controller;

    @Override
    public String[] list() {
        return maps.toArray(new String[maps.size()]);
    }

    @Override
    public String join(long who, String name, Group group) {
        if (System.currentTimeMillis() < getK2(who)) {
            return String.format(ACTIVITY_WAIT_TIPS, Tool.tool.getTimeTips(getK2(who)));
        }
        GhostObj ghostObj = getGhostObjFrom(who);
        if (ghostObj != null) {
            if (ghostObj.getTime() <= System.currentTimeMillis()) {
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

    public static final GhostBehavior DEFAULT_0 = new GhostBehavior(-1L, null) {
        @Override
        public void thisOver() {

        }
    };

    @Override
    public Object startSelect(long who, String select) {
        String what = select.trim();
        what = what.replace("选择", "").trim();
        int i = decideMaps.indexOf(what);
        if (i == -1) return NOT_FOUND_SELECT;
        GhostObj ghostObj = getGhostObjFrom(who);
        if (ghostObj != null) {
            if (ghostObj.getTime() > System.currentTimeMillis()) {
                return service.select(i, ghostObj, who);
            } else {
                saveGhostObjIn(who, null);
                return "已超过七分钟,超时无效!";
            }
        } else if (challengeDetailService.isTemping(who)) {
            return controller.o3(who);
        }
        GhostBehavior.MAP.getOrDefault(who, DEFAULT_0).thisOver();
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
                        case GhostObj.NOT_NEED:
                            ghostObj.setState(GhostObj.NEED_AND_NO);
                            saveGhostObjIn(who, ghostObj);
                            putPerson(getInfo(who).addHelpC());
                            return REQUEST_HELP_SUCCEED;
                        case GhostObj.NEED_AND_NO:
                            return YOU_REQUEST_HELPING;
                        case GhostObj.NEED_AND_YES:
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
    public String helpTo(long q1, long q2) {
        if (q1 == q2) return CANT_HELP_YOURSELF;
        GhostObj ghostObj = getGhostObjFrom(q1);
        GhostObj ghostObj1 = getGhostObjFrom(q2);
        if (ghostObj1 != null) {
            if (ghostObj != null && ghostObj.getTime() < System.currentTimeMillis()) {
                return IN_SELECT;
            } else {
                if (getInfo(q1).getHelpToc() >= MAX_HELP_TO_C) {
                    return DAY_ONLY_HELP_TIPS;
                } else {
                    switch (ghostObj1.getState()) {
                        case GhostObj.NOT_NEED:
                            return NOT_NEED_HELP;
                        case GhostObj.NEED_AND_NO:
                            saveGhostObjIn(q1, null);
                            ghostObj = GhostObj.createHelp(String.valueOf(q2));
                            ghostObj.setState(GhostObj.HELPING);
                            saveGhostObjIn(q1, ghostObj);
                            ghostObj1.getWiths().add(q1);
                            int max = GameTool.getMaxHelpNumByGhostIdAndLevel(ghostObj1.getId(), ghostObj1.getLevel());
                            int in = ghostObj1.getWiths().size();
                            if (in >= max)
                                ghostObj1.setState(GhostObj.NEED_AND_YES);
                            saveGhostObjIn(q2, ghostObj1);
                            putPerson(getInfo(q1).addHelpToC());
                            GInfo.getInstance(q1).addHelpc().apply();
                            GInfo.getInstance(q2).addReqc().apply();
                            return HELP_SUCCEED;
                        case GhostObj.NEED_AND_YES:
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
        int bvc = 100 - bv;
        bvc = bvc > maxLose ? maxLose : bvc < 0 ? 0 : bvc;
        long ev = percentTo(bvc, getInfo(qq).getHjL());
        if (getInfo(qq).getHj() < ev) {
            return HJ_NOT_ENOUGH;
        }
        putPerson(getInfo(qq).addHj(-ev));
        sb.append(String.format("探查成功,这消耗了你%s%%的精神力", bvc));
        sb.append(SourceDataBase.getImgPathById(ghostObj.getId()));
        if (ghostObj.getHjL() > 1000) {
            sb.append(Tool.tool.pathToImg(Drawer.drawGhostInfo(ghostObj)));
        }else{
            sb.append(SourceDataBase.getImgPathById(ghostObj.getId()))
                    .append(getImageFromStrings(
                            "名字:" + ID_2_NAME_MAPS.get(ghostObj.getId()),
                            "等级:" + ghostObj.getL(),
                            "攻击:" + ghostObj.getAtt(),
                            "生命:" + ghostObj.getHp(),
                            "经验:" + ghostObj.getXp(),
                            "精神力:" + ghostObj.getHj()
                    ));
        }

        StringBuilder sb1 = new StringBuilder();
        GhostObj finalGhostObj = ghostObj;
        SkillDataBase.TAG2NAME.forEach((k, v) -> {
            Number v0 = finalGhostObj.getTagValue(k);
            if (v0 != null && v0.longValue() > 0) {
                String s0 = v + v0.toString() + ",";
                sb1.append(NEGATIVE_TAGS.contains(k) ? "负:" : "增:");
                sb1.append(s0);
                sb1.append(NEWLINE);
            }
        });
        if (sb1.length() > 0) {
            sb.append(getImageFromStrings(sb1.toString().trim().split(NEWLINE)));
        }
        return sb.toString();
    }
}
