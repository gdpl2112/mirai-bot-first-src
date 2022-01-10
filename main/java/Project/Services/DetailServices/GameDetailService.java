package Project.Services.DetailServices;

import Entitys.gameEntitys.AttributeBone;
import Entitys.gameEntitys.GhostObj;
import Entitys.gameEntitys.PersonInfo;
import Project.Controllers.GameControllers.GameController;
import Project.DataBases.GameDataBase;
import Project.DataBases.skill.SkillDataBase;
import Project.Services.DetailServices.roles.Role;
import Project.Services.DetailServices.roles.Roles;
import Project.Services.Iservice.IGameBoneService;
import Project.Services.impl.GameBoneServiceImpl;
import Project.Tools.Tool;
import Project.broadcast.game.HpChangeBroadcast;
import Project.broadcast.game.PlayerLostBroadcast;
import io.github.kloping.Mirai.Main.Handlers.MyTimer;
import io.github.kloping.Mirai.Main.Resource;
import io.github.kloping.MySpringTool.annotations.AutoStand;
import io.github.kloping.MySpringTool.annotations.Entity;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static Project.DataBases.GameDataBase.*;
import static Project.DataBases.skill.SkillDataBase.*;

/**
 * @author github-kloping
 */
@Entity
public class GameDetailService {

    @AutoStand
    public static IGameBoneService gameBoneService;

    static {
        Resource.StartOkRuns.add(new Runnable() {
            @Override
            public void run() {
                MyTimer.ZeroRuns.add(() -> {
                    GameController.deleteC.clear();
                    File file = new File(GameDataBase.path + "/dates/users/");
                    for (File f1 : file.listFiles()) {
                        try {
                            String endN = f1.getName();
                            PersonInfo personInfo = getInfo(endN);
                            if (isNeedUpdate(personInfo)) {
                                putPerson(personInfo.setHelpC(0).setHelpToc(0).setBuyHelpC(0).setBuyHelpToC(0).setDied(false).setDowned(false));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    private static boolean isNeedUpdate(PersonInfo info) {
        return info.getBuyHelpC() > 0 || info.getHelpToc() > 0 || info.getHelpC() > 0 || info.getHelpToc() > 0 || info.isDied() || info.isDowned();
    }

    /**
     * 当被打了
     *
     * @param qq  我   被
     * @param qq2 ta 打了
     * @param o   这么多  血量
     * @return
     */
    public static synchronized String beaten(Number qq, Number qq2, final long o) {
        long oNow = o;
        StringBuilder sb = new StringBuilder();
        PersonInfo p1 = getInfo(qq);
        Map<String, Object> maps = new HashMap<>();
        for (Role r : Roles.RS) {
            Role.Response response = r.call(sb, qq, qq2, o, oNow, p1, maps);
            if (response != null) {
                oNow = response.getNowV();
                if (!response.getArgs().isEmpty()) {
                    maps.putAll(response.getArgs());
                }
                if (response.getState() == Role.State.STOP) {
                    break;
                }
            }
        }
        if (oNow > 0) {
            long v1 = p1.getHj();
            long v2 = p1.getHjL();
            int b = toPercent(v1, v2);
            long ev = 0;
            if (b > 80) {
                ev = percentTo(10, oNow);
                sb.append("\n精神力高于80%,额外抵挡10%的伤害\n============");
            } else if (b < 40) {
                ev = -percentTo(10, oNow);
                sb.append("\n精神力低于40%,额外受到10%的伤害\n============");
            } else if (b <= 1) {
                ev = 0;
            }
            if (ev != 0) {
                oNow -= ev;
            }
            //===== 消耗精神力 判断
            int sf = toPercent(oNow, p1.getHpl());
            sf = sf > 80 ? 80 : sf < 1 ? 1 : sf;
            long sv = percentTo(sf, p1.getHjL());
            p1.addHj(-sv);
            sb.append(String.format("\n消耗了%s精神力\n============", sv));
        }
        //=====广播
        HpChangeBroadcast.INSTANCE.broadcast(qq.longValue(), p1.getHp(), p1.getHp() - oNow,
                oNow, qq2.longValue()
                , qq2.longValue() > 0 ?
                        HpChangeBroadcast.HpChangeReceiver.type.fromQ :
                        HpChangeBroadcast.HpChangeReceiver.type.fromG
        );
        if (oNow > 0) {
            p1.addHp(-oNow);
            p1.apply();

            if (p1.hp <= 0) {
                PlayerLostBroadcast.INSTANCE.broadcast(qq.longValue(),
                        qq2.longValue(), PlayerLostBroadcast.PlayerLostReceiver.type.att);
            }
        }


        return sb.toString();
    }

    public static synchronized String consumedHl(long who, final long o) {
        PersonInfo personInfo = getInfo(who);
        StringBuilder sb = new StringBuilder();
        AttributeBone attributeBone = gameBoneService.getAttribute(who);
        long oNow = o;
        //=====恢复了
        if (proZ(attributeBone.getHl_pro())) {
            float fn;
            if (o > 100) {
                fn = percentTo(attributeBone.getHl_Rec_Eff(), o);
            } else {
                fn = attributeBone.getHl_Rec_Eff();
            }
            oNow -= fn;
            sb.append("\n得益于 魂骨 你恢复了" + fn + "魂力\n============");
        }
        personInfo.addHl(-oNow);
        putPerson(personInfo);
        return sb.toString();
    }

    public static boolean proZ(Integer n) {
        int i = Tool.rand.nextInt(100) + 1;
        return n >= i;
    }

    public static synchronized String onAtt(Number qq, Number qq2, Long v) {
        PersonInfo info = getInfo(qq);
        StringBuilder sb = new StringBuilder();
        //=====
        if (info.containsTag(SkillDataBase.tag_Xx)) {
            String tag = info.getTag(SkillDataBase.tag_Xx);
            Integer p = Integer.valueOf(tag.replace(SkillDataBase.tag_Xx, ""));
            long v1 = percentTo(p, v);
            if (v1 < 1) {
                v1 = 1;
            }
            info.addHp(v1);
            sb.append("\n攻击者,由于吸血技能恢复了 " + v1 + "的生命值\n============");
        }
        if (qq2.longValue() > 0) {
            PersonInfo info1 = getInfo(qq2);
            if (info.containsTag(tag_She) && info.containsTag(tag_Shield)) {
                int b = info.getTagValue(tag_She).intValue();
                long v2 = percentTo(b, v);
                putPerson(getInfo(qq2.longValue()).addHp(v2));
                sb.append("\n对有护盾的敌人额外造成").append(v2).append("伤害\n=========");
            }
        }
        putPerson(info);
        return "";
    }
}


