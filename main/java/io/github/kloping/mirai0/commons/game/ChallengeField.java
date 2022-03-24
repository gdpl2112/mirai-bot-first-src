package io.github.kloping.mirai0.commons.game;

import io.github.kloping.mirai0.unitls.drawers.entity.GameMap;
import io.github.kloping.mirai0.unitls.drawers.entity.MapPosition;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.HashMap;
import java.util.Map;

import static io.github.kloping.mirai0.unitls.Tools.Tool.getEntry;

/**
 * @author github.kloping
 */

@Setter
@Getter
@Accessors(chain = true)
@EqualsAndHashCode
public class ChallengeField {
    public static final Map<Character, Map.Entry<Integer, Integer>> MOVE_CHAR = new HashMap<>();

    static {
        MOVE_CHAR.put('上', getEntry(0, -1));
        MOVE_CHAR.put('下', getEntry(0, 1));
        MOVE_CHAR.put('左', getEntry(-1, 0));
        MOVE_CHAR.put('右', getEntry(1, 0));
    }

    public static final String RED_SIDE = "RED", BLUE_SIDE = "BLUE";
    private int id;
    private long gid;
    private String now = RED_SIDE;
    private Map<Long, ChallengeSide> redSide = new HashMap<>();
    private Map<Long, ChallengeSide> blueSide = new HashMap<>();
    private int w = 10;
    private int h = 5;
    private boolean cycle = true;

    public GameMap getGameMapFrame() {
        GameMap.GameMapBuilder builder = new GameMap.GameMapBuilder()
                .setWidth(w).setHeight(h);
        for (ChallengeSide challengeSide : redSide.values()) {
            builder.append(challengeSide.getX(), challengeSide.getY(),challengeSide.getIcon()
                    , MapPosition.Type.p);
        }
        for (ChallengeSide challengeSide : blueSide.values()) {
            builder.append(challengeSide.getX(), challengeSide.getY(), challengeSide.getIcon()
                    , MapPosition.Type.p);
        }
        return builder.build();
    }

    public boolean canOperation(long id) {
        if (redSide.containsKey(id)) {
            return now.equals(RED_SIDE);
        } else if (blueSide.containsKey(id)) {
            return now.equals(BLUE_SIDE);
        }
        return false;
    }

    public void operation(long id) {
        getChallengeSide(id).setOperation(true);
        testRound();
    }

    private void testRound() {

    }

    public ChallengeSide getChallengeSide(long id) {
        if (redSide.containsKey(id)) {
            return redSide.get(id);
        } else if (blueSide.containsKey(id)) {
            return blueSide.get(id);
        }
        return null;
    }
}
