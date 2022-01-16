package Project.Controllers.NormalController;

import Entitys.Group;
import Entitys.User;
import Project.DataBases.DataBase;
import Project.Tools.Tool;
import io.github.kloping.Mirai.Main.BotStarter;
import io.github.kloping.Mirai.Main.Handlers.CapHandler;
import io.github.kloping.Mirai.Main.ITools.MemberTools;
import io.github.kloping.Mirai.Main.ITools.MessageTools;
import io.github.kloping.Mirai.Main.Resource;
import io.github.kloping.MySpringTool.StarterApplication;
import io.github.kloping.MySpringTool.annotations.*;
import io.github.kloping.MySpringTool.exceptions.NoRunException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static Project.Tools.Tool.getRandString;
import static io.github.kloping.Mirai.Main.ITools.MemberTools.getUser;
import static io.github.kloping.Mirai.Main.Resource.*;

/**
 * @author github-kloping
 */
@Controller
public class SuperController {
    public SuperController() {
        println(this.getClass().getSimpleName() + "构建");
    }

    public long tempSuperL = -1;

    @Before
    public void before(@AllMess String mess, Group group, User qq) throws NoRunException {
        if (tempSuperL != -1L) {
            if (qq.getId() == tempSuperL) {
                tempSuperL = -1L;
                return;
            }
        }
        if (qq.getId() != superQL) {
            throw new NoRunException("can`t do this");
        }
    }

    @Action("跳过验证.+")
    public String o3(@AllMess String mess) {
        try {
            String numStr = Tool.findNumberFromString(mess);
            long qid = Long.parseLong(numStr);
            CapHandler.ok(qid);
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return "not found";
        }
    }

    @Action("/execute.+")
    public String o1(@AllMess String str, Group group) {
        long q = MessageTools.getAtFromString(str);
        if (q == -1) {
            throw new NoRunException("");
        }
        String qStr = q == bot.getId() ? "me" : String.valueOf(q);
        str = str.replaceFirst("/execute\\[@" + qStr + "]", "");
        StarterApplication.ExecuteMethod(q, str, q, getUser(q), Group.get(group.getId()), 0);
        return "executing";
    }

    @Action("赋予一次超级权限.+")
    public String f0(@AllMess String s) {
        long q = MessageTools.getAtFromString(s);
        if (q == -1) {
            throw new NoRunException("");
        }
        tempSuperL = q;
        return "ok";
    }


    @Action("open")
    public String m1(Group group) {
        if (!BotStarter.test) {
            return null;
        }
        DataBase.openGroup(group.getId());
        return "opened";
    }

    @Action("addScore.{1,}")
    public String addScore(@AllMess String messages, User qq, Group gr) throws NoRunException {
        if (qq.getId() == superQL) {
            long who = MessageTools.getAtFromString(messages);
            messages = messages.replace(Long.toString(who), "");
            if (who == -1) {
                return ("Are You True??");
            }
            long num = Long.parseLong(Tool.findNumberFromString(messages));
            DataBase.addScore(num, who);
            return new StringBuilder().append("给 =》 ").append(MemberTools.getNameFromGroup(who, gr)).append("增加了\r\n=>").append(num + "").append("积分").toString();
        } else {
            throw new NoRunException();
        }
    }

    @Action("全体加积分.{1,}")
    public String addAllScore(@AllMess String messages, User qq) throws NoRunException {
        if (qq.getId() == superQL) {
            long num = Long.parseLong(Tool.findNumberFromString(messages));
            DataBase.AddAllScore(num);
            return new StringBuilder().append("加积分=>异步执行中... On 积分").toString();
        } else {
            throw new NoRunException();
        }
    }

    private static final Map<Long, Long> UP_AKS = new ConcurrentHashMap<>();
    public static final String[] IS_ADMINISTRATOR_TIPS = new String[]
            {"应该是", "是的", "是吧", "是", "你猜", "yeah", "is", "1"};
    public static final String[] IST_ADMINISTRATOR_TIPS = new String[]
            {"你觉得呢", "不是", "?你猜", "问我呢?", "no", "222", "你确定?", "-1"};

    @Action(value = "ta是管理<.+=>par>", otherName = {"他是管理<.+=>par>", "她是管理<.+=>par>"})
    public Object isIAdministrator(@Param("par") String par, User qq) {
        Number q2 = null;
        try {
            q2 = Long.valueOf(Tool.findNumberFromString(par));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        if (q2 == null || q2.longValue() == -1) {
            return "谁?";
        }
        UP_AKS.put(qq.getId(), q2.longValue());
        if (DataBase.isFather(q2.longValue())) {
            return IS_ADMINISTRATOR_TIPS[Tool.rand.nextInt(IS_ADMINISTRATOR_TIPS.length)];
        } else {
            return IST_ADMINISTRATOR_TIPS[Tool.rand.nextInt(IST_ADMINISTRATOR_TIPS.length)];
        }
    }

    public static final String[] ADD_FATHER_TIPS = new String[]
            {"好的", "听你的", "OK", "嗯嗯", "真好"};
    public static final String[] NDD_FATHER_TIPS = new String[]
            {"不要", "为啥", "凭什么", "阿巴巴", "就不"};

    @Action("现在是了")
    public Object isAdministratorNow(User qq) {
        Long q2 = UP_AKS.get(qq.getId());
        if (q2 == null || q2 == 0) {
            return "是什么?";
        }
        if (DataBase.isFather(q2)) {
            return "ta本来就是好吧...";
        }
        if (qq.getId() == Resource.superQL) {
            DataBase.addFather(q2);
            return getRandString(ADD_FATHER_TIPS);
        } else {
            return getRandString(NDD_FATHER_TIPS);
        }
    }

    public static final String[] EDD_FATHER_TIPS =
            new String[]{"好的", "收到", "OK", "完成", "弄好了"};

    @Action("现在不是了")
    public Object istAdministratorNow(User qq) {
        Long q2 = UP_AKS.get(qq.getId());
        if (q2 == null || q2 == 0) {
            return "不是什么?";
        }
        if (!DataBase.isFather(q2)) {
            return "ta本来就不是好吧...";
        }
        if (qq.getId() == Resource.superQL) {
            DataBase.removeFather(q2);
            return getRandString(EDD_FATHER_TIPS);
        } else {
            return getRandString(NDD_FATHER_TIPS);
        }
    }


}
