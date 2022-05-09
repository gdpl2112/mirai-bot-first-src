package io.github.kloping.mirai0.Main.temp;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * @author github.kloping
 */
public class C1 {

    private List<C0> list = new LinkedList<>();

    public List<C0> getList() {
        return list;
    }

    public void setList(List<C0> list) {
        this.list = list;
    }

    public C0 nearestC0() {
        C0 c0 = null;
        long st0 = -1;
        for (C0 c1 : list) {
            if (c0 == null) {
                c0 = c1;
                st0 = c1.st();
                continue;
            } else if (c1.st() < st0) {
                c0 = c1;
            }
        }
        return c0;
    }

    private static final SimpleDateFormat F0 = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

    private static int getYear() {
        String s = F0.format(new Date());
        return Integer.parseInt(s.substring(0, 4));
    }

    private static int getMon() {
        String s = F0.format(new Date());
        return Integer.parseInt(s.substring(5, 7));
    }

    static int getDay() {
        String s = F0.format(new Date());
        return Integer.parseInt(s.substring(8, 10));
    }

    public static long getTimeFromNowTo(int hour, int mini, int mil) {
        Date date = null;
        try {
            String p1 = String.format("%s-%s-%s-%s-%s-%s", getYear(), getMon(), getDay(), hour, mini, mil);
            date = F0.parse(p1);
        } catch (Exception e) {
        }
        long millis = date.getTime();
        long now = System.currentTimeMillis();
        return millis - now;
    }
}
