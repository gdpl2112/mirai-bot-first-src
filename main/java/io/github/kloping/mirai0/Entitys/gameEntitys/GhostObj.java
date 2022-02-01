package io.github.kloping.mirai0.Entitys.gameEntitys;


import Project.services.DetailServices.GameJoinDetailService;
import Project.services.DetailServices.ac.entity.*;
import com.alibaba.fastjson.annotation.JSONField;
import io.github.kloping.mirai0.Entitys.gameEntitys.base.BaseInfo;
import io.github.kloping.mirai0.Entitys.gameEntitys.base.BaseInfoTemp;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;

import static Project.DataBases.GameDataBase.getNameById;
import static Project.DataBases.skill.SkillDataBase.percentTo;
import static Project.DataBases.skill.SkillDataBase.toPercent;
import static Project.services.DetailServices.GameJoinDetailService.getGhostObjFrom;
import static io.github.kloping.mirai0.unitls.Tools.GameTool.Lmax;
import static io.github.kloping.mirai0.unitls.Tools.GameTool.randFloatByte1;
import static io.github.kloping.mirai0.unitls.Tools.Tool.randA;
import static io.github.kloping.mirai0.unitls.Tools.Tool.randLong;

/**
 * @author github-kloping
 */
public class GhostObj implements Serializable, BaseInfo {
    public static int idx = 100;
    @Nullable
    private Long hp = -1L;
    @Nullable
    private Long att = -1L;
    @Nullable
    private Long xp = -1L;
    @Nullable
    private Long id = -1L;
    @Nullable
    private Long L = -1L;
    @Nullable
    private Long time = -1L;
    @Nullable
    private int IDX = -1;
    @Nullable
    private int state = -99;
    @Nullable
    private String forWhoStr = "";
    @Nullable
    private Long maxHp = 0L;
    @Nullable
    private Long hj = 0L;
    @Nullable
    private Long hjL = 0L;
    @Nullable
    private String name;
    @Nullable
    private Long with = -1L;
    private long whoMeet = -1;

    @JSONField(serialize = false, deserialize = false)
    public static final int NotNeed = 0;
    @JSONField(serialize = false, deserialize = false)
    public static final int NeedAndNo = 1;
    @JSONField(serialize = false, deserialize = false)
    public static final int NeedAndY = 2;
    @JSONField(serialize = false, deserialize = false)
    public static final int HELPING = 3;

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
            updateHp(v.longValue(), null);
        }
        return this;
    }

    public GhostObj(String forWhoStr) {
        this.forWhoStr = forWhoStr;
        state = HELPING;
        time = System.currentTimeMillis() + 1000 * 60 * 7;
        GhostObj ghostObj = getGhostObjFrom(Long.parseLong(forWhoStr.trim()));
        IDX = ghostObj.IDX;
        id = ghostObj.id;
        name = ghostObj.name;
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
        IDX = getIdx();
    }

    public GhostObj(long hp, long att, long xp, int idMin, int idMax, long l, boolean rand, float bl) {
        this.hp = randFloatByte1(hp);
        this.att = randFloatByte1(att);
        this.xp = randFloatByte1(xp);
        this.id = randA(idMin, idMax);
        L = summonL(bl);
        time = System.currentTimeMillis() + 1000 * 60 * 7;
        state = NotNeed;
        name = getNameById(this.id);
        initHj();
        IDX = getIdx();
    }

    private Long summonL(float bl) {
        long vv = (long) ((hp / bl + att) / 2);
        vv = randLong(vv, 0.56f, 0.82f);
        vv = vv <= 0 ? 1 : vv;
        return vv;
    }

    public long getWhoMeet() {
        return whoMeet;
    }

    public void setWhoMeet(long whoMeet) {
        this.whoMeet = whoMeet;
    }

    private void initHj() {
        long v = att / 7 + maxHp / 6;
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

    public long getIDxL() {
        return (long) IDX;
    }

    public Long getMaxHp() {
        return maxHp;
    }

    @Override
    public Long getHj() {
        return hj;
    }

    @Override
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
        return id.intValue();
    }

    public Integer getL() {
        return L.intValue();
    }

    public static GhostObj create(long hp, long att, long xp, int idMin, int idMax, long l, boolean rand, float bl) {
        if (idMax > 700) {
            int id = (int) randA(idMin, idMax);
            switch (id) {
                case 701:
                    return new Ghost701(hp, att, xp, idMin, idMax, l, rand, bl);
                case 702:
                    return new Ghost702(hp, att, xp, idMin, idMax, l, rand, bl);
                case 703:
                    return new Ghost703(hp, att, xp, idMin, idMax, l, rand, bl);
                case 704:
                    return new Ghost704(hp, att, xp, idMin, idMax, l, rand, bl);
                case 705:
                    return new Ghost705(hp, att, xp, idMin, idMax, l, rand, bl);
                default:
                    return null;
            }
        } else {
            return new GhostObj(hp, att, xp, idMin, idMax, l, rand, bl);
        }
    }

    public static GhostObj createHelp(String valueOf) {
        return new GhostObj(valueOf);
    }

    public static <T extends GhostObj> T create(int level, int idMin, int idMax) {
        if (idMax > 700) {
            int id = (int) randA(idMin, idMax);
            switch (id) {
                case 701:
                    return (T) new Ghost701(randA(4 * level, 7 * level), randA(2 * level, 8 * level)
                            , randA(10 * level, 35 * level)
                            , id, randA(level + 1, Lmax(level)));
                case 702:
                    return (T) new Ghost702(randA(4 * level, 7 * level), randA(2 * level, 8 * level)
                            , randA(10 * level, 35 * level)
                            , id, randA(level + 1, Lmax(level)));
                case 703:
                    return (T) new Ghost703(randA(4 * level, 7 * level), randA(2 * level, 8 * level)
                            , randA(10 * level, 35 * level)
                            , id, randA(level + 1, Lmax(level)));
                case 704:
                    return (T) new Ghost704(randA(4 * level, 7 * level), randA(2 * level, 8 * level)
                            , randA(10 * level, 35 * level)
                            , id, randA(level + 1, Lmax(level)));
                case 705:
                    return (T) new Ghost705(randA(4 * level, 7 * level), randA(2 * level, 8 * level)
                            , randA(10 * level, 35 * level)
                            , id, randA(level + 1, Lmax(level)));
                default:
                    return null;
            }
        } else {
            GhostObj ghostObj = new GhostObj(randA(4 * level, 7 * level), randA(2 * level, 8 * level)
                    , randA(10 * level, 35 * level)
                    , randA(idMin, idMax), randA(level + 1, Lmax(level)));
            return (T) ghostObj;
        }
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

    public long updateHp(long l, BaseInfo who) {
        setHp(getHp() + l);
        int bv = toPercent(-l, maxHp);
        bv = bv > 100 ? 100 : bv < 1 ? 1 : bv;
        long v = percentTo(bv, hj);
        hj -= v;
        return getHp();
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

    @Override
    public boolean isVertigo() {
        return BaseInfoTemp.isVertigo(getId());
    }

    @Override
    public GhostObj cancelVertigo() {
        BaseInfoTemp.removeVertigo(getId().longValue());
        return this;
    }

    @Override
    public GhostObj letVertigo(long t) {
        BaseInfoTemp.letVertigo(getId(), t);
        return this;
    }

    @Override
    public String getTips() {
        return null;
    }

    public static int getIdx() {
        return idx++;
    }

    public static void setIdx(int idx) {
        GhostObj.idx = idx;
    }

    public void setHp(Long hp) {
        this.hp = hp;
    }

    public void setXp(Long xp) {
        this.xp = xp;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setL(Long l) {
        L = l;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public void setIDX(int IDX) {
        this.IDX = IDX;
    }

    public void setMaxHp(Long maxHp) {
        this.maxHp = maxHp;
    }

    public void setHjL(Long hjL) {
        this.hjL = hjL;
    }

    public void setName(String name) {
        this.name = name;
    }
}
