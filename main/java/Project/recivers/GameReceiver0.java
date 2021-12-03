package Project.recivers;

import Entitys.gameEntitys.GInfo;
import Entitys.gameEntitys.GhostObj;
import Entitys.gameEntitys.PersonInfo;
import Project.DataBases.GameDataBase;
import Project.broadcast.GhostLostBroadcast;
import Project.broadcast.GotOrLostObjBroadcast;
import Project.broadcast.JoinBroadcast;
import Project.broadcast.PlayerLostBroadcast;
import Project.broadcast.enums.ObjType;
import io.github.kloping.MySpringTool.StarterApplication;
import io.github.kloping.MySpringTool.annotations.Entity;

import java.lang.reflect.Method;

import static Project.Controllers.NormalController.NoticeController.lowst;
import static io.github.kloping.Mirai.Main.Resource.threads;

@Entity
public class GameReceiver0 {
    public GameReceiver0() {
        threads.execute(() -> {
            Method[] methods = this.getClass().getDeclaredMethods();
            for (Method method : methods) {
                if (method.getName().equals("lambda$new$0")) continue;
                try {
                    method.invoke(null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private static void init() {
        GhostLostBroadcast.INSTANCE.add(new GhostLostBroadcast.GhostLostReceiver() {
            @Override
            public void onReceive(long who, Long with, GhostObj ghostObj) {
                StarterApplication.logger.Log(String.format("ghost losted by %s level=%s with %s",
                        who, ghostObj.getL(), with), 0);
                if (with.longValue() == -1) {
                    if (ghostObj.getL() >= lowst * 10000L) {
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
                StarterApplication.logger.Log(String.format("%s,got/lost,%s,%s nums , from %s",
                        who, id, num, type.name()), 0);
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
                }
            }
        });
    }

    private static void init2() {
        PlayerLostBroadcast.INSTANCE.add(new PlayerLostBroadcast.PlayerLostReceiver() {
            @Override
            public void onReceive(long who, long from, type type) {
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
                GInfo.getInstance(who).addDiedc().apply();
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
}
