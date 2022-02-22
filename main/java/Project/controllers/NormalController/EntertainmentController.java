package Project.controllers.NormalController;

import Project.ResourceSet;
import Project.broadcast.PicBroadcast;
import Project.dataBases.DataBase;
import Project.detailPlugin.SearchSong;
import Project.detailPlugin.WeatherGetter;
import Project.interfaces.http_api.ApiIyk0;
import Project.interfaces.http_api.ApiKit9;
import Project.interfaces.Iservice.IOtherService;
import Project.services.detailServices.Idiom;
import io.github.kloping.MySpringTool.annotations.*;
import io.github.kloping.MySpringTool.exceptions.NoRunException;
import io.github.kloping.mirai0.Entitys.Group;
import io.github.kloping.mirai0.Entitys.GroupConf;
import io.github.kloping.mirai0.Entitys.UScore;
import io.github.kloping.mirai0.Entitys.User;
import io.github.kloping.mirai0.Entitys.apiEntitys.Songs;
import io.github.kloping.mirai0.Main.ITools.MessageTools;
import io.github.kloping.mirai0.Main.Resource;
import io.github.kloping.mirai0.unitls.Tools.Tool;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.MessageChainBuilder;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static Project.ResourceSet.FinalString.*;
import static Project.controllers.ControllerTool.canGroup;
import static Project.controllers.NormalController.CustomController.QLIST;
import static Project.controllers.NormalController.CustomController.builderAndAdd;
import static Project.controllers.Plugins.PointSongController.sing;
import static Project.dataBases.DataBase.canBackShow;
import static Project.dataBases.DataBase.getConf;
import static io.github.kloping.mirai0.Main.ITools.MessageTools.speak;
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
    public String weather0(@Param("name") String name, Group group) {
        String line = weatherGetter.detail(name);
        if (getConf(group.getId()).getVoiceK()) {
            speak(line, group);
        }
        return line;
    }

    @Action("天气预报.+")
    public String weather1(@AllMess String mess, Group group) {
        String line = otherService.talk(mess);
        return line;
    }

    @AutoStand
    private ApiIyk0 apiIyk0;

    @Action("全国降水量")
    public String lowWater() {
        return Tool.pathToImg(apiIyk0.getJyu().getImg());
    }

    public static final String BASE_URL_CLOUD = "http://img.nsmc.org.cn/CLOUDIMAGE/FY4A/MTCC/FY4A_CHINA.JPG";
    public static final String BASE_URL_CLOUD0 = "http://img.nsmc.org.cn/CLOUDIMAGE/FY4A/MTCC/FY4A_DISK.JPG";

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
        Image image = MessageTools.createImage(group, BASE_URL_CLOUD0);
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

    @Action("\\[@me]<.{1,}=>str>")
    public Object atMe(long qq, Group group, @Param("str") String str) {
        if (str.startsWith(SPEAK_STR)) {
            speak(str.substring(1), group);
            return null;
        } else if (str.startsWith(SING_STR)) {
            sing(str.substring(1), group);
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
                if (!Tool.isIlleg(str)) {
                    String talk = otherService.talk(str);
                    return talk;
                }
            }
        }
        throw new NoRunException();
    }

    @AutoStand
    private ManagerController controller;

    @Action("语音")
    public String a1(Group group) {
        GroupConf conf = getConf(group.getId());
        conf.setVoiceK(!conf.getVoiceK());
        return conf.getVoiceK() ? "当前开启" : "当前关闭";
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

    /**
     * 成语接龙消耗积分
     */
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
    private SearchSong searchSong;

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
    private ApiKit9 apiKit9;

    @Action("QQ信息.*?")
    public Object info(@AllMess String mess, long q) {
        String str = Tool.findNumberFromString(mess);
        try {
            Long q2 = Long.parseLong(str);
            q = q2.longValue();
        } catch (NumberFormatException e) {
        }
        try {
            return apiKit9.getInfo(q).toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "获取失败";
        }
    }

    @Action("QQ群信息.*?")
    public Object groupInfo(@AllMess String mess, Group group) {
        long q = group.getId();
        String str = Tool.findNumberFromString(mess);
        try {
            Long q2 = Long.parseLong(str);
            q = q2.longValue();
        } catch (NumberFormatException e) {
        }
        try {
            return apiKit9.getGroupInfo(q).toStrings();
        } catch (Exception e) {
            e.printStackTrace();
            return "获取失败";
        }
    }

    @Action("QQ达人.*?")
    public Object getTalent(@AllMess String mess, long q) {
        String str = Tool.findNumberFromString(mess);
        try {
            Long q2 = Long.parseLong(str);
            q = q2.longValue();
        } catch (NumberFormatException e) {
        }
        try {
            return "QQ达人天数:" + apiKit9.getTalent(q);
        } catch (Exception e) {
            e.printStackTrace();
            return "获取失败";
        }
    }
}
