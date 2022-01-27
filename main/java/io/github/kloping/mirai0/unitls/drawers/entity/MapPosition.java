package io.github.kloping.mirai0.unitls.drawers.entity;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author github-kloping
 */
@Accessors(chain = true)
@Data
public class MapPosition {
    private int x;
    private int y;
    private Object arg;
    private Type type = Type.p;

    public static enum Type {
        p, warn, err
    }
}
