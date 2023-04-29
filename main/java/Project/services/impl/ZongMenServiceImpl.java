package Project.services.impl;

import Project.aSpring.SpringBootResource;
import Project.commons.SpGroup;
import Project.commons.TradingRecord;
import Project.commons.gameEntitys.Zon;
import Project.commons.rt.ResourceSet;
import Project.controllers.auto.ConfirmController;
import Project.controllers.auto.ControllerTool;
import Project.dataBases.GameDataBase;
import Project.dataBases.ZongMenDataBase;
import Project.interfaces.Iservice.IZongMenService;
import Project.plugins.KlopingDetail;
import Project.plugins.NetMain;
import Project.services.detailServices.ZongDetailService;
import io.github.kloping.MySpringTool.annotations.AutoStand;
import io.github.kloping.MySpringTool.annotations.Entity;
import io.github.kloping.file.FileUtils;
import io.github.kloping.mirai0.Main.iutils.MemberUtils;
import io.github.kloping.mirai0.commons.PersonInfo;
import io.github.kloping.mirai0.commons.Zong;
import io.github.kloping.mirai0.unitls.Tools.Tool;
import net.mamoe.mirai.message.data.Message;
import net.mamoe.mirai.message.data.MessageChainBuilder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static Project.commons.rt.ResourceSet.FinalString.*;
import static Project.controllers.auto.TimerController.MORNING_RUNNABLE;
import static Project.controllers.gameControllers.zongmenContrller.ZongMenController.COB_CD;
import static Project.dataBases.GameDataBase.getInfo;
import static Project.dataBases.ZongMenDataBase.*;
import static io.github.kloping.mirai0.Main.BootstarpResource.BOT;
import static io.github.kloping.mirai0.unitls.Tools.GameTool.getFhName;
import static io.github.kloping.mirai0.unitls.drawers.Drawer.filterImg;
import static io.github.kloping.mirai0.unitls.drawers.Drawer.getImageFromStrings;

/**
 * @author github-kloping
 */
@Entity
public class ZongMenServiceImpl implements IZongMenService {

    public static final int GV = 50;

    static {
        MORNING_RUNNABLE.add(() -> {
            if (Tool.INSTANCE.getWeekOfDate(new Date()).equals(Tool.INSTANCE.WEEK_DAYS[Tool.INSTANCE.WEEK_DAYS.length - 1])) {
                List<Zong> zongs = SpringBootResource.getZongMapper().selectAllSortByActive();
                MessageChainBuilder builder = new MessageChainBuilder();
                if (zongs.size() > 0) {
                    Zong z1 = zongs.get(0);
                    builder.append("上周最活跃宗门:\n\t").append(z1.getName()).append(NEWLINE);
                    builder.append("活跃点数:").append(z1.getActive().toString()).append(NEWLINE);
                    builder.append("宗主奖励8000金魂币\n长老奖励5000金魂币\n成员奖励2000金魂币").append(NEWLINE);
                    reward(z1, 8000, 5000, 2000);
                }
                if (zongs.size() > 1) {
                    Zong z2 = zongs.get(1);
                    builder.append("上周次活跃宗门:\n\t").append(z2.getName()).append(NEWLINE);
                    builder.append("活跃点数:").append(z2.getActive().toString()).append(NEWLINE);
                    builder.append("宗主奖励6000金魂币\n长老奖励3500金魂币\n成员奖励1500金魂币").append(NEWLINE);
                    reward(z2, 60000, 3500, 1500);
                }
                SpringBootResource.getZongMapper().updateAll();
                SpringBootResource.getZonMapper().updateAll();
                Message message = builder.build();
                for (net.mamoe.mirai.contact.Group group : BOT.getGroups()) {
                    if (ControllerTool.opened(group.getId(), ZongMenServiceImpl.class)) {
                        group.sendMessage(message);
                    }
                }
            }
        });
    }

    @AutoStand
    KlopingDetail detail;

    private static void reward(Zong z1, long i, long i1, long i2) {
        long q1 = z1.getMain();
        long i0 = i - i2 - i1;
        getInfo(q1).addGold(i0, new TradingRecord().setFrom(-1).setMain(q1).setDesc("宗门奖励").setTo(q1).setMany(i0).setType0(TradingRecord.Type0.gold).setType1(TradingRecord.Type1.add));
        long i3 = i1 - i2;
        for (Number q : z1.getElder()) {
            getInfo(q.longValue()).addGold(i3, new TradingRecord().setFrom(-1).setMain(q.longValue()).setDesc("宗门奖励").setTo(q.longValue()).setMany(i3).setType0(TradingRecord.Type0.gold).setType1(TradingRecord.Type1.add));
        }
        for (Number q : z1.getMember()) {
            getInfo(q.longValue()).addGold(i2, new TradingRecord().setFrom(-1).setMain(q.longValue()).setDesc("宗门奖励").setTo(q.longValue()).setMany(i2).setType0(TradingRecord.Type0.gold).setType1(TradingRecord.Type1.add));
        }
    }

    public synchronized void addActivePoint(Long qid, int point) {
        if (qq2id.containsKey(qid)) {
            int id = qq2id.get(qid).intValue();
            Zong zong = getZongInfo(id);
            zong.setActive(zong.getActive() + point);
            putZongInfo(zong);
            Zon zon = getZonInfo(qid);
            zon.setActive(zon.getActive() + point);
            putZonInfo(zon);
        }
    }

    public long isJkOk(Long qq) {
        PersonInfo info = GameDataBase.getInfo(qq);
        if (info.getJk1() <= System.currentTimeMillis()) return 0;
        return info.getJk1();
    }

    @Override
    public String create(String name, Long who, SpGroup group) {
        if (Tool.INSTANCE.isIlleg(name)) return "存在敏感字符";
        if (name.length() > 4) return "名字过长最大4个长度";
        long t1 = -1;
        if ((t1 = isJkOk(who)) > 0) return "宗门活动冷却中...==>" + Tool.INSTANCE.getTimeDDHHMM(t1);
        //=================
        if (qq2id.containsKey(who)) return "你已经在宗门之中";
        PersonInfo info = GameDataBase.getInfo(who);
        if (info.getGold() < 500) return "创建,至少需要500金魂币才能出创建宗门";
        if (info.getLevel() < 50) return "威望不足，50级以上才可创建宗门";
        try {
            Method method = this.getClass().getDeclaredMethod("createNow", Long.class, String.class, SpGroup.class);
            method.setAccessible(true);
            ConfirmController.regConfirm(who, method, this, new Object[]{who, name, group});
            return "确定要创建宗门 ' " + name + " ' 吗,\r\n这将花费450金魂币\r\n请在30秒内回复(确定/确认/取消)";
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return "创建异常";
        }
    }

    public String createNow(Long who, String name, SpGroup group) {
        if (name == null || name.isEmpty() || NULL_LOW_STR.equals(name)) return ("创建异常..");
        ZongMenDataBase.createNewZong(who, name);
        (GameDataBase.getInfo(who).addGold(-450L, new TradingRecord().setType1(TradingRecord.Type1.lost).setType0(TradingRecord.Type0.gold).setTo(-1).setMain(who).setFrom(who).setDesc("创建宗门").setMany(450))).apply();
        GameDataBase.getInfo(who).setJk1(System.currentTimeMillis() + 1000 * 60 * 60 * 12).apply();
        return zongInfo(who, group);
    }

    @Override
    public String zongInfo(Long qq, SpGroup group) {
        if (qq2id.containsKey(qq)) {
            Integer id = qq2id.get(qq).intValue();
            return zongInfo(id, group);
        } else return ("你没有加入任何宗门");
    }

    @Override
    public String zongInfo(Zong zong, SpGroup group) {
        StringBuilder sb = new StringBuilder();
        String icon = zong.getIcon();
        sb.append("宗门名称:").append(zong.getName()).append("\r\n");
        sb.append("宗门等级:").append(zong.getLevel()).append("\r\n");
        sb.append("宗门经验:").append(zong.getXp()).append("/").append(zong.getXpMax()).append("\r\n");
        sb.append("宗门状态:").append(zong.getState() == 1 ? "正常运转" : "停止运转").append("\r\n");
        String name = "宗主";
        try {
            name = MemberUtils.getNameFromGroup(zong.getMain().longValue(), group);
        } catch (Exception e) {
            e.printStackTrace();
        }
        sb.append("宗门宗主:").append(name).append("(").append(zong.getMain()).append(")").append("\r\n");
        sb.append("宗门人数:").append(zong.getMembers()).append("/").append(zong.getMaxP()).append("\r\n");
        sb.append("宗门长老:").append(zong.getElders()).append("/").append(zong.getElderNum()).append("\r\n");
        return ((icon == null || icon.trim().isEmpty()) ? "" : Tool.INSTANCE.pathToImg(icon)) + getImageFromStrings(false, sb.toString().split("\\s+"));
    }

    @Override
    public String zongInfo(Integer id, SpGroup group) {
        Zong zong = getZongInfo(id);
        return zongInfo(zong, group);
    }

    @Override
    public String list(SpGroup group) {
        return getImageFromStrings(getAllZongNames());
    }

    @Override
    public String setIcon(String imageUrl, Long who, SpGroup group) {
        if (!qq2id.containsKey(who)) return ("你没有加入任何宗门");
        Zong zong = getZongInfo(who);
        Zon zon = getZonInfo(who);
        if (zon.getLevel() != 2) return ("仅宗主有权限修改宗门信息");
        if (zong.getMk() > System.currentTimeMillis())
            return ("宗门修改信息 冷却中 =>" + Tool.INSTANCE.getTimeDDHHMM(zong.getMk()));
        String path = "./temp/" + UUID.randomUUID() + ".jpg";
        downloadFile(imageUrl, path);
        File file = filterImg(new File(path));
        String fn = NetMain.ROOT_PATH + "/" + detail.uploadImg(file);
        zong.setIcon(fn).setMk(System.currentTimeMillis() + 1000 * 60 * 60 * 2);
        putZongInfo(zong);
        return zongInfo(who, group);
    }

    private void downloadFile(String imageUrl, String path) {
        try {
            File file = new File(path);
            file.getParentFile().mkdirs();
            file.createNewFile();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            InputStream is = new URL(imageUrl).openStream();
            byte[] bytes = new byte[1024 * 1024];
            int len = -1;
            while ((len = is.read(bytes)) != -1) {
                baos.write(bytes, 0, len);
            }
            is.close();
            FileUtils.writeBytesToFile(baos.toByteArray(), file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String invite(long who, long qq, SpGroup group) {
        if (!qq2id.containsKey(who)) return ("你没有加入任何宗门");
        if (qq2id.containsKey(qq)) return ("ta已经加入宗门");
        if (!GameDataBase.exist(qq)) return (PLAYER_NOT_REGISTERED);
        Zong zong = getZongInfo(who);
        Zon zon = getZonInfo(who);
        if (zon.getLevel() < 1) return ("仅宗主和长老有权限邀请成员");
        try {
            Method method = this.getClass().getDeclaredMethod("join", Long.class, Long.class, SpGroup.class);
            if (zong.getMembers() >= zong.getMaxP()) {
                return ("宗门已满");
            }
            ConfirmController.regAgree(qq, method, this, who, qq, group);
            return new StringBuilder().append("邀请成功\r\n").append(MemberUtils.getNameFromGroup(qq, group)).append("请在30秒内回复同意/不同意").toString();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return ("邀请异常");
        }
    }

    public String join(Long who, Long qq, SpGroup group) {
        if (GameDataBase.getInfo(qq).getJk1() > System.currentTimeMillis()) {
            return ("宗门活动冷却中...==>" + Tool.INSTANCE.getTimeDDHHMM(GameDataBase.getInfo(qq).getJk1()));
        }
        Zong zong = getZongInfo(who);
        (GameDataBase.getInfo(qq).setJk1(System.currentTimeMillis() + 1000 * 60 * 60 * 12)).apply();
        addPer(zong, qq);
        putZongInfo(zong);
        return new StringBuilder().append("加入成功!!\r\n").append(listPer(who, group)).toString();
    }

    @Override
    public String listPer(Long who, SpGroup group) {
        if (!qq2id.containsKey(who)) return ("你没有加入任何宗门");
        Zong zong = getZongInfo(who);
        StringBuilder sb = new StringBuilder();
        sb.append(zong.getName()).append("\r\n==================\r\n");
        int i = 1;
        for (Number z1 : zong.getMember()) {
            try {
                Zon zon = getZonInfo(z1.longValue());
                sb.append(i).append(":").append(getFhName(z1.longValue(), true)).append("(").append(zon.getLevel() == 1 ? "长老" : zon.getLevel() == 2 ? "宗主" : "").append("\r\n\t  ");
                sb.append(zon.getXper()).append("点贡献").append("\r\n");
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println(z1);
            }
            i++;
        }
        String end = getImageFromStrings(false, sb.toString().split("\r\n"));
        return end;
    }

    @Override
    public String setName(String name, long who, SpGroup group) {
        if (Tool.INSTANCE.isIlleg(name)) return ("存在敏感字符");
        if (!qq2id.containsKey(who)) return ("你没有加入任何宗门");
        Zong zong = getZongInfo(who);
        Zon zon = getZonInfo(who);
        if (zon.getLevel() == 0) return ("仅宗主和长老有权限修改宗门名字");
        if (zong.getMk() > System.currentTimeMillis())
            return ("宗门修改信息 冷却中 =>" + Tool.INSTANCE.getTimeDDHHMM(zong.getMk()));
        zong.setName(name).setMk(System.currentTimeMillis() + 1000 * 60 * 60 * 2);
        putZongInfo(zong);
        return zongInfo(who, group);
    }

    @Override
    public String cob(Long who) {
        if (!qq2id.containsKey(who)) return "你没有加入任何宗门";
        Zong zong = getZongInfo(who);
        Zon zon = getZonInfo(who);
        PersonInfo info = GameDataBase.getInfo(who);
        if (info.getGold() < info.getLevel()) return "金魂币不足";
        if (info.getCbk1() > System.currentTimeMillis())
            return "贡献时间未到 => " + Tool.INSTANCE.getTimeDDHHMM(info.getCbk1());
        zon.setXper(zon.getXper() + info.getLevel());
        zong.setXp(zong.getXp() + info.getLevel());
        info.setCbk1(System.currentTimeMillis() + 1000 * 60 * 60 * COB_CD);
        info.addGold((long) -info.getLevel(), new TradingRecord().setType1(TradingRecord.Type1.lost).setType0(TradingRecord.Type0.gold).setTo(-1).setMain(who).setFrom(who).setDesc("宗门贡献").setMany(info.getLevel()));
        putZonInfo(zon);
        putZongInfo(zong);
        (info).apply();
        return "贡献成功贡献" + info.getLevel() + "点";
    }

    @Override
    public String help(long qq, long who) {
        if (!qq2id.containsKey(qq)) return "你没有加入任何宗门";
        if (!qq2id.containsKey(who)) return "ta没有加入任何宗门";
        if (!GameDataBase.exist(who)) return PLAYER_NOT_REGISTERED;
        Long l1 = Long.valueOf(qq2id.get(qq) + "");
        Long l2 = Long.valueOf(qq2id.get(who) + "");
        if (l1 != l2) return "你们不在同一个宗门";
        Zong zong = getZongInfo(qq);
        Zon zon = getZonInfo(qq);
        PersonInfo info = GameDataBase.getInfo(qq);
        PersonInfo info1 = GameDataBase.getInfo(who);
        if (info1.getHp() > 0) return "ta仍有状态";
        if (zon.getTimes() > 0) {
            (info1.setHp(100L).addHl(100L)).apply();
            zon.setTimes(zon.getTimes() - 1);
            putZonInfo(zon);
            return "救援成功";
        } else {
            if (zong.getPub() > 0) {
                zong.setPub(zong.getPub() - 1);
                (info1.setHp(100L).addHl(100L)).apply();
                putZongInfo(zong);
                return "消耗一次公共机会,救援成功  剩余=>" + zong.getPub();
            } else {
                return "无救援次数";
            }
        }
    }

    @Override
    public String setElder(long id, long who) {
        String s1 = setUpElderPermission(id, who);
        if (!s1.isEmpty()) return s1;
        Zong zong = getZongInfo(id);
        Zong zong1 = getZongInfo(who);
        if (zong.getId().intValue() != zong1.getId().intValue()) return ILLEGAL_OPERATION;
        Zon zon = getZonInfo(id);
        Zon zon1 = getZonInfo(who);
        if (zong.getElders() >= zong.getElderNum()) {
            return "长老数量上线";
        } else {
            if (zon.getLevel() != 2) return "仅宗主可设置长老";
            else {
                if (zong.getElder().contains(who)) return "ta 本来就是长老";
                zong.setElders(zong.getElders() + 1);
                zong.getElder().add(who);
                zon1.setLevel(1);
                putZongInfo(zong);
                putZonInfo(zon1);
                return "设置成功";
            }
        }
    }

    private String setUpElderPermission(long id, long who) {
        if (!qq2id.containsKey(id)) return "你没有加入任何宗门";
        if (!qq2id.containsKey(who)) return "ta没有加入任何宗门";
        if (!GameDataBase.exist(who)) return PLAYER_NOT_REGISTERED;
        Long l1 = Long.valueOf(qq2id.get(who).toString());
        Long l2 = Long.valueOf(qq2id.get(who).toString());
        if (!l1.equals(l2)) return "你们不在同一个宗门";
        return "";
    }

    @Override
    public String cancelElder(long id, long who) {
        if (id == who) return ILLEGAL_OPERATION;
        String s1 = setUpElderPermission(id, who);
        if (!s1.isEmpty()) return s1;
        Zong zong = getZongInfo(id);
        Zong zong1 = getZongInfo(who);
        if (zong.getId().intValue() != zong1.getId().intValue()) return ILLEGAL_OPERATION;
        Zon zon = getZonInfo(id);
        Zon zon1 = getZonInfo(who);
        if (zon.getLevel() < 2) return "仅宗主可设置长老";
        else {
            if (!zong.getElder().contains(who)) return "ta 本来就不是长老";
            zong.setElders(zong.getElders() - 1);
            zong.getElder().remove(id);
            zon1.setLevel(0);
            putZongInfo(zong);
            putZonInfo(zon1);
            return "取消设置成功";
        }
    }

    @Override
    public String upUp(long id, SpGroup g) {
        if (!qq2id.containsKey(id)) return "你没有加入任何宗门";
        Zon zon = getZonInfo(id);
        if (zon.getLevel() != 2) return "仅宗主可升级";
        Zong zong = getZongInfo(id);
        if (zong.getXp() >= zong.getXpMax()) {
            switch (zong.getLevel()) {
                case 1:
                    zong.setXp(zong.getXp() - zong.getXpMax());
                    zong.setLevel(2);
                    zong.setElderNum(3);
                    zong.setMaxP(zong.getMaxP() + 6);
                    zong.setXpMax(1000L);
                    putZongInfo(zong);
                    return zongInfo(id, g);
                case 2:
                    zong.setXp(zong.getXp() - zong.getXpMax());
                    zong.setLevel(3);
                    zong.setElderNum(4);
                    zong.setMaxP(zong.getMaxP() + 8);
                    zong.setXpMax(4800L);
                    putZongInfo(zong);
                    return zongInfo(id, g);
                case 3:
                    zong.setXp(zong.getXp() - zong.getXpMax());
                    zong.setLevel(4);
                    zong.setElderNum(5);
                    zong.setMaxP(zong.getMaxP() + 8);
                    zong.setXpMax(8200L);
                    putZongInfo(zong);
                    return zongInfo(id, g);
                case 4:
                    zong.setXp(zong.getXp() - zong.getXpMax());
                    zong.setLevel(5);
                    zong.setElderNum(7);
                    zong.setMaxP(zong.getMaxP() + 9);
                    zong.setXpMax(12500L);
                    putZongInfo(zong);
                    return zongInfo(id, g);
                case 5:
                    zong.setXp(zong.getXp() - zong.getXpMax());
                    zong.setLevel(6);
                    zong.setElderNum(7);
                    zong.setMaxP(zong.getMaxP() + 10);
                    zong.setXpMax(54500L);
                    putZongInfo(zong);
                    return zongInfo(id, g);
                case 6:
                    return ("宗门最大等级");

            }
        } else {
            return ("宗门经验不足");
        }
        return ("升级异常");
    }

    @Override
    public String quite(long id) {
        if (!qq2id.containsKey(id)) return ("你没有加入任何宗门");
        Zon zon = getZonInfo(id);
        try {
            Method method = this.getClass().getDeclaredMethod("quiteNow", Long.class);
            ConfirmController.regConfirm(id, method, this, new Object[]{Long.valueOf(id)});
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return ("退出异常");
        }
        switch (zon.getLevel()) {
            case 0:
                return ("确定退出宗门吗? 请在30秒内回复(确定/确认/取消)");
            case 1:
                return ("确定退出宗门吗? \r\n 这将丢失你的长老身份 请在30秒内回复(确定/确认/取消)");
            case 2:
                return ("确定退出宗门吗? \r\n 这将解散你的宗门 请在30秒内回复(确定/确认/取消)");
        }
        return null;
    }

    public String quiteNow(Long id) {
        if (ZongDetailService.quite(id)) return (" 成功");
        else return (" 失败");
    }

    @Override
    public String quiteOne(long id, long who) {
        if (!qq2id.containsKey(id)) return "你没有加入任何宗门";
        Zong zong = getZongInfo(id);
        Zong zong1 = getZongInfo(who);
        if (zong.getId().intValue() != zong1.getId().intValue()) return ILLEGAL_OPERATION;
        Zon zon = getZonInfo(id);
        Zon zon1 = getZonInfo(who);
        if (zon.getLevel() != 2) return "仅宗主可移除成员";
        if (zon1.getLevel() > 1) return "不可以移除宗主或长老";
        try {
            Method method = this.getClass().getDeclaredMethod("quiteNow", Long.class);
            ConfirmController.regConfirm(id, method, this, new Object[]{who});
            return "确定要移除 ta 么? ";
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return "移除异常";
        }
    }

    @Override
    public String addMax(long id) {
        try {
            Zong zong = getZongInfo(id);
            Zon zon = getZonInfo(id);
            if (zon.getLevel() < 1) return "宗主,长老可扩增";
            int max = zong.getMaxP();
            max += 10;
            max *= GV;
            Method method = this.getClass().getDeclaredMethod("addMaxNow", long.class);
            ConfirmController.regConfirm(id, method, this, new Object[]{id});
            StringBuilder sb = new StringBuilder();
            sb.append("确定要扩增宗门吗\n扩增后最大人数增加到").append(zong.getMaxP() + 10).append("\n但将花费").append(max).append("金魂币");
            return sb.toString();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Object addMaxNow(long qid) {
        Zong zong = getZongInfo(qid);
        int max = zong.getMaxP();
        max += 10;
        max *= GV;
        PersonInfo personInfo = getInfo(qid);
        if (personInfo.getGold() >= max) {
            personInfo.addGold((long) -max, new TradingRecord().setType1(TradingRecord.Type1.lost).setType0(TradingRecord.Type0.gold).setTo(-1).setMain(qid).setFrom(qid).setDesc("扩建宗门").setMany(max));
            personInfo.apply();
            zong.setMaxP(zong.getMaxP() + 10);
            ZongMenDataBase.putZongInfo(zong);
            return "完成";
        } else {
            return ResourceSet.FinalString.NOT_ENOUGH_GOLD;
        }
    }
}