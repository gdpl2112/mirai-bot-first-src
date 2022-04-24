
package Project.controllers.normalController;

import Project.detailPlugin.*;
import Project.interfaces.Iservice.IOtherService;
import Project.interfaces.http_api.GetPvpQQ;
import Project.interfaces.http_api.MuXiaoGuo;
import io.github.kloping.MySpringTool.annotations.*;
import io.github.kloping.MySpringTool.exceptions.NoRunException;
import io.github.kloping.mirai0.Main.ITools.MessageTools;
import io.github.kloping.mirai0.Main.Resource;
import io.github.kloping.mirai0.commons.Group;
import io.github.kloping.mirai0.commons.apiEntitys.pvpQQH0.Data;
import io.github.kloping.mirai0.commons.apiEntitys.pvpQQVoice.Yy_4e;
import io.github.kloping.mirai0.commons.apiEntitys.pvpQqCom.Response0;
import io.github.kloping.mirai0.commons.apiEntitys.pvpSkin.Pcblzlby_c6;
import io.github.kloping.mirai0.commons.apiEntitys.pvpSkin.PvpSkin;
import io.github.kloping.mirai0.unitls.Tools.Tool;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.Message;
import net.mamoe.mirai.message.data.MessageChainBuilder;

import static Project.controllers.auto.ControllerTool.opened;
import static Project.dataBases.DataBase.getConf;
import static io.github.kloping.mirai0.Main.ITools.MessageTools.speak;
import static io.github.kloping.mirai0.Main.Resource.println;
import static io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet.FinalString.NEWLINE;
import static io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet.FinalString.SPLIT_LINE_0;
import static io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet.FinalValue.NOT_OPEN_NO_RUN_EXCEPTION;
import static io.github.kloping.mirai0.unitls.Tools.Tool.findNumberFromString;
import static io.github.kloping.mirai0.unitls.Tools.Tool.getInteagerFromStr;

/**
 * @author github-kloping
 */
@Controller
public class CallLocalApiController {

    public CallLocalApiController() {
        println(this.getClass().getSimpleName() + "构建");
    }

    @Before
    public void before(Group group) throws NoRunException {
        if (!opened(group.getId(), this.getClass())) {
            throw NOT_OPEN_NO_RUN_EXCEPTION;
        }
    }

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
    @AutoStand
    BaiduBaiKe baiduBaiKe;
    @AutoStand
    WeatherGetter weatherGetter;
    @AutoStand
    IOtherService otherService;

   public PvpSkin skin = null;
    public static final int PAGE_SIZE = 5;
    public static long upNewsId = 0;

    @Action("百科<.+=>str>")
    public Object m1(@Param("str") String name) {
        try {
            return baiduBaiKe.getBaiKe(name);
        } catch (Exception e) {
            e.printStackTrace();
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
    public String pvpQqPic(@AllMess String a, Group group) {
        a = a.replaceFirst("王者图片", "");
        Data data = pvpQq.getD(a);
        return Tool.pathToImg("http:" + data.getHeroimg()) + "\n相关链接 " + data.getInfourl();
    }

    @Action("王者最新皮肤.*?")
    public Object pvpQqSkin(@AllMess String m) {
        PvpSkin pvpSkin = skin == null ? pvpQqi.getSkins() : skin;
        skin = pvpSkin;
        Integer i = getInteagerFromStr(m);
        i = i == null || i >= (pvpSkin.getPcblzlby_c6().length / 5) ? 0 : i;
        StringBuilder sb = new StringBuilder();
        int[] ints = {i * PAGE_SIZE, i * PAGE_SIZE + 1, i * PAGE_SIZE + 2, i * PAGE_SIZE + 3, i * PAGE_SIZE + 4};
        for (int i1 : ints) {
            Pcblzlby_c6 c6 = pvpSkin.getPcblzlby_c6()[i1];
            sb.append("皮肤名:").append(c6.getPcblzlbybt_d3()).append(NEWLINE)
                    .append("预览图:").append(NEWLINE).append(Tool.pathToImg("https:" + c6.getPcblzlbydt_8b()))
                    .append(NEWLINE).append("相关链接:").append(c6.getPcblzlbyxqydz_c4().substring(2))
                    .append(NEWLINE).append(SPLIT_LINE_0).append(NEWLINE);
        }
        return sb.toString();
    }

    @Action("王者皮肤.*?")
    public Object pvpQqSkin0(@AllMess String m) {
        PvpSkin pvpSkin = skin == null ? pvpQqi.getSkins() : skin;
        skin = pvpSkin;
        String name = m.replace("王者皮肤", "");
        Integer i = getInteagerFromStr(m);
        i = i == null || i >= (pvpSkin.getPcblzlby_c6().length / 5) ? 0 : i;
        Pcblzlby_c6 c6 = pvpSkin.getPcblzlby_c6()[i];
        return pvpQq.getSkinPic("https:" + c6.getPcblzlbyxqydz_c4());
    }

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
}
