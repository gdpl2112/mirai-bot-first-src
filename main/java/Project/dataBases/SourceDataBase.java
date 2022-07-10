package Project.dataBases;

import io.github.kloping.MySpringTool.annotations.Entity;
import io.github.kloping.mirai0.unitls.Tools.Tool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @author github.kloping
 */
@Entity
public class SourceDataBase {
    private static final Map<Integer, File> ID2FILE = new HashMap<>();

    public SourceDataBase() {
        init();
    }

    private static synchronized void init() {
        if (!ID2FILE.isEmpty()) return;
        for (File file : new File("images/game").listFiles()) {
            String fn = file.getName();
            Integer id = Integer.valueOf(Tool.tool.findNumberFromString(fn));
            ID2FILE.put(id, file);
        }
        System.out.println(ID2FILE);
    }

    public static String getImgPathById(Integer id, Boolean k) {
        if (ID2FILE.isEmpty()) init();
        if (ID2FILE.containsKey(id))
            return k ? Tool.tool.pathToImg(ID2FILE.get(id.intValue()).getAbsolutePath()) :
                    ID2FILE.get(id.intValue()).getAbsolutePath();
        else return null;
    }

    public static String getImgPathById(Integer id) {
        return getImgPathById(id, true);
    }

    public static Image getImageById(Integer id) throws Exception {
        return ImageIO.read(new File(getImgPathById(id, false)));
    }
}
