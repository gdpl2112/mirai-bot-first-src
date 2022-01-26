package Entitys.gameEntitys;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import static Project.ASpring.SpringBootResource.gInfoMapper;

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

    public GInfo addQid() {
        this.qid++;
        return this;
    }

    public GInfo addQid(int i) {
        this.qid += i;
        return this;
    }

    public GInfo addMasterPoint() {
        this.masterPoint++;
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

    public void apply() {
        UpdateWrapper<GInfo> wrapper = new UpdateWrapper<>();
        wrapper.eq("qid", this.getQid());
        if (gInfoMapper.update(this, wrapper) == 0) {
            gInfoMapper.insert(this);
        }
    }

    public static GInfo getInstance(long qid) {
        GInfo gInfo = gInfoMapper.selectOne(new QueryWrapper<GInfo>().eq("qid", qid));
        if (gInfo != null) {
            return gInfo;
        }
        return new GInfo().setQid(qid);
    }
}
