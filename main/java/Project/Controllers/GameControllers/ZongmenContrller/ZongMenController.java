package Project.Controllers.GameControllers.ZongmenContrller;


import Entitys.Group;
import Entitys.User;
import Project.Services.Iservice.IZongMenService;
import io.github.kloping.Mirai.Main.ITools.MessageTools;
import io.github.kloping.MySpringTool.annotations.*;
import io.github.kloping.MySpringTool.exceptions.NoRunException;

import static Project.Controllers.ControllerTool.CanGroup;
import static Project.Controllers.NormalController.ScoreController.longs;
import static io.github.kloping.Mirai.Main.Resource.Switch.AllK;
import static io.github.kloping.Mirai.Main.Resource.println;

@Controller
public class ZongMenController {

    public ZongMenController() {
        println(this.getClass().getSimpleName() + "构建");
    }

    @AutoStand
    IZongMenService zongMenService;

    @Before
    public void before(Group group) throws NoRunException {
        if (!AllK)
            throw new NoRunException();
        if (!CanGroup(group.getId())) {
            throw new NoRunException();
        }
    }

    private static String line = "1.创建宗门(名称)\n" +
            "2.宗门信息\n" +
            "3.宗门列表     #列出所有宗门\n" +
            "4.设置宗门图标(图片)\n" +
            "5.设置宗门名称(名称)\n" +
            "6.邀请(@at)  #邀请加入宗门\n" +
            "7.宗门人数    #查看宗门人数\n" +
            "8.宗门升级\n" +
            "9.退出宗门\n" +
            "10.设置长老(@)\n" +
            "11.取消长老(@)\n" +
            "12.移除成员(@)\n" +
            "宗门的作用请见'宗门作用'";

    @Action("宗门系统")
    public String Menu() {
        return line;
    }

    @Action("创建宗门<.+=>name>")
    public String Create(@Param("name") String name, User qq, Group group) {
        if (name == null || name.isEmpty() || name.equals("null"))
            return "名字 不可为空";
        if (longs.contains(qq.getId())) return "Can't";
        return zongMenService.create(name, qq.getId(), group);
    }

    @Action("宗门信息")
    public String Info(User qq, Group group) {
        return zongMenService.ZongInfo(qq.getId(), group);
    }

    @Action("宗门列表")
    public String List(Group g) {
        return zongMenService.List(g);
    }

    @Action("设置宗门图标<.+=>name>")
    public String setIcon(@AllMess String message, Group group, User qq) {
        String img = MessageTools.getImageUrlFromMessageString(message);
        if (img == null)
            return ("请携带图片");
        return zongMenService.setIcon(img, qq.getId(), group);
    }

    @Action("设置宗门名称<.+=>name>")
    public String setName(@Param("name") String name, Group group, User qq) {
        return zongMenService.setName(name, qq.getId(), group);
    }

    @Action("邀请.+")
    public Object Invite(@AllMess String mess, User qq, Group group) {
        long l1 = MessageTools.getAtFromString(mess);
        if (l1 < 0) return ("邀请谁");
        if (longs.contains(l1)) return "Can't";
        return zongMenService.Invite(qq.getId(), l1, group);
    }

    @Action("宗门人数")
    public String ListPer(User qq, Group group) {
        return zongMenService.ListPer(qq.getId(), group);
    }

    private static String line2 = "";

    static {
        StringBuilder sb = new StringBuilder();
        sb.append("一.宗门等级作用").append("\r\n\t")
                .append("1级,宗主每天能免费救援一名宗门内成员(即无状态时救援变有状态)").append("\r\n\t")
                .append("2级,宗主和长老都可救援").append("\r\n\t")
                .append("3级,经验共享,当宗门内成员猎杀魂兽获得经验时所有成员获得部分经验加成(随等级而每人加成不一样)").append("\r\n\t")
                .append("4级,经验共享加成增加").append("\r\n\t")
                .append("5级,宗门内所有成员每天共享5次救援机会").append("\r\n\t")
                .append("6级,宗门内所有长老及宗主每天多一次\"(请求)支援\"的次数,27人数").append("\r\n\t")
        ;
        sb.append("二.如何增加宗门经验").append("\r\n\t")
                .append("每4个小时成员可使用‘宗门贡献’来贡献与等级相同的贡献点消耗同点金魂币").append("\r\n");
        line2 = sb.toString();
    }

    @Action("宗门作用")
    public String Effect() {
        return line2;
    }

    @Action("宗门贡献")
    public String Cob(User qq) {
        return zongMenService.Cob(qq.getId());
    }

    @Action("救援.+")
    public String help(@AllMess String mess, User qq, Group group) {
        long who = MessageTools.getAtFromString(mess);
        if (who < 0)
            return "谁";
        return zongMenService.help(qq.getId(), who);
    }

    @Action("设置长老.+")
    public String setElder(User qq, @AllMess String mess) {
        long who = MessageTools.getAtFromString(mess);
        if (who < 0)
            return "谁";
        return zongMenService.setElder(qq.getId(), who);
    }

    @Action("取消长老.+")
    public String cancelElder(User qq, @AllMess String mess) {
        long who = MessageTools.getAtFromString(mess);
        if (who < 0)
            return "谁";
        return zongMenService.cancelElder(qq.getId(), who);
    }

    @Action("宗门升级")
    public String UpUp(User qq, Group group) {
        return zongMenService.UpUp(qq.getId(), group);
    }

    @Action("退出宗门")
    public String QuiteZong(User qq) {
        return zongMenService.quite(qq.getId());
    }

    @Action("移除成员.+")
    public String QuiteOne(User qq, @AllMess String mess) {
        long who = MessageTools.getAtFromString(mess);
        if (who < 0) {
            return "谁?";
        }
        return zongMenService.QuiteOne(qq.getId(), who);
    }
}
