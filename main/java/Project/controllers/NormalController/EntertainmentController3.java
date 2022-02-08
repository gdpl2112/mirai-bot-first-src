package Project.controllers.NormalController;

import Project.broadcast.PicBroadcast;
import Project.detailPlugin.BaiduShituDetail;
import Project.interfaces.IBaiduShitu;
import Project.interfaces.Qxu66;
import Project.interfaces.WeiJieYue;
import io.github.kloping.MySpringTool.annotations.*;
import io.github.kloping.MySpringTool.exceptions.NoRunException;
import io.github.kloping.mirai0.Entitys.Group;
import io.github.kloping.mirai0.Entitys.User;
import io.github.kloping.mirai0.Entitys.apiEntitys.baiduShitu.BaiduShitu;
import io.github.kloping.mirai0.Entitys.apiEntitys.baiduShitu.response.BaiduShituResponse;
import io.github.kloping.mirai0.Main.ITools.MessageTools;
import io.github.kloping.mirai0.unitls.Tools.Tool;
import io.github.kloping.mirai0.unitls.drawers.GameDrawer;
import io.github.kloping.mirai0.unitls.drawers.ImageDrawer;
import io.github.kloping.mirai0.unitls.drawers.entity.GameMap;
import net.mamoe.mirai.message.data.ForwardMessageBuilder;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.SimpleServiceMessage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.CountDownLatch;

import static Project.ResourceSet.FinalString.*;
import static Project.controllers.ControllerTool.opened;
import static Project.detailPlugin.All.getTitle;
import static io.github.kloping.mirai0.Main.Resource.*;

/**
 * @author github-kloping
 */
@Controller
public class EntertainmentController3 {
    public EntertainmentController3() {
        println(this.getClass().getSimpleName() + "构建");
    }

    @Before
    public void before(Group group) throws NoRunException {
        if (!opened(group.getId(), this.getClass())) {
            throw new NoRunException("未开启");
        }
    }

    @Action("/testMap<.+=>str>")
    public String t1(@Param("str") String str, User user) throws IOException {
        String[] ss = str.split("-");
        Map<String, Integer> maps = new HashMap<>();
        for (String s : ss) {
            if (s.trim().isEmpty() || !s.contains("=")) {
                continue;
            }
            try {
                String[] s2 = s.split("=");
                maps.put(s2[0], Integer.valueOf(s2[1]));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        int w = maps.containsKey("w") ? maps.get("w") : 10;
        int h = maps.containsKey("h") ? maps.get("h") : 6;
        int x = maps.containsKey("x") ? maps.get("x") : 1;
        int y = maps.containsKey("y") ? maps.get("y") : 1;
        GameMap.GameMapBuilder builder = new GameMap.GameMapBuilder()
                .setWidth(w)
                .setHeight(h)
                .append(x, y, "https://q1.qlogo.cn/g?b=qq&nk=" + user.getId() + "&s=640");
        return Tool.pathToImg(GameDrawer.drawerMap(builder.build()));
    }

    @AutoStand
    WeiJieYue weiJieYue;

    @AutoStand
    Qxu66 qxu66;

    @Action("/爬.+")
    public Object o1(@AllMess String m, Group group, long q1) {
        long q = MessageTools.getAtFromString(m);
        String urlStr = null;
        if (q == -1) {
            return "目前只支@的形式";
        }
        byte[] bytes = weiJieYue.paImg(q);
        MessageTools.sendImageByBytesOnGroupWithAt(bytes, group.getId(), q1);
        return null;
    }

    @Action("/赞.+")
    public Object o3(@AllMess String m, Group group, long q1) {
        long q = MessageTools.getAtFromString(m);
        String urlStr = null;
        if (q == -1) {
            return "目前只支@的形式";
        }
        byte[] bytes = weiJieYue.zan(q);
        MessageTools.sendImageByBytesOnGroupWithAt(bytes, group.getId(), q1);
        return null;
    }

    @Action("/举牌子.+")
    public Object o2(@AllMess String m, Group group, long q1) {
        String msg = m.replace("/举牌子", "");
        if (msg == null || msg.trim().isEmpty()) {
            msg = "请指定内容哦~";
        }
        byte[] bytes = qxu66.jupaizi(msg);
        MessageTools.sendImageByBytesOnGroupWithAt(bytes, group.getId(), q1);
        return null;
    }

    public static File[] filesTui = new File("./images/tui").listFiles();
    public static File[] filesWq = new File("./images/wq").listFiles();
    public static File fileDiu = new File("./images/diu/diu.png");

    static {
        Arrays.sort(filesWq);
        Arrays.sort(filesTui);
    }

    @Action("/推.*")
    public String m1(@AllMess String m) {
        long q = MessageTools.getAtFromString(m);
        String urlStr = null;
        if (q == -1) {
            urlStr = MessageTools.getImageUrlFromMessageString(m);
            if (urlStr == null) {
                return "目前只支@的形式、或携带图片";
            }
        } else {
            urlStr = Tool.getTouUrl(q);
        }
        try {
            URL u = new URL(urlStr);
            File outFile = new File("./temp/" + UUID.randomUUID() + "-tui.png");
            outFile = ImageDrawer.getTuiGift(filesTui, u, outFile);
            return Tool.pathToImg(outFile.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
            return "error:for\n" + e.getMessage();
        }
    }

    @Action("/玩球.*")
    public String m2(@AllMess String m) {
        long q = MessageTools.getAtFromString(m);
        String urlStr = null;
        if (q == -1) {
            urlStr = MessageTools.getImageUrlFromMessageString(m);
            if (urlStr == null) {
                return "目前只支@的形式、或携带图片";
            }
        } else {
            urlStr = Tool.getTouUrl(q);
        }
        try {
            URL u = new URL(urlStr);
            File outFile = new File("./temp/" + UUID.randomUUID() + "-wq.png");
            outFile = ImageDrawer.getWq(filesWq, u, outFile);
            return Tool.pathToImg(outFile.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
            return "error:for\n" + e.getMessage();
        }
    }

    @Action("/丢.*")
    public String m3(@AllMess String m) {
        long q = MessageTools.getAtFromString(m);
        String urlStr = null;
        if (q == -1) {
            urlStr = MessageTools.getImageUrlFromMessageString(m);
            if (urlStr == null) {
                return "目前只支@的形式、或携带图片";
            }
        } else {
            urlStr = Tool.getTouUrl(q);
        }
        try {
            URL u = new URL(urlStr);
            File outFile = new File("./temp/" + UUID.randomUUID() + "-wq.png");
            outFile = ImageDrawer.getDui(fileDiu, u, outFile);
            return Tool.pathToImg(outFile.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
            return "error:for\n" + e.getMessage();
        }
    }

    public static final File EMPTY_FILE = new File("./images/gunc/empty200.png");
    public static final File DIRT_FILE = new File("./images/gunc/dirt.png");

    @Action("/滚草.*")
    public String gunC(@AllMess String m) {
        long q = MessageTools.getAtFromString(m);
        String urlStr = null;
        if (q == -1) {
            urlStr = MessageTools.getImageUrlFromMessageString(m);
            if (urlStr == null) {
                return "目前只支@的形式、或携带图片";
            }
        } else {
            urlStr = Tool.getTouUrl(q);
        }
        try {
            URL u = new URL(urlStr);
            File outFile = new File("./temp/" + UUID.randomUUID() + "-gc.png");
            outFile = ImageDrawer.getGunOnDirt(EMPTY_FILE, u, DIRT_FILE, 2, outFile);
            return Tool.pathToImg(outFile.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
            return "error:for\n" + e.getMessage();
        }
    }

    private static final String XML_STR0 = "<?xml version='1.0' encoding='UTF-8' standalone='yes' ?>\n" +
            "<msg serviceID=\"5\" templateID=\"1\" action=\"\" brief=\"[超级大图片表情]\" sourceMsgId=\"0\" url=\"\" flag=\"0\" adverSign=\"0\"\n" +
            "     multiMsgFlag=\"0\">\n" +
            "    <item layout=\"0\" advertiser_id=\"0\" aid=\"0\">\n" +
            "        <image uuid=\"%s\" md5=\"%s\"\n" +
            "               minWidth=\"%s\" minHeight=\"%s\" maxWidth=\"%s\" maxHeight=\"%s\"/>\n" +
            "    </item>\n" +
            "    <source name=\"\" icon=\"\" action=\"\" appid=\"-1\"/>\n" +
            "</msg>";

    private static final String COM_PRE = "-";

    @Action("变大.*+")
    public void m0(@AllMess String mess, Group group, long qId) {
        int size0 = 1200;
        if (mess.contains(COM_PRE)) {
            String[] ss = mess.split("-");
            Map<String, Integer> maps = new HashMap<>();
            for (String s : ss) {
                if (s.trim().isEmpty() || !s.contains("=")) {
                    continue;
                }
                try {
                    String[] s2 = s.split("=");
                    maps.put(s2[0], Integer.valueOf(s2[1]));
                } catch (NumberFormatException e) {
                }
            }
            size0 = maps.containsKey("size") ? maps.get("size") : size0;
            size0 = size0 >= 2000 ? 2000 : size0;
        }
        MessageTools.sendMessageInGroup("请在发送要变大的图片", group.getId());
        int size = size0;
        PicBroadcast.INSTANCE.add(new PicBroadcast.PicReceiverOnce() {
            @Override
            public Object onReceive(long qid, long gid, String pic, Object[] objects) {
                if (qId == qid) {
                    int i = pic.indexOf("{");
                    int i2 = pic.indexOf("}");
                    String md5 = pic.substring(i + 1, i2);
                    md5 = md5.replaceAll("-", "");
                    String suffix = pic.substring(i2 + 1, i2 + 5);
                    String xmlStr = XML_STR0;
                    xmlStr = String.format(xmlStr, md5 + suffix, md5, size, size, size, size);
                    SimpleServiceMessage simpleServiceMessage = new SimpleServiceMessage(5, xmlStr);
                    MessageTools.sendMessageInGroup(simpleServiceMessage, group.getId());
                    return "ok";
                }
                return null;
            }
        });
    }

    @Action("/搜图.+")
    public Object searchPic(@AllMess String mess, Group group) throws InterruptedException {
        net.mamoe.mirai.contact.Group g = bot.getGroup(group.getId());
        long q = MessageTools.getAtFromString(mess);
        String urlStr = null;
        if (q == -1) {
            urlStr = MessageTools.getImageUrlFromMessageString(mess);
            if (urlStr == null) {
                return "目前只支@的形式、或携带图片";
            }
        } else {
            urlStr = Tool.getTouUrl(q);
            urlStr = Image.queryUrl(MessageTools.createImage(g, urlStr));
        }
        BaiduShitu baiduShitu = BaiduShituDetail.get(urlStr);
        BaiduShituResponse response = iBaiduShitu.response(baiduShitu.getData().getSign());
        Iterator<io.github.kloping.mirai0.Entitys.apiEntitys.baiduShitu.response.List> iterator = Arrays.asList(response.getData().getList()).iterator();
        ForwardMessageBuilder builder = new ForwardMessageBuilder(bot.getGroup(group.getId()));
        while (iterator.hasNext() && builder.size() <= 8) {
            io.github.kloping.mirai0.Entitys.apiEntitys.baiduShitu.response.List e = iterator.next();
            String title = "标题";
            try {
                title = getTitle(e.getFromUrl());
            } catch (Throwable ex) {
            }
            MessageChainBuilder b = new MessageChainBuilder();
            b.append(MessageTools.createImage(g, e.getThumbUrl())).append(NEWLINE)
                    .append(IMAGE_SOURCE).append(NEWLINE).append("<<").append(title).append(">>").append(NEWLINE).append(e.getFromUrl());
            builder.add(g.getBotAsMember(), b.build());
        }
        return builder.build();
    }

    @AutoStand
    IBaiduShitu iBaiduShitu;
}
