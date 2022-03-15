package Project.skill;

import io.github.kloping.MySpringTool.StarterApplication;
import io.github.kloping.MySpringTool.interfaces.component.PackageScanner;
import io.github.kloping.mirai0.unitls.Tools.Tool;
import io.github.kloping.object.ObjectUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author github.kloping
 */
public class SkillFactory {
    private static final Map<Integer, Class<SkillTemplate>> CLASS_MAP = new HashMap<>();
    public static int normalSkillNum = 0;

    static {
        PackageScanner scanner = StarterApplication.Setting.INSTANCE.getPackageScanner();
        try {
            for (Class<?> aClass : scanner.scan(SkillFactory.class.getPackage().getName())) {
                if (aClass == SkillTemplate.class) continue;
                if (ObjectUtils.isSuperOrInterface(aClass, SkillTemplate.class)) {
                    String nStr = aClass.getSimpleName();
                    Integer jid = Integer.valueOf(Tool.findNumberFromString(nStr));
                    if (jid < 70) {
                        normalSkillNum++;
                    }
                    CLASS_MAP.put(jid, (Class<SkillTemplate>) aClass);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static final Map<Integer, SkillTemplate> SKILL_MAP = new HashMap<>();

    public static SkillTemplate factory(int jid) {
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
}
