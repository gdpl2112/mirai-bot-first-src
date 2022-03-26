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
    @AutoStand
    static GameController gc;

    static {
        ChallengeSteppedBroadcast.INSTANCE.add(new ChallengeSteppedBroadcast.ChallengeSteppedReceiver() {
            @Override
            public void onReceive(ChallengeField field, String side) {
            }
        });
    }

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
    }

    @Override
    public Object moveOnChallenge(long id, String str) {
        return "";
    }
}
