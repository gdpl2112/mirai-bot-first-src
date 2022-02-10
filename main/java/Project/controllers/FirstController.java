package Project.controllers;

import Project.detailPlugin.BaiduShituDetail;
import Project.interfaces.old.ShiTuPaoDeKuaiWeiXinQunProxyGoogle;
import io.github.kloping.MySpringTool.annotations.Action;
import io.github.kloping.MySpringTool.annotations.AllMess;
import io.github.kloping.MySpringTool.annotations.AutoStand;
import io.github.kloping.MySpringTool.annotations.Controller;
import io.github.kloping.mirai0.Entitys.Group;
import io.github.kloping.mirai0.Entitys.User;
import io.github.kloping.mirai0.Entitys.apiEntitys.baiduShitu.BaiduShitu;
import io.github.kloping.mirai0.Main.ITools.MessageTools;
import net.mamoe.mirai.message.data.SimpleServiceMessage;

import static io.github.kloping.mirai0.Main.Resource.bot;

/**
 * @author github-kloping
 */
@Controller
public class FirstController {

    private String f0 = "./data/t0.hml";

    @AutoStand
    ShiTuPaoDeKuaiWeiXinQunProxyGoogle proxyGoogle;

    @Action("测试.+")
    public Object a(@AllMess String mess, Group group, User user) throws Exception {
        SimpleServiceMessage message = new SimpleServiceMessage(1, "{\"app\":\"com.tencent.weather\",\"desc\":\"天气\",\"view\":\"RichInfoView\",\"ver\":\"0.0.0.1\",\"prompt\":\"[应用]天气\",\"appID\":\"\",\"sourceName\":\"\",\"actionData\":\"\",\"actionData_A\":\"\",\"sourceUrl\":\"\",\"meta\":{\"richinfo\":{\"adcode\":\"\",\"air\":\"1\",\"city\":\"郑州\",\"date\":\"02月10日 周四\",\"max\":\"10\",\"min\":\"0\",\"ts\":\"1644462202\",\"type\":\"208\",\"wind\":\"\"}},\"text\":\"\",\"sourceAd\":\"\",\"extra\":\"\"}");
        bot.getGroup(group.getId()).sendMessage(message);
        return null;
    }
}
