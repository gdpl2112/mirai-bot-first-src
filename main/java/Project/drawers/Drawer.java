package Project.drawers;

import Entitys.gameEntitys.GInfo;
import Entitys.gameEntitys.PersonInfo;
import Entitys.gameEntitys.Warp;
import Entitys.gameEntitys.Zong;
import Project.Tools.GameTool;
import Project.Tools.Tool;
import com.google.gson.internal.LinkedHashTreeMap;
import io.github.kloping.Mirai.Main.ITools.MemberTools;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static Project.DataBases.GameDataBase.getImgById;
import static Project.DataBases.ZongMenDataBase.getZongInfo;
import static Project.DataBases.ZongMenDataBase.qq2id;
import static Project.DataBases.skill.SkillDataBase.toPercent;
import static Project.Tools.Tool.filterBigNum;

/**
 * @author github-kloping
 */
public class Drawer {
    private static final Font small1Font = new Font("宋体", Font.BOLD, 15);
    private static final Font smallFont = new Font("宋体", Font.BOLD, 28);
    private static final Font bigFont = new Font("宋体", Font.BOLD, 35);
    private static Color xpColor = fromStrToARGB("FFB9FFB9");
    private static Color hpColor = fromStrToARGB("FF5Dff5D");
    private static Color hlColor = fromStrToARGB("FFFFB946");
    private static Color hjColor = fromStrToARGB("FF8b8bFF");
    private static Color levelColor = new Color(255, 154, 87);

    private static Map<String, Image> map = new LinkedHashTreeMap<>();

    public static final ExecutorService threads = Executors.newFixedThreadPool(10);

    public static Color BORDER_COLOR = Color.BLACK;
    public static Color BACKGROUD_COLOR = Color.LIGHT_GRAY;
    private static BufferedImage INFO_BASE = null;

    static {
        BORDER_COLOR = new Color(123, 20, 135);
        BACKGROUD_COLOR = new Color(249, 79, 108);
        try {
            INFO_BASE = ImageIO.read(new File("./images/b0.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static final String drawInfoPng(PersonInfo p) {
        int width = 600;
        int height = 600 - 50;
        BufferedImage image = null;
        Graphics g = null;
        if (INFO_BASE == null) {
            image = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
            g = image.getGraphics();
            g.setClip(0, 0, width, height);
            g.setColor(BACKGROUD_COLOR);
            g.fillRect(0, 0, width, height);
        } else {
            try {
                image = (BufferedImage) JImageDrawerUtils.image2Size(INFO_BASE, width, height);
            } catch (IOException e) {
                e.printStackTrace();
            }
            g = image.getGraphics();
        }
        g.setFont(bigFont);
        Rectangle clip = g.getClipBounds();
        FontMetrics fm = g.getFontMetrics(bigFont);
        int ascent = fm.getAscent();
        int descent = fm.getDescent();
        int y = 40;
        g.setColor(BORDER_COLOR);
        g.drawString("※====☆=?==★===?====$==*=※", 10, 40);
        g.setFont(smallFont);
        //==================================
        int x = 10;
        y = 2 * 40 - 10;
        g.setColor(Color.WHITE);
        g.fillRect(x, y, width - x, 50);
        g.setColor(xpColor);
        g.fillRect(x, y, (int) (toPercent(p.getXp(), p.getXpL()) / 100.0 * width) - x, 50);
        g.setColor(Color.black);
        g.drawString(filterBigNum(String.format("经验:%s/%s", p.getXp(), p.getXpL())), x, y + smallFont.getSize());
        //==================================
        y = y + 60;
        g.setColor(Color.WHITE);
        g.fillRect(x, y, width - x, 50);
        g.setColor(hpColor);
        g.fillRect(x, y, (int) (toPercent(p.getHp(), p.getHpL()) / 100.0 * width) - x, 50);
        g.setColor(Color.black);
        g.drawString(filterBigNum(String.format("血量:%s/%s", p.getHp(), p.getHpL())), x, y + smallFont.getSize());

        //==================================
        y = y + 60;
        g.setColor(Color.WHITE);
        g.fillRect(x, y, width, 50);
        g.setColor(hlColor);
        g.fillRect(x, y, (int) (toPercent(p.getHl(), p.getHll()) / 100.0 * width), 50);
        g.setColor(Color.black);
        g.drawString(filterBigNum(String.format("魂力:%s/%s", p.getHl(), p.getHll())), x, y + smallFont.getSize());
        //==================================
        y = y + 60;
        g.setColor(Color.WHITE);
        g.fillRect(x, y, width, 50);
        g.setColor(hjColor);
        g.fillRect(x, y, (int) (toPercent(p.getHj(), p.getHjL()) / 100.0 * width), 50);
        g.setColor(Color.black);
        g.drawString(filterBigNum(String.format("精神力:%s/%s", p.getHj(), p.getHjL())), x, y + smallFont.getSize());
        //==================================
        g.setFont(small1Font);
        y = y + 60;
        g.drawImage(loadImage(getImgById(-1, false)), x, y, 50, 50, null);
        g.drawString("金魂币", x, y + small1Font.getSize());
        g.setFont(smallFont);
        g.drawString(filterBigNum(" : " + p.getGold() + " 个"), x + 60, y + smallFont.getSize());
        g.setFont(small1Font);
        //==================================
        y = y + 60;
        g.drawImage(loadImage(getImgById(-2, false)), x, y, 50, 50, null);
        g.drawString("攻击值", x, y + small1Font.getSize());
        g.setFont(smallFont);
        g.drawString(filterBigNum(" : " + p.getAtt() + "点"), x + 60, y + smallFont.getSize());
        y = y + 85;
        g.setColor(levelColor);
        g.setFont(bigFont);
        g.drawString("等级:" + p.getLevel() + "=>" + GameTool.getFH(p.getLevel().intValue()), x, y);
        /*y = y + 50;
        g.setColor(Color.BLUE);
        g.setFont(bigFont);
        g.drawString("融合状态:" + (p.getBindQ().longValue() == -1 ? "未融合" : "已融合"), x, y);
        */
        //==================================
        g.setColor(BORDER_COLOR);
        g.setFont(bigFont);
        g.drawString("※====☆=?==★===?====$==*=※", 10, height - 30);
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

    public static final String drawWarpPng(Warp p) {
        int width = 400;
        int height = 400;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
        Graphics g = image.getGraphics();
        g.setClip(0, 0, width, height);
        g.setColor(BACKGROUD_COLOR);
        g.fillRect(0, 0, width, height);
        g.setFont(bigFont);
        Rectangle clip = g.getClipBounds();
        FontMetrics fm = g.getFontMetrics(bigFont);
        int ascent = fm.getAscent();
        int descent = fm.getDescent();
        int y = (clip.height - (ascent + descent)) / 2 + ascent;
        g.setColor(BORDER_COLOR);
        g.drawString("※====☆=?==★===?====$==*=※", 10, 40);
        g.setFont(smallFont);
        //==================================
        int x = 10;
        int eve = 40;
        y = 2 * 40 - eve;
        boolean k = false;
        k = p.getBindQ().longValue() == -1;
        String m = null;
        m = k ? "无" : (" & " + MemberTools.getName(p.getBindQ().longValue()));
        g.setColor(k ? Color.RED : Color.GREEN);
        g.drawString("融合:" + m, x, y + smallFont.getSize());

        y = y + eve;
        k = p.getMaster().longValue() == -1;
        m = k ? "无" : (" & " + MemberTools.getName(p.getMaster().longValue()));
        g.setColor(k ? Color.RED : Color.GREEN);
        g.drawString("师傅:" + m, x, y + smallFont.getSize());

        y = y + eve;
        k = p.getPrentice().longValue() == -1;
        m = k ? "无" : (" & " + MemberTools.getName(p.getPrentice().longValue()));
        g.setColor(k ? Color.RED : Color.GREEN);
        g.drawString("徒弟:" + m, x, y + smallFont.getSize());

        y = y + eve;
        Integer id = qq2id.get(p.getId().longValue());
        if (id == null) {
            k = true;
            m = " 无 ";
        } else {
            k = false;
            Zong zong = getZongInfo(id);
            m = " in " + zong.getName();
        }
        g.setColor(k ? Color.RED : Color.GREEN);
        g.drawString("所处宗门:" + m, x, y + smallFont.getSize());
        //==================================
        g.setColor(BORDER_COLOR);
        g.setFont(bigFont);
        g.drawString("※====☆=?==★===?====$==*=※", 10, height - 30);
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

    public static final String drawGInfopPng(GInfo p) {
        int width = 400;
        int height = 400;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
        Graphics g = image.getGraphics();
        g.setClip(0, 0, width, height);
        g.setColor(BACKGROUD_COLOR);
        g.fillRect(0, 0, width, height);
        g.setFont(bigFont);
        Rectangle clip = g.getClipBounds();
        FontMetrics fm = g.getFontMetrics(bigFont);
        int ascent = fm.getAscent();
        int descent = fm.getDescent();
        int y = (clip.height - (ascent + descent)) / 2 + ascent;
        g.setColor(BORDER_COLOR);
        g.drawString("※=★===?====$=====☆=?==*=※", 10, 40);
        g.setFont(smallFont);
        //==================================
        int x = 10;
        int eve = 40;
        y = 2 * 40 - eve;
        String m = p.getMasterPoint() + "点";
        g.setColor(Color.BLUE);
        g.drawString("名师点: " + m, x, y + smallFont.getSize());
        //========
        y = y + eve;
        g.drawString("累计获得物品: " + p.getGotc() + " 个", x, y + smallFont.getSize());
        //========
        y = y + eve;
        g.drawString("累计使用/失去 物品: " + p.getLostc() + " 个", x, y + smallFont.getSize());
        //========
        y = y + eve;
        g.drawString("进入 活动: " + p.getJoinc() + "次", x, y + smallFont.getSize());
        //========
        y = y + eve;
        g.drawString("累计死亡次数: " + p.getDiedc() + "次", x, y + smallFont.getSize());
        //========
        y = y + eve;
        g.drawString("累计使用魂技次数: " + p.getUseskillc() + "次", x, y + smallFont.getSize());

        //==================================
        g.setColor(BORDER_COLOR);
        g.setFont(bigFont);
        g.drawString("※=?======★=====$==☆=?=*=※", 10, height - 30);
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

    public static final synchronized Image loadImage(String fileName) {
        try {
            if (map.containsKey(fileName)) return map.get(fileName);
            BufferedImage img = ImageIO.read(new File(fileName));
            map.put(fileName, img);
            return img;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(fileName + "can't read");
        }
    }

    public static Color fromStrToARGB(String str) {
        String str1 = str.substring(0, 2);
        String str2 = str.substring(2, 4);
        String str3 = str.substring(4, 6);
        String str4 = str.substring(6, 8);
        int alpha = Integer.parseInt(str1, 16);
        int red = Integer.parseInt(str2, 16);
        int green = Integer.parseInt(str3, 16);
        int blue = Integer.parseInt(str4, 16);
        Color color = new Color(red, green, blue, alpha);
        return color;
    }

    public static String createImage(String... strs) {
        int width = 500;
        int height = (strs.length + 3) * 40;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
        Graphics g = image.getGraphics();
        g.setClip(0, 0, width, height);
        g.setColor(BACKGROUD_COLOR);
        g.fillRect(0, 0, width, height);
        g.setFont(font);
        Rectangle clip = g.getClipBounds();
        FontMetrics fm = g.getFontMetrics(font);
        int ascent = fm.getAscent();
        int descent = fm.getDescent();
        int y = (clip.height - (ascent + descent)) / 2 + ascent;
        g.setColor(BORDER_COLOR);
        g.drawString("※====☆=?==★===?====$==*=※", 10, 40);
        for (int i = 0; i < strs.length; i++) {
            g.setColor(colors[Tool.rand.nextInt(colors.length)]);
            g.drawString("◎" + filterBigNum(strs[i]), 10, (i + 2) * 40);
        }
        g.setColor(BORDER_COLOR);
        g.drawString("※====☆=?==★===?====$==*=※", 10, (strs.length + 2) * 40);
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

    public static String createImage(int n, String... strs) {
        int width = 500;
        int height = (strs.length + 3) * 40;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
        Graphics g = image.getGraphics();
        g.setClip(0, 0, width, height);
        g.setColor(BACKGROUD_COLOR);
        g.fillRect(0, 0, width, height);
        g.setFont(font);
        Rectangle clip = g.getClipBounds();
        FontMetrics fm = g.getFontMetrics(font);
        int ascent = fm.getAscent();
        int descent = fm.getDescent();
        int y = (clip.height - (ascent + descent)) / 2 + ascent;
        g.setColor(BORDER_COLOR);
        g.drawString("※====☆=?==★===?====$==*=※", 10, 40);
        for (int i = 0; i < strs.length; i++) {
            g.setColor(colors[Tool.rand.nextInt(colors.length)]);
            g.drawString("◎" + filterBigNum(strs[i]), 10, (i + 2) * 40);
        }
        g.setColor(BORDER_COLOR);
        g.drawString("※====☆=?==★===?====$==*=※", 10, (strs.length + 2) * 40);
        g.dispose();
        String name = UUID.randomUUID() + ".jpg";
        new File("./temp/" + n).mkdirs();
        File file = new File("./temp/" + n + "/" + name);
        try {
            ImageIO.write(image, "png", file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file.getPath();
    }

    public static String createImage(boolean k, String... strs) {
        int width = 500;
        int height = (strs.length + 3) * 40;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
        Graphics g = image.getGraphics();
        g.setClip(0, 0, width, height);
        g.setColor(BACKGROUD_COLOR);
        g.fillRect(0, 0, width, height);
        g.setFont(font);
        Rectangle clip = g.getClipBounds();
        FontMetrics fm = g.getFontMetrics(font);
        int ascent = fm.getAscent();
        int descent = fm.getDescent();
        int y = (clip.height - (ascent + descent)) / 2 + ascent;
        g.setColor(BORDER_COLOR);
        g.drawString("※====☆=?==★===?====$==*=※", 10, 40);
        for (int i = 0; i < strs.length; i++) {
            g.setColor(colors[Tool.rand.nextInt(colors.length)]);
            if (k)
                g.drawString("◎" + filterBigNum(strs[i]), 10, (i + 2) * 40);
            else
                g.drawString("◎" + strs[i], 10, (i + 2) * 40);
        }
        g.setColor(BORDER_COLOR);
        g.drawString("※====☆=?==★===?====$==*=※", 10, (strs.length + 2) * 40);
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

    private static final Color[] colors = new Color[]{
            Color.BLUE, Color.RED, Color.cyan, Color.ORANGE,
            Color.green, Color.getHSBColor(1, 1, 1), Color.RED, Color.DARK_GRAY, Color.black};
    private static final Font font = new Font("宋体", Font.BOLD, 30);
    private static final Font font1 = new Font("宋体", Font.HANGING_BASELINE, 40);
    private static int width = 500;
    private static int height = 100;

    private static final Map<String, File> HIST_FONT_IMAGES = new ConcurrentHashMap<>();

    public static String createFont(String str) {
        if (HIST_FONT_IMAGES.containsKey(str)) {
            if (HIST_FONT_IMAGES.get(str).exists()) {
                return HIST_FONT_IMAGES.get(str).getAbsolutePath();
            }
        }
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
        Graphics g = image.getGraphics();
        g.setClip(0, 0, width, height);
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);
        g.setFont(font1);
        g.setColor(Color.RED);
        g.drawString(str, 20, height / 3 * 2);
        g.dispose();
        String name = UUID.randomUUID() + ".jpg";
        new File("./temp").mkdirs();
        File file = new File("./temp/" + name);
        try {
            ImageIO.write(image, "png", file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        HIST_FONT_IMAGES.put(str, file);
        return file.getPath();
    }

    public static String createPro(int index) {
        BufferedImage image = new BufferedImage(100, 25, BufferedImage.TYPE_INT_BGR);
        Graphics g = image.getGraphics();
        g.setClip(0, 0, 100, 25);
        g.setColor(BACKGROUD_COLOR);
        g.fillRect(0, 0, 100, 25);
        g.setColor(Color.GREEN);
        g.fillRect(0, 0, index, 25);
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

    /**
     * 过滤图片 至 大小
     *
     * @param file
     * @return
     */
    public static File FilterImg(File file) {
        try {
            BufferedImage Img = ImageIO.read(file);
            BufferedImage bi = new BufferedImage(Img.getWidth(), (int) (Img.getWidth() / 1.78f), BufferedImage.TYPE_BYTE_INDEXED);
            Graphics g = bi.getGraphics();
            g.drawImage(Img, 0, 0, Img.getWidth(), Img.getHeight(), null);
            g.dispose();
            ImageIO.write(bi, "png", file);
            return file;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String drawStringOnTwoColumns(String... ss) {
        int width = 1000;
        int le = ss.length;
        le += le % 2 == 0 ? 0 : 1;
        int height = le * 20 + 40;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
        Graphics g = image.getGraphics();
        g.setClip(0, 0, width, height);
        g.setColor(BACKGROUD_COLOR);
        g.fillRect(0, 0, width, height);
        g.setFont(font);
        Rectangle clip = g.getClipBounds();
        FontMetrics fm = g.getFontMetrics(font);
        int ascent = fm.getAscent();
        int descent = fm.getDescent();
        g.setColor(BORDER_COLOR);
        int y = (clip.height - (ascent + descent)) / 2 + ascent;
        g.drawString("※====☆=?==★===?====$==*=※===※====☆=?==★===?====$==*=※===", 15, 25);
        for (int i = 0; i < ss.length; i++) {
            if (i % 2 == 0) {
                g.setColor(colors[Tool.rand.nextInt(colors.length)]);
                g.drawString(filterBigNum(ss[i]), 15, 60 + (i) * 20);
                g.setColor(colors[Tool.rand.nextInt(colors.length)]);
                if (ss.length > i + 1)
                    g.drawString(filterBigNum(ss[i + 1]), width / 2, 60 + (i) * 20);
            }
        }
        g.dispose();

        try {
            String name = UUID.randomUUID() + ".jpg";
            new File("./temp").mkdirs();
            File file = new File("./temp/" + name);
            ImageIO.write(image, "png", file);
            return file.getPath();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getImageFromStrings(String... strings) {
        return Tool.pathToImg(createImage(strings));
    }

    public static String getImageFromStrings(int n, String... strings) {
        return Tool.pathToImg(createImage(n, strings));
    }

    public static String getImageFromStrings(boolean k, String... strings) {
        return Tool.pathToImg(createImage(false, strings));
    }

    public static String getImageFromFontString(String string) {
        return Tool.pathToImg(createFont(string));
    }

    public static String getImageFromStringsOnTwoColumns(String... strings) {
        return Tool.pathToImg(drawStringOnTwoColumns(strings));
    }


    private static final Font smallFont_40 = new Font("宋体", Font.BOLD, 40);

    public static String drawImages2Image(String... urls) {
        Image[] images = getImages(urls);
        int width = images[0].getWidth(null) * (images.length / 5);
        int height = images[0].getHeight(null) * (images.length / 4);
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
        Graphics g = image.getGraphics();
        g.setFont(smallFont_40);
        int x = 0, y = 0;
        final int[] i = {1};
        CountDownLatch countDownLatch = new CountDownLatch(images.length);
        for (Image img : images) {
            final int[] nx = {x};
            final int[] ny = {y};
            if (nx[0] + img.getWidth(null) > width) {
                ny[0] += img.getHeight(null);
                nx[0] = 0;
            }
            threads.execute(() -> {
                g.setColor(Color.RED);
                g.drawString(i[0]++ + "个", nx[0], ny[0]);
                g.drawImage(img, nx[0]++, ny[0]++, img.getWidth(null), img.getHeight(null), null);
                countDownLatch.countDown();
            });
            nx[0] += img.getWidth(null);
            x = nx[0];
            y = ny[0];
        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
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

    private static Image[] getImages(String[] urls) {
        Image[] images = new Image[urls.length];
        try {
            CountDownLatch countDownLatch = new CountDownLatch(urls.length);
            for (int i = 0; i < urls.length; i++) {
                int finalI = i;
                threads.execute(() -> {
                    images[finalI] = loadImageFromUrl(urls[finalI]);
                    countDownLatch.countDown();
                });
            }
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return images;

    }

    /**
     * 从网络流中获取Image
     *
     * @param url
     * @return
     */
    private static Image loadImageFromUrl(String url) {
        try {
            BufferedImage bufferedImage = ImageIO.read(new URL(url).openStream());
            return bufferedImage;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 从本地流获取Image
     *
     * @param path
     * @return
     */
    private static Image loadImageFromPath(String path) {
        try {
            BufferedImage bufferedImage = ImageIO.read(new FileInputStream(path));
            return bufferedImage;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据地址获得数据的字节流
     *
     * @param strUrl 网络连接地址
     * @return
     */
    public static byte[] getImageFromNetByUrl(String strUrl) {
        try {
            URL url = new URL(strUrl);
            InputStream inStream = url.openStream();
            return readInputStream(inStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 从输入流中获取数据
     *
     * @param inStream 输入流
     * @return
     * @throws Exception
     */
    public static byte[] readInputStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[10240];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        inStream.close();
        return outStream.toByteArray();
    }

    /**
     * 专属国旗
     *
     * @param endU
     * @return
     */
    public static String bundler_0(String endU) {
        Image image_ = loadImageFromUrl(endU);
        int width = image_.getWidth(null);
        int height = image_.getHeight(null);
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
        Graphics g = image.getGraphics();
        g.drawImage(image_, 0, 0, width, height, null);
        g.drawImage(loadImageFromPath("./temp/1/base.png"), 0, 0, width, height, null);
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

    public static String drawHh(String... hhs) {
        int width = 1000;
        int height = 1000;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
        Graphics g = image.getGraphics();

        g.dispose();
        try {
            String name = UUID.randomUUID() + ".jpg";
            new File("./temp").mkdirs();
            File file = new File("./temp/" + name);
            ImageIO.write(image, "png", file);
            return file.getPath();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
