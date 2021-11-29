package Project.Tools;

import com.madgag.gif.fmsware.AnimatedGifEncoder;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class ImageDrawer {

    /**
     * 推 的动画
     *
     * @param files   gif集
     * @param oFile   要被推的 url
     * @param outFile 输出
     * @return
     * @throws Exception
     */
    public static File getTuiGift(File[] files, URL oFile, File outFile) throws Exception {
        AnimatedGifEncoder encoder = new AnimatedGifEncoder();
        encoder.start(outFile.getAbsolutePath());
        encoder.setRepeat(0);
        encoder.setQuality(5);
        encoder.setFrameRate(200);
        int rotateEve = 360 / files.length;
        for (int i = 0; i < files.length; i++) {
            encoder.setDelay(100);
            BufferedImage main = ImageIO.read(files[i]);
            BufferedImage image = ImageIO.read(oFile);
            float rotate = rotateEve * i;
            image = (BufferedImage) Image2Size(image, 200, 200);
            image = (BufferedImage) rotateImage(image, rotate);
            image = roundImage(image, 9999);

            image = putImage(main, image, 93, 83);
            encoder.addFrame(image);
        }
        encoder.finish();
        return outFile;
    }

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
            int w = i1.getWidth();
            int h = i1.getHeight();
            Thumbnails.of(i1)
                    .sourceRegion(Positions.CENTER, 200, 200)
                    .size(200, 200)
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
}
