package Project.Controllers.NormalController;

import Entitys.Group;
import Entitys.apiEntitys.pvpQqCom.Response0;
import Project.Controllers.FirstController;
import Project.DataBases.DataBase;
import Project.Plugins.GetPvpNews;
import Project.Tools.Tool;
import io.github.kloping.Mirai.Main.Resource;
import io.github.kloping.MySpringTool.annotations.Before;
import io.github.kloping.MySpringTool.annotations.Controller;
import io.github.kloping.MySpringTool.exceptions.NoRunException;
import net.mamoe.mirai.message.data.Message;

import static Project.Controllers.ControllerTool.CanGroup;
import static io.github.kloping.Mirai.Main.Resource.Switch.AllK;
import static io.github.kloping.Mirai.Main.Resource.bot;
import static io.github.kloping.Mirai.Main.Resource.println;

@Controller
public class EntertainmentController2 {
    public EntertainmentController2() {
        println(this.getClass().getSimpleName() + "构建");
        Resource.StartOkRuns.add(this::startTips);
    }

    public static long upNewsId = 0;

    private void startTips() {
        long gid = bot.getGroups().contains(278681553L) ? 278681553L : bot.getGroups().contains(759590727L) ? 759590727L : -1L;
        if (gid != -1) {
            String s1 = DataBase.getString("upNewsId");
            if (s1 != null && !s1.trim().isEmpty()) {
                upNewsId = Long.parseLong(s1.trim());
            }
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Response0 r0 = GetPvpNews.m1(FirstController.getPvpQQ);
                        long newsId = r0.getData().getItems()[0].getINewsId().longValue();
                        if (upNewsId != newsId) {
                            Message message = GetPvpNews.getNews("王者荣耀更新公告\n", newsId, gid);
                            upNewsId = newsId;
                            DataBase.setString(upNewsId, "upNewsId");
                            bot.getGroup(gid).sendMessage(message);
                        }
                        int r = Tool.rand.nextInt(30) + 30;
                        Thread.sleep(1000 * 60 * r);
                        run();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    @Before
    public void before(Group group) throws NoRunException {
        if (!AllK)
            throw new NoRunException();
        if (!CanGroup(group.getId())) {
            throw new NoRunException();
        }
    }
}
