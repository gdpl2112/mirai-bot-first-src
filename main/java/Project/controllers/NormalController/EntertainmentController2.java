package Project.controllers.NormalController;

import Project.detailPlugin.GetPvpNews;
import Project.detailPlugin.MihoyoP0;
import Project.detailPlugin.PvpQq;
import Project.interfaces.http_api.ApiIyk0;
import Project.interfaces.http_api.GetPvpQQ;
import Project.interfaces.http_api.MuXiaoGuo;
import io.github.kloping.MySpringTool.annotations.*;
import io.github.kloping.MySpringTool.exceptions.NoRunException;
import io.github.kloping.mirai0.Entitys.Group;
import io.github.kloping.mirai0.Entitys.apiEntitys.baiKe.BaiKe;
import io.github.kloping.mirai0.Entitys.apiEntitys.colb.PickupABottle;
import io.github.kloping.mirai0.Entitys.apiEntitys.pvpQQH0.Data;
import io.github.kloping.mirai0.Entitys.apiEntitys.pvpQQVoice.Yy_4e;
import io.github.kloping.mirai0.Entitys.apiEntitys.pvpQqCom.Response0;
import io.github.kloping.mirai0.Entitys.apiEntitys.pvpSkin.Pcblzlby_c6;
import io.github.kloping.mirai0.Entitys.apiEntitys.pvpSkin.PvpSkin;
import io.github.kloping.mirai0.Entitys.apiEntitys.thb.ThrowABottle;
import io.github.kloping.mirai0.Main.ITools.MessageTools;
import io.github.kloping.mirai0.unitls.Tools.Tool;
import net.mamoe.mirai.message.data.Message;

import static Project.controllers.auto.ControllerTool.opened;
import static Project.ResourceSet.FinalString.NEWLINE;
import static Project.ResourceSet.FinalString.SPLIT_LINE_0;
import static io.github.kloping.mirai0.Main.Resource.println;
import static io.github.kloping.mirai0.Main.Resource.superQL;
import static io.github.kloping.mirai0.unitls.Tools.Tool.findNumberFromString;
import static io.github.kloping.mirai0.unitls.Tools.Tool.getInteagerFromStr;

/**
 * @author github-kloping
 */
@Controller
public class EntertainmentController2 {
    public EntertainmentController2() {
        println(this.getClass().getSimpleName() + "构建");
    }

    public static long upNewsId = 0;

    @Before
    public void before(Group group) throws NoRunException {
        if (!opened(group.getId(), this.getClass())) {
            throw new NoRunException("未开启");
        }
    }

    @Action(value = "捡漂流瓶", otherName = {"捡瓶子"})
    public String getBottle() {
        PickupABottle pab = null;
        try {
            pab = apiIyk0.pickupBottle(2);
            StringBuilder sb = new StringBuilder();
            sb.append("你捡到一个瓶子\n它来自QQ群:").append(pab.getData().getGroup())
                    .append("\n的:").append(pab.getData().getUin())
                    .append("\n在:").append(pab.getData().getTime())
                    .append("\n写的:").append(pab.getData().getMsg());
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "没捡到瓶子...";
        }
    }

    @AutoStand
    ApiIyk0 apiIyk0;

    @Action(value = "扔漂流瓶<.+=>str>", otherName = {"扔瓶子<.+=>str>"})
    public String setBottle(long q, Group group, @Param("str") String str) {
        if (str == null || str.trim().isEmpty()) return "请携带内容~";
        try {
            ThrowABottle throwABottle = apiIyk0.throwBottle(1,
                    str, q, group.getId());
            return throwABottle.getData().getMsg();
        } catch (Exception e) {
            e.printStackTrace();
            return "扔瓶子失败,大海不允许有敏感词汇的瓶子飘向远方";
        }

    }

    @Action("随机头像")
    public String sjtx() {
        try {
            return Tool.pathToImg(muXiaoGuo.getSjtx("pc").getData().getImgurl());
        } catch (Exception e) {
            e.printStackTrace();
            return "获取失败";
        }
    }

    @AutoStand
    MuXiaoGuo muXiaoGuo;

    @Action("百科<.+=>str>")
    public String m1(@Param("str") String name) {
        try {
            BaiKe baiKe = muXiaoGuo.getBaiKe("Baidu", name);
            return baiKe.getData().getContent() + "\n相关图片:" + Tool.pathToImg(baiKe.getData().getImgUrl());
        } catch (Exception e) {
            return "百科中没有找到相关资料";
        }
    }

    @AutoStand
    GetPvpQQ getpvpqq;

    @Action(value = "王者荣耀最新公告.*", otherName = {"王者公告.*"})
    public Object m3(Group group, @AllMess String str) throws Exception {
        Response0 r0 = getPvpNews.m1();
        Message message;
        String numStr = findNumberFromString(str);
        int st = 0;
        if (numStr != null && !numStr.trim().isEmpty()) {
            int n = Integer.parseInt(numStr);
            if (r0.getData().getItems().length > n) {
                st = n;
            }
        }
        long newsId = r0.getData().getItems()[st].getINewsId().longValue();
        message = getPvpNews.getNews("王者荣耀更新公告\n", newsId, group.getId());
        return message;
    }

    @AutoStand
    GetPvpNews getPvpNews;

    @AutoStand
    MihoyoP0 mihoyoP0;

    @Action(value = "原神最新公告.*", otherName = {"原神公告.*"})
    public Object m4(Group group, @AllMess String str) throws Exception {
        io.github.kloping.mirai0.Entitys.apiEntitys.mihoyoYuanshen.Data data = mihoyoP0.getNews().getData()[0];
        String numStr = findNumberFromString(str);
        int st = 0;
        if (numStr != null && !numStr.trim().isEmpty()) {
            int n = Integer.parseInt(numStr);
            if (data.getMainList().length > n) {
                st = n;
            }
        }
        String cid = data.getMainList()[st].getContentId();
        String[] sss = mihoyoP0.getNews(cid.substring(1, cid.length() - 1));
        if (!sss[0].startsWith("http")) {
            sss[0] = "https://ys.mihoyo.com" + sss[0];
        }
        return Tool.pathToImg(sss[0]) + "\n" + sss[1] + "\n===========\n" + sss[2];
    }

    @Action("催更")
    public String cg() {
        return "<At:" + superQL + ">\n\n催更新,问题反馈,需要新功能,可以开issue(如果会的话\nhttps://github.com/gdpl2112/mirai-bot-first/issues/new\n或\nhttps://hub.fastgit.org/gdpl2112/mirai-bot-first/issues/new";
    }

    private static final String KFJH =
            "开发计划请见\nhttps://github.com/gdpl2112/mirai-bot-first/milestones\n或\nhttps://hub.fastgit.org/gdpl2112/mirai-bot-first/milestones因为DNS污染可能某些时间段无法访问";

    @Action("开发计划")
    public String kfjh() {
        return KFJH;
    }

    @Action("/init_pvp")
    public String p() {
        pvpQq.m1();
        return "ok";
    }

    @AutoStand
    PvpQq pvpQq;

    @Action("王者语音.+")
    public String m0(@AllMess String a, Group group) {
        String numStr = findNumberFromString(a);
        int i = 0;
        try {
            i = Integer.parseInt(numStr);
        } catch (Exception e) {
        }
        a = a.replace(numStr, "");
        a = a.replaceFirst("王者语音", "");
        Yy_4e[] yy4es = pvpQq.getY4e(a);
        if (yy4es == null) return "未发现相关英雄";
        Yy_4e yy4e = yy4es[0];
        if (yy4es.length > i) {
            yy4e = yy4es[i];
        }
        MessageTools.sendVoiceMessageInGroup("http:" + yy4e.getYyyp_9a(), group.getId());
        return "&" + yy4e.getYywa1_f2();
    }

    @Action("王者图片.+")
    public String pvpQQpic(@AllMess String a, Group group) {
        a = a.replaceFirst("王者图片", "");
        Data data = pvpQq.getD(a);
        return Tool.pathToImg("http:" + data.getHeroimg()) + "\n相关链接 " + data.getInfourl();
    }

    @AutoStand
    Project.interfaces.http_api.PvpQq pvpQqi;

    public static final int PAGE_SIZE = 5;
    public PvpSkin upPS = null;

    @Action("王者最新皮肤.*?")
    public Object pvpQQSkin(@AllMess String m) {
        PvpSkin pvpSkin = upPS == null ? pvpQqi.getSkins() : upPS;
        upPS = pvpSkin;
        Integer i = getInteagerFromStr(m);
        i = i == null || i >= (pvpSkin.getPcblzlby_c6().length / 5) ? 0 : i;
        StringBuilder sb = new StringBuilder();
        int[] ints = {i * PAGE_SIZE, i * PAGE_SIZE + 1, i * PAGE_SIZE + 2, i * PAGE_SIZE + 3, i * PAGE_SIZE + 4};
        for (int i1 : ints) {
            Pcblzlby_c6 pcblzlby_c6 = pvpSkin.getPcblzlby_c6()[i1];
            sb.append("皮肤名:").append(pcblzlby_c6.getPcblzlbybt_d3()).append(NEWLINE)
                    .append("预览图:").append(NEWLINE).append(Tool.pathToImg("https:" + pcblzlby_c6.getPcblzlbydt_8b()))
                    .append(NEWLINE).append("相关链接:").append(pcblzlby_c6.getPcblzlbyxqydz_c4().substring(2))
                    .append(NEWLINE).append(SPLIT_LINE_0).append(NEWLINE);
        }
        return sb.toString();
    }

    @Action("王者皮肤.*?")
    public Object pvpQQSkin0(@AllMess String m) {
        PvpSkin pvpSkin = upPS == null ? pvpQqi.getSkins() : upPS;
        upPS = pvpSkin;
        String name = m.replace("王者皮肤", "");
        Integer i = getInteagerFromStr(m);
        i = i == null || i >= (pvpSkin.getPcblzlby_c6().length / 5) ? 0 : i;
        Pcblzlby_c6 pcblzlby_c6 = pvpSkin.getPcblzlby_c6()[i];
        return pvpQq.getSkinPic("https:" + pcblzlby_c6.getPcblzlbyxqydz_c4());
    }
}
