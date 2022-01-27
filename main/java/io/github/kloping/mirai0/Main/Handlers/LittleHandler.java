package io.github.kloping.mirai0.Main.Handlers;

import io.github.kloping.mirai0.unitls.Tools.Tool;
import io.github.kloping.mirai0.Main.ITools.EventTools;
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
import io.github.kloping.file.FileUtils;
import kotlin.coroutines.CoroutineContext;
import net.mamoe.mirai.contact.AnonymousMember;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.MemberPermission;
import net.mamoe.mirai.contact.NormalMember;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.*;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author github-kloping
 */
@Controller
public class LittleHandler extends SimpleListenerHost {

    public static final String WANT_TITLE = "我要头衔";
    public static final String ILLEGAL = "敏感字节!";
    public static String TOK = "设置头衔完成";
    public static final String PRE = "/";
    public static final String PRE0 = "#";
    public static ActionManagerImpl am = null;
    public static ContextManager contextManager;

    public static File file = new File("./superQList.txt");

    public static final Set<Long> SUPER_LIST = new CopyOnWriteArraySet<>();

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
        loadConf();
    }

    public LittleHandler() {
        super();
    }

    private static void loadConf() {
        try {
            FileUtils.testFile(file.getAbsolutePath());
            for (String s : FileUtils.getStringsFromFile(file.getPath())) {
                try {
                    if (s.trim().isEmpty()) continue;
                    long q = Long.parseLong(s.trim());
                    SUPER_LIST.add(q);
                    System.err.println("add SuperQL: " + q);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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
                    group.sendMessage(TOK);
                } else {
                    group.sendMessage(ILLEGAL);
                }
            }
        }
        if (isSuperQ(yid)) {
            String text = EventTools.getStringFromGroupMessageEvent(event);
            if (group.get(iid).getPermission().getLevel() > 0) {
                if (text.startsWith(PRE0)) {
                    text = text.replaceFirst(PRE0, "");
                    MatherResult result = am.mather(text);
                    if (result != null) {
                        MessageSource.recall(event.getSource());
                        Class2OMap c2m = Class2OMap.create(event.getMessage());
                        for (Method method : result.getMethods()) {
                            method.invoke(this, event, c2m).toString();
                        }
                    }
                } else if (text.startsWith(PRE)) {
                    text = text.replaceFirst(PRE, "");
                    MatherResult result = am.mather(text);
                    if (result != null) {
                        MessageSource.recall(event.getSource());
                        Class2OMap c2m = Class2OMap.create(event.getMessage());
                        for (Method method : result.getMethods()) {
                            String arg = method.invoke(this, event, c2m).toString();
                            if (arg != null) {
                                event.getSubject().sendMessage(arg);
                            }
                        }
                    }
                }
            }
        }
    }

    public static boolean isSuperQ(long q) {
        return SUPER_LIST.contains(q);
    }

    @Action("setAdmin.+")
    public String m1(GroupMessageEvent event, Class2OMap class2OMap) {
        At at = class2OMap.get(At.class);
        if (at == null) {
            return "Not Found";
        } else {
            event.getSubject().get(at.getTarget()).modifyAdmin(true);
            return "succeed";
        }
    }

    @Action("unAdmin.+")
    public String m2(GroupMessageEvent event, Class2OMap class2OMap) {
        At at = class2OMap.get(At.class);
        if (at == null) {
            return "Not Found";
        } else {
            event.getSubject().get(at.getTarget()).modifyAdmin(false);
            return "succeed";
        }
    }

    @Action("mute.+")
    public String m3(GroupMessageEvent event, Class2OMap class2OMap) {
        At at = class2OMap.get(At.class);
        PlainText plainText = class2OMap.get(PlainText.class, 1);
        if (at == null || plainText == null) {
            return "Not Found";
        } else {
            int st = Integer.parseInt(Tool.findNumberFromString(plainText.getContent()));
            event.getSubject().get(at.getTarget()).mute(st);
            return "succeed";
        }
    }

    @Action("unmute.+")
    public String m4(GroupMessageEvent event, Class2OMap class2OMap) {
        At at = class2OMap.get(At.class);
        PlainText plainText = class2OMap.get(PlainText.class, 1);
        if (at == null || plainText == null) {
            return "Not Found";
        } else {
            event.getSubject().get(at.getTarget()).mute(0);
            return "succeed";
        }
    }

    @Action("setTou.+")
    public String m5(GroupMessageEvent event, Class2OMap class2OMap) {
        At at = class2OMap.get(At.class);
        PlainText plainText = class2OMap.get(PlainText.class, 1);
        if (at == null || plainText == null) {
            return "Not Found";
        } else {
            event.getSubject().get(at.getTarget())
                    .setSpecialTitle(plainText.getContent().replaceFirst("/setTou", ""));
            return "succeed";
        }
    }

    @Action("recall.+")
    public String m6(GroupMessageEvent event, Class2OMap class2OMap) {
        At at = class2OMap.get(At.class);
        QuoteReply qr = class2OMap.get(QuoteReply.class);
        if (at == null || qr == null) {
            return "Not Found";
        } else {
            MessageSource.recall(qr.getSource());
            return "succeed";
        }
    }

    @Action("parseJson.+")
    public void m7(GroupMessageEvent event, Class2OMap class2OMap) {
        try {
            PlainText plainText = class2OMap.get(PlainText.class);
            String jsonStr = plainText.getContent().replaceFirst("[/|#]parseJson", "");
            MessageChain chain = MessageChain.deserializeFromJsonString(jsonStr);
            event.getSubject().sendMessage(chain);
        } catch (Throwable e) {
            e.printStackTrace();
            event.getSubject().sendMessage(e.getMessage());
        }
    }

    @Action("makeName.+")
    public String m8(GroupMessageEvent event, Class2OMap class2OMap) {
        try {
            At at = class2OMap.get(At.class);
            NormalMember member = event.getGroup().get(at.getTarget());
            PlainText plainText = class2OMap.get(PlainText.class, 1);
            member.setNameCard(plainText.getContent().trim());
            return "ok";
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }
}
