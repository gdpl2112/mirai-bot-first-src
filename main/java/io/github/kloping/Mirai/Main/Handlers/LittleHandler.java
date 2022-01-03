package io.github.kloping.Mirai.Main.Handlers;

import Project.Tools.Tool;
import io.github.kloping.Mirai.Main.ITools.EventTools;
import io.github.kloping.Mirai.Main.Resource;
import io.github.kloping.MySpringTool.annotations.Action;
import io.github.kloping.MySpringTool.annotations.Controller;
import io.github.kloping.MySpringTool.h1.impl.AutomaticWiringParamsImpl;
import io.github.kloping.MySpringTool.h1.impl.InstanceCraterImpl;
import io.github.kloping.MySpringTool.h1.impl.component.ActionManagerImpl;
import io.github.kloping.MySpringTool.h1.impl.component.ClassManagerImpl;
import io.github.kloping.MySpringTool.h1.impl.component.ContextManagerImpl;
import io.github.kloping.MySpringTool.interfaces.component.ClassManager;
import io.github.kloping.MySpringTool.interfaces.component.ContextManager;
import io.github.kloping.MySpringTool.interfaces.entitys.MatherResult;
import io.github.kloping.arr.Class2OMap;
import kotlin.coroutines.CoroutineContext;
import net.mamoe.mirai.contact.AnonymousMember;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.MemberPermission;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.*;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;

/**
 * @author github-kloping
 */
@Controller
public class LittleHandler extends SimpleListenerHost {

    public static final String WANT_TITLE = "我要头衔";
    public static final String PRE = "/";
    public static ActionManagerImpl am = null;
    public static ContextManager contextManager;

    static {
        ClassManager classManager = new ClassManagerImpl(
                new InstanceCraterImpl(),
                contextManager = new ContextManagerImpl(),
                new AutomaticWiringParamsImpl(),
                am
        );
        am = new ActionManagerImpl(classManager);
        try {
            classManager.add(LittleHandler.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public LittleHandler() {
        super();
    }

    @Override
    public void handleException(@NotNull CoroutineContext context, @NotNull Throwable exception) {
        exception.printStackTrace();
    }

    @EventHandler
    public void onMessage(@NotNull GroupMessageEvent event) throws Exception {
        if (event.getSender() instanceof AnonymousMember) {
            return;
        }
        Group group = event.getGroup();
        long gid = group.getId();
        long iid = event.getBot().getId();
        long yid = event.getSender().getId();
        if (group.get(iid).getPermission().equals(MemberPermission.OWNER)) {
            String text = EventTools.getStringFromGroupMessageEvent(event);
            if (text.startsWith(WANT_TITLE)) {
                text = text.replaceFirst(WANT_TITLE, "");
                if (!Tool.isIlleg(text) && !text.isEmpty()) {
                    group.get(yid).setSpecialTitle(text);
                    group.sendMessage("=>O了");
                } else {
                    group.sendMessage("敏感字节!");
                }
            }
        }
        if (yid == Resource.superQL) {
            String text = EventTools.getStringFromGroupMessageEvent(event);
            if (group.get(iid).getPermission().getLevel() > 0) {
                if (text.startsWith(PRE)) {
                    text = text.replaceFirst(PRE, "");
                    MatherResult result = am.mather(text);
                    if (result != null) {
                        Class2OMap c2m = Class2OMap.create(event.getMessage());
                        for (Method method : result.getMethods()) {
                            method.invoke(this, event, c2m);
                        }
                    }
                }
            }
        }
    }

    @Action("setAdmin.+")
    public void m1(GroupMessageEvent event, Class2OMap class2OMap) {
        At at = class2OMap.get(At.class);
        if (at == null) {
            event.getSubject().sendMessage("Not Found");
        } else {
            event.getSubject().get(at.getTarget()).modifyAdmin(true);
            event.getSubject().sendMessage("succeed");
        }
    }

    @Action("unAdmin.+")
    public void m2(GroupMessageEvent event, Class2OMap class2OMap) {
        At at = class2OMap.get(At.class);
        if (at == null) {
            event.getSubject().sendMessage("Not Found");
        } else {
            event.getSubject().get(at.getTarget()).modifyAdmin(false);
            event.getSubject().sendMessage("succeed");
        }
    }

    @Action("mute.+")
    public void m3(GroupMessageEvent event, Class2OMap class2OMap) {
        At at = class2OMap.get(At.class);
        PlainText plainText = class2OMap.get(PlainText.class,1);
        if (at == null || plainText == null) {
            event.getSubject().sendMessage("Not Found");
        } else {
            int st = Integer.parseInt(Tool.findNumberFromString(plainText.getContent()));
            event.getSubject().get(at.getTarget()).mute(st);
            event.getSubject().sendMessage("succeed");
        }
    }

    @Action("unmute.+")
    public void m4(GroupMessageEvent event, Class2OMap class2OMap) {
        At at = class2OMap.get(At.class);
        PlainText plainText = class2OMap.get(PlainText.class,1);
        if (at == null || plainText == null) {
            event.getSubject().sendMessage("Not Found");
        } else {
            event.getSubject().get(at.getTarget()).mute(0);
            event.getSubject().sendMessage("succeed");
        }
    }

    @Action("setTou.+")
    public void m5(GroupMessageEvent event, Class2OMap class2OMap) {
        At at = class2OMap.get(At.class);
        PlainText plainText = class2OMap.get(PlainText.class,1);
        if (at == null || plainText == null) {
            event.getSubject().sendMessage("Not Found");
        } else {
            event.getSubject().get(at.getTarget())
                    .setSpecialTitle(plainText.getContent().replaceFirst("/setTou", ""));
            event.getSubject().sendMessage("succeed");
        }
    }

    @Action("recall.+")
    public void m6(GroupMessageEvent event, Class2OMap class2OMap) {
        At at = class2OMap.get(At.class);
        QuoteReply qr = class2OMap.get(QuoteReply.class);
        if (at == null || qr == null) {
            event.getSubject().sendMessage("Not Found");
        } else {
            MessageSource.recall(qr.getSource());
            event.getSubject().sendMessage("succeed");
        }
    }

    @Action("parseJson.+")
    public void m7(GroupMessageEvent event, Class2OMap class2OMap) {
        try {
            PlainText plainText = class2OMap.get(PlainText.class);
            String jsonStr = plainText.getContent().replaceFirst("/parseJson", "");
            MessageChain chain = MessageChain.deserializeFromJsonString(jsonStr);
            event.getSubject().sendMessage(chain);
        } catch (Throwable e) {
            e.printStackTrace();
            event.getSubject().sendMessage(e.getMessage());
        }
    }
}
