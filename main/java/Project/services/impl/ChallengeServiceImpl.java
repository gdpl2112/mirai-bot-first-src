package Project.services.impl;

import Project.broadcast.game.PlayerLostBroadcast;
import Project.broadcast.game.challenge.ChallengeSteppedBroadcast;
import Project.broadcast.game.challenge.TrialChallengeEndBroadcast;
import Project.controllers.auto.ConfirmController;
import Project.controllers.gameControllers.GameController;
import Project.dataBases.GameDataBase;
import Project.interfaces.Iservice.IChallengeService;
import Project.interfaces.Iservice.IGameService;
import Project.services.autoBehaviors.GhostBehavior;
import Project.services.detailServices.ChallengeDetailService;
import Project.services.detailServices.GameBoneDetailService;
import Project.services.detailServices.GameJoinDetailService;
import Project.services.detailServices.roles.v1.TagManagers;
import io.github.kloping.MySpringTool.annotations.AutoStand;
import io.github.kloping.MySpringTool.annotations.Entity;
import io.github.kloping.MySpringTool.exceptions.NoRunException;
import io.github.kloping.mirai0.Main.BotStarter;
import io.github.kloping.mirai0.Main.ITools.MessageTools;
import io.github.kloping.mirai0.commons.GhostObj;
import io.github.kloping.mirai0.commons.Group;
import io.github.kloping.mirai0.commons.PersonInfo;
import io.github.kloping.mirai0.commons.broadcast.Receiver;
import io.github.kloping.mirai0.commons.broadcast.enums.ObjType;
import io.github.kloping.mirai0.commons.game.ChallengeField;
import io.github.kloping.mirai0.commons.gameEntitys.challange.AbstractChallenge;
import io.github.kloping.mirai0.unitls.Tools.GameTool;
import io.github.kloping.mirai0.unitls.Tools.Tool;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import static Project.dataBases.GameDataBase.*;
import static Project.services.detailServices.ChallengeDetailService.TEMP_PERSON_INFOS;
import static Project.services.detailServices.GameJoinDetailService.getGhostObjFrom;
import static Project.services.detailServices.GameJoinDetailService.willTips;
import static io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet.FinalString.*;

/**
 * @author github.kloping
 */
@Entity
public class ChallengeServiceImpl implements IChallengeService {
    @AutoStand
    static GameController gc;

    static {
        ChallengeSteppedBroadcast.INSTANCE.add(new ChallengeSteppedBroadcast.ChallengeSteppedReceiver() {
            @Override
            public void onReceive(ChallengeField field, String side) {

            }
        });
    }

    @AutoStand
    ChallengeDetailService service0;


    @AutoStand
    IGameService service;
    private final Map<Long, Receiver> RECEIVER_MAP = new HashMap<>();

    private void testWill(long qid) throws NoRunException {
        if (service0.challenges.contains(qid)) {
            throw new NoRunException("不要重复参赛哦.");
        }
        if (getGhostObjFrom(qid) != null) {
            throw new NoRunException(IN_SELECT);
        }
    }

    @Override
    public Object createTrialChallenge(long qid, long gid) {
        if (!BotStarter.test) return "关闭调试中";
        try {
            testWill(qid);
        } catch (NoRunException e) {
            return e.getMessage();
        }
        AbstractChallenge challenge = new AbstractChallenge() {
            @Override
            public boolean ready() {
                return p1 > 0 && p2 > 0;
            }

            @Override
            public AbstractChallenge start() {
                PersonInfo pi1 = getInfo(p1);
                PersonInfo pi2 = getInfo(p2);

                PersonInfo p11 = copyBase(pi1);
                PersonInfo p22 = copyBase(pi2);

                p11.setAtt(pi1.getAtt() / 2);
                p22.setAtt(pi2.getAtt() / 2);

                p11 = toMax(p11);
                p22 = toMax(p22);

                TEMP_PERSON_INFOS.put(p1, p11);
                TEMP_PERSON_INFOS.put(p2, p22);

                Receiver receiver = null;
                PlayerLostBroadcast.INSTANCE.add(receiver = new PlayerLostBroadcast.OncePlayerLostReceiver() {
                    @Override
                    public boolean onReceive(long who, long from) {
                        if (who == p1 || who == p2) {
                            MessageTools.instance.sendMessageInGroup("挑战结束\r\n<At:" + from + "> 胜利\n<At:" + who + "> 失败", getGid());
                            deleteTempInfo(p1, p2);
                            return true;
                        }
                        TrialChallengeEndBroadcast.INSTANCE.broadcast(from, who, 1);
                        state = FINISHED;
                        return false;
                    }
                });
                RECEIVER_MAP.put(p1, receiver);
                RECEIVER_MAP.put(p2, receiver);

                MessageTools.instance.sendMessageInGroup("一方无状态后,挑战结束,缓存信息清除", getGid());
                state = PROCESSING;
                return this;
            }

            @Override
            public long getGid() {
                return gid;
            }
        };
        service0.challenges.create(qid, challenge);
        return CREATE_CHALLENGE_OK;
    }

    @Override
    public Object joinChallenge(long q1, long q2) {
        try {
            testWill(q1);
        } catch (NoRunException e) {
            return e.getMessage();
        }
        return service0.challenges.join(q1, q2) ? "参与成功" : "参与失败";
    }

    @Override
    public Object destroy(long qid) {
        if (service0.challenges.contains(qid)) {
            deleteTempInfo(qid, service0.challenges.Q2Q.get(qid));
        } else {
            TEMP_PERSON_INFOS.remove(qid);
        }
        return "尝试结束";
    }

    public static Map<GameBoneDetailService.Type, Number> EMPTY = new HashMap<>();

    private void deleteTempInfo(long q1, long q2) {
        GameBoneDetailService.TEMP_ATTR.getOrDefault(q1, EMPTY).clear();
        GameBoneDetailService.TEMP_ATTR.getOrDefault(q2, EMPTY).clear();

        TagManagers.getTagManager(q1).removeAll();
        TagManagers.getTagManager(q2).removeAll();

        TEMP_PERSON_INFOS.remove(q1);
        TEMP_PERSON_INFOS.remove(q2);

        try {
            service0.challenges.destroy(q1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            service0.challenges.destroy(q2);
        } catch (Exception e) {
            e.printStackTrace();
        }

        PlayerLostBroadcast.INSTANCE.remove(RECEIVER_MAP.get(q1));
        PlayerLostBroadcast.INSTANCE.remove(RECEIVER_MAP.get(q2));
    }

    private void summonTempInfo(long q1, long q2) {
        PersonInfo p1 = getInfo(q1);
        PersonInfo p2 = getInfo(q2);
        PersonInfo p11 = copyBase(p1);
        PersonInfo p22 = copyBase(p2);

        Long hp = (p1.getHpL() + p2.getHpL()) / 2;
        Long att = (p1.att() + p2.att()) / 2 / 2;
        Long hl = (p1.getHll() + p2.getHll()) / 2;
        Long hj = (p1.getHjL() + p2.getHjL()) / 2;
        Integer level = (p1.getLevel() + p2.getLevel()) / 2;
        p11.setHpl(hp).setHp(hp).setAtt(att).setHll(hl).setHl(hl).setHj(hj).setHjL(hj).setLevel(level);
        p22.setHpl(hp).setHp(hp).setAtt(att).setHll(hl).setHl(hl).setHj(hj).setHjL(hj).setLevel(level);

        TEMP_PERSON_INFOS.put(q1, p11);
        TEMP_PERSON_INFOS.put(q2, p22);
    }

    private PersonInfo copyBase(PersonInfo p1) {
        PersonInfo pInfo = new PersonInfo();
        pInfo.setName(p1.getName())
                .setWhType(p1.getWhType())
                .setWh(p1.getWh())
                .setXpL(p1.getXpL()).setXp(0L)
                .setHpl(p1.getHpL())
                .setHjL(p1.getHjL())
                .setHll(p1.getHll())
                .setHelpC(99).setHelpToc(99).setGold(0L)
                .setK1(Long.MAX_VALUE).setK2(Long.MAX_VALUE).setGk1(Long.MAX_VALUE)
                .setCbk1(Long.MAX_VALUE).setMk1(Long.MAX_VALUE).setUk1(System.currentTimeMillis())
                .setAk1(System.currentTimeMillis()).setJak1(System.currentTimeMillis());
        return pInfo;
    }

    private PersonInfo toMax(PersonInfo personInfo) {
        return personInfo.setHl(personInfo.getHll()).setHp(personInfo.getHpL()).setHj(personInfo.getHjL());
    }


    @AutoStand
    GameJoinDetailService gameJoinDetailService;

    @Override
    public Object joinChallenge(long qid, String str, Group group) {
        int ghostId = NAME_2_ID_MAPS.get(str);
        if (ghostId < 500 || ghostId > 1000) return ERR_TIPS;
        int needId = 129;
        if (!GameDataBase.containsInBg(needId, qid)) return "您没有" + ID_2_NAME_MAPS.get(needId);
        try {
            Method method = this.getClass().getDeclaredMethod("joinChallengeNow", long.class, int.class, Group.class);
            ConfirmController.regConfirm(qid, method, this, new Object[]{qid, ghostId, group});
            return String.format("即将挑战魂兽'%s' \r\n它是一只'%s等级'魂兽\r\n请在30秒内回复 确定/确认/取消",
                    ID_2_NAME_MAPS.get(ghostId), GameTool.getLevelByGhostId(ghostId));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return ERR_TIPS;
    }

    public Object joinChallengeNow(long qid, int ghostId, Group group) {
        int r = -1;
        GhostObj ghostObj = null;
        if (ghostId < 600) {
            r = Tool.tool.RANDOM.nextInt(10);
            if (r <= 7) {
                switch (GameTool.getLevelByGhostId(ghostId)) {
                    case "十":
                        ghostObj = GhostObj.create(10, ghostId);
                        break;
                    case "百":
                        ghostObj = GhostObj.create(100, ghostId);
                        break;
                    case "千":
                        ghostObj = GhostObj.create(1000, ghostId);
                        break;
                    case "万":
                        ghostObj = GhostObj.create(10000, ghostId);
                        break;
                    case "十万":
                        ghostObj = GhostObj.create(100000, ghostId);
                        break;
                    case "百万":
                        ghostObj = GhostObj.create(1000000, ghostId);
                        break;
                    case "神":
                        ghostObj = GhostObj.create(10000000, ghostId);
                        break;
                    default:
                        ghostObj = null;
                        break;
                }
            } else {
                ghostObj = gameJoinDetailService.summonFor(String.valueOf(qid), ghostId);
            }
        } else if (ghostId < 700) {
            r = Tool.tool.RANDOM.nextInt(13);
            if (r == 0) {
                ghostObj = gameJoinDetailService.summonFor(String.valueOf(qid), ghostId);
            } else if (r <= 2) {
                ghostObj = GhostObj.create(1000000, ghostId);
            } else if (r <= 5) {
                ghostObj = GhostObj.create(100000, ghostId);
            } else if (r <= 8) {
                ghostObj = GhostObj.create(10000, ghostId);
            } else if (r <= 12) {
                ghostObj = GhostObj.create(1000, ghostId);
            }
        } else if (ghostId < 800) {
            r = Tool.tool.RANDOM.nextInt(13);
            if (r == 0) {
                ghostObj = gameJoinDetailService.summonFor(String.valueOf(qid), ghostId);
            } else if (r <= 2) {
                ghostObj = GhostObj.create(1000000, ghostId);
            } else if (r <= 5) {
                ghostObj = GhostObj.create(100000, ghostId);
            } else if (r <= 8) {
                ghostObj = GhostObj.create(10000, ghostId);
            } else if (r <= 12) {
                ghostObj = GhostObj.create(1000, ghostId);
            }
        }
        GameDataBase.removeFromBgs(qid, 129, ObjType.use);
        ghostObj.setWhoMeet(qid);
        GameJoinDetailService.saveGhostObjIn(qid, ghostObj);
        if (ghostObj.getL() > 3000L)
            GhostBehavior.exRun(new GhostBehavior(qid, group));
        return willTips(qid, ghostObj, false);
    }
}
