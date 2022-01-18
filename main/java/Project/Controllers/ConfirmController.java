package Project.Controllers;

import Entitys.Group;
import Entitys.User;
import io.github.kloping.MySpringTool.annotations.Action;
import io.github.kloping.MySpringTool.annotations.Controller;
import io.github.kloping.MySpringTool.exceptions.NoRunException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

import static io.github.kloping.Mirai.Main.Resource.println;

/**
 * @author github-kloping
 */
@Controller
public class ConfirmController {
    public ConfirmController() {
        println(this.getClass().getSimpleName() + "构建");
    }

    public static List<Long> Confirming = new LinkedList<>();
    public static Map<Long, Object[]> ConfirmMap = new ConcurrentHashMap<>();

    public static List<Long> Agreeing = new LinkedList<>();
    public static Map<Long, Object[]> AgreeMap = new ConcurrentHashMap<>();

    /**
     * 提交确认
     *
     * @param who
     * @param objects [0] Method [1]This [3...] par
     */
    public static void regConfirm(Long who, Object[] objects) {
        Confirming.add(who);
        ConfirmMap.put(who, objects);
        startTime(who, 1);
    }

    /**
     * 提交同意
     *
     * @param who
     * @param objects [0] Method [1]This [3...] par
     */
    public static void regAgree(Long who, Object[] objects) {
        Agreeing.add(who);
        AgreeMap.put(who, objects);
        startTime(who, 2);
    }


    private static final ExecutorService THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(25, 25, 60, TimeUnit.SECONDS, new ArrayBlockingQueue<>(25));

    private static void startTime(Long who, int type1) {
        THREAD_POOL_EXECUTOR.execute(new Runnable() {
            private int t = 30;
            private Long id = who;
            private int type = type1;

            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    t--;
                    if ((Confirming.contains(id) && ConfirmMap.containsKey(id)) || (AgreeMap.containsKey(id) && Agreeing.contains(id))) {
                        if (t > 0) {
                            run();
                        } else {
                            if (type == 1) {
                                Confirming.remove(id);
                                ConfirmMap.remove(id);
                            } else if (type == 2) {
                                Agreeing.remove(id);
                                AgreeMap.remove(id);
                            }
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Action(value = "确定", otherName = "确认")
    public Object confirm(User qq, Group group) throws NoRunException {
        if (Confirming.contains(qq.getId())) {
            Object[] objects = ConfirmMap.get(qq.getId());
            Method method = (Method) objects[0];
            method.setAccessible(true);
            Object[] pars = (Object[]) objects[2];
            Object result = null;
            try {
                result = method.invoke(objects[1], pars).toString();
                Long id = qq.getId();
                Confirming.remove(id);
                ConfirmMap.remove(id);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            return result;
        } else {
            throw new NoRunException();
        }
    }

    @Action("取消")
    public Object cancel(User qq, Group group) throws NoRunException {
        if (Confirming.contains(qq.getId())) {
            Long id = qq.getId();
            Confirming.remove(id);
            ConfirmMap.remove(id);
            return ("已取消");
        }
        throw new NoRunException();
    }

    @Action("同意")
    public Object agree(User qq, Group group) throws NoRunException {
        if (Agreeing.contains(qq.getId())) {
            Object[] objects = AgreeMap.get(qq.getId());
            Method method = (Method) objects[0];
            Object[] pars = (Object[]) objects[2];
            Object result = null;
            try {
                result = method.invoke(objects[1], pars).toString();
                Long id = qq.getId();
                Agreeing.remove(id);
                AgreeMap.remove(id);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            return result;
        } else {
            throw new NoRunException();
        }
    }

    @Action("不同意")
    public String noAgree(User qq, Group group) throws NoRunException {
        if (Agreeing.contains(qq.getId())) {
            Long id = qq.getId();
            Agreeing.remove(id);
            AgreeMap.remove(id);
            return ("已拒绝");
        } else {
            throw new NoRunException();
        }
    }
}