package io.github.kloping.gb.services;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import io.github.kloping.MySpringTool.annotations.AutoStand;
import io.github.kloping.MySpringTool.annotations.Entity;
import io.github.kloping.gb.ImageManager;
import io.github.kloping.gb.Utils;
import io.github.kloping.gb.alone.Operate;
import io.github.kloping.gb.drawers.Drawer;
import io.github.kloping.gb.finals.FinalFormat;
import io.github.kloping.gb.game.GameConfig;
import io.github.kloping.gb.game.GameRules;
import io.github.kloping.gb.spring.dao.PersonInfo;
import io.github.kloping.gb.spring.dao.WhInfo;
import io.github.kloping.gb.spring.mapper.PersonInfoMapper;
import io.github.kloping.gb.spring.mapper.WhInfoMapper;

/**
 * @author github.kloping
 */
@Entity
public class GameService {

    @AutoStand
    PersonInfoMapper personInfoMapper;

    @AutoStand
    WhInfoMapper whInfoMapper;

    public WhInfo getWhInfo(String sid, Integer p) {
        QueryWrapper<WhInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("qid", sid);
        queryWrapper.eq("p", p);
        WhInfo info = whInfoMapper.selectOne(queryWrapper);
        if (info == null) {
            info = new WhInfo();
            info.setQid(sid);
            info.setP(p);
            whInfoMapper.insert(info);
        }
        return info;
    }

    public PersonInfo getPersonInfo(String sid) {
        PersonInfo info = personInfoMapper.selectById(sid);
        if (info == null) {
            info = new PersonInfo();
            info.setName(sid);
            info.setP(1);
            personInfoMapper.insert(info);
        }
        return info;
    }

    public void apply(WhInfo wi) {
        UpdateWrapper<WhInfo> uq = new UpdateWrapper<>();
        uq.eq("qid", wi.getQid());
        uq.eq("p", wi.getP());
        whInfoMapper.update(wi, uq);
    }

    public void apply(PersonInfo pi) {
        personInfoMapper.updateById(pi);
    }

    public void operateW(String sid, Operate<WhInfo> op) {
        PersonInfo p = getPersonInfo(sid);
        WhInfo w = getWhInfo(sid, p.getP());
        op.operate(w);
        apply(w);
    }

    public void operateP(String sid, Operate<PersonInfo> op) {
        PersonInfo p = getPersonInfo(sid);
        op.operate(p);
        apply(p);
    }

    //==================================================

    @AutoStand
    GameConfig config;

    public String info(String sid) {
        PersonInfo is = getPersonInfo(sid);
        WhInfo wi = getWhInfo(sid, is.getP());
        StringBuilder sb = new StringBuilder();
        if (!is.getSname().isEmpty() && wi.getLevel() >= 90) {
            sb.append(String.format(FinalFormat.FORMAT_IMAGE,
                    Drawer.createFont(is.getSname() + GameConfig.getFH(wi.getLevel()))));
        }
        long n = wi.getWh();
        if (n <= 0) {
            sb.append("你的武魂:暂未获得").append("\r\n");
        } else {
            sb.append("你的武魂:" + config.ID_2_NAME_MAPS.get(n)).append("\r\n");
            sb.append(ImageManager.getImgPathById((int) n)).append("\r\n");
        }
        return sb + String.format(FinalFormat.FORMAT_IMAGE, Drawer.drawInfo(wi, is));
    }

    public String xl(String sid) {
        PersonInfo pi = getPersonInfo(sid);
        WhInfo wi = getWhInfo(sid, pi.getP());
        if (wi.getWh() == 0 && wi.getLevel() >= 2) return "请先觉醒武魂";
        long l = pi.getK1();
        long now = System.currentTimeMillis();
        if (now >= l) {
            int tr = Utils.RANDOM.nextInt(6) + 9;
            int c = (GameConfig.getRandXl(wi.getLevel()));
            long mx = wi.getXpL();
            long xr = mx / c;
            wi.setXp(xr);
            pi.setK1(now + (tr * 1000 * 60));

            long ll1 = hfHp(wi, 1.0f);
            long ll2 = hfHl(wi, 1.0f);
            long ll3 = hfHj(wi, 1.0f);
            StringBuilder sb = new StringBuilder();
            if (wi.getWh() > 0) {
                sb.append(config.ID_2_NAME_MAPS.get(wi.getWh()));
            }
            apply(wi);
            apply(pi);

//            GInfo.getInstance(who).addXlc().apply();
//            zongMenService.addActivePoint(who, 1);

            sb.append(String.format("你花费了%s分钟修炼", tr)).append(",");
            sb.append(String.format("获得了%s点经验", xr)).append(",");
            sb.append(String.format("恢复了%s点血量", ll1)).append(",");
            sb.append(String.format("恢复了%s点魂力", ll2)).append(",");
            sb.append(String.format("恢复了%s点精神力", ll3)).append(",");

            String out = null;
            if (wi.getWh() <= 0) {
                out = String.format(FinalFormat.FORMAT_IMAGE, Drawer.drawLines(sb.toString().split(",")));
            } else {
                out = String.format(FinalFormat.FORMAT_IMAGE, ImageManager.getImgPathById(wi.getWh())) +
                        String.format(FinalFormat.FORMAT_IMAGE, Drawer.drawLines(sb.toString().split(",")));
            }
            return out;
        } else {
            return String.format(FinalFormat.XL_WAIT_TIPS, Utils.getTimeTips(l));
        }
    }

    @AutoStand
    GameRules rules;

    private long hfHl(WhInfo wi, float v) {
        long hll = wi.getHll();
        long hl = wi.getHl();
        if (hl >= hll) return 0;
        if (hl > hll) {
            wi.setHl(wi.getHll());
            return hll - hl;
        }
        int c1 = GameConfig.getRandXl(wi.getLevel());
        if (c1 > 24) c1 = 24;
        if (c1 < 4) c1 = 4;
        long l5 = wi.getHll() / c1;
        l5 += Utils.randLong(l5, 0.5f, 0.7f);
        l5 *= v;
        if ((hll - hl) < l5) {
            l5 = (hll - hl);
        }
        l5 = rules.getDeserveHl(wi, l5);
        wi.addHl(l5);
        return l5;
    }

    private long hfHj(WhInfo wi, float v) {
        long hjl = wi.getHjL();
        long hj = wi.getHj();
        if (hj >= hjl) return 0;
        if (hj > hjl) {
            wi.setHj(wi.getHjL());
            return hjl - hj;
        }
        int c1 = GameConfig.getRandXl(wi.getLevel());
        if (c1 > 12) c1 = 24;
        if (c1 < 3) c1 = 4;
        long l5 = wi.getHll() / c1;
        l5 += Utils.randLong(l5, 0.6f, 0.7f);
        l5 *= v;
        if ((hjl - hj) < l5) {
            l5 = (hjl - hj);
        }
        l5 = rules.getDeserveHj(wi, l5);
        wi.addHj(l5);
        return l5;
    }

    private long hfHp(WhInfo wi, float v) {
        long hpl = wi.getHpl();
        long hp = wi.getHp();
        if (hp >= hpl) return 0;
        if (hp > hpl) {
            wi.setHp(wi.getHpl());
            return hpl - hp;
        }
        int c1 = GameConfig.getRandXl(wi.getLevel());
        if (c1 > 30) c1 = 30;
        if (c1 < 4) c1 = 4;
        long l5 = wi.getHpl() / c1;
        l5 += Utils.randLong(l5, 0.5f, 0.6f);
        l5 *= v;
        if ((hpl - hp) < l5) {
            l5 = (hpl - hp);
        }
        l5 = rules.getDeserveHp(wi, l5);
        wi.addHp(l5);
        return l5;
    }
}
