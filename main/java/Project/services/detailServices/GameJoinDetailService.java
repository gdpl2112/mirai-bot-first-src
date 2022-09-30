package Project.services.detailServices;


import Project.broadcast.game.GhostLostBroadcast;
import Project.broadcast.game.JoinBroadcast;
import Project.broadcast.game.SelectAttBroadcast;
import Project.controllers.gameControllers.ChallengeController;
import Project.dataBases.SourceDataBase;
import Project.dataBases.skill.SkillDataBase;
import Project.interfaces.Iservice.IGameService;
import Project.services.detailServices.ac.JoinAcService;
import Project.services.detailServices.ac.entity.*;
import Project.services.detailServices.roles.BeatenRoles;
import Project.services.detailServices.roles.Role;
import Project.services.detailServices.roles.RoleResponse;
import Project.services.detailServices.roles.RoleState;
import Project.services.player.PlayerBehavioralManager;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.github.kloping.MySpringTool.annotations.AutoStand;
import io.github.kloping.MySpringTool.annotations.Entity;
import io.github.kloping.mirai0.commons.GhostObj;
import io.github.kloping.mirai0.commons.Group;
import io.github.kloping.mirai0.commons.PersonInfo;
import io.github.kloping.mirai0.commons.broadcast.enums.ObjType;
import io.github.kloping.mirai0.unitls.Tools.Tool;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import static Project.dataBases.GameDataBase.*;
import static io.github.kloping.mirai0.commons.resouce_and_tool.CommonSource.*;
import static io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet.FinalFormat.HL_NOT_ENOUGH_TIPS0;
import static io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet.FinalNormalString.VERTIGO_ING;
import static io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet.FinalString.*;
import static io.github.kloping.mirai0.unitls.Tools.GameTool.*;
import static io.github.kloping.mirai0.unitls.Tools.JsonUtils.jsonStringToObject;
import static io.github.kloping.mirai0.unitls.drawers.Drawer.getImageFromStrings;

/**
 * @author github-kloping
 */
@Entity
public class GameJoinDetailService {
    public static final List<Integer> IDXS = new CopyOnWriteArrayList<>();
    public static final Map<Long, GhostObj> GHOST_TEMP = new ConcurrentHashMap<>();
    public static final int MAX_F = 3;
    @AutoStand
    IGameService gameService;
    @AutoStand
    JoinAcService join;
    @AutoStand
    ChallengeController challengeController;
    @AutoStand
    PlayerBehavioralManager manager;

    /**
     * 攻击一个魂兽
     *
     * @param who       攻击者 与 魂兽
     * @param att       值
     * @param show      返回攻击后的魂兽信息
     * @param canAttMe  魂兽是否可攻击我
     * @param type      攻击类型
     * @param mandatory 强制
     * @param bo        if false don't broadcast
     * @return
     */
    public static String attGho(long who, long att, boolean show, boolean canAttMe,
                                GhostLostBroadcast.KillType type, boolean mandatory, boolean bo) {
        GhostObj ghostObj = GameJoinDetailService.getGhostObjFrom(who);
        Long o0 = -ghostObj.getWhoMeet();
        synchronized (o0) {
            if (ghostObj == null) {
                return "\n没有遇到魂兽或 已过期,或已死亡";
            }
            try {
                String q2 = "";
                boolean isHelp = false;
                StringBuilder sb = new StringBuilder();
                sb.append(NEWLINE);
                if (ghostObj.getState() == GhostObj.HELPING) {
                    isHelp = true;
                    q2 = ghostObj.getForWhoStr();
                    ghostObj = GameJoinDetailService.getGhostObjFrom(Long.parseLong(q2));
                }
                if (!mandatory && IDXS.contains(ghostObj.getIDX())) {
                    return "\n该魂兽,正在被攻击中";
                }
                IDXS.add(ghostObj.getIDX());
                if (canAttMe) {
                    long at2 = Tool.tool.randLong(ghostObj.getAtt(), 0.333f, 0.48f);
                    at2 = percentTo(ghostObj.getTagValueOrDefault(SkillDataBase.TAG_STRENGTHEN_ATT, 100).intValue(), at2);
                    sb.append(getNameById(ghostObj.getId())).append("对你造成").append(at2).append("点伤害\n")
                            .append(GameDetailService.beaten(who, -2, at2));
                }
                long oNow = att;
                Map<String, Object> maps = new ConcurrentHashMap<>();
                for (Role r : BeatenRoles.RS) {
                    RoleResponse response = r.call(sb, -ghostObj.getWhoMeet(), who, att, oNow, ghostObj, maps);
                    if (response != null) {
                        oNow = response.getNowV();
                        if (!response.getArgs().isEmpty()) {
                            maps.putAll(response.getArgs());
                        }
                        if (response.getState() == RoleState.STOP) {
                            break;
                        }
                    }
                }
                if (oNow > 0) {
                    ghostObj.updateHp(-oNow, getInfo(who));
                    sb.append("你对").append(getNameById(ghostObj.getId())).append("造成").append(oNow).append("点伤害");
                }
                boolean showY = false;
                if (ghostObj.getHp() > 0) {
                    showY = true;
                    if (isHelp) GameJoinDetailService.saveGhostObjIn(Long.parseLong(q2), ghostObj);
                    else GameJoinDetailService.saveGhostObjIn(who, ghostObj);
                } else {
                    sb.append(NEWLINE).append(getNameById(ghostObj.getId())).append("被你打败了");
                    sb.append(willGet(ghostObj.getL(), who, ghostObj.getId()));
                    sb.append(willGetXp(ghostObj, who, isHelp));
                    ZongDetailService.onKilled(who, ghostObj.getXp());
                    if (isHelp) {
                        GameJoinDetailService.saveGhostObjIn(Long.parseLong(q2), null);
                        GameJoinDetailService.saveGhostObjIn(who, null);
                    } else {
                        GameJoinDetailService.saveGhostObjIn(who, null);
                    }
                    //广播
                    if (!isHelp) {
                        GhostLostBroadcast.INSTANCE.broadcast(who, ghostObj, type);
                    } else {
                        GhostLostBroadcast.INSTANCE.broadcast(Long.parseLong(q2), ghostObj, type);
                    }
                }
                if (showY && show) {
                    sb.append(NEWLINE).append(willTips(who, ghostObj, false));
                }
                return sb.toString();
            } finally {
                IDXS.remove((Object) ghostObj.getIDX());
                if (bo) SelectAttBroadcast.INSTANCE.broadcast(who, -2, att, 2);
            }
        }
    }

    public static String attGho(long who, long att, boolean show, boolean canAttMe, GhostLostBroadcast.KillType type, boolean mandatory) {
        return attGho(who, att, show, canAttMe, type, mandatory, true);
    }

    public static String attGho(long who, long att, boolean show, boolean canAttMe, GhostLostBroadcast.KillType type) {
        return attGho(who, att, show, canAttMe, type, false);
    }

    public static String willTips(Number qq, GhostObj ghostObj, boolean k) {
        int id = ghostObj.getId();
        long v1 = getInfo(qq).getHj();
        long v2 = ghostObj.getHj();
        int bv = toPercent(v1, v2);
        if (bv >= 90) {
            return "!!!\n你遇到了魂兽\n做出你的选择(选择 攻击/逃跑)\n" + SourceDataBase.getImgPathById(id) +
                    getImageFromStrings(
                            "名字:" + ID_2_NAME_MAPS.get(ghostObj.getId()),
                            "等级:" + ghostObj.getL(),
                            "攻击:" + ghostObj.getAtt(),
                            "生命:" + ghostObj.getHp(),
                            "经验:" + ghostObj.getXp(),
                            "精神力:" + ghostObj.getHj()
                    );
        } else {
            return "!!!\n你遇到了魂兽且无法探查真正实力\n做出你的选择(选择 攻击/逃跑)\n" + SourceDataBase.getImgPathById(id) +
                    getImageFromStrings(
                            "名字:" + ID_2_NAME_MAPS.get(ghostObj.getId()),
                            "等级:" + getLevelTips(ghostObj.getL()),
                            "攻击:??",
                            "生命:??",
                            "经验:??",
                            "精神力:??");
        }
    }

    public static String getLevelTips(long v) {
        if (v < 100) return "低级魂兽";
        if (v < 1000) return "百年级别";
        if (v < 10000) return "千年级别";
        if (v < 100000) return "万年级别";
        if (v < 1000000) return "十万年级别";
        if (v < 5000000) return "百万年级别";
        if (v < 10000000) return "五百万年以上级别";
        return "未知级别";
    }

    public synchronized static GhostObj getGhostObjFrom(long qq) {
        if (GHOST_TEMP.containsKey(qq)) {
            return GHOST_TEMP.get(qq);
        } else {
            return GHOST_TEMP.get(qq);
        }
    }

    public static <T extends GhostObj> T getGhost(String jsonStr) {
        JSONObject jo = JSON.parseObject(jsonStr);
        int id = jo.getInteger("id");
        if (id > 700) {
            switch (id) {
                case 701:
                    return (T) jsonStringToObject(jsonStr, Ghost701.class);
                case 702:
                    return (T) jsonStringToObject(jsonStr, Ghost702.class);
                case 703:
                    return (T) jsonStringToObject(jsonStr, Ghost703.class);
                case 704:
                    return (T) jsonStringToObject(jsonStr, Ghost704.class);
                case 705:
                    return (T) jsonStringToObject(jsonStr, Ghost705.class);
                default:
                    return null;
            }
        } else {
            return (T) jsonStringToObject(jsonStr, GhostObj.class);
        }
    }

    public synchronized static GhostObj saveGhostObjIn(long qq, GhostObj ghostObj) {
        if (ghostObj == null) {
            try {
                if (GHOST_TEMP.containsKey(qq) && GHOST_TEMP.get(qq) != null) {
                    GHOST_TEMP.get(qq).dispose();
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
            GHOST_TEMP.remove(qq);
        } else {
            GHOST_TEMP.put(qq, ghostObj);
        }
        return getGhostObjFrom(qq);
    }

    public static String willGet(int level, long who, int id) {
        onKilled(who);
        if (RANDOM.nextInt(20) == 0) {
            int sid = 120;
            addToBgs(who, sid, ObjType.got);
            return "\n你获得了" + getNameById(sid);
        } else if (id > 700) {
            return willGetLr(level, who);
        } else if (id > 600) {
            return willGetBone(level, who);
        } else {
            return willGetHh(level, who);
        }
    }

    /**
     * 几率获取魂环
     *
     * @param level
     * @return
     */
    public static boolean randHh(int level) {
        if (level > 10000000) {
            return Tool.tool.RANDOM.nextInt(100) < 28;
        } else if (level > 1000000) {
            return Tool.tool.RANDOM.nextInt(100) < 38;
        } else if (level > 100000) {
            return Tool.tool.RANDOM.nextInt(100) < 54;
        }
        return Tool.tool.RANDOM.nextInt(100) < 75;
    }

    public static boolean randHh1(int level) {
        if (level > 10000000) {
            return Tool.tool.RANDOM.nextInt(100) < 38;
        } else if (level > 1000000) {
            return Tool.tool.RANDOM.nextInt(100) < 48;
        } else if (level > 100000) {
            return Tool.tool.RANDOM.nextInt(100) < 64;
        }
        return Tool.tool.RANDOM.nextInt(100) < 75;
    }

    public static String willGetXp(GhostObj ghostObj, long who, boolean isHelp) {
        long v = ghostObj.getXp();
        long mxv = getInfo(who).getXpL();
        v = v > mxv / MAX_F ? mxv / MAX_F : v;
        if (isHelp) {
            v /= 2;
            putPerson(getInfo(who).addXp(v));
            return "\n由于在支援别人,所以获得经验减半 获得了" + v + "点经验";
        } else {
            putPerson(getInfo(who).addXp(v));
            return "\n获得了 " + ghostObj.getXp() + "点经验";
        }
    }

    private static String willGetBone(int level, long who) {
        if (randHh1(level)) {
            Integer id = 0;
            int r1 = Tool.tool.RANDOM.nextInt(5) + 1;
            if (level > 5000) {
                if (level < 20000) {
                    id = Integer.valueOf("15" + r1 + "1");
                } else if (level < 350000) {
                    id = Integer.valueOf("15" + r1 + "2");
                } else {
                    id = Integer.valueOf("15" + r1 + "3");
                }
                addToBgs(who, id, ObjType.got);
                return "\n你获得了 " + getNameById(id) + SourceDataBase.getImgPathById(id);
            }
        }
        return "";
    }

    private static String willGetHh(int level, long who) {
        int sid = getHhByGh(level);
        if (randHh(level)) {
            addToBgs(who, sid, ObjType.got);
            return "\n你获得了" + getNameById(sid);
        }
        return "";
    }

    private static String willGetLr(int level, long who) {
        int r = Tool.tool.RANDOM.nextInt(100);
        if (r > 75) {

        } else if (r > 50) {
            return willGetHh(level, who);
        } else if (r > 25) {
            return willGetBone(level, who);
        } else {
            r = Tool.tool.RANDOM.nextInt(300);
            int n = Tool.tool.RANDOM.nextInt(4) + 1;
            if (r == 1) {
                if (level >= 3000) {
                    addToBgs(who, 1601, ObjType.got);
                    return "\n你获得了一个" + getNameById(1601) + SourceDataBase.getImgPathById(1601);
                }
            } else if (r < 30) {
                addToAqBgs(who, 1005, n);
                return String.format("\n你获得了一个%s耐久的孔雀翎", n) + SourceDataBase.getImgPathById(1005);
            } else if (r < 75) {
                addToAqBgs(who, 1004, n);
                return String.format("\n你获得了一个%s耐久的子母追魂夺命胆", n) + SourceDataBase.getImgPathById(1004);
            } else if (r < 130) {
                addToAqBgs(who, 1003, n);
                return String.format("\n你获得了一个%s耐久的含沙射影", n) + SourceDataBase.getImgPathById(1003);
            } else if (r < 160) {
                addToAqBgs(who, 1002, n);
                return String.format("\n你获得了一个%s耐久的龙须针", n) + SourceDataBase.getImgPathById(1002);
            }
        }
        return "";
    }

    public String run(int id, long who, Group group) {
        try {
            //广播
            JoinBroadcast.INSTANCE.broadcast(who, id);
            return join.join(id, who, group);
        } finally {
            int r = Tool.tool.RANDOM.nextInt(8) + 10;
            putPerson(getInfo(who).setK2(System.currentTimeMillis() + r * 60 * 1000));
        }
    }

    /**
     * @param who
     * @param idType 5 星斗 6 极北
     * @return
     */
    public <T extends GhostObj> T summonFor(String who, int idMin, int idMax) {
        int id = (int) Tool.tool.randA(idMin, idMax);
        return summonFor(who, id);
    }

    public <T extends GhostObj> T summonFor(String who, int id) {
        PersonInfo personInfo = getInfo(who);
        float bl = getAllHHBL(Long.valueOf(who));
        GhostObj ghostObj = GhostObj.create(
                (long) (personInfo.att() * bl),
                personInfo.getHpL(),
                (long) (personInfo.getXpL() / getRandXl(personInfo.getLevel()) / 3),
                id,
                -1,
                true, bl);
        return (T) ghostObj;
    }

    public Object select(int id, GhostObj ghostObj, long who) {
        PersonInfo personInfo = getInfo(who);
        if (personInfo.isVertigo()) return VERTIGO_ING;
        Object s0 = null;
        switch (id) {
            case 0:
                s0 = att(who, ghostObj);
                break;
            case 1:
                s0 = taoPao(ghostObj, who);
                break;
            default:
                break;
        }
        return s0;
    }

    private String taoPao(GhostObj ghostObj, long who) {
        if (ghostObj.getState() == GhostObj.HELPING) {
            String whos = ghostObj.getForWhoStr();
            GameJoinDetailService.saveGhostObjIn(who, null);
            GhostObj ghostObj1 = null;
            ghostObj1 = GameJoinDetailService.getGhostObjFrom(who);
            ghostObj1.setState(GhostObj.NOT_NEED);
            GameJoinDetailService.saveGhostObjIn(Long.parseLong(whos), ghostObj1);
        } else {
            PersonInfo personInfo = getInfo(who);
            if (Tool.tool.RANDOM.nextInt(10) < 7 && ghostObj.getHp() > ghostObj.getMaxHp() / 2 && ghostObj.getAtt()
                    >= personInfo.att() && personInfo.getHp() <= ghostObj.getHp()) {
                return ghostObj.getName() + "觉得 还有再战之力 ，ta跳到了你面前\n逃跑失败";
            }
            ghostObj.dispose();
            GameJoinDetailService.saveGhostObjIn(who, null);
        }
        return "逃跑完成";
    }

    private String att(long who, GhostObj ghostObj) {
        if (getInfo(who).getJak1() > System.currentTimeMillis()) return ATT_WAIT_TIPS;
        try {
            getInfo(who).setJak1(System.currentTimeMillis() + manager.getAttPost(who) / 2).apply();
            boolean isHelp = ghostObj.getState() == GhostObj.HELPING;
            String whos = "";
            if (isHelp) {
                whos = ghostObj.getForWhoStr();
                ghostObj = GameJoinDetailService.getGhostObjFrom(Long.parseLong(whos));
            }
            if (IDXS.contains(ghostObj.getIDX())) return SYNC_GHOST_TIPS;
            IDXS.add(ghostObj.getIDX());
            PersonInfo personInfo = getInfo(who);
            long hl1 = Tool.tool.randLong(personInfo.getHll(), 0.125f, 0.16f);
            long at1 = Tool.tool.randLong(personInfo.att(), 0.35f, 0.48f);
            long at2 = Tool.tool.randLong(ghostObj.getAtt(), 0.25f, 0.48f);
            at2 = percentTo(ghostObj.getTagValueOrDefault(SkillDataBase.TAG_STRENGTHEN_ATT, 100).intValue(), at2);
            StringBuilder sb = new StringBuilder();
            sb.append(NEWLINE);
            if (personInfo.getHl() > hl1) {
                sb.append("消耗了").append(hl1).append("点魂力\n").append(GameDetailService.consumedHl(who, hl1).trim());
                long oNow = at1;
                Map<String, Object> maps = new ConcurrentHashMap<>();
                for (Role r : BeatenRoles.RS) {
                    RoleResponse response = r.call(sb, -2, who, at1, oNow, ghostObj, maps);
                    if (response != null) {
                        oNow = response.getNowV();
                        if (!response.getArgs().isEmpty()) maps.putAll(response.getArgs());
                        if (response.getState() == RoleState.STOP) break;
                    }
                }
                if (oNow > 0) {
                    ghostObj.updateHp(-oNow, getInfo(who));
                    sb.append("你对").append(getNameById(ghostObj.getId())).append("造成").append(oNow).append("点伤害");
                    sb.append(GameDetailService.onAtt(who, -2, oNow));
                }
            } else {
                sb.append(HL_NOT_ENOUGH_TIPS0);
            }
            sb.append(NEWLINE);
            sb.append(getNameById(ghostObj.getId())).append("对你造成").append(at2).append("点伤害")
                    .append(GameDetailService.beaten(who, -2, at2));
            boolean showI = true;
            boolean showY = false;
            if (isAlive(who)) {
                if (ghostObj.getHp() > 0) {
                    showY = true;
                    showI = true;
                    if (isHelp) GameJoinDetailService.saveGhostObjIn(Long.parseLong(whos), ghostObj);
                    else GameJoinDetailService.saveGhostObjIn(who, ghostObj);
                } else {
                    showI = true;
                    sb.append(NEWLINE).append(getNameById(ghostObj.getId())).append("被你打败了");
                    sb.append(willGet(ghostObj.getL(), who, ghostObj.getId()));
                    sb.append(willGetXp(ghostObj, who, isHelp));
                    ZongDetailService.onKilled(who, ghostObj.getXp());
                    if (isHelp) {
                        GameJoinDetailService.saveGhostObjIn(Long.parseLong(whos), null);
                        GameJoinDetailService.saveGhostObjIn(who, null);
                    } else {
                        GameJoinDetailService.saveGhostObjIn(who, null);
                    }
                }
                //广播
                if (!isHelp)
                    GhostLostBroadcast.INSTANCE.broadcast(who, ghostObj, GhostLostBroadcast.KillType.NORMAL_ATT);
                else
                    GhostLostBroadcast.INSTANCE.broadcast(Long.parseLong(whos), ghostObj, GhostLostBroadcast.KillType.NORMAL_ATT);
            } else {
                sb.append(NEWLINE);
                sb.append("你被打败了!!!");
                if (ghostObj.getHp() < 0) {
                    sb.append(NEWLINE);
                    sb.append(getNameById(ghostObj.getId())).append("也失败了,但你无法获得魂环");
                }
                showY = true;
                showI = true;
                if (isHelp) {
                    ghostObj.setState(GhostObj.NOT_NEED);
                    GameJoinDetailService.saveGhostObjIn(Long.parseLong(whos), ghostObj);
                }
                GameJoinDetailService.saveGhostObjIn(who, null);
            }
            if (showI) sb.append(NEWLINE).append(gameService.info(who));
            if (showY) sb.append(NEWLINE).append(willTips(who, ghostObj, false));
            return sb.toString();
        } finally {
            IDXS.remove((Object) ghostObj.getIDX());
        }
    }
}
