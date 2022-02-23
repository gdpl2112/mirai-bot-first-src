package Project.dataBases;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import io.github.kloping.mirai0.Entitys.gameEntitys.ShopItem;
import io.github.kloping.mirai0.Main.Resource;
import io.github.kloping.mirai0.unitls.Tools.JsonUtils;
import io.github.kloping.mirai0.unitls.Tools.Tool;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static Project.aSpring.SpringBootResource.getShopItemMapper;

/**
 * @author github-kloping
 */
public class ShopDataBase {
    public static String path;
    private static Integer anID = 0;
    public static final Map<Integer, ShopItem> ITEM_MAP = new ConcurrentHashMap<>();

    public ShopDataBase(String mainPath) {
        try {
            path = mainPath + "/dates/games/mainfist/shops";
            File file = new File(path);
            file.mkdirs();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("ShopDataBase初始化异常");
        }
        Resource.START_AFTER.add(()->{
            initList();
        });
    }

    private void initList() {
        for (ShopItem item : getShopItemMapper().all()) {
            ITEM_MAP.put(item.getId(), item);
            anID = anID < item.getId() ? item.getId() : anID;
        }
        /*File[] files = new File(path).listFiles();
        for (File file : files) {
            String js = Tool.getStringFromFile(file.getPath(), "utf-8");
            ShopItem item = JsonUtils.jsonStringToObject(js, ShopItem.class);
            ITEM_MAP.put(item.getId(), item);
            anID = anID < item.getId() ? item.getId() : anID;
        }*/
    }

    public static synchronized Integer saveItem(ShopItem item) {
        item.setId(getID());
        ITEM_MAP.put(item.getId(), item);
        return getShopItemMapper().insert(item);
//        String js = JsonUtils.objectToJsonString(item);
//        Tool.putStringInFile(js, path + "/" + (item.getId()) + ".json", "utf-8");
//        return item.getId();
    }

    public static synchronized boolean deleteItem(Integer id) {
        ShopItem item = ITEM_MAP.get(id);
        ITEM_MAP.remove(id);
        item.setState(1);
        UpdateWrapper<ShopItem> q = new UpdateWrapper<>();
        q.eq("id", id);
        return getShopItemMapper().update(item, q) > 0;
//        File file = new File(path + "/" + (item.getId()) + ".json");
//        file.delete();
//        return true;
    }

    private static synchronized Integer getID() {
        return ++anID;
    }
}
