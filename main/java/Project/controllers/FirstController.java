package Project.controllers;

import io.github.kloping.MySpringTool.annotations.Action;
import io.github.kloping.MySpringTool.annotations.AllMess;
import io.github.kloping.MySpringTool.annotations.Controller;
import io.github.kloping.mirai0.Entitys.Group;
import io.github.kloping.mirai0.Entitys.User;

/**
 * @author github-kloping
 */
@Controller
public class FirstController {
    @Action("测试.+")
    public Object a(@AllMess String mess, Group group, User user) throws Exception {
//        SimpleServiceMessage message = new SimpleServiceMessage(1, "{\"app\":\"com.tencent.gamecenter.gameshare\",\"desc\":\"\",\"view\":\"noDataView\",\"ver\":\"0.0.0.0\",\"prompt\":\"\",\"appID\":\"\",\"sourceName\":\"\",\"actionData\":\"\",\"actionData_A\":\"\",\"sourceUrl\":\"\",\"meta\":{\"shareData\":{\"scene\":\"SCENE_SHARE_VIDEO\",\"jumpUrl\":\"http:\\/\\/kissme.\\/1.mp3\",\"type\":\"video\",\"url\":\"https:\\/\\/game.gtimg.cn\\/images\\/x5\\/cp\\/a20180702newversionm\\/song\\/song2.mp3\",\"DATA13\":\"0\",\"DATA10\":\"\",\"DATA14\":\"videotest1\"}},\"config\":{\"type\":\"normal\",\"width\":-92200,\"height\":-93000,\"forward\":0},\"text\":\"\",\"sourceAd\":\"\",\"extra\":\"\"}");
//        return message;
        return null;
    }
}
