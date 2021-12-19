package io.github.kloping.Mirai.Main.Handlers;

import Project.Tools.Tool;
import io.github.kloping.Mirai.Main.ITools.EventTools;
import io.github.kloping.Mirai.Main.ITools.Saver;
import io.github.kloping.Mirai.Main.Resource;
import io.github.kloping.MySpringTool.annotations.Action;
import io.github.kloping.MySpringTool.annotations.Controller;
import io.github.kloping.MySpringTool.h1.impl.AutomaticWiringParamsImpl;
import io.github.kloping.MySpringTool.h1.impl.InstanceCraterImpl;
import io.github.kloping.MySpringTool.h1.impl.component.ActionManagerImpl;
import io.github.kloping.MySpringTool.h1.impl.component.ClassManagerImpl;
import io.github.kloping.MySpringTool.h1.impl.component.ContextManagerImpl;
import io.github.kloping.MySpringTool.h1.impl.component.MethodManagerImpl;
import io.github.kloping.MySpringTool.interfaces.component.ActionManager;
import io.github.kloping.MySpringTool.interfaces.component.ClassManager;
import io.github.kloping.MySpringTool.interfaces.component.ContextManager;
import io.github.kloping.MySpringTool.interfaces.component.MethodManager;
import io.github.kloping.MySpringTool.interfaces.entitys.MatherResult;
import io.github.kloping.arr.Class2OMap;
import kotlin.coroutines.CoroutineContext;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.AnonymousMember;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.MemberPermission;
import net.mamoe.mirai.contact.NormalMember;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.FriendMessageEvent;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.event.events.GroupMessageSyncEvent;
import net.mamoe.mirai.message.data.*;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Controller
public class LittleHandler extends SimpleListenerHost {

    public LittleHandler(Bot bot) {
        super();
        LittleHandler.bot = bot;
    }

    public static ContextManager contextManager = new ContextManagerImpl();
    public static ActionManager actionManager = null;
    private static ClassManager classManager;
    private static Bot bot;

    public static void init() {
        classManager = new ClassManagerImpl(
                new InstanceCraterImpl(), contextManager, new AutomaticWiringParamsImpl(), actionManager
        );
        MethodManager methodManager = new MethodManagerImpl(new AutomaticWiringParamsImpl(), classManager);
        actionManager = new ActionManagerImpl(classManager);
        try {
            classManager.add(LittleHandler.class);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handleException(@NotNull CoroutineContext context, @NotNull Throwable exception) {
        super.handleException(context, exception);
    }

    boolean init = false;
    public static final List<Long> hasRebot = new CopyOnWriteArrayList<>();

    static {
        hasRebot.add(291841860L);
        hasRebot.add(3597552450L);
    }

    private static final Map<Long, Message> upMessages = new ConcurrentHashMap<>();

    @EventHandler
    public void onMessage(@NotNull GroupMessageEvent event) throws Exception {
        upMessages.put(event.getGroup().getId(), event.getMessage());
        if (event.getSender() instanceof AnonymousMember) return;
        Group group = event.getGroup();
        long gid = group.getId();
        Resource.threads.execute(() -> {
            for (long q : hasRebot) if (group.contains(q)) return;
            try {
                String json = MessageChain.serializeToJsonString(event.getMessage());
                Saver.saveMessage(json, gid, event.getSender().getId());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        if (gid != 278681553L) return;
        String text = EventTools.getStringFromGroupMessageEvent(event);
        Long q2 = event.getSender().getId();
        if (text.startsWith("我要头衔")) {
            text = text.replace("我要头衔", "").replaceAll(" ", "");
            if (!Tool.isIlleg(text) && !text.isEmpty()) {
                group.get(q2).setSpecialTitle(text);
                group.sendMessage("=>O了");
            } else group.sendMessage("敏感字节!");
        }
    }

    @EventHandler
    public void onFriendMessage(FriendMessageEvent event) {
        Resource.threads.execute(() -> {
            if (event.getSender().getId() == bot.getId()) {
                String m1 = (event.getMessage().get(1).toString()).trim();
                if (m1.startsWith("/get")) {
                    m1 = m1.substring(4);
                    String[] ss = m1.split(":");
                    Long qid = Long.parseLong(ss[0].trim());
                    int[] ints = Tool.StringToInts(ss[1].trim());
                    try {
                        String[] sss = Saver.getTexts2(bot.getId(), qid.longValue(), ints);
                        for (String s1 : sss) {
                            MessageChain o = MessageChain.deserializeFromJsonString(s1);
                            event.getSender().sendMessage(o);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return;
            }
            try {
                String json = MessageChain.serializeToJsonString(event.getMessage());
                Saver.saveMessage2(json, bot.getId(), event.getSender().getId());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @EventHandler
    public void sync(GroupMessageSyncEvent event) {
        if (event.getMessage().size() < 1) return;
        if (!(event.getMessage().get(1) instanceof PlainText
                || event.getMessage().get(1) instanceof QuoteReply)) return;
        String text = EventTools.getStringFromMessageChain(event.getMessage()).trim();
        if (text.startsWith("/")) {
            MatherResult result = actionManager.mather(text.substring(1));
            if (result != null) {
                MessageSource.recall(event.getMessage());
                for (Method method : result.getMethods()) {
                    try {
                        method.invoke(this, event);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Action("++1")
    public void mm(GroupMessageSyncEvent event) {
        if (upMessages.containsKey(event.getGroup().getId()))
            event.getGroup().sendMessage(upMessages.get(event.getGroup().getId()));
    }

    @Action("++2")
    public void mm2(GroupMessageSyncEvent event) {
        if (upMessages.containsKey(event.getGroup().getId())) {
            event.getGroup().sendMessage(upMessages.get(event.getGroup().getId()));
            event.getGroup().sendMessage(upMessages.get(event.getGroup().getId()));
        }
    }

    @Action("setAdmin.+")
    public void m1(GroupMessageSyncEvent event) {
        if (event.getSender().getPermission() == MemberPermission.MEMBER) return;
        Class2OMap co = Class2OMap.create(event.getMessage());
        At at = co.get(At.class);
        if (at == null) return;
        NormalMember m1 = event.getGroup().get(at.getTarget());
        if (m1.getPermission().getLevel() < event.getSender().getPermission().getLevel())
            m1.modifyAdmin(true);
    }

    @Action("unAdmin.+")
    public void m2(GroupMessageSyncEvent event) {
        if (event.getSender().getPermission() == MemberPermission.MEMBER) return;
        Class2OMap co = Class2OMap.create(event.getMessage());
        At at = co.get(At.class);
        if (at == null) return;
        NormalMember m1 = event.getGroup().get(at.getTarget());
        if (m1.getPermission().getLevel() < event.getSender().getPermission().getLevel())
            m1.modifyAdmin(false);
    }

    @Action("mute.+")
    public void m3(GroupMessageSyncEvent event) {
        if (event.getSender().getPermission() == MemberPermission.MEMBER) return;
        Class2OMap co = Class2OMap.create(event.getMessage());
        At at = co.get(At.class);
        if (at == null) return;
        String t = EventTools.getStringFromMessageChain(event.getMessage());
        NormalMember m1 = event.getGroup().get(at.getTarget());
        if (m1.getPermission().getLevel() < event.getSender().getPermission().getLevel()) {
            String s1 = Tool.findNumberFromString(t.replace(String.valueOf(at.getTarget()), ""));
            m1.mute(Integer.parseInt(s1));
        }
    }

    @Action("unmute.+")
    public void m4(GroupMessageSyncEvent event) {
        if (event.getSender().getPermission() == MemberPermission.MEMBER) return;
        Class2OMap co = Class2OMap.create(event.getMessage());
        At at = co.get(At.class);
        if (at == null) return;
        String t = EventTools.getStringFromMessageChain(event.getMessage());
        NormalMember m1 = event.getGroup().get(at.getTarget());
        if (m1.getPermission().getLevel() < event.getSender().getPermission().getLevel())
            m1.unmute();
    }

    @Action("setTou.+")
    public void m5(GroupMessageSyncEvent event) {
        if (event.getSender().getPermission() != MemberPermission.OWNER) return;
        Class2OMap co = Class2OMap.create(event.getMessage());
        At at = co.get(At.class);
        if (at == null) return;
        String t = EventTools.getStringFromMessageChain(event.getMessage());
        String name = event.getMessage().get(3).toString();
        NormalMember m1 = event.getGroup().get(at.getTarget());
        m1.setSpecialTitle(name);
    }

    @Action("recall.+")
    public void m6(GroupMessageSyncEvent event) {
        if (event.getSender().getPermission() == MemberPermission.MEMBER) return;
        QuoteReply qr = (QuoteReply) event.getMessage().get(1);
        At at = null;
        try {
            at = (At) event.getMessage().get(3);
        } catch (Exception e) {
            at = (At) event.getMessage().get(2);
        }
        String t = EventTools.getStringFromMessageChain(event.getMessage());
        NormalMember m1 = event.getGroup().get(at.getTarget());
        if (m1.getPermission().getLevel() < event.getSender().getPermission().getLevel())
            MessageSource.recall(qr.getSource());
    }

//    @Action("sendAllGroup.+")
//    private void m7(String m) {
//        String ms = m.substring("sendAllGroup".length());
//        for (Group group : bot.getGroups()) {
//            group.sendMessage(ms);
//        }
//    }
}
