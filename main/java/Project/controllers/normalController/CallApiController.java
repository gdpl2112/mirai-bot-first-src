package Project.controllers.normalController;

import Project.interfaces.http_api.*;
import com.alibaba.fastjson.JSONObject;
import io.github.kloping.MySpringTool.annotations.*;
import io.github.kloping.MySpringTool.exceptions.NoRunException;
import io.github.kloping.mirai0.Main.ITools.MessageTools;
import io.github.kloping.mirai0.Main.Resource;
import io.github.kloping.mirai0.commons.Group;
import io.github.kloping.mirai0.commons.User;
import io.github.kloping.mirai0.commons.apiEntitys.apiIyk0.YiQing;
import io.github.kloping.mirai0.commons.apiEntitys.jiuli.tianqi.Data;
import io.github.kloping.mirai0.commons.apiEntitys.jiuli.tianqi.Weather;
import io.github.kloping.mirai0.commons.apiEntitys.kloping.VideoAnimeDetail;
import io.github.kloping.mirai0.commons.apiEntitys.kloping.VideoAnimeSource;
import io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet;
import io.github.kloping.mirai0.unitls.Tools.Tool;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.MessageChainBuilder;

import static Project.controllers.auto.ControllerTool.opened;
import static io.github.kloping.mirai0.Main.Resource.println;
import static io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet.FinalNormalString.EMPTY_STR;
import static io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet.FinalNormalString.GET_FAILED;
import static io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet.FinalString.ERR_TIPS;
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
    private static final String[] SJTX_PARMS = {"女", "男", "动漫", "情侣"};
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
    private Kloping kloping;

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
        return ResourceSet.FinalNormalString.FUNCTION_CLOSEING_TIPS;
//        PickupABottle pab = null;
//        try {
//            pab = apiIyk0.pickupBottle(2);
//            StringBuilder sb = new StringBuilder();
//            sb.append("你捡到一个瓶子\n它来自QQ群:").append(pab.getData().getGroup())
//                    .append("\n的:").append(pab.getData().getUin())
//                    .append("\n在:").append(pab.getData().getTime())
//                    .append("\n写的:").append(pab.getData().getMsg());
//            return sb.toString();
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "没捡到瓶子...";
//        }
    }

    @Action(value = "扔漂流瓶<.+=>str>", otherName = {"扔瓶子<.+=>str>"})
    public String setBottle(long q, Group group, @Param("str") String str) {
        return ResourceSet.FinalNormalString.FUNCTION_CLOSEING_TIPS;
//        if (str == null || str.trim().isEmpty()) return "请携带内容~";
//        try {
//            ThrowABottle throwABottle = apiIyk0.throwBottle(1,
//                    str, q, group.getId());
//            return throwABottle.getData().getMsg();
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "扔瓶子失败,大海不允许有敏感词汇的瓶子飘向远方";
//        }
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
            MessageTools.sendImageByBytesOnGroupWithAt(dzzui.avatar(), group.getId(), user.getId());
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return GET_FAILED;
        }
    }

    @Action("随机男头像")
    public String sjtx1() {
        try {
            JSONObject jo = apiIyk0.sjtx(SJTX_PARMS[1]);
            return Tool.pathToImg(jo.getString("img"));
        } catch (Exception e) {
            e.printStackTrace();
            return GET_FAILED;
        }
    }

    @Action("随机女头像")
    public String sjtx2() {
        try {
            JSONObject jo = apiIyk0.sjtx(SJTX_PARMS[0]);
            return Tool.pathToImg(jo.getString("img"));
        } catch (Exception e) {
            e.printStackTrace();
            return GET_FAILED;
        }
    }

    @Action("随机情侣头像")
    public String sjtx3() {
        try {
            JSONObject jo = apiIyk0.sjtx(SJTX_PARMS[3]);
            return Tool.pathToImg(jo.getString("img1")) + "\n" + Tool.pathToImg(jo.getString("img2"));
        } catch (Exception e) {
            e.printStackTrace();
            return GET_FAILED;
        }
    }

    @Action("疫情<.+=>name>")
    public String yq0(@Param("name") String address) {
        YiQing qing = apiIyk0.yq(address);
        if (qing == null) {
            return ERR_TIPS;
        }
        StringBuilder sb = new StringBuilder()
                .append("地区:").append(qing.get查询地区()).append(NEWLINE)
                .append("时间:").append(qing.getTime()).append(NEWLINE)
                .append("新增确诊:").append(qing.get新增确诊()).append(NEWLINE)
                .append("目前确诊:").append(qing.get目前确诊()).append(NEWLINE)
                .append("现存确诊:").append(qing.get现存确诊()).append(NEWLINE)
                .append("现存无症状:").append(qing.get现存无症状()).append(NEWLINE)
                .append("治愈人数:").append(qing.get治愈人数()).append(NEWLINE)
                .append("死亡人数:").append(qing.get死亡人数()).append(NEWLINE);

        return sb.toString();
    }

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

    @Action("全国降水量")
    public String lowWater() {
        return Tool.pathToImg(apiIyk0.getJyu().getImg());
    }

    @Action("卫星云图")
    public void mn(Group g) {
        net.mamoe.mirai.contact.Group group = Resource.BOT.getGroup(g.getId());
        Image image = MessageTools.createImage(group, BASE_URL_CLOUD);
        MessageChainBuilder builder = new MessageChainBuilder();
        builder.append("当前时间:" + Tool.getTimeYMdhm(System.currentTimeMillis()));
        builder.append("\n");
        builder.append(image);
        group.sendMessage(builder.build());
    }

    @Action("全球卫星云图")
    public void m1(Group g) {
        net.mamoe.mirai.contact.Group group = Resource.BOT.getGroup(g.getId());
        Image image = MessageTools.createImage(group, BASE_URL_CLOUD0);
        MessageChainBuilder builder = new MessageChainBuilder();
        builder.append("当前时间:" + Tool.getTimeYMdhm(System.currentTimeMillis()));
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
                String n0 = Tool.findNumberFromString(ss[0]);
                String n1 = Tool.findNumberFromString(ss[1]);
                s0 = s0.replaceFirst(SPLIT_POINT_STR, EMPTY_STR).replaceFirst(n0, EMPTY_STR).replaceFirst(n1, EMPTY_STR);
                select0 = Integer.valueOf(n0);
                select1 = Integer.valueOf(n1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            String n0 = Tool.findNumberFromString(mess);
            if (n0 != null && !n0.isEmpty()) {
                select0 = Integer.parseInt(n0);
            }
        }
        s0 = s0.replace(select0.toString(), "").replace(select1.toString(), "");
        select0--;
        select1--;
        VideoAnimeSource[] sources = kloping.videoSearch(s0, "all");
        if (select0 < 0 && select1 < 0) {
            int i = 1;
            StringBuilder sb = new StringBuilder();
            for (VideoAnimeSource source0 : sources) {
                sb.append(i++).append(":").append(source0.getName())
                        .append(NEWLINE);
            }
            return sb.toString().trim();
        } else if (select0 > 0 && select1 < 0) {
            VideoAnimeSource source = sources[select0];
            return source.getName() + NEWLINE + "更新至" + source.getSt();
        } else {
            VideoAnimeSource source = sources[select0];
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
