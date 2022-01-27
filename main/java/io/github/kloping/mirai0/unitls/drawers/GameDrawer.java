package io.github.kloping.mirai0.unitls.drawers;

import io.github.kloping.mirai0.unitls.drawers.entity.GameMap;
import io.github.kloping.mirai0.unitls.drawers.entity.MapPosition;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.UUID;

import static io.github.kloping.mirai0.unitls.drawers.JImageDrawerUtils.getImageByUrl2Size;

/**
 * @author github-kloping
 */
public class GameDrawer {
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
            }
        }
        g.dispose();
        String name = UUID.randomUUID() + ".jpg";
        new File("./temp").mkdirs();
        File file = new File("./temp/" + name);
        try {
            ImageIO.write(image, "png", file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file.getPath();
    }
}
