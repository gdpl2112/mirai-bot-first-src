package Project.Services.DetailServices;

import io.github.kloping.judge.Judge;
import io.github.kloping.map.MapUtils;
import net.sourceforge.pinyin4j.PinyinHelper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public abstract class Idiom {
    private String path;
    private int maxFail = 5;

    public Idiom(String path) {
        this.path = path;
        Init();
    }

    public Idiom(String path, int maxFail) {
        this.path = path;
        this.maxFail = maxFail;
        Init();
    }

    private static boolean inited = false;
    private static Map<Character, List<String>> idiom = new ConcurrentHashMap<>();
    private static int length = -1;
    private static List<Character> list;

    public List<String> sys(char c) {
        return idiom.get(c);
    }

    private void Init() {
        if (inited) return;
        inited = true;
        String[] strings = getStringsFromFile(path);
        for (String s : strings) {
            if (s.length() == 4)
                MapUtils.append(idiom, s.charAt(0), s);
        }
        length = idiom.size();
        list = new LinkedList<>(idiom.keySet());
    }

    /**
     * 从文件中获取 字符数组 一行一个元素
     *
     * @param path
     * @return strs
     */
    public static String[] getStringsFromFile(String path) {
        try {
            if (!Judge.isNotNull(path)) return null;
            List<String> ls = new LinkedList<>();
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(path)), "UTF-8"));
            String line = null;
            while ((line = br.readLine()) != null) {
                ls.add(line);
            }
            br.close();
            return ls.toArray(new String[0]);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String upWord = null;
    private String upPinYin = null;
    public static final Random random = new Random();

    public String getRandom() {
        int r = random.nextInt(length);
        char c = list.get(r);
        upWord = idiom.get(c).get(0);
        upPinYin = getCharPinYinDontWithYinDiao(upWord.charAt(3));
        hist.add(upPinYin);
        return upWord;
    }

    public abstract void fail(String s);

    private Set<String> hist = new LinkedHashSet<>();
    private int f1 = 0;

    public String meet(String s) {
        if (s == null) return null;
        if (s.length() != 4) return "-1";
        if (hist.contains(s)) {
            if (++f1 >= maxFail)
                fail(upWord);
            return "-4";
        }
        char c1 = s.charAt(0);
        if (idiom.containsKey(c1)) {
            if (idiom.get(c1).contains(s)) {
                if (getCharPinYinDontWithYinDiao(c1).equals(upPinYin)) {
                    hist.add(s);
                    upWord = s;
                    upPinYin = getCharPinYinDontWithYinDiao(upWord.charAt(3));
                    f1 = 0;
                    return s;
                }
                if (++f1 >= maxFail)
                    fail(upWord);
                return "-3";
            }
        }
        if (++f1 >= maxFail)
            fail(upWord);
        return "-2";
    }

    /**
     * 对单个字进行转换
     *
     * @param pinYinStr 需转换的汉字字符串
     * @return 拼音字符串数组
     */
    public static String getCharPinYin(char pinYinStr) {
        String[] pinyin = null;
        pinyin = PinyinHelper.toHanyuPinyinStringArray(pinYinStr);
        if (pinyin == null) {
            return null;
        }
        return pinyin[0];
    }

    private static final Set<Character> sets = new LinkedHashSet<>();

    static {
        sets.add('0');
        sets.add('1');
        sets.add('2');
        sets.add('3');
        sets.add('4');
        sets.add('5');
    }

    public static String getCharPinYinDontWithYinDiao(char c) {
        String s = getCharPinYin(c);
        for (char c1 : sets) {
            if (s.contains(c1 + ""))
                s = s.replace(c1 + "", "");
        }
        return s;
    }

    public String getUpWord() {
        return upWord;
    }

    public String getUpPinYin() {
        return upPinYin;
    }
}
