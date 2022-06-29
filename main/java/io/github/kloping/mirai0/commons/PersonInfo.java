package io.github.kloping.mirai0.commons;


import Project.broadcast.game.RecordBroadcast;
import Project.dataBases.GameDataBase;
import Project.dataBases.skill.SkillDataBase;
import Project.services.detailServices.roles.v1.TagManagers;
import com.baomidou.mybatisplus.annotation.TableId;
import io.github.kloping.mirai0.commons.game.NormalTagPack;
import io.github.kloping.mirai0.commons.gameEntitys.base.BaseInfo;
import io.github.kloping.mirai0.commons.gameEntitys.base.BaseInfoTemp;

import java.lang.reflect.Field;
import java.util.Iterator;

import static Project.controllers.gameControllers.GameController.MAX_XP;
import static io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet.FinalValue.MAX_LEVEL;

/**
 * @author github-kloping
 */
public class PersonInfo implements BaseInfo {
    /**
     * 攻击
     */
    public Long att = 10L;
    /**
     * 金魂币
     */
    public Long gold = 200L;
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
     * 支援次数
     */
    public Integer helpToc = 0;
    /**
     * 请求支援次数
     */
    public Integer helpC = 0;
    /**
     * 购买请求次数
     */
    public Integer BuyHelpC = 0;
    /**
     * 购买支援次数
     */
    public Integer BuyHelpToC = 0;
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
    /**
     * 修炼冷却
     */
    public Long k1 = 1L;
    /**
     * 进入冷却
     */
    public Long k2 = 1L;
    /**
     * 购买 冷却
     */
    public Long gk1 = 1L;
    /**
     * 使用冷却
     */
    public Long uk1 = 1L;
    /**
     * 加入宗门冷却
     */
    public Long jk1 = 1L;
    /**
     * 宗门贡献冷却
     */
    public Long Cbk1 = 1L;
    /**
     * 封号修改冷却
     */
    public Long mk1 = 1L;
    /**
     * 攻击冷却
     */
    public Long ak1 = 1L;
    /**
     * 选择攻击冷却
     */
    public Long jak1 = 1L;
    /**
     * 经验最大值 冷却
     */
    public Long xpL = 100L;
    /**
     * 使用 物品 标志
     */
    public String usinged = "null";
    /**
     * 名字QQ
     */
    @TableId
    public String name = null;
    /**
     * 下次进入星斗 R
     */
    public Integer nextR1 = -1;
    /**
     * 下次进入 极北
     */
    public Integer nextR2 = -1;
    /**
     * 下次进 落日
     */
    public Integer nextR3 = -1;
    /**
     * 封号名字
     */
    public String Sname = "";
    /**
     * 标记
     */
    public String myTag = "";
    /**
     * 死过
     */
    public boolean died = false;
    /**
     * 降级
     */
    public boolean downed = false;

    /**
     * 下次血量为空生效时
     */
    public Long dt1 = 0L;

    /**
     * 斗魂中...
     */
    private Boolean temp = false;

    /**
     * 勋章数
     */
    private Integer winC = 0;

    private boolean bg = false;
    private Long bgk = 0L;

    public boolean isBg() {
        return bg;
    }

    public PersonInfo setBg(boolean bg) {
        this.bg = bg;
        return this;
    }

    public Long getBgk() {
        return bgk;
    }

    public PersonInfo setBgk(Long bgk) {
        this.bgk = bgk;
        return this;
    }

    public Boolean getTemp() {
        return temp;
    }

    public void setTemp(Boolean temp) {
        this.temp = temp;
    }

    public Integer getWinC() {
        return winC;
    }

    public PersonInfo addWinc(Integer num) {
        winC++;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        try {
            Field[] fields = PersonInfo.class.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                sb.append(field.getName()).append("=").append(field.get(this)).append("\r\n");
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public Integer getNextR1() {
        return nextR1;
    }

    public PersonInfo setNextR1(Integer nextR1) {
        this.nextR1 = nextR1;
        return this;
    }

    public Integer getNextR2() {
        return nextR2;
    }

    public PersonInfo setNextR2(Integer nextR2) {
        this.nextR2 = nextR2;
        return this;
    }

    public Long getUk1() {
        return uk1;
    }

    public PersonInfo setUk1(Long uk1) {
        this.uk1 = uk1;
        return this;
    }

    @Override
    public String getName() {
        return name;
    }

    public PersonInfo setName(String name) {
        this.name = name;
        return this;
    }

    public Long getGk1() {
        return gk1;
    }

    public PersonInfo setGk1(Long gk1) {
        this.gk1 = gk1;
        return this;
    }

    @Override
    public Long getAtt() {
        return att;
    }

    @Override
    public PersonInfo setAtt(Long att) {
        this.att = att;
        return this;
    }

    public Long att() {
        long at1 = att;
        try {
            if (SkillDataBase.HAS_ADDER_MAP_LIST.containsKey(getId().longValue())) {
                if (!SkillDataBase.HAS_ADDER_MAP_LIST.get(getId().longValue()).isEmpty()) {
                    Iterator<SkillDataBase.HasTimeAdder> iterator =
                            SkillDataBase.HAS_ADDER_MAP_LIST.get(getId().longValue()).iterator();
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

    public Long getGold() {
        return gold;
    }

    public PersonInfo setGold(Long gold) {
        this.gold = gold;
        return this;
    }

    public Long getHl() {
        return hl;
    }

    public PersonInfo setHl(Long hl) {
        this.hl = hl;
        return this;
    }

    public Long getHll() {
        return hll;
    }

    public PersonInfo setHll(Long hll) {
        this.hll = hll;
        return this;
    }

    @Override
    public Long getHp() {
        return hp;
    }

    public PersonInfo setHp(Long hp) {
        this.hp = hp;
        return this;
    }

    @Override
    public Long getHpL() {
        return hpl;
    }

    public PersonInfo setHpl(Long hpl) {
        this.hpl = hpl;
        return this;
    }

    public Long getK1() {
        return k1;
    }

    public PersonInfo setK1(Long k1) {
        this.k1 = k1;
        return this;
    }

    public Long getK2() {
        return k2;
    }

    public PersonInfo setK2(Long k2) {
        this.k2 = k2;
        return this;
    }

    @Override
    public Integer getLevel() {
        return Level;
    }

    public PersonInfo setLevel(Integer level) {
        this.Level = level;
        return this;
    }

    public int getWh() {
        return wh;
    }

    public PersonInfo setWh(Integer wh) {
        this.wh = wh;
        return this;
    }

    public Long getXp() {
        return xp;
    }

    public PersonInfo setXp(Long xp) {
        this.xp = xp;
        return this;
    }

    public Long getXpL() {
        if (getLevel() >= 150) {
            return 99999999999L;
        }
        return xpL;
    }

    public PersonInfo setXpL(Long xpL) {
        this.xpL = xpL;
        return this;
    }

    public String getUsinged() {
        return usinged;
    }

    public PersonInfo setUsinged(String usinged) {
        this.usinged = usinged;
        return this;
    }

    public PersonInfo addAtt(Long o) {
        att += o;
        return this;
    }

    public PersonInfo addGold(Long o, TradingRecord tradingRecord) {
        gold += o;
        RecordBroadcast.INSTANCE.broadcast(Long.parseLong(name), tradingRecord.setNow(gold));
        return this;
    }

    public PersonInfo addHl(Long o) {
        hl += o;
        if (hl > hll) {
            hl = hll;
        }
        if (hl <= 0) {
            hl = 0L;
        }
        return this;
    }

    public PersonInfo addHll(Long o) {
        hll += o;
        return this;
    }

    public boolean isDowned() {
        return downed;
    }

    public PersonInfo setDowned(boolean downed) {
        this.downed = downed;
        return this;
    }

    @Override
    public PersonInfo addHp(Long o) {
        hp += o;
        if (hp > hpl) {
            hp = hpl;
        }
        if (hp <= 0) {
            hp = 0L;
        }
        return this;
    }

    public boolean isDied() {
        return died;
    }

    public PersonInfo setDied(boolean died) {
        this.died = died;
        return this;
    }

    public PersonInfo addHpl(Long o) {
        hpl += o;
        return this;
    }

    public PersonInfo addK1(Long o) {
        k1 += o;
        return this;
    }

    public PersonInfo addK2(Long o) {
        k2 += o;
        return this;
    }

    public PersonInfo addLevel(Integer o) {
        Level += o;
        return this;
    }

    public PersonInfo addXp(Long o) {
        xp += o;
        if (Level < MAX_LEVEL) {
            if (xp > xpL * MAX_XP) {
                this.xp = (long) (xpL * MAX_XP);
            }
        }
        return this;
    }

    public PersonInfo addGk1(Long o) {
        gk1 += o;
        return this;
    }

    public PersonInfo addXpL(Long o) {
        xpL += o;
        return this;
    }

    public String getSname() {
        return Sname;
    }

    public PersonInfo setSname(String sname) {
        Sname = sname;
        return this;
    }

    public Long getJk1() {
        return jk1;
    }

    public PersonInfo setJk1(Long jk1) {
        this.jk1 = jk1;
        return this;
    }

    public Long getCbk1() {
        return Cbk1;
    }

    public PersonInfo setCbk1(Long cbk1) {
        Cbk1 = cbk1;
        return this;
    }

    public PersonInfo addCbk1(Long cbk1) {
        Cbk1 += cbk1;
        return this;
    }

    public Long getMk1() {
        return mk1;
    }

    public PersonInfo setMk1(Long mk1) {
        this.mk1 = mk1;
        return this;
    }

    public Long getAk1() {
        return ak1;
    }

    public PersonInfo setAk1(Long ak1) {
        this.ak1 = ak1;
        return this;
    }

    @Override
    public PersonInfo addTag(String myTag, Number percent, long t) {
        NormalTagPack pack = new NormalTagPack(myTag, t);
        pack.setValue(percent.longValue());
        pack.setQ(getId().longValue());
        TagManagers.getTagManager(getId().longValue()).addTag(pack);
        return this;
    }

    @Override
    public PersonInfo addTag(String myTag, Number percent, Number max, long t) {
        long v0 = getTagValue(myTag).longValue();
        if (v0 >= max.longValue()) {
            return this;
        } else if (v0 + percent.longValue() > max.longValue()) {
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
    public PersonInfo eddTag(String myTag, Number percent) {
        TagManagers.getTagManager(getId().longValue()).eddValue(myTag, percent.longValue());
        return this;
    }

    @Override
    public PersonInfo eddTag(String myTag) {
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

    public Integer getHelpToc() {
        return helpToc;
    }

    public PersonInfo setHelpToc(Integer helpToc) {
        if (helpToc > this.helpToc) {
            return this;
        }
        this.helpToc = helpToc;
        return this;
    }

    public Integer getHelpC() {
        return helpC;
    }

    public PersonInfo setHelpC(Integer helpC) {
        this.helpC = helpC;
        return this;
    }

    public PersonInfo addHelpC() {
        helpC++;
        return this;
    }

    public PersonInfo addHelpToC() {
        helpToc++;
        return this;
    }

    public PersonInfo addBuyHelpC() {
        BuyHelpC++;
        return this;
    }

    public PersonInfo addBuyHelpToC() {
        BuyHelpToC++;
        return this;
    }

    public Integer getBuyHelpC() {
        return BuyHelpC;
    }

    public PersonInfo setBuyHelpC(Integer buyHelpC) {
        BuyHelpC = buyHelpC;
        return this;
    }

    public Integer getBuyHelpToC() {
        return BuyHelpToC;
    }

    public PersonInfo setBuyHelpToC(Integer buyHelpToC) {
        BuyHelpToC = buyHelpToC;
        return this;
    }

    public String getMyTag() {
        return myTag;
    }

    public PersonInfo setMyTag(String myTag) {
        this.myTag = myTag;
        return this;
    }

    public Long getJak1() {
        return jak1;
    }

    public PersonInfo setJak1(Long jak1) {
        this.jak1 = jak1;
        return this;
    }

    @Override
    public Long getHj() {
        return hj;
    }

    public PersonInfo setHj(Long hj) {
        this.hj = hj;
        return this;
    }

    @Override
    public Long getHjL() {
        return hjL;
    }

    public PersonInfo setHjL(Long hjL) {
        this.hjL = hjL;
        return this;
    }

    @Override
    public Number getId() {
        return Long.parseLong(name);
    }

    @Override
    public PersonInfo addHj(Long l) {
        this.hj += l;
        if (hj < 0) {
            hj = 0L;
        }
        if (hj > hjL) {
            hj = hjL;
        }
        return this;
    }

    public PersonInfo addHjL(Long l) {
        this.hjL += l;
        return this;
    }

    public Integer getWhType() {
        return GameDataBase.getWhTypeByWh(wh);
    }

    public PersonInfo setWhType(Integer whType) {
        this.whType = whType;
        return this;
    }

    @Override
    public boolean isVertigo() {
        return BaseInfoTemp.isVertigo(getId().longValue());
    }

    @Override
    public PersonInfo cancelVertigo() {
        BaseInfoTemp.removeVertigo(getId().longValue());
        return this;
    }

    @Override
    public PersonInfo letVertigo(long t) {
        BaseInfoTemp.letVertigo(getId().longValue(), t);
        return this;
    }

    @Override
    public PersonInfo apply() {
        GameDataBase.putPerson(this);
        return this;
    }

    public Integer getNextR3() {
        return nextR3;
    }

    public PersonInfo setNextR3(Integer nextR3) {
        this.nextR3 = nextR3;
        return this;
    }

    @Override
    public String getTips() {
        return null;
    }
}