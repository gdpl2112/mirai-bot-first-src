package Project.Tools;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author github-kloping
 */
public class JSONUtils {
    public static String objectToJsonString(Object obj) {
        try {
            JSONObject json = new JSONObject();
            Field[] fields = obj.getClass().getDeclaredFields();
            for (Field field : fields) {
                if (field.isAnnotationPresent(Transient.class)) {
                    continue;
                }
                String name = field.getName();
                field.setAccessible(true);
                json.put(name, field.get(obj));
            }
            return json.toJSONString();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return "";
        }
    }

    @Target(ElementType.FIELD)
    @java.lang.annotation.Retention(RetentionPolicy.RUNTIME)
    public @interface Transient {
    }

    private static final List<Class<?>> LIST = new CopyOnWriteArrayList<>();

    static {
        LIST.add(byte.class);
        LIST.add(short.class);
        LIST.add(int.class);
        LIST.add(long.class);
        LIST.add(boolean.class);
        LIST.add(float.class);
        LIST.add(double.class);
        LIST.add(char.class);

        LIST.add(Byte.class);
        LIST.add(Short.class);
        LIST.add(Integer.class);
        LIST.add(Long.class);
        LIST.add(Boolean.class);
        LIST.add(Float.class);
        LIST.add(Double.class);
        LIST.add(Character.class);

        LIST.add(Number.class);
        LIST.add(String.class);
    }

    public static boolean isBasicType(Class<?> cla) {
        return LIST.contains(cla);
    }

    /**
     * 若想使用此方法,该类必须提供无参构造
     *
     * @param json
     * @param cla
     * @param <T>
     * @return
     */
    public static <T> T jsonStringToObject(String json, Class<T> cla) {
        return JSON.toJavaObject(JSON.parseObject(json), cla);
//        try {
//            JSONObject jsonObject = JSON.parseObject(json);
//            T t = cla.newInstance();
//            Field[] fields = cla.getDeclaredFields();
//            for (Field field : fields) {
//                try {
//                    String name = field.getName();
//                    field.setAccessible(true);
//                    Object v = jsonObject.get(name);
//                    if (v == null) continue;
//                    if (field.getType() == Number.class) {
//                        v = new BigInteger(v.toString());
//                    }
//                    if (field.getType() == Long.class) {
//                        v = Long.valueOf(v.toString());
//                    }
//                    if (v.getClass() == JSONArray.class && field.getType() == Set.class) {
//                        v = JSONArray.parseObject(((JSONArray) v).toJSONString(), Set.class);
//                    }
//                    field.set(t, v);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//            return (T) t;
//        } catch (Exception e) {
//            return null;
//        }
    }
}
