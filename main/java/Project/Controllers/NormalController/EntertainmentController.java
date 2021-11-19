package Project.Controllers.NormalController;

import Entitys.Group;
import Project.DataBases.DataBase;
import Project.Plugins.WeatherGetter;
import Project.Services.IServer.IOtherService;
import Project.Tools.Tool;
import io.github.kloping.Mirai.Main.ITools.MessageTools;
import io.github.kloping.Mirai.Main.Resource;
import io.github.kloping.MySpringTool.annotations.*;
import io.github.kloping.MySpringTool.exceptions.NoRunException;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.MessageChainBuilder;

import java.net.URLEncoder;

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


    @Action("[闪照<.+=>s1>")
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

    @Action("[@me]<.{1,}=>str>")
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

}
