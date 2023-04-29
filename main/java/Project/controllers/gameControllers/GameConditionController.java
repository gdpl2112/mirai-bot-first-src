package Project.controllers.gameControllers;

import Project.broadcast.game.GhostLostBroadcast;
import Project.broadcast.game.PlayerLostBroadcast;
import Project.broadcast.game.SelectTaoPaoBroadcast;
import Project.commons.SpGroup;
import Project.commons.SpUser;
import Project.commons.broadcast.enums.ObjType;
import Project.dataBases.GameDataBase;
import Project.dataBases.SourceDataBase;
import Project.interfaces.httpApi.KlopingWeb;
import Project.services.detailServices.condition.GameConditionDetailService;
import io.github.kloping.MySpringTool.annotations.*;
import io.github.kloping.MySpringTool.exceptions.NoRunException;
import io.github.kloping.judge.Judge;
import io.github.kloping.mirai0.Main.BootstarpResource;
import io.github.kloping.mirai0.Main.BotStarter;
import io.github.kloping.mirai0.Main.iutils.MemberUtils;
import io.github.kloping.mirai0.Main.iutils.MessageUtils;
import io.github.kloping.mirai0.commons.GhostObj;
import io.github.kloping.mirai0.commons.PersonInfo;
import io.github.kloping.number.NumberUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static Project.commons.rt.ResourceSet.FinalString.ERR_TIPS;
import static Project.commons.rt.ResourceSet.FinalString.IN_SELECT;
import static Project.dataBases.GameDataBase.getInfo;
import static Project.services.detailServices.ac.GameJoinDetailService.getGhostObjFrom;

/**
 * @author github.kloping
 */
@Controller
public class GameConditionController {
    public static final Map<Long, Integer> CONDITIONING = new HashMap<>();
    public static final String TIPS0 = "15秒后进入下阶段";
    @AutoStand
    static KlopingWeb klopingWeb;
    @AutoStand
    static GameConditionDetailService detailService;

    static {
        BootstarpResource.START_AFTER.add(() -> {
            GhostLostBroadcast.INSTANCE.add(new GhostLostBroadcast.GhostLostReceiver() {
                @Override
                public void onReceive(long who, Set<Long> withs, GhostObj ghostObj, GhostLostBroadcast.KillType killType) {
                    if (!CONDITIONING.containsKey(who))
                        return;
                    int i = CONDITIONING.get(who);
                    if (i == 3) {
                        CONDITIONING.remove(who);
                        long gid = MemberUtils.getRecentSpeechesGid(who);
                        MessageUtils.INSTANCE.sendMessageInGroupWithAt("挑战成功", gid, who);
                        String iv = klopingWeb.get(String.valueOf(who), GameConditionDetailService.PWD);
                        if (Judge.isEmpty(iv)) iv = "0";
                        Integer c = Integer.valueOf(iv);
                        if (c >= 3) {
                            MessageUtils.INSTANCE.sendMessageInGroupWithAt("奖励上限", gid, who);
                        } else {
                            GameDataBase.addToBgs(who, 0, 10, ObjType.got);
                            MessageUtils.INSTANCE.sendMessageInGroupWithAt("获得奖励" +
                                    SourceDataBase.getImgPathById(0), gid, who);
                            klopingWeb.put(String.valueOf(who), String.valueOf(c + 1), GameConditionDetailService.PWD);
                        }
                    } else {
                        i = i + 1;
                        PersonInfo pInfo = getInfo(who);
                        long v = NumberUtils.percentTo(40, pInfo.getHpL());
                        pInfo.addHp(v);
                        long v1 = NumberUtils.percentTo(40, pInfo.getHll());
                        pInfo.addHl(v1);
                        pInfo.apply();
                        detailService.run(who, i);
                        CONDITIONING.put(who, i);
                    }
                }
            });
            PlayerLostBroadcast.INSTANCE.add(new PlayerLostBroadcast.PlayerLostReceiver() {
                @Override
                public void onReceive(long who, long from, LostType type) {
                    if (!CONDITIONING.containsKey(who))
                        return;
                    CONDITIONING.remove(who);
                    long gid = MemberUtils.getRecentSpeechesGid(who);
                    MessageUtils.INSTANCE.sendMessageInGroupWithAt("挑战失败", gid, who);
                }
            });
            SelectTaoPaoBroadcast.INSTANCE.add(new SelectTaoPaoBroadcast.SelectTaoPaoReceiver() {
                @Override
                public boolean onReceive(long q1, GhostObj ghostObj) {
                    if (!CONDITIONING.containsKey(q1))
                        return false;
                    CONDITIONING.remove(q1);
                    long gid = MemberUtils.getRecentSpeechesGid(q1);
                    MessageUtils.INSTANCE.sendMessageInGroupWithAt("挑战失败", gid, q1);
                    return false;
                }
            });
        });
    }

    @AutoStand
    GameController gameController;

    @Before
    public void before(SpUser qq, SpGroup group, @AllMess String mess) throws NoRunException {
        if (!BotStarter.test) throw new NoRunException();
        gameController.before(qq, group, mess);
    }

    @Action("进入遇境")
    public Object join(long qid) {
        int i = 1;
        if (CONDITIONING.containsKey(qid)) return ERR_TIPS;
        CONDITIONING.put(qid, i);
        GhostObj ghostObj = getGhostObjFrom(qid);
        if (ghostObj != null) {
            if (ghostObj.getTime() <= System.currentTimeMillis()) {
                return IN_SELECT;
            }
        }
        detailService.run(qid, i);
        return "准备中...";
    }

    @Action("遇境说明")
    public String getIntro() {
        return detailService.getBuffIntro();
    }
}
