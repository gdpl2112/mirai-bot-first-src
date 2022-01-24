package Project.drawers.entity;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author github-kloping
 */
public class GameMap {
    private int width;
    private int height;

    private Set<MapPosition> positions = new LinkedHashSet<>();

    public Set<MapPosition> getPositions() {
        return positions;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Set<MapPosition> addPosition(MapPosition p) {
        positions.add(p);
        return positions;
    }

    public static class GameMapBuilder {
        private GameMap map = new GameMap();

        public GameMapBuilder setSize(int size) {
            map.width = size;
            map.height = size;
            return this;
        }

        public GameMapBuilder setWidth(int width) {
            map.width = width;
            return this;
        }

        public GameMapBuilder setHeight(int height) {
            map.height = height;
            return this;
        }

        public GameMapBuilder append(MapPosition position) {
            map.positions.add(position);
            return this;
        }

        public GameMapBuilder append(int x, int y, String arg) {
            map.positions.add(new MapPosition().setX(x).setY(y).setArg(arg));
            return this;
        }

        public GameMapBuilder append(int x, int y, Object arg, MapPosition.Type type) {
            map.positions.add(new MapPosition().setX(x).setY(y).setArg(arg).setType(type));
            return this;
        }

        public GameMap build() {
            return map;
        }
    }
}
