package Project.controllers;

import io.github.kloping.mirai0.Entitys.Group;
import io.github.kloping.mirai0.Entitys.User;
import io.github.kloping.MySpringTool.annotations.Action;
import io.github.kloping.MySpringTool.annotations.AllMess;
import io.github.kloping.MySpringTool.annotations.Controller;

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
