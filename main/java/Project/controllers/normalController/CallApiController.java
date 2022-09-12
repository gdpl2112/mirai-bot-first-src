package Project.controllers.normalController;

import Project.interfaces.http_api.ApiKit9;
import Project.interfaces.http_api.Dzzui;
import Project.interfaces.http_api.JuiLi;
import Project.interfaces.http_api.KlopingWeb;
import Project.interfaces.http_api.old.ApiIyk0;
import io.github.kloping.MySpringTool.annotations.*;
import io.github.kloping.MySpringTool.exceptions.NoRunException;
import io.github.kloping.mirai0.Main.ITools.MemberTools;
import io.github.kloping.mirai0.Main.ITools.MessageTools;
import io.github.kloping.mirai0.Main.Resource;
import io.github.kloping.mirai0.commons.Group;
import io.github.kloping.mirai0.commons.User;
import io.github.kloping.mirai0.commons.apiEntitys.BottleMessage;
import io.github.kloping.mirai0.commons.apiEntitys.jiuli.tianqi.Data;
import io.github.kloping.mirai0.commons.apiEntitys.jiuli.tianqi.Weather;
import io.github.kloping.mirai0.commons.apiEntitys.kloping.VideoAnimeDetail;
import io.github.kloping.mirai0.commons.apiEntitys.kloping.VideoAnimeSource;
import io.github.kloping.mirai0.unitls.Tools.Tool;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.MessageChainBuilder;

import java.net.URLEncoder;
import java.util.Date;

import static Project.controllers.auto.ControllerTool.opened;
import static io.github.kloping.mirai0.Main.Resource.println;
import static io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet.FinalNormalString.EMPTY_STR;
import static io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet.FinalNormalString.GET_FAILED;
import static io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet.FinalString.NEWLINE;
import static io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet.FinalValue.NOT_OPEN_NO_RUN_EXCEPTION;

/**
 * @author github-kloping
 */
@Controller
public class CallApiController {
    public static final String BASE_URL_CLOUD = "http://img.nsmc.org.cn/CLOUDIMAGE/FY4A/MTCC/FY4A_CHINA.JPG";
    public static final String BASE_URL_CLOUD0 = "http://img.nsmc.org.cn/CLOUDIMAGE/FY4A/MTCC/FY4A_DISK.JPG";
    public static final String S0 = "https://api.okjx.cc:3389/jx.php?url=";
    private static final String SPLIT_POINT_STR = ",";

    @AutoStand
    JuiLi juiLi;

    @AutoStand
    private ApiIyk0 apiIyk0;

    @AutoStand
    private ApiKit9 apiKit9;

    @AutoStand
    private Dzzui dzzui;

    @AutoStand
    private KlopingWeb kloping;

    public CallApiController() {
        println(this.getClass().getSimpleName() + "构建");
    }

    @Before
    public void before(Group group) throws NoRunException {
        if (!opened(group.getId(), this.getClass())) {
            throw NOT_OPEN_NO_RUN_EXCEPTION;
        }
    }

    @Action(value = "捡漂流瓶", otherName = {"捡瓶子"})
    public String getBottle() {
        BottleMessage pab = null;
        pab = kloping.pickUpBottle();
        StringBuilder sb = new StringBuilder();
        sb.append("你捡到一个瓶子\n它来自QQ群:").append(pab.getGid())
                .append("\n的:").append(pab.getSid()).append("(").append(pab.getName()).append(")")
                .append("\n在:").append(Tool.tool.df4.format(new Date(pab.getTime())))
                .append("\n写的:").append(pab.getMessage());
        return sb.toString();
    }

    @Action(value = "扔漂流瓶<.+=>str>", otherName = {"扔瓶子<.+=>str>"})
    public String setBottle(long q, Group group, @Param("str") String str) {
        if (str == null || str.trim().isEmpty()) return "请携带内容~";
        String name = MemberTools.getName(q);
        name = name.replaceAll("\\s", "").isEmpty() ? "默认昵称" : name;
        return kloping.throwBottle(group.getId(), q, str, name);
    }

    @Action("未来天气<.+=>ms>")
    public String weather1(@Param("ms") String mess, Group group) {
        Weather weather = juiLi.weather(mess);
        if (weather.getData() == null) {
            return weather.getMsg();
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append(weather.getTips()).append(NEWLINE);
            for (Data datum : weather.getData()) {
                sb.append(datum.getDate())
                        .append("\n\t").append(datum.getWeather())
                        .append("\n\t").append(datum.getHigh())
                        .append("\n\t").append(datum.getLow())
                        .append("\n\t").append(datum.getFx()).append(datum.getFl()).append(NEWLINE);
            }
            return sb.toString().trim();
        }
    }

    @Action("随机头像")
    public String sjtx0(Group group, User user) {
        try {
            MessageTools.instance.sendImageByBytesOnGroupWithAt(dzzui.avatar(), group.getId(), user.getId());
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return GET_FAILED;
        }
    }

    @Action("QQ信息.*?")
    public Object info(@AllMess String mess, long q) {
        String str = Tool.tool.findNumberFromString(mess);
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
        String str = Tool.tool.findNumberFromString(mess);
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
        String str = Tool.tool.findNumberFromString(mess);
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

    @Action("卫星云图")
    public void mn(Group g) {
        net.mamoe.mirai.contact.Group group = Resource.BOT.getGroup(g.getId());
        Image image = MessageTools.instance.createImage(group, BASE_URL_CLOUD);
        MessageChainBuilder builder = new MessageChainBuilder();
        builder.append("当前时间:" + Tool.tool.getTimeYMdhm(System.currentTimeMillis()));
        builder.append("\n");
        builder.append(image);
        group.sendMessage(builder.build());
    }

    @Action("全球卫星云图")
    public void m1(Group g) {
        net.mamoe.mirai.contact.Group group = Resource.BOT.getGroup(g.getId());
        Image image = MessageTools.instance.createImage(group, BASE_URL_CLOUD0);
        MessageChainBuilder builder = new MessageChainBuilder();
        builder.append("当前时间:" + Tool.tool.getTimeYMdhm(System.currentTimeMillis()));
        builder.append("\n");
        builder.append(image);
        group.sendMessage(builder.build());
    }

    @Action("我要看.+")
    public String s0(@AllMess String mess) {
        String s0 = mess.substring(3);
        Integer select0 = -1;
        Integer select1 = -1;
        if (mess.contains(SPLIT_POINT_STR)) {
            try {
                String[] ss = s0.split(SPLIT_POINT_STR);
                String n0 = Tool.tool.findNumberFromString(ss[0]);
                String n1 = Tool.tool.findNumberFromString(ss[1]);
                s0 = s0.replaceFirst(SPLIT_POINT_STR, EMPTY_STR).replaceFirst(n0, EMPTY_STR).replaceFirst(n1, EMPTY_STR);
                select0 = Integer.valueOf(n0);
                select1 = Integer.valueOf(n1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            String n0 = Tool.tool.findNumberFromString(mess);
            if (n0 != null && !n0.isEmpty()) {
                select0 = Integer.parseInt(n0);
            }
        }
        s0 = s0.replace(select0.toString(), "").replace(select1.toString(), "");
        select0--;
        select1--;
        VideoAnimeSource[] sources = kloping.videoSearch(s0, "tp");
        if (select0 < 0 && select1 < 0) {
            int i = 1;
            StringBuilder sb = new StringBuilder();
            for (VideoAnimeSource source0 : sources) {
                sb.append(i++).append(":").append(source0.getName())
                        .append(NEWLINE);
            }
            return sb.toString().trim();
        } else if (select0 > 0 && select1 < 0) {
            VideoAnimeSource source = kloping.videoSearch(s0, "tp", URLEncoder.encode(sources[select0].url))[0];
            return source.getName() + NEWLINE + "更新至" + source.getSt();
        } else {
            VideoAnimeSource source = kloping.videoSearch(s0, "tp", URLEncoder.encode(sources[select0].url))[0];
            VideoAnimeDetail detail = source.details[select1];
            try {
                if (detail.isVip) {
                    return detail.getName() + NEWLINE + S0 + detail.getPlayUrl();
                } else {
                    return detail.getName() + NEWLINE + S0 + detail.getPlayUrl() + NEWLINE + detail.playUrl;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return source.getName() + NEWLINE + "更新至" + source.getSt();
            }
        }
    }
}
