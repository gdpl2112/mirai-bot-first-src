package Project.controllers.normalController;

import Project.broadcast.PicBroadcast;
import Project.commons.SpGroup;
import Project.commons.SpUser;
import Project.interfaces.httpApi.Atoolbox;
import Project.interfaces.httpApi.KlopingWeb;
import io.github.kloping.MySpringTool.annotations.*;
import io.github.kloping.MySpringTool.exceptions.NoRunException;
import io.github.kloping.mirai0.Main.iutils.MessageUtils;
import Project.utils.Tools.Tool;
import Project.utils.drawers.GameDrawer;
import Project.utils.drawers.ImageDrawer;
import Project.utils.drawers.entity.GameMap;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import static Project.commons.rt.ResourceSet.FinalValue.NOT_OPEN_NO_RUN_EXCEPTION;
import static Project.controllers.auto.ControllerTool.opened;
import static io.github.kloping.mirai0.Main.BootstarpResource.println;

/**
 * @author github-kloping
 */
@Controller
public class SummonPicController {
    private static final String COM_PRE = "-";
    public static File[] filesTui = new File("./images/tui").listFiles();
    public static File[] filesWq = new File("./images/wq").listFiles();
    public static File fileDiu = new File("./images/diu/diu.png");

    static {
        Arrays.sort(filesWq);
        Arrays.sort(filesTui);
    }

    @AutoStand
    KlopingWeb klopingWeb;

    @AutoStand
    Atoolbox atoolbox;

    public SummonPicController() {
        println(this.getClass().getSimpleName() + "构建");
    }

    @Before
    public void before(SpGroup group) throws NoRunException {
        if (!opened(group.getId(), this.getClass())) {
            throw NOT_OPEN_NO_RUN_EXCEPTION;
        }
    }

    @Action("/testMap<.+=>str>")
    public String t1(@Param("str") String str, SpUser user) throws IOException {
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
        GameMap.GameMapBuilder builder = new GameMap.GameMapBuilder().setWidth(w).setHeight(h).append(x, y, "https://q1.qlogo.cn/g?b=qq&nk=" + user.getId() + "&s=640");
        return Tool.INSTANCE.pathToImg(GameDrawer.drawerMap(builder.build()));
    }

    @Action("/爬.+")
    public Object o1(@AllMess String m, SpGroup group, long q1) {
        long q = Project.utils.Utils.getAtFromString(m);
        String urlStr = null;
        if (q == -1) {
            return "目前只支@的形式";
        }
        String url = klopingWeb.pa(q);
        return Tool.INSTANCE.pathToImg(url);
    }

    @Action("/赞.+")
    public Object o3(@AllMess String m, SpGroup group, long q1) {
        long q = Project.utils.Utils.getAtFromString(m);
        String urlStr = null;
        if (q == -1) {
            urlStr = MessageUtils.INSTANCE.getImageUrlFromMessageString(m);
            if (urlStr == null) {
                return "目前只支@的形式、或携带图片";
            }
        } else {
            urlStr = Tool.INSTANCE.getTouUrl(q);
        }
        try {
            URL u = new URL(urlStr);
            File outFile = new File("./temp/" + UUID.randomUUID() + "-zan.png");
            outFile = ImageDrawer.getZan(new File("./images/zan.jpg"), u, outFile);
            return Tool.INSTANCE.pathToImg(outFile.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
            return "error:for\n" + e.getMessage();
        }
    }

    @Action("/举牌子.+")
    public Object o2(@AllMess String m, SpGroup group, long q1) {
        String msg = m.replace("/举牌子", "");
        if (msg == null || msg.trim().isEmpty()) {
            msg = "请指定内容哦~";
        }
        Entry<String, String> e0 = Tool.INSTANCE.getEntry("c", msg);
        Entry<String, Boolean> e1 = Tool.INSTANCE.getEntry("t", true);
        Entry<String, String> e2 = Tool.INSTANCE.getEntry("b", "#000000");
        byte[] bytes = Tool.INSTANCE.getBase64Data(atoolbox.s0(e0, e1, e2));
        MessageUtils.INSTANCE.sendImageByBytesOnGroupWithAt(bytes, group.getId(), q1);
        return null;
    }

    @Action("/推.*")
    public String m1(@AllMess String m) {
        long q = Project.utils.Utils.getAtFromString(m);
        String urlStr = null;
        if (q == -1) {
            urlStr = MessageUtils.INSTANCE.getImageUrlFromMessageString(m);
            if (urlStr == null) {
                return "目前只支@的形式、或携带图片";
            }
        } else {
            urlStr = Tool.INSTANCE.getTouUrl(q);
        }
        try {
            URL u = new URL(urlStr);
            File outFile = new File("./temp/" + UUID.randomUUID() + "-tui.png");
            outFile = ImageDrawer.getTuiGift(filesTui, u, outFile);
            return Tool.INSTANCE.pathToImg(outFile.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
            return "error:for\n" + e.getMessage();
        }
    }

    @Action("/玩球.*")
    public String m2(@AllMess String m) {
        long q = Project.utils.Utils.getAtFromString(m);
        String urlStr = null;
        if (q == -1) {
            urlStr = MessageUtils.INSTANCE.getImageUrlFromMessageString(m);
            if (urlStr == null) {
                return "目前只支@的形式、或携带图片";
            }
        } else {
            urlStr = Tool.INSTANCE.getTouUrl(q);
        }
        try {
            URL u = new URL(urlStr);
            File outFile = new File("./temp/" + UUID.randomUUID() + "-wq.png");
            outFile = ImageDrawer.getWq(filesWq, u, outFile);
            return Tool.INSTANCE.pathToImg(outFile.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
            return "error:for\n" + e.getMessage();
        }
    }

    @Action("/丢.*")
    public String m3(@AllMess String m) {
        long q = Project.utils.Utils.getAtFromString(m);
        String urlStr = null;
        if (q == -1) {
            urlStr = MessageUtils.INSTANCE.getImageUrlFromMessageString(m);
            if (urlStr == null) {
                return "目前只支@的形式、或携带图片";
            }
        } else {
            urlStr = Tool.INSTANCE.getTouUrl(q);
        }
        try {
            URL u = new URL(urlStr);
            File outFile = new File("./temp/" + UUID.randomUUID() + "-wq.png");
            outFile = ImageDrawer.getDui(fileDiu, u, outFile);
            return Tool.INSTANCE.pathToImg(outFile.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
            return "error:for\n" + e.getMessage();
        }
    }

    @Action("识别.*+")
    public void a1(@AllMess String mess, SpGroup group, long qId) {
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
        MessageUtils.INSTANCE.sendMessageInGroup("请在发送要识别的图片", group.getId());
        int size = size0;
        PicBroadcast.INSTANCE.add(new PicBroadcast.PicReceiverOnce() {
            @Override
            public Object onReceive(long qid, long gid, String pic, Object[] objects) {
                if (qId == qid) {
                    String url = MessageUtils.INSTANCE.getImageUrlFromMessageString(pic);
                    String end = "识别结果:\n" + klopingWeb.ocr(url);
                    MessageUtils.INSTANCE.sendMessageInGroupWithAt(end, gid, qid);
                    return "ok";
                }
                return null;
            }
        });
    }

    @Action("/转字符图<.+=>str>")
    public void s0(long qId, @Param("str") String word, SpGroup group) {
        if (word.length() < 1 || word.length() > 4) {
            MessageUtils.INSTANCE.sendMessageInGroup("将字符控制在1-4个之间", group.getId());
            return;
        }
        MessageUtils.INSTANCE.sendMessageInGroup("请在发送要变的图片", group.getId());
        PicBroadcast.INSTANCE.add(new PicBroadcast.PicReceiverOnce() {
            @Override
            public Object onReceive(long qid, long gid, String pic, Object[] objects) {
                if (qId == qid) {
                    String name = UUID.randomUUID() + ".png";
                    new File("./temp").mkdirs();
                    File file = new File("./temp/" + name);
                    String url = MessageUtils.INSTANCE.getImageUrlFromMessageString(pic);
                    try {
                        ImageDrawer.getPixelWordImage(new URL(url), file, word);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    MessageUtils.INSTANCE.sendMessageInGroup(Tool.INSTANCE.pathToImg(file.getAbsolutePath()), gid);
                    return "ok";
                }
                return null;
            }
        });
    }
}
