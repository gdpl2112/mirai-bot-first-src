package Project.dataBases;

import Project.aSpring.SpringBootResource;
import Project.dataBases.skill.SkillDataBase;
import io.github.kloping.mirai0.commons.GInfo;
import io.github.kloping.mirai0.commons.PersonInfo;
import io.github.kloping.mirai0.commons.TradingRecord;
import io.github.kloping.mirai0.commons.broadcast.enums.ObjType;
import io.github.kloping.mirai0.commons.gameEntitys.Achievement;
import io.github.kloping.mirai0.commons.gameEntitys.AchievementEntity;
import io.github.kloping.mirai0.commons.gameEntitys.SoulBone;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static Project.dataBases.ZongMenDataBase.qq2id;
import static Project.services.detailServices.GameDetailService.gameBoneService;
import static io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet.FinalNormalString.ACHIEVEMENT_NOT_ACHIEVED;
import static io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet.FinalNormalString.ACHIEVEMENT_RECEIVED;

/**
 * @author github.kloping
 */
public class AchievementDataBase {
    public static final AchievementDataBase INSTANCE = new AchievementDataBase();

    public Map<Integer, AchievementEntity> entityMap = new HashMap<>();

    public AchievementDataBase() {
        entityMap.put(1, new AchievementEntity(1) {
            @Override
            public String finish(long qid) {
                if (!isFinish(qid)) return ACHIEVEMENT_NOT_ACHIEVED;
                if (finished(qid, this.getAid())) return ACHIEVEMENT_RECEIVED;
                GameDataBase.addToBgs(qid, 103, 10, ObjType.got);
                Achievement achievement = new Achievement(null, this.getAid(), qid, System.currentTimeMillis());
                SpringBootResource.getAchievementMapper().insert(achievement);
                return "成就达成;奖励10大瓶经验";
            }

            @Override
            public String intro(long qid) {
                return "等级达到10级;";
            }

            @Override
            public boolean isFinish(long qid) {
                PersonInfo pInfo = GameDataBase.getInfo(qid);
                return pInfo.getLevel() >= 10;
            }
        });
        entityMap.put(2, new AchievementEntity(2) {
            @Override
            public String finish(long qid) {
                if (!isFinish(qid)) return ACHIEVEMENT_NOT_ACHIEVED;
                if (finished(qid, this.getAid())) return ACHIEVEMENT_RECEIVED;
                GameDataBase.addToBgs(qid, 1602, 1, ObjType.got);
                Achievement achievement = new Achievement(null, this.getAid(), qid, System.currentTimeMillis());
                SpringBootResource.getAchievementMapper().insert(achievement);
                return "成就达成;奖励黄升级券";
            }

            @Override
            public String intro(long qid) {
                return "等级达到70级并解锁武魂真身;";
            }

            @Override
            public boolean isFinish(long qid) {
                PersonInfo pInfo = GameDataBase.getInfo(qid);
                return pInfo.getLevel() >= 70 || SkillDataBase.getSkillInfo(qid).size() == 7;
            }
        });
        entityMap.put(3, new AchievementEntity(3) {
            @Override
            public String finish(long qid) {
                if (!isFinish(qid)) return ACHIEVEMENT_NOT_ACHIEVED;
                if (finished(qid, this.getAid())) return ACHIEVEMENT_RECEIVED;
                GameDataBase.addToBgs(qid, 128, 1, ObjType.got);
                Achievement achievement = new Achievement(null, this.getAid(), qid, System.currentTimeMillis());
                SpringBootResource.getAchievementMapper().insert(achievement);
                return "成就达成;奖励升级券";
            }

            @Override
            public String intro(long qid) {
                return "等级达到150级;";
            }

            @Override
            public boolean isFinish(long qid) {
                PersonInfo pInfo = GameDataBase.getInfo(qid);
                return pInfo.getLevel() >= 150;
            }
        });
        entityMap.put(4, new AchievementEntity(4) {
            @Override
            public String finish(long qid) {
                if (!isFinish(qid)) return ACHIEVEMENT_NOT_ACHIEVED;
                if (finished(qid, this.getAid())) return ACHIEVEMENT_RECEIVED;
                int id = 121;
                int num = 2;
                GameDataBase.addToBgs(qid, id, num, ObjType.got);
                Achievement achievement = new Achievement(null, this.getAid(), qid, System.currentTimeMillis());
                SpringBootResource.getAchievementMapper().insert(achievement);
                return "成就达成;奖励" + GameDataBase.getNameById(id) + "x" + num;
            }

            @Override
            public String intro(long qid) {
                return "精通魂技;使用魂技达到二百次;";
            }

            @Override
            public boolean isFinish(long qid) {
                GInfo info = GInfo.getInstance(qid);
                return info.getUseskillc() >= 200;
            }
        });
        entityMap.put(5, new AchievementEntity(5) {
            @Override
            public String finish(long qid) {
                if (!isFinish(qid)) return ACHIEVEMENT_NOT_ACHIEVED;
                if (finished(qid, this.getAid())) return ACHIEVEMENT_RECEIVED;
                int id = 122;
                int num = 2;
                GameDataBase.addToBgs(qid, id, num, ObjType.got);
                Achievement achievement = new Achievement(null, this.getAid(), qid, System.currentTimeMillis());
                SpringBootResource.getAchievementMapper().insert(achievement);
                return "成就达成;奖励" + GameDataBase.getNameById(id) + "x" + num;
            }

            @Override
            public String intro(long qid) {
                return "熟练魂技;使用魂技达到一千次;";
            }

            @Override
            public boolean isFinish(long qid) {
                GInfo info = GInfo.getInstance(qid);
                return info.getUseskillc() >= 1000;
            }
        });
        entityMap.put(6, new AchievementEntity(6) {
            @Override
            public String finish(long qid) {
                if (!isFinish(qid)) return ACHIEVEMENT_NOT_ACHIEVED;
                if (finished(qid, this.getAid())) return ACHIEVEMENT_RECEIVED;
                int id = 109;
                int num = 5;
                GameDataBase.addToBgs(qid, id, num, ObjType.got);
                Achievement achievement = new Achievement(null, this.getAid(), qid, System.currentTimeMillis());
                SpringBootResource.getAchievementMapper().insert(achievement);
                return "成就达成;奖励" + GameDataBase.getNameById(id) + "x" + num;
            }

            @Override
            public String intro(long qid) {
                return "乐于助人;支援次数达到100次;";
            }

            @Override
            public boolean isFinish(long qid) {
                GInfo info = GInfo.getInstance(qid);
                return info.getHelpc() >= 100;
            }
        });
        entityMap.put(7, new AchievementEntity(7) {
            @Override
            public String finish(long qid) {
                if (!isFinish(qid)) return ACHIEVEMENT_NOT_ACHIEVED;
                if (finished(qid, this.getAid())) return ACHIEVEMENT_RECEIVED;
                int id = 110;
                int num = 10;
                GameDataBase.addToBgs(qid, id, num, ObjType.got);
                Achievement achievement = new Achievement(null, this.getAid(), qid, System.currentTimeMillis());
                SpringBootResource.getAchievementMapper().insert(achievement);
                return "成就达成;奖励" + GameDataBase.getNameById(id) + "x" + num;
            }

            @Override
            public String intro(long qid) {
                return "热心肠;支援次数达到500次;";
            }

            @Override
            public boolean isFinish(long qid) {
                GInfo info = GInfo.getInstance(qid);
                return info.getHelpc() >= 500;
            }
        });
        entityMap.put(8, new AchievementEntity(8) {
            @Override
            public String finish(long qid) {
                if (!isFinish(qid)) return ACHIEVEMENT_NOT_ACHIEVED;
                if (finished(qid, this.getAid())) return ACHIEVEMENT_RECEIVED;
                int id = 110;
                int num = 3;
                GameDataBase.addToBgs(qid, id, num, ObjType.got);
                Achievement achievement = new Achievement(null, this.getAid(), qid, System.currentTimeMillis());
                SpringBootResource.getAchievementMapper().insert(achievement);
                return "成就达成;奖励" + GameDataBase.getNameById(id) + "x" + num;
            }

            @Override
            public String intro(long qid) {
                return "救救我;请求支援次数达到50次;";
            }

            @Override
            public boolean isFinish(long qid) {
                GInfo info = GInfo.getInstance(qid);
                return info.getReqc() >= 50;
            }
        });
        entityMap.put(9, new AchievementEntity(9) {
            @Override
            public String finish(long qid) {
                if (!isFinish(qid)) return ACHIEVEMENT_NOT_ACHIEVED;
                if (finished(qid, this.getAid())) return ACHIEVEMENT_RECEIVED;
                int id = 109;
                int num = 15;
                GameDataBase.addToBgs(qid, id, num, ObjType.got);
                Achievement achievement = new Achievement(null, this.getAid(), qid, System.currentTimeMillis());
                SpringBootResource.getAchievementMapper().insert(achievement);
                return "成就达成;奖励" + GameDataBase.getNameById(id) + "x" + num;
            }

            @Override
            public String intro(long qid) {
                return "救救救救我;请求支援次数达到500次;";
            }

            @Override
            public boolean isFinish(long qid) {
                GInfo info = GInfo.getInstance(qid);
                return info.getReqc() >= 500;
            }
        });
        entityMap.put(10, new AchievementEntity(10) {
            @Override
            public String finish(long qid) {
                if (!isFinish(qid)) return ACHIEVEMENT_NOT_ACHIEVED;
                if (finished(qid, this.getAid())) return ACHIEVEMENT_RECEIVED;
                int id = 101;
                int num = 3;
                GameDataBase.addToBgs(qid, id, num, ObjType.got);
                Achievement achievement = new Achievement(null, this.getAid(), qid, System.currentTimeMillis());
                SpringBootResource.getAchievementMapper().insert(achievement);
                return "成就达成;奖励" + GameDataBase.getNameById(id) + "x" + num;
            }

            @Override
            public String intro(long qid) {
                return "修炼达人;修炼次数达到100次;";
            }

            @Override
            public boolean isFinish(long qid) {
                GInfo info = GInfo.getInstance(qid);
                return info.getXlc() >= 100;
            }
        });
        entityMap.put(11, new AchievementEntity(11) {
            @Override
            public String finish(long qid) {
                if (!isFinish(qid)) return ACHIEVEMENT_NOT_ACHIEVED;
                if (finished(qid, this.getAid())) return ACHIEVEMENT_RECEIVED;
                int id = 103;
                int num = 15;
                GameDataBase.addToBgs(qid, id, num, ObjType.got);
                Achievement achievement = new Achievement(null, this.getAid(), qid, System.currentTimeMillis());
                SpringBootResource.getAchievementMapper().insert(achievement);
                return "成就达成;奖励" + GameDataBase.getNameById(id) + "x" + num;
            }

            @Override
            public String intro(long qid) {
                return "修炼高手;修炼次数达到1000次;";
            }

            @Override
            public boolean isFinish(long qid) {
                GInfo info = GInfo.getInstance(qid);
                return info.getXlc() >= 1000;
            }
        });
        entityMap.put(12, new AchievementEntity(12) {
            @Override
            public String finish(long qid) {
                if (!isFinish(qid)) return ACHIEVEMENT_NOT_ACHIEVED;
                if (finished(qid, this.getAid())) return ACHIEVEMENT_RECEIVED;
                long t = 2000;
                PersonInfo pInfo = GameDataBase.getInfo(qid);
                pInfo.addGold(t, new TradingRecord()
                        .setType1(TradingRecord.Type1.add)
                        .setType0(TradingRecord.Type0.gold)
                        .setTo(qid)
                        .setMain(qid)
                        .setFrom(-1)
                        .setDesc("完成成就")
                        .setMany(t));
                Achievement achievement = new Achievement(null, this.getAid(), qid, System.currentTimeMillis());
                SpringBootResource.getAchievementMapper().insert(achievement);
                return "成就达成;奖励" + t + "金魂币";
            }

            @Override
            public String intro(long qid) {
                return "购物达人;购买次数达到200次;";
            }

            @Override
            public boolean isFinish(long qid) {
                GInfo info = GInfo.getInstance(qid);
                return info.getBuyc() >= 200;
            }
        });
        entityMap.put(13, new AchievementEntity(13) {
            @Override
            public String finish(long qid) {
                if (!isFinish(qid)) return ACHIEVEMENT_NOT_ACHIEVED;
                if (finished(qid, this.getAid())) return ACHIEVEMENT_RECEIVED;
                long t = 10000;
                PersonInfo pInfo = GameDataBase.getInfo(qid);
                pInfo.addGold(t, new TradingRecord()
                        .setType1(TradingRecord.Type1.add)
                        .setType0(TradingRecord.Type0.gold)
                        .setTo(qid)
                        .setMain(qid)
                        .setFrom(-1)
                        .setDesc("完成成就")
                        .setMany(t));
                Achievement achievement = new Achievement(null, this.getAid(), qid, System.currentTimeMillis());
                SpringBootResource.getAchievementMapper().insert(achievement);
                return "成就达成;奖励" + t + "金魂币";
            }

            @Override
            public String intro(long qid) {
                return "我是富豪;购买次数达到2000次;";
            }

            @Override
            public boolean isFinish(long qid) {
                GInfo info = GInfo.getInstance(qid);
                return info.getBuyc() >= 2000;
            }
        });
        entityMap.put(14, new AchievementEntity(14) {
            @Override
            public String finish(long qid) {
                if (!isFinish(qid)) return ACHIEVEMENT_NOT_ACHIEVED;
                if (finished(qid, this.getAid())) return ACHIEVEMENT_RECEIVED;
                int id = 107;
                int num = 2;
                GameDataBase.addToBgs(qid, id, num, ObjType.got);
                Achievement achievement = new Achievement(null, this.getAid(), qid, System.currentTimeMillis());
                SpringBootResource.getAchievementMapper().insert(achievement);
                return "成就达成;奖励" + GameDataBase.getNameById(id) + "x" + num;
            }

            @Override
            public String intro(long qid) {
                return "小试牛刀;击杀1次魂兽;";
            }

            @Override
            public boolean isFinish(long qid) {
                Integer integer = SpringBootResource.getKillGhostMapper().getNum(qid);
                return integer != null && integer >= 1;
            }
        });
        entityMap.put(15, new AchievementEntity(15) {
            @Override
            public String finish(long qid) {
                if (!isFinish(qid)) return ACHIEVEMENT_NOT_ACHIEVED;
                if (finished(qid, this.getAid())) return ACHIEVEMENT_RECEIVED;
                int num = 3;
                GameDataBase.addToBgs(qid, 107, num, ObjType.got);
                GameDataBase.addToBgs(qid, 108, num, ObjType.got);
                GameDataBase.addToBgs(qid, 1015, num, ObjType.got);

                Achievement achievement = new Achievement(null, this.getAid(), qid, System.currentTimeMillis());
                SpringBootResource.getAchievementMapper().insert(achievement);

                return "成就达成;奖励各种花瓣x" + num;
            }

            @Override
            public String intro(long qid) {
                return "大显身手;击杀一百次魂兽;";
            }

            @Override
            public boolean isFinish(long qid) {
                Integer integer = SpringBootResource.getKillGhostMapper().getNum(qid);
                return integer >= 100;
            }
        });
        entityMap.put(16, new AchievementEntity(16) {
            @Override
            public String finish(long qid) {
                if (!isFinish(qid)) return ACHIEVEMENT_NOT_ACHIEVED;
                if (finished(qid, this.getAid())) return ACHIEVEMENT_RECEIVED;
                int num = 6;
                GameDataBase.addToBgs(qid, 107, num, ObjType.got);
                GameDataBase.addToBgs(qid, 108, num, ObjType.got);
                GameDataBase.addToBgs(qid, 1015, num, ObjType.got);

                Achievement achievement = new Achievement(null, this.getAid(), qid, System.currentTimeMillis());
                SpringBootResource.getAchievementMapper().insert(achievement);

                return "成就达成;奖励各种花瓣x" + num;
            }

            @Override
            public String intro(long qid) {
                return "无敌之路;击杀一千次魂兽;";
            }

            @Override
            public boolean isFinish(long qid) {
                Integer integer = SpringBootResource.getKillGhostMapper().getNum(qid);
                return integer >= 1000;
            }
        });
        entityMap.put(17, new AchievementEntity(17) {
            @Override
            public String finish(long qid) {
                if (!isFinish(qid)) return ACHIEVEMENT_NOT_ACHIEVED;
                if (finished(qid, this.getAid())) return ACHIEVEMENT_RECEIVED;
                int id = 119;
                int num = 2;
                GameDataBase.addToBgs(qid, id, num, ObjType.got);
                Achievement achievement = new Achievement(null, this.getAid(), qid, System.currentTimeMillis());
                SpringBootResource.getAchievementMapper().insert(achievement);
                return "成就达成;奖励" + GameDataBase.getNameById(id) + "x" + num;
            }

            @Override
            public String intro(long qid) {
                return "我有套装;佩戴一套魂骨;";
            }

            @Override
            public boolean isFinish(long qid) {
                List<SoulBone> list = gameBoneService.getSoulBones(qid);
                return list.size() == 5;
            }
        });
        entityMap.put(18, new AchievementEntity(18) {
            @Override
            public String finish(long qid) {
                if (!isFinish(qid)) return ACHIEVEMENT_NOT_ACHIEVED;
                if (finished(qid, this.getAid())) return ACHIEVEMENT_RECEIVED;
                int id = 118;
                int num = 5;
                GameDataBase.addToBgs(qid, id, num, ObjType.got);
                Achievement achievement = new Achievement(null, this.getAid(), qid, System.currentTimeMillis());
                SpringBootResource.getAchievementMapper().insert(achievement);
                return "成就达成;奖励" + GameDataBase.getNameById(id) + "x" + num;
            }

            @Override
            public String intro(long qid) {
                return "高级全套;佩戴一套高级魂骨;";
            }

            private boolean isGao(int oid) {
                boolean k0 = oid == 1513;
                boolean k1 = oid == 1523;
                boolean k2 = oid == 1533;
                boolean k3 = oid == 1543;
                boolean k4 = oid == 1553;
                return k0 || k1 || k2 || k3 || k4;
            }

            @Override
            public boolean isFinish(long qid) {
                List<SoulBone> list = gameBoneService.getSoulBones(qid);
                int i = 0;
                for (SoulBone soulBone : list) {
                    if (isGao(soulBone.getOid())) {
                        i++;
                    }
                }
                return i == 5;
            }
        });
        entityMap.put(19, new AchievementEntity(19) {
            @Override
            public String finish(long qid) {
                if (!isFinish(qid)) return ACHIEVEMENT_NOT_ACHIEVED;
                if (finished(qid, this.getAid())) return ACHIEVEMENT_RECEIVED;
                int id = 120;
                int num = 3;
                GameDataBase.addToBgs(qid, id, num, ObjType.got);
                Achievement achievement = new Achievement(null, this.getAid(), qid, System.currentTimeMillis());
                SpringBootResource.getAchievementMapper().insert(achievement);
                return "成就达成;奖励" + GameDataBase.getNameById(id) + "x" + num;
            }

            @Override
            public String intro(long qid) {
                return "我有宗门;加入宗门;";
            }

            @Override
            public boolean isFinish(long qid) {
                return qq2id.containsKey(qid);
            }
        });

    }

    public boolean finished(long qid, int aid) {
        Achievement achievement = SpringBootResource.getAchievementMapper().selectByAidAndQid(aid, qid);
        return achievement != null;
    }
}
