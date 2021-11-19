package Entitys;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class AttributeBone {
    private Integer Hide_pro = 0;
    private Integer Hl_Rec_Eff = 10;
    private Integer Hl_pro = 0;
    private Integer Hp_Rec_Eff = 10;
    private Integer Hp_pro = 0;

    public AttributeBone setHide_pro(Integer Hide_pro) {
        this.Hide_pro = Hide_pro;
        return this;
    }

    public AttributeBone setHl_Rec_Eff(Integer Hl_Rec_Eff) {
        this.Hl_Rec_Eff = Hl_Rec_Eff;
        return this;
    }

    public AttributeBone setHl_pro(Integer Hl_pro) {
        this.Hl_pro = Hl_pro;
        return this;
    }

    public AttributeBone setHp_Rec_Eff(Integer Hp_Rec_Eff) {
        this.Hp_Rec_Eff = Hp_Rec_Eff;
        return this;
    }

    public AttributeBone setHp_pro(Integer Hp_pro) {
        this.Hp_pro = Hp_pro;
        return this;
    }

    public Integer getHide_pro() {
        return Hide_pro;
    }

    public Integer getHl_Rec_Eff() {
        return Hl_Rec_Eff;
    }

    public Integer getHl_pro() {
        return Hl_pro;
    }

    public Integer getHp_Rec_Eff() {
        return Hp_Rec_Eff;
    }

    public Integer getHp_pro() {
        return Hp_pro;
    }

    public AttributeBone addHide_Pro(Integer i) {
        Hide_pro += i;
        return this;
    }

    public AttributeBone addHl_Pro(Integer i) {
        Hl_pro += i;
        return this;
    }

    public AttributeBone addHl_Rec_Eff(Integer i) {
        Hl_Rec_Eff += i;
        return this;
    }

    public AttributeBone addHp_Pro(Integer i) {
        Hp_pro += i;
        return this;
    }

    public AttributeBone addHp_Rec_Eff(Integer i) {
        Hp_Rec_Eff += i;
        return this;
    }

    @Override
    public String toString() {
        return "Hide_pro = " + Hide_pro +
                "\r\nHl_Rec_Eff = " + Hl_Rec_Eff +
                "\r\nHl_pro = " + Hl_pro +
                "\r\nHp_Rec_Eff = " + Hp_Rec_Eff +
                "\r\nHp_pro = " + Hp_pro;
    }

    public static <T extends Object> T ParseObj(T obj, String line) {
        try {
            String[] lines = null;
            lines = line.split(line.contains("\r") ? "\r\n" : "\n");
            for (String s1 : lines) {
                String[] kv = s1.split("=");
                String k = kv[0].trim();
                String v = kv[1].trim();
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
