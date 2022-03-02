package Project.dataBases;

import Project.aSpring.SpringBootResource;
import io.github.kloping.mirai0.Entitys.TradingRecord;

import java.io.File;
import java.util.List;

/**
 * @author github-kloping
 */
public class OtherDatabase {
    public static String lib = "";

    public OtherDatabase(String lib) {
        OtherDatabase.lib = new File(lib, "records").getAbsolutePath();
    }

    public static List<TradingRecord> getList(long q) {
        return SpringBootResource.getTradingRecordMapper().getList(q);
    }

    public static boolean insert(TradingRecord tradingRecord) {
        return SpringBootResource.getTradingRecordMapper().insert(tradingRecord) > 0;
    }

    /*

    public static String lib = "";

    public OtherDatabase(String lib) {
        OtherDatabase.lib = new File(lib, "records").getAbsolutePath();
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
    }*/
}
