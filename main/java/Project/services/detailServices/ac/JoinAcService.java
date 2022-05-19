package Project.services.detailServices.ac;

import Project.dataBases.GameDataBase;
import Project.services.autoBehaviors.GhostBehavior;
import Project.services.detailServices.GameDetailService;
import Project.services.detailServices.GameJoinDetailService;
import Project.services.detailServices.ac.entity.GhostWithGroup;
import io.github.kloping.MySpringTool.annotations.AutoStand;
import io.github.kloping.MySpringTool.annotations.Entity;
import io.github.kloping.mirai0.commons.GhostObj;
import io.github.kloping.mirai0.commons.Group;
import io.github.kloping.mirai0.commons.TradingRecord;
import io.github.kloping.mirai0.commons.broadcast.enums.ObjType;
import io.github.kloping.mirai0.unitls.Tools.Tool;

import static Project.dataBases.GameDataBase.*;
import static Project.services.detailServices.GameJoinDetailService.willTips;
import static io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet.FinalString.*;
import static io.github.kloping.mirai0.unitls.Tools.Tool.RANDOM;
import static io.github.kloping.mirai0.unitls.Tools.Tool.randA;

/**
 * @author github-kloping
 * @version 1.0
 */
@Entity
public class JoinAcService {
    public static final int MIN_MEED = 60;
    public static final String[] TIPS0 = {
            "你去落日森林,欣赏了风景<Face:335>",
            "你从花开<Face:64>看到了花落<Face:63>",
            "你去落日森林,溜达了一圈<Face:185>",
    };
    private static final int MUST_MEED = 70;
    private static final int MAX_RAND_2 = 150;
    private static final int MAX_RAND3 = 300;
    private static final int MIN_MEET3 = 50;

    @AutoStand
    GameJoinDetailService gameJoinDetailService;

    public String join(int id, long who, Group group) {
        if (id == 0) {
            return join0(who, group);
        } else if (id == 1) {
            return join1(who, group);
        } else if (id == 2) {
            return join2(who, group);
        } else {
            return "暂未实现";
        }
    }

    public String join0(long who, Group group) {
        GhostObj ghostObj = null;
        int r0 = getInfo(who).getNextR1();
        int r = r0;
        if (r0 == -2) {
            long n = randA(0, 100);
            if (n < 33) {
                ghostObj = gameJoinDetailService.summonFor(String.valueOf(who), 501, 521);
            } else {
                r = Tool.RANDOM.nextInt(31);
            }
        } else if (r0 == -1) {
            r = Tool.RANDOM.nextInt(250);
        } else {
            r = r0;
        }
        getInfo(who).setNextR1(Tool.RANDOM.nextInt(250)).apply();
        if (ghostObj == null) {
            if (r < 3) {
                //十万年
                ghostObj = GhostObj.create(100000, 501, 521);
            } else if (r < 6) {
                //万年
                ghostObj = GhostObj.create(10000, 501, 521);
            } else if (r < 16) {
                //千年
                ghostObj = GhostObj.create(1000, 501, 521);
            } else if (r < 31) {
                //百年
                ghostObj = GhostObj.create(100, 501, 521);
            } else if (r < 61) {
                //十年
                ghostObj = GhostObj.create(10, 501, 521);
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
                int r1 = Tool.RANDOM.nextInt(3) + 1;
                for (int i = 0; i < r1; i++) {
                    addToBgs(who, 1000, ObjType.got);
                }
                return "你去星斗森林,捡到了" + r1 + "个暗器零件已存入背包";
            } else if (r < 190) {
                int rr = Tool.RANDOM.nextInt(90) + 30;
                putPerson(getInfo(who).addGold((long) rr
                        , new TradingRecord()
                                .setFrom(-1)
                                .setMain(who).setDesc("从星斗森林捡到")
                                .setTo(who)
                                .setMany(rr)
                                .setType0(TradingRecord.Type0.gold)
                                .setType1(TradingRecord.Type1.add)
                ));
                return "你去星斗森林,只捡到了" + rr + "个金魂币" + Tool.toFaceMes(String.valueOf(188));
            } else if (Tool.RANDOM.nextInt(1000) == 0) {
                int id = 111;
                addToBgs(who, id, ObjType.got);
                return "震惊!!!\n你去星斗森林捡到一个" + getNameById(id);
            } else {
                return "你去星斗森林,只捡到了个寂寞!" + Tool.toFaceMes(String.valueOf(239));
            }
        }

        if (ghostObj != null) {
            ghostObj.setWhoMeet(who);
            GameJoinDetailService.saveGhostObjIn(who, ghostObj);
            if (ghostObj.getL() > 3000L)
                GhostBehavior.exRun(new GhostBehavior(who, group));
            return willTips(who, ghostObj, false);
        }
        return "你将遇到魂兽,功能为实现,尽请期待";
    }

    public String join1(long who, Group group) {
        int r = 0;
        r = getInfo(who).getNextR2();
        if (r == -1) {
            r = Tool.RANDOM.nextInt(MAX_RAND_2);
        } else if (r == -2) {
            r = Tool.RANDOM.nextInt(MIN_MEED);
        }
        putPerson(getInfo(who).setNextR2(Tool.RANDOM.nextInt(MAX_RAND_2)));
        GhostObj ghostObj = null;
        if (r < MUST_MEED) {
            if (r == 0) {
                ghostObj = gameJoinDetailService.summonFor(String.valueOf(who), 601, 604);
            } else if (r < 3) {
                //十万年0.5%
                ghostObj = GhostObj.create(100000, 601, 604);
            } else if (r < 16) {
                //万年2%
                ghostObj = GhostObj.create(10000, 601, 604);
            } else if (r < 31) {
                //千年5%
                ghostObj = GhostObj.create(1000, 601, 604);
            } else if (r < MIN_MEED) {
                //百年8%
                ghostObj = GhostObj.create(100, 601, 604);
            } else {
                addToBgs(who, 112, ObjType.got);
                return "你去极贝之地,捡到了一个精神神石已存入背包" + Tool.toFaceMes("318");
            }
        } else {
            return "你去极北之地 ,只捡到了个寂寞。。" + Tool.toFaceMes(String.valueOf(271));
        }
        if (ghostObj != null) {
            ghostObj.setWhoMeet(who);
            GameJoinDetailService.saveGhostObjIn(who, ghostObj);
            int id = ghostObj.getId();
            if (ghostObj.getL() > 3000L)
                GhostBehavior.exRun(new GhostBehavior(who, group));
            return willTips(who, ghostObj, false);
        }
        return "你将遇到魂兽,功能为实现,尽请期待";
    }

    public String join2(long who, Group group) {
        int r = getInfo(who).getNextR3();
        switch (r) {
            case -1:
                r = RANDOM.nextInt(MAX_RAND3);
                break;
            case -2:
                r = RANDOM.nextInt(MIN_MEET3);
                break;
            default:
                break;
        }
        getInfo(who).setNextR3(RANDOM.nextInt(MAX_RAND3)).apply();
        GhostWithGroup ghostObj = null;
        if (r == 0) {
            ghostObj = gameJoinDetailService.summonFor(String.valueOf(who), 701, 706);
        } else if (r < 3) {
            ghostObj = GhostObj.create(1000000, 701, 706);
        } else if (r < 16) {
            ghostObj = GhostObj.create(100000, 701, 706);
        } else if (r < 25) {
            ghostObj = GhostObj.create(10000, 701, 706);
        } else if (r < MIN_MEET3) {
            ghostObj = GhostObj.create(1000, 701, 706);
        } else if (r < MIN_MEET3 + 20) {
            GameDetailService.addHp(who, 3);
            return "<Pic:./images/ac2.png>.\n" + JOIN_AC3_EVENT0;
        } else if (r < MIN_MEET3 + 40) {
            GameDetailService.addHl(who, 4);
            return "<Pic:./images/ac2.png>.\n" + JOIN_AC3_EVENT1;
        } else if (r < MIN_MEET3 + 60) {
            GameDetailService.addHj(who, 5);
            return "<Pic:./images/ac2.png>.\n" + JOIN_AC3_EVENT2;
        } else if (r < MIN_MEET3 + 100) {
            GameDataBase.addToBgs(who, 114, ObjType.got);
            return "<Pic:./images/ac2.png>.\n你去落日森林捡到了一片落叶碎片\uD83C\uDF42已存入背包";
        } else {
            return "<Pic:./images/ac2.png>.\n" + Tool.getRandT(TIPS0);
        }
        if (ghostObj != null) {
            ghostObj.setGroup(group);
            ghostObj.setWhoMeet(who);
            GameJoinDetailService.saveGhostObjIn(who, ghostObj);
            int id = ghostObj.getId();
            if (ghostObj.getL() > 3000L) {
                GhostBehavior.exRun(GhostBehavior.create(who, group, ghostObj.getLevel()));
            }
            return willTips(who, ghostObj, false);
        }
        return ERR_TIPS;
    }
}
