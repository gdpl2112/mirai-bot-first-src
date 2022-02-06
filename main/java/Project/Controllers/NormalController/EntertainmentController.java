package Project.Controllers.NormalController;

import Project.DataBases.DataBase;
import Project.ResourceSet;
import Project.broadcast.PicBroadcast;
import Project.detailPlugin.SearchSong;
import Project.detailPlugin.WeatherGetter;
import Project.interfaces.ApiIyk0;
import Project.interfaces.ApiKit9;
import Project.services.DetailServices.Idiom;
import Project.services.Iservice.IOtherService;
import io.github.kloping.MySpringTool.annotations.*;
import io.github.kloping.MySpringTool.exceptions.NoRunException;
import io.github.kloping.mirai0.Entitys.Group;
import io.github.kloping.mirai0.Entitys.UScore;
import io.github.kloping.mirai0.Entitys.User;
import io.github.kloping.mirai0.Entitys.apiEntitys.Songs;
import io.github.kloping.mirai0.Main.ITools.MessageTools;
import io.github.kloping.mirai0.Main.Resource;
import io.github.kloping.mirai0.unitls.Tools.Tool;
import io.github.kloping.mirai0.unitls.drawers.ImageDrawer;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.SimpleServiceMessage;

import java.io.File;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static Project.Controllers.ControllerTool.canGroup;
import static Project.Controllers.NormalController.CustomController.QLIST;
import static Project.Controllers.NormalController.CustomController.builderAndAdd;
import static Project.Controllers.TimerController.BASE_URL_CLOUD;
import static Project.DataBases.DataBase.canBackShow;
import static Project.ResourceSet.FinalString.*;
import static io.github.kloping.mirai0.Main.Resource.Switch.AllK;
import static io.github.kloping.mirai0.Main.Resource.Switch.sendFlashToSuper;
import static io.github.kloping.mirai0.Main.Resource.*;

/**
 * @author github-kloping
 */
@Controller
public class EntertainmentController {
    public EntertainmentController() {
        println(this.getClass().getSimpleName() + "构建");
    }

    @Before
    public void before(Group group, @AllMess String mess) throws NoRunException {
        if (!AllK) {
            throw new NoRunException();
        }
        if (mess.startsWith("[闪照")) {

        } else {
            if (!canGroup(group.getId())) {
                throw new NoRunException();
            }
        }
    }

    @Action("\\[Pic:.+")
    public String onPic(@AllMess String mess, Group group, Object[] objects, long qq) {
        PicBroadcast.INSTANCE.broadcast(qq, group.getId(), mess, objects);
        if (QLIST.containsKey(qq)) {
            String str = QLIST.get(qq);
            str = str.replaceFirst("\\*", mess);
            if (str.contains("*")) {
                QLIST.remove(qq);
                QLIST.put(qq, str);
                return "已填充1个";
            } else {
                QLIST.remove(qq);
                if (builderAndAdd(str, qq)) {
                    return "填充完成\r\n添加完成";
                } else {
                    return ResourceSet.FinalString.ADD_TO_AUTO_REPLY_ERROR;
                }
            }
        } else {
            throw new NoRunException("没有在添加");
        }
    }

    @AutoStand
    public static IOtherService otherService;

    @AutoStand
    public WeatherGetter weatherGetter;

    @Action("短时预报<.+=>address>")
    public String m2(@Param("address") String address) {
        return weatherGetter.get(address);
    }

    @Action("天气<.+=>name>")
    public String Weather(@Param("name") String name, Group group) {
        String line = weatherGetter.detail(name);
        if (voiceK) {
            speak(line, group);
        }
        return line;
    }

    @AutoStand
    ApiIyk0 apiIyk0;

    @Action("全国降水量")
    public String lowWater() {
        return Tool.pathToImg(apiIyk0.getJyu().getImg());
    }

    public static void speak(String line, Group group) {
        try {
            MessageTools.sendVoiceMessageInGroup(String.format(BASE_VOICE_URL, URLEncoder.encode(line, "utf-8")), group.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Action("卫星云图")
    public void mn(Group g) {
        net.mamoe.mirai.contact.Group group = Resource.bot.getGroup(g.getId());
        Image image = MessageTools.createImage(group, BASE_URL_CLOUD);
        MessageChainBuilder builder = new MessageChainBuilder();
        builder.append("当前时间:" + Tool.getTimeYMdhm(System.currentTimeMillis()));
        builder.append("\n");
        builder.append(image);
        group.sendMessage(builder.build());
    }

    @Action("全球卫星云图")
    public void m1(Group g) {
        net.mamoe.mirai.contact.Group group = Resource.bot.getGroup(g.getId());
        Image image = MessageTools.createImage(group, "http://img.nsmc.org.cn/CLOUDIMAGE/FY4A/MTCC/FY4A_DISK.JPG");
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

    private final long cd_ = 10 * 1000;
    private long cd = 10 * 1000;

    @Action("\\[@me]<.{1,}=>str>")
    public Object atMe(long qq, Group group, @Param("str") String str) {
        if (cd > System.currentTimeMillis()) {
            return null;
        }
        if (str.startsWith(SPEAK_STR)) {
            if (voiceK) {
                speak(str.substring(1), group);
            }
            return null;
        } else {
            if (OPEN_STR.equals(str)) {
                if (!DataBase.isFather(qq)) {
                    return null;
                }
                return controller.open(group);
            } else if (CLOSE_STR.equals(str)) {
                if (!DataBase.isFather(qq)) {
                    return null;
                }
                return controller.close(group);
            } else if (DataBase.canSpeak(group.getId())) {
                String talk = otherService.Talk(str);
                if (voiceK) {
                    speak(talk, group);
                }
                cd = System.currentTimeMillis() + cd_;
                return talk;
            } else {
                throw new NoRunException();
            }
        }

    }

    @AutoStand
    private ManagerController controller;

    public static boolean voiceK = false;

    public static final String BASE_VOICE_URL = "https://tts.youdao.com/fanyivoice?word=%s&le=zh&keyfrom=speaker-target";

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
        if (idiom == null) {
            return "游戏未开始:请说 开始成语接龙";
        }
        UScore score = DataBase.getAllInfo(user.getId());
        if (score.getScore() < eveS1) {
            return "您的积分不足...";
        }
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
            if (sb.toString().endsWith("=>")) {
                sb = sb.replace(sb.length() - 2, sb.length(), "");
            }
        }
        return sb.toString();
    }

    @AutoStand
    SearchSong searchSong;

    @Action("QQ歌词<.+=>name>")
    public Object mq(@Param("name") String name, Group group) {
        try {
            Songs songs = searchSong.qq(name);
            String lyric = songs.getData()[0].getLyric();
            MessageTools.sendMessageByForward(group.getId(), lyric.split("\r|\n"));
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return "歌词获取失败";
        }
    }

    @Action("酷狗歌词<.+=>name>")
    public Object mk(@Param("name") String name, Group group) {
        try {
            Songs songs = searchSong.kugou(name);
            String lyric = songs.getData()[0].getLyric();
            MessageTools.sendMessageByForward(group.getId(), lyric.split("\r|\n"));
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return "歌词获取失败";
        }
    }

    @Action("网易歌词<.+=>name>")
    public Object mw(@Param("name") String name, Group group) {
        try {
            Songs songs = searchSong.netEase(name);
            String lyric = songs.getData()[0].getLyric();
            MessageTools.sendMessageByForward(group.getId(), lyric.split("\r|\n"));
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return "歌词获取失败";
        }
    }

    @AutoStand
    ApiKit9 apiKit9;

    @Action("QQ信息.*?")
    public Object info(@AllMess String mess, long q) {
        String str = Tool.findNumberFromString(mess);
        try {
            Long q2 = Long.parseLong(str);
            q = q2.longValue();
        } catch (NumberFormatException e) {
        }
        return apiKit9.getInfo(q).toString();
    }
}
