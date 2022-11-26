package Project.controllers.gameControllers;

import Project.broadcast.game.GhostLostBroadcast;
import Project.broadcast.game.PlayerLostBroadcast;
import Project.broadcast.game.SelectTaoPaoBroadcast;
import Project.dataBases.GameDataBase;
import Project.dataBases.SourceDataBase;
import Project.interfaces.http_api.KlopingWeb;
import Project.services.detailServices.condition.GameConditionDetailService;
import io.github.kloping.MySpringTool.annotations.*;
import io.github.kloping.MySpringTool.exceptions.NoRunException;
import io.github.kloping.mirai0.Main.BotStarter;
import io.github.kloping.mirai0.Main.ITools.MemberTools;
import io.github.kloping.mirai0.Main.ITools.MessageTools;
import io.github.kloping.mirai0.Main.Resource;
import io.github.kloping.mirai0.commons.GhostObj;
import io.github.kloping.mirai0.commons.Group;
import io.github.kloping.mirai0.commons.PersonInfo;
import io.github.kloping.mirai0.commons.User;
import io.github.kloping.mirai0.commons.broadcast.enums.ObjType;
import io.github.kloping.number.NumberUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static Project.dataBases.GameDataBase.getInfo;
import static Project.services.detailServices.ac.GameJoinDetailService.getGhostObjFrom;
import static io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet.FinalString.ERR_TIPS;
import static io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet.FinalString.IN_SELECT;

/**
 * @author github.kloping
 */
@Controller
public class GameConditionController {
    @AutoStand
    GameController gameController;

    @Before
    public void before(User qq, Group group, @AllMess String mess) throws NoRunException {
        if (!BotStarter.test) throw new NoRunException();
        gameController.before(qq, group, mess);
    }

    @AutoStand
    KlopingWeb klopingWeb;

    public static final Map<Long, Integer> CONDITIONING = new HashMap<>();

    {
        Resource.START_AFTER.add(() -> {
            GhostLostBroadcast.INSTANCE.add(new GhostLostBroadcast.GhostLostReceiver() {
                @Override
                public void onReceive(long who, Set<Long> withs, GhostObj ghostObj, GhostLostBroadcast.KillType killType) {
                    if (!CONDITIONING.containsKey(who))
                        return;
                    int i = CONDITIONING.get(who);
                    if (i >= 3) {
                        CONDITIONING.remove(who);
                        long gid = MemberTools.getRecentSpeechesGid(who);
                        MessageTools.instance.sendMessageInGroupWithAt("挑战成功", gid, who);
                        String iv = klopingWeb.get(String.valueOf(who), GameConditionDetailService.PWD);
                        Integer c = Integer.valueOf(i);
                        if (c >= 3) {
                            MessageTools.instance.sendMessageInGroupWithAt("奖励上限", gid, who);
                        } else {
                            GameDataBase.addToBgs(who, 0, 10, ObjType.got);
                            MessageTools.instance.sendMessageInGroupWithAt("获得奖励" +
                                    SourceDataBase.getImgPathById(0), gid, who);
                        }
                    } else {
                        PersonInfo pInfo = getInfo(who);
                        long v = NumberUtils.percentTo(40, pInfo.getHpL());
                        pInfo.addHp(v);
                        pInfo.apply();
                        detailService.run(who, ++i);
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
                    long gid = MemberTools.getRecentSpeechesGid(who);
                    MessageTools.instance.sendMessageInGroupWithAt("挑战失败", gid, who);
                }
            });
            SelectTaoPaoBroadcast.INSTANCE.add(new SelectTaoPaoBroadcast.SelectTaoPaoReceiver() {
                @Override
                public boolean onReceive(long q1, GhostObj ghostObj) {
                    if (!CONDITIONING.containsKey(q1))
                        return false;
                    CONDITIONING.remove(q1);
                    long gid = MemberTools.getRecentSpeechesGid(q1);
                    MessageTools.instance.sendMessageInGroupWithAt("挑战失败", gid, q1);
                    return false;
                }
            });
        });
    }

    public static final String TIPS0 = "15秒后进入下阶段";

    @Action("进入遇境")
    public Object join(long qid) {
        if (CONDITIONING.containsKey(qid)) return ERR_TIPS;
        CONDITIONING.put(qid, 1);
        GhostObj ghostObj = getGhostObjFrom(qid);
        if (ghostObj != null) {
            if (ghostObj.getTime() <= System.currentTimeMillis()) {
                return IN_SELECT;
            }
        }
        detailService.run(qid, 1);
        return TIPS0;
    }

    @AutoStand
    GameConditionDetailService detailService;

    @Action("遇境说明")
    public String getIntro() {
        return detailService.getBuffIntro();
    }
}
