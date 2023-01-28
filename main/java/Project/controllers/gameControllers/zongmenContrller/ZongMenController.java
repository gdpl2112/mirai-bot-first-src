package Project.controllers.gameControllers.zongmenContrller;


import Project.aSpring.SpringBootResource;
import Project.controllers.auto.ConfirmController;
import Project.interfaces.Iservice.IZongMenService;
import io.github.kloping.MySpringTool.annotations.*;
import io.github.kloping.MySpringTool.exceptions.NoRunException;
import io.github.kloping.mirai0.Main.ITools.MessageTools;
import io.github.kloping.mirai0.commons.Group;
import io.github.kloping.mirai0.commons.User;
import io.github.kloping.mirai0.commons.Zong;
import io.github.kloping.mirai0.commons.gameEntitys.Zon;

import java.lang.reflect.Method;
import java.util.*;

import static Project.aSpring.SpringBootResource.getZonMapper;
import static Project.controllers.auto.ControllerTool.opened;
import static Project.controllers.normalController.ScoreController.longs;
import static Project.dataBases.ZongMenDataBase.*;
import static io.github.kloping.mirai0.Main.Resource.println;
import static io.github.kloping.mirai0.Main.Resource.qq;
import static io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet.FinalString.*;
import static io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet.FinalValue.NOT_OPEN_NO_RUN_EXCEPTION;
import static io.github.kloping.mirai0.unitls.Tools.GameTool.getFhName;
import static io.github.kloping.mirai0.unitls.drawers.Drawer.getImageFromStrings;

/**
 * @author github-kloping
 */
@Controller
public class ZongMenController {

    public static final int COB_CD = 6;
    private static final String MENU =
            "1.创建宗门<Name>\n" +
                    "2.宗门信息\n" +
                    "3.宗门列表     #列出所有宗门\n" +
                    "4.设置宗门图标<Pic>\n" +
                    "5.设置宗门名称<Name>\n" +
                    "6.邀请<At>  #邀请加入宗门\n" +
                    "7.宗门人数    #查看宗门人数\n" +
                    "8.宗门升级\n" +
                    "9.退出宗门\n" +
                    "10.设置长老<At>\n" +
                    "11.取消长老<At>\n" +
                    "12.移除成员<At>\n" +
                    "13.宗门扩增\n" +
                    "14.活跃排行\n" +
                    "15.宗门活跃排行\n" +
                    "15.宗门转让<At> #将清除活跃值贡献值\n" +
                    "宗门的作用请见'宗门作用'";
    private static String line2 = "";

    static {
        StringBuilder sb = new StringBuilder();
        sb.append("一.宗门等级作用").append("\r\n\t").append("1级,宗主每天能免费救援一名宗门内成员(即无状态时救援变有状态)").append("\r\n\t").append("2级,宗主和长老都可救援").append("\r\n\t").append("3级,经验共享,当宗门内成员猎杀魂兽获得经验时所有成员获得部分经验加成(随等级而每人加成不一样)").append("\r\n\t").append("4级,经验共享加成增加").append("\r\n\t").append("5级,宗门内所有成员每天共享5次救援机会").append("\r\n\t").append("6级,宗门内所有长老及宗主每天多一次\"(请求)支援\"的次数,27人数").append(NEWLINE);
        sb.append("二.如何增加宗门经验").append("\r\n\t").append("每" + COB_CD + "个小时成员可使用‘宗门贡献’来贡献与等级相同的贡献点消耗同点金魂币").append(NEWLINE);
        sb.append("三.宗门活跃排行").append("\r\n\t").append("每周六早结算;").append(NEWLINE).append("\t").append("最活跃宗门;宗主奖励8000金魂币;长老奖励5000金魂币;成员奖励2000金魂币").append(NEWLINE).append("\t").append("次活跃宗门;宗主奖励6000金魂币;长老奖励3500金魂币;成员奖励1500金魂币").append(NEWLINE).append("\t").append("进入活动增加2点活跃").append(NEWLINE).append("\t").append("击败魂兽增加3点活跃").append(NEWLINE).append("\t").append("修炼/双修增加1点活跃").append(NEWLINE).append("\t").append("升级增加5点活跃").append(NEWLINE);
        line2 = sb.toString();
    }

    @AutoStand
    IZongMenService zongMenService;

    public ZongMenController() {
        println(this.getClass().getSimpleName() + "构建");
    }

    @Before
    public void before(Group group) throws NoRunException {
        if (!opened(group.getId(), this.getClass())) {
            throw NOT_OPEN_NO_RUN_EXCEPTION;
        }
    }

    @Action("救援.+")
    public String help(@AllMess String mess, User qq, Group group) {
        long who = MessageTools.instance.getAtFromString(mess);
        if (who < 0) {
            return NOT_FOUND_AT;
        }
        return zongMenService.help(qq.getId(), who);
    }

    @Action("宗门系统")
    public String menu() {
        return MENU;
    }

    @Action("创建宗门<.+=>name>")
    public String create(@Param("name") String name, User qq, Group group) {
        if (name == null || name.isEmpty() || NULL_LOW_STR.equals(name)) return "名字 不可为空";
        if (longs.contains(qq.getId())) return "Can't";
        return zongMenService.create(name, qq.getId(), group);
    }

    @Action("宗门信息")
    public String info(User qq, Group group) {
        return zongMenService.zongInfo(qq.getId(), group);
    }

    @Action("宗门列表")
    public String list(Group g) {
        return zongMenService.list(g);
    }

    @Action("设置宗门图标<.+=>name>")
    public String setIcon(@AllMess String message, Group group, User qq) {
        String img = MessageTools.instance.getImageUrlFromMessageString(message);
        if (img == null) return ("请携带图片");
        return zongMenService.setIcon(img, qq.getId(), group);
    }

    @Action("设置宗门名称<.+=>name>")
    public String setName(@Param("name") String name, Group group, User qq) {
        return zongMenService.setName(name, qq.getId(), group);
    }

    @Action("邀请.+")
    public Object invite(@AllMess String mess, User qq, Group group) {
        long l1 = MessageTools.instance.getAtFromString(mess);
        if (l1 < 0) return NOT_FOUND_AT;
        if (longs.contains(l1)) return "Can't";
        return zongMenService.invite(qq.getId(), l1, group);
    }

    @Action("宗门人数")
    public String listPer(User qq, Group group) {
        return zongMenService.listPer(qq.getId(), group);
    }

    @Action("宗门作用")
    public String effectIntro() {
        return line2;
    }

    @Action("宗门贡献")
    public String cob(User qq) {
        return zongMenService.cob(qq.getId());
    }

    @Action("设置长老.+")
    public String setElder(User qq, @AllMess String mess) {
        long who = MessageTools.instance.getAtFromString(mess);
        if (who < 0) {
            return NOT_FOUND_AT;
        }
        return zongMenService.setElder(qq.getId(), who);
    }

    @Action("取消长老.+")
    public String cancelElder(User qq, @AllMess String mess) {
        long who = MessageTools.instance.getAtFromString(mess);
        if (who < 0) {
            return NOT_FOUND_AT;
        }
        return zongMenService.cancelElder(qq.getId(), who);
    }

    @Action("宗门升级")
    public String upUp(User qq, Group group) {
        return zongMenService.upUp(qq.getId(), group);
    }

    @Action("退出宗门")
    public String quiteZong(User qq) {
        return zongMenService.quite(qq.getId());
    }

    @Action("移除成员.+")
    public String quiteOne(User qq, @AllMess String mess) {
        long who = MessageTools.instance.getAtFromString(mess);
        if (who < 0) {
            return NOT_FOUND_AT;
        }
        return zongMenService.quiteOne(qq.getId(), who);
    }

    @Action("宗门扩增")
    public String addMax(User user) {
        return zongMenService.addMax(user.getId());
    }

    @Action("活跃排行")
    public String listPh(long qid) {
        if (!qq2id.containsKey(qid)) return ("你没有加入任何宗门");
        Zong zong = getZongInfo(qid);
        Map<Long, Integer> map = new LinkedHashMap<>();
        for (Number number : zong.getMember()) {
            Zon zon = getZonInfo(number.longValue());
            int a = zon.getActive();
            map.put(number.longValue(), a);
        }
        List<Map.Entry<Long, Integer>> entryList2 = new ArrayList<Map.Entry<Long, Integer>>(map.entrySet());
        Collections.sort(entryList2, new Comparator<Map.Entry<Long, Integer>>() {
            @Override
            public int compare(Map.Entry<Long, Integer> e1, Map.Entry<Long, Integer> e2) {
                return e2.getValue().compareTo(e1.getValue()); // 降序排序
            }
        });

        StringBuilder sb = new StringBuilder();
        sb.append(zong.getName()).append(NEWLINE).append("============").append(NEWLINE);
        int i = 1;
        for (Map.Entry<Long, Integer> e1 : entryList2) {
            try {
                Zon zon = getZonInfo(e1.getKey());
                sb.append(i).append(":").append(getFhName(e1.getKey(), true)).append("(").append(zon.getLevel() == 1 ? "长老" : zon.getLevel() == 2 ? "宗主" : "").append(e1.getValue()).append("点活跃").append(NEWLINE);
            } catch (Exception e) {
                e.printStackTrace();
            }
            i++;
        }
        return getImageFromStrings(false, sb.toString().split(NEWLINE));
    }

    @Action("宗门活跃排行")
    public String listZong(long qid) {
        List<Zong> zs = SpringBootResource.getZongMapper().selectAllSortByActive();
        StringBuilder sb = new StringBuilder();
        for (Zong z : zs) {
            sb.append(String.format("<%s>", z.getName())).append("活跃值:").append(z.getActive()).append(NEWLINE);
        }
        return getImageFromStrings(false, sb.toString().split(NEWLINE));
    }

    @Action("宗门转让.+")
    public String trans(long qid, @AllMess String mess) {
        long q2 = MessageTools.instance.getAtFromString(mess);
        if (q2 < 0) {
            return NOT_FOUND_AT;
        }
        if (!qq2id.containsKey(qid)) return "你没有加入任何宗门";
        if (qq2id.containsKey(q2)) return "他已经加入宗门";
        Zong zong = getZongInfo(qq2id.get(qid));
        if (zong.getMain() != qid) return "仅限于宗主使用!";
        try {
            Method method = this.getClass().getDeclaredMethod("transNow", long.class, long.class);
            ConfirmController.regConfirm(qid, method, this, new Object[]{qid, q2});
            return "确定转让宗门吗?\n这将使你退出宗门且不可逆\n请在30秒内回复(确定/确认/取消)";
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    public String transNow(long q1, long q2) {
        Zong zong = getZongInfo(qq2id.get(q1));
        zong.setMain(q2);
        putZongInfo(zong);
        getZonMapper().deleteById(q1);
        qq2id.remove(q1);
        qq2id.put(q2, zong.getId());
        return "成功";
    }
}
