package Project.services.impl;

import Project.broadcast.game.challenge.ChallengeSteppedBroadcast;
import Project.controllers.gameControllers.GameController;
import Project.interfaces.Iservice.IChallengeService;
import io.github.kloping.MySpringTool.annotations.AutoStand;
import io.github.kloping.MySpringTool.annotations.Entity;
import io.github.kloping.MySpringTool.exceptions.NoRunException;
import io.github.kloping.mirai0.commons.game.ChallengeField;

import static Project.services.detailServices.ChallengeDetailService.A2R;
import static Project.services.detailServices.GameJoinDetailService.getGhostObjFrom;
import static io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet.FinalString.IN_SELECT;

/**
 * @author github.kloping
 */
@Entity
public class ChallengeServiceImpl implements IChallengeService {
    private void testWill(long qid) throws NoRunException {
        if (A2R.containsKey(qid)) {
            throw new NoRunException("不要重复参赛哦.");
        }
        if (getGhostObjFrom(qid) != null) {
            throw new NoRunException(IN_SELECT);
        }
    }

    private void testOp(long qid) throws NoRunException {
    }

    @Override
    public Object startWithBot(long qid, long gid) {
        return "";
//        try {
//            testWill(qid);
//        } catch (NoRunException e) {
//            return e.getMessage();
//        }
//        ChallengeField field = new ChallengeField()
//                .setCycle(true)
//                .setGid(gid);
//        ChallengeSide side = new ChallengeSide();
//        side.setQid(qid);
//        side.setSide(ChallengeField.RED_SIDE).setX(2).setY(3);
//        field.getRedSide().put(qid, side);
//        challengeFieldMap.put(qid, field);
//        return Tool.pathToImg(GameDrawer.drawerMap0(field.getGameMapFrame()));
    }

    @Override
    public Object moveOnChallenge(long id, String str) {
        return "";
//        ChallengeField field = challengeFieldMap.get(id);
//        if (field == null) {
//            return NOT_PARTICIPATION_STR;
//        }
//        if (!field.canOperation(id)) {
//            return WAITING_STR;
//        }
//        ChallengeSide side = field.getChallengeSide(id);
//        AtomicBoolean k0 = new AtomicBoolean(false);
//        AtomicInteger i = new AtomicInteger();
//        for (char c : str.toCharArray()) {
//            ChallengeField.MOVE_CHAR.forEach((k, v) -> {
//                if (k.charValue() == c) {
//                    if (i.get() >= side.getMaxMove()) return;
//                    side.setX(side.getX() + v.getKey());
//                    side.setY(side.getY() + v.getValue());
//                    i.getAndIncrement();
//                    k0.set(true);
//                }
//            });
//        }
//        if (k0.get()) {
//            field.operation(id);
//        }
//        field.flush();
//        return Tool.pathToImg(GameDrawer.drawerMap0(field.getGameMapFrame()));
    }

    @AutoStand
    static GameController gc;

    static {
        ChallengeSteppedBroadcast.INSTANCE.add(new ChallengeSteppedBroadcast.ChallengeSteppedReceiver() {
            @Override
            public void onReceive(ChallengeField field, String side) {
//                MessageTools.sendMessageInGroup("一方结束了行动,请另一方开始行动", field.getGid());
            }
        });
    }
}
