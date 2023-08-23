package Project.aSpring.dao;


import Project.aSpring.SpringBootResource;
import Project.commons.gameEntitys.base.BaseInfo;
import Project.commons.gameEntitys.base.BaseInfoTemp;
import Project.dataBases.GameDataBase;
import Project.dataBases.skill.SkillDataBase;
import Project.services.detailServices.roles.v1.TagManagers;
import io.github.kloping.mirai0.commons.game.NormalTagPack;

import java.util.Iterator;

import static Project.commons.rt.ResourceSet.FinalValue.MAX_LEVEL;
import static Project.controllers.gameControllers.GameController.MAX_XP;

/**
 * @author github-kloping
 */
public class WhInfo implements BaseInfo {
    /**
     * 攻击
     */
    public Long att = 10L;
    /**
     * 魂力
     */
    public Long hl = 100L;
    /**
     * 魂力最大值
     */
    public Long hll = 100L;
    /**
     * 血量
     */
    public Long hp = 100L;
    /**
     * 最大血量
     */
    public Long hpl = 100L;
    /**
     * 精神力
     */
    public Long hj = 100L;
    /**
     * 最大精神力
     */
    public Long hjL = 100L;
    /**
     * 等级
     */
    public Integer Level = 1;
    /**
     * 武魂
     */
    public Integer wh = 0;
    /**
     * 武魂类型
     */
    public Integer whType = -1;
    /**
     * 经验
     */
    public Long xp = 0L;
    public Long xpL = 100L;
    /**
     * 主人
     */
    public Long qid;

    public Integer p;


    public Long getHpl() {
        return hpl;
    }

    public WhInfo setHpl(Long hpl) {
        this.hpl = hpl;
        return this;
    }

    public Long getQid() {
        return qid;
    }

    //====

    public void setQid(Long qid) {
        this.qid = qid;
    }

    public Integer getP() {
        return p;
    }

    public void setP(Integer p) {
        this.p = p;
    }

    @Override
    public Long getHj() {
        return hj;
    }

    public WhInfo setHj(Long hj) {
        this.hj = hj;
        return this;
    }

    @Override
    public Long getHjL() {
        return hjL;
    }

    public WhInfo setHjL(Long hjL) {
        this.hjL = hjL;
        return this;
    }

    @Override
    public Long getId() {
        return qid;
    }

    @Override
    public WhInfo addHj(Long l) {
        this.hj += l;
        if (hj < 0) {
            hj = 0L;
        }
        if (hj > hjL) {
            hj = hjL;
        }
        return this;
    }

    public WhInfo addHjL(Long l) {
        this.hjL += l;
        return this;
    }

    public Integer getWhType() {
        return GameDataBase.getWhTypeByWh(wh);
    }

    public WhInfo setWhType(Integer whType) {
        this.whType = whType;
        return this;
    }

    @Override
    public Long getAtt() {
        return att;
    }

    @Override
    public WhInfo setAtt(Long att) {
        this.att = att;
        return this;
    }

    public Long att() {
        long at1 = att;
        try {
            if (SkillDataBase.HAS_ADDER_MAP_LIST.containsKey(getId().longValue())) {
                if (!SkillDataBase.HAS_ADDER_MAP_LIST.get(getId().longValue()).isEmpty()) {
                    Iterator<SkillDataBase.HasTimeAdder> iterator = SkillDataBase.HAS_ADDER_MAP_LIST.get(getId().longValue()).iterator();
                    while (iterator.hasNext()) {
                        SkillDataBase.HasTimeAdder adder = iterator.next();
                        if (adder.test()) {
                            at1 += adder.getValue().longValue();
                        } else {
                            iterator.remove();
                        }
                    }
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return at1;
    }

    public Long getHl() {
        return hl;
    }

    public WhInfo setHl(Long hl) {
        this.hl = hl;
        return this;
    }

    public Long getHll() {
        return hll;
    }

    public WhInfo setHll(Long hll) {
        this.hll = hll;
        return this;
    }

    @Override
    public Long getHp() {
        return hp;
    }

    public WhInfo setHp(Long hp) {
        this.hp = hp;
        return this;
    }

    @Override
    public Long getHpL() {
        return hpl;
    }

    @Override
    public Integer getLevel() {
        return Level;
    }

    public WhInfo setLevel(Integer level) {
        this.Level = level;
        return this;
    }

    public int getWh() {
        return wh;
    }

    public WhInfo setWh(Integer wh) {
        this.wh = wh;
        return this;
    }

    public Long getXp() {
        return xp;
    }

    public WhInfo setXp(Long xp) {
        this.xp = xp;
        return this;
    }

    public Long getXpL() {
        if (getLevel() > 150) {
            return 99999999999L;
        } else if (getLevel() == 150) {
            return 200000000L;
        }
        return xpL;
    }

    public WhInfo setXpL(Long xpL) {
        this.xpL = xpL;
        return this;
    }

    public WhInfo addAtt(Long o) {
        att += o;
        return this;
    }

    public WhInfo addHl(Long o) {
        hl += o;
        if (hl > hll) {
            hl = hll;
        }
        if (hl <= 0) {
            hl = 0L;
        }
        return this;
    }

    public WhInfo addHll(Long o) {
        hll += o;
        return this;
    }

    @Override
    public WhInfo addHp(Long o) {
        hp += o;
        if (hp > hpl) {
            hp = hpl;
        }
        if (hp <= 0) {
            hp = 0L;
        }
        return this;
    }

    public WhInfo addHpl(Long o) {
        hpl += o;
        return this;
    }

    public WhInfo addLevel(Integer o) {
        Level += o;
        return this;
    }

    public WhInfo addXp(Long o) {
        xp += o;
        if (Level < MAX_LEVEL) {
            if (xp > xpL * MAX_XP) {
                this.xp = (long) (xpL * MAX_XP);
            }
        }
        return this;
    }

    public WhInfo addXpL(Long o) {
        xpL += o;
        return this;
    }

    //===
    @Override
    public WhInfo addTag(String myTag, Number percent, long t) {
        NormalTagPack pack = new NormalTagPack(myTag, t);
        pack.setValue(percent.longValue());
        pack.setQ(getId().longValue());
        TagManagers.getTagManager(getId().longValue()).addTag(pack);
        return this;
    }

    @Override
    public WhInfo addTag(String myTag, Number percent, Number max, long t) {
        long v0 = getTagValue(myTag).longValue();
        if (v0 >= max.longValue()) {
            return this;
        } else if (v0 + percent.longValue() >= max.longValue()) {
            NormalTagPack pack = new NormalTagPack(myTag, t);
            pack.setValue(max.longValue() - v0);
            pack.setQ(getId().longValue());
            TagManagers.getTagManager(getId().longValue()).addTag(pack);
            return this;
        } else {
            NormalTagPack pack = new NormalTagPack(myTag, t);
            pack.setValue(percent.longValue());
            pack.setQ(getId().longValue());
            TagManagers.getTagManager(getId().longValue()).addTag(pack);
            return this;
        }
    }

    @Override
    public WhInfo eddTag(String myTag, Number percent) {
        TagManagers.getTagManager(getId().longValue()).eddValue(myTag, percent.longValue());
        return this;
    }

    @Override
    public WhInfo eddTag(String myTag) {
        TagManagers.getTagManager(getId().longValue()).eddValue(myTag);
        return this;
    }

    @Override
    public boolean containsTag(String tag) {
        return TagManagers.getTagManager(getId().longValue()).contains(tag);
    }

    @Override
    public Number getTagValue(String tag) {
        return TagManagers.getTagManager(getId().longValue()).getValue(tag);
    }

    @Override
    public boolean isVertigo() {
        return BaseInfoTemp.isVertigo(getId().longValue());
    }

    @Override
    public WhInfo cancelVertigo() {
        BaseInfoTemp.removeVertigo(getId().longValue());
        return this;
    }

    public WhInfo cancelVertigo(long t) {
        BaseInfoTemp.removeVertigo(getId().longValue(), t);
        return this;
    }

    @Override
    public WhInfo letVertigo(long t) {
        BaseInfoTemp.letVertigo(getId().longValue(), t);
        return this;
    }

    @Override
    public WhInfo apply() {
        GameDataBase.putWhInfo(this);
        return this;
    }

    @Override
    public String getTips() {
        return null;
    }

    @Override
    public String getName() {
        return qid.toString();
    }

    //===========ext

    public void create() {
        SpringBootResource.getWhInfoMapper().insert(this);
    }
}