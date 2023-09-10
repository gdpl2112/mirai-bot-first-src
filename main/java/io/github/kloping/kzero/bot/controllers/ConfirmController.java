package io.github.kloping.kzero.bot.controllers;

import io.github.kloping.MySpringTool.annotations.Action;
import io.github.kloping.MySpringTool.annotations.Controller;
import io.github.kloping.MySpringTool.exceptions.NoRunException;
import io.github.kloping.kzero.bot.commons.invokes.MethodCanCall;
import io.github.kloping.kzero.main.api.MessagePack;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.*;

/**
 * @author github-kloping
 */
@Controller
public class ConfirmController {
    public static final int CONFIRMING = 0;
    public static final int AGREEING = 0;
    private static final ExecutorService THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(25, 25, 60, TimeUnit.SECONDS, new ArrayBlockingQueue<>(25));
    public static Map<String, MethodCanCall> confirmMap = new ConcurrentHashMap<>();

    public static Map<String, MethodCanCall> agreeMap = new ConcurrentHashMap<>();

    public static void regConfirm(String qid, Method method, Object o, Object... args) {
        MethodCanCall canCall = new MethodCanCall().setMethod(method).setObjThis(o).setArgs(args);
        confirmMap.put(qid, canCall);
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
    public static void regAgree(String qid, Method method, Object o, Object... args) {
        MethodCanCall canCall = new MethodCanCall().setMethod(method).setObjThis(o).setArgs(args);
        agreeMap.put(qid, canCall);
        startTime(qid, AGREEING);
    }

    private static void startTime(String qid, int type1) {
        startTime(qid, type1, 40);
    }

    private static void startTime(String qid, int type1, Integer wait) {
        THREAD_POOL_EXECUTOR.execute(new Runnable() {
            private int t = wait.intValue();
            private String id = qid;
            private int type = type1;

            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    t--;
                    boolean k = (confirmMap.keySet().contains(id) && confirmMap.containsKey(id)) || (agreeMap.containsKey(id) && agreeMap.keySet().contains(id));
                    if (k) {
                        if (t > 0) {
                            run();
                        } else {
                            if (type == CONFIRMING) {
                                confirmMap.remove(id);
                            } else if (type == AGREEING) {
                                agreeMap.remove(id);
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
    public Object confirm(String sid, MessagePack pack) throws NoRunException {
        if (confirmMap.keySet().contains(sid)) {
            MethodCanCall mcc = confirmMap.get(sid);
            Object result = null;
            try {
                result = mcc.invoke();
                confirmMap.remove(sid);
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
    public Object cancel(String sid) throws NoRunException {
        if (confirmMap.keySet().contains(sid)) {
            confirmMap.remove(sid);
            return ("已取消");
        }
        throw new NoRunException();
    }

    @Action("同意")
    public Object agree(String sid) throws NoRunException {
        if (agreeMap.keySet().contains(sid)) {
            MethodCanCall mcc = agreeMap.get(sid);
            Object result = null;
            try {
                result = mcc.invoke();
                agreeMap.remove(sid);
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
    public String noAgree(String sid) throws NoRunException {
        if (agreeMap.keySet().contains(sid)) {
            agreeMap.remove(sid);
            return ("已拒绝");
        } else {
            throw new NoRunException();
        }
    }
}