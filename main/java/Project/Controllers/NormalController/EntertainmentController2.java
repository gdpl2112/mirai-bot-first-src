package Project.Controllers.NormalController;

import Entitys.Group;
import Entitys.User;
import Entitys.apiEntitys.baiKe.BaiKe;
import Entitys.apiEntitys.colb.PickupABottle;
import Entitys.apiEntitys.pvpQQH0.Data;
import Entitys.apiEntitys.pvpQQVoice.Yy_4e;
import Entitys.apiEntitys.pvpQqCom.Response0;
import Entitys.apiEntitys.thb.ThrowABottle;
import Project.Controllers.FirstController;
import Project.Plugins.GetPvpNews;
import Project.Plugins.Mihoyo;
import Project.Plugins.PvpQq;
import Project.Tools.Tool;
import Project.drawers.GameDrawer;
import Project.drawers.entity.GameMap;
import io.github.kloping.Mirai.Main.ITools.MessageTools;
import io.github.kloping.Mirai.Main.Resource;
import io.github.kloping.MySpringTool.annotations.*;
import io.github.kloping.MySpringTool.exceptions.NoRunException;
import net.mamoe.mirai.message.data.Message;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static Project.Controllers.ControllerTool.CanGroup;
import static Project.Tools.Tool.findNumberFromString;
import static io.github.kloping.Mirai.Main.Resource.Switch.AllK;
import static io.github.kloping.Mirai.Main.Resource.println;
import static io.github.kloping.Mirai.Main.Resource.superQL;

@Controller
public class EntertainmentController2 {
    public EntertainmentController2() {
        println(this.getClass().getSimpleName() + "构建");
        Resource.StartOkRuns.add(this::startTips);
    }

    public static long upNewsId = 0;

    private void startTips() {
//        Set<Long> sets = new LinkedHashSet<>();
//        if (bot.getGroups().contains(278681553L)) sets.add(278681553L);
//        else if (bot.getGroups().contains(759590727L)) sets.add(759590727L);
//        else if (bot.getGroups().contains(794238572L)) sets.add(794238572L);
//        if (sets.size() > 0) {
//            String s1 = DataBase.getString("upNewsId");
//            if (s1 != null && !s1.trim().isEmpty()) {
//                upNewsId = Long.parseLong(s1.trim());
//            }
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    try {
//                        Response0 r0 = GetPvpNews.m1(FirstController.getPvpQQ);
//                        long newsId = r0.getData().getItems()[0].getINewsId().longValue();
//                        if (upNewsId < newsId) {
//                            Message message = GetPvpNews.getNews("王者荣耀更新公告\n", newsId, sets.iterator().next());
//                            upNewsId = newsId;
//                            DataBase.setString(upNewsId, "upNewsId");
//                            bot.getGroup(sets.iterator().next()).sendMessage(message);
//                        }
//                        int r = Tool.rand.nextInt(30) + 30;
//                        Thread.sleep(1000 * 60 * r);
//                        run();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            }).start();
//        }
    }

    @Before
    public void before(Group group) throws NoRunException {
        if (!AllK)
            throw new NoRunException();
        if (!CanGroup(group.getId())) {
            throw new NoRunException();
        }
    }


    @Action("/testMap<.+=>str>")
    public String t1(@Param("str") String str, User user) throws IOException {
        String[] ss = str.split("-");
        Map<String, Integer> maps = new HashMap<>();
        for (String s : ss) {
            if (s.trim().isEmpty() || !s.contains("=")) continue;
            try {
                String[] s2 = s.split("=");
                maps.put(s2[0], Integer.valueOf(s2[1]));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        int w = maps.containsKey("w") ? maps.get("w") : 10;
        int h = maps.containsKey("h") ? maps.get("h") : 6;
        int x = maps.containsKey("x") ? maps.get("x") : 1;
        int y = maps.containsKey("y") ? maps.get("y") : 1;
        GameMap.GameMapBuilder builder = new GameMap.GameMapBuilder()
                .setWidth(w)
                .setHeight(h)
                .append(x, y, "https://q1.qlogo.cn/g?b=qq&nk=" + user.getId() + "&s=640");
        return Tool.pathToImg(GameDrawer.drawerMap(builder.build()));
    }

    @Action(value = "捡漂流瓶", otherName = {"捡瓶子"})
    public String getBottle() {
        PickupABottle pab = null;
        try {
            pab = FirstController.apiIyk0.pickupABottle(2);
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

    @Action(value = "扔漂流瓶<.+=>str>", otherName = {"扔瓶子<.+=>str>"})
    public String setBottle(long q, Group group, @Param("str") String str) {
        if (str == null || str.trim().isEmpty()) return "请携带内容~";
        try {
            ThrowABottle throwABottle = FirstController.apiIyk0.throwABottle(1,
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
            return Tool.pathToImg(FirstController.muXiaoGuo.getSjtx("pc").getData().getImgurl());
        } catch (Exception e) {
            e.printStackTrace();
            return "获取失败";
        }
    }

    @Action("百科<.+=>str>")
    public String m1(@Param("str") String name) {
        try {
            BaiKe baiKe = FirstController.muXiaoGuo.getBaiKe("Baidu", name);
            return baiKe.getData().getContent() + "\n相关图片:" + Tool.pathToImg(baiKe.getData().getImgUrl());
        } catch (Exception e) {
            e.printStackTrace();
            return "百科中没有找到相关资料";
        }
    }

    @Action(value = "王者荣耀最新公告.*", otherName = {"王者公告.*"})
    public Object m3(Group group, @AllMess String str) throws Exception {
        Response0 r0 = GetPvpNews.m1(FirstController.getPvpQQ);
        Message message;
        String numStr = findNumberFromString(str);
        int st = 0;
        if (numStr != null && !numStr.trim().isEmpty()) {
            int n = Integer.parseInt(numStr);
            if (r0.getData().getItems().length > n)
                st = n;
        }
        long newsId = r0.getData().getItems()[st].getINewsId().longValue();
        message = GetPvpNews.getNews("王者荣耀更新公告\n", newsId, group.getId());
        return message;
    }

    @Action(value = "原神最新公告.*", otherName = {"原神公告.*"})
    public Object m4(Group group, @AllMess String str) throws Exception {
        Entitys.apiEntitys.mihoyoYuanshen.Data data = Mihoyo.getNews().getData()[0];
        String numStr = findNumberFromString(str);
        int st = 0;
        if (numStr != null && !numStr.trim().isEmpty()) {
            int n = Integer.parseInt(numStr);
            if (data.getMainList().length > n)
                st = n;
        }
        String cid = data.getMainList()[st].getContentId();
        String[] sss = Mihoyo.getNews(cid.substring(1, cid.length() - 1));
        return Tool.pathToImg(sss[0]) + "\n" + sss[1] + "\n===========\n" + sss[2];
    }

    @Action("催更")
    public String cg() {
        return "<At:" + superQL + ">\n\n催更新,问题反馈,需要新功能,可以开issue\nhttps://github.com/gdpl2112/mirai-bot-first/issues/new";
    }

    @Action("开发计划")
    public String kfjh() {
        return "开发计划请见\nhttps://github.com/gdpl2112/mirai-bot-first/milestones\n因为DNS污染可能某些时间段无法访问";
    }

    @Action("/init_pvp")
    public String p() {
        PvpQq.m1();
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
        Yy_4e[] yy4es = PvpQq.getY4e(a);
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
        Data data = PvpQq.getD(a);
        return Tool.pathToImg("http:" + data.getHeroimg()) + "\n相关链接 " + data.getInfourl();
    }
}
