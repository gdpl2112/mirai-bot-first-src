package io.github.kloping.mirai0.commons;


import Project.commons.gameEntitys.base.BaseInfo;
import Project.commons.gameEntitys.base.BaseInfoTemp;
import Project.services.detailServices.ac.GameJoinDetailService;
import Project.services.detailServices.ac.entity.*;
import Project.services.detailServices.roles.v1.TagManagers;
import com.alibaba.fastjson.annotation.JSONField;
import io.github.kloping.mirai0.commons.game.NormalTagPack;
import io.github.kloping.mirai0.unitls.Tools.Tool;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import static Project.commons.resouce_and_tool.CommonSource.percentTo;
import static Project.commons.resouce_and_tool.CommonSource.toPercent;
import static Project.dataBases.GameDataBase.getNameById;
import static Project.services.detailServices.ac.GameJoinDetailService.getGhostObjFrom;
import static io.github.kloping.mirai0.unitls.Tools.GameTool.*;

/**
 * @author github-kloping
 */
public class GhostObj implements Serializable, BaseInfo {
    @JSONField(serialize = false, deserialize = false)
    public static final int NOT_NEED = 0;
    @JSONField(serialize = false, deserialize = false)
    public static final int NEED_AND_NO = 1;
    @JSONField(serialize = false, deserialize = false)
    public static final int NEED_AND_YES = 2;
    @JSONField(serialize = false, deserialize = false)
    public static final int HELPING = 3;
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
    private Set<Long> withs = new HashSet<>();
    public Boolean canGet = true;
    private long whoMeet = -1;
    private Integer nc = 0;
    private String myTag = "";

    public GhostObj() {
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

    public GhostObj(long hp, long att, long id, long l) {
        this(hp, att, id, l, true);
    }

    public GhostObj(long hp, long att, long id, long l, boolean bal) {
        this.maxHp = this.hp = hp;
        this.att = att;
        this.xp = Tool.INSTANCE.randA((int) (1f * l), (int) (4 * l));
        this.id = id;
        L = l;
        time = System.currentTimeMillis() + 1000 * 60 * 7;
        state = NOT_NEED;
        initHj();
        IDX = getIdx();
        name = getNameById(this.id);
        if (bal) balance1();
    }

    public GhostObj(long hp, long att, long xp, int id, long l, float bl) {
        this(hp, att, xp, id, l, bl, true);
    }

    public GhostObj(long hp, long att, long xp, int id, long l, float bl, boolean balance) {
        this.hp = randFloatByte1(hp);
        this.maxHp = this.hp;
        this.att = randFloatByte1(att);
        this.xp = randFloatByte1(xp);
        this.id = Long.valueOf(id);
        L = summonL(bl);
        time = System.currentTimeMillis() + 1000 * 60 * 7;
        state = NOT_NEED;
        initHj();
        IDX = getIdx();
        name = getNameById(this.id);
        if (balance) balance1();
    }

    private void balance1() {
        if (id <= 520) {
            int i0 = getHhByGh(getL());
            switch (i0) {
                case 201://十
                    this.id = Tool.INSTANCE.randA(501, 503);
                    return;
                case 202://百
                    this.id = Tool.INSTANCE.randA(503, 505);
                    return;
                case 203://千
                    this.id = Tool.INSTANCE.randA(505, 507);
                    return;
                case 204://万
                    this.id = Tool.INSTANCE.randA(507, 510);
                    return;
                case 205://十万
                    this.id = Tool.INSTANCE.randA(510, 514);
                    return;
                case 206:
                    this.id = Tool.INSTANCE.randA(514, 518);
                    return;
                case 207:
                    this.id = Tool.INSTANCE.randA(518, 521);
                    return;
                default:
                    return;
            }
        }
        name = getNameById(this.id);
    }

    public static GhostObj create(long hp, long att, long xp, int idMin, int idMax, long l, float bl, boolean balance) {
        int id = (int) Tool.INSTANCE.randA(idMin, idMax);
        return create(hp, att, xp, id, l, bl, balance);
    }

    public static GhostObj create(long hp, long att, long xp, int id, long l, float bl, boolean balance) {
        if (id > 700) {
            switch (id) {
                case 701:
                    return new Ghost701(hp, att, xp, id, l, bl);
                case 702:
                    return new Ghost702(hp, att, xp, id, l, bl);
                case 703:
                    return new Ghost703(hp, att, xp, id, l, bl);
                case 704:
                    return new Ghost704(hp, att, xp, id, l, bl);
                case 705:
                    return new Ghost705(hp, att, xp, id, l, bl);
                case 710:
                    return new Ghost710(hp, att, xp, id, l, bl);
                case 711:
                    return new Ghost711(hp, att, xp, id, l, bl);
                default:
                    return null;
            }
        } else {
            return new GhostObj(hp, att, xp, id, l, bl, balance);
        }
    }

    public static GhostObj createHelp(String valueOf) {
        return new GhostObj(valueOf);
    }

    public static <T extends GhostObj> T create(int level, int idMin, int idMax) {
        int id = (int) Tool.INSTANCE.randA(idMin, idMax);
        return create(level, id);
    }

    public static <T extends GhostObj> T create(int level, int id) {
        return create(level, id, true);
    }

    public static <T extends GhostObj> T create(int level, int id, boolean bal) {
        if (id > 700) {
            switch (id) {
                case 701:
                    return (T) new Ghost701(Tool.INSTANCE.randA(4 * level, 7 * level), Tool.INSTANCE.randA(2 * level, 8 * level), id, Tool.INSTANCE.randA(level + 1, Lmax(level)));
                case 702:
                    return (T) new Ghost702(Tool.INSTANCE.randA(4 * level, 7 * level), Tool.INSTANCE.randA(2 * level, 8 * level), id, Tool.INSTANCE.randA(level + 1, Lmax(level)));
                case 703:
                    return (T) new Ghost703(Tool.INSTANCE.randA(4 * level, 7 * level), Tool.INSTANCE.randA(2 * level, 8 * level), id, Tool.INSTANCE.randA(level + 1, Lmax(level)));
                case 704:
                    return (T) new Ghost704(Tool.INSTANCE.randA(4 * level, 7 * level), Tool.INSTANCE.randA(2 * level, 8 * level), id, Tool.INSTANCE.randA(level + 1, Lmax(level)));
                case 705:
                    return (T) new Ghost705(Tool.INSTANCE.randA(4 * level, 7 * level), Tool.INSTANCE.randA(2 * level, 8 * level), id, Tool.INSTANCE.randA(level + 1, Lmax(level)));
                case 710:
                    return (T) new Ghost710(Tool.INSTANCE.randA(4 * level, 7 * level), Tool.INSTANCE.randA(2 * level, 8 * level), id, Tool.INSTANCE.randA(level + 1, Lmax(level)));
                case 711:
                    return (T) new Ghost711(Tool.INSTANCE.randA(4 * level, 7 * level), Tool.INSTANCE.randA(2 * level, 8 * level), id, Tool.INSTANCE.randA(level + 1, Lmax(level)));
                default:
                    return null;
            }
        } else {
            GhostObj ghostObj = new GhostObj(Tool.INSTANCE.randA(4 * level, 7 * level), Tool.INSTANCE.randA(2 * level, 8 * level), id, Tool.INSTANCE.randA(level + 1, Lmax(level)), bal);
            return (T) ghostObj;
        }
    }

    public static int getIdx() {
        return idx++;
    }

    public static void setIdx(int idx) {
        GhostObj.idx = idx;
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

    private Long summonL(float bl) {
        long vv = (long) ((hp / bl + att) / 2);
        vv = Tool.INSTANCE.randLong(vv, 0.56f, 0.82f);
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

    public void setHjL(Long hjL) {
        this.hjL = hjL;
    }

    public int getIDX() {
        return IDX;
    }

    public void setIDX(int IDX) {
        this.IDX = IDX;
    }

    public long getIDxL() {
        return (long) IDX;
    }

    public Long getMaxHp() {
        return maxHp;
    }

    public void setMaxHp(Long maxHp) {
        this.maxHp = maxHp;
    }

    @Override
    public Long getHj() {
        return hj;
    }

    public void setHj(Long hj) {
        this.hj = hj;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        this.hp = hp <= 0 ? 0 : hp;
        return hp;
    }

    public void setHp(long hp) {
        this.hp = hp <= 0 ? 0 : hp;
        this.hp = hp >= maxHp ? maxHp : hp;
    }

    public void setHp(Long hp) {
        this.hp = hp;
    }

    @Override
    public Long getHpL() {
        return maxHp.longValue();
    }

    @Override
    public Integer getLevel() {
        return getL();
    }

    @Override
    public Long getAtt() {
        return att;
    }

    @Override
    public GhostObj setAtt(Long att) {
        this.att = att;
        return this;
    }

    public long getXp() {
        return xp;
    }

    public void setXp(Long xp) {
        this.xp = xp;
    }

    @Override
    public Integer getId() {
        return id.intValue();
    }

    public void setId(Integer id) {
        this.id = Long.valueOf(id);
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getL() {
        return L.intValue();
    }

    public void setL(Long l) {
        L = l;
    }

    public Long getTime() {
        if (state == HELPING) {
            GhostObj ghostObj = getGhostObjFrom(Long.parseLong(forWhoStr));
            return ghostObj == null ? 0 : ghostObj.getTime();
        }
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public synchronized long updateHp(long l, BaseInfo who) {
        int bv = toPercent(-l, maxHp);
        bv = bv > 100 ? 100 : bv < 1 ? 1 : bv;
        long v = percentTo(bv, hj);
        hj -= v;
        setHp(getHp() + l);
        return getHp();
    }

    public Set<Long> getWiths() {
        return withs;
    }

    public void setWiths(Set<Long> withs) {
        this.withs = withs;
    }

    @Override
    public GhostObj apply() {
        return GameJoinDetailService.saveGhostObjIn(whoMeet, this);
    }

    @Override
    public boolean isVertigo() {
        return BaseInfoTemp.isVertigo(-getWhoMeet());
    }

    @Override
    public GhostObj cancelVertigo() {
        BaseInfoTemp.removeVertigo(-getWhoMeet());
        return this;
    }

    @Override
    public GhostObj letVertigo(long t) {
        BaseInfoTemp.letVertigo(-getWhoMeet(), t);
        return this;
    }

    @Override
    public Integer getTips() {
        return nc;
    }

    public void setNc(Integer nc) {
        this.nc = nc;
    }

    public void dispose() {
    }

    @Override
    public GhostObj addTag(String myTag, Number percent, long t) {
        NormalTagPack pack = new NormalTagPack(myTag, t);
        pack.setValue(percent.longValue());
        pack.setQ(-getWhoMeet());
        TagManagers.getTagManager(-getWhoMeet()).addTag(pack);
        return this;
    }

    @Override
    public GhostObj addTag(String myTag, Number percent, Number max, long t) {
        long v0 = getTagValue(myTag).longValue();
        if (v0 >= max.longValue()) {
            return this;
        } else if (v0 + percent.longValue() > max.longValue()) {
            NormalTagPack pack = new NormalTagPack(myTag, t);
            pack.setValue(max.longValue() - v0);
            pack.setQ(-getWhoMeet());
            TagManagers.getTagManager(-getWhoMeet()).addTag(pack);
            return this;
        } else {
            NormalTagPack pack = new NormalTagPack(myTag, t);
            pack.setValue(percent.longValue());
            pack.setQ(-getWhoMeet());
            TagManagers.getTagManager(-getWhoMeet()).addTag(pack);
            return this;
        }
    }

    @Override
    public GhostObj eddTag(String myTag, Number percent) {
        TagManagers.getTagManager(-getWhoMeet()).eddValue(myTag, percent.longValue());
        return this;
    }

    @Override
    public GhostObj eddTag(String myTag) {
        TagManagers.getTagManager(-getWhoMeet()).eddValue(myTag);
        return this;
    }

    @Override
    public boolean containsTag(String tag) {
        return TagManagers.getTagManager(-getWhoMeet()).contains(tag);
    }

    @Override
    public Number getTagValue(String tag) {
        return TagManagers.getTagManager(-getWhoMeet()).getValue(tag);
    }

    public Number getTagValueOrDefault(String tag, Number d) {
        Number v = TagManagers.getTagManager(-getWhoMeet()).getValue(tag);
        return v == null || v.longValue() <= 0 ? d : v;
    }
}
