package io.github.kloping.mirai0.commons;

import Project.dataBases.GameDataBase;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import io.github.kloping.MySpringTool.StarterApplication;
import io.github.kloping.file.FileUtils;
import io.github.kloping.serialize.HMLObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.File;

import static Project.aSpring.SpringBootResource.getgInfoMapper;

/**
 * @author github-kloping
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class GInfo {
    private long qid = -1;
    /**
     * 名师点
     */
    private int masterPoint = 0;
    /**
     * 修炼次数
     */
    private int xlc = 0;
    /**
     * 进入次数
     */
    private int joinc = 0;
    /**
     * 获得物品次数
     */
    private int gotc = 0;
    /**
     * 失去物品次数
     */
    private int lostc = 0;
    /**
     * 累计死亡次数
     */
    private int diedc = 0;
    /**
     * 魂技使用次数
     */
    private int useskillc = 0;
    /**
     * 星级
     */
    private int star = 0;
    /**
     * 请求支援次数
     */
    private int reqc = 0;
    /**
     * 支援次数
     */
    private int helpc = 0;
    /**
     * 购买次数
     */
    private int buyc = 0;
    /**
     * 出售次数
     */
    private int salec = 0;
    /**
     * 完成任务次数
     */
    private int ftc = 0;

    public static GInfo getInstance(long qid) {
        if (getgInfoMapper() != null) {
            GInfo gInfo = getgInfoMapper().selectOne(new QueryWrapper<GInfo>().eq("qid", qid));
            if (gInfo != null) {
                return gInfo;
            }
            return new GInfo().setQid(qid);
        }
        return null;
    }

    public GInfo addReqc() {
        reqc++;
        return this;
    }

    public GInfo addHelpc() {
        helpc++;
        return this;
    }

    public GInfo addBuyc() {
        buyc++;
        return this;
    }

    public GInfo addSalec() {
        salec++;
        return this;
    }

    public GInfo addFtc() {
        ftc++;
        return this;
    }

    public GInfo addQid() {
        this.qid++;
        return this;
    }

    public GInfo addQid(int i) {
        this.qid += i;
        return this;
    }

    public GInfo addMasterPoint() {
        ++this.masterPoint;
        return this;
    }

    public GInfo addMasterPoint(int i) {
        this.masterPoint += i;
        return this;
    }

    public GInfo addXlc() {
        this.xlc++;
        return this;
    }

    public GInfo addXlc(int i) {
        this.xlc += i;
        return this;
    }

    public GInfo addJoinc() {
        this.joinc++;
        return this;
    }

    public GInfo addJoinc(int i) {
        this.joinc += i;
        return this;
    }

    public GInfo addGotc() {
        this.gotc++;
        return this;
    }

    public GInfo addGotc(int i) {
        this.gotc += i;
        return this;
    }

    public GInfo addLostc() {
        this.lostc++;
        return this;
    }

    public GInfo addLostc(int i) {
        this.lostc += i;
        return this;
    }

    public GInfo addDiedc() {
        this.diedc++;
        return this;
    }

    public GInfo addDiedc(int i) {
        this.diedc += i;
        return this;
    }

    public GInfo addUseskillc() {
        this.useskillc++;
        return this;
    }

    public GInfo addUseskillc(int i) {
        this.useskillc += i;
        return this;
    }

    public GInfo addStar() {
        this.star++;
        return this;
    }

    public GInfo addStar(int i) {
        this.star += i;
        return this;
    }

    public GInfo apply() {
        if (getgInfoMapper() == null) {
            return this;
        } else {
            UpdateWrapper<GInfo> wrapper = new UpdateWrapper<>();
            wrapper.eq("qid", this.getQid());
            if (getgInfoMapper().update(this, wrapper) == 0) {
                getgInfoMapper().insert(this);
            }
            StarterApplication.logger.waring(String.format("update for %s ", this.toString()));
            return this;
        }
    }
}
