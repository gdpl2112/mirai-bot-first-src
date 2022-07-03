package io.github.kloping.mirai0.commons.game;

import Project.broadcast.game.challenge.ChallengeSteppedBroadcast;
import io.github.kloping.mirai0.commons.Group;
import io.github.kloping.mirai0.unitls.Tools.Tool;
import io.github.kloping.mirai0.unitls.drawers.entity.GameMap;
import io.github.kloping.mirai0.unitls.drawers.entity.MapPosition;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.HashMap;
import java.util.Map;


/**
 * @author github.kloping
 */

@Setter
@Getter
@Accessors(chain = true)
@EqualsAndHashCode
public class ChallengeField {
    public static final Map<Character, Map.Entry<Integer, Integer>> MOVE_CHAR = new HashMap<>();
    public static final String RED_SIDE = "RED", BLUE_SIDE = "BLUE";

    static {
        MOVE_CHAR.put('上', Tool.tool.getEntry(0, -1));
        MOVE_CHAR.put('下', Tool.tool.getEntry(0, 1));
        MOVE_CHAR.put('左', Tool.tool.getEntry(-1, 0));
        MOVE_CHAR.put('右', Tool.tool.getEntry(1, 0));
    }

    private int id;
    private long gid;
    private String now = RED_SIDE;
    private int step = 0;
    private Map<Long, ChallengeSide> redSide = new HashMap<>();
    private Map<Long, ChallengeSide> blueSide = new HashMap<>();
    private int w = 10;
    private int h = 5;
    private boolean cycle = true;

    public GameMap getGameMapFrame() {
        GameMap.GameMapBuilder builder = new GameMap.GameMapBuilder()
                .setWidth(w).setHeight(h);
        for (ChallengeSide challengeSide : redSide.values()) {
            builder.append(challengeSide.getX(), challengeSide.getY(), challengeSide.getIcon()
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
        step++;
        testRound();
    }

    private void testRound() {
        if (RED_SIDE.equals(now) && step >= redSide.values().size()) {
            step = 0;
            broadcastStepped();
            now = BLUE_SIDE;
        } else if (BLUE_SIDE.equals(now) && step >= blueSide.values().size()) {
            step = 0;
            broadcastStepped();
            now = RED_SIDE;
        }
    }

    public ChallengeSide getChallengeSide(long id) {
        if (redSide.containsKey(id)) {
            return redSide.get(id);
        } else if (blueSide.containsKey(id)) {
            return blueSide.get(id);
        }
        return null;
    }

    public void flush() {
        for (ChallengeSide side : redSide.values()) {
            if (isCycle()) {
                side.setX(side.getX() > getW() ? 0 : side.getX());
                side.setX(side.getX() < 0 ? getW() : side.getX());
                side.setY(side.getY() > getH() ? 0 : side.getY());
                side.setY(side.getY() < 0 ? getH() : side.getY());
            } else {
                side.setX(side.getX() > getW() ? 0 : side.getX());
                side.setX(side.getX() < 0 ? getW() : side.getX());
                side.setY(side.getY() > getH() ? 0 : side.getY());
                side.setY(side.getY() < 0 ? getH() : side.getY());
            }
        }
        for (ChallengeSide side : blueSide.values()) {
            if (isCycle()) {
                side.setX(side.getX() > getW() ? 0 : side.getX());
                side.setX(side.getX() < 0 ? getW() : side.getX());
                side.setY(side.getY() > getH() ? 0 : side.getY());
                side.setY(side.getY() < 0 ? getH() : side.getY());
            } else {
                side.setX(side.getX() > getW() ? 0 : side.getX());
                side.setX(side.getX() < 0 ? getW() : side.getX());
                side.setY(side.getY() > getH() ? 0 : side.getY());
                side.setY(side.getY() < 0 ? getH() : side.getY());
            }
        }
    }

    private void broadcastStepped() {
        ChallengeSteppedBroadcast.INSTANCE.broadcast(this, Group.get(gid), now);
    }
}
