package io.github.kloping.mirai0.Main;

import Project.aSpring.SpringBootResource;
import Project.dataBases.GameDataBase;
import Project.dataBases.skill.SkillDataBase;
import Project.services.impl.GameBoneServiceImpl;
import io.github.kloping.MySpringTool.StarterApplication;
import io.github.kloping.mirai0.Entitys.gameEntitys.PersonInfo;
import io.github.kloping.mirai0.Entitys.gameEntitys.SoulAttribute;
import io.github.kloping.mirai0.Entitys.gameEntitys.SoulBone;
import io.github.kloping.mirai0.Entitys.gameEntitys.Warp;
import io.github.kloping.mirai0.Entitys.gameEntitys.task.TaskPoint;

import java.io.File;
import java.util.Arrays;
import java.util.Map;

/**
 * @author github.kloping
 */
public class TempStarter {
    public static void main(String[] args) {
        GameBoneServiceImpl bs = StarterApplication.Setting.INSTANCE.getContextManager().getContextEntity(GameBoneServiceImpl.class);
//        File[] files = new File(GameDataBase.path, "dates/users").listFiles();
//        Arrays.sort(files);
//        for (File file : files) {
//            Long qid = Long.valueOf(file.getName());
//            if (qid <= 0) continue;
//            try {
//                //person info
//                PersonInfo personInfo = GameDataBase.getInfoFromFile(qid);
//                SpringBootResource.getPersonInfoMapper().insert(personInfo);
//                //bg
//                for (Integer bg : GameDataBase.getBgsFromFile(qid.longValue())) {
//                    SpringBootResource.getBagMapper().insert(bg, qid, System.currentTimeMillis());
//                }
//                //aq
//                Map<Integer, Map.Entry<Integer, Integer>> map = GameDataBase.getBgswFromFile(qid);
//                map.forEach((k, v) -> {
//                    SpringBootResource.getAqBagMapper().insert(v.getKey(), qid, v.getValue(), System.currentTimeMillis());
//                });
//                //bone
//                bs.getAttributeMap(qid, false).forEach((k, v) -> {
//                    SoulBone soulBone = new SoulBone();
//                    soulBone.setQid(qid.longValue());
//                    soulBone.setOid(k);
//                    soulBone.setType(SoulAttribute.MAP0.get(v.getKey()));
//                    soulBone.setValue(v.getValue());
//                    SpringBootResource.getSoulBoneMapper().insert(soulBone);
//                });
//                //hhpz
//                for (Integer oid : GameDataBase.getHhsFromFile(qid)) {
//                    if (oid > 0) {
//                        SpringBootResource.getHhpzMapper().insert(qid, oid, System.currentTimeMillis());
//                        System.err.println("insert " + oid);
//                    }
//                }
//                //warp
//                Warp warp = GameDataBase.getWarpFromFile(qid);
//                SpringBootResource.getWarpMapper().insert(warp);
//                //point
//                TaskPoint taskPoint = TaskPoint.getInstanceFromFile(qid);
//                taskPoint.apply();
//                System.out.println("qid => " + qid);
//            } catch (Throwable e) {
//                e.printStackTrace();
//                System.err.println("failed for qq=>" + qid);
//            }
//        }
//        sourceSAM();
        System.err.println("all is ok");
    }

    private static void sourceSAM() {
//        String whs = "1:蓝电霸王龙\n" +
        SpringBootResource.getSoulAttributeMapper().insert(new SoulAttribute()
                .setWh(1).setHideChance(5)
        );
//                "2:昊天锤\n" +
        SpringBootResource.getSoulAttributeMapper().insert(new SoulAttribute()
                .setWh(2)
        );
//                "3:六翼天使\n" +
        SpringBootResource.getSoulAttributeMapper().insert(new SoulAttribute()
                .setWh(3).setHlChance(5).setHlEffect(15)
        );
//                "4:噬魂珠皇\n" +
        SpringBootResource.getSoulAttributeMapper().insert(new SoulAttribute()
                .setWh(4).setHjChance(5)
        );
//                "5:蓝银皇\n" +
        SpringBootResource.getSoulAttributeMapper().insert(new SoulAttribute()
                .setWh(5).setHpChance(5).setHlChance(5)
        );
//                "6:柔骨兔\n" +
        SpringBootResource.getSoulAttributeMapper().insert(new SoulAttribute()
                .setWh(6).setHideChance(5)
        );
//                "7:邪眸白虎\n" +
        SpringBootResource.getSoulAttributeMapper().insert(new SoulAttribute()
                .setWh(7)
        );
//                "8:邪火凤凰\n" +
        SpringBootResource.getSoulAttributeMapper().insert(new SoulAttribute()
                .setWh(8)
        );
//                "9:七杀剑\n" +
        SpringBootResource.getSoulAttributeMapper().insert(new SoulAttribute()
                .setWh(9)
        );
//                "10:碧灵蛇皇\n" +
        SpringBootResource.getSoulAttributeMapper().insert(new SoulAttribute()
                .setWh(10)
        );
//                "11:破魂枪\n" +
        SpringBootResource.getSoulAttributeMapper().insert(new SoulAttribute()
                .setWh(11).setHlChance(4)
        );
//                "12:大力金刚熊\n" +
        SpringBootResource.getSoulAttributeMapper().insert(new SoulAttribute()
                .setWh(12)
        );
//                "13:奇茸通天菊\n" +
        SpringBootResource.getSoulAttributeMapper().insert(new SoulAttribute()
                .setWh(13)
        );
//                "14:鬼魅\n" +
        SpringBootResource.getSoulAttributeMapper().insert(new SoulAttribute()
                .setWh(14)
        );
//                "15:刺豚\n" +
        SpringBootResource.getSoulAttributeMapper().insert(new SoulAttribute()
                .setWh(15)
        );
//                "16:蛇矛\n" +
        SpringBootResource.getSoulAttributeMapper().insert(new SoulAttribute()
                .setWh(16).setHlChance(3)
        );
//                "17:骨龙\n" +
        SpringBootResource.getSoulAttributeMapper().insert(new SoulAttribute()
                .setWh(17).setHlChance(3)
        );
//                "18:蛇杖\n" +
        SpringBootResource.getSoulAttributeMapper().insert(new SoulAttribute()
                .setWh(18)
        );
//                "19:蓝银草\n" +
        SpringBootResource.getSoulAttributeMapper().insert(new SoulAttribute()
                .setWh(19).setHpChance(5)
        );
//                "20:玄龟\n" +
        SpringBootResource.getSoulAttributeMapper().insert(new SoulAttribute()
                .setWh(20).setHpEffect(25).setHpChance(5)
        );
//                "21:幽冥灵猫\n" +
        SpringBootResource.getSoulAttributeMapper().insert(new SoulAttribute()
                .setWh(21).setHideChance(10)
        );
//                "22:光明圣龙\n" +
        SpringBootResource.getSoulAttributeMapper().insert(new SoulAttribute()
                .setWh(22).setHideChance(6).setHlChance(5)
        );
//                "23:黑暗圣龙\n" +
        SpringBootResource.getSoulAttributeMapper().insert(new SoulAttribute()
                .setWh(23).setHideChance(5).setHjChance(5)
        );
//                "24:修罗神剑\n" +
        SpringBootResource.getSoulAttributeMapper().insert(new SoulAttribute()
                .setWh(24).setHjEffect(4)
        );
//                "25:青龙\n" +
        SpringBootResource.getSoulAttributeMapper().insert(new SoulAttribute()
                .setWh(25).setHideChance(3)
        );
//                "26:海神\n" +
        SpringBootResource.getSoulAttributeMapper().insert(new SoulAttribute()
                .setWh(26).setHlChance(5)
        );
//                "27:锄头\n" +
        SpringBootResource.getSoulAttributeMapper().insert(new SoulAttribute()
                .setWh(27)
        );
//                "28:斧头\n" +
        SpringBootResource.getSoulAttributeMapper().insert(new SoulAttribute()
                .setWh(28)
        );
//                "29:杀神昊天锤\n" +
        SpringBootResource.getSoulAttributeMapper().insert(new SoulAttribute()
                .setWh(29)
        );
//                "30:魔神剑\n" +
        SpringBootResource.getSoulAttributeMapper().insert(new SoulAttribute()
                .setWh(30).setHjEffect(4)
        );
//                "31:暗金恐爪熊\n";
        SpringBootResource.getSoulAttributeMapper().insert(new SoulAttribute()
                .setWh(31)
        );
    }
}
