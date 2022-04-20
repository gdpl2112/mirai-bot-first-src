package Project.services.detailServices.ac.entity.challenge;

import io.github.kloping.mirai0.commons.game.ChallengeGhost;

import java.util.ArrayList;
import java.util.List;

/**
 * @author github.kloping
 */
public class GhostTianQingNiuMang extends ChallengeGhost {
    public static final String NAME = "天青牛蟒";
    public static final List<Integer> LOSE_ITEMS = new ArrayList<>();

    public GhostTianQingNiuMang() {
        super(NAME, LOSE_ITEMS, att, hp, maxHp, hj, maxHj, "", new ArrayList<>());
    }

    @Override
    public void run() {

    }
}
