package Project.Services.DetailServices;


import Entitys.Group;
import Entitys.gameEntitys.GhostObj;
import Entitys.gameEntitys.PersonInfo;
import Project.Services.AutoBehaviors.Ghost_Behavior;
import Project.Services.Iservice.IGameService;
import Project.Tools.GameTool;
import Project.Tools.Tool;
import Project.broadcast.enums.ObjType;
import Project.broadcast.game.GhostLostBroadcast;
import Project.broadcast.game.JoinBroadcast;
import io.github.kloping.MySpringTool.annotations.AutoStand;
import io.github.kloping.MySpringTool.annotations.Entity;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static Project.DataBases.GameDataBase.*;
import static Project.DataBases.skill.SkillDataBase.toPercent;
import static Project.Tools.GameTool.*;
import static Project.Tools.JSONUtils.JsonStringToObject;
import static Project.Tools.JSONUtils.ObjectToJsonString;
import static Project.Tools.Tool.randA;
import static Project.Tools.Tool.randLong;
import static Project.drawers.Drawer.getImageFromStrings;

@Entity
public class GameJoinDetailService {

    @AutoStand
    IGameService gameService;

    public String run(int id, long who, Group group) {
        try {
            //广播
            JoinBroadcast.INSTANCE.broadcast(who, id);
            if (id == 0) {
                String str = join0(who, group);
                return str;
            }
            if (id == 1) {
                String str = join1(who, group);
                return str;
            }
            return "未知错误";
        } finally {
            int r = Tool.rand.nextInt(8) + 10;
            putPerson(getInfo(who).setK2(System.currentTimeMillis() + r * 60 * 1000));
        }
    }

    private GhostObj isUse107(String who) {
        PersonInfo personInfo = getInfo(who);
        String da = personInfo.getUsinged();
        if (da == null || da.equals("null") || da.isEmpty())
            return null;
        else {
            putPerson(personInfo.setUsinged("null"));
            GhostObj ghostObj = null;
            long n = randA(0, 100);
            if (n < 36) {
                ghostObj = summonAFor(who, 501, 521);
            } else {
                ghostObj = new GhostObj(-1, 0, 0, 0, 0);
            }
            return ghostObj;
        }
    }

    public String join0(long who, Group group) {
        int r = Tool.rand.nextInt(250);
        GhostObj ghostObj = isUse107(String.valueOf(who));
        boolean need = true;
        if (ghostObj != null)
            if (ghostObj.getHp() == -1) {
                r = Tool.rand.nextInt(51);
            } else {
                if (ghostObj.getHp() > 1)
                    need = false;
            }
        int ro = r;
        r = getInfo(who).getNextR1();
        if (r == -1) {
            r = ro;
        }
        if (need) {
            if (r < 2) {
                //十万年0.5%
                ghostObj = GhostObj.create(100000, 500, 521);
            } else if (r < 5) {
                //万年2%
                ghostObj = GhostObj.create(10000, 500, 521);
            } else if (r < 15) {
                //千年5%
                ghostObj = GhostObj.create(1000, 500, 521);
            } else if (r < 31) {
                //百年8%
                ghostObj = GhostObj.create(100, 500, 521);
            } else if (r < 61) {
                //十年15%
                ghostObj = GhostObj.create(10, 500, 521);
            } else if (r < 71) {
                //时光胶囊5%
                addToBgs(who, 101, ObjType.got);
                return "你去星斗森林,只捡到了一个时光胶囊已存入背包";
            } else if (r < 81) {
                //恢复药水5%
                addToBgs(who, 102, ObjType.got);
                return "你去星斗森林,只捡到了一个恢复药水已存入背包";
            } else if (r < 91) {
                //大瓶经验5%
                addToBgs(who, 103, ObjType.got);
                return "你去星斗森林,只捡到了一个大瓶经验已存入背包";
            } else if (r < 116) {
                int r1 = Tool.rand.nextInt(3) + 1;
                for (int i = 0; i < r1; i++) {
                    addToBgs(who, 1000, ObjType.got);
                }
                return "你去星斗森林,捡到了" + r1 + "个暗器零件已存入背包";
            } else if (r < 190) {
                int rr = Tool.rand.nextInt(90) + 30;
                putPerson(getInfo(who).addGold((long) rr));
                return "你去星斗森林,只捡到了" + rr + "个金魂币" + Tool.toFaceMes(String.valueOf(188));
            } else if (Tool.rand.nextInt(1000) == 0) {
                int id = 111;
                addToBgs(who, id, ObjType.got);
                return "震惊!!!\n你去星斗森林捡到一个" + getNameById(id);
            } else {
                return "你去星斗森林,只捡到了个寂寞!" + Tool.toFaceMes(String.valueOf(239));
            }
        }
        if (ghostObj != null) {
            GameJoinDetailService.saveGhostObjIn(who, ghostObj);
            System.out.println(ghostObj);
            int id = ghostObj.getId();
            if (ghostObj.getL() > 3000L)
                Ghost_Behavior.ExRun(new Ghost_Behavior(who, group));
            return WillTips(who, ghostObj, false);
        }
        return "你将遇到魂兽,功能为实现,尽请期待";
    }

    private static int maxRand2 = 150;
    public static int minMeed = 60;
    private static int mustMeed = 70;

    public String join1(long who, Group group) {
        int r = 0;
        r = getInfo(who).getNextR2();
        if (r == -1) {
            r = Tool.rand.nextInt(maxRand2);
        } else if (r == -2) {
            r = Tool.rand.nextInt(minMeed);
        }
        putPerson(getInfo(who).setNextR2(Tool.rand.nextInt(maxRand2)));
        GhostObj ghostObj = null;
        if (r < mustMeed) {
            if (r == 0) {
                ghostObj = summonAFor(String.valueOf(who), 601, 604);
            } else if (r < 3) {
                //十万年0.5%
                ghostObj = GhostObj.create(100000, 601, 604);
            } else if (r < 16) {
                //万年2%
                ghostObj = GhostObj.create(10000, 601, 604);
            } else if (r < 31) {
                //千年5%
                ghostObj = GhostObj.create(1000, 601, 604);
            } else if (r < minMeed) {
                //百年8%
                ghostObj = GhostObj.create(100, 601, 604);
            } else {
                addToBgs(who, 112, ObjType.got);
                return "你去极贝之地,捡到了一个精神神石已存入背包" + Tool.toFaceMes("318");
            }
            int r1 = Tool.rand.nextInt(3);
            ghostObj.setId(601 + r1);
        } else {
            return "你去极北之地 ,只捡到了个寂寞。。" + Tool.toFaceMes(String.valueOf(271));
        }
        if (ghostObj != null) {
            GameJoinDetailService.saveGhostObjIn(who, ghostObj);
            System.out.println(ghostObj);
            int id = ghostObj.getId();
            if (ghostObj.getL() > 3000L)
                Ghost_Behavior.ExRun(new Ghost_Behavior(who, group));
            return WillTips(who, ghostObj, false);
        }
        return "你将遇到魂兽,功能为实现,尽请期待";
    }

    /**
     * @param who
     * @param idType 5 星斗 6 极北
     * @return
     */
    public GhostObj summonAFor(String who, int idMin, int idMax) {
        PersonInfo personInfo = getInfo(who);
        float bl = getAllHHBL(Long.valueOf(who));
        return new GhostObj(
                (long) (personInfo.getAtt() * bl),
                personInfo.getHpl(),
                (long) (personInfo.getXpL() / GameTool.getRandXl(personInfo.getLevel())),
                idMin, idMax,
                personInfo.getLevel(),
                true);
    }

    public Object Select(int id, GhostObj ghostObj, long who) {
        PersonInfo personInfo = getInfo(who);
        switch (id) {
            case 0:
                String m1 = att(who, ghostObj);
                return m1;
            case 1:
                return taop(ghostObj, who);
            default:
                break;
        }
        return "未知选择";
    }

    private String taop(GhostObj ghostObj, long who) {
        if (ghostObj.getState() == GhostObj.HELPING) {
            String whos = ghostObj.getForWhoStr();
            GameJoinDetailService.saveGhostObjIn(who, null);
            GhostObj ghostObj1 = null;
            ghostObj1 = GameJoinDetailService.getGhostObjFrom(who);
            ghostObj1.setState(GhostObj.NotNeed);
            GameJoinDetailService.saveGhostObjIn(Long.parseLong(whos), ghostObj1);
        } else {
            PersonInfo personInfo = getInfo(who);
            if (Tool.rand.nextInt(10) < 7 && ghostObj.getHp() > ghostObj.getMaxHp() / 2 && ghostObj.getAtt() >= personInfo.getAtt() && personInfo.getHp() <= ghostObj.getHp()) {
                return ghostObj.getName() + "觉得 还有再战之力 ，ta跳到了你面前\n逃跑失败";
            }
            GameJoinDetailService.saveGhostObjIn(who, null);
        }
        return "逃跑完成";
    }

    public static final List<Integer> idxs = new CopyOnWriteArrayList<>();

    public static String AttGho(long who, long att, boolean show, boolean canAtMe) {
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
            if (idxs.contains(ghostObj.getIDX()))
                return "该魂兽,正在被攻击中";
            idxs.add(ghostObj.getIDX());
            long at2 = randLong(ghostObj.getAtt(), 0.333f, 0.48f);
            if (canAtMe)
                sb.append(getNameById(ghostObj.getId())).append("对你造成").append(att).append("点伤害\n").append(GameDetailService.Beaten(who, -2, at2));

            ghostObj.updateHp(-att);
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
                sb.append(WillGet(ghostObj.getL(), who, ghostObj.getId()));
                sb.append(WillGetXp(ghostObj, who, isHelp));
                ZongDetailService.OnKilled(who, ghostObj.getXp());
                if (isHelp) {
                    GameJoinDetailService.saveGhostObjIn(Long.parseLong(whos), null);
                    GameJoinDetailService.saveGhostObjIn(who, null);
                } else {
                    GameJoinDetailService.saveGhostObjIn(who, null);
                }
                //广播
                if (!isHelp)
                    GhostLostBroadcast.INSTANCE.broadcast(who, ghostObj);
                else
                    GhostLostBroadcast.INSTANCE.broadcast(Long.parseLong(whos), ghostObj);
            }
            if (showY && show) sb.append("\n").append(WillTips(who, ghostObj, false));
            return sb.toString();
        } finally {
            idxs.remove((Object) ghostObj.getIDX());
        }
    }

    private String att(long who, GhostObj ghostObj) {
        if (getInfo(who).getJak1() > System.currentTimeMillis())
            return "攻击冷却中...";
        try {
            putPerson(getInfo(who).setJak1(System.currentTimeMillis() + 2000));
            boolean isHelp = ghostObj.getState() == GhostObj.HELPING;
            String whos = "";
            if (isHelp) {
                whos = ghostObj.getForWhoStr();
                ghostObj = GameJoinDetailService.getGhostObjFrom(Long.parseLong(whos));
            }
            if (idxs.contains(ghostObj.getIDX()))
                return "该魂兽,正在被攻击中";
            idxs.add(ghostObj.getIDX());
            PersonInfo personInfo = getInfo(who);
            long hl1 = randLong(personInfo.getHll(), 0.125f, 0.25f);
            long at1 = randLong(personInfo.getAtt(), 0.35f, 0.48f);
            long at2 = randLong(ghostObj.getAtt(), 0.333f, 0.48f);
            StringBuilder sb = new StringBuilder();
            if (personInfo.getHl() > hl1) {
                sb.append("\n消耗了").append(hl1).append("点魂力\n").append(GameDetailService.ConsumedHl(who, hl1));
                sb.append("你对").append(getNameById(ghostObj.getId())).append("造成").append(at1).append("点伤害").append("\n");
                ghostObj.updateHp(-at1);
                sb.append(GameDetailService.onAtt(who, -2, at1));
            } else {
                sb.append("魂力不足,攻击失败").append("\n");
            }
            sb.append(getNameById(ghostObj.getId())).append("对你造成").append(at2).append("点伤害\n")
                    .append(GameDetailService.Beaten(who, -2, at2));
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
                    sb.append(WillGet(ghostObj.getL(), who, ghostObj.getId()));
                    sb.append(WillGetXp(ghostObj, who, isHelp));
                    ZongDetailService.OnKilled(who, ghostObj.getXp());
                    if (isHelp) {
                        GameJoinDetailService.saveGhostObjIn(Long.parseLong(whos), null);
                        GameJoinDetailService.saveGhostObjIn(who, null);
                    } else {
                        GameJoinDetailService.saveGhostObjIn(who, null);
                    }
                }
                //广播
                if (!isHelp)
                    GhostLostBroadcast.INSTANCE.broadcast(who, ghostObj);
                else
                    GhostLostBroadcast.INSTANCE.broadcast(Long.parseLong(whos), ghostObj);
            } else {
                sb.append("你被打败了!!!");
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
            if (showI)
                sb.append("\n").append(gameService.info(who)).append("\n");
            if (showY) sb.append("\n").append(WillTips(who, ghostObj, false));
            return sb.toString();
        } finally {
            idxs.remove((Object) ghostObj.getIDX());
        }
    }

    public static synchronized String WillTips(Number qq, GhostObj ghostObj, boolean k) {
        int id = ghostObj.getId();
        long v1 = getInfo(qq).getHj();
        long v2 = ghostObj.getHj();
        int bv = toPercent(v1, v2);
        if (bv >= 125)
            return "!!!\n你遇到了魂兽\n做出你的选择(选择 攻击/逃跑)\n" + getImgById(id) +
                    getImageFromStrings(
                            "名字:" + id2NameMaps.get(ghostObj.getId()),
                            "等级:" + ghostObj.getL(),
                            "攻击:" + ghostObj.getAtt(),
                            "生命:" + ghostObj.getHp(),
                            "经验:" + ghostObj.getXp(),
                            "精神力:" + ghostObj.getHj()
                    );
        else
            return "!!!\n你遇到了魂兽且无法探查真正实力\n做出你的选择(选择 攻击/逃跑)\n" + getImgById(id) +
                    getImageFromStrings(
                            "名字:" + id2NameMaps.get(ghostObj.getId()),
                            "等级:" + getLevelTips(ghostObj.getL()),
                            "攻击:??",
                            "生命:??",
                            "经验:??",
                            "精神力:??");
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

    public static synchronized GhostObj getGhostObjFrom(long qq) {
        String js = getDataString(qq, "decide").toString();
        if (js == null || js.trim().isEmpty()) return null;
        GhostObj g = JsonStringToObject(js, GhostObj.class);
        return g;
    }

    public static synchronized GhostObj saveGhostObjIn(long qq, GhostObj ghostObj) {
        GhostObj ghostObj1 = getGhostObjFrom(qq);
        if (ghostObj == null)
            putDataString(qq, "decide", "");
        else
            putDataString(qq, "decide", ObjectToJsonString(ghostObj));
        return ghostObj1;
    }

    public static String WillGet(int level, long who, int id) {
        OnKilldc(who);
        if (id > 600) {
            return WillGetBone(level, who);
        } else {
            return WillGetHh(level, who);
        }
    }

    /**
     * 几率获取魂环
     *
     * @param level
     * @return
     */
    public static boolean randHh(int level) {
        if (level > 100 * 10000) return Tool.rand.nextInt(100) < 38;
        else if (level > 10 * 10000) return Tool.rand.nextInt(100) < 54;
        return Tool.rand.nextInt(100) < 75;
    }

    public static String WillGetXp(GhostObj ghostObj, long who, boolean isHelp) {
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

    private static String WillGetBone(int level, long who) {
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

    private static String WillGetHh(int level, long who) {
        int sid = getHhByGh(level);
        if (randHh(level)) {
            addToBgs(who, sid, ObjType.got);
            return "你获得了" + getNameById(sid);
        }
        return "";
    }
}
