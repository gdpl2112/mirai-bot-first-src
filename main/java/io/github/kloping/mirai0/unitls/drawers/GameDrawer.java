package io.github.kloping.mirai0.unitls.drawers;

import com.madgag.gif.fmsware.AnimatedGifEncoder;
import io.github.kloping.mirai0.unitls.drawers.entity.GameMap;
import io.github.kloping.mirai0.unitls.drawers.entity.MapPosition;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.*;

import static io.github.kloping.mirai0.unitls.drawers.ImageDrawerUtils.getImageByColor2Size;
import static io.github.kloping.mirai0.unitls.drawers.ImageDrawerUtils.getImageByUrl2Size;

/**
 * @author github-kloping
 */
public class GameDrawer {
    public static String drawerMap0(GameMap map) {
        try {
            return drawerMap(map);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String drawerMap(GameMap map) throws IOException {
        int wc = map.getWidth();
        int hc = map.getHeight();
        int width = wc * 50;
        int height = hc * 50;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
        Graphics g = image.getGraphics();
        g.setClip(0, 0, width, height);
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);
        g.setColor(Color.BLACK);
        for (int i = 1; i <= wc; i++) {
            g.drawString(String.valueOf(i), 50 * (i - 1), 10);
            g.drawLine(50 * i, 0, 50 * i, height);
        }
        for (int i1 = 1; i1 <= hc; i1++) {
            g.drawString(String.valueOf(i1), 0, 50 * (i1 - 1) + 10);
            g.drawLine(0, 50 * i1, width, 50 * i1);
        }
        for (MapPosition position : map.getPositions()) {
            int x1;
            int y1;
            switch (position.getType()) {
                case p:
                    x1 = (position.getX() - 1) * 50;
                    y1 = (position.getY() - 1) * 50;
                    g.drawImage(getImageByUrl2Size(new URL(position.getArg().toString()), 50, 50)
                            , x1, y1, null);
                    break;
                default:
                    break;
            }
        }
        g.dispose();
        String name = UUID.randomUUID() + ".png";
        new File("./temp").mkdirs();
        File file = new File("./temp/" + name);
        try {
            ImageIO.write(image, "png", file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file.getPath();
    }

    public static String drawerStatic(GameMap map) throws Exception {
        int wc = map.getWidth();
        int hc = map.getHeight();
        int width = wc * 50;
        int height = hc * 50;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
        Graphics g = image.getGraphics();
        g.setClip(0, 0, width, height);
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);
        g.setColor(Color.BLACK);
        for (int i = 1; i <= wc; i++) {
            g.drawString(String.valueOf(i), 50 * (i - 1), 10);
            g.drawLine(50 * i, 0, 50 * i, height);
        }
        for (int i1 = 1; i1 <= hc; i1++) {
            g.drawString(String.valueOf(i1), 0, 50 * (i1 - 1) + 10);
            g.drawLine(0, 50 * i1, width, 50 * i1);
        }
        for (MapPosition position : map.getPositions()) {
            int x1;
            int y1;
            switch (position.getType()) {
                case p:
                    x1 = (position.getX() - 1) * 50;
                    y1 = (position.getY() - 1) * 50;
                    g.drawImage(getImageByUrl2Size(new File(position.getArg().toString()).toURL(), 50, 50)
                            , x1, y1, null);
                    break;
                default:
                    break;
            }
        }
        g.dispose();
        String name = UUID.randomUUID() + ".png";
        new File("./temp").mkdirs();
        File file = new File("./temp/" + name);
        try {
            ImageIO.write(image, "png", file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file.getPath();
    }

    private static final List<Map.Entry<Integer, Integer>> OR_LIST = new LinkedList<>();
    private static final List<Integer> RATE_LIST = new LinkedList<>();

    static {
        for (int i1 = 1; i1 <= 5; i1++) {
            OR_LIST.add(new AbstractMap.SimpleEntry<>(i1, 1));
        }
        OR_LIST.add(new AbstractMap.SimpleEntry<>(5, 2));
        for (int i1 = 5; i1 > 0; i1--) {
            OR_LIST.add(new AbstractMap.SimpleEntry<>(i1, 3));
        }
        OR_LIST.add(new AbstractMap.SimpleEntry<>(1, 2));

        RATE_LIST.add(16);
        RATE_LIST.add(15);
        RATE_LIST.add(13);
        RATE_LIST.add(10);
        RATE_LIST.add(7);
        RATE_LIST.add(3);
        for (int i = 0; i < 30; i++) {
            RATE_LIST.add(3);
        }
        RATE_LIST.add(3);
        RATE_LIST.add(7);
        RATE_LIST.add(10);
        RATE_LIST.add(13);
        RATE_LIST.add(15);
        RATE_LIST.add(16);
    }

    public static String drawerDynamic(GameMap map, int od, String end, File outFile) throws Exception {
        File baseFile = new File(drawerStatic(map));
        AnimatedGifEncoder encoder = new AnimatedGifEncoder();
        encoder.start(outFile.getAbsolutePath());
        encoder.setRepeat(0);
        encoder.setQuality(5);
        int rate = 25;
        encoder.setFrameRate(rate);
        int r = od - 5;
        r = r < 0 ? 12 + r : r;
        for (Integer y : RATE_LIST) {
            if (r >= OR_LIST.size()) r -= OR_LIST.size();
            Map.Entry<Integer, Integer> kv = OR_LIST.get(r);
            r++;
            int x1 = (kv.getKey() - 1) * 50;
            int y1 = (kv.getValue() - 1) * 50;
            BufferedImage o0 = (BufferedImage) getImageByColor2Size(
                    new Color(238, 0, 0, 255)
                    , 50, 10);
            o0 = ImageDrawerUtils.putImage(ImageIO.read(baseFile), o0, x1, y1);
            for (int i = 0; i < y; i++) {
                encoder.addFrame(o0);
            }
        }
        int x1;
        int y1;
        BufferedImage o0;
        BufferedImage o1;
        x1 = 1 * 50;
        y1 = 1 * 50;
        o0 = (BufferedImage) getImageByUrl2Size(new File(end).toURL(), 50, 50);
        o1 = ImageDrawerUtils.putImage(ImageIO.read(baseFile), o0, x1, y1);
        x1 = 2 * 50;
        y1 = 1 * 50;
        o0 = (BufferedImage) getImageByUrl2Size(new File(end).toURL(), 50, 50);
        o0 = ImageDrawerUtils.putImage(o1, o0, x1, y1);
        x1 = 3 * 50;
        y1 = 1 * 50;
        o0 = (BufferedImage) getImageByUrl2Size(new File(end).toURL(), 50, 50);
        o0 = ImageDrawerUtils.putImage(o1, o0, x1, y1);
        for (int i = 0; i < rate * 2; i++) {
            encoder.addFrame(o0);
        }
        encoder.finish();
        return outFile.getAbsolutePath();
    }
}
