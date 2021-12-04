package Project.Tools;


import Entitys.gameEntitys.GhostObj;
import Entitys.gameEntitys.PersonInfo;
import Project.DataBases.GameDataBase;
import Project.Services.DetailServices.GameJoinDetailService;
import io.github.kloping.Mirai.Main.Resource;

import java.io.File;
import java.io.PrintWriter;
import java.util.*;

import static Project.DataBases.GameDataBase.*;
import static Project.Tools.Tool.*;
import static io.github.kloping.Mirai.Main.Resource.threads;

public class GameTool {
    /**
     * 1~9级魂士；10~19级魂师。
     * <p>
     * 20~29级大魂师 ；  30~39级魂尊。
     * <p>
     * 40~49级魂宗  ； 50~59级魂王。
     * <p>
     * 60~69级魂帝 ； 70~79级魂圣。
     * <p>
     * 80~89级魂斗罗  ； 90~99级封号斗罗。
     * <p>
     * 95~99级巅峰斗罗 ；100级神。
     *
     * @param level
     * @return
     */
    public static String getFH(Integer level) {
        if (level < 10) return "魂士";
        else if (level < 20) return "魂师";
        else if (level < 30) return "大魂师";
        else if (level < 40) return "魂尊";
        else if (level < 50) return "魂宗";
        else if (level < 60) return "魂王";
        else if (level < 70) return "魂帝";
        else if (level < 80) return "魂圣";
        else if (level < 90) return "魂斗罗";
        else if (level < 95) return "封号斗罗";
        else if (level < 100) return "巅峰斗罗";
        else if (level < 110) return "三级神祇";
        else if (level < 120) return "二级神祇";
        else if (level < 150) return "一级神祇";
        else if (level < 160) return "神王";
        return "垃圾废铁";
    }

    /**
     * 是否瓶颈
     *
     * @param who
     * @return
     */
    public static boolean isJTop(Long who) {
        long lev = getInfo(who).getLevel();
        lev++;
        boolean k1 = lev % 10 == 0;
        boolean k2 = getInfo(who).getXp() >= getInfo(who).getXpL();
        return k1 && k2;
    }

    /**
     * 某人是否有魂环
     *
     * @param who
     * @return
     */
    public static boolean HasHh(Long who) {
        GameDataBase.testMan(who);
        return GameDataBase.getHhs(who)[0] != 0;
    }

    /**
     * 某人是否活着
     *
     * @param who
     * @return
     */
    public static boolean isAlive(Long who) {
        GameDataBase.testMan(who);
        long is = getInfo(who).getHp();
        return is > 0;
    }

    public static double getAHBl(int id) {
        switch (id) {
            case 201:
                return 0.5;
            case 202:
                return 0.68;
            case 203:
                return 0.88;
            case 204:
                return 1.24;
            case 205:
                return 1.31;
            case 206:
                return 1.82;
            case 207:
                return 2.2;
        }
        return 0;
    }

    /**
     * 获取魂环加成的魂技
     *
     * @param id
     * @return
     */
    public static double getAHBl_(int id) {
        switch (id) {
            case 201:
                return 1;
            case 202:
                return 1.2;
            case 203:
                return 1.5;
            case 204:
                return 1.8;
            case 205:
                return 2.0;
            case 206:
                return 2.5;
            case 207:
                return 3;
        }
        return -1;
    }

    public static int Lmax(int level) {
        switch (level) {
            case 10:
                return 99;
            case 100:
                return 999;
            case 1000:
                return 9999;
            case 10000:
                return 99999;
            case 100000:
                return 999999;
            case 1000000:
                return 9999999;
            case 10000000:
                return 99999999;
        }
        return -1;
    }

    public static String getFhName(Long who) {
        PersonInfo personInfo = getInfo(who);
        if (personInfo.getLevel() < 90 || personInfo.getSname().isEmpty())
            return "";
        if (personInfo.getLevel() < 100)
            return personInfo.getSname() + " 斗罗";
        else if (personInfo.getLevel() < 150)
            return personInfo.getSname() + " 神";
        else if (personInfo.getLevel() >= 150)
            return personInfo.getSname() + " 神王";
        return "";
    }

    public static String getFhName(Long who, boolean tr) {
        PersonInfo personInfo = getInfo(who);
        if (personInfo.getLevel() < 90 || personInfo.getSname().isEmpty())
            return who + "";
        if (personInfo.getLevel() < 100)
            return personInfo.getSname() + "斗罗";
        else if (personInfo.getLevel() < 150)
            return personInfo.getSname() + "神";
        else if (personInfo.getLevel() >= 150)
            return personInfo.getSname() + "神王";
        return who + "";
    }

    /**
     * 玩家等级 => 魂兽等级
     *
     * @param l
     * @return
     */
    public static long getLtoGhsL(long l) {
        if (l < 10) return randA(100, 200);
        else if (l < 20) return randA(200, 1000);
        else if (l < 30) return randA(1000, 10000);
        else if (l < 40) return randA(10000, 20000);
        else if (l < 60) return randA(20000, 40000);
        else if (l < 80) return randA(40000, 80000);
        else if (l < 90) return randA(80000, 120000);
        else if (l < 95) return randA(120000, 200000);
        else if (l < 100) return randA(200000, 1000000);
        else if (l < 110) return randA(300000, 1500000);
        else if (l < 120) return randA(800000, 2000000);
        else if (l < 130) return randA(900000, 3000000);
        else if (l <= 151) return randA(990000, 10010000);
        else return 1L;
    }

    /**
     * 玩家等级 => 升级加成
     *
     * @param level
     * @return
     */
    public static long getAArtt(int level) {
        if (level < 10)
            return randA(80, 120);
        else if (level < 40)
            return randA(290, 315);
        else if (level < 60)
            return randA(900, 1100);
        else if (level < 80)
            return randA(2500, 2600);
        else if (level < 90)
            return randA(3900, 4000);
        else if (level < 95)
            return randA(39000, 40000);
        else if (level < 100) return randA(95000, 10000);
        else if (level < 110) return randA(179500, 190000);
        else if (level < 120) return randA(290000, 310000);
        else if (level < 150) return randA(160000, 166667);
        return -1;
    }

    /**
     * 获取每次修炼 的 加的 比例 例如 100 26 次修炼才可升级
     *
     * @param level
     * @return
     */
    public static int getRandXl(int level) {
        if (level < 5) return 2;
        else if (level < 20) return 3;
        else if (level < 40) return 4;
        else if (level < 60) return 7;
        else if (level < 70) return 8;
        else if (level < 80) return 10;
        else if (level < 90) return 12;
        else if (level < 95) return 19;
        else if (level < 98) return 24;
        else if (level < 100) return 29;
        else if (level < 110) return 33;
        else if (level < 120) return 46;
        else if (level < 130) return 68;
        else if (level < 145) return 80;
        else if (level < 148) return 168;
        else return 1200;
    }

    /**
     * 等级 => 魂环
     *
     * @param level
     * @return
     */
    public static Integer getHhByGh(int level) {
        if (level < 100) return 201;
        else if (level < 1000) return 202;
        else if (level < 10000) return 203;
        else if (level < 100000) return 204;
        else if (level < 1000000) return 205;
        else if (level < 10000000) return 206;
        else if (level < 100000000) return 207;
        return -1;
    }

    /**
     * 获取魂环加成率
     *
     * @param who
     * @return
     */
    public static final float getAllHHBL(Long who) {
        float bl = 1;
        if (HasHh(who)) {
            for (int i : getHhs(who)) {
                bl += getAHBl(i);
            }
        }
        return bl;
    }

    /**
     * 从 0.9 ~ 1.18 x v 的值
     *
     * @param v
     * @return
     */
    public static Long randFloatByte1(Long v) {
        return Long.valueOf(randLong(v, 0.90f, 1.18f));
    }

    /**
     * 魂兽是否活着
     *
     * @param who
     * @return
     */
    public static boolean isATrue(Long who) {
        GhostObj ghostObj =
                GameJoinDetailService.getGhostObjFrom(who);
        if (ghostObj != null) {
            if (ghostObj.getTime() > System.currentTimeMillis()) {
                return true;
            } else {
                GameJoinDetailService.saveGhostObjIn(who, null);
                return false;
            }
        }
        return false;
    }

    private static final File indexsFile = new File(GameDataBase.path + "/dates/indexsUserLevel");

    public static void loadPh() {
        try {
            if (indexsFile.exists()) {
                loadPhIndexs();
            } else {
                File file = new File(GameDataBase.path + "/dates/users/");
                String endN = null;
                for (File f1 : file.listFiles()) {
                    try {
                        endN = f1.getName();
                        phMaps.put(endN, getInfo(endN).getLevel());
                        String finalEndN = endN;
                        threads.execute(() -> {
                            removeAllTag(Long.valueOf(finalEndN));
                        });
                    } catch (Exception e) {
                        System.err.println(f1.getPath());
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        flushIndex = flushIndexMax;
        ph.clear();
        ph.addAll(phMaps.entrySet());
        upDateMan(Resource.bot.getId(), 1);
    }

    private static void loadPhIndexs() {
        String[] strings = getStringsFromFile(indexsFile.getPath());
        for (String s2 : strings) {
            try {
                String[] ss = s2.split(":");
                Long who = Long.valueOf(ss[0]);
                Integer level = Integer.valueOf(ss[1]);
                phMaps.put(who.toString(), level);
                threads.execute(() -> {
                    removeAllTag(who);
                });
            } catch (Exception e) {
                System.err.println(s2);
                e.printStackTrace();
                continue;
            }
        }
    }

    public static synchronized void removeAllTag(Number number) {
        putPerson(getInfo(number).setTag(""));
    }

    public static List<Map.Entry<String, Integer>> phGet(int num) {
        if (num >= ph.size())
            return ph;
        List<Map.Entry<String, Integer>> ph1 = new ArrayList<>();
        for (Map.Entry e : ph) {
            if (ph1.size() == num)
                break;
            else {
                ph1.add(e);
            }
        }
        return ph1;
    }

    public static final List<Map.Entry<String, Integer>> ph = new ArrayList<>();
    private static final Map<String, Integer> phMaps = new LinkedHashMap<>();
    public static int num = 199;

    public static void upDateMan(Long who, Integer level) {
        Resource.DaeThreads.submit(() -> {
            if (who == null || level == null) return;
            if (phMaps.isEmpty() || phMaps.size() < num) {
                phMaps.put(String.valueOf(who), level);
            } else {
                if (phMaps.containsKey(who)) {
                    phMaps.put(String.valueOf(who), level);
                } else {
                    if (level > ph.get(ph.size() - 1).getValue()) {
                        phMaps.remove(ph.get(ph.size() - 1).getKey());
                        phMaps.put(String.valueOf(who), level);
                    } else {

                    }
                }
            }
            ph.clear();
            ph.addAll(phMaps.entrySet());
            Collections.sort(ph, new Comparator<Map.Entry<String, Integer>>() {
                @Override
                public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                    return o2.getValue() - o1.getValue();
                }
            });
            flushIndexs();
        });
    }

    public static boolean containsKey(Long who) {
        for (Map.Entry<String, Integer> entry : ph) {
            if (who.equals(entry.getKey().trim()))
                return true;
        }
        return false;
    }

    public static <T, E> Map.Entry<T, E> toEntry(T t, E e) {
        return new Map.Entry<T, E>() {
            @Override
            public T getKey() {
                return t;
            }

            @Override
            public E getValue() {
                return e;
            }

            @Override
            public E setValue(E value) {
                return value;
            }
        };
    }

    private static int flushIndexMax = 10;
    private static int flushIndex = 0;

    private static void flushIndexs() {
        if (flushIndex++ % flushIndexMax != 0) return;
        try {
            PrintWriter pw = new PrintWriter(indexsFile);
            for (Map.Entry<String, Integer> entry : ph) {
                pw.println(entry.getKey() + ":" + entry.getValue());
            }
            pw.flush();
            pw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
