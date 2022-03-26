package Project.services.detailServices;

import io.github.kloping.map.MapUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author github.kloping
 */
public class GameBoneDetailService {
    public static final Map<Long, Map<Type, Number>> TEMP_ATTR = new ConcurrentHashMap<>();

    public static void addForAttr(long q, Number v, Type type) {
        Number oldV = 0;
        if (TEMP_ATTR.containsKey(q)) {
            if (TEMP_ATTR.get(q).containsKey(type)) {
                oldV = TEMP_ATTR.get(q).get(type);
            }
        }
        Number nv = v.intValue() + oldV.intValue();
        if (nv.intValue() != 0) {
            MapUtils.append(TEMP_ATTR, q, type, nv);
        } else {
            TEMP_ATTR.get(q).remove(type);
        }
    }

    public static enum Type {
        /**
         * 闪避率
         */
        HIDE_PRO("hideChance"),
        /**
         * 回血效果
         */
        HP_REC_EFF("hpEffect"),
        /**
         * 回血率
         */
        HP_PRO("hpChance"),
        /**
         * 魂力恢复率
         */
        HL_PRO("hlChance"),
        /**
         * 魂力回复效果
         */
        HL_REC_EFF("hlEffect"),
        /**
         * 精神力恢复率
         */
        HJ_PRO("hjChance"),
        /**
         * 精神力回复效果
         */
        HJ_REC_EFF("hjEffect");
        String value;

        Type(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}
