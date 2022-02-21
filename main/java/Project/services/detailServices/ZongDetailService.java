package Project.services.detailServices;

import io.github.kloping.mirai0.Entitys.gameEntitys.PersonInfo;
import io.github.kloping.mirai0.Entitys.gameEntitys.Zon;
import io.github.kloping.mirai0.Entitys.gameEntitys.Zong;
import Project.dataBases.ZongMenDataBase;
import io.github.kloping.mirai0.Main.Resource;
import io.github.kloping.MySpringTool.annotations.Entity;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static Project.controllers.TimerController.ZERO_RUNS;
import static Project.dataBases.GameDataBase.getInfo;
import static Project.dataBases.GameDataBase.putPerson;
import static Project.dataBases.ZongMenDataBase.*;

/**
 * @author github-kloping
 */
@Entity
public class ZongDetailService {

    private static ExecutorService threads = Executors.newFixedThreadPool(10);

    public static void onKilled(Long who, Long xp) {
        threads.execute(() -> {
            try {
                startShare(who, xp);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private static void startShare(Long who, Long xp) {
        threads.execute(new Runnable() {
            private final Long aXp = xp;
            private final Long aWho = who;

            @Override
            public void run() {
                if (!qq2id.containsKey(who))
                    return;
                Zong zong = getZongInfo(who);
                if (zong.getLevel() < 3)
                    return;
                int t = zong.getLevel() - 2;
                t = t > 1 ? 2 : 1;
                for (Number z1 : zong.getMember()) {
                    Long qq = null;
                    if (z1 instanceof Integer) {
                        qq = Long.valueOf(z1 + "");
                    } else
                        qq = z1.longValue();
                    if (qq == who) continue;
                    PersonInfo info = getInfo(qq);
                    if (info.getLevel() >= 150) continue;
                    int level = info.getLevel();
                    float bt = level / 1000f;
                    if (bt < 0.01f * t)
                        bt = 0.01f * t;
                    else if (bt > 0.1f * t)
                        bt = 0.1f * t;
                    long xpr = (long) (xp * bt);
                    int num = zong.getElders();
                    if (num >= 6) {
                        if (info.getLevel() > 100) {
                            if (xpr > info.getXpL() / 3 / ((int) num / 3)) {
                                xpr = info.getXpL() / 3 / ((int) num / 3);
                            }
                        } else if (info.getLevel() >= 120) {
                            if (xpr > info.getXpL() / 4 / ((int) num / 3)) {
                                xpr = info.getXpL() / 4 / ((int) num / 3);
                            }
                        } else if (xpr > info.getXpL() / 2 / ((int) num / 3)) {
                            xpr = info.getXpL() / 2 / ((int) num / 3);
                        }
                    } else {
                        if (info.getLevel() > 100) {
                            if (xpr > info.getXpL() / 3) {
                                xpr = info.getXpL() / 3;
                            }
                        } else if (info.getLevel() >= 120) {
                            if (xpr > info.getXpL() / 4) {
                                xpr = info.getXpL() / 4;
                            }
                        } else if (xpr > info.getXpL() / 2) {
                            xpr = info.getXpL() / 2;
                        }
                    }
                    if (xpr < 20)
                        xpr = 20;
                    if (t == 2)
                        xpr *= 0.15f;
                    System.out.println(who + "得到" + xp + "====共享到" + z1 + "===>" + xpr + "点");
                    info.addXp(xpr);
                    putPerson(info);
                    try {
                        Thread.sleep(5 * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    static {
        Resource.START_AFTER.add(() -> {
            ZERO_RUNS.add(() -> {
                File[] files = new File(ZongMenDataBase.path).listFiles();
                for (File file : files) {
                    if (file.getName().startsWith("t")) continue;
                    Zong zong = getZongInfo(Integer.valueOf(file.getName()));
                    ZongDetailService.update(zong);
                }
            });
        });
    }

    public static void update(Zong zong) {
        Zon zon0;
        switch (zong.getLevel()) {
            case 1:
                Zon zon = getZonInfo(zong.getMain().longValue());
                zon.setTimes(1);
                putZonInfo(zon);
                return;
            case 2:
            case 3:
            case 4:
                zon0 = getZonInfo(zong.getMain().longValue());
                zon0.setTimes(1);
                putZonInfo(zon0);
                for (Number who1 : zong.getElder()) {
                    Long who = Long.valueOf(who1 + "");
                    Zon zon1 = getZonInfo(who);
                    zon1.setTimes(1);
                    putZonInfo(zon1);
                }
                return;
            case 5:
                zon0 = getZonInfo(zong.getMain().longValue());
                zon0.setTimes(1);
                putZonInfo(zon0);
                for (Number who1 : zong.getElder()) {
                    Long who = who1.longValue();
                    Zon zon1 = getZonInfo(who);
                    zon1.setTimes(1);
                    putZonInfo(zon1);
                }
                zong.setPub(5);
                putZongInfo(zong);
                break;
            case 6:
                zon0 = getZonInfo(zong.getMain().longValue());
                zon0.setTimes(1);
                putPerson(getInfo(zon0.getQq()).setHelpC(-1).setHelpToc(-1));
                putZonInfo(zon0);
                for (Number who1 : zong.getElder()) {
                    Long who = who1.longValue();
                    Zon zon1 = getZonInfo(who);
                    putPerson(getInfo(who).setHelpC(-1).setHelpToc(-1));
                    zon1.setTimes(1);
                    putZonInfo(zon1);
                }
                zong.setPub(5);
                putZongInfo(zong);
                break;
        }
    }

    public static boolean quite(long who) {
        try {
            Zong zong = getZongInfo(who);
            Zon zon = getZonInfo(who);
            if (zon.getLevel() == 2) {
                for (Number z1 : zong.getMember()) {
                    long z = z1.longValue();
                    qq2id.remove(z);
                }
                File file = new File(path + "/" + zong.getId());
                for (File f1 : file.listFiles()) {
                    f1.delete();
                }
                file.delete();
                getInfo(who).setJk1(System.currentTimeMillis() + 1000 * 60 * 30).apply();
                return true;
            } else if (zon.getLevel() == 1) {
                zong.getElder().remove(who);
                zong.getMember().remove(who);
                qq2id.remove(who);
                File file = new File(path + "/" + zong.getId() + "/" + zon.getQq() + ".json");
                putPerson(getInfo(who).setJk1(System.currentTimeMillis() + 1000 * 60 * 15));
                putZongInfo(zong);
                return file.delete() & putZongInfo(zong);
            } else if (zon.getLevel() == 0) {
                qq2id.remove(who);
                zong.getMember().remove(who);
                putZongInfo(zong);
                File file = new File(path + "/" + zong.getId() + "/" + zon.getQq() + ".json");
                return file.delete() & putZongInfo(zong);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
