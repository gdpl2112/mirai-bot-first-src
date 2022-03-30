package Project.skill;

import io.github.kloping.MySpringTool.StarterApplication;
import io.github.kloping.MySpringTool.interfaces.component.PackageScanner;
import io.github.kloping.mirai0.unitls.Tools.Tool;
import io.github.kloping.object.ObjectUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.github.kloping.mirai0.Main.Resource.THREADS;
import static io.github.kloping.mirai0.commons.resouce_and_tool.CommonSource.toStr;
import static io.github.kloping.mirai0.unitls.Tools.Tool.getRandT;

/**
 * @author github.kloping
 */
public class SkillFactory {
    private static final Map<Integer, Class<SkillTemplate>> CLASS_MAP = new HashMap<>();
    private static final Map<Integer, Class<SkillTemplate>> CLASS_MAP2 = new HashMap<>();
    private static final Map<Integer, SkillTemplate> SKILL_MAP = new HashMap<>();
    public static int normalSkillNum = 0;

    static {
        THREADS.submit(() -> {
            PackageScanner scanner = StarterApplication.Setting.INSTANCE.getPackageScanner();
            try {
                for (Class<?> aClass : scanner.scan(SkillFactory.class.getPackage().getName())) {
                    if (aClass == SkillTemplate.class) continue;
                    if (ObjectUtils.isSuperOrInterface(aClass, SkillTemplate.class)) {
                        Class<SkillTemplate> c0 = (Class<SkillTemplate>) aClass;

                        String nStr = aClass.getSimpleName();
                        Integer jid = Integer.valueOf(Tool.findNumberFromString(nStr));
                        if (jid < 70) {
                            normalSkillNum++;
                        }
                        if (jid >= 800) {
                            CLASS_MAP2.put(jid, c0);
                        } else {
                            CLASS_MAP.put(jid, c0);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });
    }

    public static SkillTemplate factory(int jid) {
        if (jid >= 800) {
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
        Integer jid = getRandT(list);
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
}
