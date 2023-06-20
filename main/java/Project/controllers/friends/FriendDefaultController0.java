package Project.controllers.friends;

import Project.dataBases.DataBase;
import io.github.kloping.MySpringTool.annotations.Action;
import io.github.kloping.MySpringTool.annotations.AllMess;
import io.github.kloping.MySpringTool.annotations.Controller;
import io.github.kloping.MySpringTool.exceptions.NoRunException;
import Project.utils.Tools.Tool;

import static Project.commons.rt.ResourceSet.FinalString.ERR_TIPS;
import static Project.commons.rt.ResourceSet.FinalString.PERMISSION_DENIED;
import static io.github.kloping.mirai0.Main.BootstarpResource.isSuperQ;
import static io.github.kloping.mirai0.Main.BootstarpResource.println;

/**
 * @author github.kloping
 */
@Controller
public class FriendDefaultController0 {
    public FriendDefaultController0() {
        println(this.getClass().getSimpleName() + "构建");
    }

    @Action("加积分.+")
    public String addScore(@AllMess String content, Long qid) throws NoRunException {
        if (!isSuperQ(qid)) return PERMISSION_DENIED;
        long q2 = Project.utils.Utils.getAtFromString(content);
        content = content.replace(Long.toString(q2), "");
        if (q2 == -1) return ERR_TIPS;
        long count = Long.parseLong(Tool.INSTANCE.findNumberFromString(content));
        DataBase.addScore(count, q2);
        return new StringBuilder().append("给 =》 ").append(q2).append("增加了\r\n=>").append(count + "").append("积分").toString();
    }

    @Action("开启聊天")
    public String openTalk(Long qid) {
        DataBase.setSpeak(qid, true);
        return "OK";
    }

    @Action("关闭聊天")
    public String closeTalk(Long qid) {
        DataBase.setSpeak(qid, false);
        return "OK";
    }
}
