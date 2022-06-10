package Project.controllers;

import io.github.kloping.MySpringTool.annotations.Action;
import io.github.kloping.MySpringTool.annotations.Controller;
import io.github.kloping.file.FileUtils;
import io.github.kloping.mirai0.Main.Resource;

import java.io.File;
import java.lang.reflect.Field;

/**
 * @author github-kloping
 */
@Controller
public class FirstController {

    @Action("/test9")
    public Object s0() {
        try {
            Field field = Resource.contextManager.getClass().getDeclaredField("contexts");
            field.setAccessible(true);
            FileUtils.putStringInFile(field.get(Resource.contextManager).toString(), new File("./temp/temp.txt"));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return "null";
    }
}
