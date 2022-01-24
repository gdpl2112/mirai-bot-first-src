package Project.drawers;

import com.madgag.gif.fmsware.AnimatedGifEncoder;
import com.madgag.gif.fmsware.GifDecoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import static Project.ResourceSet.FinalString.NOT_SUPPORT_LENGTH_IMG;
import static Project.Tools.Tool.getTouUrl;
import static Project.Tools.Tool.rand;
import static Project.drawers.JImageDrawerUtils.*;

/**
 * @author github-kloping
 */
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
        if (oImage.getHeight() != oImage.getWidth()) throw new RuntimeException(NOT_SUPPORT_LENGTH_IMG);
        GifDecoder decoder = new GifDecoder();
        decoder.read(oFile.openStream());
        final int max = decoder.getFrameCount();
        for (int i = 0; i < files.length; i++) {
            encoder.setDelay(100);
            BufferedImage main = ImageIO.read(files[i]);
            float rotate = rotateEve * i;
            BufferedImage image = max == 0 ? oImage : decoder.getFrame(i >= max ? i - max : i);
            image = (BufferedImage) image2Size(image, 200, 200);
            image = (BufferedImage) rotateImage(image, rotate);
            image = roundImage(image, 9999);
            image = putImage(main, image, 93, 83);
            encoder.addFrame(image);
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
            default:
                return new int[]{x, y};
        }
        return new int[]{x, y};
    }

    /**
     * 丢的动画
     *
     * @param file
     * @param oFile
     * @param outFile
     * @return
     * @throws Exception
     */
    public static File getDui(File file, URL oFile, File outFile) throws Exception {
        BufferedImage oImage = ImageIO.read(oFile);
        if (oImage.getHeight() != oImage.getWidth()) throw new RuntimeException(NOT_SUPPORT_LENGTH_IMG);
        oImage = roundImage(oImage, 9999);
        oImage = (BufferedImage) image2Size(oImage, 150, 150);
        oImage = (BufferedImage) rotateImage(oImage, rand.nextInt(160) + 60);
        BufferedImage bgImage = ImageIO.read(file);
        bgImage = (BufferedImage) image2Size(bgImage, 512, 512);
        BufferedImage image = putImage(bgImage, oImage, 10, 175);
        ImageIO.write(image, "png", outFile);
        return outFile;
    }

    /**
     * 玩球的动画
     *
     * @param files
     * @param oFile
     * @param outFile
     * @return
     * @throws Exception
     */
    public static File getWq(File[] files, URL oFile, File outFile) throws Exception {
        AnimatedGifEncoder encoder = new AnimatedGifEncoder();
        encoder.start(outFile.getAbsolutePath());
        encoder.setRepeat(0);
        encoder.setQuality(5);
        encoder.setFrameRate(200);
        final BufferedImage oImage = ImageIO.read(oFile);
        if (oImage.getHeight() != oImage.getWidth()) throw new RuntimeException(NOT_SUPPORT_LENGTH_IMG);
        GifDecoder decoder = new GifDecoder();
        decoder.read(oFile.openStream());
        final int max = decoder.getFrameCount();
        int rotateEve = 360 / files.length;
        for (int i = 0; i < files.length; i++) {
            encoder.setDelay(100);
            BufferedImage main = ImageIO.read(files[i]);
            BufferedImage image = max == 0 ? oImage : decoder.getFrame(i >= max ? i - max : i);
            float rotate = rotateEve * i;
            image = (BufferedImage) image2Size(image, 132, 132);
            image = (BufferedImage) rotateImage(image, rotate);
            image = roundImage(image, 9999);
            int[] vs = getWt(i);
            image = putImage(main, image, vs[0], vs[1]);
            encoder.addFrame(image);
        }
        encoder.finish();
        return outFile;
    }

    public static File getGunOnDirt(File emptyFile, URL url, File dirtFile, int r0, File outFile) throws IOException {
        AnimatedGifEncoder encoder = new AnimatedGifEncoder();
        encoder.start(outFile.getAbsolutePath());
        encoder.setRepeat(0);
        encoder.setQuality(5);
        encoder.setFrameRate(100);
        encoder.setDelay(100);
        final BufferedImage bgImage = ImageIO.read(emptyFile);
        final BufferedImage dirt = ImageIO.read(dirtFile);
        final BufferedImage oImage = ImageIO.read(url);
        int sl = 24 * r0;
        float r = 0;
        int w = bgImage.getWidth();
        int h = bgImage.getHeight();
        for (int i = 0; i < sl; i++) {
            r += (360 * r0 / sl);
            BufferedImage main = ImageIO.read(emptyFile);
            BufferedImage o1 = (BufferedImage) image2Size(oImage, 60, 60);
            o1 = roundImage(o1, 9999);
            o1 = (BufferedImage) rotateImage(o1, r);
            o1 = putImage(main, o1, (w / sl) * i, h / 2);
            o1 = putImage(o1, dirt, 0, h / 2 + 60);
            encoder.addFrame(o1);
        }
        encoder.finish();
        return outFile;
    }

    public static void main(String[] args) throws Throwable {
        File outFile = new File("./data/out.gif");
        AnimatedGifEncoder encoder = new AnimatedGifEncoder();
        encoder.start(outFile.getAbsolutePath());
        encoder.setRepeat(0);
        encoder.setQuality(5);
        encoder.setFrameRate(100);
        encoder.setDelay(100);
        File bgFile = new File("data/empty200.png");
        final BufferedImage oBImage = ImageIO.read(bgFile);
        File dirtFile = new File("./data/dirt.png");
        final BufferedImage dirt = ImageIO.read(dirtFile);
        URL url = new URL(getTouUrl(3474006766L));
        final BufferedImage oImage = ImageIO.read(url);
        int r0 = 2;
        int sl = 24 * r0;
        float r = 0;
        int w = oBImage.getWidth();
        int h = oBImage.getHeight();
        for (int i = 0; i < sl; i++) {
            r += (360 * r0 / sl);
            BufferedImage main = ImageIO.read(bgFile);
            BufferedImage o1 = (BufferedImage) image2Size(oImage, 60, 60);
            o1 = roundImage(o1, 9999);
            o1 = (BufferedImage) rotateImage(o1, r);
            o1 = putImage(main, o1, (w / sl) * i, h / 2);
            o1 = putImage(o1, dirt, 0, h / 2 + 60);
            encoder.addFrame(o1);
        }
        encoder.finish();
    }

}
