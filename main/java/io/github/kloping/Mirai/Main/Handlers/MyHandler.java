package io.github.kloping.Mirai.Main.Handlers;

import Project.Controllers.NormalController.CustomController;
import Project.Controllers.SessionController;
import Project.DataBases.DataBase;
import io.github.kloping.Mirai.Main.ITools.EventTools;
import io.github.kloping.Mirai.Main.ITools.Saver;
import io.github.kloping.Mirai.Main.Resource;
import io.github.kloping.MySpringTool.Starter;
import io.github.kloping.MySpringTool.StarterApplication;
import kotlin.coroutines.CoroutineContext;
import net.mamoe.mirai.contact.AnonymousMember;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.Member;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.event.events.MemberJoinRequestEvent;
import net.mamoe.mirai.event.events.NewFriendRequestEvent;
import net.mamoe.mirai.message.action.Nudge;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import org.jetbrains.annotations.NotNull;

import java.net.URL;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static Project.Controllers.SessionController.gotoSession;

public class MyHandler extends SimpleListenerHost {
    public MyHandler() {
        super();
    }

    public MyHandler(@NotNull CoroutineContext coroutineContext) {
        super(coroutineContext);
    }

    @Override
    public void handleException(@NotNull CoroutineContext context, @NotNull Throwable exception) {
        super.handleException(context, exception);
    }

    private static final Map<Long, Entitys.Group> map = new ConcurrentHashMap<>();

    @EventHandler
    public void onMessage(@NotNull GroupMessageEvent event) throws Exception {
        if (!Resource.Switch.AllK) return;
        String text = null;
        Entitys.Group eGroup = null;
        Group group = null;
        MessageChain chain = null;
        long id = -1;
        try {
            if (event.getSender() instanceof AnonymousMember) return;
            chain = event.getMessage();
            id = event.getSender().getId();
            boolean inS = SessionController.contains(id);
            group = event.getGroup();
            eGroup = map.containsKey(group.getId()) ? map.get(group.getId()) : new Entitys.Group(group.getId(), group.getName());
            Entitys.User eUser = new Entitys.User(id, group.getId(), group.get(id).getNick(), group.get(id).getNameCard());
            text = EventTools.getStringFromGroupMessageEvent(event, !inS);
            if (!inS)
//                Starter.ExecuteMethod(id, text, id, eUser, eGroup, 0, event.getMessage());
                StarterApplication.ExecuteMethod(id, text, id, eUser, eGroup, 0);
            else {
                gotoSession(group, text, id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            eveEnd(text, id, eGroup, group, event.getSender(), chain);
        }
    }

    private static void eveEnd(String text, long id, Entitys.Group eGroup, Group group, Member member, MessageChain message) {
        daeThreads.execute(() -> {
            if (!text.trim().isEmpty())
                if (text.contains("[") || !Starter.matcher(text)) {
                    String finalText = text;
                    CustomController.action(id, finalText, eGroup);
                }
            DataBase.addTimes(1, id);
            if (upMessage != null && upMessage.equals(text)) {
                Nudge nudge = member.nudge();
                nudge.sendTo(group);
                group.sendMessage(message);
                upMessage = null;
            } else {
                upMessage = text;
            }
            try {
                String json = MessageChain.serializeToJsonString(message);
                Saver.saveMessage(json, eGroup.getId(), id);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private static final ExecutorService daeThreads = Executors.newFixedThreadPool(10);

    public static String upMessage = null;

    public static final ExecutorService threads = Executors.newFixedThreadPool(10);

    public static MemberJoinRequestEvent joinRequestEvent;

    @EventHandler
    public void onMemberRequest(@NotNull MemberJoinRequestEvent event) {
        try {
            MessageChainBuilder builder = new MessageChainBuilder();
            joinRequestEvent = event;
            builder.append("收到加群申请:").append("\r\n");
            builder.append(Contact.uploadImage(event.getGroup(), new URL(Project.Tools.Tool.getTouUrl(event.getFromId())).openStream())).append("\r\n");
            builder.append("QQ号:").append(event.getFromId() + "").append("\r\n");
            builder.append("QQ昵称:").append(event.getFromNick()).append("\r\n");
            builder.append("邀请者:").append(event.getInvitor() == null ? "无" : String.format("%s(%s)", event.getInvitor().getNameCard(), event.getInvitorId())).append("\r\n");
            builder.append("请管理员回复(通过/不通过)");
            event.getGroup().sendMessage(builder.build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean autoAcceptFriend = false;

    @EventHandler
    public void onAcceFirend(NewFriendRequestEvent event) {
        if (autoAcceptFriend)
            event.accept();
    }

/*
    public static boolean accept = true;

    @EventHandler
    public void autoAcceptJoinRequest(MemberJoinRequestEvent requestEvent) {
        if (accept)
            requestEvent.accept();
        else requestEvent.getGroup().sendMessage(String.format("收到新加群请求:%s",requestEvent.getFromId()));
    }*/
}