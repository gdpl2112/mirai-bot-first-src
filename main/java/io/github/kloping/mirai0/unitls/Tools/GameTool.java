package io.github.kloping.mirai0.unitls.Tools;


import Project.aSpring.SpringBootResource;
import Project.dataBases.GameDataBase;
import Project.services.detailServices.ac.GameJoinDetailService;
import io.github.kloping.mirai0.commons.GhostObj;
import io.github.kloping.mirai0.commons.PersonInfo;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static Project.dataBases.GameDataBase.getHhs;

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
        long lev = GameDataBase.getInfo(who).getLevel();
        lev++;
        boolean k1 = lev % 10 == 0;
        boolean k2 = GameDataBase.getInfo(who).getXp() >= GameDataBase.getInfo(who).getXpL();
        return k1 && k2;
    }

    public static boolean isJTop0(Long who) {
        long lev = GameDataBase.getInfo(who).getLevel();
        lev++;
        boolean k1 = lev % 10 == 0;
        return k1;
    }

    /**
     * 某人是否有魂环
     *
     * @param who
     * @return
     */
    public static boolean hasHh(Long who) {
        GameDataBase.testMan(who);
        return getHhs(who).length > 0;
    }

    /**
     * 某人是否活着
     *
     * @param who
     * @return
     */
    public static boolean isAlive(Long who) {
        GameDataBase.testMan(who);
        long is = GameDataBase.getInfo(who).getHp();
        return is > 0L;
    }

    /**
     * 魂环加成
     *
     * @param id
     * @return
     */
    public static double getAHBl(int id) {
        switch (id) {
            case 201:
                return 0.1;
            case 202:
                return 0.2;
            case 203:
                return 0.36;
            case 204:
                return 0.48;
            case 205:
                return 0.78;
            case 206:
                return 1.2;
            case 207:
                return 1.4;
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
                return 0.9;
            case 202:
                return 1.3;
            case 203:
                return 1.6;
            case 204:
                return 1.9;
            case 205:
                return 2.2;
            case 206:
                return 2.6;
            case 207:
                return 3.0;
        }
        return 1;
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
        PersonInfo personInfo = GameDataBase.getInfo(who);
        if (personInfo.getLevel() < 90 || personInfo.getSname().isEmpty()) return "";
        if (personInfo.getLevel() < 100) return personInfo.getSname() + " 斗罗";
        else if (personInfo.getLevel() < 150) return personInfo.getSname() + " 神";
        else if (personInfo.getLevel() >= 150) return personInfo.getSname() + " 神王";
        return "";
    }

    public static String getFhName(Long who, boolean tr) {
        PersonInfo personInfo = GameDataBase.getInfo(who);
        if (personInfo.getLevel() < 90 || personInfo.getSname().isEmpty()) return who + "";
        if (personInfo.getLevel() < 100) return personInfo.getSname() + "斗罗";
        else if (personInfo.getLevel() < 150) return personInfo.getSname() + "神";
        else if (personInfo.getLevel() >= 150) return personInfo.getSname() + "神王";
        return who + "";
    }

    /**
     * 玩家等级 => 魂兽等级
     *
     * @param l
     * @return
     */
    public static long getLtoGhsL(long l) {
        if (l < 10) return Tool.tool.randA(100, 200);
        else if (l < 20) return Tool.tool.randA(200, 1000);
        else if (l < 30) return Tool.tool.randA(1000, 10000);
        else if (l < 40) return Tool.tool.randA(10000, 20000);
        else if (l < 60) return Tool.tool.randA(20000, 40000);
        else if (l < 80) return Tool.tool.randA(40000, 80000);
        else if (l < 90) return Tool.tool.randA(80000, 120000);
        else if (l < 95) return Tool.tool.randA(120000, 200000);
        else if (l < 100) return Tool.tool.randA(200000, 1000000);
        else if (l < 110) return Tool.tool.randA(300000, 1500000);
        else if (l < 120) return Tool.tool.randA(800000, 2000000);
        else if (l < 130) return Tool.tool.randA(900000, 3000000);
        else if (l <= 151) return Tool.tool.randA(990000, 10010000);
        else return 1L;
    }

    /**
     * 玩家等级 => 升级加成
     *
     * @param level
     * @return
     */
    public static long getAArtt(int level) {
        if (level < 10) return Tool.tool.randA(80, 120);
        else if (level < 40) return Tool.tool.randA(290, 315);
        else if (level < 60) return Tool.tool.randA(900, 1100);
        else if (level < 80) return Tool.tool.randA(2500, 2600);
        else if (level < 90) return Tool.tool.randA(3900, 4000);
        else if (level < 95) return Tool.tool.randA(39000, 40000);
        else if (level < 100) return Tool.tool.randA(95000, 100000);
        else if (level < 110) return Tool.tool.randA(179500, 190000);
        else if (level < 120) return Tool.tool.randA(280000, 285000);
        else return Tool.tool.randA(160000, 166667);
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
        else if (level < 80) return 11;
        else if (level < 90) return 14;
        else if (level < 95) return 19;
        else if (level < 98) return 24;
        else if (level < 100) return 29;
        else if (level < 110) return 33;
        else if (level < 120) return 46;
        else if (level < 130) return 68;
        else if (level < 145) return 80;
        else if (level < 147) return 120;
        else if (level < 148) return 168;
        else if (level < 150) return 1200;
        else if (level == 150) return 700;
        else return 99999;
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
        if (hasHh(who)) {
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
        return Long.valueOf(Tool.tool.randLong(v, 0.87f, 1.18f));
    }

    /**
     * 魂兽是否活着
     *
     * @param who
     * @return
     */
    public static boolean isATrue(Long who) {
        GhostObj ghostObj = GameJoinDetailService.getGhostObjFrom(who);
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

    public static List<Map.Entry<String, Integer>> phGet(int num) {
        List<Map.Entry<String, Integer>> list = new LinkedList<>();
        for (PersonInfo personInfo : SpringBootResource.getPersonInfoMapper().getOrderByLevel(num)) {
            list.add(Tool.tool.getEntry(personInfo.getName(), personInfo.getLevel()));
        }
        return list;
    }

    public static String getLevelByGhostId(Integer id) {
        if (id < 503) return "十";
        else if (id < 505) return "百";
        else if (id < 507) return "千";
        else if (id < 510) return "万";
        else if (id < 514) return "十万";
        else if (id < 518) return "百万";
        else if (id < 521) return "神";
        else if (id < 1000) return "全";
        else return "未知";
    }

    /**
     * 获取 魂兽最大支援数量
     *
     * @param id
     * @param level
     * @return
     */
    public static int getMaxHelpNumByGhostIdAndLevel(Integer id, Integer level) {
        if (level >= 10000000) return 2;
        else if (id < 800) return 1;
        else return 0;
    }
}
