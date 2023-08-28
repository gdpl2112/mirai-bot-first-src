package Project.skills;

import Project.utils.Tools.Tool;
import io.github.kloping.MySpringTool.StarterApplication;
import io.github.kloping.MySpringTool.annotations.Entity;
import io.github.kloping.MySpringTool.interfaces.component.PackageScanner;
import io.github.kloping.object.ObjectUtils;

import java.io.IOException;
import java.util.*;

import static Project.commons.rt.CommonSource.toStr;

/**
 * @author github.kloping
 */
@Entity
public class SkillFactory {
    private static final Map<Integer, Class<SkillTemplate>> CLASS_MAP = new HashMap<>();
    private static final Map<Integer, Class<SkillTemplate>> CLASS_MAP2 = new HashMap<>();
    private static final Map<Integer, Class<SkillTemplate>> CLASS_MAP100 = new HashMap<>();
    private static final Map<Integer, SkillTemplate> SKILL_MAP = new HashMap<>();
    public static int normalSkillNum = 0;
    public static int ghostSkillNum = 0;

    static {
        PackageScanner scanner = StarterApplication.Setting.INSTANCE.getPackageScanner();
        try {
            for (Class<?> aClass : scanner.scan(SkillFactory.class, SkillFactory.class.getClassLoader(),
                    SkillFactory.class.getPackage().getName())) {
                if (aClass == SkillTemplate.class) continue;
                if (ObjectUtils.isSuperOrInterface(aClass, SkillTemplate.class)) {
                    Class<SkillTemplate> c0 = (Class<SkillTemplate>) aClass;
                    String nStr = aClass.getSimpleName();
                    Integer jid = Integer.valueOf(Tool.INSTANCE.findNumberFromString(nStr));
                    if (jid >= 8000) {
                        CLASS_MAP2.put(jid, c0);
                    } else if (jid > 1500) {
                        CLASS_MAP.put(jid, c0);
                    } else if (jid > 1000) {
                        CLASS_MAP100.put(jid, c0);
                        ghostSkillNum++;
                    } else {
                        CLASS_MAP.put(jid, c0);
                        if (jid > 700) {

                        } else {
                            normalSkillNum++;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static SkillTemplate factory(int jid) {
        if (jid >= 8000) {
            return factory8(jid);
        }
        if (SKILL_MAP.containsKey(jid)) return SKILL_MAP.get(jid);
        try {
            SkillTemplate st = CLASS_MAP.get(jid).newInstance();
            SKILL_MAP.put(jid, st);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return SKILL_MAP.get(jid);
    }

    public static Integer factory8id(int wh) {
        List<Integer> list = new ArrayList<>();
        String wh0 = 8 + toStr(2, wh);
        CLASS_MAP2.forEach((k, v) -> {
            if (k.toString().startsWith(wh0)) {
                list.add(k);
            }
        });
        Integer jid = Tool.INSTANCE.getRandT(list);
        return jid;
    }

    public static SkillTemplate factory8(int jid) {
        if (SKILL_MAP.containsKey(jid)) return SKILL_MAP.get(jid);
        try {
            SkillTemplate st = CLASS_MAP2.get(jid).newInstance();
            SKILL_MAP.put(jid, st);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return SKILL_MAP.get(jid);
    }

    public static SkillTemplate factory100(int jid, int id) {
        if (SKILL_MAP.containsKey(jid)) return SKILL_MAP.get(jid);
        try {
            SkillTemplate st = CLASS_MAP100.get(jid).newInstance();
            st.setId(id);
            SKILL_MAP.put(jid, st);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return SKILL_MAP.get(jid);
    }

    public static Integer[] skillListIds() {
        Set<Integer> set = new HashSet<>();
        set.addAll(CLASS_MAP2.keySet());
        set.addAll(CLASS_MAP.keySet());
        return set.toArray(new Integer[0]);
    }
}
