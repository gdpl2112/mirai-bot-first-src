package temp;

import Entitys.gameEntitys.task.Task;
import io.github.kloping.file.FileUtils;
import io.github.kloping.serialize.HMLObject;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class M2 {
    private Map<String, String> maps = new ConcurrentHashMap<>();

    public static void main(String[] args) {
        M2 m2 = new M2();
        m2.maps.put("k1", "v1");
        m2.maps.put("k2", "v2");
        m2.maps.put("k3", String.valueOf(111));
        m2.maps.put("k4", String.valueOf(true));
        m2.maps.put("k5", "???");
        String str = HMLObject.toHMLString(m2);
        FileUtils.putStringInFile(str, new File("./conf/conf.hml"));
    }
}
