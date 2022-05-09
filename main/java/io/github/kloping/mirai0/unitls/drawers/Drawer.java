package io.github.kloping.mirai0.unitls.drawers;

import Project.dataBases.skill.SkillDataBase;
import com.google.gson.internal.LinkedHashTreeMap;
import com.madgag.gif.fmsware.AnimatedGifEncoder;
import io.github.kloping.mirai0.Main.ITools.MemberTools;
import io.github.kloping.mirai0.commons.GInfo;
import io.github.kloping.mirai0.commons.PersonInfo;
import io.github.kloping.mirai0.commons.Warp;
import io.github.kloping.mirai0.commons.Zong;
import io.github.kloping.mirai0.commons.gameEntitys.SoulBone;
import io.github.kloping.mirai0.commons.gameEntitys.WinStar;
import io.github.kloping.mirai0.unitls.Tools.GameTool;
import io.github.kloping.mirai0.unitls.Tools.Tool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static Project.dataBases.SourceDataBase.getImageById;
import static Project.dataBases.SourceDataBase.getImgPathById;
import static Project.dataBases.ZongMenDataBase.getZongInfo;
import static Project.dataBases.ZongMenDataBase.qq2id;
import static io.github.kloping.mirai0.commons.gameEntitys.WinStar.LEVEL4;
import static io.github.kloping.mirai0.commons.resouce_and_tool.CommonSource.toPercent;
import static io.github.kloping.mirai0.unitls.Tools.Tool.RANDOM;
import static io.github.kloping.mirai0.unitls.Tools.Tool.filterBigNum;
import static io.github.kloping.mirai0.unitls.drawers.ImageDrawerUtils.*;

/**
 * @author github-kloping
 */
public class Drawer {
    public static final ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(10);
    private static final Font SMALL_FONT15 = new Font("宋体", Font.BOLD, 15);
    private static final Font SMALL_FONT18 = new Font("宋体", Font.BOLD, 28);
    private static final Font BIG_FONT35 = new Font("宋体", Font.BOLD, 35);
    private static final Map<String, Image> TEMP_IMAGES_MAP = new LinkedHashTreeMap<>();
    private static final Color[] COLORS = new Color[]{
            Color.BLUE, Color.RED, Color.cyan, Color.ORANGE,
            Color.green, Color.getHSBColor(1, 1, 1), Color.RED, Color.DARK_GRAY, Color.black
    };
    private static final Font FONT30 = new Font("宋体", Font.BOLD, 30);
    private static final Font FONT140 = new Font("宋体", Font.HANGING_BASELINE, 40);
    private static final Map<String, File> HIST_FONT_IMAGES = new ConcurrentHashMap<>();
    private static final Font SMALL_FONT40 = new Font("宋体", Font.BOLD, 40);
    public static Color BORDER_COLOR = Color.BLACK;
    public static Color BACKGROUND_COLOR = Color.LIGHT_GRAY;
    private static Color xpColor = fromStrGetArgb("FFB9FFB9");
    private static Color hpColor = fromStrGetArgb("FF5Dff5D");
    private static Color hlColor = fromStrGetArgb("FFFFB946");
    private static Color hjColor = fromStrGetArgb("FF8b8bFF");
    private static Color levelColor = new Color(255, 0, 136);
    private static Color shieldColor = new Color(239, 239, 239, 200);
    private static BufferedImage INFO_BASE = null;

    static {
        BORDER_COLOR = new Color(123, 20, 135);
//        BACKGROUD_COLOR = new Color(249, 79, 108);
//        try {
//            INFO_BASE = ImageIO.read(new File("./images/b0.png"));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    public static File saveTempImage(BufferedImage image) {
        String name = UUID.randomUUID() + ".png";
        new File("./temp").mkdirs();
        File file = new File("./temp/" + name);
        try {
            ImageIO.write(image, "png", file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    public static final String drawInfo(PersonInfo p) {
        int width = 600;
        int height = 600 - 50;
        BufferedImage image = null;
        Graphics g = null;
        if (INFO_BASE == null) {
            image = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
            g = image.getGraphics();
            g.setClip(0, 0, width, height);
            g.setColor(BACKGROUND_COLOR);
            g.fillRect(0, 0, width, height);
        } else {
            try {
                image = (BufferedImage) image2Size(INFO_BASE, width, height);
            } catch (IOException e) {
                e.printStackTrace();
            }
            g = image.getGraphics();
        }
        g.setFont(BIG_FONT35);
        Rectangle clip = g.getClipBounds();
        FontMetrics fm = g.getFontMetrics(BIG_FONT35);
        int ascent = fm.getAscent();
        int descent = fm.getDescent();
        int y = 40;
        g.setColor(BORDER_COLOR);
        g.drawString("※====☆=?==★===?====$==*=※", 10, 40);
        g.setFont(SMALL_FONT18);
        //==================================
        int x = 10;
        y = 2 * 40 - 10;
        g.setColor(Color.WHITE);
        g.fillRect(x, y, width - x, 50);
        g.setColor(xpColor);
        g.fillRect(x, y, (int) (toPercent(p.getXp(), p.getXpL()) / 100.0 * width) - x, 50);
        g.setColor(Color.black);
        g.drawString(filterBigNum(String.format("经验:%s/%s", p.getXp(), p.getXpL())), x, y + SMALL_FONT18.getSize());
        //==================================
        y = y + 60;
        g.setColor(Color.WHITE);
        g.fillRect(x, y, width - x, 50);
        g.setColor(hpColor);
        g.fillRect(x, y, (int) (toPercent(p.getHp(), p.getHpL()) / 100.0 * width) - x, 50);
        if (p.containsTag(SkillDataBase.TAG_SHIELD)) {
            Number v0 = p.getTagValue(SkillDataBase.TAG_SHIELD);
            g.setColor(shieldColor);
            g.fillRect(x, y, (int) (toPercent(v0.longValue(), p.getHpL()) / 100.0 * width) - x, 50);
        }
        g.setColor(Color.black);
        g.drawString(filterBigNum(String.format("血量:%s/%s", p.getHp(), p.getHpL())), x, y + SMALL_FONT18.getSize());
        //==================================
        y = y + 60;
        g.setColor(Color.WHITE);
        g.fillRect(x, y, width - x, 50);
        g.setColor(hlColor);
        g.fillRect(x, y, (int) (toPercent(p.getHl(), p.getHll()) / 100.0 * width) - x, 50);
        g.setColor(Color.black);
        g.drawString(filterBigNum(String.format("魂力:%s/%s", p.getHl(), p.getHll())), x, y + SMALL_FONT18.getSize());
        //==================================
        y = y + 60;
        g.setColor(Color.WHITE);
        g.fillRect(x, y, width - x, 50);
        g.setColor(hjColor);
        g.fillRect(x, y, (int) (toPercent(p.getHj(), p.getHjL()) / 100.0 * width) - x, 50);
        g.setColor(Color.black);
        g.drawString(filterBigNum(String.format("精神力:%s/%s", p.getHj(), p.getHjL())), x, y + SMALL_FONT18.getSize());
        //==================================
        g.setFont(SMALL_FONT15);
        y = y + 60;
        g.drawImage(loadImage(getImgPathById(2001, false)), x, y, 50, 50, null);
        g.drawString("金魂币", x, y + SMALL_FONT15.getSize());
        g.setFont(SMALL_FONT18);
        g.drawString(filterBigNum(" : " + p.getGold() + " 个"), x + 60, y + SMALL_FONT18.getSize());
        g.setFont(SMALL_FONT15);
        //==================================
        y = y + 60;
        g.drawImage(loadImage(getImgPathById(2002, false)), x, y, 50, 50, null);
        g.drawString("攻击值", x, y + SMALL_FONT15.getSize());
        g.setFont(SMALL_FONT18);
        g.drawString(filterBigNum(" : " + p.att() + "点"), x + 60, y + SMALL_FONT18.getSize());
        y = y + 85;
        g.setColor(levelColor);
        g.setFont(BIG_FONT35);
        g.drawString("等级:" + p.getLevel() + "=>" + GameTool.getFH(p.getLevel().intValue()), x, y);
        drawStar(g, new WinStar(p.getWinC()));
        //==================================
        g.setColor(BORDER_COLOR);
        g.setFont(BIG_FONT35);
        g.drawString("※====☆=?==★===?====$==*=※", 10, height - 30);
        g.dispose();
        return saveTempImage(image).getPath();
    }

    private static void drawStar(Graphics g, WinStar winStar) {
        int x = 350;
        AtomicInteger y = new AtomicInteger(350);
        winStar.flushMap();
        winStar.getStarMap().forEach((k, v) -> {
            String s0 = "";
            if (k == LEVEL4) {
                s0 = "☆x" + v;
            } else {
                for (Integer integer = 0; integer < v; integer++) {
                    s0 += "☆";
                }
            }
            g.setColor(k);
            g.drawString(s0, x, y.get());
            y.addAndGet(40);
        });
    }

    public static final String drawWarp(Warp p) {
        int width = 400;
        int height = 400;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
        Graphics g = image.getGraphics();
        g.setClip(0, 0, width, height);
        g.setColor(BACKGROUND_COLOR);
        g.fillRect(0, 0, width, height);
        g.setFont(BIG_FONT35);
        Rectangle clip = g.getClipBounds();
        FontMetrics fm = g.getFontMetrics(BIG_FONT35);
        int ascent = fm.getAscent();
        int descent = fm.getDescent();
        int y = (clip.height - (ascent + descent)) / 2 + ascent;
        g.setColor(BORDER_COLOR);
        g.drawString("※====☆=?==★===?====$==*=※", 10, 40);
        g.setFont(SMALL_FONT18);
        //==================================
        int x = 10;
        int eve = 40;
        y = 2 * 40 - eve;
        boolean k = false;
        k = p.getBindQ().longValue() == -1;
        String m = null;
        m = k ? "无" : (" & " + MemberTools.getName(p.getBindQ().longValue()));
        g.setColor(k ? Color.RED : Color.GREEN);
        g.drawString("融合:" + m, x, y + SMALL_FONT18.getSize());

        y = y + eve;
        k = p.getMaster().longValue() == -1;
        m = k ? "无" : (" & " + MemberTools.getName(p.getMaster().longValue()));
        g.setColor(k ? Color.RED : Color.GREEN);
        g.drawString("师傅:" + m, x, y + SMALL_FONT18.getSize());

        y = y + eve;
        k = p.getPrentice().longValue() == -1;
        m = k ? "无" : (" & " + MemberTools.getName(p.getPrentice().longValue()));
        g.setColor(k ? Color.RED : Color.GREEN);
        g.drawString("徒弟:" + m, x, y + SMALL_FONT18.getSize());

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
        g.drawString("所处宗门:" + m, x, y + SMALL_FONT18.getSize());
        //==================================
        g.setColor(BORDER_COLOR);
        g.setFont(BIG_FONT35);
        g.drawString("※====☆=?==★===?====$==*=※", 10, height - 30);
        g.dispose();
        return saveTempImage(image).getPath();
    }

    public static final String drawGInfo(GInfo p) {
        return drawGInfoWith(p, "");
    }

    public static final String drawGInfoWith(GInfo p, String tips) {
        int width = 400;
        int height = 400;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
        Graphics g = image.getGraphics();
        g.setClip(0, 0, width, height);
        g.setColor(BACKGROUND_COLOR);
        g.fillRect(0, 0, width, height);
        g.setFont(BIG_FONT35);
        Rectangle clip = g.getClipBounds();
        FontMetrics fm = g.getFontMetrics(BIG_FONT35);
        int ascent = fm.getAscent();
        int descent = fm.getDescent();
        int y = (clip.height - (ascent + descent)) / 2 + ascent;
        g.setColor(BORDER_COLOR);
        g.drawString("※=★===?====$=====☆=?==*=※", 10, 40);
        g.setFont(SMALL_FONT18);
        //==================================
        int x = 10;
        int eve = 40;
        y = 2 * 40 - eve;
        String m = p.getMasterPoint() + "点";
        g.setColor(Color.BLUE);
        g.drawString("名师点: " + m, x, y + SMALL_FONT18.getSize());
        //========
        y = y + eve;
        g.drawString("累计获得物品: " + p.getGotc() + " 个", x, y + SMALL_FONT18.getSize());
        //========
        y = y + eve;
        g.drawString("累计使用/失去 物品: " + p.getLostc() + " 个", x, y + SMALL_FONT18.getSize());
        //========
        y = y + eve;
        g.drawString("进入 活动: " + p.getJoinc() + "次", x, y + SMALL_FONT18.getSize());
        //========
        y = y + eve;
        g.drawString("累计死亡次数: " + p.getDiedc() + "次", x, y + SMALL_FONT18.getSize());
        //========
        y = y + eve;
        g.drawString("累计使用魂技次数: " + p.getUseskillc() + "次", x, y + SMALL_FONT18.getSize());
        if (tips != null && !tips.isEmpty()) {
            y = y + eve;
            g.drawString(tips, x, y + SMALL_FONT18.getSize());
        }
        //==================================
        g.setColor(BORDER_COLOR);
        g.setFont(BIG_FONT35);
        g.drawString("※=?======★=====$==☆=?=*=※", 10, height - 30);
        g.dispose();
        return saveTempImage(image).getPath();
    }

    public static final String drawBoneMap(List<SoulBone> bones) {
        int width = 400;
        int height = 400;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
        Graphics g = image.getGraphics();
        g.setClip(0, 0, width, height);
        g.setColor(BACKGROUND_COLOR);
        g.fillRect(0, 0, width, height);
        g.setFont(BIG_FONT35);
        Rectangle clip = g.getClipBounds();
        FontMetrics fm = g.getFontMetrics(BIG_FONT35);
        int ascent = fm.getAscent();
        int descent = fm.getDescent();
        int y = (clip.height - (ascent + descent)) / 2 + ascent;
        g.dispose();
        for (SoulBone bone : bones) {
            try {
                int id0 = bone.partId();
                Image i0 = loadImage(getImgPathById(bone.getOid(), false));
                i0 = ImageDrawerUtils.image2Size((BufferedImage) i0, 100, 100);
                int[] xy = getBoneXY(bone.partId());
                if (xy != null)
                    image = putImage(image, (BufferedImage) i0, xy[0], xy[1]);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return saveTempImage(image).getPath();
    }

    private static int[] getBoneXY(int id) {
        switch (id) {
            case 151:
                return new int[]{150, 0};
            case 152:
                return new int[]{50, 130};
            case 153:
                return new int[]{250, 130};
            case 154:
                return new int[]{80, 260};
            case 155:
                return new int[]{220, 260};
            default:
                return null;
        }
    }

    public static synchronized Image loadImage(String fileName) {
        try {
            if (TEMP_IMAGES_MAP.containsKey(fileName)) return TEMP_IMAGES_MAP.get(fileName);
            BufferedImage img = ImageIO.read(new File(fileName));
            TEMP_IMAGES_MAP.put(fileName, img);
            return img;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(fileName + "can't read");
        }
    }

    public static Color fromStrGetArgb(String str) {
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

    public static String createImage(String... sss) {
        int width = 500;
        int height = (sss.length + 3) * 40;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
        Graphics g = image.getGraphics();
        g.setClip(0, 0, width, height);
        g.setColor(BACKGROUND_COLOR);
        g.fillRect(0, 0, width, height);
        g.setFont(FONT30);
        Rectangle clip = g.getClipBounds();
        FontMetrics fm = g.getFontMetrics(FONT30);
        int ascent = fm.getAscent();
        int descent = fm.getDescent();
        int y = (clip.height - (ascent + descent)) / 2 + ascent;
        g.setColor(BORDER_COLOR);
        g.drawString("※====☆=?==★===?====$==*=※", 10, 40);
        for (int i = 0; i < sss.length; i++) {
            g.setColor(COLORS[Tool.RANDOM.nextInt(COLORS.length)]);
            g.drawString("◎" + filterBigNum(sss[i]), 10, (i + 2) * 40);
        }
        g.setColor(BORDER_COLOR);
        g.drawString("※====☆=?==★===?====$==*=※", 10, (sss.length + 2) * 40);
        g.dispose();
        return saveTempImage(image).getPath();
    }

    /**
     * 创建以 sss 为行数的图片
     *
     * @param k   过滤大数字
     * @param sss
     * @return
     */
    public static String createImage(boolean k, String... sss) {
        int width = 500;
        int height = (sss.length + 3) * 40;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
        Graphics g = image.getGraphics();
        g.setClip(0, 0, width, height);
        g.setColor(BACKGROUND_COLOR);
        g.fillRect(0, 0, width, height);
        g.setFont(FONT30);
        Rectangle clip = g.getClipBounds();
        FontMetrics fm = g.getFontMetrics(FONT30);
        int ascent = fm.getAscent();
        int descent = fm.getDescent();
        int y = (clip.height - (ascent + descent)) / 2 + ascent;
        g.setColor(BORDER_COLOR);
        g.drawString("※====☆=?==★===?====$==*=※", 10, 40);
        for (int i = 0; i < sss.length; i++) {
            g.setColor(COLORS[Tool.RANDOM.nextInt(COLORS.length)]);
            if (k) {
                g.drawString("◎" + filterBigNum(sss[i]), 10, (i + 2) * 40);
            } else {
                g.drawString("◎" + sss[i], 10, (i + 2) * 40);
            }
        }
        g.setColor(BORDER_COLOR);
        g.drawString("※====☆=?==★===?====$==*=※", 10, (sss.length + 2) * 40);
        g.dispose();
        return saveTempImage(image).getPath();
    }

    public static String createFont(String str) {
        int width = 500;
        int height = 100;
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
        g.setFont(FONT140);
        g.setColor(Color.RED);
        g.drawString(str, 20, height / 3 * 2);
        g.dispose();
        String name = UUID.randomUUID() + ".png";
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

    /**
     * 过滤图片 至 大小
     *
     * @param file
     * @return
     */
    public static File filterImg(File file) {
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
        g.setColor(BACKGROUND_COLOR);
        g.fillRect(0, 0, width, height);
        g.setFont(FONT30);
        Rectangle clip = g.getClipBounds();
        FontMetrics fm = g.getFontMetrics(FONT30);
        int ascent = fm.getAscent();
        int descent = fm.getDescent();
        g.setColor(BORDER_COLOR);
        int y = (clip.height - (ascent + descent)) / 2 + ascent;
        g.drawString("※====☆=?==★===?====$==*=※===※====☆=?==★===?====$==*=※===", 15, 25);
        for (int i = 0; i < ss.length; i++) {
            if (i % 2 == 0) {
                g.setColor(COLORS[Tool.RANDOM.nextInt(COLORS.length)]);
                g.drawString(filterBigNum(ss[i]), 15, 60 + (i) * 20);
                g.setColor(COLORS[Tool.RANDOM.nextInt(COLORS.length)]);
                if (ss.length > i + 1)
                    g.drawString(filterBigNum(ss[i + 1]), width / 2, 60 + (i) * 20);
            }
        }
        g.dispose();
        try {
            String name = UUID.randomUUID() + ".png";
            new File("./temp").mkdirs();
            File file = new File("./temp/" + name);
            ImageIO.write(image, "png", file);
            return file.getPath();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getImageFromStrings(String... sss) {
        return Tool.pathToImg(createImage(sss));
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

    public static String drawHh(Integer mid, List<Integer> ids) throws Exception {
        File temp = new File("temp/", UUID.randomUUID() + ".gif");
        AnimatedGifEncoder encoder = new AnimatedGifEncoder();
        encoder.start(temp.getAbsolutePath());
        encoder.setRepeat(0);
        encoder.setQuality(5);
        encoder.setFrameRate(150);
        int base = 100;
        int width = 5 * base;
        int hs = 1;
        if (ids.size() > 5) {
            hs++;
        }
        if (ids.size() > 10) {
            hs++;
        }
        int height = hs * base;
        Map<Integer, Integer> st2tr = new HashMap<>();
        for (int i = 0; i < 9; i++) {
            BufferedImage bg = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
            Graphics g = bg.getGraphics();
            g.setClip(0, 0, width, height);
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, width, height);
            g.dispose();
            int xi = 0, yi = 0;
            for (int i1 = 0; i1 < ids.size(); i1++) {
                BufferedImage hi = (BufferedImage) getImageById(ids.get(i1));

                int w = base;
                int h = w;

                hi = (BufferedImage) image2Size(hi, w, h);


                int r0 = 0;
                if (st2tr.containsKey(i1)) {
                    r0 = st2tr.get(i1);
                } else {
                    r0 = RANDOM.nextInt(20) - 10;
                    st2tr.put(i1, r0);
                }
                if (r0 > 0) {
                    r0 += 10;
                } else {
                    r0 -= 10;
                }
                st2tr.put(i1, r0);
                hi = (BufferedImage) rotateImage(hi, r0);
                bg = putImage(bg, hi, base * xi, base * yi);

                xi++;
                if (xi == 5) {
                    xi = 0;
                    yi++;
                }
            }
            encoder.addFrame(bg);
        }
        encoder.finish();
        return temp.getAbsolutePath();
    }
}
