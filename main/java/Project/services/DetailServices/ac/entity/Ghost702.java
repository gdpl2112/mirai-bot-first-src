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
public class Ghost702 extends GhostWithGroup {
    private Long shield = -1L;


    public Long getShield() {
        return shield;
    }

    public void setShield(Long shield) {
        this.shield = shield;
    }

    @Override
    public long updateHp(long l, BaseInfo who) {
        if (shield.longValue() == -1) {

        }
        return super.updateHp(l, who);
    }
}
