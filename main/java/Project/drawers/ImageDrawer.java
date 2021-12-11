package Project.drawers;

import com.madgag.gif.fmsware.AnimatedGifEncoder;
import com.madgag.gif.fmsware.GifDecoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.Random;

import static Project.Tools.Tool.rand;
import static Project.drawers.JImageDrawerUtils.*;

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
        final BufferedImage oImage = ImageIO.read(oFile);
        if (oImage.getHeight() != oImage.getWidth()) throw new RuntimeException("不支持长方形图片");
        GifDecoder decoder = new GifDecoder();
        decoder.read(oFile.openStream());
        final int max = decoder.getFrameCount();
        for (int i = 0; i < files.length; i++) {
            encoder.setDelay(100);
            BufferedImage main = ImageIO.read(files[i]);
            float rotate = rotateEve * i;
            BufferedImage image = max == 0 ? oImage : decoder.getFrame(i >= max ? i - max : i);
            image = (BufferedImage) Image2Size(image, 200, 200);
            image = (BufferedImage) rotateImage(image, rotate);
            image = roundImage(image, 9999);
            image = putImage(main, image, 93, 83);
            encoder.addFrame(image);
        }
        encoder.finish();
        return outFile;
    }

    public static File getWq(File[] files, URL oFile, File outFile) throws Exception {
        AnimatedGifEncoder encoder = new AnimatedGifEncoder();
        encoder.start(outFile.getAbsolutePath());
        encoder.setRepeat(0);
        encoder.setQuality(5);
        encoder.setFrameRate(200);
        final BufferedImage oImage = ImageIO.read(oFile);
        if (oImage.getHeight() != oImage.getWidth()) throw new RuntimeException("不支持长方形图片");
        GifDecoder decoder = new GifDecoder();
        decoder.read(oFile.openStream());
        final int max = decoder.getFrameCount();
        int rotateEve = 360 / files.length;
        for (int i = 0; i < files.length; i++) {
            encoder.setDelay(100);
            BufferedImage main = ImageIO.read(files[i]);
            BufferedImage image = max == 0 ? oImage : decoder.getFrame(i >= max ? i - max : i);
            float rotate = rotateEve * i;
            image = (BufferedImage) Image2Size(image, 132, 132);
            image = (BufferedImage) rotateImage(image, rotate);
            image = roundImage(image, 9999);
            int[] vs = getWt(i);
            image = putImage(main, image, vs[0], vs[1]);
            encoder.addFrame(image);
//            ImageIO.write(image, "png", new File("./data/temp/" + i + ".png"));
        }
        encoder.finish();
        return outFile;
    }

    private static int[] getWt(int i) {
        int x = 52, y = 27;
        switch (i) {
            case 2:
                y += 8;
                x -= 2;
                break;
            case 3:
                y += 6;
                x -= 2;
                break;
            case 4:
                y += 7;
                x -= 2;
                break;
            case 5:
                y += 7;
                x -= 3;
                break;
            case 6:
                y += 3;
                x -= 3;
                break;
            case 7:
                y += 1;
                x -= 3;
                break;
            case 8:
            case 9:
                x -= 3;
                break;
            case 10:
                y -= 5;
                x -= 3;
                break;
            case 11:
                y -= 5;
                x -= 5;
                break;
            case 13:
                y -= 4;
                x -= 4;
                break;
            case 14:
                y += 3;
                x -= 2;
                break;
            case 15:
                y += 2;
                x -= 2;
                break;
            case 16:
                x -= 1;
                y += 1;
                break;
            case 25:
                y += 3;
                x += 3;
                break;
            case 26:
                y += 4;
                break;
            case 27:
                y += 4;
                break;
            case 28:
            case 29:
                x -= 1;
                y += 3;
                break;

        }
        return new int[]{x, y};
    }

    public static File getDui(File file, URL oFile, File outFile) throws Exception {
        BufferedImage oImage = ImageIO.read(oFile);
        oImage = roundImage(oImage, 9999);
        oImage = (BufferedImage) Image2Size(oImage, 150, 150);
        oImage = (BufferedImage) rotateImage(oImage, rand.nextInt(160) + 60);
        BufferedImage bgImage = ImageIO.read(file);
        bgImage = (BufferedImage) Image2Size(bgImage, 512, 512);
        BufferedImage image = putImage(bgImage, oImage, 10, 175);
        ImageIO.write(image, "png", outFile);
        return outFile;
    }

    public static void main(String[] args) throws Exception {
        getDui(new File("./images/diu/diu.png"), new URL("https://q1.qlogo.cn/g?b=qq&nk=189696825&s=640")
                , new File("./temp/a.png"));

    }
}
