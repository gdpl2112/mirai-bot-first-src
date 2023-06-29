package io.github.kloping.gb.drawers;

import com.madgag.gif.fmsware.AnimatedGifEncoder;
import com.madgag.gif.fmsware.GifDecoder;
import io.github.kloping.gb.ImageManager;
import io.github.kloping.gb.Utils;
import io.github.kloping.gb.finals.FinalStrings;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.UUID;


/**
 * @author github-kloping
 */
public class ImageDrawer {

    public static final int SIZE = 12;
    private static final Font FONT0 = new Font("宋体", Font.BOLD, SIZE);

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
        if (oImage.getHeight() - oImage.getWidth() > 30)
            throw new RuntimeException(FinalStrings.NOT_SUPPORT_LENGTH_IMG);
        GifDecoder decoder = new GifDecoder();
        decoder.read(oFile.openStream());
        final int max = decoder.getFrameCount();
        for (int i = 0; i < files.length; i++) {
            encoder.setDelay(100);
            BufferedImage main = ImageIO.read(files[i]);
            float rotate = rotateEve * i;
            BufferedImage image = max == 0 ? oImage : decoder.getFrame(i >= max ? i - max : i);
            image = (BufferedImage) ImageDrawerUtils.image2Size(image, 200, 200);
            image = (BufferedImage) ImageDrawerUtils.rotateImage(image, rotate);
            image = ImageDrawerUtils.roundImage(image, 9999);
            image = ImageDrawerUtils.putImage(main, image, 95, 80);
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
        if (oImage.getHeight() - oImage.getWidth() > 30)
            throw new RuntimeException(FinalStrings.NOT_SUPPORT_LENGTH_IMG);
        oImage = ImageDrawerUtils.roundImage(oImage, 9999);
        oImage = (BufferedImage) ImageDrawerUtils.image2Size(oImage, 150, 150);
        oImage = (BufferedImage) ImageDrawerUtils.rotateImage(oImage, Utils.RANDOM.nextInt(160) + 60);
        BufferedImage bgImage = ImageIO.read(file);
        bgImage = (BufferedImage) ImageDrawerUtils.image2Size(bgImage, 512, 512);
        BufferedImage image = ImageDrawerUtils.putImage(bgImage, oImage, 10, 175);
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
        if (oImage.getHeight() - oImage.getWidth() > 30)
            throw new RuntimeException(FinalStrings.NOT_SUPPORT_LENGTH_IMG);
        GifDecoder decoder = new GifDecoder();
        decoder.read(oFile.openStream());
        final int max = decoder.getFrameCount();
        int rotateEve = 360 / files.length;
        for (int i = 0; i < files.length; i++) {
            encoder.setDelay(100);
            BufferedImage main = ImageIO.read(files[i]);
            BufferedImage image = max == 0 ? oImage : decoder.getFrame(i >= max ? i - max : i);
            float rotate = rotateEve * i;
            image = (BufferedImage) ImageDrawerUtils.image2Size(image, 132, 132);
            image = (BufferedImage) ImageDrawerUtils.rotateImage(image, rotate);
            image = ImageDrawerUtils.roundImage(image, 9999);
            int[] vs = getWt(i);
            image = ImageDrawerUtils.putImage(main, image, vs[0], vs[1]);
            encoder.addFrame(image);
        }
        encoder.finish();
        return outFile;
    }

    public static File getZan(File file, URL oFile, File outFile) throws Exception {
        BufferedImage oImage = ImageIO.read(oFile);
        oImage = (BufferedImage) ImageDrawerUtils.image2Size(oImage, 138, 138);
        oImage = ImageDrawerUtils.roundImage(oImage, 9999);
        BufferedImage bI = ImageIO.read(file);
        bI = ImageDrawerUtils.putImage(bI, oImage, 4, 242);
        ImageIO.write(bI, "png", outFile);
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
            BufferedImage o1 = (BufferedImage) ImageDrawerUtils.image2Size(oImage, 60, 60);
            o1 = ImageDrawerUtils.roundImage(o1, 9999);
            o1 = (BufferedImage) ImageDrawerUtils.rotateImage(o1, r);
            o1 = ImageDrawerUtils.putImage(main, o1, (w / sl) * i, h / 2);
            o1 = ImageDrawerUtils.putImage(o1, dirt, 0, h / 2 + 60);
            encoder.addFrame(o1);
        }
        encoder.finish();
        return outFile;
    }

    public static File getPixelWordImage(URL url, File outFile, String word) throws IOException {
        int JRX = SIZE * word.length();
        int JRY = SIZE;
        BufferedImage image = ImageIO.read(url);
        int wh0 = image.getHeight() > image.getWidth() ? image.getWidth() : image.getHeight();
        int r0 = 2000 / wh0;
        image = (BufferedImage) ImageDrawerUtils.image2Size(image, image.getWidth() * r0, image.getHeight() * r0);
        BufferedImage imageO;
        imageO = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_BGR);
        int width = image.getWidth();
        int height = image.getHeight();
        Graphics g = imageO.getGraphics();
        g.setClip(0, 0, width, height);
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);
        g.setFont(FONT0);
        int jrx = 0;
        int jry = 0;
        for (int x = 0; x < image.getWidth(); x++) {
            if (jrx++ % JRX != 0) continue;
            for (int y = 0; y < image.getHeight(); y++) {
                if (jry++ % JRY != 0) continue;
                int rgb = image.getRGB(x, y);
                Color color = new Color(rgb);
                g.setColor(color);
                g.drawString(word, x, y);
            }
        }
        g.dispose();
        ImageIO.write(imageO, "jpg", outFile);
        return outFile;
    }

    public static final Font BIG_FONT = new Font("宋体", Font.BOLD, 150);
    public static final String[] DB0 = {"端午快乐！", "端午安康!", "粽子好吃.你好看"};

    public static String dragonBot(long qid, int oid, int num) throws Exception {
        String es = Utils.getRandString(DB0);
        BufferedImage oi = (BufferedImage) ImageManager.getImageById(oid);
        oi = (BufferedImage) ImageDrawerUtils.image2Size(oi, 300, 300);
        File of = new File("images/dragon_boat.gif");
        GifDecoder decoder = new GifDecoder();
        decoder.read(new FileInputStream(of));

        File outFile = new File("./temp/" + UUID.randomUUID() + ".gif");

        AnimatedGifEncoder encoder = new AnimatedGifEncoder();
        encoder.start(outFile.getAbsolutePath());
        encoder.setRepeat(0);
        encoder.setQuality(5);
        encoder.setFrameRate(180);
        final BufferedImage icon0 = ImageIO.read(new URL(Utils.getTouUrl(qid)));
        final BufferedImage icon1 = (BufferedImage) ImageDrawerUtils.image2Size(icon0, 450, 450);
        final BufferedImage icon = ImageDrawerUtils.roundImage(icon1, 999);
        for (int i = 0; i < decoder.getFrameCount(); i++) {
            BufferedImage f = decoder.getFrame(i);
            f = ImageDrawerUtils.putImage(f, icon, 750, 350);
            f = ImageDrawerUtils.putImage(f, oi, 100, 100);
            Graphics graphics = f.getGraphics();

            graphics.setFont(BIG_FONT);
            graphics.setColor(Color.BLACK);
            graphics.drawString("+" + num, 450, 300);

            graphics.setFont(BIG_FONT);
            graphics.setColor(new Color(142, 217, 180, 191));
            graphics.fillRoundRect(300, 1700, 1400, 280, 50, 50);
            graphics.setColor(new Color(208, 118, 1, 235));
            graphics.drawString(es, 500, 1900);

            graphics.dispose();
            encoder.addFrame(f);

        }
        encoder.finish();
        return outFile.getAbsolutePath();
    }
}
