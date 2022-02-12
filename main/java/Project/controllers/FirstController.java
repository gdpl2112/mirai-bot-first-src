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
import net.mamoe.mirai.message.data.Audio;
import net.mamoe.mirai.message.data.OfflineAudio;
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
        return "";
    }
}
