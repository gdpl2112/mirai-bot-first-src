package Project.services.impl;


import Project.aSpring.SpringBootResource;
import Project.broadcast.enums.ObjType;
import Project.broadcast.game.PlayerLostBroadcast;
import Project.dataBases.GameDataBase;
import Project.dataBases.SourceDataBase;
import Project.interfaces.Iservice.IGameBoneService;
import Project.services.detailServices.GameBoneDetailService;
import io.github.kloping.MySpringTool.annotations.Entity;
import io.github.kloping.mirai0.Entitys.gameEntitys.SoulAttribute;
import io.github.kloping.mirai0.Entitys.gameEntitys.SoulBone;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static Project.dataBases.GameDataBase.*;
import static Project.dataBases.SourceDataBase.getImgPathById;
import static io.github.kloping.mirai0.unitls.Tools.Tool.getEntry;
import static io.github.kloping.mirai0.unitls.Tools.Tool.RANDOM;
import static io.github.kloping.mirai0.unitls.drawers.Drawer.getImageFromStrings;

/**
 * @author github-kloping
 */
@Entity
public class GameBoneServiceImpl implements IGameBoneService {
    @Override
    public String getInfoAttributes(Long who) {
        SoulAttribute attributeBone = getSoulAttribute(who);
        String[] sss = new String[8];
        sss[0] = "============我的属性";
        sss[1] = "伤害闪避率:" + attributeBone.getHideChance() + "%";
        sss[2] = "生命恢复率:" + attributeBone.getHpChance() + "%";
        sss[3] = "生命恢复效果:" + attributeBone.getHpEffect() + "%";
        sss[4] = "魂力恢复率:" + attributeBone.getHlChance() + "%";
        sss[5] = "魂力恢复效果:" + attributeBone.getHlEffect() + "%";
        sss[6] = "精神力恢复率:" + attributeBone.getHjChance() + "%";
        sss[7] = "精神力恢复效果:" + attributeBone.getHjEffect() + "%";
        return getImageFromStrings(sss);
    }

    @Override
    public SoulAttribute getSoulAttribute(Long who) {
        Integer wh = GameDataBase.getInfo(who).getWh();
        SoulAttribute soulAttribute;
        if (wh > 0) {
            soulAttribute = SpringBootResource.getSoulAttributeMapper().selectById(wh);
            for (SoulBone soulBone : getSoulBones(who.longValue())) {
                soulAttribute.appendSoulBone(soulBone);
            }
        } else {
            soulAttribute = new SoulAttribute().setWh(wh);
        }
        return soulAttribute;
    }

    @Override
    public List<SoulBone> getSoulBones(Long who) {
        return SpringBootResource.getSoulBoneMapper().selectBons(who);
    }

    public Map<Integer, Map.Entry<String, Integer>> getAttributeMap(Long who, boolean k1) {
        try {
            String str = GameDataBase.getStringFromData(who, "AttributeBoneMap");
            String[] sss = str.split(str.contains("\r") ? "\r\n" : "\n");
            Map<Integer, Map.Entry<String, Integer>> map = new LinkedHashMap<>();
            for (String s1 : sss) {
                if (s1.trim().isEmpty())
                    continue;
                String[] ss = s1.split("=");
                Integer k = Integer.valueOf(ss[0]);
                String[] vv = ss[1].split(":");
                String v1 = vv[0];
                Integer v2 = Integer.valueOf(vv[1]);
                map.put(k, getEntry(v1, v2));
            }
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            return new LinkedHashMap<>();
        }
    }

    @Override
    public String parseBone(int id, long qq) {
        //判断 该 是否有 此魂骨
        if (!GameDataBase.containsInBg(id, qq))
            return "你的背包里没有 " + getNameById(id);
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
        int r1 = RANDOM.nextInt(7);
        switch (r1) {
            case 0:
                nu = i * 3;
                SpringBootResource.getSoulBoneMapper().insert(new SoulBone()
                        .setType(GameBoneDetailService.Type.HIDE_PRO.getValue())
                        .setOid(id).setQid(qq).setTime(System.currentTimeMillis()).setValue(nu)
                );
                return "吸收成功 获得了 " + nu + "点 闪避\r\n" + SourceDataBase.getImgPathById(id);
            case 1:
                nu = i * 5;
                SpringBootResource.getSoulBoneMapper().insert(new SoulBone()
                        .setType(GameBoneDetailService.Type.HP_PRO.getValue())
                        .setOid(id).setQid(qq).setTime(System.currentTimeMillis()).setValue(nu)
                );
                return "吸收成功 获得了 " + nu + "点 生命回复率\r\n" + SourceDataBase.getImgPathById(id);
            case 2:
                nu = i * 6;
                SpringBootResource.getSoulBoneMapper().insert(new SoulBone()
                        .setType(GameBoneDetailService.Type.HP_REC_EFF.getValue())
                        .setOid(id).setQid(qq).setTime(System.currentTimeMillis()).setValue(nu)
                );
                return "吸收成功 获得了 " + nu + "点 生命回复效果\r\n" + SourceDataBase.getImgPathById(id);
            case 3:
                nu = i * 5;
                SpringBootResource.getSoulBoneMapper().insert(new SoulBone()
                        .setType(GameBoneDetailService.Type.HL_PRO.getValue())
                        .setOid(id).setQid(qq).setTime(System.currentTimeMillis()).setValue(nu)
                );
                return "吸收成功 获得了 " + nu + "点 魂力回复率\r\n" + SourceDataBase.getImgPathById(id);
            case 4:
                nu = i * 6;
                SpringBootResource.getSoulBoneMapper().insert(new SoulBone()
                        .setType(GameBoneDetailService.Type.HL_REC_EFF.getValue())
                        .setOid(id).setQid(qq).setTime(System.currentTimeMillis()).setValue(nu)
                );
                return "吸收成功 获得了 " + nu + "点 魂力回复效果\r\n" + SourceDataBase.getImgPathById(id);
            case 5:
                nu = i * 5;
                SpringBootResource.getSoulBoneMapper().insert(new SoulBone()
                        .setType(GameBoneDetailService.Type.HJ_PRO.getValue())
                        .setOid(id).setQid(qq).setTime(System.currentTimeMillis()).setValue(nu)
                );
                return "吸收成功 获得了 " + nu + "点精神力回复率\r\n" + SourceDataBase.getImgPathById(id);
            case 6:
                nu = i * 6;
                SpringBootResource.getSoulBoneMapper().insert(new SoulBone()
                        .setType(GameBoneDetailService.Type.HJ_REC_EFF.getValue())
                        .setOid(id).setQid(qq).setTime(System.currentTimeMillis()).setValue(nu)
                );
                return "吸收成功 获得了" + nu + "点精神力回复效果\r\n" + SourceDataBase.getImgPathById(id);
            default:
                return "未知 Bug ";
        }
    }

    @Override
    public String unInstallBone(Integer id, Long who) {
        List<SoulBone> list = getSoulBones(who);
        for (SoulBone soulBone : list) {
            if (soulBone.getOid().intValue() == id.intValue()) {
                SpringBootResource.getSoulBoneMapper().delete(soulBone);
                addToBgs(who, id, ObjType.un);
                GameDataBase.putPerson(getInfo(who).setHp(0L).setHl(0L).setXp(0L));
                PlayerLostBroadcast.INSTANCE.broadcast(who, who, PlayerLostBroadcast.PlayerLostReceiver.LostType.un);
                return "卸掉成功 状态全无";
            }
        }
        return "你没有 对应的魂骨";
    }

    private static boolean hasSamePart(List<SoulBone> list, Integer id) {
        for (SoulBone soulBone : list) {
            String s1 = soulBone.getOid().toString().substring(0, 3);
            String s2 = id.toString().substring(0, 3);
            if (s1.trim().equals(s2.trim())) return true;
        }
        return false;
    }

/*
    @Override
    public AttributeBone getAttribute(Long who) {
        AttributeBone attributeBone = null;
        try {
            attributeBone = new AttributeBone();
            attributeBone = AttributeBone.ParseObj(attributeBone, GameDataBase.getStringFromData(who, "AttributeBone"));
            Map<Integer, Map.Entry<String, Integer>> map = getAttributeMap(who, true);
            for (Integer id : map.keySet()) {
                Map.Entry<String, Integer> entry = map.get(id);
                String name = entry.getKey();
                switch (name) {
                    case "hide":
                        attributeBone.addHidePro(entry.getValue());
                        break;
                    case "hpp":
                        attributeBone.addHpPro(entry.getValue());
                        break;
                    case "hpe":
                        attributeBone.addHpRecEff(entry.getValue());
                        break;
                    case "hlp":
                        attributeBone.addHlPro(entry.getValue());
                        break;
                    case "hle":
                        attributeBone.addHlRecEff(entry.getValue());
                        break;
                }
            }
        } catch (Exception e) {
            attributeBone = new AttributeBone();
            GameDataBase.putStringFromData(who, "AttributeBone", attributeBone.toString());
        }
        return attributeBone;
    }

    @Override
    public void PutAttributeMap(Long who, Map<Integer, Map.Entry<String, Integer>> map) {
        StringBuilder sb = new StringBuilder();
        for (Integer k : map.keySet()) {
            Map.Entry<String, Integer> e = map.get(k);
            sb.append(k).append("=").append(e.getKey()).append(":").append(e.getValue()).append("\r\n");
        }
        GameDataBase.putStringFromData(who, "AttributeBoneMap", sb.toString());
    }

    @Override
    public Map<Integer, Map.Entry<String, Integer>> getAttributeMap(Long who, boolean k1) {
        try {
            String str = GameDataBase.getStringFromData(who, "AttributeBoneMap");
            String[] sss = str.split(str.contains("\r") ? "\r\n" : "\n");
            Map<Integer, Map.Entry<String, Integer>> map = new LinkedHashMap<>();
            for (String s1 : sss) {
                if (s1.trim().isEmpty())
                    continue;
                String[] ss = s1.split("=");
                Integer k = Integer.valueOf(ss[0]);
                String[] vv = ss[1].split(":");
                String v1 = vv[0];
                Integer v2 = Integer.valueOf(vv[1]);
                map.put(k, getEntry(v1, v2));
            }
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            return new LinkedHashMap<>();
        }
    }

    public static Integer[] getIdsFromAttributeMap(Map<Integer, Map.Entry<String, Integer>> map, String name) {
        List<Integer> list = new ArrayList<>();
        for (Integer k : map.keySet()) {
            Map.Entry<String, Integer> en = map.get(k);
            if (en.getKey().equals(name))
                list.add(k);
        }
        return list.toArray(new Integer[0]);
    }

    @Override
    public String parseBone(Integer id, Long who) {
        //判断 该 是否有 此魂骨
        if (!GameDataBase.containsInBg(id, who))
            return "你的背包里没有 " + getNameById(id);
        // Id Int To String
        String sb = String.valueOf(id);
        // 解析 id 位数
        sb = sb.substring(sb.length() - 1);
        // 转 Int
        int i = Integer.parseInt(sb);
        // 计算加成
        int nu = 0;
        int r1 = rand.nextInt(5);
        Map<Integer, Map.Entry<String, Integer>> map;
        try {
            map = getAttributeMap(who, true);
        } catch (Exception e) {
            map = new LinkedHashMap<>();
        }
        if (hasSamePart(map, id)) {
            return "已经吸收过 相同部位的魂骨了";
        }
        GameDataBase.removeFromBgs(who, id, ObjType.use);
        switch (r1) {
            case 0:
                nu = i * 3;
                map.put(id, getEntry("hide", nu));
                PutAttributeMap(who, map);
                return "吸收成功 获得了 " + nu + "点 闪避\r\n" + getImgById(id);
            case 1:
                nu = i * 5;
                map.put(id, getEntry("hpp", nu));
                PutAttributeMap(who, map);
                return "吸收成功 获得了 " + nu + "点 生命回复率\r\n" + getImgById(id);
            case 2:
                nu = i * 6;
                map.put(id, getEntry("hpe", nu));
                PutAttributeMap(who, map);
                return "吸收成功 获得了 " + nu + "点 生命回复效果\r\n" + getImgById(id);
            case 3:
                nu = i * 5;
                map.put(id, getEntry("hlp", nu));
                PutAttributeMap(who, map);
                return "吸收成功 获得了 " + nu + "点 魂力回复率\r\n" + getImgById(id);
            case 4:
                nu = i * 6;
                map.put(id, getEntry("hle", nu));
                PutAttributeMap(who, map);
                return "吸收成功 获得了 " + nu + "点 魂力回复效果\r\n" + getImgById(id);
        }
        return "未知 Bug ";
    }

    private static synchronized boolean hasSamePart(Map<Integer, Map.Entry<String, Integer>> map, Integer id) {
        for (Integer key : map.keySet()) {
            String s1 = (key + "").substring(0, 3);
            String s2 = (id + "").substring(0, 3);
            if (s1.trim().equals(s2.trim())) return true;
        }
        return false;
    }


    @Override
    public String unInstallBone(Integer id, Long who) {
        Map<Integer, Map.Entry<String, Integer>> map;
        try {
            map = getAttributeMap(who, true);
        } catch (Exception e) {
            map = new LinkedHashMap<>();
        }
        if (map.containsKey(id)) {
            map.remove(id);
            addToBgs(who, id, ObjType.un);
            PutAttributeMap(who, map);
            GameDataBase.putPerson(getInfo(who).setHp(0L).setHl(0L).setXp(0L));
            PlayerLostBroadcast.INSTANCE.broadcast(who, who, PlayerLostBroadcast.PlayerLostReceiver.type.un);
            return "卸掉成功 状态全无";
        } else {
            return "你没有 对应的魂骨";
        }
    }*/
}