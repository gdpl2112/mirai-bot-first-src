package Project.services.impl;


import Project.aSpring.SpringBootResource;
import Project.broadcast.game.PlayerLostBroadcast;
import Project.commons.broadcast.enums.ObjType;
import Project.commons.gameEntitys.SkillInfo;
import Project.commons.gameEntitys.SoulAttribute;
import Project.commons.gameEntitys.SoulBone;
import Project.dataBases.GameDataBase;
import Project.dataBases.SourceDataBase;
import Project.dataBases.skill.SkillDataBase;
import Project.interfaces.Iservice.IGameBoneService;
import Project.services.detailServices.GameBoneDetailService;
import Project.services.player.PlayerBehavioralManager;
import io.github.kloping.MySpringTool.annotations.AutoStand;
import io.github.kloping.MySpringTool.annotations.Entity;
import io.github.kloping.mirai0.Main.iutils.MemberUtils;
import io.github.kloping.mirai0.Main.iutils.MessageUtils;
import io.github.kloping.mirai0.unitls.Tools.Tool;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static Project.commons.rt.CommonSource.toPercent;
import static Project.dataBases.GameDataBase.*;
import static Project.dataBases.skill.SkillDataBase.getSkillInfo;
import static Project.dataBases.skill.SkillDataBase.remove;
import static Project.services.detailServices.GameBoneDetailService.TEMP_ATTR;
import static Project.services.detailServices.GameBoneDetailService.append;
import static Project.services.detailServices.GameSkillDetailService.getBasePercent;
import static Project.services.detailServices.GameSkillDetailService.getCoolTime;
import static io.github.kloping.mirai0.unitls.drawers.Drawer.getImageFromStrings;

/**
 * @author github-kloping
 */
@Entity
public class GameBoneServiceImpl implements IGameBoneService {
    public static final SoulAttribute EMPTY = new SoulAttribute();
    @AutoStand
    PlayerBehavioralManager manager;

    private static boolean hasSamePart(List<SoulBone> list, Integer id) {
        for (SoulBone soulBone : list) {
            String s1 = soulBone.getOid().toString().substring(0, 3);
            String s2 = id.toString().substring(0, 3);
            if (s1.trim().equals(s2.trim())) return true;
        }
        return false;
    }

    @Override
    public String getInfoAttributes(Long who) {
        SoulAttribute attributeBone = getSoulAttribute(who);
        List<String> list = new ArrayList<>();
        list.add("=====我的属性=====");
        list.add("伤害闪避率:" + attributeBone.getHideChance() + "%");
        list.add("生命恢复率:" + attributeBone.getHpChance() + "%");
        list.add("生命恢复效果:" + attributeBone.getHpEffect() + "%");
        list.add("魂力恢复率:" + attributeBone.getHlChance() + "%");
        list.add("魂力恢复效果:" + attributeBone.getHlEffect() + "%");
        list.add("精神力恢复率:" + attributeBone.getHjChance() + "%");
        list.add("精神力恢复效果:" + attributeBone.getHjEffect() + "%");
        list.add("攻击前/后摇:" + Tool.INSTANCE.device(manager.getAttPre(who), 1000f, 1) + "/" + Tool.INSTANCE.device(manager.getAttPost(who), 1000f, 1) + "s");
        list.add("魂力节省比:" + toPercent(getInfo(who).getLevel(), 150) / 2 + "%");
        return getImageFromStrings(list.toArray(new String[0]));
    }

    @Override
    public SoulAttribute getSoulAttribute(Long who) {
        if (who <= 0) {
            return EMPTY;
        }
        Integer wh = GameDataBase.getInfo(who).getWh();
        final SoulAttribute[] soulAttribute = new SoulAttribute[1];
        if (wh > 0) {
            soulAttribute[0] = SpringBootResource.getSoulAttributeMapper().selectById(wh);
            for (SoulBone soulBone : getSoulBones(who.longValue())) {
                soulAttribute[0].appendSoulBone(soulBone);
            }
        } else {
            soulAttribute[0] = new SoulAttribute().setWh(wh);
        }
        if (TEMP_ATTR.containsKey(who.longValue())) {
            TEMP_ATTR.get(who.longValue()).forEach((k, v) -> {
                soulAttribute[0] = append(soulAttribute[0], k, v.intValue());
            });
        }
        return soulAttribute[0];
    }

    @Override
    public List<SoulBone> getSoulBones(Long who) {
        return SpringBootResource.getSoulBoneMapper().selectBons(who);
    }

    @Override
    public String parseBone(int id, long qq) {
        //判断 该 是否有 此魂骨
        if (!GameDataBase.containsInBg(id, qq)) return "你的背包里没有 " + getNameById(id);
        // Id Int To String
        String sb = String.valueOf(id);
        // 解析 id 位数
        sb = sb.substring(sb.length() - 1);
        // 转 Int
        int i = Integer.parseInt(sb);
        // 计算加成
        int nu = 0;
        List<SoulBone> list = getSoulBones(qq);
        if (hasSamePart(list, id)) {
            return "已经吸收过 相同部位的魂骨了";
        }
        GameDataBase.removeFromBgs(qq, id, ObjType.use);
        if (SoulAttribute.SKILLS.contains(id)) {
            int st = -Integer.parseInt(String.valueOf(id).substring(2, 3));
            SkillInfo info = new SkillInfo()
                    .setId(id).setJid(id).setQq(qq).setTime(1L)
                    .setTimeL(getCoolTime(id, id, getInfo(qq).getWh(), st).longValue() * 60 * 1000L)
                    .setSt(st)
                    .setAddPercent(getBasePercent(id).intValue())
                    .setUsePercent(5);

            SkillDataBase.saveSkillInfo(info);
            String intro = GameSkillServiceImpl.getIntro(id, id, st, getInfo(qq).getWh());
            MessageUtils.INSTANCE.sendMessageInGroupWithAt(
                    "您激活了魂骨技能:\n" + intro, MemberUtils.getRecentSpeechesGid(qq), qq);
        }
        int r1 = Tool.INSTANCE.RANDOM.nextInt(7);
        switch (r1) {
            case 0:
                nu = i * 3;
                SpringBootResource.getSoulBoneMapper().insert(new SoulBone().setType(GameBoneDetailService.Type.HIDE_PRO.getValue()).setOid(id).setQid(qq).setTime(System.currentTimeMillis()).setValue(nu));
                return "吸收成功 获得了 " + nu + "点 闪避\r\n" + SourceDataBase.getImgPathById(id);
            case 1:
                nu = i * 10;
                SpringBootResource.getSoulBoneMapper().insert(new SoulBone().setType(GameBoneDetailService.Type.HP_PRO.getValue()).setOid(id).setQid(qq).setTime(System.currentTimeMillis()).setValue(nu));
                return "吸收成功 获得了 " + nu + "点 生命回复率\r\n" + SourceDataBase.getImgPathById(id);
            case 2:
                nu = i * 12;
                SpringBootResource.getSoulBoneMapper().insert(new SoulBone().setType(GameBoneDetailService.Type.HP_REC_EFF.getValue()).setOid(id).setQid(qq).setTime(System.currentTimeMillis()).setValue(nu));
                return "吸收成功 获得了 " + nu + "点 生命回复效果\r\n" + SourceDataBase.getImgPathById(id);
            case 3:
                nu = i * 10;
                SpringBootResource.getSoulBoneMapper().insert(new SoulBone().setType(GameBoneDetailService.Type.HL_PRO.getValue()).setOid(id).setQid(qq).setTime(System.currentTimeMillis()).setValue(nu));
                return "吸收成功 获得了 " + nu + "点 魂力回复率\r\n" + SourceDataBase.getImgPathById(id);
            case 4:
                nu = i * 12;
                SpringBootResource.getSoulBoneMapper().insert(new SoulBone().setType(GameBoneDetailService.Type.HL_REC_EFF.getValue()).setOid(id).setQid(qq).setTime(System.currentTimeMillis()).setValue(nu));
                return "吸收成功 获得了 " + nu + "点 魂力回复效果\r\n" + SourceDataBase.getImgPathById(id);
            case 5:
                nu = i * 10;
                SpringBootResource.getSoulBoneMapper().insert(new SoulBone().setType(GameBoneDetailService.Type.HJ_PRO.getValue()).setOid(id).setQid(qq).setTime(System.currentTimeMillis()).setValue(nu));
                return "吸收成功 获得了 " + nu + "点精神力回复率\r\n" + SourceDataBase.getImgPathById(id);
            case 6:
                nu = i * 12;
                SpringBootResource.getSoulBoneMapper().insert(new SoulBone().setType(GameBoneDetailService.Type.HJ_REC_EFF.getValue()).setOid(id).setQid(qq).setTime(System.currentTimeMillis()).setValue(nu));
                return "吸收成功 获得了" + nu + "点精神力回复效果\r\n" + SourceDataBase.getImgPathById(id);
            default:
                return "未知 Bug ";
        }
    }

    @Override
    public String unInstallBone(Integer id, Long qq) {
        List<SoulBone> list = getSoulBones(qq);
        for (SoulBone soulBone : list) {
            if (SoulAttribute.SKILLS.contains(id)) {
                int st = -Integer.parseInt(String.valueOf(id).substring(2, 3));
                Map<Integer, SkillInfo> infos = getSkillInfo(qq);
                remove(infos.get(st));
            }
            if (soulBone.getOid().intValue() == id.intValue()) {
                SpringBootResource.getSoulBoneMapper().delete(soulBone);
                addToBgs(qq, id, ObjType.un);
                (getInfo(qq).setHp(0L).setHl(0L)).apply();
                PlayerLostBroadcast.INSTANCE.broadcast(qq, qq, PlayerLostBroadcast.PlayerLostReceiver.LostType.un);
                return "卸掉成功 状态全无";
            }
        }
        return "你没有 对应的魂骨";
    }
}