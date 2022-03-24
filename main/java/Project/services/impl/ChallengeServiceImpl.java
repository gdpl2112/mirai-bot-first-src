package Project.services.impl;

import Project.interfaces.Iservice.IChallengeService;
import io.github.kloping.MySpringTool.annotations.Entity;
import io.github.kloping.mirai0.commons.game.ChallengeField;
import io.github.kloping.mirai0.commons.game.ChallengeSide;
import io.github.kloping.mirai0.unitls.Tools.Tool;
import io.github.kloping.mirai0.unitls.drawers.GameDrawer;

import static Project.services.detailServices.ChallengeDetailService.challengeFieldMap;
import static io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet.FinalString.NOT_PARTICIPATION_STR;
import static io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet.FinalString.WAITING_STR;

/**
 * @author github.kloping
 */
@Entity
public class ChallengeServiceImpl implements IChallengeService {
    @Override
    public Object startWithBot(long qid, long gid) {
        ChallengeField field = new ChallengeField()
                .setCycle(true)
                .setGid(gid);
        ChallengeSide side = new ChallengeSide();
        side.setQid(qid);
        side.setSide(ChallengeField.RED_SIDE).setX(2).setY(3);
        field.getRedSide().put(qid, side);
        challengeFieldMap.put(qid, field);
        return Tool.pathToImg(GameDrawer.drawerMap0(field.getGameMapFrame()));
    }

    @Override
    public Object moveOnChallenge(long id, String str) {
        ChallengeField field = challengeFieldMap.get(id);
        if (field == null) {
            return NOT_PARTICIPATION_STR;
        }
        if (!field.canOperation(id)) {
            return WAITING_STR;
        }
        ChallengeSide side = field.getChallengeSide(id);
        for (char c : str.toCharArray()) {
            ChallengeField.MOVE_CHAR.forEach((k, v) -> {
                if (k.charValue() == c) {
                    side.setX(side.getX() + v.getKey());
                    side.setY(side.getY() + v.getValue());
                }
            });
        }
        if (field.isCycle()) {
            side.setX(side.getX() > field.getW() ? 0 : side.getX());
            side.setX(side.getX() < 0 ? field.getW() : side.getX());
            side.setY(side.getY() > field.getH() ? 0 : side.getY());
            side.setY(side.getY() < 0 ? field.getH() : side.getY());
        } else {
            side.setX(side.getX() > field.getW() ? 0 : side.getX());
            side.setX(side.getX() < 0 ? field.getW() : side.getX());
            side.setY(side.getY() > field.getH() ? 0 : side.getY());
            side.setY(side.getY() < 0 ? field.getH() : side.getY());
        }
        return Tool.pathToImg(GameDrawer.drawerMap0(field.getGameMapFrame()));
    }
}
