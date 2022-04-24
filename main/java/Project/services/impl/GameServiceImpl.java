package Project.services.impl;


import Project.aSpring.SpringBootResource;
import Project.controllers.auto.ConfirmController;
import Project.controllers.gameControllers.GameController;
import Project.controllers.gameControllers.GameController2;
import Project.dataBases.DataBase;
import Project.dataBases.GameDataBase;
import Project.dataBases.SourceDataBase;
import Project.dataBases.ZongMenDataBase;
import Project.dataBases.skill.SkillDataBase;
import Project.interfaces.Iservice.IGameService;
import Project.services.detailServices.GameDetailService;
import Project.services.player.PlayerBehavioralManager;
import io.github.kloping.MySpringTool.annotations.AutoStand;
import io.github.kloping.MySpringTool.annotations.Entity;
import io.github.kloping.mirai0.Main.ITools.MemberTools;
import io.github.kloping.mirai0.Main.ITools.MessageTools;
import io.github.kloping.mirai0.commons.*;
import io.github.kloping.mirai0.commons.broadcast.enums.ObjType;
import io.github.kloping.mirai0.commons.gameEntitys.SoulBone;
import io.github.kloping.mirai0.commons.gameEntitys.Zon;
import io.github.kloping.mirai0.commons.gameEntitys.base.BaseInfoTemp;
import io.github.kloping.mirai0.unitls.Tools.Tool;
import io.github.kloping.mirai0.unitls.drawers.Drawer;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static Project.controllers.auto.ControllerSource.challengeDetailService;
import static Project.dataBases.GameDataBase.*;
import static Project.dataBases.ZongMenDataBase.getZonInfo;
import static Project.dataBases.ZongMenDataBase.putZonInfo;
import static Project.services.detailServices.roles.BeatenRoles.THIS_DANGER_OVER_FLAG;
import static io.github.kloping.mirai0.Main.ITools.MemberTools.getNameFromGroup;
import static io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet.FinalFormat.TXL_WAIT_TIPS;
import static io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet.FinalFormat.XL_WAIT_TIPS;
import static io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet.FinalNormalString.ATTACK_BREAK;
import static io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet.FinalString.*;
import static io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet.FinalValue.MAX_LEVEL;
import static io.github.kloping.mirai0.unitls.Tools.GameTool.*;
import static io.github.kloping.mirai0.unitls.Tools.Tool.*;
import static io.github.kloping.mirai0.unitls.drawers.Drawer.*;

/**
 * @author github-kloping
 */
@Entity
public class GameServiceImpl implements IGameService {

    public static final ExecutorService threads = Executors.newFixedThreadPool(10);
    public int st = 24;
    @AutoStand
    GameController gameController;
    @AutoStand
    GameController2 gameController2;

    @Override
    public String xl(Long who) {
        PersonInfo is = getInfo(who);
        if (is.getWh() == 0 && is.getLevel() >= 2) {
            return "请先觉醒武魂 ";
        }
        long l = getK1(who);
        long now = System.currentTimeMillis();
        if (now >= l) {
            int tr = RANDOM.nextInt(6) + 9;
            int c = (getRandXl(getInfo(who).getLevel()));
            long mx = is.getXpL();
            long xr = mx / c;
            is.addXp(xr).setK1(now + (tr * 1000 * 60));
            putPerson(is);
            long ll1 = hfHp(who, 1.0f);
            long ll2 = hfHl(who, 1.0f);
            long ll3 = hfHj(who, 1.0f);
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
                    SourceDataBase.getImgPathById(is.getWh()) + getImageFromStrings(sb.toString().split(","));
        } else {
            return String.format(XL_WAIT_TIPS, getTimeTips(l));
        }
    }

    @Override
    public String xl2(long who) {
        PersonInfo is = getInfo(who);
        if (is.getWh() == 0 && is.getLevel() >= 2) {
            return PLEASE_AWAKENING_WH;
        }
        long l = getK1(who);
        long now = System.currentTimeMillis();
        if (now >= l) {
            int tr = RANDOM.nextInt(9) + 6;
            int c = (getRandXl(getInfo(who).getLevel()));
            long mx = is.getXpL();
            long xr = mx / c;
            xr *= 1.1;
            is.addXp(xr).setK1(now + (tr * 1000 * 60));
            putPerson(is);

            long ll1 = hfHp(who, 1.1);
            long ll2 = hfHl(who, 1.1);
            long ll3 = hfHj(who, 1.1);

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
                    SourceDataBase.getImgPathById(is.getWh()) + getImageFromStrings(sb.toString().split(","));
        } else {
            return String.format(TXL_WAIT_TIPS, getTimeTips(l));
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
        if (n == 0) {
            str1.append("你的武魂:暂未获得").append("\r\n");
        } else {
            str1.append("你的武魂:" + GameDataBase.getNameById(n)).append("\r\n");
            str1.append(SourceDataBase.getImgPathById((int) n)).append("\r\n");
        }
        return str1 + pathToImg(Drawer.drawInfo(is));
    }

    @Override
    public String upUp(Long who) {
        PersonInfo is = getInfo(who);
        long xp = is.getXp();
        long xpL = is.getXpL();
        long L = is.getLevel();
        if (L != 2 || getInfo(who).getWh() != 0) {
            if (xp >= xpL) {
                if (L > MAX_LEVEL) {
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
        } else {
            return PLEASE_AWAKENING_WH;
        }
    }

    @Override
    public String upTrue(Long who) {
        StringBuilder sb = new StringBuilder();
        PersonInfo personInfo = getInfo(who);

        int L = personInfo.getLevel();
        if (SpringBootResource.getUpupMapper().select(who, L) != null) {
            return "在该等级升级过\r\n不增加属性";
        }
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

        personInfo.addGold(50L, new TradingRecord()
                .setType1(TradingRecord.Type1.lost)
                .setType0(TradingRecord.Type0.gold)
                .setTo(-1)
                .setMain(who)
                .setFrom(who)
                .setDesc("升级")
                .setMany(50L)
        );

        if (L == 1) {
            sb.append("\r\n请觉醒武魂后再进行修炼(觉醒)");
        }

        sb.append("\r\n当前等级:").append(personInfo.getLevel());
        SpringBootResource.getUpupMapper().insert(who, L);
        putPerson(personInfo);

        return sb.toString();
    }

    @Override
    public String openEyeWh(Long who) {
        PersonInfo is = getInfo(who);
        if (is.getWh() != 0) {
            return AWAKENED_WH;
        }
        long level = is.getLevel();
        if (level < 2) {
            return LEVEL2_AWAKENING_WH_TIPS;
        }
        int r = RANDOM.nextInt(31) + 1;
        putPerson(is.setWh(r));
        return AWAKENING_WH_SUCCEED;
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
    public String buyGold(Long who, long num) {
        long is = DataBase.getAllInfo(who).getScore();
        if (is >= num * 2) {
            DataBase.addScore(-(num * 2), who);
            putPerson(getInfo(who).addGold(num,
                    new TradingRecord()
                            .setType1(TradingRecord.Type1.add)
                            .setType0(TradingRecord.Type0.gold)
                            .setTo(-1)
                            .setMain(who)
                            .setFrom(who)
                            .setDesc("购买金魂币" + num)
                            .setMany(num)
            ));
            return BUY_SUCCESS;
        } else {
            return getImageFromStrings("积分不足", "你需要" + num * 2 + "积分", "才能购买" + num + "个金魂币");
        }
    }

    @Override
    public String showHh(Long who) {
        Integer[] iii = getHhs(who);
        if (iii.length <= 0) {
            return "暂无魂环";
        } else {
            try {
                String f0 = drawHh(getInfo(who.longValue()).getWh(), Arrays.asList(iii));
                return Tool.pathToImg(f0);
            } catch (Exception e) {
                e.printStackTrace();
                return "魂环画显示异常";
            }
        }
    }

    @Override
    public String parseHh(Long who, int id) {
        if (challengeDetailService.isTemping(who)) {
            return CHALLENGE_ING;
        }
        try {
            List<Integer> bgids = new ArrayList<>(Arrays.asList(GameDataBase.getBgs(who)));
            if (bgids.contains(id)) {
                return joinHh(who, id);
            } else {
                return "你的背包里没有" + getNameById(id);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "未找到相关";
        }
    }

    @Override
    public String att(Long who, Long q2, Group group) {
        if (challengeDetailService.isTemping(q2)) {
            return CHALLENGE_ING;
        }
        if (ZongMenDataBase.qq2id.containsKey(q2)) {
            if (ZongMenDataBase.qq2id.containsKey(who)) {
                Long id1 = Long.valueOf(ZongMenDataBase.qq2id.get(who));
                Long id2 = Long.valueOf(ZongMenDataBase.qq2id.get(q2));
                if (id1.equals(id2)) {
                    try {
                        Method method = this.getClass().getDeclaredMethod("attNow", Long.class, Long.class, Group.class, Integer.class);
                        ConfirmController.regConfirm(who,
                                method, this, new Object[]{who, q2, group, 2}
                        );
                        return "即将攻击 宗门内成员 \r\n你确定要攻击ta吗？这将减少你的10点贡献点\r\n请在30秒内回复 确定/确认/取消";
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                        return "攻击异常";
                    }
                }
            } else {
                return attNow(who, q2, group, 1);
            }
        }
        return attNow(who, q2, group, 0);
    }

    @AutoStand
    PlayerBehavioralManager behavioral;

    /**
     * @param p1 攻击者
     * @param p2 被攻击者
     * @param g1 所处gid
     * @param v  type
     * @return
     */
    public String attNow(Long p1, Long p2, Group g1, Integer v) {
        Future future = threads.submit(new Runnable() {
            private Group group = g1;
            private String tips;
            private long who = p1;
            private long q2 = p2;
            private int i = v;

            @Override
            public void run() {
                try {
                    Thread.sleep(behavioral.getAttPre(p1));
                    if (i == 2) {
                        Zon zon = getZonInfo(who);
                        zon.setXper(zon.getXper() - 10);
                        putZonInfo(zon);
                    }
                    tips = "\n";
                    PersonInfo iper = getInfo(who);
                    PersonInfo yper = getInfo(q2);
                    if (iper.getHp() > 10) {
                        if (yper.getHp() > 0) {
                            long l = randLong(iper.getHll(), 0.2f, 0.3f);
                            if (iper.getHl() >= l) {
                                long l1 = randLong(iper.att(), 0.2f, 0.25f);
                                if (i == 1) {
                                    l1 *= 0.9f;
                                }
                                tips += GameDetailService.consumedHl(who, l);
                                tips += GameDetailService.beaten(q2, who, l1);
                                if (!tips.contains(THIS_DANGER_OVER_FLAG)) {
                                    tips += GameDetailService.onAtt(who, q2, l1);
                                }
                                if (getInfo(q2).getHp() > 0) {
                                    tips = "\n你对'" + getNameFromGroup(q2, group) + "'造成了" + l1 + " 点伤害\r\n消耗了" + l + "点魂力\n" + tips + (i == 1 ? "\r\n宗门护体 免疫10%外人的攻击" : "");
                                } else {
                                    long lg = randLong(240, 0.6f, 0.9f);
                                    putPerson(getInfo(who).addGold(lg,
                                            new TradingRecord()
                                                    .setType1(TradingRecord.Type1.add)
                                                    .setType0(TradingRecord.Type0.gold)
                                                    .setTo(-1)
                                                    .setMain(who)
                                                    .setFrom(who)
                                                    .setDesc("击败" + q2)
                                                    .setMany(lg)

                                    ));
                                    tips = "\n你对'" + getNameFromGroup(q2, group) + "'造成了" + l1 + " 点伤害剩余了 0 点血 " + "\r\n消耗了" + l + "点魂力\n" + tips + "\r\n你获得了" + lg + "个金魂币";
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
                } catch (InterruptedException e) {
                    send(ATTACK_BREAK);
                }
            }

            private void send(String line) {
                MessageTools.sendMessageInGroup(at(who) + "\r\n" + line, group.getId());
            }
        });
        BaseInfoTemp.append(p1, future);
        return "准备攻击中...";
    }

    @Override
    public String getScoreFromGold(Long who, long num) {
        PersonInfo is = getInfo(who);
        if (is.getGold() <= 500) {
            return "高出1000的金魂币才能换成积分";
        }
        if (is.getGold() - 500 >= num) {
            DataBase.addScore((long) (num * 1.5f), who);
            putPerson(is.addGold(-num,
                    new TradingRecord()
                            .setType1(TradingRecord.Type1.lost)
                            .setType0(TradingRecord.Type0.gold)
                            .setTo(-1)
                            .setMain(who)
                            .setFrom(who)
                            .setDesc("换积分" + num)
                            .setMany(num)
            ));
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
                    putPerson(getInfo(who).addLevel(1).setXp(0L));
                    String str = upTrue(who);
                    if (id <= 202) {
                        return "吸收成功!!提升一级\r\n" + getImageFromStrings(str.split("\r\n")) + showHh(who);
                    } else {
                        putPerson(getInfo(who).addLevel(1).setXp(0L));
                        str += "\n" + upTrue(who);
                        return "吸收成功!!提升两级\r\n" + getImageFromStrings(str.split("\r\n")) + showHh(who);
                    }
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
        boolean k1 = (id >= 204 && level < 10);
        boolean k2 = (id >= 205 && level < 50);
        boolean k3 = (id >= 206 && level < 70);
        if (k1 || k2 || k3) {
            return false;
        }
        int r1 = RANDOM.nextInt(10);
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

    private long hfHp(Long who, double d1) {
        PersonInfo personInfo = getInfo(who);
        long hpl = personInfo.getHpL();
        long hp = personInfo.getHp();
        if (hp >= hpl)
            return 0;
        if (hp > hpl) {
            putPerson(personInfo.setHp(personInfo.getHpL()));
            return hpl - hp;
        }
        int c1 = getRandXl(personInfo.getLevel());
        if (c1 > 30) c1 = 30;
        if (c1 < 4) c1 = 4;
        long l5 = personInfo.getHpL() / c1;
        l5 += randLong(l5, 0.5f, 0.6f);
        l5 *= d1;
        if ((hpl - hp) < l5) {
            l5 = (hpl - hp);
        }
        putPerson(personInfo.addHp(l5));
        return l5;
    }

    private long hfHl(Long who, double d1) {
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
        l5 *= d1;
        if ((vL - v) < l5) {
            l5 = (vL - v);
        }
        putPerson(personInfo.addHl(l5));
        return l5;
    }

    private long hfHj(Long who, double d1) {
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
        l5 *= d1;
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
                Method method = this.getClass().getDeclaredMethod("makeName", Long.class, String.class, Group.class);
                ConfirmController.regConfirm(who,
                        method, this, new Object[]{who, name, group});
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
            method = this.getClass().getDeclaredMethod("returnNow", Long.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        ConfirmController.regConfirm(id, method, this, new Object[]{id});
        return "你确定需要转生吗,这将丢失所有信息数据(除积分之外)\r\n请在30秒内回复=>确定/取消";
    }

    private String returnNow(Long id) {
        HIST_INFOS.remove(id);
        Warp warp = null;
        int i = 0;
        try {
            SpringBootResource.getPersonInfoMapper().deleteById(id.longValue());
            i++;
        } catch (Exception e) {e.printStackTrace();}
        try {
            SkillDataBase.remove(id);
            i++;
        } catch (Exception e) {e.printStackTrace();}
        try {
            warp = getWarp(id);
            if (warp.getMaster().longValue() != -1) {
                chuShiNow(id.longValue());
            }
            i++;
        } catch (Exception e) {e.printStackTrace();}
        try {
            if (warp.getBindQ().longValue() != -1) {
                gameController2.removeFusionNow(id.longValue());
            }
            i++;
        } catch (Exception e) {e.printStackTrace();}
        try {
            for (Integer id0 : SpringBootResource.getBagMapper().selectAllIds(id.longValue())) {
                SpringBootResource.getBagMapper().update(id0);
            }
            i++;
        } catch (Exception e) {e.printStackTrace();}
        try {
            for (Map<String, Integer> map : SpringBootResource.getAqBagMapper().selectAq(id)) {
                SpringBootResource.getAqBagMapper().update(map.get("num"), 1, map.get("id").intValue());
            }
            i++;
        } catch (Exception e) {e.printStackTrace();}
        try {
            for (SoulBone soulBone : SpringBootResource.getSoulBoneMapper().selectBons(id)) {
                SpringBootResource.getSoulBoneMapper().delete(soulBone);
            }
            i++;
        } catch (Exception e) {e.printStackTrace();}
        try {
            SpringBootResource.getgInfoMapper().deleteById(id);
            i++;
        } catch (Exception e) {e.printStackTrace();}
        try {
            for (Integer integer : SpringBootResource.getHhpzMapper().selectIds(id.longValue())) {
                SpringBootResource.getHhpzMapper().delete(integer);
            }
            i++;
        } catch (Exception e) {e.printStackTrace();}
        try {
            SpringBootResource.getTaskPointMapper().deleteById(id);
            i++;
        } catch (Exception e) {e.printStackTrace();}
        try {
            SpringBootResource.getUpupMapper().deleteByQq(id);
            i++;
        } catch (Exception e) {e.printStackTrace();}
        return "转生完成\n已移除" + i + "项记录";
    }

    @Override
    public String fusion(Long q1, Long q2, Group group) {
        if (containsInBg(111, q1)) {
            try {
                if (q1.longValue() == q2.longValue()) return "Cant Do this";
                if (sameTypeWh(q1, q2))
                    return "武魂种类相同,不能融合";
                Method method = this.getClass().getDeclaredMethod("fusionNow", Long.class, Long.class);
                ConfirmController.regAgree(q2, method, this, q1, q2);
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
        if (p1.getWh() <= 0 || p2.getWh() <= 0) return false;
        if (p1.getWhType().intValue() == 2 || p2.getWhType().intValue() == 2) return false;
        return p1.getWhType().intValue() == p2.getWhType().intValue();
    }

    public Object fusionNow(Long q1, Long q2) {
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
        return pathToImg(drawGInfo(gInfo));
    }

    @Override
    public String shouTu(long q, long q2) {
        if (getWarp(q).getPrentice().longValue() != -1)
            return "你已经有徒弟了";
        if (getWarp(q2).getMaster().longValue() != -1)
            return "他已经有师傅了";
        if ((getWarp(q).getMaster().longValue() == q2) || (getWarp(q2).getPrentice().longValue() == q))
            return "can't do that";
        GInfo gInfo = GInfo.getInstance(q);
        if (gInfo.getMasterPoint() < st)
            return "名师点不足:需要=>" + st + "\n现在:" + gInfo.getMasterPoint();
        try {
            ConfirmController.regAgree(q2, this.getClass().getDeclaredMethod("shouTuNow", long.class, long.class), this, q, q2);
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
        GInfo.getInstance(q).addMasterPoint(-st).apply();
        return pathToImg(drawWarp(warp1));
    }

    @Override
    public String chuShi(long q) {
        if (getWarp(q).getMaster().longValue() == -1)
            return "您没有师傅";
        try {
            ConfirmController.regConfirm(q,
                    this.getClass().getDeclaredMethod("chuShiNow", long.class),
                    this, new Object[]{q}
            );
            return "您确定要解除吗?\r\n请在30秒内回复\r\n确定/取消";
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return "未知异常";
    }

    public String chuShiNow(long q) {
        Warp warp1 = getWarp(q);
        Warp warp2 = getWarp(warp1.getMaster());
        warp1.setMaster(-1L);
        warp2.setPrentice(-1L);
        setWarp(warp1);
        setWarp(warp2);
        return pathToImg(drawWarp(warp1));
    }

    @Override
    public String upHh(long q, int st) {
        Integer[] ints = getHhs(q);
        int id;
        try {
            id = ints[st - 1];
        } catch (Exception e) {
            return "您没有对应的魂环";
        }
        int needId = -1;
        switch (id) {
            case 201:
                needId = 1602;
                break;
            case 202:
                needId = 1603;
                break;
            case 203:
                needId = 1604;
                break;
            case 204:
                needId = 1605;
                break;
            default:
                return "魂环等级过高无法升级";
        }
        if (containsInBg(needId, q)) {
            removeFromBgs(q, needId, ObjType.use);
            GameDataBase.upHh(q, st - 1, ++id);
            return showHh(q);
        } else {
            return "需要:" + getNameById(needId);
        }
    }

    @Override
    public String attByHj(long q, long q2, int br) {
        PersonInfo p1 = getInfo(q);
        br = br > 20 ? 20 : br;
        br = br < 1 ? 1 : br;
        if (p1.getHjL() == 0) {
            return "精神力不足";
        } else {
            int br0 = toPercent(p1.getHj(), p1.getHjL());
            if (br0 >= 30) {
                if (br0 >= br) {
                    String s1 = GameDetailService.onSpiritAttack(q, q2, br);
                    return "您发射了" + br + "%的精神力\n" + s1 + "\n";
                } else {
                    return "精神力不足" + br + "%或不足及发射指定比率的精神力";
                }
            } else {
                return "精神力不足30%或不足及发射指定比率的精神力";
            }
        }
    }
}
