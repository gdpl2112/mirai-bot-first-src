package Project.Controllers.NormalController;

import Entitys.Group;
import Entitys.UScore;
import Entitys.User;
import Project.DataBases.DataBase;
import Project.Plugins.WeatherGetter;
import Project.Services.DetailServices.Idiom;
import Project.Services.Iservice.IOtherService;
import Project.Tools.ImageDrawer;
import Project.Tools.Tool;
import io.github.kloping.Mirai.Main.ITools.MessageTools;
import io.github.kloping.Mirai.Main.Resource;
import io.github.kloping.MySpringTool.annotations.*;
import io.github.kloping.MySpringTool.exceptions.NoRunException;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.MessageChainBuilder;

import java.io.File;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static Project.Controllers.ControllerTool.CanGroup;
import static Project.Controllers.TimerController.baseUrlCloud;
import static Project.DataBases.DataBase.canBackShow;
import static io.github.kloping.Mirai.Main.Resource.*;
import static io.github.kloping.Mirai.Main.Resource.Switch.AllK;
import static io.github.kloping.Mirai.Main.Resource.Switch.sendFlashToSuper;

@Controller
public class EntertainmentController {
    public EntertainmentController() {
        println(this.getClass().getSimpleName() + "构建");

    }

    @Before
    public void before(Group group) throws NoRunException {
        if (!AllK)
            throw new NoRunException();
        if (!CanGroup(group.getId())) {
            throw new NoRunException();
        }
    }

    @AutoStand
    IOtherService otherService;

    @Action("短时预报<.+=>address>")
    public String m2(@Param("address") String address) {
        return WeatherGetter.get(address);
    }

    @Action("天气<.+=>name>")
    public String Weather(@Param("name") String name, Group group) {
        String line = WeatherGetter.detail(name);
        if (voiceK) {
            speak(line, group);
        }
        return line;
    }

    public static void speak(String line, Group group) {
        try {
            MessageTools.sendVoiceMessageInGroup(String.format(baseUrlV, URLEncoder.encode(line, "utf-8")), group.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Action("卫星云图")
    public void mn(Group g) {
        net.mamoe.mirai.contact.Group group = Resource.bot.getGroup(g.getId());
        Image image = MessageTools.createImageInGroup(group, baseUrlCloud);
        MessageChainBuilder builder = new MessageChainBuilder();
        builder.append("当前时间:" + Tool.getTimeYMdhm(System.currentTimeMillis()));
        builder.append("\n");
        builder.append(image);
        group.sendMessage(builder.build());
    }

    @Action("全球卫星云图")
    public void m1(Group g) {
        net.mamoe.mirai.contact.Group group = Resource.bot.getGroup(g.getId());
        Image image = MessageTools.createImageInGroup(group, "http://img.nsmc.org.cn/CLOUDIMAGE/FY4A/MTCC/FY4A_DISK.JPG");
        MessageChainBuilder builder = new MessageChainBuilder();
        builder.append("当前时间:" + Tool.getTimeYMdhm(System.currentTimeMillis()));
        builder.append("\n");
        builder.append(image);
        group.sendMessage(builder.build());
    }


    @Action("时间")
    public Object nowTime() {
        return Tool.getTimeYMdhms(System.currentTimeMillis());
    }

    @Action("2传话<.+=>str>")
    public Object transTo2(@Param("str") String str, Group group, Long qq) {
        return otherService.trans2(str, group, qq);
    }

    @Action("传话<.+=>str>")
    public Object transTo(@Param("str") String str, Group group, Long qq) {
        return otherService.trans(str, group, qq);
    }

    @Action(value = "晚安", otherName = {"晚", "晚好"})
    public String Eveing(@AllMess String Strings) throws NoRunException {
        if (Switch.isWelcome)
            return otherService.Talk(Strings);
        else throw new NoRunException();
    }

    @Action(value = "早", otherName = {"早啊", "早安"})
    public String Morning(@AllMess String Strings) throws NoRunException {
        if (Switch.isWelcome)
            return otherService.Talk(Strings);
        else throw new NoRunException();
    }


    @Action("\\[闪照<.+=>s1>")
    public String flash(@AllMess String str, Group group) throws NoRunException {
        String url = MessageTools.getFlashUrlFromMessageString(str);
        if (canBackShow(group.getId())) {
            return Tool.pathToImg(url);
        } else if (sendFlashToSuper) {
            try {
                bot.getGroup(794238572L).sendMessage(url);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        throw new NoRunException();
    }

    @Action("\\[@me]<.{1,}=>str>")
    public Object AtMe(long qq, Group group, @Param("str") String str) {
        if (str.startsWith("读")) {
            if (voiceK) {
                speak(str.substring(1), group);
            }
            return null;
        }
        if (DataBase.canSpeak(group.getId())) {
            String talk = otherService.Talk(str);
            if (voiceK) {
                speak(talk, group);
            }
            return talk;
        } else {
            throw new NoRunException("未开启聊天");
        }
    }

    public static boolean voiceK = true;

    public static final String baseUrlV = "https://tts.youdao.com/fanyivoice?word=%s&le=zh&keyfrom=speaker-target";

    @Action("语音")
    public String a1() {
        voiceK = !voiceK;
        return voiceK ? "当前开启" : "当前关闭";
    }

    @Action(value = "掷骰子", otherName = "摇骰子")
    public String rand(Group group) {
        StringBuilder builder = new StringBuilder();
        int r = Tool.rand.nextInt(6);
        String str = datePath + "/GameFile/Rt_";
        str += r;
        str += ".jpg";
        return Tool.pathToImg(str);
    }

    public static int maxFail = 5;
    public Map<Long, Idiom> longIdiomMap = new ConcurrentHashMap<>();

    @Action("开始成语接龙")
    public String s1(Group group) {
        if (longIdiomMap.containsKey(group.getId())) {
            return "游戏已经开始了哦~";
        }
        Idiom idiom = new Idiom(DataBase.path + "/idiom.txt") {
            @Override
            public void fail(String s) {
                longIdiomMap.remove(group.getId());
            }
        };
        longIdiomMap.put(group.getId(), idiom);
        return "游戏开始:\n当前成语: " + idiom.getRandom() + "\n命令:我接xxxx";
    }

    private static int eveS1 = 2;

    @Action("我接<.+=>str>")
    public String s2(@Param("str") String str, Group group, User user) {
        Idiom idiom = longIdiomMap.get(group.getId());
        if (idiom == null) return "游戏未开始:请说 开始成语接龙";
        UScore score = DataBase.getAllInfo(user.getId());
        if (score.getScore() < eveS1)
            return "您的积分不足...";
        String s = idiom.meet(str);
        StringBuilder sb = new StringBuilder();
        switch (s) {
            case "-1":
                sb.append("必须是4个字的成语哦\n");
                sb.append("扣除1积分");
                score.setScore(score.getScore() - 1);
                DataBase.putInfo(score);
                break;
            case "-2":
                sb.append("\"").append(str);
                sb.append("\"好像不是一个成语呢\n");
                sb.append("扣除").append(eveS1).append("积分");
                score.setScore(score.getScore() - eveS1);
                DataBase.putInfo(score);
                break;
            case "-3":
                sb.append("音节好像不对哦\n");
                sb.append("扣除").append(eveS1).append("积分");
                score.setScore(score.getScore() - eveS1);
                DataBase.putInfo(score);
                break;
            case "-4":
                sb.append("这个词已经用过了哦\n");
                sb.append("扣除").append(eveS1).append("积分");
                score.setScore(score.getScore() - eveS1);
                DataBase.putInfo(score);
                break;
            default:
                sb.append(s);
                sb.append("接上了\n");
                sb.append("获得").append(eveS1 * 2).append("积分");
                score.setScore(score.getScore() + eveS1 * 2);
                DataBase.putInfo(score);
                break;
        }
        sb.append("\n第").append(idiom.getHist().size()).append("次的接龙");
        sb.append("\n当前成语: ").append(idiom.getUpWord()).append("\n")
                .append("末尾音节: ").append(idiom.getUpPinYin());
        if (!longIdiomMap.containsKey(group.getId())) {
            sb.append("\n游戏结束!!");
            sb.append("\n=========\n");
            for (String s1 : idiom.getHist()) {
                sb.append(s1).append("=>");
            }
            if (sb.toString().endsWith("=>"))
                sb = sb.replace(sb.length() - 2, sb.length(), "");
        }
        return sb.toString();
    }

    public static final File[] files_tui = new File("./images/tui").listFiles();
    public static final File[] files_wq = new File("./images/wq").listFiles();

    @Action("/推.*")
    public String m1(@AllMess String m) {
        long q = MessageTools.getAtFromString(m);
        String urlStr = null;
        if (q == -1) {
            urlStr = MessageTools.getImageUrlFromMessageString(m);
            if (urlStr == null)
                return "目前只支@的形式、或携带图片";
        } else {
            urlStr = Tool.getTouUrl(q);
        }
        try {
            URL u = new URL(urlStr);
            File outFile = new File("./temp/" + UUID.randomUUID() + "-tui.png");
            outFile = ImageDrawer.getTuiGift(files_wq, u, outFile);
            return Tool.pathToImg(outFile.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
            return "error:for\n" + e.getMessage();
        }
    }

    @Action("/玩球.*")
    public String m_2(@AllMess String m) {
        long q = MessageTools.getAtFromString(m);
        String urlStr = null;
        if (q == -1) {
            urlStr = MessageTools.getImageUrlFromMessageString(m);
            if (urlStr == null)
                return "目前只支@的形式、或携带图片";
        } else {
            urlStr = Tool.getTouUrl(q);
        }
        try {
            URL u = new URL(urlStr);
            File outFile = new File("./temp/" + UUID.randomUUID() + "-wq.png");
            outFile = ImageDrawer.getWq(files_wq, u, outFile);
            return Tool.pathToImg(outFile.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
            return "error:for\n" + e.getMessage();
        }
    }
}
