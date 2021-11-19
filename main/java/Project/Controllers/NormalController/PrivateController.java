package Project.Controllers.NormalController;

import Project.Controllers.ControllerTool;
import io.github.kloping.Mirai.Main.Resource;
import io.github.kloping.MySpringTool.annotations.Before;
import io.github.kloping.MySpringTool.annotations.Controller;
import io.github.kloping.MySpringTool.exceptions.NoRunException;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.NormalMember;
import net.mamoe.mirai.contact.User;

import java.lang.reflect.Member;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static io.github.kloping.Mirai.Main.Resource.Switch.AllK;
import static io.github.kloping.Mirai.Main.Resource.superQL;

@Controller
public class PrivateController {
    @Before
    public void before(User qq) throws NoRunException {
        if (!AllK)
            throw new NoRunException("总开关——关闭");
        if (qq instanceof Member || qq instanceof NormalMember)
            throw new NoRunException("群聊消息");
    }

    public static void action(String str, long num) {
        if (num == superQL) {
            switch (str) {
            }
            if (str.startsWith("You Send All Group")) {
                sendAllGroup(str.replace("You Send All Group", ""));
            }
        } else {

        }
    }

    private static void sendAllGroup(String mess) {
        for (Group group : Resource.bot.getGroups()) {
            if (!ControllerTool.CanGroup(group.getId())) continue;
            group.sendMessage(mess);
        }
    }

    public static final Map<Long, List<String>> his = new ConcurrentHashMap<>();
}