package Project.controllers;

import Project.interfaces.http_api.AiBaidu;
import io.github.kloping.MySpringTool.annotations.Action;
import io.github.kloping.MySpringTool.annotations.AllMess;
import io.github.kloping.MySpringTool.annotations.AutoStand;
import io.github.kloping.MySpringTool.annotations.Controller;
import io.github.kloping.mirai0.commons.Group;
import io.github.kloping.mirai0.commons.User;
import io.github.kloping.mirai0.unitls.Tools.Tool;
import io.github.kloping.mirai0.unitls.drawers.Drawer;

import java.util.LinkedList;
import java.util.List;

/**
 * @author github-kloping
 */
@Controller
public class FirstController {
    @AutoStand
    AiBaidu aiBaidu;

    @Action("测试.+")
    public Object a(@AllMess String mess, Group group, User user) throws Exception {
//        SimpleServiceMessage message = new SimpleServiceMessage(1, "{\"app\":\"com.tencent.gamecenter.gameshare\",\"desc\":\"\",\"view\":\"noDataView\",\"ver\":\"0.0.0.0\",\"prompt\":\"\",\"appID\":\"\",\"sourceName\":\"\",\"actionData\":\"\",\"actionData_A\":\"\",\"sourceUrl\":\"\",\"meta\":{\"shareData\":{\"scene\":\"SCENE_SHARE_VIDEO\",\"jumpUrl\":\"http:\\/\\/kissme.\\/1.mp3\",\"type\":\"video\",\"url\":\"https:\\/\\/game.gtimg.cn\\/images\\/x5\\/cp\\/a20180702newversionm\\/song\\/song2.mp3\",\"DATA13\":\"0\",\"DATA10\":\"\",\"DATA14\":\"videotest1\"}},\"config\":{\"type\":\"normal\",\"width\":-92200,\"height\":-93000,\"forward\":0},\"text\":\"\",\"sourceAd\":\"\",\"extra\":\"\"}");
        List<Integer> lids = new LinkedList<>();
        lids.add(201);
        lids.add(202);
        lids.add(203);
        lids.add(203);
        lids.add(204);
        lids.add(204);
        lids.add(205);
        lids.add(205);
        lids.add(206);
        lids.add(206);
        lids.add(206);
        lids.add(207);
        String f = Drawer.drawHh(1, lids);
        return Tool.pathToImg(f);
    }
}
