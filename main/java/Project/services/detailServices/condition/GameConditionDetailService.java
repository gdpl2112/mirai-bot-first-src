package Project.services.detailServices.condition;

import Project.broadcast.game.SkillUseBroadcast;
import Project.commons.SpGroup;
import Project.commons.apiEntitys.RunnableWithOver;
import Project.commons.gameEntitys.SkillInfo;
import Project.commons.rt.ResourceSet;
import Project.controllers.auto.ControllerSource;
import Project.controllers.auto.TimerController;
import Project.controllers.gameControllers.GameConditionController;
import Project.dataBases.skill.SkillDataBase;
import Project.interfaces.httpApi.KlopingWeb;
import Project.services.autoBehaviors.GhostBehavior;
import Project.services.detailServices.ac.GameJoinDetailService;
import Project.services.detailServices.ac.entity.GhostWithGroup;
import io.github.kloping.MySpringTool.annotations.AutoStand;
import io.github.kloping.MySpringTool.annotations.Entity;
import io.github.kloping.judge.Judge;
import io.github.kloping.mirai0.Main.BootstarpResource;
import io.github.kloping.mirai0.Main.iutils.MemberUtils;
import io.github.kloping.mirai0.Main.iutils.MessageUtils;
import io.github.kloping.mirai0.commons.GhostObj;
import io.github.kloping.mirai0.commons.PersonInfo;
import Project.utils.Tools.Tool;
import io.github.kloping.number.NumberUtils;

import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static Project.services.detailServices.ac.GameJoinDetailService.willTips;
import static io.github.kloping.mirai0.Main.BootstarpResource.THREADS;

/**
 * @author github.kloping
 */
@Entity
public class GameConditionDetailService {
    public static final Integer[] GHOST_IDS = {
            501, 502, 503, 504, 505,
            506, 507, 508, 509, 510,
            511, 512, 513, 514, 515,
            516, 517, 518, 519, 510,
            601, 602, 603, 701, 702,
            703, 704, 705,
    };
    public static final String TIPS0 = "技能已刷新";
    public static final String PWD = "GameConditionController";
    public static final String GHOST_BUFF = "gf";
    public static final String P_BUFF = "pf";
    public static final String[] GHOST_BUFFS = {"A", "B", "C"};
    public static final String[] P_BUFFS = {"a", "b", "c", "d"};
    public static final String INTRO = "遇境说明:\n" +
            "1.每周六0点刷新遇境魂兽与魂师的随机BUFF与奖励\n" +
            "2.每周前三次通过遇境获得奖励\n" +
            "3.遇境通过表现为打败连续出现的三只魂兽\n" +
            "4.遇境中无法使用武器\n" +
            "5.遇境中选择逃跑默认挑战失败(魂兽不会使用逃跑类技能)\n" +
            "6.遇境中每阶段恢复40%%的血量\n" +
            "7.遇境中每阶段恢复40%%的魂力\n" +
            "8.所有魂技冷却为缩短40倍\n" +
            "<===============>\n本周BUFF:\n魂兽:%s\n魂师:%s";
    public static final Long TIME = 10L * 60 * 1000L;
    public static final List<GhostObj> GHOST_OBJS0 = new CopyOnWriteArrayList<>();
    public static final List<PersonInfo> P_OBJS0 = new CopyOnWriteArrayList<>();
    public static String GB = null;
    public static String PB = null;
    @AutoStand
    KlopingWeb klopingWeb;
    @AutoStand
    GameJoinDetailService gameJoinDetailService;

    public GameConditionDetailService() {
        BootstarpResource.START_AFTER.add(() -> {
            TimerController.ZERO_RUNS.add(() -> {
                if (Tool.INSTANCE.getWeekOfDate(new Date()).equals(Tool.INSTANCE.WEEK_DAYS[Tool.INSTANCE.WEEK_DAYS.length - 1])) {
                    klopingWeb.del("", PWD);
                    PB = null;
                    GB = null;
                }
            });
            ControllerSource.m3000.add(new RunnableWithOver() {
                @Override
                public boolean over() {
                    return false;
                }

                @Override
                public void run() {
                    try {
                        for (GhostObj ghostObj : GHOST_OBJS0) {
                            if (ghostObj == null) continue;
                            Long hp = NumberUtils.percentTo(3, ghostObj.getHpL());
                            ghostObj.addHp(hp);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            SkillUseBroadcast.INSTANCE.add(new SkillUseBroadcast.SkillUseReceiver() {
                @Override
                public void onReceive(long qid, int jid, int st, SkillInfo info) {
                    try {
                        if (P_OBJS0.contains(qid)) {
                            if (Tool.INSTANCE.RANDOM.nextInt(4) == 0) {
                                info.setTime(1L);
                                long gid = MemberUtils.getRecentSpeechesGid(qid);
                                MessageUtils.INSTANCE.sendMessageInGroupWithAt(TIPS0, gid, qid);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        });
    }

    public String getBuffIntro() {
        if (GB == null || PB == null) {
            GB = klopingWeb.get(GHOST_BUFF, PWD);
            if (Judge.isEmpty(GB)) {
                GB = Tool.INSTANCE.getRandT(GHOST_BUFFS);
                klopingWeb.put(GHOST_BUFF, GB, PWD);
            }
            PB = klopingWeb.get(P_BUFF, PWD);
            if (Judge.isEmpty(PB)) {
                PB = Tool.INSTANCE.getRandT(P_BUFFS);
                klopingWeb.put(P_BUFF, PB, PWD);
            }
        }
        return String.format(INTRO, parseName(GB), parseName(PB));
    }

    public String parseName(String name) {
        switch (name) {
            case "A":
                return "出场获得25%最大生命值得护盾";
            case "B":
                return "获得20%免伤";
            case "C":
                return "每三秒回复5%的血量";
            case "a":
                return "伤害附带12%雷电效果";
            case "b":
                return "魂技有20%几率立刻刷新";
            case "c":
                return "获得20%的增伤";
            case "d":
                return "获得25%的吸血";
            default:
                return ResourceSet.FinalString.ERR_TIPS;
        }
    }

    public void run(long qid, int n) {
        THREADS.submit(() -> {
            MessageUtils.INSTANCE.sendMessageInGroupWithAt(GameConditionController.TIPS0,
                    MemberUtils.getRecentSpeechesGid(qid), qid);
            try {
                Thread.sleep(15000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            GhostObj ghostObj = null;
            switch (n) {
                case 1:
                    ghostObj = GhostObj.create(100000, Tool.INSTANCE.getRandT(GHOST_IDS), false);
                    break;
                case 2:
                    ghostObj = GhostObj.create(1000000, Tool.INSTANCE.getRandT(GHOST_IDS), false);
                    break;
                case 3:
                    ghostObj = gameJoinDetailService.summonFor(String.valueOf(qid), Tool.INSTANCE.getRandT(GHOST_IDS));
                    break;
                default:
            }
            ghostObj.canGet = false;
            ghostObj.setWhoMeet(qid);
            SpGroup group = SpGroup.get(MemberUtils.getRecentSpeechesGid(qid));
            if (ghostObj instanceof GhostWithGroup) {
                GhostWithGroup gwg = (GhostWithGroup) ghostObj;
                gwg.setGroup(group);
            }
            GameJoinDetailService.saveGhostObjIn(qid, ghostObj);
            GhostBehavior gb = new GhostBehavior(qid, group);
            gb.getNowAllowJid().add(1006);
            GhostBehavior.exRun(gb);
            MessageUtils.INSTANCE.sendMessageInGroupWithAt(
                    willTips(qid, ghostObj, false), group.getId(), qid
            );
        });
    }

    public void act(PersonInfo pInfo, GhostObj ghostObj) {
        switch (GB) {
            case "A":
                ghostObj.addTag(SkillDataBase.TAG_SHIELD,
                        NumberUtils.percentTo(25, ghostObj.getHpL()),
                        TIME);
                break;
            case "B":
                ghostObj.addTag(SkillDataBase.TAG_DAMAGE_REDUCTION,
                        NumberUtils.percentTo(25, ghostObj.getHpL()),
                        TIME);
                break;
            case "C":
                GHOST_OBJS0.add(ghostObj);
                break;
            default:
                break;
        }
        switch (PB) {
            case "a":
                pInfo.addTag(SkillDataBase.TAG_LIGHT_ATT, 12, TIME);
                break;
            case "b":
                P_OBJS0.add(pInfo);
                break;
            case "c":
                pInfo.addTag(SkillDataBase.TAG_ADD_ATT, 20, TIME);
                break;
            case "d":
                pInfo.addTag(SkillDataBase.TAG_XX, 25, TIME);
                break;
            default:
                break;
        }
    }

    public void destroy(PersonInfo pInfo, GhostObj ghostObj) {
        switch (GB) {
            case "A":
                ghostObj.eddTag(SkillDataBase.TAG_SHIELD);
                break;
            case "B":
                ghostObj.eddTag(SkillDataBase.TAG_DAMAGE_REDUCTION);
                break;
            case "C":
                GHOST_OBJS0.remove(ghostObj);
                break;
            default:
                break;
        }
        switch (PB) {
            case "a":
                pInfo.eddTag(SkillDataBase.TAG_LIGHT_ATT);
                break;
            case "b":
                P_OBJS0.remove(pInfo);
                break;
            case "c":
                pInfo.eddTag(SkillDataBase.TAG_ADD_ATT);
                break;
            case "d":
                pInfo.eddTag(SkillDataBase.TAG_XX);
                break;
            default:
                break;
        }
    }
}
