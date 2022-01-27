package io.github.kloping.mirai0.Entitys.gameEntitys;

import Project.DataBases.GameDataBase;
import io.github.kloping.map.MapUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author github-kloping
 */
public class AttributeBone {
    private Integer hide_pro = 0;
    private Integer hl_rec_eff = 10;
    private Integer hl_pro = 0;
    private Integer hp_rec_eff = 10;
    private Integer hp_pro = 0;
    private long q = -1;

    public Integer getHide_pro() {
        return hide_pro;
    }

    public void setHide_pro(Integer hide_pro) {
        this.hide_pro = hide_pro;
    }

    public Integer getHl_rec_eff() {
        return hl_rec_eff;
    }

    public void setHl_rec_eff(Integer hl_rec_eff) {
        this.hl_rec_eff = hl_rec_eff;
    }

    public Integer getHl_pro() {
        return hl_pro;
    }

    public void setHl_pro(Integer hl_pro) {
        this.hl_pro = hl_pro;
    }

    public Integer getHp_rec_eff() {
        return hp_rec_eff;
    }

    public void setHp_rec_eff(Integer hp_rec_eff) {
        this.hp_rec_eff = hp_rec_eff;
    }

    public Integer getHp_pro() {
        return hp_pro;
    }

    public void setHp_pro(Integer hp_pro) {
        this.hp_pro = hp_pro;
    }

    public long getQ() {
        return q;
    }

    public void setQ(long q) {
        this.q = q;
    }

    public Integer getHidePro() {
        if (TEMP_ATTR.containsKey(q)) {
            if (TEMP_ATTR.get(q).containsKey(Type.HIDE_PRO)) {
                return hide_pro + TEMP_ATTR.get(q).get(Type.HIDE_PRO).intValue();
            }
        }
        return hide_pro;
    }

    public Integer getHlRecEff() {
        if (TEMP_ATTR.containsKey(q)) {
            if (TEMP_ATTR.get(q).containsKey(Type.HL_REC_EFF)) {
                return hl_rec_eff + TEMP_ATTR.get(q).get(Type.HL_REC_EFF).intValue();
            }
        }
        return hl_rec_eff;
    }

    public Integer getHlPro() {
        if (TEMP_ATTR.containsKey(q)) {
            if (TEMP_ATTR.get(q).containsKey(Type.HL_PRO)) {
                return hl_pro + TEMP_ATTR.get(q).get(Type.HL_PRO).intValue();
            }
        }
        return hl_pro;
    }

    public Integer getHpRecEff() {
        if (TEMP_ATTR.containsKey(q)) {
            if (TEMP_ATTR.get(q).containsKey(Type.HP_REC_EFF)) {
                return hp_rec_eff + TEMP_ATTR.get(q).get(Type.HP_REC_EFF).intValue();
            }
        }
        return hp_rec_eff;
    }

    public Integer getHpPro() {
        if (TEMP_ATTR.containsKey(q)) {
            if (TEMP_ATTR.get(q).containsKey(Type.HP_PRO)) {
                return hp_pro + TEMP_ATTR.get(q).get(Type.HP_PRO).intValue();
            }
        }
        return hp_pro;
    }

    public AttributeBone addHidePro(Integer i) {
        hide_pro += i;
        return this;
    }

    public AttributeBone addHlPro(Integer i) {
        hl_pro += i;
        return this;
    }

    public AttributeBone addHlRecEff(Integer i) {
        hl_rec_eff += i;
        return this;
    }

    public AttributeBone addHpPro(Integer i) {
        hp_pro += i;
        return this;
    }

    public AttributeBone addHpRecEff(Integer i) {
        hp_rec_eff += i;
        return this;
    }

    public static enum Type {

        HIDE_PRO,
        HP_REC_EFF,
        HP_PRO,
        HL_REC_EFF,
        HL_PRO,
    }

    private static final Map<Long, Map<Type, Number>> TEMP_ATTR = new ConcurrentHashMap<>();

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

    public static AttributeBone getInstance(long q) {
        AttributeBone attributeBone = new AttributeBone();
        attributeBone.q = q;
        return AttributeBone.ParseObj(attributeBone, GameDataBase.getStringFromData(q, "AttributeBone"));
    }

    public void apply() {
        GameDataBase.putStringFromData(q, "AttributeBone", this.toString());
    }

    @Override
    public String toString() {
        return "Hide_pro = " + hide_pro +
                "\r\nHl_Rec_Eff = " + hl_rec_eff +
                "\r\nHl_pro = " + hl_pro +
                "\r\nHp_Rec_Eff = " + hp_rec_eff +
                "\r\nHp_pro = " + hp_pro;
    }

    public static <T extends Object> T ParseObj(T obj, String line) {
        try {
            String[] lines = null;
            lines = line.split(line.contains("\r") ? "\r\n" : "\n");
            for (String s1 : lines) {
                String[] kv = s1.split("=");
                String k = kv[0].trim().toLowerCase();
                String v = kv[1].trim().toLowerCase();
                Field field = obj.getClass().getDeclaredField(k);
                Class cls = field.getType();
                Method method = cls.getMethod("valueOf", String.class);
                field.set(obj, method.invoke(null, v));
            }
            return obj;
        } catch (Exception e) {
            throw new RuntimeException("");
        }
    }
}
