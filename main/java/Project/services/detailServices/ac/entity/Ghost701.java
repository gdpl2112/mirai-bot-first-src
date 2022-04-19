package Project.services.detailServices.ac.entity;

import Project.dataBases.GameDataBase;
import Project.services.detailServices.GameDetailService;
import io.github.kloping.MySpringTool.StarterApplication;
import io.github.kloping.mirai0.Main.ITools.MessageTools;
import io.github.kloping.mirai0.commons.apiEntitys.RunnableWithOver;
import io.github.kloping.mirai0.commons.gameEntitys.base.BaseInfo;

import java.util.Set;

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

    public Ghost701(long hp, long att, long xp, long id, long l) {
        super(hp, att, xp, id, l);
    }

    public Ghost701(long hp, long att, long xp, int id, long l, boolean rand, float bl) {
        super(hp, att, xp, id, l, rand, bl);
    }

    {
        Set<RunnableWithOver> runnable = StarterApplication.Setting.INSTANCE
                .getContextManager().getContextEntity(Set.class, "m100");
        runnable.add(new RunnableWithOver() {
            @Override
            public boolean over() {
                return Ghost701.this.getHp() <= 0;
            }

            private int index = 0;

            @Override
            public void run() {
                if (index++ % 50 == 0) {
                    baseInfo = baseInfo == null ? baseInfo = GameDataBase.getInfo(getWhoMeet()) : baseInfo;
                    if (baseInfo != null) {
                        long v = Ghost701.this.getHpL() / 100;
                        v = v <= 0 ? 1 : v;
                        MessageTools.sendMessageInGroupWithAt(
                                getName() + "对你造成" + v + "伤害"
                                        + GameDetailService.beaten(baseInfo.getId(), -1, v),
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
