package Project.Controllers.NormalController;

import Entitys.Group;
import Entitys.User;
import Entitys.apiEntitys.colb.PickupABottle;
import Entitys.apiEntitys.thb.ThrowABottle;
import Project.Controllers.FirstController;
import Project.Tools.Tool;
import Project.drawers.GameDrawer;
import Project.drawers.entity.GameMap;
import io.github.kloping.Mirai.Main.Resource;
import io.github.kloping.MySpringTool.annotations.Action;
import io.github.kloping.MySpringTool.annotations.Before;
import io.github.kloping.MySpringTool.annotations.Controller;
import io.github.kloping.MySpringTool.annotations.Param;
import io.github.kloping.MySpringTool.exceptions.NoRunException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static Project.Controllers.ControllerTool.CanGroup;
import static io.github.kloping.Mirai.Main.Resource.Switch.AllK;
import static io.github.kloping.Mirai.Main.Resource.println;

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
        } catch (Exception e) {
            e.printStackTrace();
            return "没捡到瓶子...";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("你捡到一个瓶子\n它来自QQ群:").append(pab.getData().getGroup())
                .append("\n的").append(pab.getData().getId())
                .append("\n在").append(pab.getData().getTime())
                .append("\n写的:").append(pab.getData().getMsg());
        return sb.toString();
    }

    @Action(value = "扔漂流瓶<.+=>str>", otherName = {"扔瓶子<.+=>str>"})
    public String setBottle(long q, Group group, @Param("str") String str) {
        if (str == null || str.trim().isEmpty()) return "请携带内容~";
        ThrowABottle throwABottle = FirstController.apiIyk0.throwABottle(1,
                str, q, group);
        return throwABottle.getData().getMsg();
    }

}
