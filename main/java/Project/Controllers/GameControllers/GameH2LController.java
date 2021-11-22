package Project.Controllers.GameControllers;

import Entitys.Group;
import Entitys.PersonInfo;
import Entitys.User;
import io.github.kloping.Mirai.Main.ITools.MessageTools;
import io.github.kloping.Mirai.Main.Resource;
import io.github.kloping.MySpringTool.annotations.*;
import io.github.kloping.MySpringTool.exceptions.NoRunException;
import io.github.kloping.number.NumberUtils;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static Project.Controllers.ControllerTool.CanGroup;
import static Project.DataBases.GameDataBase.*;
import static io.github.kloping.Mirai.Main.Resource.Switch.AllK;
import static io.github.kloping.Mirai.Main.Resource.println;

@Controller
public class GameH2LController {
    public GameH2LController() {
        println(this.getClass().getSimpleName() + "构建");
    }

    public static void check(Object[] objects) throws NoRunException {
        long q = Long.parseLong(objects[0].toString());
        Group group = (Group) objects[4];
        PersonInfo info = getInfo(q);
        if (info.getDouing()) {
            if (!inCanDonging(objects[1].toString())) {
                MessageTools.sendMessageInGroupWithAt("您正在斗魂,请不要做其他不相干的事情", group.getId(), q);
                throw new NoRunException("out Around");
            }
        }
    }

    private static boolean inCanDonging(String str) {
        if (str.equals("信息")) return true;
        if (str.startsWith("结束")) return true;
        return false;
    }

    public static final List<Long> douingList = new CopyOnWriteArrayList<>();

    @Before
    public void before(User qq, Group group, @AllMess String mess) throws NoRunException {
        if (!AllK) {
            throw new NoRunException("总开关——关闭");
        }
        if (!CanGroup(group.getId())) {
            throw new NoRunException("未开启");
        }
        if (getInfo(qq.getId()).getHp() <= 0) {
            /*       if (EveListStartWith(listFx, mess) == -1) {
                MessageTools.sendMessageInGroupWithAt("无状态", group.getId(), qq.getId());
                throw new NoRunException("无状态");
            }
     */
        }
    }

    @Action("创建分身<.*=>at>")
    public String m1(@Param("at") String at, long qq) {
        if (qq != Resource.superQL) return "?";
        long q = Long.parseLong(NumberUtils.findNumberFromString(at));
        PersonInfo personInfo = getInfo(q);
        personInfo.setDouing(true);
        personInfo.setGold(-1L);
        personInfo.setHelpC(999);
        personInfo.setBuyHelpC(999);
        personInfo.setBindQ(99999);
        personInfo.setHp(personInfo.getHp() / 2);
        personInfo.setHpl(personInfo.getHpl() / 2);
        personInfo.setHl(personInfo.getHl() / 2);
        personInfo.setHll(personInfo.getHll() / 2);
        personInfo.setHj(personInfo.getHj() / 2);
        personInfo.setHjL(personInfo.getHjL() / 2);
        personInfo.setXpL(Long.MAX_VALUE);
        personInfo.setXp(0L);
        personInfo.setLevel(-1);
        personInfo.setWhType(1);
        personInfo.setAtt(personInfo.getAtt() / 2);
        createTempInfo(personInfo);
        return "OKKKKKKKKKKKK";
    }

    @Action("结束分身<.*=>at>")
    public String m2(@Param("at") String at, long qq) {
        if (qq != Resource.superQL) return "?";
        long q = Long.parseLong(NumberUtils.findNumberFromString(at));
        PersonInfo personInfo = getInfo(q);
        deleteTempInfo(personInfo);
        return "OKKKKKKKK";
    }
}
