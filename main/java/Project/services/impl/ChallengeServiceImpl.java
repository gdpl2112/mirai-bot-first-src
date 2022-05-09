package Project.services.impl;

import Project.broadcast.game.PlayerLostBroadcast;
import Project.broadcast.game.challenge.ChallengeSteppedBroadcast;
import Project.controllers.gameControllers.GameController;
import Project.interfaces.Iservice.IChallengeService;
import Project.interfaces.Iservice.IGameService;
import io.github.kloping.MySpringTool.annotations.AutoStand;
import io.github.kloping.MySpringTool.annotations.Entity;
import io.github.kloping.MySpringTool.exceptions.NoRunException;
import io.github.kloping.mirai0.Main.ITools.MessageTools;
import io.github.kloping.mirai0.commons.PersonInfo;
import io.github.kloping.mirai0.commons.broadcast.Receiver;
import io.github.kloping.mirai0.commons.game.ChallengeField;

import static Project.dataBases.GameDataBase.getInfo;
import static Project.services.detailServices.ChallengeDetailService.*;
import static Project.services.detailServices.GameJoinDetailService.getGhostObjFrom;
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
    IGameService service;

    private void testWill(long qid) throws NoRunException {
        if (A2R.containsKey(qid)) {
            throw new NoRunException("不要重复参赛哦.");
        }
        if (getGhostObjFrom(qid) != null) {
            throw new NoRunException(IN_SELECT);
        }
    }

    @Override
    public Object createChallenge(long qid, long gid) {
        try {
            testWill(qid);
        } catch (NoRunException e) {
            return e.getMessage();
        }
        if (WILL_GO.containsKey(qid)) {
            return PLEASE_NOT_REPEAT;
        }
        WILL_GO.put(qid, gid);
        return CREATE_CHALLENGE_OK;
    }

    @Override
    public Object joinChallenge(long q1, long q2) {
        try {
            testWill(q1);
        } catch (NoRunException e) {
            return e.getMessage();
        }
        if (WILL_GO.containsKey(q1)) {
            return PLEASE_NOT_REPEAT;
        }
        if (!WILL_GO.containsKey(q2)) {
            return NOT_FOUND;
        }
        A2R.put(q1, q2);
        A2R.put(q2, q1);
        WILL_GO.put(q1, WILL_GO.get(q2));
        summonTempInfo(q1, q2);
        Receiver receiver = null;
        PlayerLostBroadcast.INSTANCE.add(receiver = new PlayerLostBroadcast.OncePlayerLostReceiver() {
            @Override
            public boolean onReceive(long who, long from) {
                if (who == q1 || who == q2) {
                    MessageTools.sendMessageInGroup("挑战结束\n<At:" + who + "> 胜利 加一星\n<At:" + A2R.get(who) + "> 失败", WILL_GO.get(who));
                    deleteTempInfo(q1, q2);
                    return true;
                }
                return false;
            }
        });
        RECEIVER_MAP.put(q1, receiver);
        RECEIVER_MAP.put(q2, receiver);
        try {
            return JOIN_CHALLENGE_OK;
        } finally {
            MessageTools.sendMessageInGroup(service.info(q1) + "\nVS\n" + service.info(q2), WILL_GO.get(q1));
        }
    }

    private void deleteTempInfo(long q1, long q2) {
        TEMP_PERSON_INFOS.remove(q1);
        TEMP_PERSON_INFOS.remove(q2);
        WILL_GO.remove(q1);
        WILL_GO.remove(q2);
        A2R.remove(q1);
        A2R.remove(q2);
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
}
