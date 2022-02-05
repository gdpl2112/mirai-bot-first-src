package Project.Controllers;

import io.github.kloping.mirai0.Entitys.Group;
import io.github.kloping.mirai0.Entitys.User;
import io.github.kloping.mirai0.unitls.Tools.Tool;
import io.github.kloping.mirai0.unitls.drawers.GameDrawer;
import io.github.kloping.mirai0.unitls.drawers.entity.GameMap;
import io.github.kloping.mirai0.unitls.drawers.entity.MapPosition;
import io.github.kloping.MySpringTool.annotations.Action;
import io.github.kloping.MySpringTool.annotations.AllMess;
import io.github.kloping.MySpringTool.annotations.Controller;
import io.github.kloping.file.FileUtils;
import io.github.kloping.serialize.HMLObject;
import net.mamoe.mirai.message.data.MessageSourceBuilder;

import java.io.File;

/**
 * @author github-kloping
 */
@Controller
public class FirstController {

    private String f0 = "./data/t0.hml";

    @Action("测试.+")
    public Object a(@AllMess String mess, Group group, User user) throws Exception {
//        GameMap gameMap = null;
//        String hmlStr = FileUtils.getStringFromFile(f0);
//        if (hmlStr == null || hmlStr.isEmpty()) {
//            GameMap.GameMapBuilder builder = new GameMap.GameMapBuilder()
//                    .setWidth(7)
//                    .setHeight(4)
//                    .append(1, 1, "https://q1.qlogo.cn/g?b=qq&nk=" + user.getId() + "&s=640")
//                    .append(1, 2, "", MapPosition.Type.warn)
//                    ;
//            gameMap = builder.build();
//            FileUtils.putStringInFile(HMLObject.toHMLString(gameMap), new File(f0));
//        } else {
//            gameMap = HMLObject.parseObject(hmlStr, GameMap.class);
//        }
//        return Tool.pathToImg(GameDrawer.drawerMap(gameMap));
        return null;
    }
}
