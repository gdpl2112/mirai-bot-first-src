package Project.controllers.normalController;

import Project.commons.SpGroup;
import Project.commons.SpUser;
import Project.commons.apiEntitys.BottleMessage;
import Project.interfaces.httpApi.ApiKit9;
import Project.interfaces.httpApi.Dzzui;
import Project.interfaces.httpApi.KlopingWeb;
import Project.interfaces.httpApi.old.ApiIyk0;
import io.github.kloping.MySpringTool.annotations.*;
import io.github.kloping.MySpringTool.exceptions.NoRunException;
import io.github.kloping.mirai0.Main.iutils.MemberUtils;
import io.github.kloping.mirai0.Main.iutils.MessageUtils;
import io.github.kloping.mirai0.unitls.Tools.Tool;

import java.util.Date;

import static Project.commons.rt.ResourceSet.FinalNormalString.GET_FAILED;
import static Project.commons.rt.ResourceSet.FinalValue.NOT_OPEN_NO_RUN_EXCEPTION;
import static Project.controllers.auto.ControllerTool.opened;
import static io.github.kloping.mirai0.Main.BootstarpResource.println;

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
    public void before(SpGroup group) throws NoRunException {
        if (!opened(group.getId(), this.getClass())) {
            throw NOT_OPEN_NO_RUN_EXCEPTION;
        }
    }

    @Action(value = "捡漂流瓶", otherName = {"捡瓶子"})
    public String getBottle() {
        BottleMessage pab = null;
        pab = kloping.pickUpBottle();
        StringBuilder sb = new StringBuilder();
        sb.append("你捡到一个瓶子\n它来自QQ群:").append(pab.getGid()).append("\n的:").append(pab.getSid()).append("(").append(pab.getName()).append(")").append("\n在:").append(Tool.INSTANCE.df4.format(new Date(pab.getTime()))).append("\n写的:").append(pab.getMessage());
        return sb.toString();
    }

    @Action(value = "扔漂流瓶<.+=>str>", otherName = {"扔瓶子<.+=>str>"})
    public String setBottle(long q, SpGroup group, @Param("str") String str) {
        if (str == null || str.trim().isEmpty()) return "请携带内容~";
        String name = MemberUtils.getName(q);
        name = name.replaceAll("\\s", "").isEmpty() ? "默认昵称" : name;
        return kloping.throwBottle(group.getId(), q, str, name);
    }

    @Action("随机头像")
    public String sjtx0(SpGroup group, SpUser user) {
        try {
            MessageUtils.INSTANCE.sendImageByBytesOnGroupWithAt(dzzui.avatar(), group.getId(), user.getId());
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return GET_FAILED;
        }
    }

    @Action("QQ信息.*?")
    public Object info(@AllMess String mess, long q) {
        String str = Tool.INSTANCE.findNumberFromString(mess);
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
    public Object groupInfo(@AllMess String mess, SpGroup group) {
        long q = group.getId();
        String str = Tool.INSTANCE.findNumberFromString(mess);
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
        String str = Tool.INSTANCE.findNumberFromString(mess);
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
    public String mn(SpGroup g) {
        StringBuilder sb = new StringBuilder();
        sb.append("当前时间:" + Tool.INSTANCE.getTimeYMdhm(System.currentTimeMillis()));
        sb.append("\n");
        sb.append(Tool.INSTANCE.pathToImg(BASE_URL_CLOUD));
        return sb.toString();
    }

    @Action("全球卫星云图")
    public String m1(SpGroup g) {
        StringBuilder sb = new StringBuilder();
        sb.append("当前时间:" + Tool.INSTANCE.getTimeYMdhm(System.currentTimeMillis()));
        sb.append("\n");
        sb.append(Tool.INSTANCE.pathToImg(BASE_URL_CLOUD0));
        return sb.toString();
    }
}
