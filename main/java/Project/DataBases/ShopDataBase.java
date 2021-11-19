package Project.DataBases;

import Entitys.ShopItem;
import Project.Tools.JSONUtils;
import Project.Tools.Tool;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ShopDataBase {
    public static String path;
    private static Integer _id = 0;
    public static final Map<Integer, ShopItem> map = new ConcurrentHashMap<>();

    public ShopDataBase(String mainPath) {
        try {
            path = mainPath + "/dates/games/mainfist/shops";
            File file = new File(path);
            file.mkdirs();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("ShopDataBase初始化异常");
        }
        InitList();
    }

    private void InitList() {
        File[] files = new File(path).listFiles();
        for (File file : files) {
            String js = Tool.getStringFromFile(file.getPath(), "utf-8");
            ShopItem item = JSONUtils.JsonStringToObject(js, ShopItem.class);
            map.put(item.getId(), item);
            _id = _id < item.getId() ? item.getId() : _id;
        }
    }

    public static synchronized Integer saveItem(ShopItem item) {
        item.setId(get_id());
        String js = JSONUtils.ObjectToJsonString(item);
        Tool.putStringInFile(js, path + "/" + (item.getId()) + ".json", "utf-8");
        map.put(item.getId(), item);
        return item.getId();
    }

    public static synchronized boolean deleteItem(Integer id) {
        ShopItem item = map.get(id);
        map.remove(id);
        File file = new File(path + "/" + (item.getId()) + ".json");
        file.delete();
        return true;
    }

    private static Integer get_id() {
        return ++_id;
    }
}
