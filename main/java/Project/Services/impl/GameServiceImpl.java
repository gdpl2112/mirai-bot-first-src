package Project.Services.impl;


import Entitys.Group;
import Entitys.gameEntitys.GInfo;
import Entitys.gameEntitys.PersonInfo;
import Entitys.gameEntitys.Warp;
import Entitys.gameEntitys.Zon;
import Project.Controllers.ConfirmController;
import Project.DataBases.DataBase;
import Project.DataBases.GameDataBase;
import Project.DataBases.ZongMenDataBase;
import Project.DataBases.skill.SkillDataBase;
import Project.Services.DetailServices.GameDetailService;
import Project.Services.Iservice.IGameService;
import Project.Tools.Drawer;
import Project.broadcast.enums.ObjType;
import io.github.kloping.Mirai.Main.ITools.MemberTools;
import io.github.kloping.Mirai.Main.ITools.MessageTools;
import io.github.kloping.MySpringTool.annotations.Entity;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static Project.DataBases.GameDataBase.*;
import static Project.DataBases.ZongMenDataBase.getZonInfo;
import static Project.DataBases.ZongMenDataBase.putZonInfo;
import static Project.Tools.Drawer.*;
import static Project.Tools.GameTool.*;
import static Project.Tools.Tool.*;
import static io.github.kloping.Mirai.Main.ITools.MemberTools.getNameFromGroup;

@Entity
public class GameServiceImpl implements IGameService {


    @Override
    public String xl(Long who) {
        PersonInfo is = getInfo(who);
        if (is.getWh() == 0 && is.getLevel() >= 2) {
            return "请先觉醒武魂 ";
        }
        long l = getK1(who);
        long now = System.currentTimeMillis();
        if (now >= l) {
            int tr = rand.nextInt(6) + 9;
            int c = (getRandXl(getInfo(who).getLevel()));
            long mx = is.getXpL();
            long xr = mx / c;
            if (is.getLevel() >= 150) xr = 0;
            is.addXp(xr).setK1(now + (tr * 1000 * 60));
            putPerson(is);
            long ll1 = HF_Hp(who);
            long ll2 = HF_Hl(who);
            long ll3 = HF_Hj(who);
            StringBuilder sb = new StringBuilder();
            if (is.getWh() != 0) {
                sb.append(GameDataBase.getNameById(is.getWh()));
            }
            sb.append(String.format("你花费了%s分钟修炼", tr)).append(",");
            sb.append(String.format("获得了%s点经验", xr)).append(",");
            sb.append(String.format("恢复了%s点血量", ll1)).append(",");
            sb.append(String.format("恢复了%s点魂力", ll2)).append(",");
            sb.append(String.format("恢复了%s点精神力", ll3)).append(",");

            return is.getWh() == 0 ?
                    getImageFromStrings(sb.toString().split(",")) :
                    GameDataBase.getImgById(is.getWh()) + getImageFromStrings(sb.toString().split(","));
        } else {
            return "冷却时间还没到=>" + getTimeHHMM(l);
        }
    }

    @Override
    public String xl2(long who) {
        PersonInfo is = getInfo(who);
        if (is.getWh() == 0 && is.getLevel() >= 2) {
            return "请先觉醒武魂 ";
        }
        long l = getK1(who);
        long now = System.currentTimeMillis();
        if (now >= l) {
            int tr = rand.nextInt(9) + 6;
            int c = (getRandXl(getInfo(who).getLevel()));
            long mx = is.getXpL();
            long xr = mx / c;
            if (is.getLevel() >= 150) xr = 0;
            xr *= 1.1;
            is.addXp(xr).setK1(now + (tr * 1000 * 60));
            putPerson(is);

            long ll1 = HF_Hp(who, 1.1);
            long ll2 = HF_Hl(who, 1.1);
            long ll3 = HF_Hj(who, 1.1);

            StringBuilder sb = new StringBuilder();
            if (is.getWh() != 0) {
                sb.append(GameDataBase.getNameById(is.getWh()));
            }

            sb.append(String.format("你花费了%s分钟双修", tr)).append(",");
            sb.append(String.format("获得了%s点经验", xr)).append(",");
            sb.append(String.format("恢复了%s点血量", ll1)).append(",");
            sb.append(String.format("恢复了%s点魂力", ll2)).append(",");
            sb.append(String.format("恢复了%s点精神力", ll3)).append(",");
            return is.getWh() == 0 ?
                    getImageFromStrings(sb.toString().split(",")) :
                    GameDataBase.getImgById(is.getWh()) + getImageFromStrings(sb.toString().split(","));

        } else {
            return "冷却时间还没到=>" + getTimeHHMM(l);
        }
    }

    @Override
    public String[] getBags(String who) {
        return getBags(Long.valueOf(who));
    }

    @Override
    public String info(Long who) {
        PersonInfo is = getInfo(who);
        StringBuilder str1 = new StringBuilder();
        StringBuilder str = new StringBuilder();
        if (!is.getSname().isEmpty()) {
            str1.append(pathToImg(createFont(getFhName(who))));
        }
        long n = is.getWh();
        if (n == 0)
            str1.append("你的武魂: 暂未获得").append("\r\n");
        else {
            str1.append("你的武魂: " + GameDataBase.getNameById(n)).append("\r\n");
            str1.append(GameDataBase.getImgById((int) n)).append("\r\n");
        }

//        str.append("经验:" + is.getXp() + "/" + is.getXpL()).append("\r\n");
//        str.append("血量:" + is.getHp() + "/" + is.getHpl()).append("\r\n");
//        str.append("精神力:" + is.getHj() + "/" + is.getHjL()).append("\r\n");
//        str.append("魂力:" + is.getHl() + "/" + is.getHll()).append("\r\n");
//        str.append("攻击值:" + is.getAtt()).append("\r\n");
//        str.append("等级:" + is.getLevel() + "=>" + GamegetFH(is.getLevel())).append("\r\n");
//        str.append("金魂币:" + is.getGold()).append("\r\n");
//        str.append("融合状态:" + (is.getBindQ().longValue() == -1 ? "未融合" : "已融合")).append("\r\n");
//        return str1 + pathToImg(createImage(str.toString().split("\r\n")));
        return str1 + pathToImg(Drawer.drawInfoPng(is));

    }

    @Override
    public String upUp(Long who) {
        PersonInfo is = getInfo(who);
        long xp = is.getXp();
        long xpL = is.getXpL();
        long L = is.getLevel();
        if (L == 2 && getInfo(who).getWh() == 0)
            return "请先觉醒武魂";
        if (xp >= xpL) {
            if (L > 150) {
                return "等级最大限制..";
            }
            if (isJTop(who)) {
                return "无法升级,因为到达等级瓶颈,吸收魂环后继续升级";
            }
            StringBuilder sb = new StringBuilder();
            sb.append("升级成功");
            is.addLevel(1).addXp(-xpL);
            putPerson(is);
            sb.append(upTrue(who));
            return pathToImg(createImage(sb.toString().split("\r\n")));
        } else {
            return "经验不足,无法升级!";
        }
    }

    @Override
    public String upTrue(Long who) {
        StringBuilder sb = new StringBuilder();
        PersonInfo personInfo = getInfo(who);

        int L = personInfo.getLevel();
        long xpl = getAArtt(L) * 10;
        personInfo.addXpL(xpl);

        long ir1 = getAArtt(L);
        personInfo.addHpl(ir1).addHp(ir1);
        sb.append("\r\n增加了:").append(ir1).append("最大血量");

        long ir2 = getAArtt(L);
        personInfo.addHl(ir2).addHll(ir2);
        sb.append("\r\n增加了:").append(ir2).append("最大魂力");

        long ir3 = getAArtt(L);
        personInfo.addAtt(ir3);
        sb.append("\r\n增加了:").append(ir3).append("攻击");

        long ir4 = getAArtt(L) / 10;
        personInfo.addHjL(ir4).addHj(ir4);
        sb.append("\r\n增加了:").append(ir4).append("最大精神力");

        personInfo.addGold(50L);
        if (L == 1) {
            sb.append("\r\n请觉醒武魂后再进行修炼(觉醒)");
        }

        sb.append("\r\n当前等级:").append(personInfo.getLevel());

        putPerson(personInfo);

        return sb.toString();
    }

    @Override
    public String openEyeWh(Long who) {
        PersonInfo is = getInfo(who);
        if (is.getWh() != 0) {
            return "你已经觉醒武魂了";
        }
        long L = is.getLevel();
        if (L < 2) {
            return "2级即可觉醒";
        }
        int r = rand.nextInt(31) + 1;
        putPerson(is.setWh(r));
        return "觉醒成功!!";
    }

    @Override
    public String[] getBags(Long who) {
        List<String> list = new ArrayList<>();
        String[] sss = null;
        for (int i : getBgs(who)) {
            String str = getNameById(i);
            int n = -1;
            if ((n = listEveStartWith(list, str)) != -1) {
                String or = list.get(n);
                list.remove(n);
                if (or.contains("x")) {
                    int num = Integer.parseInt(or.substring(or.indexOf("x") + 1));
                    or = or.replace("x" + num + "", "") + "x" + (++num);
                } else {
                    or = or + "x2";
                }
                list.add(or);
            } else {
                list.add(str);
            }
        }
        if (list.isEmpty()) {
            sss = new String[]{"空空如也"};
        } else {
            sss = list.toArray(new String[0]);
        }
        return sss;
    }

    @Override
    public String BuyGold(Long who, long num) {
        long is = DataBase.getAllInfo(who).getScore();
        if (is >= num * 2) {
            DataBase.addScore(-(num * 2), who);
            putPerson(getInfo(who).addGold(num));
            return "购买成功";
        } else {
            return getImageFromStrings("积分不足", "你需要" + num * 2 + "积分", "才能购买" + num + "个金魂币");
        }
    }

    @Override
    public String showHh(Long who) {
        Integer[] iii = getHhs(who);
        StringBuilder sb = new StringBuilder();
        if (iii[0] == 0) {
            return "暂无魂环";
        } else {
            for (Integer i : iii) {
                sb.append(GameDataBase.getImgById(i)).append("\r\n");
            }
        }
        return sb.toString();
    }

    @Override
    public String parseHh(Long who, int id) {
        try {
            List<Integer> bgids = new ArrayList<>(Arrays.asList(GameDataBase.getBgs(who)));
            if (bgids.contains(id)) {
                return joinHh(who, id);
            } else {
                return "你的背包里没有" + getNameById(id);
            }
        } catch (Exception e) {
            return "未找到相关";
        }
    }

    @Override
    public String AttWhos(Long who, Long whos, Group group) {
        if (ZongMenDataBase.qq2id.containsKey(whos)) {
            if (ZongMenDataBase.qq2id.containsKey(who)) {
                Long id1 = Long.valueOf(ZongMenDataBase.qq2id.get(who));
                Long id2 = Long.valueOf(ZongMenDataBase.qq2id.get(whos));
                if (id1 == id2) {
                    if (ConfirmController.Confirming.contains(who)) return "请先完成当前选项";
                    else {
                        try {
                            Method method = this.getClass().getDeclaredMethod("AttWhosNow", Long.class, Long.class, Group.class, Integer.class);
                            ConfirmController.RegConfirm(who, new Object[]{
                                    method, this, new Object[]{who, whos, group, 2}
                            });
                            return "即将攻击 宗门内成员 \r\n你确定要攻击ta吗？这将减少你的10点贡献点\r\n请在30秒内回复 确定/确认/取消";
                        } catch (NoSuchMethodException e) {
                            e.printStackTrace();
                            return "攻击异常";
                        }
                    }
                }
            } else {
                return AttWhosNow(who, whos, group, 1);
            }
        }
        return AttWhosNow(who, whos, group, 0);
    }

    public static final ExecutorService threads = Executors.newFixedThreadPool(10);

    public String AttWhosNow(Long p1, Long p2, Group g1, Integer v) {
        threads.execute(new Runnable() {
            private Group group = g1;
            private String tips;
            private long who = p1;
            private long whos = p2;
            private int i = v;

            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (i == 2) {
                    Zon zon = getZonInfo(who);
                    zon.setXper(zon.getXper() - 10);
                    putZonInfo(zon);
                }
                tips = "";
                PersonInfo iper = getInfo(who);
                PersonInfo yper = getInfo(whos);
                if (iper.getHp() > 10) {
                    if (yper.getHp() > 0) {
                        long l = randLong(iper.getHll(), 0.2f, 0.3f);
                        if (iper.getHl() >= l) {
                            long l1 = randLong(iper.getAtt(), 0.2f, 0.25f);
                            if (i == 1)
                                l1 *= 0.9f;
                            tips += GameDetailService.ConsumedHl(who, l);
                            tips += GameDetailService.Beaten(whos, who, l1);
                            if (!tips.contains("$"))
                                tips += GameDetailService.onAtt(who, whos, l1);
                            if (getInfo(whos).getHp() > 0) {
                                tips = "你对'" + getNameFromGroup(whos, group) + "'造成了" + l1 + " 点伤害\r\n消耗了" + l + "点魂力" + tips + (i == 1 ? "\r\n宗门护体 免疫10%外人的攻击" : "");
                            } else {
                                long lg = randLong(240, 0.6f, 0.9f);
                                putPerson(getInfo(who).addGold(lg));
                                tips = "你对'" + getNameFromGroup(whos, group) + "'造成了" + l1 + " 点伤害剩余了 0 点血 " + "\r\n消耗了" + l + "点魂力" + tips + "\r\n你获得了" + lg + "个金魂币";
                            }
                        } else {
                            tips = "魂力不足,攻击失败";
                        }
                    } else {
                        tips = "ta已无状态";
                    }
                } else {
                    tips = "血量不足,无法攻击";
                }
                send(tips);
            }

            private void send(String line) {
                MessageTools.sendMessageInGroup(At(who) + "\r\n" + tips, group.getId());
            }
        });
        return "准备攻击中...";

    }

    @Override
    public String getScoreFromGold(Long who, long num) {
        PersonInfo is = getInfo(who);
        if (is.getGold() <= 500)
            return "高出1000的金魂币才能换成积分";
        if (is.getGold() - 500 >= num) {
            DataBase.addScore((long) (num * 1.5f), who);
            putPerson(is.addGold(-num));
            return "转换成功\r\n获得" + (num * 1.5f) + "积分";
        } else {
            return getImageFromStrings("金魂币不足", "你需要" + (num + 500) + " 个金魂币", "才能换" + num * 1.5f + "积分");
        }
    }

    public String joinHh(Long who, Integer id) {
        if (id > 200 && id < 250) {
            PersonInfo personInfo = getInfo(who);
            if (isJTop(who)) {
                if (randHh(id, who, personInfo.getLevel())) {
                    GameDataBase.removeFromBgs(who, id, ObjType.use);
                    GameDataBase.addHh(who, id);
                    String str = upTrue(who) + "\r\n" + upTrue(who);
                    putPerson(getInfo(who).addLevel(2).setXp(0L));
                    return "吸收成功!!提升两级\r\n" + getImageFromStrings(str.split("\r\n")) + showHh(who);
                } else {
                    putPerson(personInfo.addLevel(-1).addHp(-personInfo.getHp() / 2));
                    return "吸收魂环失败,魂力等级下降1级,血量下降一半";
                }
            } else {
                return "未到达等级瓶颈,,经验未能达到升级,无法吸收魂环";
            }
        } else {
            return "错误吸收";
        }
    }

    private boolean randHh(Integer id, long who, int level) {
        if ((id >= 204 && level < 10) || (id >= 205 && level < 50) || (id >= 206 && level < 70)) {
            return false;
        }
        int r1 = rand.nextInt(10);

        PersonInfo personInfo = getInfo(who);
        if (personInfo.getLevel() > 10) r1--;
        if (personInfo.getLevel() > 30) r1--;
        if (personInfo.getLevel() > 45) r1--;
        if (personInfo.getLevel() > 60) r1--;
        if (personInfo.getLevel() > 90) r1--;
        if (personInfo.getLevel() > 120) r1--;

        switch (id) {
            case 201:
                return true;
            case 202:
                return r1 < 9;
            case 203:
                return r1 < 8;
            case 204:
                return r1 < 7;
            case 205:
                return r1 < 4;
            case 206:
                return r1 < 2;
            case 207:
                return r1 < 1;
            default:
                return false;
        }
    }

    private long HF_Hp(Long who) {
        PersonInfo personInfo = getInfo(who);
        long hpl = personInfo.getHpl();
        long hp = personInfo.getHp();
        if (hp >= hpl)
            return 0;
        if (hp > hpl) {
            putPerson(personInfo.setHp(personInfo.getHpl()));
            return hpl - hp;
        }
        int c1 = getRandXl(personInfo.getLevel());
        if (c1 > 30) c1 = 30;
        if (c1 < 4) c1 = 4;
        long l5 = personInfo.getHpl() / c1;
        l5 += randLong(l5, 0.5f, 0.6f);
        if ((hpl - hp) < l5) {
            l5 = (hpl - hp);
        }
        putPerson(personInfo.addHp(l5));
        return l5;
    }

    private long HF_Hl(Long who) {
        PersonInfo personInfo = getInfo(who);
        long vL = personInfo.getHll();
        long v = personInfo.getHl();
        if (v >= vL)
            return 0;
        if (v > vL) {
            putPerson(personInfo.setHl(personInfo.getHll()));
            return vL - v;
        }
        int c1 = getRandXl(personInfo.getLevel());
        if (c1 > 24) c1 = 24;
        if (c1 < 4) c1 = 4;
        long l5 = personInfo.getHll() / c1;
        l5 += randLong(l5, 0.5f, 0.7f);
        if ((vL - v) < l5) {
            l5 = (vL - v);
        }
        putPerson(personInfo.addHl(l5));
        return l5;
    }

    private long HF_Hj(Long who) {
        PersonInfo personInfo = getInfo(who);
        long vL = personInfo.getHjL();
        long v = personInfo.getHj();
        if (v >= vL)
            return 0;
        if (v > vL) {
            putPerson(personInfo.setHj(personInfo.getHjL()));
            return vL - v;
        }
        int c1 = getRandXl(personInfo.getLevel());
        if (c1 > 20) c1 = 20;
        if (c1 < 8) c1 = 8;
        long l5 = personInfo.getHjL() / c1;
        l5 += randLong(l5, 0.6f, 0.9f);
        if ((vL - v) < l5) {
            l5 = (vL - v);
        }
        putPerson(personInfo.addHj(l5));
        return l5;
    }

    private long HF_Hp(Long who, double d1) {
        PersonInfo personInfo = getInfo(who);
        long hpl = personInfo.getHpl();
        long hp = personInfo.getHp();
        if (hp >= hpl)
            return 0;
        if (hp > hpl) {
            putPerson(personInfo.setHp(personInfo.getHpl()));
            return hpl - hp;
        }
        int c1 = getRandXl(personInfo.getLevel());
        if (c1 > 30) c1 = 30;
        if (c1 < 4) c1 = 4;
        long l5 = personInfo.getHpl() / c1;
        l5 += randLong(l5, 0.5f, 0.6f);
        l5 *= 1.1;
        if ((hpl - hp) < l5) {
            l5 = (hpl - hp);
        }
        putPerson(personInfo.addHp(l5));
        return l5;
    }

    private long HF_Hl(Long who, double d1) {
        PersonInfo personInfo = getInfo(who);
        long vL = personInfo.getHll();
        long v = personInfo.getHl();
        if (v >= vL)
            return 0;
        if (v > vL) {
            putPerson(personInfo.setHl(personInfo.getHll()));
            return vL - v;
        }
        int c1 = getRandXl(personInfo.getLevel());
        if (c1 > 24) c1 = 24;
        if (c1 < 4) c1 = 4;
        long l5 = personInfo.getHll() / c1;
        l5 += randLong(l5, 0.5f, 0.7f);
        l5 *= 1.1;
        if ((vL - v) < l5) {
            l5 = (vL - v);
        }
        putPerson(personInfo.addHl(l5));
        return l5;
    }

    private long HF_Hj(Long who, double d1) {
        PersonInfo personInfo = getInfo(who);
        long vL = personInfo.getHjL();
        long v = personInfo.getHj();
        if (v >= vL)
            return 0;
        if (v > vL) {
            putPerson(personInfo.setHj(personInfo.getHjL()));
            return vL - v;
        }
        int c1 = getRandXl(personInfo.getLevel());
        if (c1 > 20) c1 = 20;
        if (c1 < 8) c1 = 8;
        long l5 = personInfo.getHjL() / c1;
        l5 += randLong(l5, 0.6f, 0.9f);
        l5 *= 1.1;
        if ((vL - v) < l5) {
            l5 = (vL - v);
        }
        putPerson(personInfo.addHj(l5));
        return l5;
    }

    @Override
    public String makeSname(Long who, String name, Group group) {
        if (isIlleg(name)) return "存在敏感字符";
        if (name.length() > 4) return "名字过长最大4个长度";
        if (name.endsWith("斗罗")) name = name.replace("斗罗", "");
        PersonInfo personInfo = getInfo(who);
        if (personInfo.getLevel() >= 90) {
            try {
                if (personInfo.getMk1() > System.currentTimeMillis())
                    return "修改称号 冷却中..." + getTimeDDHHMM(personInfo.getMk1());
                if (ConfirmController.Confirming.contains(who))
                    return "请先完成 当前选项";
                Method method = this.getClass().getDeclaredMethod("makeName", Long.class, String.class, Group.class);
                ConfirmController.RegConfirm(who, new Object[]{
                        method, this, new Object[]{who, name, group}
                });
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            return "确定 要取名封号 =》' " + name + " ' 吗 \r\n请在30秒内发送 确定/确认/取消 \r\n 注意，这将很久才能更改";
        } else {
            return "未达到 封号斗罗境界 无法 取名封号";
        }
    }

    public String makeName(Long who, String name, Group group) {
        putPerson(getInfo(who).setSname(name).setMk1(System.currentTimeMillis() + 1000 * 60 * 60 * 36));
        return info(who);
    }

    @Override
    public String returnA(long id) {
        Method method = null;
        try {
            method = this.getClass().getDeclaredMethod("ReturnNow", Long.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        ConfirmController.RegConfirm(id, new Object[]{
                method, this, new Object[]{id}
        });

        return "你确定需要转生吗,这将丢失所有数据(除积分之外)\r\n请在30秒内回复=>确定/取消";
    }

    private String ReturnNow(Long id) {
        histInfos.remove(id);
        boolean k1 = deleteDir(new File(GameDataBase.path + "/dates/users/" + id));
        boolean k2 = SkillDataBase.remove(id);
        return k1 && k2 ? "转生成功" : "转生失败";
    }

    @Override
    public String Fusion(Long q1, Long q2, Group group) {
        if (containsInBg(111, q1)) {
            try {
                if (q1.longValue() == q2.longValue()) return "Cant Do this";
                if (sameTypeWh(q1, q2))
                    return "武魂种类相同,不能融合";
                if (ConfirmController.Agreeing.contains(q2))
                    return "ta正在被处于同意/不同意";
                Method method = this.getClass().getDeclaredMethod("FusionNow", Long.class, Long.class);
                Object[] objects = new Object[]{
                        method, this, new Object[]{q1, q2}
                };
                ConfirmController.RegAgree(q2, objects);
                return MemberTools.getNameFromGroup(q2, group) + "\r\n请在30秒内回复\r\n同意/不同意";
            } catch (Exception e) {
                e.printStackTrace();
                return "融合异常";
            }
        } else
            return "您没有 融合戒指";
    }

    private boolean sameTypeWh(Long q1, Long q2) {
        PersonInfo p1 = getInfo(q1);
        PersonInfo p2 = getInfo(q2);
        if (p1.getWh() == 0 || p2.getWh() == 0) return false;
        if (p1.getWhType().intValue() == 2 || p2.getWhType().intValue() == 2) return false;
        return getInfo(q1).getWhType().longValue() == getInfo(q2).getWhType().longValue();
    }

    public Object FusionNow(Long q1, Long q2) {
        Warp warp1 = getWarp(q1);
        Warp warp2 = getWarp(q2);
        if (warp1.getBindQ().longValue() != -1 || warp2.getBindQ().longValue() != -1) {
            return "请先解除武魂融合";
        }
        removeFromBgs(q1, 111, ObjType.use);
        warp1.setBindQ(q2);
        warp2.setBindQ(q1);
        setWarp(warp1);
        setWarp(warp2);
        return "融合完成";
    }

    @Override
    public String detailInfo(long q) {
        GInfo gInfo = GInfo.getInstance(q);
        return pathToImg(drawGInfopPng(gInfo));
    }

    @Override
    public String shouTu(long q, long q2) {
        if (getWarp(q).getPrentice().longValue() != -1)
            return "你已经有徒弟了";
        if (getWarp(q2).getMaster().longValue() != -1)
            return "他已经有师傅了";
        GInfo gInfo = GInfo.getInstance(q);
        if (gInfo.getMasterPoint() < 25)
            return "名师点不足";
        try {
            ConfirmController.RegAgree(q2, new Object[]{
                    this.getClass().getDeclaredMethod("shouTuNow", long.class, long.class),
                    this, new Object[]{q, q2}
            });
            return "收徒ing...\n\'" + MemberTools.getName(q2) + "\'请在30秒内回复同意/不同意";
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return "未知异常";
    }

    public String shouTuNow(long q, long q2) {
        Warp warp1 = getWarp(q);
        Warp warp2 = getWarp(q2);
        warp1.setPrentice(q2);
        warp2.setMaster(q);
        setWarp(warp1);
        setWarp(warp2);
        GInfo.getInstance(q).addMasterPoint(-25).apply();
        return pathToImg(drawWarpPng(warp1));
    }

    @Override
    public String chuShi(long q) {
        if (getWarp(q).getMaster().longValue() == -1)
            return "您没有师傅";
        try {
            ConfirmController.RegConfirm(q, new Object[]{
                    this.getClass().getDeclaredMethod("chuShiNow", long.class),
                    this, new Object[]{q}
            });
            return "您确定要解除吗?\r\n请在30秒内回复\r\n确定/取消";
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return "未知异常";
    }

    public String chuShiNow(long q) {
        Warp warp1 = getWarp(q);
        Warp warp2 = getWarp(warp1.getMaster());
        warp1.setMaster(-1);
        warp2.setPrentice(-1);
        setWarp(warp1);
        setWarp(warp2);
        return pathToImg(drawWarpPng(warp1));
    }
}
