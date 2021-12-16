package Project.Tools;


import Project.DataBases.DataBase;

import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static io.github.kloping.Mirai.Main.Resource.contextManager;

public class Tool {
    public static final String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};

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
        return weekDays[w];
    }

    private static final SimpleDateFormat dfn = new SimpleDateFormat("/yyyy/MM/dd/HH_mm_ss/");

    public static String getLogTimeFormat() {
        return "./logs" + dfn.format(new Date());
    }

    private static final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm");
    private static final SimpleDateFormat df2 = new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒");
    private static final SimpleDateFormat df4 = new SimpleDateFormat("yyyy年MM月dd日HH时mm分");
    private static final SimpleDateFormat df3 = new SimpleDateFormat("MM月dd日HH时mm分ss秒");

    /**
     * 获取格式时间 yyy年....ss秒
     *
     * @param t 时间戳
     * @return
     */
    public static final String getTimeYMdhms(long t) {
        return df2.format(new Date(t));
    }

    public static final String getTimeYMdhm(long t) {
        return df4.format(new Date(t));
    }

    /**
     * 获取格式时间 xx月....ss秒
     *
     * @param t 时间戳
     * @return
     */
    public static final String getTimeM(long t) {
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
    public static long getTimeFromNowTo(int day, int hour, int mil) {
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
     * @param k
     * @param v
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V> Map.Entry<K, V> getEntry(K k, V v) {
        Map.Entry<K, V> entry = new Map.Entry<K, V>() {
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
                return null;
            }
        };

        return entry;
    }

    public static Random rand = new Random();

    /**
     * 获取图片格式头像
     *
     * @param qq
     * @return
     */
    public static String getTou(Long qq) {
        return pathToImg("http://q2.qlogo.cn/headimg_dl?dst_uin=" + qq + "&spec=100");
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
    public static int EveListStartWith(List<String> list, String withs) {
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
    public static String findNumberFromString(String str) {
        String ss = "";
        if (str != null) {
            for (int i = 0; i < str.length(); i++) {
                int c = str.codePointAt(i);// charAt(i);
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
    public static String filterBigNum(String line) {
        BigInteger[] numbers = findNums(line, 5);
        for (Number number : numbers) {
            String r = BigNumToWE(number);
            line = line.replace(number + "", r);
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

    private static final BigDecimal b2 = new BigDecimal(100000000L);
    private static final BigDecimal be = new BigDecimal(1000000000000L);
    private static final BigDecimal bw = new BigDecimal(10000000000000000L);

    /**
     * 大数字转为 WE (万亿) 单位
     *
     * @param number
     * @return
     */
    public static String BigNumToWE(Number number) {
        if (number instanceof BigInteger) {
            if (number.toString().length() < 9) {
                return toW(number.longValue());
            } else if (number.toString().length() < 13) {
                BigDecimal b1 = new BigDecimal(number + "");
                b1.setScale(2, BigDecimal.ROUND_HALF_UP);
                b1 = b1.divide(b2);
                String s = b1.toString();
                s = s.substring(0, s.indexOf(".") + 2);
                return s + "e";
            } else if (number.toString().length() < 17) {
                BigDecimal b1 = new BigDecimal(number + "");
                b1.setScale(2, BigDecimal.ROUND_HALF_UP);
                b1 = b1.divide(be);
                String s = b1.toString();
                s = s.substring(0, s.indexOf(".") + 3);
                return s + "we";
            } else {
                BigDecimal b1 = new BigDecimal(number + "");
                b1.setScale(2, BigDecimal.ROUND_HALF_UP);
                b1 = b1.divide(bw);
                String s = b1.toString() + "";
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
        return number.toString() + "";
    }

    /**
     * 大数字转为 E (亿) 单位
     *
     * @param number
     * @return
     */
    private static String toE(Number number) {
        BigDecimal b1 = new BigDecimal(number + "");
        b1.setScale(2, BigDecimal.ROUND_HALF_UP);
        b1 = b1.divide(b2);
        String s = b1.toString() + "";
        s = s.substring(0, s.indexOf(".") + 3);
        return s + "e";
    }

    /**
     * 大数字转为 W (万) 单位
     *
     * @param number
     * @return
     */
    private static String toW(long l) {
        long l1 = 10000;
        double d3 = (double) l / (double) l1;
        String s = d3 + "";
        if (s.length() >= s.indexOf(".") + 3)
            s = s.substring(0, s.indexOf(".") + 3);
        return s + "w";
    }

    private static String today = null;
    private static String toMon = null;

    /**
     * 获取今天
     *
     * @return
     */
    public static String getToday() {
        return today == null ? (today = new SimpleDateFormat("dd").format(new Date())) : today;
    }

    /**
     * 获取这个月
     *
     * @return
     */
    public static String getToMon() {
        return toMon == null ? (toMon = new SimpleDateFormat("MM").format(new Date())) : toMon;
    }

    /**
     * 从from到end随机一个数
     *
     * @param from
     * @param end
     * @return
     */
    public static long randA(int from, int end) {
        int t1 = from, t2 = end;
        return Long.valueOf(rand.nextInt(t2 - t1) + t1);
    }

    private static final SimpleDateFormat HHmmss = new SimpleDateFormat("HH:mm:ss");
    private static final SimpleDateFormat DDHHmmss = new SimpleDateFormat("dd日HH:mm:ss");

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
    public static String At(Long who) {
        return String.format("<At:%s>", who);
    }

    /**
     * 批处理 Integer.parse
     *
     * @param ss
     * @return
     */
    public static int[] StringsToInts(String... ss) {
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
    public static int[] StringToInts(String str) {
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

    public static int[] StringToInts(String str, int xy) {
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

    private static final Map<Character, Integer> char2int = new ConcurrentHashMap<>();
    private static final Map<Integer, Character> int2char = new ConcurrentHashMap<>();

    static {
        int i = 1;
        for (char c : new Character[]{'一', '二', '三', '四', '五', '六', '七', '八', '九', '十'}) {
            int2char.put(i, c);
            char2int.put(c, i);
            i++;
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

    private static final String[] numeric = new String[]{"零", "一", "二", "三", "四", "五", "六", "七", "八", "九"};

    /**
     * 阿拉伯转中文
     *
     * @param num
     * @return
     */
    public static String trans(int num) {
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

    public static final char[] cnArr = new char[]{'一', '二', '三', '四', '五', '六', '七', '八', '九'};
    private static final char[] chArr = new char[]{'十', '百', '千', '万', '亿'};

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

    // start IO 相关操作
    public static boolean testFile(String path) {
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

    public static synchronized boolean putStringInFile(String str, String filename, String character) {
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

    public static synchronized boolean addStingInFile(Object str, String filename, String character) {
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

    public static synchronized String getStringFromFile(String filename) {
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

    public static synchronized String getStringFromFile(String filename, String charcter) {
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

    public static synchronized String[] getStringsFromFile(String filename) {
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

    public static synchronized String[] getStringsFromFile(String filename, String chara) {
        testFile(filename);
        try {
            System.gc();
            File file = new File(filename);
            file.createNewFile();
            BufferedReader pw = new BufferedReader(new InputStreamReader(new FileInputStream(file), chara));
            List<String> ls = new ArrayList<>();
            String line = null;
            while ((line = pw.readLine()) != null) {
                ls.add(line);
            }
            return ls.toArray(new String[ls.size()]);
        } catch (Exception e) {
            e.printStackTrace();
            return new String[]{};
        }
    }

    public static synchronized boolean putStringInFile(String str, String filename) {
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
    //end IO 相关操作

    /**
     * 获取格式face
     *
     * @param id
     * @return
     */
    public static String toFaceMes(String id) {
        return "<Face:" + id + ">";
    }

    /**
     * 获取格式img
     *
     * @param path
     * @return
     */
    public static String pathToImg(String path) {
        return "<Pic:" + path + ">";
    }


    /**
     * 获取今天的注册表
     *
     * @return
     */
    public static synchronized String[] getDayList() {
        File file = new File(DataBase.path + "/History/", getToMon() + "day" + getToday());
        String[] sss = getStringsFromFile(file.getPath(), "utf-8");
        return sss;
    }

    /**
     * 在今天注册
     *
     * @param qq
     * @return
     */
    public static int regToday(Number qq) {
        File file = new File(DataBase.path + "/History/", getToMon() + "day" + getToday());
        String[] sss = getStringsFromFile(file.getPath(), "utf-8");
        int end;
        if ((end = Arrays.asList(sss).indexOf(qq.toString())) != -1)
            return end;
        else end = sss.length;

        addStingInFile(qq.toString(), file.getPath(), "utf-8");

        return end;
    }

    /**
     * 更新今天的日期
     */
    public static void update_Today() {
        today = null;
        toMon = null;
    }

    /**
     * 删除空目录
     *
     * @param dir 将要删除的目录路径
     */
    private static synchronized void doDeleteEmptyDir(String dir) {
        boolean success = (new File(dir)).delete();
        if (success) {
            System.out.println("Successfully deleted empty directory: " + dir);
        } else {
            System.out.println("Failed to delete empty directory: " + dir);
        }
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
        // 目录此时为空，可以删除
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

    private static int IllegalIndex = 0;
    private static String upIllegalStr = null;
    private static String[] upIllegalss = null;

    public static synchronized String[] getIllegal() {
        if (upIllegalStr == null || upIllegalss == null)
            upIllegalStr = contextManager.getContextEntity(String.class, "Illegal.txt").trim();
        return upIllegalss == null ? upIllegalss = upIllegalStr.split("\\s+") : upIllegalss;
    }

    /**
     * 违规字符
     *
     * @param s
     * @return true 违规 false 没
     */
    public static boolean isIlleg(String s) {
        for (String s1 : getIllegal()) {
            if (s.toUpperCase().contains(s1.toUpperCase()))
                return true;
        }
        return false;
    }

    public static boolean isIlleg_(String s) {
        for (String s1 : getIllegal()) {
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
    public static final String getRandString(String... sss) {
        return getRandT(sss);
    }

    public static final <T> T getRandT(T... ts) {
        return ts[rand.nextInt(ts.length)];
    }

    /**
     * 计算百分比
     *
     * @param b b%
     * @param v
     * @return v 的 b%
     */
    public final static Long percentTo(Integer b, Number v) {
        if (v.longValue() < 100) {
            float f = b / 100f;
            return (long) (f * (v.intValue()));
        }
        double d = v.longValue();
        d /= 100;
        d *= b;
        long v1 = (long) d;
        return v1;
    }

    /**
     * @param v1
     * @param v2
     * @return v1/v2 => %
     */
    public final static Integer toPercent(Number v1, Number v2) {
        double dv1 = (double) v1.longValue();
        double dv2 = (double) v2.longValue();
        double dv3 = dv1 / dv2;
        dv3 *= 100;
        int v3 = (int) dv3;
        return v3;
    }

    /**
     * 从网络上获取bytes
     *
     * @param url 网址
     * @return
     */
    public static byte[] getBytesFromHttpUrl(String url) {
        try {
//            System.out.println(url);
//            url = Jsoup.connect(url).ignoreContentType(true).get().baseUri();
//            System.out.println(url);
            URL mUrl = new URL(url);
            URLConnection connection = mUrl.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(true);
            connection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/95.0.4638.54 Safari/537.36 Edg/95.0.1020.30");
            connection.connect();
            InputStream is = connection.getInputStream();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] bytes = new byte[1024 * 1024];
            int len = -1;
            while ((len = is.read(bytes)) != -1) {
                baos.write(bytes, 0, len);
            }
            return baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <K, V extends Number> Map<K, V> sortMapByValue(Map<K, V> oriMap) {
        if (oriMap != null && !oriMap.isEmpty()) {
            List<Map.Entry<K, V>> entryList = new ArrayList<Map.Entry<K, V>>(oriMap.entrySet());
            Collections.sort(entryList,
                    new Comparator<Map.Entry<K, V>>() {
                        public int compare(Map.Entry<K, V> entry1, Map.Entry<K, V> entry2) {
                            int value1 = entry1.getValue().intValue(), value2 = entry2.getValue().intValue();
                            return value2 - value1;
                        }
                    });
            oriMap = new LinkedHashMap<>();
            Iterator<Map.Entry<K, V>> iter = entryList.iterator();
            Map.Entry<K, V> tmpEntry = null;
            while (iter.hasNext()) {
                tmpEntry = iter.next();
                oriMap.put(tmpEntry.getKey(), tmpEntry.getValue());
            }
        }
        return oriMap;
    }
}
