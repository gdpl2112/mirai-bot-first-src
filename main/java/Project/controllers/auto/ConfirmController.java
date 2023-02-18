package Project.controllers.auto;

import io.github.kloping.MySpringTool.annotations.Action;
import io.github.kloping.MySpringTool.annotations.Controller;
import io.github.kloping.MySpringTool.exceptions.NoRunException;
import io.github.kloping.mirai0.commons.SpGroup;
import io.github.kloping.mirai0.commons.SpUser;
import io.github.kloping.mirai0.commons.invokes.MethodCanCall;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.*;

import static io.github.kloping.mirai0.Main.BootstarpResource.println;

/**
 * @author github-kloping
 */
@Controller
public class ConfirmController {
    public static final int CONFIRMING = 0;
    public static final int AGREEING = 0;
    private static final ExecutorService THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(25, 25, 60, TimeUnit.SECONDS, new ArrayBlockingQueue<>(25));
    public static Map<Long, MethodCanCall> ConfirmMap = new ConcurrentHashMap<>();

    public static Map<Long, MethodCanCall> AgreeMap = new ConcurrentHashMap<>();

    public ConfirmController() {
        println(this.getClass().getSimpleName() + "构建");
    }

    public static void regConfirm(Long qid, Method method, Object o, Object... args) {
        MethodCanCall canCall = new MethodCanCall().setMethod(method).setObjThis(o).setArgs(args);
        ConfirmMap.put(qid, canCall);
        startTime(qid, CONFIRMING);
    }

    /**
     * 提交同意
     *
     * @param qid    唯一QQ
     * @param method method
     * @param o      method this object
     * @param args   args
     */
    public static void regAgree(Long qid, Method method, Object o, Object... args) {
        MethodCanCall canCall = new MethodCanCall().setMethod(method).setObjThis(o).setArgs(args);
        AgreeMap.put(qid, canCall);
        startTime(qid, AGREEING);
    }

    private static void startTime(Long qid, int type1) {
        startTime(qid, type1, 40);
    }

    private static void startTime(Long qid, int type1, Integer wait) {
        THREAD_POOL_EXECUTOR.execute(new Runnable() {
            private int t = wait.intValue();
            private Long id = qid;
            private int type = type1;

            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    t--;
                    boolean k =
                            (ConfirmMap.keySet().contains(id) && ConfirmMap.containsKey(id)) ||
                                    (AgreeMap.containsKey(id) && AgreeMap.keySet().contains(id));
                    if (k) {
                        if (t > 0) {
                            run();
                        } else {
                            if (type == CONFIRMING) {
                                ConfirmMap.remove(id);
                            } else if (type == AGREEING) {
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
    public Object confirm(SpUser qq, SpGroup group) throws NoRunException {
        if (ConfirmMap.keySet().contains(qq.getId())) {
            MethodCanCall mcc = ConfirmMap.get(qq.getId());
            Object result = null;
            try {
                result = mcc.invoke();
                Long id = qq.getId();
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
    public Object cancel(SpUser qq, SpGroup group) throws NoRunException {
        if (ConfirmMap.keySet().contains(qq.getId())) {
            Long id = qq.getId();
            ConfirmMap.remove(id);
            return ("已取消");
        }
        throw new NoRunException();
    }

    @Action("同意")
    public Object agree(SpUser qq, SpGroup group) throws NoRunException {
        if (AgreeMap.keySet().contains(qq.getId())) {
            MethodCanCall mcc = AgreeMap.get(qq.getId());
            Object result = null;
            try {
                result = mcc.invoke();
                Long id = qq.getId();
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
    public String noAgree(SpUser qq, SpGroup group) throws NoRunException {
        if (AgreeMap.keySet().contains(qq.getId())) {
            Long id = qq.getId();
            AgreeMap.remove(id);
            return ("已拒绝");
        } else {
            throw new NoRunException();
        }
    }
}