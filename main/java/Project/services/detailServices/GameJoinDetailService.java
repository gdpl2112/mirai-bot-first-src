package Project.services.detailServices;


import Project.broadcast.enums.ObjType;
import Project.broadcast.game.GhostLostBroadcast;
import Project.broadcast.game.JoinBroadcast;
import Project.services.detailServices.ac.JoinAcService;
import Project.services.detailServices.ac.entity.*;
import Project.interfaces.Iservice.IGameService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.github.kloping.MySpringTool.annotations.AutoStand;
import io.github.kloping.MySpringTool.annotations.Entity;
import io.github.kloping.mirai0.Entitys.Group;
import io.github.kloping.mirai0.Entitys.gameEntitys.GhostObj;
import io.github.kloping.mirai0.Entitys.gameEntitys.PersonInfo;
import io.github.kloping.mirai0.unitls.Tools.Tool;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import static Project.dataBases.GameDataBase.*;
import static Project.dataBases.skill.SkillDataBase.toPercent;
import static Project.ResourceSet.FinalString.SPLIT_LINE_0;
import static io.github.kloping.mirai0.unitls.Tools.GameTool.*;
import static io.github.kloping.mirai0.unitls.Tools.JsonUtils.jsonStringToObject;
import static io.github.kloping.mirai0.unitls.Tools.JsonUtils.objectToJsonString;
import static io.github.kloping.mirai0.unitls.Tools.Tool.rand;
import static io.github.kloping.mirai0.unitls.Tools.Tool.randLong;
import static io.github.kloping.mirai0.unitls.drawers.Drawer.getImageFromStrings;

/**
 * @author github-kloping
 */
@Entity
public class GameJoinDetailService {

    @AutoStand
    IGameService gameService;

    @AutoStand
    JoinAcService join;

    public String run(int id, long who, Group group) {
        try {
            //广播
            JoinBroadcast.INSTANCE.broadcast(who, id);
            return join.join(id, who, group);
        } finally {
            int r = Tool.rand.nextInt(8) + 10;
            putPerson(getInfo(who).setK2(System.currentTimeMillis() + r * 60 * 1000));
        }
    }

    /**
     * @param who
     * @param idType 5 星斗 6 极北
     * @return
     */
    public <T extends GhostObj> T summonFor(String who, int idMin, int idMax) {
        PersonInfo personInfo = getInfo(who);
        float bl = getAllHHBL(Long.valueOf(who));
        GhostObj ghostObj = GhostObj.create(
                (long) (personInfo.getAtt() * bl),
                personInfo.getHpL(),
                (long) (personInfo.getXpL() / getRandXl(personInfo.getLevel())),
                idMin, idMax,
                -1,
                true, bl);
        return (T) ghostObj;
    }

    public Object select(int id, GhostObj ghostObj, long who) {
        PersonInfo personInfo = getInfo(who);
        switch (id) {
            case 0:
                String m1 = att(who, ghostObj);
                return m1;
            case 1:
                return taoPao(ghostObj, who);
            default:
                break;
        }
        return "未知选择";
    }

    private String taoPao(GhostObj ghostObj, long who) {
        if (ghostObj.getState() == GhostObj.HELPING) {
            String whos = ghostObj.getForWhoStr();
            GameJoinDetailService.saveGhostObjIn(who, null);
            GhostObj ghostObj1 = null;
            ghostObj1 = GameJoinDetailService.getGhostObjFrom(who);
            ghostObj1.setState(GhostObj.NotNeed);
            GameJoinDetailService.saveGhostObjIn(Long.parseLong(whos), ghostObj1);
        } else {
            PersonInfo personInfo = getInfo(who);
            if (Tool.rand.nextInt(10) < 7 && ghostObj.getHp() > ghostObj.getMaxHp() / 2 && ghostObj.getAtt()
                    >= personInfo.getAtt() && personInfo.getHp() <= ghostObj.getHp()) {
                return ghostObj.getName() + "觉得 还有再战之力 ，ta跳到了你面前\n逃跑失败";
            }
            GameJoinDetailService.saveGhostObjIn(who, null);
        }
        return "逃跑完成";
    }

    public static final List<Integer> IDXS = new CopyOnWriteArrayList<>();

    /**
     * 攻击一个魂兽
     *
     * @param who      攻击者 与 魂兽
     * @param att      值
     * @param show     返回攻击后的魂兽信息
     * @param canAttMe 魂兽是否可攻击我
     * @return
     */
    public static String attGho(long who, long att, boolean show, boolean canAttMe, GhostLostBroadcast.KillType type) {
        GhostObj ghostObj = GameJoinDetailService.getGhostObjFrom(who);
        if (ghostObj == null) {
            return "没有遇到魂兽或 已过期,或已死亡";
        }
        try {
            String whos = "";
            boolean isHelp = false;
            StringBuilder sb = new StringBuilder();
            if (ghostObj.getState() == GhostObj.HELPING) {
                isHelp = true;
                whos = ghostObj.getForWhoStr();
                ghostObj = GameJoinDetailService.getGhostObjFrom(Long.parseLong(whos));
            }
            if (IDXS.contains(ghostObj.getIDX())) {
                return "该魂兽,正在被攻击中";
            }
            IDXS.add(ghostObj.getIDX());
            long at2 = randLong(ghostObj.getAtt(), 0.333f, 0.48f);

            if (canAttMe) {
                sb.append(getNameById(ghostObj.getId())).append("对你造成").append(att).append("点伤害\n")
                        .append(GameDetailService.beaten(who, -2, at2));
            }

            ghostObj.updateHp(-att, getInfo(who));

            ghostObj.setHp(ghostObj.getHp() < 0 ? 0 : ghostObj.getHp());
            sb.append("\n你对 ").append(getNameById(ghostObj.getId())).append("造成").append(att).append("点伤害").append("\r\n");
            boolean showY = false;
            if (ghostObj.getHp() > 0) {
                showY = true;
                if (isHelp) {
                    GameJoinDetailService.saveGhostObjIn(Long.parseLong(whos), ghostObj);
                } else {
                    GameJoinDetailService.saveGhostObjIn(who, ghostObj);
                }
            } else {
                sb.append("\n").append(getNameById(ghostObj.getId())).append("被你打败了\n");
                sb.append(willGet(ghostObj.getL(), who, ghostObj.getId()));
                sb.append(willGetXp(ghostObj, who, isHelp));
                ZongDetailService.onKilled(who, ghostObj.getXp());
                if (isHelp) {
                    GameJoinDetailService.saveGhostObjIn(Long.parseLong(whos), null);
                    GameJoinDetailService.saveGhostObjIn(who, null);
                } else {
                    GameJoinDetailService.saveGhostObjIn(who, null);
                }
                //广播
                if (!isHelp) {
                    GhostLostBroadcast.INSTANCE.broadcast(who, ghostObj, type);
                } else {
                    GhostLostBroadcast.INSTANCE.broadcast(Long.parseLong(whos), ghostObj, type);
                }
            }
            if (showY && show) {
                sb.append("\n").append(WillTips(who, ghostObj, false));
            }
            return sb.toString();
        } finally {
            IDXS.remove((Object) ghostObj.getIDX());
        }
    }

    private String att(long who, GhostObj ghostObj) {
        if (getInfo(who).getJak1() > System.currentTimeMillis()) {
            return "攻击冷却中...";
        }
        try {
            putPerson(getInfo(who).setJak1(System.currentTimeMillis() + 2000));
            boolean isHelp = ghostObj.getState() == GhostObj.HELPING;
            String whos = "";
            if (isHelp) {
                whos = ghostObj.getForWhoStr();
                ghostObj = GameJoinDetailService.getGhostObjFrom(Long.parseLong(whos));
            }
            if (IDXS.contains(ghostObj.getIDX())) {
                return "该魂兽,正在被攻击中";
            }
            IDXS.add(ghostObj.getIDX());

            PersonInfo personInfo = getInfo(who);

            long hl1 = randLong(personInfo.getHll(), 0.125f, 0.24f);
            long at1 = randLong(personInfo.getAtt(), 0.35f, 0.48f);
            long at2 = randLong(ghostObj.getAtt(), 0.25f, 0.48f);

            StringBuilder sb = new StringBuilder();

            if (personInfo.getHl() > hl1) {
                sb.append("\n消耗了").append(hl1).append("点魂力\n").append(GameDetailService.consumedHl(who, hl1));
                sb.append("你对").append(getNameById(ghostObj.getId())).append("造成").append(at1).append("点伤害").append("\n");
                ghostObj.updateHp(-at1, personInfo);
                sb.append(GameDetailService.onAtt(who, -2, at1));
            } else {
                sb.append("魂力不足,攻击失败").append("\n");
            }

            sb.append(getNameById(ghostObj.getId())).append("对你造成").append(at2).append("点伤害\n")
                    .append(GameDetailService.beaten(who, -2, at2));

            ghostObj.setHp(ghostObj.getHp() < 0 ? 0 : ghostObj.getHp());

            boolean showI = true;
            boolean showY = false;
            if (isAlive(who)) {
                if (ghostObj.getHp() > 0) {
                    showY = true;
                    showI = true;
                    if (isHelp) {
                        GameJoinDetailService.saveGhostObjIn(Long.parseLong(whos), ghostObj);
                    } else {
                        GameJoinDetailService.saveGhostObjIn(who, ghostObj);
                    }
                } else {
                    showI = true;
                    sb.append("\n").append(getNameById(ghostObj.getId())).append("被你打败了\n");
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
                if (!isHelp) {
                    GhostLostBroadcast.INSTANCE.broadcast(who, ghostObj, GhostLostBroadcast.KillType.NORMAL_ATT);
                } else {
                    GhostLostBroadcast.INSTANCE.broadcast(Long.parseLong(whos), ghostObj, GhostLostBroadcast.KillType.NORMAL_ATT);
                }
            } else {
                sb.append("你被打败了!!!").append(SPLIT_LINE_0);
                if (ghostObj.getHp() < 0) {
                    sb.append(getNameById(ghostObj.getId())).append("也失败了,但你无法获得魂环");
                }
                showY = true;
                showI = true;
                if (isHelp) {
                    ghostObj.setState(GhostObj.NotNeed);
                    GameJoinDetailService.saveGhostObjIn(Long.parseLong(whos), ghostObj);
                }
                GameJoinDetailService.saveGhostObjIn(who, null);
            }
            if (showI) {
                sb.append("\n").append(gameService.info(who)).append("\n");
            }
            if (showY) {
                sb.append("\n").append(WillTips(who, ghostObj, false));
            }
            return sb.toString();
        } finally {
            IDXS.remove((Object) ghostObj.getIDX());
        }
    }

    public static String WillTips(Number qq, GhostObj ghostObj, boolean k) {
        int id = ghostObj.getId();
        long v1 = getInfo(qq).getHj();
        long v2 = ghostObj.getHj();
        int bv = toPercent(v1, v2);
        if (bv >= 120) {
            return "!!!\n你遇到了魂兽\n做出你的选择(选择 攻击/逃跑)\n" + getImgById(id) +
                    getImageFromStrings(
                            "名字:" + ID_2_NAME_MAPS.get(ghostObj.getId()),
                            "等级:" + ghostObj.getL(),
                            "攻击:" + ghostObj.getAtt(),
                            "生命:" + ghostObj.getHp(),
                            "经验:" + ghostObj.getXp(),
                            "精神力:" + ghostObj.getHj()
                    );
        } else {
            return "!!!\n你遇到了魂兽且无法探查真正实力\n做出你的选择(选择 攻击/逃跑)\n" + getImgById(id) +
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

    public static final Map<Long, GhostObj> GHOST_TEMP = new ConcurrentHashMap<>();

    public static GhostObj getGhostObjFrom(long qq) {
        if (GHOST_TEMP.containsKey(qq)) {
            return GHOST_TEMP.get(qq);
        } else {
            String js = getDataString(qq, "decide").toString();
            if (js == null || js.trim().isEmpty()) return null;
            GHOST_TEMP.put(qq, getGhost(js));
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

    public static GhostObj saveGhostObjIn(long qq, GhostObj ghostObj) {
        if (ghostObj == null) {
            putDataString(qq, "decide", "");
            GHOST_TEMP.remove(qq);
        } else {
            putDataString(qq, "decide", objectToJsonString(ghostObj));
            GHOST_TEMP.put(qq, ghostObj);
        }
        return getGhostObjFrom(qq);
    }

    public static String willGet(int level, long who, int id) {
        onKilled(who);
        if (id > 700) {
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
        if (level > 100 * 10000) {
            return Tool.rand.nextInt(100) < 38;
        } else if (level > 10 * 10000) {
            return Tool.rand.nextInt(100) < 54;
        }
        return Tool.rand.nextInt(100) < 75;
    }

    public static String willGetXp(GhostObj ghostObj, long who, boolean isHelp) {
        if (getInfo(who).getLevel() >= 150) {
            return "\n等级最大限制,无法获得经验";
        } else {
            long v = ghostObj.getXp();
            long mxv = getInfo(who).getXpL();
            v = v > mxv / 2 ? mxv / 2 : v;
            if (isHelp) {
                putPerson(getInfo(who).addXp(v / 2));
                return "\n由于在支援别人,所以获得经验减半 获得了" + ghostObj.getXp() / 2 + "点经验";
            } else {
                putPerson(getInfo(who).addXp(v));
                return "\n获得了 " + ghostObj.getXp() + "点经验";
            }
        }
    }

    private static String willGetBone(int level, long who) {
        if (randHh(level)) {
            Integer id = 0;
            int r1 = Tool.rand.nextInt(5) + 1;
            if (level > 5000) {
                if (level < 20000) {
                    id = Integer.valueOf("15" + r1 + "1");
                } else if (level < 350000) {
                    id = Integer.valueOf("15" + r1 + "2");
                } else {
                    id = Integer.valueOf("15" + r1 + "3");
                }
                addToBgs(who, id, ObjType.got);
                return "你获得了 " + getNameById(id) + getImgById(id);
            }
        }
        return "";
    }

    private static String willGetHh(int level, long who) {
        int sid = getHhByGh(level);
        if (randHh(level)) {
            addToBgs(who, sid, ObjType.got);
            return "你获得了" + getNameById(sid);
        }
        return "";
    }

    private static String willGetLr(int level, long who) {
        int r = rand.nextInt(100);
        if (r > 75) {

        } else if (r > 50) {
            return willGetHh(level, who);
        } else if (r > 25) {
            return willGetBone(level, who);
        } else {
            r = rand.nextInt(300);
            int n = rand.nextInt(4) + 1;
            if (r == 1) {
                if (level >= 3000) {
                    addToBgs(who, 1601, ObjType.got);
                    return "你获得了一个" + getNameById(1601) + getImgById(1601);
                }
            } else if (r < 30) {
                addToAqBgs(who, "1005:" + n);
                return String.format("你获得了一个%s耐久的孔雀翎", n) + getImgById(1005);
            } else if (r < 75) {
                addToAqBgs(who, "1004:" + n);
                return String.format("你获得了一个%s耐久的子母追魂夺命胆", n) + getImgById(1004);
            } else if (r < 130) {
                addToAqBgs(who, "1003:" + n);
                return String.format("你获得了一个%s耐久的含沙射影", n) + getImgById(1003);
            } else if (r < 160) {
                addToAqBgs(who, "1002:" + n);
                return String.format("你获得了一个%s耐久的龙须针", n) + getImgById(1002);
            }
        }
        return "";
    }
}
