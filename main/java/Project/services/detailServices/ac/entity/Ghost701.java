package Project.services.detailServices.ac.entity;

import Project.controllers.auto.ControllerSource;
import Project.services.detailServices.GameDetailService;
import Project.services.detailServices.ac.GameJoinDetailService;
import Project.services.detailServices.roles.DamageType;
import io.github.kloping.mirai0.Main.iutils.MessageUtils;
import io.github.kloping.mirai0.commons.apiEntitys.RunnableWithOver;
import io.github.kloping.mirai0.commons.gameEntitys.base.BaseInfo;

import static Project.dataBases.GameDataBase.getInfo;

/**
 * @author github-kloping
 * @version 1.0
 */
public class Ghost701 extends GhostWithGroup {

    private BaseInfo baseInfo;

    public Ghost701() {
    }

    public Ghost701(String forWhoStr) {
        super(forWhoStr);
    }

    public Ghost701(long hp, long att, long id, long l) {
        super(hp, att, id, l);
        init();
    }

    public Ghost701(long hp, long att, long xp, int id, long l, float bl) {
        super(hp, att, xp, id, l, bl);
        init();
    }

    private void init() {
        ControllerSource.m100.add(new RunnableWithOver() {
            private int index = 0;

            @Override
            public boolean over() {
                baseInfo = baseInfo == null ? baseInfo = getInfo(getWhoMeet()) : baseInfo;
                return Ghost701.this.getHp() <= 0 || baseInfo.getHp() <= 0 || GameJoinDetailService.getGhostObjFrom(getWhoMeet()) == null;
            }

            @Override
            public void run() {
                if (index++ % 100 == 0) {
                    baseInfo = baseInfo == null ? baseInfo = getInfo(getWhoMeet()) : baseInfo;
                    if (baseInfo != null) {
                        long v = Ghost701.this.getHpL() / 100;
                        v = v <= 0 ? 1 : v;
                        MessageUtils.INSTANCE.sendMessageInGroupWithAt(
                                getName() + "对你造成" + v + "伤害"
                                        + GameDetailService.beaten(baseInfo.getId(), -1, v, DamageType.AD),
                                group.getId(), baseInfo.getId().longValue()
                        );
                    }
                }
            }
        });
    }

    @Override
    public long updateHp(long l, BaseInfo who) {
        this.baseInfo = who;
        return super.updateHp(l, who);
    }

    @Override
    public void dispose() {
        setHp(0);
    }
}
