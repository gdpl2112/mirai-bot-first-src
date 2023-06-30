package io.github.kloping.gb;

import io.github.kloping.date.DateUtils;
import io.github.kloping.gb.finals.FinalStrings;
import io.github.kloping.io.ReadUtils;
import io.github.kloping.judge.Judge;
import io.github.kloping.number.NumberUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

/**
 * @author github.kloping
 */
public class Utils {
    public static final Random RANDOM = new Random();

    public static Integer getInteger(String str, String delete, Integer def) {
        String end = str;
        if (Judge.isNotEmpty(delete)) {
            end = str.replace(delete, "");
        }
        try {
            return Integer.valueOf(NumberUtils.findNumberFromString(end));
        } catch (NumberFormatException e) {
            return def;
        }
    }

    public static Integer getInteger(String str, Integer def) {
        return getInteger(str, "", def);
    }

    public static Long getLong(String str, String delete, Long def) {
        String end = str;
        if (Judge.isNotEmpty(delete)) {
            end = str.replace(delete, "");
        }
        try {
            return Long.valueOf(NumberUtils.findNumberFromString(end));
        } catch (NumberFormatException e) {
            return def;
        }
    }

    public static Long getLong(String str, Long def) {
        return getLong(str, "", def);
    }

    public static final long DAY_LONG = 1000L * 60 * 60 * 24;
    public static final SimpleDateFormat SF_0 = new SimpleDateFormat("HH");
    public static final String[] WEEK_DAYS = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
    public static final char[] cnArr = new char[]{'一', '二', '三', '四', '五', '六', '七', '八', '九'};
    public static final SimpleDateFormat df4 = new SimpleDateFormat("yyyy年MM月dd日HH时mm分");
    /**
     * 字符串中存在 反斜杠+u 开头 的Unicode字符。本类用于把那些Unicode字符串转换成汉字
     */
    public static final String singlePattern = "[0-9|a-f|A-F]";
    public static final String pattern = singlePattern + singlePattern + singlePattern + singlePattern;
    public static final SimpleDateFormat dfn = new SimpleDateFormat("/yyyy/MM/dd/HH_mm_ss/");
    public static final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm");
    public static final SimpleDateFormat df2 = new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒");
    public static final SimpleDateFormat DF_MDHM = new SimpleDateFormat("MM月dd日HH时mm分");
    public static final SimpleDateFormat df3 = new SimpleDateFormat("MM月dd日HH时mm分ss秒");
    public static final BigDecimal b2 = new BigDecimal(100000000L);
    public static final BigDecimal be = new BigDecimal(1000000000000L);
    public static final BigDecimal bw = new BigDecimal(10000000000000000L);
    public static final SimpleDateFormat HHmmss = new SimpleDateFormat("HH:mm:ss");
    public static final SimpleDateFormat DDHHmmss = new SimpleDateFormat("dd日HH:mm:ss");
    public static final Map<Character, Integer> CHAR_2_INT = new ConcurrentHashMap<>();
    public static final Map<Integer, Character> INT_2_CHAR = new ConcurrentHashMap<>();
    public static final String[] numeric = new String[]{"零", "一", "二", "三", "四", "五", "六", "七", "八", "九"};
    public static final char[] chArr = new char[]{'十', '百', '千', '万', '亿'};
    public static final String BASE64 = "base64,";
    private String toMon = null;
    private String[] illegalSends = null;

    static {
        int i = 1;
        for (char c : new Character[]{'一', '二', '三', '四', '五', '六', '七', '八', '九', '十'}) {
            INT_2_CHAR.put(i, c);
            CHAR_2_INT.put(c, i);
            i++;
        }
    }

    public static Integer getOldestWeekOne() {
        int r = -1;
        for (int i = 0; i < 7; i++) {
            String s0 = getWeekOfDate(new Date(System.currentTimeMillis() - DAY_LONG * i));
            if (s0.equals(WEEK_DAYS[1])) {
                r = i;
            }
        }
        return r;
    }

    public static byte[] getBase64Data(String base64) {
        int i = base64.indexOf(BASE64);
        String base64Str = base64.substring(i + BASE64.length());
        byte[] bytes = Base64.getDecoder().decode(base64Str);
        return bytes;
    }

    public static String[] print(Process exec) throws IOException {
        try {
            exec.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        InputStream is = exec.getInputStream();
        String s1 = new String(ReadUtils.readAll(is));
        InputStream es = exec.getErrorStream();
        String s2 = new String(ReadUtils.readAll(es));
        return new String[]{s1, s2};
    }

    /**
     * let v in from between to
     *
     * @param v    v
     * @param from max
     * @param to   min
     * @return
     */
    public static int inRandge(int v, int from, int to) {
        if (v < from) {
            return from;
        } else if (v > to) {
            return to;
        }
        return v;
    }

    /**
     * 把 \\u 开头的单字转成汉字，如 \\u6B65 ->　步
     *
     * @param str
     * @return
     */
    public static String ustartToCn(final String str) {
        StringBuilder sb = new StringBuilder().append("0x").append(str.substring(2, 6));
        Integer codeInteger = Integer.decode(sb.toString());
        int code = codeInteger.intValue();
        char c = (char) code;
        return String.valueOf(c);
    }

    /**
     * 字符串是否以Unicode字符开头。约定Unicode字符以 \\u开头。
     *
     * @param str 字符串
     * @return true表示以Unicode字符开头.
     */
    public static boolean isStartWithUnicode(final String str) {
        if (null == str || str.length() == 0) {
            return false;
        }
        if (!str.startsWith("\\u")) {
            return false;
        }
        if (str.length() < 6) {
            return false;
        }
        String content = str.substring(2, 6);

        boolean isMatch = Pattern.matches(pattern, content);
        return isMatch;
    }

    /**
     * 字符串中，所有以 \\u 开头的UNICODE字符串，全部替换成汉字
     *
     * @param strParam
     * @return
     */
    public static String unicodeToCn(final String str) {
        StringBuilder sb = new StringBuilder();
        int length = str.length();
        for (int i = 0; i < length; ) {
            String tmpStr = str.substring(i);
            if (isStartWithUnicode(tmpStr)) { // 分支1
                sb.append(ustartToCn(tmpStr));
                i += 6;
            } else { // 分支2
                sb.append(str.substring(i, i + 1));
                i++;
            }
        }
        return sb.toString();
    }


    /**
     * 获取当前日期是星期几
     *
     * @param dt
     * @return 当前日期是星期几
     */
    public static String getWeekOfDate(Date dt) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0) {
            w = 0;
        }
        return WEEK_DAYS[w];
    }

    public static String getLogTimeFormat() {
        return "./logs" + dfn.format(new Date());
    }

    /**
     * 获取格式时间 yyy年....ss秒
     *
     * @param t 时间戳
     * @return
     */
    public static String getTimeYMdhms(long t) {
        return df2.format(new Date(t));
    }

    public static String getTimeYMdhm(long t) {
        return df4.format(new Date(t));
    }

    /**
     * 获取格式时间 xx月....ss秒
     *
     * @param t 时间戳
     * @return
     */
    public static String getTimeM(long t) {
        return df3.format(new Date(t));
    }

    public static Integer getHour() {
        return Integer.parseInt(SF_0.format(new Date()));
    }

    /**
     * 获取头像地址
     *
     * @param qq
     * @return
     */
    public static String getTouUrl(long qq) {
        return "https://q1.qlogo.cn/g?b=qq&nk=" + qq + "&s=640";
    }

    /**
     * list中 存在元素 startWith 某字符
     *
     * @param list
     * @param withs
     * @return 第几个
     */
    public static int listEveStartWith(List<String> list, String withs) {
        if (withs != null) {
            int i = 0;
            for (String s : list) {
                if (list.get(i).startsWith(withs)) {
                    return i;
                }
                i++;
            }
        }
        return -1;
    }

    /**
     * 某字符 startWith list中 某个元素
     *
     * @param list
     * @param withs
     * @return 第几个
     */
    public static int eveListStartWith(List<String> list, String withs) {
        for (int i = 0; i < list.size(); i++) {
            if (withs.startsWith(list.get(i).replace("*", "")) || withs.equals(list.get(i))) return i;
        }
        return -1;
    }

    /**
     * 过滤大数字
     *
     * @param line
     * @return
     */
    public static String filterBigNum(String line) {
        BigInteger[] numbers = findNums(line, 5);
        for (Number number : numbers) {
            String r = bigNumToWE(number);
            line = line.replace(number.toString(), r);
        }
        return line;
    }

    /**
     * 获取大数字 其长度大于 len
     *
     * @param str
     * @param len
     * @return
     */
    public static BigInteger[] findNums(String str, int len) {
        char[] chars = str.toCharArray();
        boolean k = false;
        List<BigInteger> list = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        for (char c : chars) {
            if (c >= '0' && c <= '9') {
                sb.append(c);
                k = true;
            } else k = false;
            if (!k) {
                String line = sb.toString();
                if (line.isEmpty() || line.length() < len) {
                    sb = new StringBuilder();
                    continue;
                }
                BigInteger number = new BigInteger(line);
                list.add(number);
                sb = new StringBuilder();
            }
        }
        String line = sb.toString();
        if (!line.isEmpty() && line.length() >= len) {
            BigInteger number = new BigInteger(line);
            list.add(number);
        }
        return list.toArray(new BigInteger[0]);
    }

    /**
     * 大数字转为 WE (万亿) 单位
     *
     * @param number
     * @return
     */
    public static String bigNumToWE(Number number) {
        if (number instanceof BigInteger) {
            if (number.toString().length() < 9) {
                return toW(number.longValue());
            } else if (number.toString().length() < 13) {
                BigDecimal b1 = new BigDecimal(number.toString());
                b1.setScale(2, BigDecimal.ROUND_HALF_UP);
                b1 = b1.divide(b2);
                String s = b1.toString();
                s = s.substring(0, s.indexOf(".") + 2);
                return s + "e";
            } else if (number.toString().length() < 17) {
                BigDecimal b1 = new BigDecimal(number.toString());
                b1.setScale(2, BigDecimal.ROUND_HALF_UP);
                b1 = b1.divide(be);
                String s = b1.toString();
                s = s.substring(0, s.indexOf(".") + 3);
                return s + "we";
            } else {
                BigDecimal b1 = new BigDecimal(number.toString());
                b1.setScale(2, BigDecimal.ROUND_HALF_UP);
                b1 = b1.divide(bw);
                String s = b1.toString().toString();
                s = s.substring(0, s.indexOf(".") + 3);
                return s + "kwe";
            }
        } else if (number.longValue() >= 15000) {
            if (number.toString().length() < 9) {
                return toW(number.longValue());
            } else {
                return toE(number);
            }
        }
        return number.toString().toString();
    }

    /**
     * 大数字转为 E (亿) 单位
     *
     * @param number
     * @return
     */
    public static String toE(Number number) {
        BigDecimal b1 = new BigDecimal(number.toString());
        b1.setScale(2, BigDecimal.ROUND_HALF_UP);
        b1 = b1.divide(b2);
        String s = b1.toString().toString();
        s = s.substring(0, s.indexOf(".") + 3);
        return s + "e";
    }

    /**
     * 大数字转为 W (万) 单位
     *
     * @param number
     * @return
     */
    public static String toW(long l) {
        long l1 = 10000;
        Double d3 = (double) l / (double) l1;
        String s = d3.toString();
        if (s.length() >= s.indexOf(".") + 3) s = s.substring(0, s.indexOf(".") + 3);
        return s + "w";
    }

    public static String getTodayDetialString() {
        return DateUtils.getYear() + "-" + DateUtils.getMonth() + "-" + DateUtils.getDay();
    }

    /**
     * 从from到end随机一个数
     *
     * @param from can
     * @param end  can to this
     * @return
     */
    public static long randA(int from, int end) {
        return Long.valueOf(RANDOM.nextInt(end - from) + from);
    }

    /**
     * 从from到end随机一个数
     *
     * @param from can
     * @param end  can't
     * @return
     */
    public static int randB(int from, int end) {
        return RANDOM.nextInt(end - from) + from;
    }

    /**
     * 获取 格式时间 hh时mm分ss秒
     *
     * @param l 时间戳
     * @return
     */
    public static String getTimeHHMM(long l) {
        return HHmmss.format(new Date(l));
    }

    /**
     * 获取提示冷却
     *
     * @param l
     * @return
     */
    public static String getTimeTips(long l) {
        if (l < System.currentTimeMillis()) {
            return FinalStrings.NOT_NEED_WAIT_TIPS;
        }
        long v = l - System.currentTimeMillis();
        if (v >= 60000L) {
            return (v / 60000L) + "分钟";
        } else {
            return (v / 1000) + "秒";
        }
    }

    /**
     * 获取 格式时间 dd日hh时mm分ss秒
     *
     * @param l 时间戳
     * @return
     */
    public static String getTimeDDHHMM(long l) {
        if (l == Long.MAX_VALUE) return "最大";
        return DDHHmmss.format(new Date(l));
    }

    /**
     * 获取at格式
     *
     * @param who
     * @return
     */
    public static String at(Long who) {
        return String.format("<At:%s>", who);
    }

    /**
     * 批处理 Integer.parse
     *
     * @param ss
     * @return
     */
    public static int[] stringsToInts(String... ss) {
        try {
            int[] ints = new int[ss.length];
            for (int i = 0; i < ss.length; i++) {
                ints[i] = Integer.parseInt(ss[i]);
            }
            return ints;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 以,分割后 批处理 Integer.parse
     *
     * @param ss
     * @return
     */
    public static int[] stringToInts(String str) {
        try {
            String[] ss = str.split(",| |，");
            int[] ints = new int[ss.length];
            for (int i = 0; i < ss.length; i++) {
                ints[i] = Integer.parseInt(ss[i]);
            }
            return ints;
        } catch (Exception e) {
            return null;
        }
    }

    public static int[] stringToInts(String str, int xy) {
        try {
            String[] ss = str.split(",| |，");
            int[] ints = new int[ss.length];
            for (int i = 0; i < ss.length; i++) {
                ints[i] = Integer.parseInt(ss[i]) + xy;
            }
            return ints;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 从字符串中获取 中文数字
     *
     * @param str
     * @return
     */
    public static String findNumberZh(String str) {
        List<Character> cs = new ArrayList<>();
        char[] cs1 = str.toCharArray();
        for (char c1 : cs1) {
            if (CHAR_2_INT.containsKey(c1)) {
                cs.add(c1);
                continue;
            }
        }
        StringBuilder sb = new StringBuilder();
        for (char c : cs) {
            sb.append(c);
        }
        return sb.toString();
    }

    /**
     * 阿拉伯转中文
     *
     * @param num
     * @return
     */
    public static String trans(int num) {
        StringBuilder builder = new StringBuilder();
        builder.append(numeric[num / 1000] + "千").append(numeric[num / 100 % 10] + "百").append(numeric[num / 10 % 10] + "十").append(numeric[num % 10]);
        int index = -1;
        while ((index = builder.indexOf(numeric[0], index + 1)) != -1) {
            if (index < builder.length() - 1) {
                builder.deleteCharAt(index + 1);
            }
        }
        index = 0;
        while ((index = builder.indexOf("零零", index)) != -1) {
            builder.deleteCharAt(index);
        }

        if (builder.length() > 1) {
            if (builder.indexOf(numeric[0]) == 0) {
                builder.deleteCharAt(0);
            }
            if (builder.indexOf(numeric[0]) == builder.length() - 1) {
                builder.deleteCharAt(builder.length() - 1);
            }

        }
        if (builder.indexOf("一十") == 0) {
            builder.deleteCharAt(0);
        }
        return builder.toString();
    }

    /**
     * 中文数字转阿拉伯
     *
     * @param chineseNumber
     * @return
     */
    public static int chineseNumber2Int(String chineseNumber) {
        int result = 0;
        int temp = 1;
        int count = 0;
        for (int i = 0; i < chineseNumber.length(); i++) {
            boolean b = true;
            char c = chineseNumber.charAt(i);
            for (int j = 0; j < cnArr.length; j++) {
                if (c == cnArr[j]) {
                    if (0 != count) {
                        result += temp;
                        temp = 1;
                        count = 0;
                    }
                    temp = j + 1;
                    b = false;
                    break;
                }
            }
            if (b) {
                for (int j = 0; j < chArr.length; j++) {
                    if (c == chArr[j]) {
                        switch (j) {
                            case 0:
                                temp *= 10;
                                break;
                            case 1:
                                temp *= 100;
                                break;
                            case 2:
                                temp *= 1000;
                                break;
                            case 3:
                                temp *= 10000;
                                break;
                            case 4:
                                temp *= 100000000;
                                break;
                            default:
                                break;
                        }
                        count++;
                    }
                }
            }
            if (i == chineseNumber.length() - 1) {
                result += temp;
            }
        }
        return result;
    }

    /**
     * 递归删除目录下的所有文件及子目录下所有文件
     *
     * @param dir 将要删除的文件目录
     * @return boolean Returns "true" if all deletions were successful.
     * If a deletion fails, the method stops attempting to
     * delete and returns "false".
     */
    public static synchronized boolean deleteDir(File dir) {
        if (!dir.exists()) return false;
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }

    /**
     * long 的 随机数 大小关系 1>m>e>0
     *
     * @param o 值
     * @param m 从
     * @param e 到
     * @return
     */
    public static long randLong(long o, float m, float e) {
        if (m <= 1 && m < e) {
            float em = e - m;
            if (o < 100000) {
                int r = (int) (Math.random() * (o * (em)));
                return r + (long) (o * m);
            }
            long l1 = (long) (o * m);
            long rl = (long) (o * em);
            rl /= 100000l;
            rl *= (Math.random() * 100000);
            return l1 + rl;
        }
        return -1;
    }

    /**
     * 从字符串数组随机获取一个字符
     *
     * @param sss
     * @return
     */
    public static String getRandString(String... sss) {
        return getRandT(sss);
    }

    public static <T> T getRandT(T... ts) {
        return ts[RANDOM.nextInt(ts.length)];
    }

    public static <T> T getRandT(Collection<T> ts) {
        List<T> list = new LinkedList<>();
        list.addAll(ts);
        return list.get(RANDOM.nextInt(ts.size()));
    }

    /**
     * 将l除以v 保留 d 位小数
     *
     * @param l
     * @param v
     * @param d
     * @return
     */
    public static String device(Long l, double v, int d) {
        double f = l / v;
        return String.format("%." + d + "f", f);
    }
}
