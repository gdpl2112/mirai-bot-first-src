package Project.DataBases;

import Entitys.TradingRecord;
import com.alibaba.fastjson.JSONObject;
import io.github.kloping.initialize.FileInitializeValue;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class OtherDatabase {
    private static String lib = "";

    public OtherDatabase(String lib) {
        this.lib = new File(lib, "records").getAbsolutePath();
    }

    public static List<TradingRecord> getList(long q) {
        File file = new File(lib, String.format("%s/%s.json", q, q));
        List<JSONObject> list = new LinkedList<>();
        List<TradingRecord> listo = new LinkedList<>();
        list = FileInitializeValue.getValue(file.getAbsolutePath(), list, true);
        list.forEach(e -> {
            listo.add(e.toJavaObject(TradingRecord.class));
        });
        return listo;
    }

    public static List<TradingRecord> apply(long q, List<TradingRecord> list) {
        File file = new File(lib, String.format("%s/%s.json", q, q));
        return FileInitializeValue.putValues(file.getAbsolutePath(), list, true);
    }
}
