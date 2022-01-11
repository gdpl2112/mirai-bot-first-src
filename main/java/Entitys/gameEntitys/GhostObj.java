package Entitys.gameEntitys;


import Entitys.gameEntitys.base.BaseInfo;
import Project.Services.DetailServices.GameJoinDetailService;
import Project.Tools.JSONUtils;

import java.io.Serializable;

import static Project.DataBases.GameDataBase.getNameById;
import static Project.DataBases.skill.SkillDataBase.percentTo;
import static Project.DataBases.skill.SkillDataBase.toPercent;
import static Project.Services.DetailServices.GameJoinDetailService.getGhostObjFrom;
import static Project.Tools.GameTool.*;
import static Project.Tools.Tool.randA;

/**
 * @author github-kloping
 */
public class GhostObj implements Serializable, BaseInfo {
    public static int idx = 100;
    private Long hp, att, xp, id, L, time;
    private int IDX;
    private int state = -99;
    private String forWhoStr = "";
    private Long maxHp = 0L;
    private Long hj = 0L;
    private Long hjL = 0L;
    @JSONUtils.Transient
    public static final int NotNeed = 0, NeedAndNo = 1, NeedAndY = 2, HELPING = 3;
    private String name;
    private Long with = -1L;
    private long whoMeet;

    public GhostObj() {
    }

    @Override
    public BaseInfo addHj(Long l) {
        if (l != null) {
            hj += l;
        }
        return this;
    }

    @Override
    public BaseInfo addHp(Long v) {
        if (v != null) {
            updateHp(v.longValue());
        }
        return this;
    }

    public GhostObj(String forWhoStr) {
        this.forWhoStr = forWhoStr;
        state = HELPING;
        time = System.currentTimeMillis() + 1000 * 60 * 7;
        IDX = getGhostObjFrom(Long.parseLong(forWhoStr.trim())).IDX;
    }

    public GhostObj(long hp, long att, long xp, long id, long l) {
        this.maxHp = this.hp = hp;
        this.att = att;
        this.xp = xp;
        this.id = id;
        L = l;
        time = System.currentTimeMillis() + 1000 * 60 * 7;
        state = NotNeed;
        name = getNameById(this.id);
        initHj();
        IDX = ++idx;
    }


    public GhostObj(long hp, long att, long xp, int idMin, int idMax, long l, boolean rand) {
        this.hp = randFloatByte1(hp);
        this.att = randFloatByte1(att);
        this.xp = randFloatByte1(xp);
        this.id = randA(idMin, idMax);
        L = getLtoGhsL(l);
        time = System.currentTimeMillis() + 1000 * 60 * 7;
        state = NotNeed;
        name = getNameById(this.id);
        initHj();
        IDX = ++idx;
    }

    public long getWhoMeet() {
        return whoMeet;
    }

    public void setWhoMeet(long whoMeet) {
        this.whoMeet = whoMeet;
    }

    private void initHj() {
        long v = att + maxHp;
        v = v / 10;
        v = v < 100 ? 100 : v;
        hj = v;
        hjL = v;
    }

    @Override
    public Long getHjL() {
        return hjL;
    }

    public int getIDX() {
        return IDX;
    }

    public Long getMaxHp() {
        return maxHp;
    }

    @Override
    public Long getHj() {
        return hj;
    }

    public String getName() {
        return name;
    }

    public void setId(Integer id) {
        this.id = Long.valueOf(id);
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getForWhoStr() {
        return forWhoStr;
    }

    public void setForWhoStr(String forWhoStr) {
        this.forWhoStr = forWhoStr;
    }

    @Override
    public Long getHp() {
        return hp;
    }

    @Override
    public Long getHpL() {
        return maxHp.longValue();
    }

    @Override
    public Integer getLevel() {
        return getL();
    }

    public void setHp(long hp) {
        this.hp = hp;
    }

    @Override
    public Long getAtt() {
        return att;
    }

    public long getXp() {
        return xp;
    }

    @Override
    public Integer getId() {
        return Integer.valueOf(id + "");
    }

    public Integer getL() {
        return Integer.valueOf(L + "");
    }

    public GhostObj setAtt(Long att) {
        this.att = att;
        return this;
    }

    public Long getTime() {
        if (state == HELPING) {
            GhostObj ghostObj = GameJoinDetailService.getGhostObjFrom(Long.parseLong(forWhoStr));
            return ghostObj == null ? 0 : ghostObj.getTime();
        }
        return time;
    }

    public long updateHp(long l) {
        setHp(getHp() + l);
        int bv = toPercent(l, maxHp);
        bv = bv > 100 ? 100 : bv < 1 ? 1 : bv;
        long v = percentTo(bv, hj);
        hj -= v;
        return getHp();
    }

    public static GhostObj create(int level, int idMin, int idMax) {
        switch (level) {
            case 10:
            case 100:
            case 1000:
            case 10000:
            case 100000:
                GhostObj ghostObj = new GhostObj(randA(4 * level, 9 * level), randA(2 * level, 8 * level), randA(10 * level, 35 * level)
                        , randA(idMin, idMax), randA(level + 1, Lmax(level)));
                return ghostObj;
            default:
                return null;
        }
    }

    public Long getWith() {
        return with;
    }

    public GhostObj setWith(Long with) {
        this.with = with;
        return this;
    }

    public void setHj(Long hj) {
        this.hj = hj;
    }

    @Override
    public GhostObj apply() {
        return GameJoinDetailService.saveGhostObjIn(whoMeet, this);
    }
}
