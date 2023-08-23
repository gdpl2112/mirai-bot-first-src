package Project.dataBases;

import Project.aSpring.SpringBootResource;
import Project.aSpring.dao.TradingRecord;

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
}
