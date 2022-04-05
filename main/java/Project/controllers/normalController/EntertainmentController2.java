package Project.controllers.normalController;

import Project.detailPlugin.GetPvpNews;
import Project.detailPlugin.MihoyoP0;
import Project.detailPlugin.PvpQq;
import Project.interfaces.http_api.ApiIyk0;
import Project.interfaces.http_api.GetPvpQQ;
import Project.interfaces.http_api.MuXiaoGuo;
import com.alibaba.fastjson.JSONObject;
import io.github.kloping.MySpringTool.annotations.*;
import io.github.kloping.MySpringTool.exceptions.NoRunException;
import io.github.kloping.mirai0.Main.ITools.MessageTools;
import io.github.kloping.mirai0.commons.Group;
import io.github.kloping.mirai0.commons.apiEntitys.apiIyk0.YiQing;
import io.github.kloping.mirai0.commons.apiEntitys.baiKe.BaiKe;
import io.github.kloping.mirai0.commons.apiEntitys.pvpQQH0.Data;
import io.github.kloping.mirai0.commons.apiEntitys.pvpQQVoice.Yy_4e;
import io.github.kloping.mirai0.commons.apiEntitys.pvpQqCom.Response0;
import io.github.kloping.mirai0.commons.apiEntitys.pvpSkin.Pcblzlby_c6;
import io.github.kloping.mirai0.commons.apiEntitys.pvpSkin.PvpSkin;
import io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet;
import io.github.kloping.mirai0.unitls.Tools.Tool;
import net.mamoe.mirai.message.data.Message;

import static Project.controllers.auto.ControllerTool.opened;
import static io.github.kloping.mirai0.Main.Resource.println;
import static io.github.kloping.mirai0.Main.Resource.superQL;
import static io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet.FinalNormalString.GET_FAILED;
import static io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet.FinalString.*;
import static io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet.FinalValue.NOT_OPEN_NO_RUN_EXCEPTION;
import static io.github.kloping.mirai0.unitls.Tools.Tool.findNumberFromString;
import static io.github.kloping.mirai0.unitls.Tools.Tool.getInteagerFromStr;

/**
 * @author github-kloping
 */
@Controller
public class EntertainmentController2 {
    public static final int PAGE_SIZE = 5;
    private static final String KFJH =
            "开发计划请见\nhttps://github.com/gdpl2112/mirai-bot-first/milestones\n或\nhttps://hub.fastgit.org/gdpl2112/mirai-bot-first/milestones因为DNS污染可能某些时间段无法访问";
    public static long upNewsId = 0;
    public PvpSkin upPS = null;
    @AutoStand
    ApiIyk0 apiIyk0;
    @AutoStand
    MuXiaoGuo muXiaoGuo;
    @AutoStand
    GetPvpQQ getpvpqq;
    @AutoStand
    GetPvpNews getPvpNews;
    @AutoStand
    MihoyoP0 mihoyoP0;
    @AutoStand
    PvpQq pvpQq;
    @AutoStand
    Project.interfaces.http_api.PvpQq pvpQqi;

    public EntertainmentController2() {
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

    private static final String[] SJTX_PARMS = {"女", "男", "动漫", "情侣"};

    @Action("随机头像")
    public String sjtx0() {
        try {
            JSONObject jo = apiIyk0.sjtx(SJTX_PARMS[2]);
            return Tool.pathToImg(jo.getString("img"));
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

    @Action("百科<.+=>str>")
    public String m1(@Param("str") String name) {
        try {
            BaiKe baiKe = muXiaoGuo.getBaiKe("Baidu", name);
            return baiKe.getData().getContent() + "\n相关图片:" + Tool.pathToImg(baiKe.getData().getImgUrl());
        } catch (Exception e) {
            return "百科中没有找到相关资料";
        }
    }

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

    @Action(value = "原神最新公告.*", otherName = {"原神公告.*"})
    public Object m4(Group group, @AllMess String str) throws Exception {
        io.github.kloping.mirai0.commons.apiEntitys.mihoyoYuanshen.Data data = mihoyoP0.getNews().getData()[0];
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

    @Action("开发计划")
    public String kfjh() {
        return KFJH;
    }

    @Action("/init_pvp")
    public String p() {
        pvpQq.m1();
        return "ok";
    }

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

    @Action("疫情<.+=>name>")
    public String yq0(@Param("name") String address) {
        YiQing qing = apiIyk0.yq(address);
        if (qing == null) {
            return ERR_TIPS;
        }
        // "目前确诊": "1792",
        //    "死亡人数": "9",
        //    "治愈人数": "1723",
        //    "新增确诊": "11",
        //    "现存确诊": "60",
        //    "现存无症状": "463",
        StringBuilder sb = new StringBuilder()
                .append("地区:").append(qing.get查询地区()).append(NEWLINE)
                .append("时间:").append(qing.getTime()).append(NEWLINE)
                .append("新增确诊:").append(qing.get新增确诊()).append(NEWLINE)
                .append("目前确诊:").append(qing.get目前确诊()).append(NEWLINE)
                .append("现存确诊:").append(qing.get现存确诊()).append(NEWLINE)
                .append("现存无症状:").append(qing.get现存无症状()).append(NEWLINE)
                .append("治愈人数:").append(qing.get治愈人数()).append(NEWLINE)
                .append("死亡人数:").append(qing.get死亡人数()).append(NEWLINE)
                ;

        return sb.toString();
    }
}
