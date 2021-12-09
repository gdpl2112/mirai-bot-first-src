package Project.drawers;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class JImageDrawerUtils {

    /**
     * 图片圆角
     *
     * @param image        图片
     * @param cornerRadius 幅度
     * @return
     */
    public static BufferedImage roundImage(BufferedImage image, int cornerRadius) {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage outputImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = outputImage.createGraphics();
        g2.setComposite(AlphaComposite.Src);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.WHITE);
        g2.fill(new RoundRectangle2D.Float(0, 0, width, height, cornerRadius, cornerRadius));
        g2.setComposite(AlphaComposite.SrcAtop);
        g2.drawImage(image, 0, 0, null);
        g2.dispose();
        return outputImage;
    }

    /**
     * 压缩指定宽、高
     *
     * @param bimg
     * @param width
     * @param height
     * @param tagFilePath
     * @return
     */
    public static Image Image2Size(BufferedImage bimg, int width, int height) throws IOException {
        File tempFile = null;
        try {
            tempFile = File.createTempFile("temp1", ".png");
            Thumbnails.of(bimg).size(width, height).outputQuality(1F).toFile(tempFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedImage image = ImageIO.read(tempFile);
        tempFile.delete();
        return image;
    }

    /**
     * 旋转图片
     *
     * @param image
     * @param rotate
     * @return
     * @throws IOException
     */
    public static Image rotateImage(BufferedImage image, float rotate) throws IOException {
        File tempFile = null;
        try {
            int _w = image.getWidth();
            int _h = image.getHeight();
            tempFile = File.createTempFile("temp2", ".png");
            Thumbnails.of(image).scale(1F).rotate(rotate).toFile(tempFile);
            BufferedImage i1 = ImageIO.read(tempFile);
            Thumbnails.of(i1)
                    .sourceRegion(Positions.CENTER, _w, _h)
                    .size(_w, _h)
                    .keepAspectRatio(false)
                    .toFile(tempFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        image = ImageIO.read(tempFile);
        tempFile.delete();
        return image;
    }

    /**
     * 将 一张图片放到 一张图片上
     *
     * @param image
     * @param im
     * @param x
     * @param y
     * @return
     */
    public static BufferedImage putImage(BufferedImage image, BufferedImage im, int x, int y) {
        Graphics graphics = image.getGraphics();
        graphics.drawImage(im, x, y, null);
        return image;
    }

    public static Image getImageByUrl2Size(URL url, int width, int height) {
        try {
            BufferedImage image = ImageIO.read(url);
            return Image2Size(image, width, height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Image getImageByColor2Size(Color color, int width, int height) {
        try {
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
            Graphics2D g2d = image.createGraphics();
            g2d.setClip(0, 0, width, height);
            g2d.setColor(color);
            g2d.fillRect(0, 0, width, height);
            g2d.dispose();
            return image;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
