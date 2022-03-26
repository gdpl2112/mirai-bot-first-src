package Project.recivers;

import Project.broadcast.game.*;
import Project.broadcast.normal.MemberJoinedBroadcast;
import Project.controllers.normalController.EntertainmentController3;
import Project.dataBases.GameDataBase;
import Project.dataBases.OtherDatabase;
import io.github.kloping.MySpringTool.StarterApplication;
import io.github.kloping.MySpringTool.annotations.AutoStand;
import io.github.kloping.mirai0.Main.ITools.MessageTools;
import io.github.kloping.mirai0.commons.*;
import io.github.kloping.mirai0.commons.broadcast.enums.ObjType;
import io.github.kloping.mirai0.commons.gameEntitys.SkillInfo;
import io.github.kloping.mirai0.commons.task.Task;

import static Project.controllers.normalController.NoticeController.lowst;

/**
 * @author github-kloping
 */
public class GameReceiver0 {
    @AutoStand
    static EntertainmentController3 entertainmentController3;

    public GameReceiver0() {
        init();
        init1();
        init2();
        init3();
        init4();
        init5();
        init6();
        init7();
    }

    private static void init() {
        GhostLostBroadcast.INSTANCE.add(new GhostLostBroadcast.GhostLostReceiver() {
            @Override
            public void onReceive(long who, Long with, GhostObj ghostObj, GhostLostBroadcast.KillType killType) {
                StarterApplication.logger.info(String.format("ghost losted by %s level=%s with %s",
                        who, ghostObj.getL(), with));
                if (with.longValue() == -1) {
                    if (ghostObj.getL() >= lowst * 10000L) {
                        StarterApplication.logger.info(String.format("add master point %s ", who));
                        GInfo.getInstance(who).addMasterPoint().apply();
                    }
                }
            }
        });
    }

    private static void init1() {
        GotOrLostObjBroadcast.INSTANCE.add(new GotOrLostObjBroadcast.GotOrLostReceiver() {
            @Override
            public void onReceive(long who, int id, int num, ObjType type) {
                StarterApplication.logger.log(String.format("%s,got/lost,%s,%s nums , from %s",
                        who, id, num, type.name()));
                switch (type) {
                    case buy:
                    case got:
                    case transGot:
                    case un:
                        GInfo.getInstance(who).addGotc(num).apply();
                        break;
                    case use:
                    case sell:
                    case transLost:
                        GInfo.getInstance(who).addLostc(num).apply();
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private static void init2() {
        PlayerLostBroadcast.INSTANCE.add(new PlayerLostBroadcast.PlayerLostReceiver() {
            @Override
            public void onReceive(long who, long from, LostType type) {
                StarterApplication.logger.log(String.format("%s,lost, from %s by %s",
                        who, from, type.name()));
                switch (type) {
                    case att:
                        lost(who);
                        GInfo.getInstance(who).addDiedc().apply();
                        break;
                    case un:
                        GInfo.getInstance(who).addDiedc().apply();
                        break;
                    default:
                }
            }

            public void lost(long who) {
                PersonInfo pi = GameDataBase.getInfo(who);
                if (pi.hp <= 0) {
                    if (pi.dt1 <= System.currentTimeMillis()) {
                        if (pi.died) {
                            if (pi.Level % 10 == 0 || pi.downed) {
                                pi.xp = 0L;
                            } else {
                                pi.Level--;
                                pi.downed = true;
                            }
                        } else {
                            pi.xp = 0L;
                            pi.died = true;
                        }
                        pi.dt1 = System.currentTimeMillis() + 1000 * 60 * 5;
                    }
                    GameDataBase.putPerson(pi);
                }
            }
        });
    }

    private static void init3() {
        JoinBroadcast.INSTANCE.add(new JoinBroadcast.JoinReceiver() {
            @Override
            public void onReceive(long who, int type) {
                GInfo.getInstance(who).addJoinc().apply();
            }
        });
    }

    private static void init4() {
        SkillUseBroadcast.INSTANCE.add(new SkillUseBroadcast.SkillUseReceiver() {
            @Override
            public void onReceive(long who, int jid, int st, SkillInfo info) {
                GInfo.getInstance(who).addUseskillc().apply();
            }
        });
    }

    private static void init5() {
        GameTaskBroadcast.INSTANCE.add(new GameTaskBroadcast.GameTaskReceiver() {
            @Override
            public void onReceive(long who, int taskId, Type type, Task task) {
            }
        });
    }

    public static void init6() {
        RecordBroadcast.INSTANCE.add(new RecordBroadcast.RecordReceiver() {
            @Override
            public void onReceiver(long who, TradingRecord record) {
                OtherDatabase.insert(record);
            }
        });
    }

    /**
     * 注册新人加群
     */
    public static void init7() {
        MemberJoinedBroadcast.INSTANCE.add(new MemberJoinedBroadcast.MemberJoinedReceiver() {
            @Override
            public void onReceive(long q, long g) {
                String m = entertainmentController3.o3("/赞[@" + q + "]", Group.get(g), q).toString();
                MessageTools.sendMessageInGroupWithAt(m, g, q);
            }
        });
    }
}
