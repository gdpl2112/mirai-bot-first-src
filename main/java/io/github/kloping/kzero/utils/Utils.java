package io.github.kloping.kzero.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.github.kloping.clasz.ClassUtils;
import io.github.kloping.object.ObjectUtils;
import org.springframework.web.util.UriUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author github.kloping
 */
public class Utils {
    private static final String DEFAULT_CHARSET = "UTF-8";

    public static String getAtFormat(String msg) {
        return getFormat(msg, "at");
    }

    public static String getFormat(String msg, String format) {
        return getFormatAndDefault(msg, format, null);
    }

    public static String getFormatAndDefault(String msg, String format, String def) {
        String format0 = "<" + format + ":";
        int i0 = msg.indexOf(format0);
        int ie = msg.indexOf(">");
        if (i0 == -1 || ie == -1) return def;
        return msg.substring(i0 + format0.length(), ie);
    }

    /**
     * 获取提示冷却
     *
     * @param l
     * @return
     */
    public static String getTimeTips(long l) {
        if (l < System.currentTimeMillis()) return "0秒";
        long v = l - System.currentTimeMillis();
        if (v >= 60000L) {
            return (v / 60000L) + "分钟";
        } else {
            return (v / 1000) + "秒";
        }
    }

    /**
     * 将指定数字转为指定位数字符
     * (2,9)  => 09
     *
     * @param i
     * @param value
     * @return
     */
    public static String toStr(int i, int value) {
        String s0 = Integer.toString(value);
        while (s0.length() < i) {
            s0 = "0" + s0;
        }
        return s0;
    }
    /**
     * 发起Get请求
     *
     * @param urlStr
     * @return
     */
    public final static byte[] doGetRequestForFile(String urlStr) {
        InputStream is = null;
        ByteArrayOutputStream os = null;
        byte[] buff = new byte[1024];
        int len = 0;
        try {
            URL url = new URL(UriUtils.encodePath(urlStr, DEFAULT_CHARSET));
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Content-Type", "plain/text;charset=" + DEFAULT_CHARSET);
            conn.setRequestProperty("charset", DEFAULT_CHARSET);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestMethod("GET");
            conn.setReadTimeout(15000);
            conn.connect();
            is = conn.getInputStream();
            os = new ByteArrayOutputStream();
            while ((len = is.read(buff)) != -1) {
                os.write(buff, 0, len);
            }
            return os.toByteArray();
        } catch (IOException e) {
            System.err.println("发起请求出现异常:" + e);
            return null;
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    System.err.println("【关闭流异常】");
                }
            }
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    System.err.println("【关闭流异常】");
                }
            }
        }
    }

    public static boolean isAllNumber(String sid) {
        for (char c : sid.toCharArray()) {
            if (c < '0' || c > '9')
                return false;
        }
        return true;
    }

    /**
     * Created by lingsf on 2019/11/5.
     * modify by kloping on 2022/2/24
     */
    public static class CreateTable {
        public static Map<String, String> javaProperty2SqlColumnMap = new HashMap<>();

        static {
            javaProperty2SqlColumnMap.put("Integer", "INT(9)");
            javaProperty2SqlColumnMap.put("Short", "INT(4)");
            javaProperty2SqlColumnMap.put("Long", "BIGINT(18)");
            javaProperty2SqlColumnMap.put("long", "BIGINT(18)");
            javaProperty2SqlColumnMap.put("Number", "BIGINT(18)");
            javaProperty2SqlColumnMap.put("Float", "INT(22,2)");
            javaProperty2SqlColumnMap.put("Boolean", "tinyint");
            javaProperty2SqlColumnMap.put("boolean", "tinyint");
            javaProperty2SqlColumnMap.put("Timestamp", "date");
            javaProperty2SqlColumnMap.put("String", "VARCHAR(255)");
        }

        public static String createTable(Class<?> cla) {
            String sName = cla.getSimpleName();
            sName = filterName(sName);
            return createTable(cla, sName);
        }

        public static String filterName(String sName) {
            String firstS = sName.substring(0, 1).toLowerCase();
            sName = firstS + sName.substring(1);
            Matcher matcher = p0.matcher(sName);
            while (matcher.find()) {
                String g0 = matcher.group();
                String g1 = toLow(g0);
                sName = sName.replace(g0, g1);
            }
            return sName;
        }

        public static final Pattern p0 = Pattern.compile("[A-Z]");

        public static String createTable(Class<?> clz, String tableName) {
            Field[] fields = null;
            fields = clz.getDeclaredFields();
            String paramTypeName = null;
            String column = null;
            StringBuilder sb = null;
            sb = new StringBuilder(50);
            sb.append("CREATE TABLE IF NOT EXISTS ").append(tableName).append(" ( \r\n");
            boolean firstId = true;
            File file = null;
            for (Field f : fields) {
                if (ClassUtils.isStatic(f)) continue;
                if (f.isAnnotationPresent(TableField.class)) {
                    TableField field = f.getAnnotation(TableField.class);
                    if (!field.exist()) continue;
                }
                column = f.getName();
                String columnName = filterName(column);
                paramTypeName = f.getType().getSimpleName();
                sb.append("`").append(columnName).append("`");
                String typeName = javaProperty2SqlColumnMap.get(paramTypeName);
                if (f.getType().isEnum()) {
                    typeName = javaProperty2SqlColumnMap.get("String");
                } else {
                    paramTypeName = ObjectUtils.baseToPack(f.getType()).getSimpleName();
                    typeName = javaProperty2SqlColumnMap.get(paramTypeName);
                }
                if (f.isAnnotationPresent(TableId.class)) {
                    if ("VARCHAR(255)".equals(typeName)) {
                        typeName = "VARCHAR(190)";
                    }
                }
                sb.append(typeName).append(" NOT NULL");
                if (f.isAnnotationPresent(TableId.class)) {
                    TableId tableId = f.getDeclaredAnnotation(TableId.class);
                    sb.append(" primary key");
                    if (tableId.type() == IdType.AUTO) {
                        sb.append(" auto_increment");
                    }
                }
                sb.append(",\n ");
            }
            String sql = null;
            sql = sb.toString();
            int lastIndex = sql.lastIndexOf(",");
            sql = sql.substring(0, lastIndex) + sql.substring(lastIndex + 1);
            sql = sql.substring(0, sql.length() - 1) + " );\r\n";
            return sql;
        }

        public static String toLow(String s1) {
            switch (s1.charAt(0)) {
                case 'A':
                    return "_a";
                case 'B':
                    return "_b";
                case 'C':
                    return "_c";
                case 'D':
                    return "_d";
                case 'E':
                    return "_e";
                case 'F':
                    return "_f";
                case 'G':
                    return "_g";
                case 'H':
                    return "_h";
                case 'I':
                    return "_i";
                case 'J':
                    return "_j";
                case 'K':
                    return "_k";
                case 'L':
                    return "_l";
                case 'M':
                    return "_m";
                case 'N':
                    return "_n";
                case 'O':
                    return "_o";
                case 'P':
                    return "_p";
                case 'Q':
                    return "_q";
                case 'R':
                    return "_r";
                case 'S':
                    return "_s";
                case 'T':
                    return "_t";
                case 'U':
                    return "_u";
                case 'V':
                    return "_v";
                case 'W':
                    return "_w";
                case 'X':
                    return "_x";
                case 'Y':
                    return "_y";
                case 'Z':
                    return "_z";
                default:
                    return s1;
            }
        }
    }


    /**
     * get from json
     *
     * @param t1 json
     * @param t0 表达式
     * @return
     * @throws Exception
     */
    public static <T> T gt(String t1, String t0, Class<T> cla) {
        JSON j0 = (JSON) JSON.parse(t1);
        t0 = t0.trim();
        String s0 = null;
        for (String s : t0.trim().split("\\.")) {
            if (!s.isEmpty()) {
                s0 = s;
                break;
            }
        }
        Object o = null;
        if (s0.matches("\\[\\d*]")) {
            JSONArray arr = (JSONArray) j0;
            String sts = s0.substring(1, s0.length() - 1);
            if (sts.isEmpty()) {
                o = arr;
                t0 = t0.replaceFirst("\\[]", "");
            } else {
                Integer st = Integer.parseInt(sts);
                o = arr.get(st);
                int len = 4;
                if (t0.length() >= len) t0 = t0.substring(len);
                else t0 = t0.substring(len - 1);
            }
        } else if (s0.matches(".*?\\[\\d+]")) {
            int i = s0.indexOf("[");
            int i1 = s0.indexOf("]");
            String st0 = s0.substring(0, i);
            Integer st = Integer.parseInt(s0.substring(i + 1, s0.length() - 1));
            JSONObject jo = (JSONObject) j0;
            o = jo.getJSONArray(st0).get(st);
            if (t0.length() > s0.length()) t0 = t0.substring(s0.length());
            else t0 = null;
        } else {
            JSONObject jo = (JSONObject) j0;
            o = jo.get(s0);
            int len = s0.length() + 1;
            if (t0.length() >= len) t0 = t0.substring(len);
            else t0 = t0.substring(len - 1);
        }
        if (t0 != null && t0.length() > 0) {
            return (T) gt(JSON.toJSONString(o), t0, cla);
        } else {
            return (T) o;
        }
    }

    public static class Gt {
        private String json;

        public Gt(String json) {
            this.json = json;
        }

        public String gt(String p) {
            return Utils.gt(json, p, Object.class).toString();
        }

        public <T> T gt(String p, Class<T> t) {
            return Utils.gt(json, p, t);
        }
    }
}
