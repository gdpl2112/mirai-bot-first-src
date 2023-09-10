package io.github.kloping.kzero.bot.database;

import io.github.kloping.MySpringTool.annotations.Entity;
import io.github.kloping.kzero.game.ResourceSet;
import io.github.kloping.number.NumberUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author github.kloping
 */
@Entity
public class SourceDataBase {
    private static final Map<String, File> FN2FILE = new HashMap<>();
    private static final Map<Integer, File> ID2FILE = new HashMap<>();

    static {
        for (File file : new File("images").listFiles()) {
            if (file.isDirectory()) {
                for (File f0 : file.listFiles()) {
                    String fn = f0.getName();
                    FN2FILE.put(fn, f0);
                    FN2FILE.put(fn.substring(0, fn.lastIndexOf(".")), f0);
                    ID2FILE.put(NumberUtils.getIntegerFromString(fn, -1), f0);
                }
            } else {
                String fn = file.getName();
                FN2FILE.put(fn, file);
                FN2FILE.put(fn.substring(0, fn.lastIndexOf(".")), file);
                ID2FILE.put(NumberUtils.getIntegerFromString(fn, 0), file);
            }
        }
    }

    /**
     * @param id
     * @param k  true 格式路径 false 文件路径
     * @return
     */
    public String getImgPathById(Integer id, Boolean k) {
        if (ID2FILE.containsKey(id))
            return k ? String.format(ResourceSet.FinalFormat.PIC_FORMAT0, ID2FILE.get(id.intValue()).getAbsolutePath()) : ID2FILE.get(id.intValue()).getAbsolutePath();
        else return null;
    }

    public String getImgPathById(Integer id) {
        return getImgPathById(id, true);
    }

    public String getImgPathById(String fn) {
        if (FN2FILE.containsKey(fn)) return FN2FILE.get(fn).getAbsolutePath();
        return null;
    }

    public Image getImageById(Integer id) throws Exception {
        return ImageIO.read(new File(getImgPathById(id, false)));
    }

    public String save(BufferedImage temp) {
        try {
            String fn = UUID.randomUUID() + ".jpg";
            File file = new File("./temp/" + fn);
            ImageIO.write(temp, "jpg", file);
            return file.getPath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}