package Project.controllers.plugins;

import Project.broadcast.PicBroadcast;
import Project.commons.SpGroup;
import Project.commons.apiEntitys.baiduShitu.BaiduShitu;
import Project.commons.apiEntitys.baiduShitu.response.BaiduShituResponse;
import Project.commons.apiEntitys.baiduShitu.response.List;
import Project.interfaces.httpApi.IBaiduShitu;
import Project.interfaces.httpApi.KlopingWeb;
import Project.plugins.BaiduShituDetail;
import Project.plugins.PvpQq;
import Project.plugins.entities.GloryList;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.github.kloping.MySpringTool.annotations.*;
import io.github.kloping.MySpringTool.exceptions.NoRunException;
import io.github.kloping.judge.Judge;
import io.github.kloping.mirai0.Main.iutils.MessageUtils;
import io.github.kloping.mirai0.unitls.Tools.Tool;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.message.data.ForwardMessageBuilder;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;

import static Project.commons.rt.ResourceSet.FinalNormalString.EMPTY_STR;
import static Project.commons.rt.ResourceSet.FinalString.NEWLINE;
import static Project.commons.rt.ResourceSet.FinalValue.NOT_OPEN_NO_RUN_EXCEPTION;
import static Project.controllers.auto.ControllerTool.opened;
import static Project.plugins.All.getTitle;
import static io.github.kloping.mirai0.Main.BootstarpResource.BOT;
import static io.github.kloping.mirai0.Main.BootstarpResource.THREADS;

/**
 * @author github.kloping
 */
@Controller
public class ExternalController {

    @Before
    public void before(SpGroup group) throws NoRunException {
        if (!opened(group.getId(), this.getClass())) {
            throw NOT_OPEN_NO_RUN_EXCEPTION;
        }
    }

    @AutoStand
    KlopingWeb klopingWeb;

    //=================================
    private Long up_select_time = -1L;
    private String V = "v";
    private String IOS = "i";

    @Action("荣耀查询<.*?=>name>")
    public Object select0(@Param("name") String name, SpGroup group) {
        if (Judge.isEmpty(name)) {
            return "荣耀查询(i,v)(地区)(英雄名)\ni代表ios区,v代表微信;默认安卓,Q区";
        }
        Integer id = -1;
        String hname = "";
        for (String s : PvpQq.NAME2ID.keySet()) {
            if (name.contains(s)) {
                id = PvpQq.NAME2ID.get(s);
                hname = s;
                name = name.replace(s, EMPTY_STR);
            }
        }
        // q=1 i=1  v=3
        //# 1 q 2 iq 3 v 4 iv
        Integer aid = 1;
        if (name.contains(IOS)) {
            aid++;
            name = name.replace(IOS, "");
        }
        if (name.contains(V)) {
            aid += 2;
            name = name.replace(V, "");
        }
        if (id < 0) return "未发现相关英雄";
        else {
            Integer code = null;
            String cname = null;
            if (name.trim().isEmpty()) {
                cname = "全国";
                code = 1561;
            } else {
                JSONObject jo = klopingWeb.acode(name);
                if (jo == null) return "区域错误";
                code = jo.getInteger("code");
                cname = jo.get("cName").toString();
            }
            if (up_select_time + 120000 > System.currentTimeMillis()) return "请控制在两分钟最多查询一次";
            try {
                GloryList gloryList = gloryList(aid, id, code);
                if (gloryList == null) return "获取失败";
                up_select_time = System.currentTimeMillis();
                String finalHname = hname;
                String finalCname = cname;
                Integer finalAid = aid;
                THREADS.submit(() -> {
                    Group gp = BOT.getGroup(group.getId());
                    try {
                        Long t0 = Long.valueOf(gloryList.getData().getUpdateTime() + "000");
                        StringBuilder sb = new StringBuilder();
                        sb.append("更新时间:").append(Tool.INSTANCE.DF_MDHM.format(new Date())).append("\n");
                        sb.append("当前分区: ");
                        switch (finalAid) {
                            case 1:
                                sb.append("安卓Q区");
                                break;
                            case 2:
                                sb.append("苹果Q区");
                                break;
                            case 3:
                                sb.append("安卓V区");
                                break;
                            case 4:
                                sb.append("苹果V区");
                                break;
                        }
                        gp.sendMessage(sb.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    ForwardMessageBuilder fb = new ForwardMessageBuilder(gp);
                    for (Project.plugins.entities.List data : gloryList.getData().getList()) {
                        MessageChainBuilder mb = new MessageChainBuilder();
                        mb.append(finalCname).append("第").append(Tool.INSTANCE.trans(data.getRankNo().intValue())).append(finalHname).append("\n");
                        mb.append("战力: ").append(data.getRankValue()).append("\n");
                        mb.append("段位: ").append(data.getRoleJobName()).append("\n");
                        mb.append("游戏昵称: ").append(data.getRoleName()).append("\n");
                        if (!data.getIsHide()) {
                            try {
                                Image image = Contact.uploadImage(gp, new URL(data.getRoleIcon()).openStream());
                                mb.append(image);
                            } catch (IOException e) {
                                mb.append("[图片加载失败]");
                            }
                        }
                        fb.add(BOT.getBot(), mb.build());
                        if (fb.size() >= 35) {
                            gp.sendMessage(fb.build());
                            fb.clear();
                        }
                    }
                    if (!fb.isEmpty()) {
                        gp.sendMessage(fb.build());
                    }
                });
                return null;
            } catch (Exception e) {
                return e.getMessage();
            }
        }
    }

    public static GloryList gloryList(Integer aid, Integer hid, Integer adcode) throws Exception {
        Connection connection = Jsoup.connect("https://kohcamp.qq.com/honor/ranklist").ignoreHttpErrors(true).ignoreContentType(true);
        connection.header("cChannelId", "10003391");
        connection.header("cClientVersionCode", "2037850907");
        connection.header("cClientVersionName", "7.83.0419");
        connection.header("cCurrentGameId", "20001");
        connection.header("cGameId", "20001");
        connection.header("cGzip", "1");
        connection.header("cIsArm64", "true");
        connection.header("cRand", "1684141342112");
        connection.header("cSupportArm64", "true");
        connection.header("cSystem", "android");
        connection.header("cSystemVersionCode", "31");
        connection.header("cSystemVersionName", "12");
        connection.header("cpuHardware", "qcom");
        connection.header("encodeParam", "IfdpcXDm1tFqxzKdTqSw%2B2GHLvao1QsgBdvlTtfojAraG3XnKF%2B%2FfNO0KYSfdYzRKuxRTnJIXjnPdLXB7nd0rYvjXQTfRA1JmzBht7RNK9Kh68JoiXwg2mX57kwQSPpGT%2B%2FdNw%3D%3D");
        connection.header("gameAreaId", "1");
        connection.header("gameId", "20001");
        connection.header("gameOpenId", "AC7053418F9B0542483CB7E4AEAC4F21");
        connection.header("gameRoleId", "1662968996");
        connection.header("gameServerId", "1040");
        connection.header("gameUserSex", "1");
        connection.header("openId", "D2559826E96D5C3B6BE039F346219E25");
        connection.header("tinkerId", "2037850907_64_0");
        connection.header("token", "WFdVcXAx");
        connection.header("userId", "534469328");
        connection.header("NOENCRYPT", "1");
        connection.header("X-Client-Proto", "https");
        connection.header("Content-Type", "application/json; charset=UTF-8");
        connection.header("Host", "kohcamp.qq.com");
        connection.header("Connection", "Keep-Alive");
        connection.header("Accept-Encoding", "gzip");
        connection.header("User-Agent", "okhttp/4.9.1");
        connection.requestBody("{\"recommendPrivacy\":0,\"areaId\":\"" + aid + "\",\"adcode\":" + adcode + ",\"roleId\":\"1662968996\",\"heroId\":" + hid + "}");
        Document doc = connection.post();
        return JSON.parseObject(doc.body().text(), GloryList.class);
    }

    //==================

    @AutoStand
    IBaiduShitu iBaiduShitu;

    @Action("/搜图.+")
    public Object searchPic(@AllMess String mess, SpGroup group, long q1) throws InterruptedException {
        net.mamoe.mirai.contact.Group g = BOT.getGroup(group.getId());
        Long q = Project.utils.Utils.getAtFromString(mess);
        String urlStr = null;
        if (q == -1) {
            urlStr = MessageUtils.INSTANCE.getImageUrlFromMessageString(mess);
            mess = mess.replace(MessageUtils.INSTANCE.getImageIdFromMessageString(mess), "");
            if (urlStr == null) {
                MessageUtils.INSTANCE.sendMessageInGroup("请在发送要搜索的图片", group.getId());
                PicBroadcast.INSTANCE.add(new PicBroadcast.PicReceiverOnce() {
                    @Override
                    public Object onReceive(long qid, long gid, String pic, Object[] objects) {
                        if (q1 == qid) {
                            String urlStr = null;
                            urlStr = MessageUtils.INSTANCE.getImageUrlFromMessageString(pic);
                            int i = 6;
                            BaiduShitu baiduShitu = BaiduShituDetail.get(urlStr);
                            BaiduShituResponse response = iBaiduShitu.response(baiduShitu.getData().getSign());
                            Iterator<List> iterator = Arrays.asList(response.getData().getList()).iterator();
                            java.util.List<String> list = new LinkedList();
                            while (iterator.hasNext() && list.size() <= i) {
                                Project.commons.apiEntitys.baiduShitu.response.List e = iterator.next();
                                try {
                                    String title = getTitle(e.getFromUrl());
                                    list.add(Tool.INSTANCE.pathToImg(e.getThumbUrl()) + NEWLINE + "(" + title + ")" + NEWLINE + e.getFromUrl());
                                } catch (Throwable ex) {
                                }
                            }
                            MessageUtils.INSTANCE.sendMessageByForward(gid, list.toArray());
                            return "ok";
                        }
                        return null;
                    }
                });
            }
        } else {
            mess = mess.replace(q.toString(), "");
            urlStr = Tool.INSTANCE.getTouUrl(q);
            urlStr = Image.queryUrl(MessageUtils.INSTANCE.createImage(g, urlStr));
        }
        int i = 6;
        Integer i1 = Tool.INSTANCE.getInteagerFromStr(mess);
        i = i1 == null ? i : i1;
        BaiduShitu baiduShitu = BaiduShituDetail.get(urlStr);
        BaiduShituResponse response = iBaiduShitu.response(baiduShitu.getData().getSign());
        Iterator<Project.commons.apiEntitys.baiduShitu.response.List> iterator = Arrays.asList(response.getData().getList()).iterator();
        java.util.List<String> list = new LinkedList();
        while (iterator.hasNext() && list.size() <= i) {
            Project.commons.apiEntitys.baiduShitu.response.List e = iterator.next();
            try {
                String title = getTitle(e.getFromUrl());
                list.add(Tool.INSTANCE.pathToImg(e.getThumbUrl()) + NEWLINE + "(" + title + ")" + NEWLINE + e.getFromUrl());
            } catch (Throwable ex) {
            }
        }
        return list.toArray();
    }
    //============
}
