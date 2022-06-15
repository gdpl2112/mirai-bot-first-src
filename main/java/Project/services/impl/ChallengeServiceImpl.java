package Project.services.impl;

import Project.broadcast.game.PlayerLostBroadcast;
import Project.broadcast.game.challenge.ChallengeSteppedBroadcast;
import Project.controllers.gameControllers.GameController;
import Project.interfaces.Iservice.IChallengeService;
import Project.interfaces.Iservice.IGameService;
import Project.services.detailServices.ChallengeDetailService;
import io.github.kloping.MySpringTool.annotations.AutoStand;
import io.github.kloping.MySpringTool.annotations.Entity;
import io.github.kloping.MySpringTool.exceptions.NoRunException;
import io.github.kloping.mirai0.Main.ITools.MessageTools;
import io.github.kloping.mirai0.commons.PersonInfo;
import io.github.kloping.mirai0.commons.broadcast.Receiver;
import io.github.kloping.mirai0.commons.game.ChallengeField;
import io.github.kloping.mirai0.commons.gameEntitys.challange.AbstractChallenge;

import java.util.HashMap;
import java.util.Map;

import static Project.dataBases.GameDataBase.getInfo;
import static Project.services.detailServices.ChallengeDetailService.TEMP_PERSON_INFOS;
import static Project.services.detailServices.GameJoinDetailService.getGhostObjFrom;
import static io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet.FinalString.CREATE_CHALLENGE_OK;
import static io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet.FinalString.IN_SELECT;

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
        try {
            testWill(qid);
        } catch (NoRunException e) {
            return e.getMessage();
        }
        service0.challenges.create(qid, new AbstractChallenge() {
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

                p11.setAtt(pi1.getAtt());
                p22.setAtt(pi2.getAtt());

                p11 = toMax(p11);
                p22 = toMax(p22);

                TEMP_PERSON_INFOS.put(p1, p11);
                TEMP_PERSON_INFOS.put(p2, p22);

                Receiver receiver = null;
                PlayerLostBroadcast.INSTANCE.add(receiver = new PlayerLostBroadcast.OncePlayerLostReceiver() {
                    @Override
                    public boolean onReceive(long who, long from) {
                        if (who == p1 || who == p2) {
                            MessageTools.sendMessageInGroup("挑战结束\r\n<At:" + from + "> 胜利\n<At:" + who + "> 失败", getGid());
                            deleteTempInfo(p1, p2);
                            return true;
                        }
                        return false;
                    }
                });
                RECEIVER_MAP.put(p1, receiver);
                RECEIVER_MAP.put(p2, receiver);

                MessageTools.sendMessageInGroup("一方无状态后,挑战结束,缓存信息清除", getGid());

                return this;
            }

            @Override
            public long getGid() {
                return gid;
            }
        });
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
        }
        return "尝试结束";
    }

    private void deleteTempInfo(long q1, long q2) {
        TEMP_PERSON_INFOS.remove(q1);
        TEMP_PERSON_INFOS.remove(q2);
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
        return new PersonInfo()
                .setName(p1.getName())
                .setWhType(p1.getWhType())
                .setWh(p1.getWh()).setXpL(p1.getXpL()).setXp(0L)
                .setHelpC(99).setHelpToc(99).setGold(0L)
                .setK1(Long.MAX_VALUE).setK2(Long.MAX_VALUE).setGk1(Long.MAX_VALUE)
                .setCbk1(Long.MAX_VALUE).setMk1(Long.MAX_VALUE).setUk1(System.currentTimeMillis())
                .setAk1(System.currentTimeMillis()).setJak1(System.currentTimeMillis())
                ;
    }

    private PersonInfo toMax(PersonInfo personInfo) {
        return personInfo.setHl(personInfo.getHll()).setHp(personInfo.getHpL()).setHj(personInfo.getHj());
    }
}
