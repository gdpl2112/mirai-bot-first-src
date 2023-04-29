package Project.controllers.gameControllers.zongmenContrller;


import Project.aSpring.SpringBootResource;
import Project.commons.SpGroup;
import Project.commons.SpUser;
import Project.commons.gameEntitys.Zon;
import Project.controllers.auto.ConfirmController;
import Project.interfaces.Iservice.IZongMenService;
import Project.utils.VelocityUtils;
import io.github.kloping.MySpringTool.annotations.*;
import io.github.kloping.MySpringTool.exceptions.NoRunException;
import io.github.kloping.mirai0.Main.iutils.MessageUtils;
import io.github.kloping.mirai0.commons.Zong;

import java.lang.reflect.Method;
import java.util.*;

import static Project.aSpring.SpringBootResource.getZonMapper;
import static Project.commons.rt.ResourceSet.FinalString.*;
import static Project.commons.rt.ResourceSet.FinalValue.NOT_OPEN_NO_RUN_EXCEPTION;
import static Project.controllers.auto.ControllerTool.opened;
import static Project.controllers.normalController.ScoreController.longs;
import static Project.dataBases.ZongMenDataBase.*;
import static io.github.kloping.mirai0.Main.BootstarpResource.println;
import static io.github.kloping.mirai0.unitls.Tools.GameTool.getFhName;
import static io.github.kloping.mirai0.unitls.drawers.Drawer.getImageFromStrings;

/**
 * @author github-kloping
 */
@Controller
public class ZongMenController {

    public static final int COB_CD = 6;

    @AutoStand
    IZongMenService zongMenService;

    public ZongMenController() {
        println(this.getClass().getSimpleName() + "构建");
    }

    @Before
    public void before(SpGroup group) throws NoRunException {
        if (!opened(group.getId(), this.getClass())) {
            throw NOT_OPEN_NO_RUN_EXCEPTION;
        }
    }

    @Action("救援.+")
    public String help(@AllMess String mess, SpUser qq, SpGroup group) {
        long who = Project.utils.Utils.getAtFromString(mess);
        if (who < 0) {
            return NOT_FOUND_AT;
        }
        return zongMenService.help(qq.getId(), who);
    }

    @Action("宗门系统")
    public String menu() {
        return VelocityUtils.getTemplateToString("zong.menu");
    }

    @Action("创建宗门<.+=>name>")
    public String create(@Param("name") String name, SpUser qq, SpGroup group) {
        if (name == null || name.isEmpty() || NULL_LOW_STR.equals(name)) return "名字 不可为空";
        if (longs.contains(qq.getId())) return "Can't";
        return zongMenService.create(name, qq.getId(), group);
    }

    @Action("宗门信息")
    public String info(SpUser qq, SpGroup group) {
        return zongMenService.zongInfo(qq.getId(), group);
    }

    @Action("宗门列表")
    public String list(SpGroup g) {
        return zongMenService.list(g);
    }

    @Action("设置宗门图标<.+=>name>")
    public String setIcon(@AllMess String message, SpGroup group, SpUser qq) {
        String img = MessageUtils.INSTANCE.getImageUrlFromMessageString(message);
        if (img == null) return ("请携带图片");
        return zongMenService.setIcon(img, qq.getId(), group);
    }

    @Action("设置宗门名称<.+=>name>")
    public String setName(@Param("name") String name, SpGroup group, SpUser qq) {
        return zongMenService.setName(name, qq.getId(), group);
    }

    @Action("邀请.+")
    public Object invite(@AllMess String mess, SpUser qq, SpGroup group) {
        long l1 = Project.utils.Utils.getAtFromString(mess);
        if (l1 < 0) return NOT_FOUND_AT;
        if (longs.contains(l1)) return "Can't";
        return zongMenService.invite(qq.getId(), l1, group);
    }

    @Action("宗门人数")
    public String listPer(SpUser qq, SpGroup group) {
        return zongMenService.listPer(qq.getId(), group);
    }

    @Action("宗门作用")
    public String effectIntro() {
        return VelocityUtils.getTemplateToString("zong.value.intro", COB_CD);
    }

    @Action("宗门贡献")
    public String cob(SpUser qq) {
        return zongMenService.cob(qq.getId());
    }

    @Action("设置长老.+")
    public String setElder(SpUser qq, @AllMess String mess) {
        long who = Project.utils.Utils.getAtFromString(mess);
        if (who < 0) {
            return NOT_FOUND_AT;
        }
        return zongMenService.setElder(qq.getId(), who);
    }

    @Action("取消长老.+")
    public String cancelElder(SpUser qq, @AllMess String mess) {
        long who = Project.utils.Utils.getAtFromString(mess);
        if (who < 0) {
            return NOT_FOUND_AT;
        }
        return zongMenService.cancelElder(qq.getId(), who);
    }

    @Action("宗门升级")
    public String upUp(SpUser qq, SpGroup group) {
        return zongMenService.upUp(qq.getId(), group);
    }

    @Action("退出宗门")
    public String quiteZong(SpUser qq) {
        return zongMenService.quite(qq.getId());
    }

    @Action("移除成员.+")
    public String quiteOne(SpUser qq, @AllMess String mess) {
        long who = Project.utils.Utils.getAtFromString(mess);
        if (who < 0) {
            return NOT_FOUND_AT;
        }
        return zongMenService.quiteOne(qq.getId(), who);
    }

    @Action("宗门扩增")
    public String addMax(SpUser user) {
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
                sb.append(i).append(":").append(getFhName(e1.getKey(), true))
                        .append("(").append(zon.getLevel() == 1 ? "长老" : zon.getLevel() == 2 ? "宗主" : "")
                        .append(e1.getValue()).append("点活跃").append(NEWLINE);
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
        long q2 = Project.utils.Utils.getAtFromString(mess);
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
        Zon zon = new Zon().setId(zong.getId()).setXper(0).setQq(q2).setTimes(1).setLevel(2).setActive(0);
        zong.getMember().add(q2);
        getZonMapper().insert(zon);
        getZonMapper().deleteById(q1);
        qq2id.remove(q1);
        qq2id.put(q2, zong.getId());
        return "成功";
    }
}
