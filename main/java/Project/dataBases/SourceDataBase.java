package Project.dataBases;

import Project.utils.Tools.Tool;
import io.github.kloping.MySpringTool.annotations.Entity;

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
            Integer id = Integer.valueOf(Tool.INSTANCE.findNumberFromString(fn));
            ID2FILE.put(id, file);
        }
    }

    /**
     * @param id
     * @param k  true 格式路径 false 文件路径
     * @return
     */
    public static String getImgPathById(Integer id, Boolean k) {
        if (ID2FILE.isEmpty()) init();
        if (ID2FILE.containsKey(id))
            return k ? Tool.INSTANCE.pathToImg(ID2FILE.get(id.intValue()).getAbsolutePath()) :
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
