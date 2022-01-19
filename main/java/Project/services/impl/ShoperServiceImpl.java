package Project.services.impl;

import Entitys.Group;
import Entitys.TradingRecord;
import Entitys.gameEntitys.PersonInfo;
import Entitys.gameEntitys.ShopItem;
import Project.DataBases.GameDataBase;
import Project.DataBases.ShopDataBase;
import Project.broadcast.enums.ObjType;
import Project.services.Iservice.IShoperService;
import io.github.kloping.MySpringTool.annotations.Entity;

import static Project.DataBases.GameDataBase.*;
import static Project.ResourceSet.Final.*;
import static Project.Tools.Tool.getTimeYMdhms;
import static Project.drawers.Drawer.getImageFromStringsOnTwoColumns;

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
    public String allInfo(Group group) {
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
        if (GameDataBase.contiansBgsNum(id, id1, (int) aLong)) {
            GameDataBase.removeFromBgs(id, id1, (int) aLong, ObjType.sell);
            ShopItem item = new ShopItem()
                    .setItemId(id1).setWho(id).setPrice(aLong1)
                    .setNum(Integer.valueOf(aLong + "")).setTime(System.currentTimeMillis());
            ShopDataBase.saveItem(item);
            return UP_SHOP_ITEM_OK;
        } else
            return ("你没有足够的 " + getNameById(id1));
    }

    @Override
    public synchronized String downItem(long id, int ids) {
        if (ShopDataBase.ITEM_MAP.containsKey(ids)) {
            ShopItem item = ShopDataBase.ITEM_MAP.get(ids);
            Long who = item.getWho().longValue();
            if (who == id) {
                ShopDataBase.deleteItem(item.getId());
                addToBgs(id, item.getItemId(), item.getNum(), ObjType.un);
                return DOWN_SHOP_ITEM_OK;
            } else {
                return SHOP_ITEM_NOT_IS_YOU;
            }
        } else {
            return NOT_FOUND_SHOP_ITEM;
        }
    }

    @Override
    public synchronized String buy(long id, Integer ids) {
        if (ShopDataBase.ITEM_MAP.containsKey(ids)) {
            ShopItem item = ShopDataBase.ITEM_MAP.get(ids);
            PersonInfo info = getInfo(id);
            Long price = item.getPrice().longValue();
            if (info.getGold() >= price) {
                Long who = item.getWho().longValue();
                putPerson(getInfo(who).addGold(price
                        , new TradingRecord()
                                .setType1(TradingRecord.Type1.add)
                                .setType0(TradingRecord.Type0.gold)
                                .setTo(who)
                                .setMain(who)
                                .setFrom(id)
                                .setDesc("市场被购买" + item.getNum() + "个\"" + getNameById(item.getId()) + "\"")
                                .setMany(price)
                ));
                putPerson(getInfo(id).addGold(-price
                        , new TradingRecord()
                                .setType1(TradingRecord.Type1.lost)
                                .setType0(TradingRecord.Type0.gold)
                                .setTo(who)
                                .setMain(id)
                                .setFrom(id)
                                .setDesc("市场购买" + item.getNum() + "个\"" + getNameById(item.getId()) + "\"")
                                .setMany(price)
                ));
                addToBgs(id, item.getItemId(), item.getNum(), ObjType.buy);
                ShopDataBase.deleteItem(item.getId());
                return BUY_SUCCESS;
            } else return NOT_ENOUGH_GOLD;
        } else
            return NOT_FOUND_SHOP_ITEM;
    }

    @Override
    public String intro(long id, Integer ids, Group group) {
        if (ShopDataBase.ITEM_MAP.containsKey(ids)) {
            ShopItem item = ShopDataBase.ITEM_MAP.get(ids);
            StringBuilder sb = new StringBuilder();
            sb.append(getNameById(item.getItemId())).append("\r\n");
            sb.append(getImgById(item.getItemId())).append("\r\n");
            sb.append("序号:").append(item.getId()).append("\r\n");
            sb.append("上架人:").append(item.getWho()).append("\r\n");
            sb.append("数量:").append(item.getNum()).append("\r\n");
            sb.append("价格:").append(item.getPrice()).append("金魂币\r\n");
            sb.append("上架时间:").append(getTimeYMdhms(item.getTime()));
            return sb.toString();
        } else {
            return NOT_FOUND_SHOP_ITEM;
        }
    }
}
