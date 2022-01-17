package Project.services.impl;

import Entitys.Group;
import Entitys.TradingRecord;
import Entitys.gameEntitys.PersonInfo;
import Entitys.gameEntitys.ShopItem;
import Project.DataBases.GameDataBase;
import Project.DataBases.ShopDataBase;
import Project.services.Iservice.IShoperService;
import Project.broadcast.enums.ObjType;
import io.github.kloping.MySpringTool.annotations.Entity;

import static Project.DataBases.GameDataBase.*;
import static Project.Tools.Tool.getTimeDDHHMM;
import static Project.drawers.Drawer.getImageFromStringsOnTwoColumns;

@Entity
public class ShoperServiceImpl implements IShoperService {
    private static final String info = "\r\n市场上架上架物品\t" +
            "示例:\r\n\t" +
            "=>市场上架百年魂环2个1000<=\r\n\t " +
            "以上架2个百年魂环总价为1000金魂币\r\n" +
            "市场下架 (序号) 来下架之前上架的物品\r\n" +
            "市场说明 (序号) 来查看物品信息\r\n" +
            "市场购买 (序号) 来购买 物品";

    @Override
    public String AllInfo(Group group) {
        StringBuilder sb = new StringBuilder();
        for (Integer id : ShopDataBase.map.keySet()) {
            ShopItem item = ShopDataBase.map.get(id);
            sb.append(id).append(",").append(getNameById(item.getItemId()))
                    .append("x").append(item.getNum())
                    .append("=>").append(item.getPrice()).append("金魂币").append("\r\n");
        }
        String[] sss = sb.toString().split("\r\n");
        return getImageFromStringsOnTwoColumns(sb.toString().isEmpty() ? new String[]{"暂无上架物品"} : sss) + info;
    }

    @Override
    public String UpItem(long id, Integer id1, long aLong, Long aLong1) {
        if (GameDataBase.contiansBgsNum(id, id1, (int) aLong)) {
            GameDataBase.removeFromBgs(id, id1, (int) aLong, ObjType.sell);
            ShopItem item = new ShopItem()
                    .setItemId(id1).setWho(id).setPrice(aLong1)
                    .setNum(Integer.valueOf(aLong + "")).setTime(System.currentTimeMillis());
            ShopDataBase.saveItem(item);
            return UpShopItemOk;
        } else
            return ("你没有足够的 " + getNameById(id1));
    }

    @Override
    public synchronized String DownItem(long id, int ids) {
        if (ShopDataBase.map.containsKey(ids)) {
            ShopItem item = ShopDataBase.map.get(ids);
            Long who = item.getWho().longValue();
            if (who == id) {
                ShopDataBase.deleteItem(item.getId());
                addToBgs(id, item.getItemId(), item.getNum(), ObjType.un);
                return DownShopItemOk;
            } else
                return ShopItemNotIsYou;
        } else
            return NotFoundShopItem;
    }

    @Override
    public synchronized String Buy(long id, Integer ids) {
        if (ShopDataBase.map.containsKey(ids)) {
            ShopItem item = ShopDataBase.map.get(ids);
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
                return BuySuccess;
            } else return NotEnoughGold;
        } else
            return NotFoundShopItem;
    }

    @Override
    public String Intro(long id, Integer ids, Group group) {
        if (ShopDataBase.map.containsKey(ids)) {
            ShopItem item = ShopDataBase.map.get(ids);
            StringBuilder sb = new StringBuilder();
            sb.append(getNameById(item.getItemId())).append("\r\n");
            sb.append(getImgById(item.getItemId())).append("\r\n");
            sb.append("序号:").append(item.getId()).append("\r\n");
            sb.append("上架人:").append(item.getWho()).append("\r\n");
            sb.append("数量:").append(item.getNum()).append("\r\n");
            sb.append("价格:").append(item.getPrice()).append("金魂币\r\n");
            sb.append("上架时间:").append(getTimeDDHHMM(item.getTime()));
            return sb.toString();
        } else
            return NotFoundShopItem;
    }

    private static final String
            NotFoundShopItem = ("未发现此商品"),
            DownShopItemOk = ("下架完成!"),
            UpShopItemOk = ("上架成功!!!"),
            ShopItemNotIsYou = ("那不是你上架的物品"),
            NotEnoughGold = ("金魂币不足"),
            BuySuccess = ("购买成功");
}
