package Project.services.DetailServices.ac.entity;

import Project.services.DetailServices.GameDetailService;
import io.github.kloping.MySpringTool.StarterApplication;
import io.github.kloping.mirai0.Entitys.Group;
import io.github.kloping.mirai0.Entitys.apiEntitys.RunnableWithOver;
import io.github.kloping.mirai0.Entitys.gameEntitys.GhostObj;
import io.github.kloping.mirai0.Entitys.gameEntitys.base.BaseInfo;
import io.github.kloping.mirai0.Main.ITools.MessageTools;

import java.util.Set;

/**
 * @author github-kloping
 * @version 1.0
 */
public class Ghost701 extends GhostWithGroup {

    private BaseInfo baseInfo;

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
                    if (baseInfo != null) {
                        long v = Ghost701.this.getHpL() / 100;
                        MessageTools.sendMessageInGroupWithAt(
                                GameDetailService.beaten(baseInfo.getId(), -1, v),
                                group.getId(), baseInfo.getId().longValue()
                        );
                    }
                }
            }
        });
    }

    public Ghost701() {
    }

    public Ghost701(String forWhoStr) {
        super(forWhoStr);
    }

    public Ghost701(long hp, long att, long xp, long id, long l) {
        super(hp, att, xp, id, l);
    }

    public Ghost701(long hp, long att, long xp, int idMin, int idMax, long l, boolean rand, float bl) {
        super(hp, att, xp, idMin, idMax, l, rand, bl);
    }

    @Override
    public long updateHp(long l, BaseInfo who) {
        this.baseInfo = who;
        return super.updateHp(l, who);
    }
}
