package Project.controllers.auto;

import io.github.kloping.MySpringTool.annotations.Controller;
import io.github.kloping.mirai0.commons.invokes.MethodCanCall;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static io.github.kloping.mirai0.Main.Resource.println;

/**
 * @author github-kloping
 */
@Controller
public class SelectController {
    public static Map<Long, MethodCanCall> AgreeMap = new ConcurrentHashMap<>();

    public SelectController() {
        println(this.getClass().getSimpleName() + "构建");
    }


    /**
     * 提交选择
     *
     * @param who    唯一QQ
     * @param method method
     * @param o      method this object
     * @param args   args
     */
    public static void regAgree(Long who, Method method, Object o, Object... args) {
        MethodCanCall canCall = new MethodCanCall().setMethod(method).setObjThis(o).setArgs(args);
        AgreeMap.put(who, canCall);
    }

}