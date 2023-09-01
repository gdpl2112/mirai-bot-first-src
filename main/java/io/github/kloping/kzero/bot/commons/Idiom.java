package io.github.kloping.kzero.bot.commons;

import io.github.kloping.map.MapUtils;
import net.sourceforge.pinyin4j.PinyinHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author github-kloping
 */
public abstract class Idiom {
    public static final Set<String> IDIOM_SET = new HashSet<>();
    public static final Random RANDOM = new Random();
    private static final Set<Character> sets = new LinkedHashSet<>();
    private static boolean inited = false;
    private static Map<Character, List<String>> idiom = new ConcurrentHashMap<>();
    private static int length = -1;
    private static List<Character> list;

    static {
        sets.add('0');
        sets.add('1');
        sets.add('2');
        sets.add('3');
        sets.add('4');
        sets.add('5');
    }

    private int maxFail = 5;
    private String upWord = null;
    private String upPinYin = null;
    private Set<String> hist = new LinkedHashSet<>();
    private int f1 = 0;

    public Idiom() {
        init();
    }

    public Idiom(String path, int maxFail) {
        this.maxFail = maxFail;
        init();
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

    public static String getCharPinYinDontWithYinDiao(char c) {
        String s = getCharPinYin(c);
        for (char c1 : sets) {
            if (s.contains(c1 + "")) {
                s = s.replace(c1 + "", "");
            }
        }
        return s;
    }

    public List<String> sys(char c) {
        return idiom.get(c);
    }

    private void init() {
        if (!inited) {
            try {
                URL url = Idiom.class.getClassLoader().getResource("idiom.txt");
                BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
                String line = null;
                while ((line = br.readLine()) != null) {
                    IDIOM_SET.add(line.trim());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            inited = true;
            for (String s : IDIOM_SET) {
                if (s.length() == 4) {
                    MapUtils.append(idiom, s.charAt(0), s);
                }
            }
            length = idiom.size();
            list = new LinkedList<>(idiom.keySet());
        }
    }

    public String getRandom() {
        int r = RANDOM.nextInt(length);
        char c = list.get(r);
        upWord = idiom.get(c).get(0);
        upPinYin = getCharPinYinDontWithYinDiao(upWord.charAt(3));
        hist.add(upPinYin);
        return upWord;
    }

    /**
     * 游戏结束
     *
     * @param s
     */
    public abstract void fail(String s);

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

    public String getUpWord() {
        return upWord;
    }

    public String getUpPinYin() {
        return upPinYin;
    }

    public Set<String> getHist() {
        return hist;
    }
}
