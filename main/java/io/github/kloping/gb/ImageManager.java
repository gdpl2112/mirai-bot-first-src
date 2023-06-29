package io.github.kloping.gb;

import io.github.kloping.MySpringTool.annotations.Entity;
import io.github.kloping.number.NumberUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author github.kloping
 */
@Entity
public class ImageManager {
    private static final Map<Integer, File> ID2FILE = new HashMap<>();

    public ImageManager() {
        init();
    }

    private static synchronized void init() {
        if (!ID2FILE.isEmpty()) return;
        for (File file : new File("images/game").listFiles()) {
            String fn = file.getName();
            Integer id = Integer.valueOf(NumberUtils.findNumberFromString(fn));
            ID2FILE.put(id, file);
        }
    }

    /**
     * @param id
     * @param k  true 格式路径 false 文件路径
     * @return
     */
    public static String getImgPathById(Integer id) {
        if (ID2FILE.isEmpty()) init();
        if (ID2FILE.containsKey(id))
            return ID2FILE.get(id.intValue()).getAbsolutePath();
        else return null;
    }

    public static InputStream getImgStreamById(Integer id) throws FileNotFoundException {
        return new FileInputStream(getImgPathById(id));
    }

    public static Image getImageById(Integer id) throws Exception {
        return ImageIO.read(new File(getImgPathById(id)));
    }
}
