package io.github.kloping.gb.services;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import io.github.kloping.MySpringTool.annotations.AutoStand;
import io.github.kloping.MySpringTool.annotations.Entity;
import io.github.kloping.gb.ImageManager;
import io.github.kloping.gb.Utils;
import io.github.kloping.gb.alone.Operate;
import io.github.kloping.gb.drawers.Drawer;
import io.github.kloping.gb.finals.FinalConfig;
import io.github.kloping.gb.finals.FinalFormat;
import io.github.kloping.gb.game.GameConfig;
import io.github.kloping.gb.game.GameRules;
import io.github.kloping.gb.game.business.BusinessHandler;
import io.github.kloping.gb.game.business.CoolDownHandler;
import io.github.kloping.gb.game.e0.GameDataContext;
import io.github.kloping.gb.spring.dao.PersonInfo;
import io.github.kloping.gb.spring.dao.WhInfo;
import io.github.kloping.gb.spring.mapper.PersonInfoMapper;
import io.github.kloping.gb.spring.mapper.UpupMapper;
import io.github.kloping.gb.spring.mapper.WhInfoMapper;

/**
 * @author github.kloping
 */
@Entity
public class GameService {

    @AutoStand
    PersonInfoMapper woMapper;

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
        PersonInfo info = woMapper.selectById(sid);
        if (info == null) {
            info = new PersonInfo();
            info.setName(sid);
            info.setP(1);
            woMapper.insert(info);
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
        woMapper.updateById(pi);
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

    public GameDataContext getContext(String sid) {
        PersonInfo wo = getPersonInfo(sid);
        WhInfo whInfo = getWhInfo(sid, wo.getP());
        return new GameDataContext(sid, whInfo, wo);
    }

    @AutoStand
    UpupMapper upupMapper;

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

    @AutoStand
    GameRules rules;

    public String xl(GameDataContext context) {
        BusinessHandler handler = new CoolDownHandler(new BusinessHandler(null) {
            @Override
            public String progress(GameDataContext context) {
                PersonInfo pi = context.getPersonInfo();
                WhInfo wi = context.getWhInfo();
                int tr = Utils.RANDOM.nextInt(6) + 9;
                int c = GameConfig.getRandXl(wi.getLevel());
                long mx = wi.getXpL();
                long xr = mx / c;
                wi.setXp(Utils.limit(xr + wi.getXp(), wi.getXpL(), 150));
                pi.setK1(System.currentTimeMillis() + (tr * 1000 * 60));
                long hp0 = Utils.algorithm1(wi.hp, wi.hpl, c, 4, 24);
                long hl0 = Utils.algorithm1(wi.hl, wi.hll, c, 4, 24);
                long hj0 = Utils.algorithm1(wi.hj, wi.hjL, c, 4, 30);
                hj0 = rules.getDeserveHj(wi, hj0);
                wi.addHj(hj0);

                hp0 = rules.getDeserveHp(wi, hp0);
                wi.addHp(hp0);

                hl0 = rules.getDeserveHl(wi, hl0);
                wi.addHl(hl0);

//            GInfo.getInstance(who).addXlc().apply();
//            zongMenService.addActivePoint(who, 1);

                sb.append(String.format("你花费了%s分钟修炼", tr)).append(",");
                sb.append(String.format("获得了%s点经验", xr)).append(",");
                sb.append(String.format("恢复了%s点血量", hp0)).append(",");
                sb.append(String.format("恢复了%s点魂力", hl0)).append(",");
                sb.append(String.format("恢复了%s点精神力", hj0)).append(",");
                String out = null;
                if (wi.getWh() <= 0) {
                    out = String.format(FinalFormat.FORMAT_IMAGE, Drawer.drawLines(sb.toString().split(",")));
                } else {
                    out = config.ID_2_NAME_MAPS.get(wi.getWh()) +
                            String.format(FinalFormat.FORMAT_IMAGE, ImageManager.getImgPathById(wi.getWh()))
                            + String.format(FinalFormat.FORMAT_IMAGE, Drawer.drawLines(sb.toString().split(",")));
                }
                return out;
            }
        }, context.getPersonInfo().getK1(), FinalFormat.XL_WAIT_TIPS);
        return handler.progress(context);
    }

    public String upup(GameDataContext context) {
        PersonInfo is = context.getPersonInfo();
        WhInfo wi = context.getWhInfo();
        if (wi.getXp() >= wi.getXpL()) {
            if (wi.getLevel() > FinalConfig.MAX_LEVEL) return "等级最大限制..";
            if (GameConfig.isJTop(wi)) return "无法升级,因为到达等级瓶颈,吸收魂环后继续升级";
//            zongMenService.addActivePoint(who, 5);
            StringBuilder sb = new StringBuilder();
            sb.append("升级成功");
            wi.setLevel(wi.getLevel() + 1);
            wi.setXp(wi.getXp() - wi.getXpL());

            int l = wi.getLevel();
            if (upupMapper.select(context.getPersonInfo().getName(), l, is.getP()) != null) {
                return "在该等级升级过\r\n不增加属性";
            }
            long xpl = GameConfig.getAArtt(l) * 10;
            wi.addXpL(xpl);

            long ir1 = GameConfig.getAArtt(l);
            wi.addHpl(ir1).addHp(ir1);
            sb.append("\r\n增加了:").append(ir1).append("最大血量");

            long ir2 = GameConfig.getAArtt(l);
            wi.addHll(ir2).addHl(ir2);
            sb.append("\r\n增加了:").append(ir2).append("最大魂力");

            long ir3 = GameConfig.getAArtt(l);
            wi.addAtt(ir3);
            sb.append("\r\n增加了:").append(ir3).append("攻击");

            long ir4 = GameConfig.getAArtt(l) / 10;
            wi.addHjL(ir4).addHj(ir4);
            sb.append("\r\n增加了:").append(ir4).append("最大精神力");

//            wo.addGold(50L, new TradingRecord().setType1(TradingRecord.Type1.add).setType0(TradingRecord.Type0.gold).setTo(-1).setMain(who).setFrom(who).setDesc("升级").setMany(50L));

            sb.append("\r\n当前等级:").append(wi.getLevel());
            upupMapper.insert(context.getId(), l, wi.getP());

            return String.format(FinalFormat.FORMAT_IMAGE, Drawer.createImage(sb.toString().split("\r\n")));
        } else {
            return "经验不足,无法升级!";
        }
    }
}
