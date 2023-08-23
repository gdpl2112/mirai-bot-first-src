package Project.services.impl;

import Project.commons.SpGroup;
import Project.aSpring.dao.TradingRecord;
import Project.commons.broadcast.enums.ObjType;
import Project.aSpring.dao.ShopItem;
import Project.dataBases.GameDataBase;
import Project.dataBases.ShopDataBase;
import Project.dataBases.SourceDataBase;
import Project.interfaces.Iservice.IShoperService;
import io.github.kloping.MySpringTool.annotations.Entity;
import Project.aSpring.dao.GInfo;
import Project.aSpring.dao.PersonInfo;
import Project.utils.Tools.Tool;

import static Project.commons.rt.ResourceSet.FinalString.*;
import static Project.dataBases.GameDataBase.*;
import static Project.utils.drawers.Drawer.getImageFromStringsOnTwoColumns;

/**
 * @author github-kloping
 */
@Entity
public class ShoperServiceImpl implements IShoperService {
    private static final String INFO = "\r\n市场上架上架物品\t" +
            "示例:\r\n\t" +
            "=>市场上架百年魂环2个1000<=\r\n\t " +
            "以上架2个百年魂环总价为1000金魂币\r\n" +
            "市场下架 (序号) 来下架之前上架的物品\r\n" +
            "市场说明 (序号) 来查看物品信息\r\n" +
            "市场购买 (序号) 来购买 物品";

    @Override
    public String allInfo(SpGroup group) {
        StringBuilder sb = new StringBuilder();
        for (Integer id : ShopDataBase.ITEM_MAP.keySet()) {
            ShopItem item = ShopDataBase.ITEM_MAP.get(id);
            sb.append(id).append(",").append(getNameById(item.getItemId()))
                    .append("x").append(item.getNum())
                    .append("=>").append(item.getPrice()).append("金魂币").append("\r\n");
        }
        String[] sss = sb.toString().split("\r\n");
        return getImageFromStringsOnTwoColumns(sb.toString().isEmpty() ? new String[]{"暂无上架物品"} : sss) + INFO;
    }

    @Override
    public String upItem(long id, Integer id1, long aLong, Long aLong1) {
        if (GameDataBase.containsBgsNum(id, id1, (int) aLong)) {
            GameDataBase.removeFromBgs(id, id1, (int) aLong, ObjType.sell);
            ShopItem item = new ShopItem()
                    .setItemId(id1).setWho(id).setPrice(aLong1)
                    .setNum(Long.valueOf(aLong).intValue()).setTime(System.currentTimeMillis());
            ShopDataBase.saveItem(item);
            return UP_SHOP_ITEM_OK;
        } else {
            return ("你没有足够的 " + getNameById(id1));
        }
    }

    @Override
    public synchronized String downItem(long qid, int ids) {
        if (ShopDataBase.ITEM_MAP.containsKey(ids)) {
            ShopItem item = ShopDataBase.ITEM_MAP.get(ids);
            Long who = item.getWho().longValue();
            if (who == qid) {
                ShopDataBase.deleteItem(item.getId());
                addToBgs(qid, item.getItemId(), item.getNum(), ObjType.un);
                return DOWN_SHOP_ITEM_OK;
            } else {
                return SHOP_ITEM_NOT_IS_YOU;
            }
        } else {
            return NOT_FOUND_SHOP_ITEM;
        }
    }

    @Override
    public synchronized String buy(long qid, Integer ids) {
        if (ShopDataBase.ITEM_MAP.containsKey(ids)) {
            ShopItem item = ShopDataBase.ITEM_MAP.get(ids);
            PersonInfo info = getInfo(qid);
            Long price = item.getPrice().longValue();
            if (info.getGold() >= price) {
                Long who = item.getWho().longValue();
                getInfo(who).addGold(price
                        , new TradingRecord()
                                .setType1(TradingRecord.Type1.add)
                                .setType0(TradingRecord.Type0.gold)
                                .setTo(who)
                                .setMain(who)
                                .setFrom(qid)
                                .setDesc("市场被购买" + item.getNum() + "个\"" + getNameById(item.getItemId()) + "\"")
                                .setMany(price)
                ).apply();
                getInfo(qid).addGold(-price
                        , new TradingRecord()
                                .setType1(TradingRecord.Type1.lost)
                                .setType0(TradingRecord.Type0.gold)
                                .setTo(who)
                                .setMain(qid)
                                .setFrom(qid)
                                .setDesc("市场购买" + item.getNum() + "个\"" + getNameById(item.getItemId()) + "\"")
                                .setMany(price)
                ).apply();
                addToBgs(qid, item.getItemId(), item.getNum(), ObjType.buy);
                ShopDataBase.deleteItem(item.getId());
                GInfo.getInstance(who).addBuyc().apply();
                return BUY_SUCCESS;
            } else return NOT_ENOUGH_GOLD;
        } else
            return NOT_FOUND_SHOP_ITEM;
    }

    @Override
    public String intro(long id, Integer ids, SpGroup group) {
        if (ShopDataBase.ITEM_MAP.containsKey(ids)) {
            ShopItem item = ShopDataBase.ITEM_MAP.get(ids);
            StringBuilder sb = new StringBuilder();
            sb.append(getNameById(item.getItemId())).append("\r\n");
            sb.append(SourceDataBase.getImgPathById(item.getItemId())).append("\r\n");
            sb.append("序号:").append(item.getId()).append("\r\n");
            sb.append("上架人:").append(item.getWho()).append("\r\n");
            sb.append("数量:").append(item.getNum()).append("\r\n");
            sb.append("价格:").append(item.getPrice()).append("金魂币\r\n");
            sb.append("上架时间:").append(Tool.INSTANCE.getTimeYMdhms(item.getTime()));
            return sb.toString();
        } else {
            return NOT_FOUND_SHOP_ITEM;
        }
    }
}
