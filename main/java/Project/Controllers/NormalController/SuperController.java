package Project.Controllers.NormalController;

import Entitys.Group;
import Entitys.User;
import Project.ASpring.SpringStarter;
import Project.ASpring.SpringStarter2;
import io.github.kloping.MySpringTool.annotations.Action;
import io.github.kloping.MySpringTool.annotations.AllMess;
import io.github.kloping.MySpringTool.annotations.Before;
import io.github.kloping.MySpringTool.annotations.Controller;
import io.github.kloping.MySpringTool.exceptions.NoRunException;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static io.github.kloping.Mirai.Main.Resource.println;
import static io.github.kloping.Mirai.Main.Resource.superQL;

@Controller
public class SuperController {
    public SuperController() {
        println(this.getClass().getSimpleName() + "构建");
    }

    @Before
    public void before(@AllMess String mess, Group group, User qq) throws NoRunException {
        if (qq.getId() != superQL)
            throw new NoRunException("can`t do this");
    }

    private static boolean reged = false;
    private static List<String> methodNames = new LinkedList<>();

    static {
        methodNames.add("move0");
        methodNames.add("move1");
    }

    public static void reg1() {
        reged = true;
        try {
            reg(SpringStarter.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void reg2() {
        reged = true;
        reg(SpringStarter2.class);
    }

    private static void reg(Class cla) {
        for (String s1 : methodNames) {
            try {
                Method method = cla.getDeclaredMethod(s1);
                method.setAccessible(true);
                methodMap.put(s1, method);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static Map<String, Method> methodMap = new ConcurrentHashMap<>();

    @Action("/move0")
    public void m0() {
        try {
            methodMap.get("move0").invoke(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Action("/move1")
    public void m1() {
        try {
            methodMap.get("move1").invoke(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
