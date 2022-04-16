package Project.services.detailServices.attribute;

import io.github.kloping.map.MapUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author github.kloping
 */
public enum BaseAttribute {
    /**
     * 火,克制,木,水,冰
     */
    FIRE,
    /**
     * 木,克制土
     */
    WOOD,
    /**
     * 电
     */
    LIGHTING,
    /**
     * 默认
     */
    NORMAL,
    /**
     * 铁
     */
    IRON,
    /**
     * 水克制铁
     */
    WATER,
    /**
     * 冰克制水
     */
    ICE,
    /**
     * 土克制火
     */
    SOIL;

    public static final Map<BaseAttribute, List<BaseAttribute>> BASE_ATTRIBUTE_LIST_MAP = new HashMap<>();

    static {
        MapUtils.append(BASE_ATTRIBUTE_LIST_MAP, FIRE, WOOD);
        MapUtils.append(BASE_ATTRIBUTE_LIST_MAP, FIRE, WATER);
        MapUtils.append(BASE_ATTRIBUTE_LIST_MAP, FIRE, ICE);
        MapUtils.append(BASE_ATTRIBUTE_LIST_MAP, WOOD, SOIL);
        MapUtils.append(BASE_ATTRIBUTE_LIST_MAP, WATER, FIRE);
        MapUtils.append(BASE_ATTRIBUTE_LIST_MAP, WATER, IRON);
        MapUtils.append(BASE_ATTRIBUTE_LIST_MAP, IRON, WATER);
        MapUtils.append(BASE_ATTRIBUTE_LIST_MAP, SOIL, FIRE);
    }

}
