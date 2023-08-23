package Project.dataBases;

import Project.aSpring.dao.ShopItem;
import io.github.kloping.mirai0.Main.BootstarpResource;

import java.util.Map;
import java.util.TreeMap;

import static Project.aSpring.SpringBootResource.getShopItemMapper;

/**
 * @author github-kloping
 */
public class ShopDataBase {
    public static final Map<Integer, ShopItem> ITEM_MAP = new TreeMap<>();
    private static Integer anID = 0;

    public ShopDataBase() {
        BootstarpResource.START_AFTER.add(() -> {
            initList();
        });
    }

    public static synchronized Integer saveItem(ShopItem item) {
        item.setId(getId());
        ITEM_MAP.put(item.getId(), item);
        return getShopItemMapper().insert(item);
    }

    public static synchronized boolean deleteItem(Integer id) {
        ShopItem item = ITEM_MAP.get(id);
        ITEM_MAP.remove(id.intValue());
        return getShopItemMapper().deleteById(id) > 0;
    }

    private static Integer getId() {
        return ++anID;
    }

    public void initList() {
        for (ShopItem item : getShopItemMapper().all()) {
            ITEM_MAP.put(item.getId(), item);
            anID = anID < item.getId() ? item.getId() : anID;
        }
    }
}
