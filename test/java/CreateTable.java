import io.github.kloping.clasz.ClassUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lingsf on 2019/11/5.
 * modify by kloping on 2022/2/24
 */
public class CreateTable {
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

    public static final Class c = Double.class;

    public static void main(String[] args) throws IOException {
        createTable(c);
    }

    private static void createTable(Class<?> cla) throws IOException {
        String sName = cla.getSimpleName();
        sName = filterName(sName);
        createTable(cla, sName);
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

    public static String createTable(Class<?> clz, String tableName) throws IOException {
        StringBuilder sb0 = new StringBuilder();
        Field[] fields = null;
        fields = clz.getDeclaredFields();
        String param = null;
        String column = null;
        StringBuilder sb = null;
        sb = new StringBuilder(50);
        sb.append("CREATE TABLE IF NOT EXISTS ").append(tableName).append(" ( \r\n");
        boolean firstId = true;
        File file = null;
        for (Field f : fields) {
            if (ClassUtils.isStatic(f)) continue;
            column = f.getName();
            column = filterName(column);
            param = f.getType().getSimpleName();
            sb.append("`");
            sb.append(column);
            String typeName = javaProperty2SqlColumnMap.get(param);
            if (typeName == null) {
                if (f.getType().isEnum()) {
                    typeName = javaProperty2SqlColumnMap.get("String");
                }
            }
            sb.append("`").append(typeName).append(" NOT NULL").append(",\n ");

            sb0.append("\n@TableField(\"`").append(column).append("`\")\n");
            sb0.append("private ").append(f.getType().getSimpleName()).append(" ").append(f.getName()).append(";\n");
        }
        String sql = null;
        sql = sb.toString();
        int lastIndex = sql.lastIndexOf(",");
        sql = sql.substring(0, lastIndex) + sql.substring(lastIndex + 1);
        sql = sql.substring(0, sql.length() - 1) + " );\r\n";
        System.out.println("sql :");
        System.out.println(sql);
        System.out.println(sb0.toString());
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
