package Entitys.gameEntitys;


import Project.DataBases.GameDataBase;
import Project.DataBases.SkillDataBase;

import java.lang.reflect.Field;

import static Project.DataBases.GameDataBase.getInfo;

public class PersonInfo {
    /**
     * 攻击
     */
    public Long att = 10l;
    /**
     * 金魂币
     */
    public Long gold = 200l;
    /**
     * 魂力
     */
    public Long hl = 100l;
    /**
     * 魂力最大值
     */
    public Long hll = 100l;
    /**
     * 血量
     */
    public Long hp = 100l;
    /**
     * 最大血量
     */
    public Long hpl = 100l;
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
    public Integer level = 1;
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
    public Long xp = 0l;
    /**
     * 修炼冷却
     */
    public Long k1 = 1l;
    /**
     * 进入冷却
     */
    public Long k2 = 1l;
    /**
     * 购买 冷却
     */
    public Long gk1 = 1l;
    /**
     * 使用冷却
     */
    public Long uk1 = 1l;
    /**
     * 加入宗门冷却
     */
    public Long jk1 = 1l;
    /**
     * 宗门贡献冷却
     */
    public Long Cbk1 = 1l;
    /**
     * 封号修改冷却
     */
    public Long mk1 = 1l;
    /**
     * 攻击冷却
     */
    public Long ak1 = 1l;
    /**
     * 选择攻击冷却
     */
    public Long jak1 = 1l;
    /**
     * 经验最大值 冷却
     */
    public Long xpL = 100l;
    /**
     * 使用 物品 标志
     */
    public String using = null;
    /**
     * 名字QQ
     */
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
     * 封号名字
     */
    public String Sname = "";
    /**
     * 标题
     */
    public String myTag = "";
    /**
     * 死过
     */
    private boolean died = false;
    /**
     * 降级
     */
    private boolean downed = false;

    /**
     * 下次血量为空生效时
     */
    private Long dt1 = 0L;

    /**
     * 斗魂中...
     */
    private Boolean temp = false;

    /**
     * 勋章数
     */
    private Integer winC = 0;

    public Boolean getTemp() {
        return temp;
    }

    public Integer getWinC() {
        return winC;
    }

    public PersonInfo addWinc(Integer num) {
        winC++;
        return this;
    }

    public void setTemp(Boolean temp) {
        this.temp = temp;
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

    public Long getAtt() {
        long at1 = att;
        if (SkillDataBase.hasAdder.containsKey(Long.valueOf(name))) {
            SkillDataBase.HasTimeAdder adder = SkillDataBase.hasAdder.get(Long.valueOf(name));
            if (adder.test())
                at1 += adder.getValue().longValue();
        }
        return at1;
    }

    public PersonInfo setAtt(Long att) {
        this.att = att;
        return this;

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

    public Long getHp() {
        return hp;
    }

    public PersonInfo setHp(Long hp) {
        this.hp = hp;
        return this;
    }

    public Long getHpl() {
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

    public Integer getLevel() {
        return level;
    }

    public PersonInfo setLevel(Integer level) {
        this.level = level;
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
        if (level >= 150) this.xp = 0L;
        if (xp > xpL * 1.5) this.xp = (long) (xpL * 1.5);
        return this;
    }

    public Long getXpL() {
        return xpL;
    }

    public PersonInfo setXpL(Long xpL) {
        this.xpL = xpL;
        return this;
    }

    public String getUsing() {
        return using;
    }

    public PersonInfo setUsing(String using) {
        this.using = using;
        return this;
    }

    public PersonInfo addAtt(Long o) {
        att += o;
        return this;
    }

    public PersonInfo addGold(Long o) {
        gold += o;
        return this;
    }

    public PersonInfo addHl(Long o) {
        hl += o;
        if (hl > hll) hl = hll;
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

    public PersonInfo addHp(Long o) {
        hp += o;
        if (hp > hpl) hp = hpl;
        if (hp <= 0) hp = 0L;
        return this;
    }

    public void test() {
        if (hp <= 0 && dt1 <= System.currentTimeMillis()) {
            if (died) {
                if (level % 10 == 0 || downed) {
                    xp = 0L;
                } else {
                    level--;
                    downed = true;
                }
            } else {
                xp = 0L;
                died = true;
            }
            dt1 = System.currentTimeMillis() + 1000 * 60 * 5;
        }
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
        level += o;
        return this;
    }

    public PersonInfo addXp(Long o) {
        xp += o;
        if (level >= 150) this.xp = 0L;
        if (xp > xpL * 1.5) this.xp = (long) (xpL * 1.5);
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

    public PersonInfo setSname(String sname) {
        Sname = sname;
        return this;
    }

    public String getSname() {
        return Sname;
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

    public PersonInfo setTag(String myTag) {
        this.myTag = myTag;
        return this;
    }

    public PersonInfo addTag(String myTag, Number percent) {
        if (this.myTag.contains(myTag)) return this;
        this.myTag += myTag + percent + ",";
        return this;
    }

    public PersonInfo eddTag(String myTag, Number percent) {
        if (this.myTag.contains(myTag + percent + ","))
            this.myTag = this.myTag.replaceAll(myTag + percent + ",", "");
        return this;
    }

    public PersonInfo eddTag(String myTag) {
        if (this.myTag.contains(myTag)) {
            String t1 = this.myTag;
            int i1 = t1.indexOf(myTag);
            int i2 = t1.substring(i1).indexOf(",");
            t1 = t1.substring(i1, i2 + i1 + 1);
            this.myTag = this.myTag.replaceAll(t1, "");
        }
        return this;
    }

    public boolean containsTag(String tag) {
        return myTag.contains(tag);
    }

    public String getTag(String tag) {
        if (myTag.contains(tag)) {
            int start = myTag.indexOf(tag);
            int end = myTag.indexOf(",", start);
            String s1 = myTag.substring(start, end);
            return s1;
        } else return "0";
    }

    public Number getTagValue(String tag) {
        PersonInfo info = getInfo(this.name);
        String sb = info.getMyTag();
        int i = sb.indexOf(tag);
        if (i < 0) return -1;
        sb = sb.substring(i);
        int i2 = sb.indexOf(",");
        String vs = sb.substring(1, i2);
        return Long.valueOf(vs);
    }

    public Integer getHelpToc() {
        return helpToc;
    }

    public PersonInfo setHelpToc(Integer helpToc) {
        if (helpToc > this.helpToc) return this;
        this.helpToc = helpToc;
        return this;
    }

    public Integer getHelpC() {
        return helpC;
    }

    public PersonInfo setHelpC(Integer helpC) {
        if (helpC > this.helpC) return this;
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

    public Long getHj() {
        return hj;
    }

    public PersonInfo setHj(Long hj) {
        this.hj = hj;
        return this;
    }

    public Long getHjL() {
        return hjL;
    }

    public PersonInfo setHjL(Long hjL) {
        this.hjL = hjL;
        return this;
    }

    public PersonInfo addHj(Long l) {
        this.hj += l;
        if (hj < 0) hj = 0L;
        if (hj > hjL) hj = hjL;
        return this;
    }

    public PersonInfo addHjL(Long l) {
        this.hjL += l;
        return this;
    }

    public Integer getWhType() {
        if (whType == -1)
            whType = GameDataBase.wh2Type.get(wh);
        return whType;
    }

    public PersonInfo setWhType(Integer whType) {
        this.whType = whType;
        return this;
    }
}