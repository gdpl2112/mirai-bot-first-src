package Project.services.impl;

import Project.controllers.auto.ConfirmController;
import Project.dataBases.GameDataBase;
import Project.dataBases.ZongMenDataBase;
import Project.interfaces.Iservice.IZongMenService;
import Project.services.detailServices.ZongDetailService;
import io.github.kloping.MySpringTool.annotations.Entity;
import io.github.kloping.mirai0.Entitys.Group;
import io.github.kloping.mirai0.Entitys.TradingRecord;
import io.github.kloping.mirai0.Entitys.gameEntitys.PersonInfo;
import io.github.kloping.mirai0.Entitys.gameEntitys.Zon;
import io.github.kloping.mirai0.Entitys.gameEntitys.Zong;
import io.github.kloping.mirai0.Main.ITools.MemberTools;

import java.io.File;
import java.lang.reflect.Method;

import static Project.ResourceSet.FinalString.NULL_LOW_STR;
import static Project.ResourceSet.FinalString.PLAYER_NOT_REGISTERED;
import static Project.controllers.GameControllers.ZongmenContrller.ZongMenController.COB_CD;
import static Project.dataBases.GameDataBase.getInfo;
import static Project.dataBases.GameDataBase.putPerson;
import static Project.dataBases.ZongMenDataBase.*;
import static io.github.kloping.mirai0.unitls.Tools.GameTool.getFhName;
import static io.github.kloping.mirai0.unitls.Tools.Tool.*;
import static io.github.kloping.mirai0.unitls.drawers.Drawer.FilterImg;
import static io.github.kloping.mirai0.unitls.drawers.Drawer.getImageFromStrings;

@Entity
public class ZongMenServiceImpl implements IZongMenService {

    public long isJkOk(Long qq) {
        PersonInfo info = getInfo(qq);
        if (info.getJk1() <= System.currentTimeMillis())
            return 0;
        return info.getJk1();
    }

    @Override
    public String create(String name, Long who, Group group) {
        if (isIlleg(name)) return "存在敏感字符";
        if (name.length() > 4) return "名字过长最大4个长度";
        if (ConfirmController.Confirming.contains(who)) return "请先完成 当前选项 尝试 '取消'";
        long t1 = -1;
        if ((t1 = isJkOk(who)) > 0) return "宗门活动冷却中...==>" + getTimeDDHHMM(t1);
        //=================
        if (qq2id.containsKey(who))
            return "你已经在宗门之中";
        PersonInfo info = getInfo(who);
        if (info.getGold() < 500)
            return "创建,至少需要500金魂币才能出创建宗门";
        if (info.getLevel() < 50)
            return "威望不足，50级以上才可创建宗门";
        try {
            Method method = this.getClass().getDeclaredMethod("createNow", Long.class, String.class, Group.class);
            method.setAccessible(true);
            ConfirmController.regConfirm(who, new Object[]{
                    method, this, new Object[]{
                    who, name, group}
            });
            return "确定要创建宗门 ' " + name + " ' 吗,\r\n这将花费450金魂币\r\n请在30秒内回复(确定/确认/取消)";
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return "创建异常";
        }
    }

    public String createNow(Long who, String name, Group group) {
        if (name == null || name.isEmpty() || NULL_LOW_STR.equals(name))
            return ("创建异常..");
        ZongMenDataBase.createNewZong(who, name);
        GameDataBase.putPerson(getInfo(who).addGold(-450L, new TradingRecord()
                .setType1(TradingRecord.Type1.lost)
                .setType0(TradingRecord.Type0.gold)
                .setTo(-1)
                .setMain(who)
                .setFrom(who)
                .setDesc("创建宗门")
                .setMany(450)
        ));
        putPerson(getInfo(who).setJk1(System.currentTimeMillis() + 1000 * 60 * 60 * 12));
        return ZongInfo(who, group);
    }

    @Override
    public String ZongInfo(Long qq, Group group) {
        if (qq2id.containsKey(qq)) {
            Integer id = Integer.valueOf(qq2id.get(qq) + "");
            return ZongInfo(id, group);
        } else
            return ("你没有加入任何宗门");
    }

    @Override
    public String ZongInfo(Zong zong, Group group) {
        StringBuilder sb = new StringBuilder();
        String icon = zong.getIcon();
        sb.append("宗门名称:").append(zong.getName()).append("\r\n");
        sb.append("宗门等级:").append(zong.getLevel()).append("\r\n");
        sb.append("宗门经验:").append(zong.getXp()).append("/").append(zong.getXpMax()).append("\r\n");
        sb.append("宗门状态:").append(zong.getState() == 1 ? "正常运转" : "停止运转").append("\r\n");
        String name = "宗主";
        try {
            name = MemberTools.getNameFromGroup(zong.getMain().longValue(), group);
        } catch (Exception e) {
            e.printStackTrace();
        }
        sb.append("宗门宗主:").append(name).append("(").append(zong.getMain()).append(")").append("\r\n");
        sb.append("宗门人数:").append(zong.getMembers()).append("/").append(zong.getMaxP()).append("\r\n");
        sb.append("宗门长老:").append(zong.getElders()).append("/").append(zong.getElderNum()).append("\r\n");
        return (icon.isEmpty() ? "" : pathToImg(icon)) + getImageFromStrings(false, sb.toString().split("\\s+"));
    }

    @Override
    public String ZongInfo(Integer id, Group group) {
        Zong zong = getZongInfo(id);
        return ZongInfo(zong, group);
    }

    @Override
    public String List(Group group) {
        return getImageFromStrings(getAllZongNames());
    }

    @Override
    public String setIcon(String imageUrl, Long who, Group group) {
        if (!qq2id.containsKey(who))
            return ("你没有加入任何宗门");
        Zong zong = getZongInfo(who);
        Zon zon = getZonInfo(who);
        if (zon.getLevel() != 2)
            return ("仅宗主有权限修改宗门信息");
        if (zong.getMk() > System.currentTimeMillis())
            return ("宗门修改信息 冷却中 =>" + getTimeDDHHMM(zong.getMk()));
        String path = ZongMenDataBase.path + "/" + getZongInfo(who).getId() + "/icon.png";
        io.github.kloping.url.UrlUtils.downloadFile(imageUrl, path);
        FilterImg(new File(path));
        zong.setIcon(path).setMk(System.currentTimeMillis() + 1000 * 60 * 60 * 2);
        putZongInfo(zong);
        return ZongInfo(who, group);
    }

    @Override
    public String Invite(long who, long qq, Group group) {
        if (!qq2id.containsKey(who))
            return ("你没有加入任何宗门");
        if (qq2id.containsKey(qq))
            return ("ta已经加入宗门");
        if (!GameDataBase.exist(qq))
            return ("该玩家尚未注册");
        Zong zong = getZongInfo(who);
        Zon zon = getZonInfo(who);
        if (zon.getLevel() < 1)
            return ("仅宗主和长老有权限邀请成员");
        try {
            Method method = this.getClass().getDeclaredMethod("join", Long.class, Long.class, Group.class);
            if (ConfirmController.Agreeing.contains(qq))
                return ("ta正在被邀请中...");
            if (zong.getMembers() >= zong.getMaxP()) {
                return ("宗门已满");
            }
            ConfirmController.regAgree(qq, new Object[]{
                    method, this, new Object[]{who, qq, group}
            });
            return new StringBuilder().append("邀请成功\r\n").append(MemberTools.getNameFromGroup(qq, group)).append("请在30秒内回复同意/不同意").toString();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return ("邀请异常");
        }
    }

    public String join(Long who, Long qq, Group group) {
        if (getInfo(qq).getJk1() > System.currentTimeMillis()) {
            return ("宗门活动冷却中...==>" + getTimeDDHHMM(getInfo(qq).getJk1()));
        }
        Zong zong = getZongInfo(who);
        putPerson(getInfo(qq).setJk1(System.currentTimeMillis() + 1000 * 60 * 60 * 12));
        addPer(zong, qq);
        putZongInfo(zong);
        return new StringBuilder().append("加入成功!!\r\n").append(listPer(who, group)).toString();
    }

    @Override
    public String listPer(Long who, Group group) {
        if (!qq2id.containsKey(who))
            return ("你没有加入任何宗门");
        Zong zong = getZongInfo(who);
        StringBuilder sb = new StringBuilder();
        sb.append(zong.getName()).append("\r\n==================\r\n");
        int i = 1;
        for (Number z1 : zong.getMember()) {
            try {
                Zon zon = getZonInfo(z1.longValue());
                sb.append(i).
                        append(":").
                        append(getFhName(z1.longValue(), true))
                        .append("(").append(
                                zon.getLevel() == 1 ? "长老" :
                                        zon.getLevel() == 2 ? "宗主" : "").append("\r\n\t  ");
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
    public String setName(String name, long who, Group group) {
        if (isIlleg(name)) return ("存在敏感字符");
        if (!qq2id.containsKey(who))
            return ("你没有加入任何宗门");
        Zong zong = getZongInfo(who);
        Zon zon = getZonInfo(who);
        if (zon.getLevel() == 0)
            return ("仅宗主和长老有权限修改宗门名字");
        if (zong.getMk() > System.currentTimeMillis())
            return ("宗门修改信息 冷却中 =>" + getTimeDDHHMM(zong.getMk()));
        zong.setName(name).setMk(System.currentTimeMillis() + 1000 * 60 * 60 * 2);
        putZongInfo(zong);
        return ZongInfo(who, group);
    }

    @Override
    public String Cob(Long who) {
        if (!qq2id.containsKey(who))
            return "你没有加入任何宗门";
        Zong zong = getZongInfo(who);
        Zon zon = getZonInfo(who);
        PersonInfo info = getInfo(who);
        if (info.getGold() < info.getLevel())
            return "金魂币不足";
        if (info.getCbk1() > System.currentTimeMillis())
            return "贡献时间未到 => " + getTimeDDHHMM(info.getCbk1());
        zon.setXper(zon.getXper() + info.getLevel());
        zong.setXp(zong.getXp() + info.Level);
        info.setCbk1(System.currentTimeMillis() + 1000 * 60 * 60 * COB_CD);
        info.addGold((long) -info.getLevel(), new TradingRecord()
                .setType1(TradingRecord.Type1.lost)
                .setType0(TradingRecord.Type0.gold)
                .setTo(-1)
                .setMain(who)
                .setFrom(who)
                .setDesc("宗门贡献")
                .setMany(info.getLevel())
        );
        putZonInfo(zon);
        putZongInfo(zong);
        putPerson(info);
        return "贡献成功贡献" + info.getLevel() + "点";
    }

    @Override
    public String help(long qq, long who) {
        if (!qq2id.containsKey(qq))
            return "你没有加入任何宗门";
        if (!qq2id.containsKey(who))
            return "ta没有加入任何宗门";
        if (!GameDataBase.exist(who))
            return PLAYER_NOT_REGISTERED;
        Long l1 = Long.valueOf(qq2id.get(qq) + "");
        Long l2 = Long.valueOf(qq2id.get(who) + "");
        if (l1 != l2)
            return "你们不在同一个宗门";
        Zong zong = getZongInfo(qq);
        Zon zon = getZonInfo(qq);
        PersonInfo info = getInfo(qq);
        PersonInfo info1 = getInfo(who);
        if (info1.getHp() > 0)
            return "ta仍有状态";
        if (zon.getTimes() > 0) {
            putPerson(info1.setHp(100L).addHl(100L));
            zon.setTimes(zon.getTimes() - 1);
            putZonInfo(zon);
            return "救援成功";
        } else {
            if (zong.getPub() > 0) {
                zong.setPub(zong.getPub() - 1);
                putPerson(info1.setHp(100L).addHl(100L));
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
        Zon zon = getZonInfo(id);
        Zon zon1 = getZonInfo(who);
        if (zong.getElders() >= zong.getElderNum()) {
            return "长老数量上线";
        } else {
            if (zon.getLevel() != 2)
                return "仅宗主可设置长老";
            else {
                if (zong.getElder().contains(who)) {
                    return "ta 本来就是长老";
                }
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
        if (!qq2id.containsKey(id))
            return "你没有加入任何宗门";
        if (!qq2id.containsKey(who))
            return "ta没有加入任何宗门";
        if (!GameDataBase.exist(who))
            return "该玩家尚未注册";
        Long l1 = Long.valueOf(qq2id.get(who) + "");
        Long l2 = Long.valueOf(qq2id.get(who) + "");
        if (l1 != l2)
            return "你们不在同一个宗门";
        return "";
    }

    @Override
    public String cancelElder(long id, long who) {
        String s1 = setUpElderPermission(id, who);
        if (!s1.isEmpty()) return s1;
        Zong zong = getZongInfo(id);
        Zon zon = getZonInfo(id);
        Zon zon1 = getZonInfo(who);
        if (zon.getLevel() != 2)
            return "仅宗主可设置长老";
        else {
            if (!zong.getElder().contains(who)) {
                return "ta 本来就不是长老";
            }
            zong.setElders(zong.getElders() - 1);
            zong.getElder().remove(id);
            zon.setLevel(0);

            putZongInfo(zong);
            putZonInfo(zon);
            return "取消设置成功";
        }
    }

    @Override
    public String UpUp(long id, Group g) {
        if (!qq2id.containsKey(id))
            return ("你没有加入任何宗门");
        Zon zon = getZonInfo(id);
        if (zon.getLevel() != 2)
            return ("仅宗主可升级");
        Zong zong = getZongInfo(id);
        if (zong.getXp() >= zong.getXpMax()) {
            switch (zong.getLevel()) {
                case 1:
                    zong.setXp(zong.getXp() - zong.getXpMax());
                    zong.setLevel(2);
                    zong.setElderNum(3);
                    zong.setMaxP(14);
                    zong.setXpMax(1000L);
                    putZongInfo(zong);
                    return ZongInfo(id, g);
                case 2:
                    zong.setXp(zong.getXp() - zong.getXpMax());
                    zong.setLevel(3);
                    zong.setElderNum(4);
                    zong.setMaxP(18);
                    zong.setXpMax(4800L);
                    putZongInfo(zong);
                    return ZongInfo(id, g);
                case 3:
                    zong.setXp(zong.getXp() - zong.getXpMax());
                    zong.setLevel(4);
                    zong.setElderNum(5);
                    zong.setMaxP(22);
                    zong.setXpMax(8200L);
                    putZongInfo(zong);
                    return ZongInfo(id, g);
                case 4:
                    zong.setXp(zong.getXp() - zong.getXpMax());
                    zong.setLevel(5);
                    zong.setElderNum(7);
                    zong.setMaxP(25);
                    zong.setXpMax(12500L);
                    putZongInfo(zong);
                    return ZongInfo(id, g);
                case 5:
                    zong.setXp(zong.getXp() - zong.getXpMax());
                    zong.setLevel(6);
                    zong.setElderNum(7);
                    zong.setMaxP(29);
                    zong.setXpMax(54500L);
                    putZongInfo(zong);
                    return ZongInfo(id, g);
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
        if (!qq2id.containsKey(id))
            return ("你没有加入任何宗门");
        if (ConfirmController.Confirming.contains(id)) return ("请先完成 当前选项 尝试 '取消'");
        Zon zon = getZonInfo(id);
        try {
            Method method = this.getClass().getDeclaredMethod("quiteNow", Long.class);
            ConfirmController.regConfirm(id, new Object[]{
                    method, this, new Object[]{new Long(id)}
            });
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
        if (ZongDetailService.quite(id))
            return (" 成功");
        else
            return (" 失败");
    }

    @Override
    public String QuiteOne(long id, long who) {
        if (!qq2id.containsKey(id))
            return "你没有加入任何宗门";
        if (ConfirmController.Confirming.contains(id))
            return "请先完成 当前选项 尝试 '取消'";
        Zon zon = getZonInfo(id);
        Zon zon1 = getZonInfo(who);
        if (zon.getLevel() != 2)
            return "仅宗主可移除成员";
        if (zon1.getLevel() > 1)
            return "不可以移除宗主或长老";
        try {
            Method method = this.getClass().getDeclaredMethod("quiteNow", Long.class);
            ConfirmController.regConfirm(id, new Object[]{
                    method, this, new Object[]{who}
            });
            return "确定要移除 ta 么? ";
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return "移除异常";
        }
    }
}