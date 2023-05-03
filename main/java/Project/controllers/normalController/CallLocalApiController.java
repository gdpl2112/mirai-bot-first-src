package Project.controllers.normalController;

import Project.commons.SpGroup;
import Project.commons.apiEntitys.pvpqq.pvpQQVoice.Dqpfyy5403;
import Project.commons.apiEntitys.pvpqq.pvpQQVoice.Yylbzt9132;
import Project.commons.apiEntitys.pvpqq.pvpQqCom.Response0;
import Project.commons.apiEntitys.pvpqq.pvpSkin.Pcblzlby_c6;
import Project.commons.apiEntitys.pvpqq.pvpSkin.PvpSkin;
import Project.controllers.plugins.PointPicController;
import Project.interfaces.httpApi.KlopingWeb;
import Project.interfaces.httpApi.QZone;
import Project.interfaces.httpApi.XiaoaPi;
import Project.plugins.*;
import com.alibaba.fastjson.JSONObject;
import io.github.kloping.MySpringTool.annotations.*;
import io.github.kloping.MySpringTool.exceptions.NoRunException;
import io.github.kloping.date.DateUtils;
import io.github.kloping.mirai.BotInstance;
import io.github.kloping.mirai0.Main.iutils.MessageUtils;
import io.github.kloping.mirai0.unitls.Tools.Tool;
import io.github.kloping.mirai0.unitls.drawers.ImageDrawerUtils;
import net.mamoe.mirai.internal.utils.ExternalResourceImplByByteArray;
import net.mamoe.mirai.message.data.Message;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

import static Project.commons.rt.CommonSource.toStr;
import static Project.commons.rt.ResourceSet.FinalString.NEWLINE;
import static Project.commons.rt.ResourceSet.FinalString.SPLIT_LINE_0;
import static Project.commons.rt.ResourceSet.FinalValue.NOT_OPEN_NO_RUN_EXCEPTION;
import static Project.controllers.auto.ControllerTool.opened;
import static Project.dataBases.DataBase.getConf;
import static io.github.kloping.mirai0.Main.BootstarpResource.BOT;
import static io.github.kloping.mirai0.Main.BootstarpResource.println;

/**
 * @author github-kloping
 */
@Controller
public class CallLocalApiController {
    public static final int PAGE_SIZE = 5;
    public static final String WENDY_URL = "http://image.nmc.cn/product/%s/%s/%s/STFC/medium/SEVP_NMC_STFC_SFER_ET0_ACHN_L88_PB_%s%s%s%s0000000.jpg";
    private static final SimpleDateFormat SF_HH = new SimpleDateFormat("HH");
    public static long upNewsId = 0;
    public PvpSkin skin = null;
    @AutoStand
    GetPvpNews getPvpNews;
    @AutoStand
    MihoyoP0 mihoyoP0;
    @AutoStand
    PvpQq pvpQq;
    @AutoStand
    Project.interfaces.httpApi.PvpQq pvpQqi;
    @AutoStand
    BaiduBaiKe baiduBaiKe;
    @AutoStand
    WeatherGetter weatherGetter;
    @AutoStand
    QZone zone;
    @AutoStand
    KlopingWeb kloping;
    @AutoStand
    XiaoaPi xiaoaPi;

    public CallLocalApiController() {
        println(this.getClass().getSimpleName() + "构建");
    }

    @Before
    public void before(SpGroup group) throws NoRunException {
        if (!opened(group.getId(), this.getClass())) {
            throw NOT_OPEN_NO_RUN_EXCEPTION;
        }
    }

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
    public Object m3(SpGroup group, @AllMess String str) throws Exception {
        Response0 r0 = getPvpNews.m1();
        Message message;
        String numStr = Tool.INSTANCE.findNumberFromString(str);
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
    public Object m4(SpGroup group, @AllMess String str) throws Exception {
        Project.commons.apiEntitys.mihoyoYuanshen.Data data = mihoyoP0.getNews().getData()[0];
        String numStr = Tool.INSTANCE.findNumberFromString(str);
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
        return Tool.INSTANCE.pathToImg(sss[0]) + "\n" + sss[1] + "\n===========\n" + sss[2];
    }

    @Action("/init_pvp")
    public String p() {
        PvpQq.m1();
        return "ok";
    }

    @Action("王者语音.+")
    public String m0(@AllMess String a, SpGroup group) {
        String numStr = Tool.INSTANCE.findNumberFromString(a);
        int i = 0;
        try {
            i = Integer.parseInt(numStr);
        } catch (Exception e) {
        }
        a = a.replace(numStr, "");
        a = a.replaceFirst("王者语音", "");
        Dqpfyy5403 dq = pvpQq.getY4e(a);
        if (dq == null) {
            return "未发现相关英雄";
        }
        Yylbzt9132[] yys = dq.getYylbzt9132();
        Yylbzt9132 yy = yys[0];
        if (yys.length > i) {
            yy = yys[i];
        }
        MessageUtils.INSTANCE.sendVoiceMessageInGroup("http:" + yy.getYywjzt5304(), group.getId());
        return "&" + yy.getYywbzt1517();
    }

    @Action("王者最新皮肤.*?")
    public Object pvpQqSkin(@AllMess String m) {
        PvpSkin pvpSkin = skin == null ? pvpQqi.getSkins() : skin;
        skin = pvpSkin;
        Integer i = Tool.INSTANCE.getInteagerFromStr(m);
        i = i == null || i >= (pvpSkin.getPcblzlby_c6().length / 5) ? 0 : i;
        StringBuilder sb = new StringBuilder();
        int[] ints = {i * PAGE_SIZE, i * PAGE_SIZE + 1, i * PAGE_SIZE + 2, i * PAGE_SIZE + 3, i * PAGE_SIZE + 4};
        for (int i1 : ints) {
            Pcblzlby_c6 c6 = pvpSkin.getPcblzlby_c6()[i1];
            sb.append("皮肤名:").append(c6.getPcblzlbybt_d3()).append(NEWLINE).append("预览图:").append(NEWLINE).append(Tool.INSTANCE.pathToImg("https:" + c6.getPcblzlbydt_8b())).append(NEWLINE).append("相关链接:").append(c6.getPcblzlbyxqydz_c4().substring(2)).append(NEWLINE).append(SPLIT_LINE_0).append(NEWLINE);
        }
        return sb.toString();
    }

    @Action("王者皮肤.*?")
    public Object pvpQqSkin0(@AllMess String m) {
        PvpSkin pvpSkin = skin == null ? pvpQqi.getSkins() : skin;
        skin = pvpSkin;
        String name = m.replace("王者皮肤", "");
        Integer i = Tool.INSTANCE.getInteagerFromStr(m);
        i = i == null || i >= (pvpSkin.getPcblzlby_c6().length / 5) ? 0 : i;
        Pcblzlby_c6 c6 = pvpSkin.getPcblzlby_c6()[i];
        return pvpQq.getSkinPic("https:" + c6.getPcblzlbyxqydz_c4());
    }

    @Action("短时预报<.+=>address>")
    public String m2(@Param("address") String address) {
        return weatherGetter.get(address);
    }

    @Action("天气<.+=>name>")
    public String weather0(@Param("name") String name, SpGroup group) {
        String line = weatherGetter.detail(name);
        if (getConf(group.getId()).getVoiceK()) {
            BotInstance.getInstance().speak(line, group);
        }
        return line;
    }

    @Action("气温图")
    public Object gaowen() {
        String year = String.valueOf(io.github.kloping.date.DateUtils.getYear());
        String month = String.valueOf(DateUtils.getMonth());
        String day = String.valueOf(io.github.kloping.date.DateUtils.getDay());
        String hour0 = SF_HH.format(new Date());
        Integer h0 = Integer.parseInt(hour0);
        h0 -= 8;
        h0 = h0 < 0 ? 0 : h0;
        String hour = toStr(2, h0);
        month = toStr(2, Integer.parseInt(month));
        day = toStr(2, Integer.parseInt(day));
        List<String> list = new ArrayList<>();
        for (Integer i0 = 0; i0 < h0; i0++) {
            String url0 = String.format(WENDY_URL, year, month, day, year, month, day, toStr(2, i0));
            list.add(url0);
        }
        File outFile = new File("./temp/" + UUID.randomUUID() + "-gaoWen.gif");
        ImageDrawerUtils.image2giftIncrease(400, outFile, list.toArray(new String[0]));
        return Tool.INSTANCE.pathToImg(outFile.getAbsolutePath());
    }

    @Action("QQ空间")
    public Object qqZone(Long qid) {
        String pskey = kloping.get("qzone-pskey-930204019", "4432120");
        Map.Entry<String, String> uin = new AbstractMap.SimpleEntry<>("uin", "o930204019");
        Map.Entry<String, String> puin = new AbstractMap.SimpleEntry<>("p_uin", "o930204019");
        Map.Entry<String, String> p_skey = new AbstractMap.SimpleEntry<>("p_skey", pskey);
        String p0 = String.format("3_%s_0%%7C8_8_%s_0_1_0_0_1%%7C15%%7C16", qid, 930204019L);
        JSONObject o0 = zone.mainCgi(qid, null, p0, uin, puin, p_skey);
        Integer SS = o0.getJSONObject("data").getJSONObject("module_16").getJSONObject("data").getInteger("SS");
        Integer RZ = o0.getJSONObject("data").getJSONObject("module_16").getJSONObject("data").getInteger("RZ");
        Integer XC = o0.getJSONObject("data").getJSONObject("module_16").getJSONObject("data").getInteger("XC");
        Document doc0 = zone.feedds(qid, 930204019L, 5, uin, puin, p_skey);
        Elements es = doc0.getElementsByTag("ul");
        Set set = new LinkedHashSet();
        set.add("说说: " + SS);
        set.add("日志: " + RZ);
        set.add("相册: " + XC);
        set.add("最近一条空间");
        Elements e0 = es.get(0).getElementsByClass("f-item f-s-i");
        Element e1 = es.get(0).getElementsByClass("f-like-cnt").get(0);
        for (Element element : e0.get(0).children()) {
            Elements ess = element.getElementsByTag("img");
            if (ess.size() > 0) {
                for (Element e : ess) {
                    String href = e.attr("src");
                    set.add(Tool.INSTANCE.pathToImg(href));
                }
            } else {
                set.add(element.text());
            }
        }
        set.remove(null);
        set.remove("");
        return set.toArray();
    }

    @Action("解析图集音频<.+=>str>")
    public Object parseVoiceFromPics(@Param("str") String str) {
        String url = PointPicController.getUrl(str);
        return kloping.parsePic(url);
    }

    @Action("解析视频音频<.+=>str>")
    public Object parseVoiceFromV(SpGroup group, @Param("str") String str) throws Exception {
        String url = PointPicController.getUrl(str);
        JSONObject jo = xiaoaPi.parseV(url);
        String u0 = jo.getString("url");
        ByteArrayOutputStream baos = All.mp42mp3(new URL(u0).openStream());
        BOT.getGroup(group.getId()).getFiles().uploadNewFile("/音频解析-" + UUID.randomUUID() + ".mp3", new ExternalResourceImplByByteArray(baos.toByteArray(), "mp3"));
        MessageUtils.INSTANCE.sendVoiceMessageInGroup(baos.toByteArray(), group.getId());
        return null;
    }
}
