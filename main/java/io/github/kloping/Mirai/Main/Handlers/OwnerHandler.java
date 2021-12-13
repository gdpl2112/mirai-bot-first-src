package io.github.kloping.Mirai.Main.Handlers;

import Project.broadcast.game.GroupMessageBroadcast;
import io.github.kloping.Mirai.Main.ITools.MessageTools;
import kotlin.coroutines.CoroutineContext;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.NudgeEvent;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import static Project.Tools.Tool.rand;
import static io.github.kloping.Mirai.Main.Resource.threads;

public class OwnerHandler extends SimpleListenerHost {
    public OwnerHandler() {
        super();
    }

    @Override
    public void handleException(@NotNull CoroutineContext context, @NotNull Throwable exception) {
        exception.printStackTrace();
    }

    public static final Set<Runnable> tenEveRunnable = new LinkedHashSet<>();

    private static int nudgeC = 0;
    private static GroupMessageBroadcast.GroupMessageReceiver receiver0 = null;
    private static boolean working = false;

    static {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                tenEveRunnable.forEach((t) -> t.run());
            }
        }, 20 * 1000, 10 * 60 * 1000);

        tenEveRunnable.add(() -> {
            nudgeC = nudgeC == 0 ? 0 : --nudgeC;
        });
    }

    @EventHandler
    public void onNudge(NudgeEvent event) {
        if (!(event.getSubject() instanceof Group)) return;
        if (working) return;
        long q2 = event.getTarget().getId();
        if (q2 != event.getBot().getId()) return;
        long q = event.getFrom().getId();
        Group group = (Group) event.getSubject();
        working = true;
        threads.submit(() -> {
            try {
                if (receiver0 != null) GroupMessageBroadcast.INSTANCE.remove(receiver0);
                nudgeC++;
                switch (nudgeC) {
                    case 1:
                        Thread.sleep(rand.nextInt(3) + 2 * 1000);
                        MessageTools.sendMessageInGroupWithAt("?", group.getId(), q);
                        Thread.sleep(rand.nextInt(4) + 2 * 1000);
                        MessageTools.sendMessageInGroup("干嘛", group.getId());
                        GroupMessageBroadcast.INSTANCE.add(receiver0 = new GroupMessageBroadcast.GroupMessageReceiver() {
                            @Override
                            public void onReceive(long who, long from, String text) {
                                if (text.startsWith("没干嘛"))
                                    MessageTools.sendMessageInGroup("哦~", group.getId());
                                else if (text.startsWith("有事"))
                                    MessageTools.sendMessageInGroup("有什么事(不懈", group.getId());
                                threads.submit(() -> GroupMessageBroadcast.INSTANCE.remove(receiver0));
                            }
                        });
                        break;
                    case 2:
                        Thread.sleep(rand.nextInt(3) + 2 * 1000);
                        MessageTools.sendMessageInGroupWithAt("????", group.getId(), q);
                        Thread.sleep(rand.nextInt(4) + 2 * 1000);
                        MessageTools.sendMessageInGroup("又干嘛", group.getId());
                        GroupMessageBroadcast.INSTANCE.add(receiver0 = new GroupMessageBroadcast.GroupMessageReceiver() {
                            @Override
                            public void onReceive(long who, long from, String text) {
                                if (text.startsWith("没干嘛"))
                                    MessageTools.sendMessageInGroup("淦!", group.getId());
                                else if (text.startsWith("有事"))
                                    MessageTools.sendMessageInGroup("有事???(有事说,有屁放", group.getId());
                                threads.submit(() -> GroupMessageBroadcast.INSTANCE.remove(receiver0));
                            }
                        });
                        break;
                    case 3:
                        Thread.sleep(rand.nextInt(3) + 2 * 1000);
                        MessageTools.sendMessageInGroupWithAt("???????", group.getId(), q);
                        Thread.sleep(rand.nextInt(4) + 2 * 1000);
                        MessageTools.sendMessageInGroup("淦", group.getId());
                        GroupMessageBroadcast.INSTANCE.add(receiver0 = new GroupMessageBroadcast.GroupMessageReceiver() {
                            @Override
                            public void onReceive(long who, long from, String text) {
                                if (text.startsWith("急了")) {
                                    try {
                                        MessageTools.sendMessageInGroup("淦!!", group.getId());
                                        Thread.sleep(1000);
                                        MessageTools.sendMessageInGroupWithAt("别烦我 =_=", group.getId(), q);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                } else if (text.startsWith("没干嘛"))
                                    MessageTools.sendMessageInGroup("焯!-走你", group.getId());
                                threads.submit(() -> GroupMessageBroadcast.INSTANCE.remove(receiver0));
                            }
                        });
                        break;
                    case 4:
                        Thread.sleep(rand.nextInt(3) + 2 * 1000);
                        MessageTools.sendMessageInGroupWithAt("焯", group.getId(), q);
                        Thread.sleep(rand.nextInt(2) + 2 * 1000);
                        event.getFrom().nudge().sendTo(group);
                        Thread.sleep(rand.nextInt(2) + 1 * 1000);
                        MessageTools.sendMessageInGroup("淦", group.getId());
                        break;
                    case 5:
                        Thread.sleep(rand.nextInt(3) + 2 * 1000);
                        MessageTools.sendMessageInGroupWithAt("淦!", group.getId(), q);
                        Thread.sleep(rand.nextInt(2) + 1 * 1000);
                        MessageTools.sendMessageInGroupWithAt("毁灭吧,", group.getId(), q);
                        break;
                    case 6:
                        Thread.sleep(rand.nextInt(3) + 2 * 1000);
                        MessageTools.sendMessageInGroup("阿巴巴巴吧", group.getId());
                        Thread.sleep(rand.nextInt(2) + 1 * 1000);
                        MessageTools.sendMessageInGroup("哎", group.getId());
                        GroupMessageBroadcast.INSTANCE.add(receiver0 = new GroupMessageBroadcast.GroupMessageReceiver() {
                            @Override
                            public void onReceive(long who, long from, String text) {
                                if (text.startsWith("你咋了")) {
                                    MessageTools.sendMessageInGroup("没咋,就累了", group.getId());
                                }
                                threads.submit(() -> GroupMessageBroadcast.INSTANCE.remove(receiver0));
                            }
                        });
                        break;
                    case 7:
                        Thread.sleep(rand.nextInt(3) + 2 * 1000);
                        MessageTools.sendMessageInGroup("阿巴巴巴吧阿巴巴巴吧阿巴巴巴吧阿巴巴巴吧阿巴巴巴吧阿巴巴巴吧", group.getId());
                        Thread.sleep(rand.nextInt(2) + 1 * 1000);
                        MessageTools.sendMessageInGroup("不理你们了,", group.getId());
                        GroupMessageBroadcast.INSTANCE.add(receiver0 = new GroupMessageBroadcast.GroupMessageReceiver() {
                            @Override
                            public void onReceive(long who, long from, String text) {
                                if (text.startsWith("别啊")) {
                                    try {
                                        MessageTools.sendMessageInGroup("?为啥", group.getId());
                                        Thread.sleep(rand.nextInt(5) + 1 * 1000);
                                        MessageTools.sendMessageInGroup("想想", group.getId());
                                        int r = rand.nextInt(2);
                                        if (r == 0) {
                                            nudgeC = 0;
                                            Thread.sleep(rand.nextInt(5) + 5 * 1000);
                                            MessageTools.sendMessageInGroup("最后信你一次", group.getId());
                                        } else {
                                            Thread.sleep(rand.nextInt(5) + 5 * 1000);
                                            MessageTools.sendMessageInGroup("你们自己玩吧", group.getId());
                                        }
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                                threads.submit(() -> GroupMessageBroadcast.INSTANCE.remove(receiver0));
                            }
                        });
                        break;
                }
                working = false;
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
