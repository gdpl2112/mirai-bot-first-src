package io.github.kloping.mirai0.unitls.Tools;


import io.github.kloping.io.ReadUtils;

import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

import static io.github.kloping.date.DateUtils.*;
import static io.github.kloping.mirai0.Main.Resource.contextManager;
import static io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet.FinalString.NOT_NEED_WAIT_TIPS;

public class Tool {
    public static Tool tool = new Tool();
    public final String[] WEEK_DAYS = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
    public final Random RANDOM = new SecureRandom();
    public final char[] cnArr = new char[]{'一', '二', '三', '四', '五', '六', '七', '八', '九'};
    /**
     * 字符串中存在 反斜杠+u 开头 的Unicode字符。本类用于把那些Unicode字符串转换成汉字
     */
    private final String singlePattern = "[0-9|a-f|A-F]";
    private final String pattern = singlePattern + singlePattern +
            singlePattern + singlePattern;
    private final SimpleDateFormat dfn = new SimpleDateFormat("/yyyy/MM/dd/HH_mm_ss/");
    private final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm");
    private final SimpleDateFormat df2 = new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒");
    public final SimpleDateFormat df4 = new SimpleDateFormat("yyyy年MM月dd日HH时mm分");
    private final SimpleDateFormat df3 = new SimpleDateFormat("MM月dd日HH时mm分ss秒");
    private final BigDecimal b2 = new BigDecimal(100000000L);
    private final BigDecimal be = new BigDecimal(1000000000000L);
    private final BigDecimal bw = new BigDecimal(10000000000000000L);
    private final SimpleDateFormat HHmmss = new SimpleDateFormat("HH:mm:ss");
    private final SimpleDateFormat DDHHmmss = new SimpleDateFormat("dd日HH:mm:ss");
    private final Map<Character, Integer> char2int = new ConcurrentHashMap<>();
    private final Map<Integer, Character> int2char = new ConcurrentHashMap<>();
    private final String[] numeric = new String[]{"零", "一", "二", "三", "四", "五", "六", "七", "八", "九"};
    private final char[] chArr = new char[]{'十', '百', '千', '万', '亿'};
    private final String BASE64 = "base64,";
    private String today = null;
    private String toMon = null;
    private String[] illegalSends = null;

    {
        int i = 1;
        for (char c : new Character[]{'一', '二', '三', '四', '五', '六', '七', '八', '九', '十'}) {
            int2char.put(i, c);
            char2int.put(c, i);
            i++;
        }
    }

    public byte[] getBase64Data(String base64) {
        int i = base64.indexOf(BASE64);
        String base64Str = base64.substring(i + BASE64.length());
        byte[] bytes = Base64.getDecoder().decode(base64Str);
        return bytes;
    }

    public String[] print(Process exec) throws IOException {
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
    public int inRandge(int v, int from, int to) {
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
    private String ustartToCn(final String str) {
        StringBuilder sb = new StringBuilder().append("0x")
                .append(str.substring(2, 6));
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
    private boolean isStartWithUnicode(final String str) {
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
    public String unicodeToCn(final String str) {
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
     * get int
     *
     * @param str
     * @return
     */
    public Integer getInteagerFromStr(String str) {
        String s1 = findNumberFromString(str);
        if (s1 == null || s1.trim().isEmpty()) {
            return null;
        } else {
            return Integer.parseInt(s1);
        }
    }

    public void setOnErrInFIle(String path) {
        try {
            PrintStream oldPrintStream = System.err;
            new File(path).getParentFile().mkdirs();
            new File(path).createNewFile();
            FileOutputStream bos = new FileOutputStream(path, true);
            PrintStream printStream = new PrintStream(bos) {
                @Override
                public void write(int b) {
                    super.write(b);
                    oldPrintStream.write((int) b);
                }

                @Override
                public void write(byte[] buf, int off, int len) {
                    super.write(buf, off, len);
                    oldPrintStream.write(buf, off, len);
                }
            };
            System.setErr(printStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setOnOutInFIle(String path) {
        try {
            PrintStream oldPrintStream = System.out;
            new File(path).getParentFile().mkdirs();
            new File(path).createNewFile();
            FileOutputStream bos = new FileOutputStream(path, true);
            PrintStream printStream = new PrintStream(bos) {
                @Override
                public void write(int b) {
                    super.write(b);
                    oldPrintStream.write((int) b);
                }

                @Override
                public void write(byte[] buf, int off, int len) {
                    super.write(buf, off, len);
                    oldPrintStream.write(buf, off, len);
                }
            };
            System.setOut(printStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取当前日期是星期几
     *
     * @param dt
     * @return 当前日期是星期几
     */
    public String getWeekOfDate(Date dt) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0) {
            w = 0;
        }
        return WEEK_DAYS[w];
    }

    public String getLogTimeFormat() {
        return "./logs" + dfn.format(new Date());
    }

    /**
     * 获取格式时间 yyy年....ss秒
     *
     * @param t 时间戳
     * @return
     */
    public String getTimeYMdhms(long t) {
        return df2.format(new Date(t));
    }

    public String getTimeYMdhm(long t) {
        return df4.format(new Date(t));
    }

    /**
     * 获取格式时间 xx月....ss秒
     *
     * @param t 时间戳
     * @return
     */
    public String getTimeM(long t) {
        return df3.format(new Date(t));
    }

    /**
     * 获取从现在到某刻的时间
     *
     * @param day  天
     * @param hour 小时
     * @param mil  分钟
     * @return long
     */
    public long getTimeFromNowTo(int day, int hour, int mil) {
        Date date = null;
        try {
            date = df.parse("2021-" + getToMon() + "-" + day + "-" + hour + "-" + mil);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long millis = date.getTime();
        long now = System.currentTimeMillis();
        return millis - now;
    }

    /**
     * 获取 Entry<K,V>
     *
     * @param _k
     * @param _v
     * @param <K>
     * @param <V>
     * @return
     */
    public <K, V> Map.Entry<K, V> getEntry(K _k, V _v) {
        Map.Entry<K, V> entry = new Map.Entry<K, V>() {
            private K k = _k;
            private V v = _v;

            @Override
            public K getKey() {
                return k;
            }

            @Override
            public V getValue() {
                return v;
            }

            @Override
            public V setValue(V value) {
                V v1 = v;
                this.v = value;
                return v1;
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
                if (o instanceof Map.Entry) {
                    Entry<K, V> e1 = (Entry<K, V>) o;
                    return k.equals(e1.getKey()) && v.equals(e1.getValue());
                } else {
                    return false;
                }
            }
        };
        return entry;
    }

    /**
     * 获取图片格式头像
     *
     * @param qq
     * @return
     */
    public String getTou(Long qq) {
        return pathToImg("http://q2.qlogo.cn/headimg_dl?dst_uin=" + qq + "&spec=100");
    }

    /**
     * 获取头像地址
     *
     * @param qq
     * @return
     */
    public String getTouUrl(long qq) {
        return "https://q1.qlogo.cn/g?b=qq&nk=" + qq + "&s=640";
    }

    /**
     * list中 存在元素 startWith 某字符
     *
     * @param list
     * @param withs
     * @return 第几个
     */
    public int listEveStartWith(List<String> list, String withs) {
        if (withs != null)
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).startsWith(withs))
                    return i;
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
    public int EveListStartWith(List<String> list, String withs) {
        for (int i = 0; i < list.size(); i++) {
            if (withs.startsWith(list.get(i).replace("*", "")) || withs.equals(list.get(i)))
                return i;
        }
        return -1;
    }

    /**
     * 从字符串中发现阿拉伯数字
     *
     * @param str
     * @return
     */
    public String findNumberFromString(String str) {
        String ss = "";
        if (str != null) {
            for (int i = 0; i < str.length(); i++) {
                int c = str.codePointAt(i);
                if (c >= '0' && c <= '9') {
                    ss += (char) c;
                }
            }
        }
        return ss;
    }

    /**
     * 过滤大数字
     *
     * @param line
     * @return
     */
    public String filterBigNum(String line) {
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
    public BigInteger[] findNums(String str, int len) {
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
    public String bigNumToWE(Number number) {
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
    private String toE(Number number) {
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
    private String toW(long l) {
        long l1 = 10000;
        Double d3 = (double) l / (double) l1;
        String s = d3.toString();
        if (s.length() >= s.indexOf(".") + 3)
            s = s.substring(0, s.indexOf(".") + 3);
        return s + "w";
    }

    /**
     * 获取今天
     *
     * @return
     */
    public String getToday() {
        return today = new SimpleDateFormat("dd").format(new Date());
    }

    public Integer getTodayInt() {
        return Integer.valueOf(getToday());
    }

    public String getTodayDetialString() {
        return getYear() + "-" + getMonth() + "-" + getDay();
    }

    /**
     * 获取这个月
     *
     * @return
     */
    public String getToMon() {
        return toMon == null ? (toMon = new SimpleDateFormat("MM").format(new Date())) : toMon;
    }

    /**
     * 从from到end随机一个数
     *
     * @param from can
     * @param end  can to this
     * @return
     */
    public long randA(int from, int end) {
        return Long.valueOf(RANDOM.nextInt(end - from) + from);
    }

    /**
     * 获取 格式时间 hh时mm分ss秒
     *
     * @param l 时间戳
     * @return
     */
    public String getTimeHHMM(long l) {
        return HHmmss.format(new Date(l));
    }

    /**
     * 获取提示冷却
     *
     * @param l
     * @return
     */
    public String getTimeTips(long l) {
        if (l < System.currentTimeMillis()) {
            return NOT_NEED_WAIT_TIPS;
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
    public String getTimeDDHHMM(long l) {
        if (l == Long.MAX_VALUE) return "最大";
        return DDHHmmss.format(new Date(l));
    }

    /**
     * 获取at格式
     *
     * @param who
     * @return
     */
    public String at(Long who) {
        return String.format("<At:%s>", who);
    }

    /**
     * 批处理 Integer.parse
     *
     * @param ss
     * @return
     */
    public int[] stringsToInts(String... ss) {
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
    public int[] stringToInts(String str) {
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

    public int[] stringToInts(String str, int xy) {
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
    public String findNumberZh(String str) {
        List<Character> cs = new ArrayList<>();
        char[] cs1 = str.toCharArray();
        for (char c1 : cs1) {
            if (char2int.containsKey(c1)) {
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
    public String trans(int num) {
        StringBuilder builder = new StringBuilder();
        builder.append(numeric[num / 1000] + "千").
                append(numeric[num / 100 % 10] + "百").
                append(numeric[num / 10 % 10] + "十").
                append(numeric[num % 10]);
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
    public int chineseNumber2Int(String chineseNumber) {
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

    // start IO 相关操作
    public boolean testFile(String path) {
        try {
            File file = new File(path);
            if (file.exists()) return true;
            File pf = file.getParentFile();
            if (pf != null && !pf.exists())
                pf.mkdirs();
            file.createNewFile();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public synchronized boolean putStringInFile(String str, String filename, String character) {
        testFile(filename);
        try {
            System.gc();
            FileOutputStream fos = new FileOutputStream(filename);
            fos.write(str.trim().getBytes(character));
            fos.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public synchronized boolean addStingInFile(Object str, String filename, String character) {
        testFile(filename);
        try {
            File f = new File(filename);
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(f, true), character), true);
            pw.println(str.toString());
            pw.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public synchronized String getStringFromFile(String filename) {
        testFile(filename);
        try {
            System.gc();
            File file = new File(filename);
            FileInputStream fis = new FileInputStream(file);
            byte[] bytes = new byte[(int) file.length()];
            fis.read(bytes);
            String result = new String(bytes, "utf-8");
            fis.close();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

    public synchronized String getStringFromFile(String filename, Long defaultValue) {
        testFile(filename);
        try {
            System.gc();
            File file = new File(filename);
            FileInputStream fis = new FileInputStream(file);
            byte[] bytes = new byte[(int) file.length()];
            fis.read(bytes);
            String result = new String(bytes, "utf-8");
            fis.close();
            if (result.trim().isEmpty()) {
                return defaultValue.toString();
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return defaultValue.toString();
        }
    }

    public synchronized String getStringFromFile(String filename, Long defaultValue, Long defaultValue1) {
        testFile(filename);
        try {
            System.gc();
            File file = new File(filename);
            FileInputStream fis = new FileInputStream(file);
            byte[] bytes = new byte[(int) file.length()];
            fis.read(bytes);
            String result = new String(bytes, "utf-8");
            fis.close();
            if (result.trim().isEmpty()) {
                return defaultValue + ":" + defaultValue1;
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return defaultValue + ":" + defaultValue1;
        }
    }

    public synchronized String getStringFromFile(String filename, String charcter) {
        testFile(filename);
        try {
            System.gc();
            File file = new File(filename);
            FileInputStream fis = new FileInputStream(file);
            byte[] bytes = new byte[(int) file.length()];
            fis.read(bytes);
            String result = new String(bytes, charcter);
            fis.close();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

    public synchronized String[] getStringsFromFile(String filename) {
        testFile(filename);
        try {
            System.gc();
            File file = new File(filename);
            file.createNewFile();
            BufferedReader pw = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            List<String> ls = new ArrayList<>();
            String line = null;
            while ((line = pw.readLine()) != null) {
                ls.add(line);
            }
            return ls.toArray(new String[ls.size()]);
        } catch (Exception e) {
            e.printStackTrace();
            return new String[]{"error"};
        }
    }
    //end IO 相关操作

    public synchronized boolean putStringInFile(String str, String filename) {
        testFile(filename);
        try {
            System.gc();
            FileOutputStream fos = new FileOutputStream(filename);
            fos.write(str.trim().getBytes("utf-8"));
            fos.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获取格式face
     *
     * @param id
     * @return
     */
    public String toFaceMes(String id) {
        return "<Face:" + id + ">";
    }

    /**
     * 获取格式img
     *
     * @param path
     * @return
     */
    public String pathToImg(String path) {
        return "<Pic:" + path + ">";
    }

    public String pathToImg0(String path) {
        return "&" + pathToImg(path);
    }

    /**
     * 更新今天的日期
     */
    public void updateToday() {
        today = null;
        toMon = null;
    }

    /**
     * 递归删除目录下的所有文件及子目录下所有文件
     *
     * @param dir 将要删除的文件目录
     * @return boolean Returns "true" if all deletions were successful.
     * If a deletion fails, the method stops attempting to
     * delete and returns "false".
     */
    public synchronized boolean deleteDir(File dir) {
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
    public long randLong(long o, float m, float e) {
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

    public String[] getIllegal() {
        String s0 = contextManager.getContextEntity(String.class, "Illegal.txt");
        if (s0 != null) {
            return s0.trim().split("\\s+");
        } else {
            return new String[]{};
        }
    }

    public String[] getIllegalSend() {
        String s0 = contextManager.getContextEntity(String.class, "Illegal.send");
        if (s0 != null) {
            return illegalSends == null ?
                    illegalSends = contextManager.getContextEntity(String.class, "Illegal.send").trim().split("\\s+") :
                    illegalSends;
        } else {
            return new String[]{};
        }
    }

    /**
     * 违规字符
     *
     * @param s
     * @return true 违规 false 没
     */
    public boolean isIlleg(String s) {
        for (String s1 : getIllegal()) {
            if (s.toUpperCase().contains(s1.toUpperCase()))
                return true;
        }
        return false;
    }

    public boolean isIllegSend(String s) {
        for (String s1 : getIllegalSend()) {
            if (s.toUpperCase().contains(s1.toUpperCase()))
                return true;
        }
        return false;
    }

    /**
     * 从字符串数组随机获取一个字符
     *
     * @param sss
     * @return
     */
    public String getRandString(String... sss) {
        return getRandT(sss);
    }

    public <T> T getRandT(T... ts) {
        return ts[RANDOM.nextInt(ts.length)];
    }

    public <T> T getRandT(Collection<T> ts) {
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
    public String device(Long l, double v, int d) {
        double f = l / v;
        return String.format("%." + d + "f", f);
    }
}
