package Project.Tools;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.math.BigInteger;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

public class JSONUtils {
    public static String ObjectToJsonString(Object obj) {
        try {
            JSONObject json = new JSONObject();
            Field[] fields = obj.getClass().getDeclaredFields();
            for (Field field : fields) {
                if (field.isAnnotationPresent(Transient.class)) continue;
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

    private static final List<Class<?>> list = new CopyOnWriteArrayList<>();

    static {
        list.add(byte.class);
        list.add(short.class);
        list.add(int.class);
        list.add(long.class);
        list.add(boolean.class);
        list.add(float.class);
        list.add(double.class);
        list.add(char.class);

        list.add(Byte.class);
        list.add(Short.class);
        list.add(Integer.class);
        list.add(Long.class);
        list.add(Boolean.class);
        list.add(Float.class);
        list.add(Double.class);
        list.add(Character.class);

        list.add(Number.class);
        list.add(String.class);
    }

    public static boolean isBasicType(Class<?> cla) {
        return list.contains(cla);
    }

    /**
     * 若想使用此方法,该类必须提供无参构造
     *
     * @param json
     * @param cla
     * @param <T>
     * @return
     */
    public static <T> T JsonStringToObject(String json, Class<T> cla) {
        try {
            JSONObject jsonObject = JSON.parseObject(json);
            T t = cla.newInstance();
            Field[] fields = cla.getDeclaredFields();
            for (Field field : fields) {
                try {
                    String name = field.getName();
                    field.setAccessible(true);
                    Object v = jsonObject.get(name);
                    if (v == null) continue;
                    if (field.getType() == Number.class) {
                        v = new BigInteger(v + "");
                    }
                    if (field.getType() == Long.class) {
                        v = Long.valueOf(v + "");
                    }
                    if (v.getClass() == JSONArray.class && field.getType() == Set.class) {
                        v = JSONArray.parseObject(((JSONArray) v).toJSONString(), Set.class);
                    }
                    field.set(t, v);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return (T) t;
        } catch (Exception e) {
            return null;
        }
    }
}
