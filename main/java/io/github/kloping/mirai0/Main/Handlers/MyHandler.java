package io.github.kloping.mirai0.Main.Handlers;

import Project.broadcast.game.GroupMessageBroadcast;
import Project.broadcast.normal.MessageBroadcast;
import Project.controllers.auto.ControllerSource;
import Project.controllers.auto.ControllerTool;
import Project.controllers.normalController.EntertainmentController;
import Project.controllers.normalController.SessionController;
import Project.dataBases.DataBase;
import io.github.kloping.MySpringTool.StarterApplication;
import io.github.kloping.mirai0.Main.ITools.Client;
import io.github.kloping.mirai0.Main.ITools.EventTools;
import io.github.kloping.mirai0.Main.ITools.MemberTools;
import io.github.kloping.mirai0.Main.Resource;
import io.netty.buffer.Unpooled;
import kotlin.coroutines.CoroutineContext;
import net.mamoe.mirai.contact.AnonymousMember;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.Member;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.*;
import net.mamoe.mirai.message.data.Face;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import org.jetbrains.annotations.NotNull;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static io.github.kloping.MySpringTool.StarterApplication.Setting.INSTANCE;
import static io.github.kloping.mirai0.Main.Handlers.CapHandler.CAP_2;
import static io.github.kloping.mirai0.Main.Handlers.CapHandler.join;
import static io.github.kloping.mirai0.Main.ITools.MemberTools.getUser;
import static io.github.kloping.mirai0.Main.Resource.BOT;

/**
 * @author github-kloping
 */
public class MyHandler extends SimpleListenerHost {
    public static final Map<Long, io.github.kloping.mirai0.commons.Group> HIST_GROUP_MAP = new ConcurrentHashMap<>();
    private static final ExecutorService DAE_THREADS = Executors.newFixedThreadPool(10);
    public static MemberJoinRequestEvent joinRequestEvent;

    static {
//        Resource.START_AFTER.add(() -> {
//            io.github.kloping.mirai0.commons.User.create(BOT.getId()
//                    , BOT.getGroups().stream().iterator().next().getId()
//                    , BOT.getNick(), BOT.getNick());
//        });
    }

    public MyHandler() {
        super();
    }

    public MyHandler(@NotNull CoroutineContext coroutineContext) {
        super(coroutineContext);
    }

    private static void eveEnd(String text, long id, io.github.kloping.mirai0.commons.Group eGroup, Group group, Member member, MessageChain message) {
        DAE_THREADS.submit(() -> {
            DataBase.addTimes(1, id);
            GroupMessageBroadcast.INSTANCE.broadcast(id, eGroup.getId(), text.trim());
        });
    }

    @Override
    public void handleException(@NotNull CoroutineContext context, @NotNull Throwable exception) {
        exception.printStackTrace();
    }

    @EventHandler
    public void onMessage(@NotNull GroupMessageEvent event) throws Exception {
        if (CapHandler.CAPING.containsKey(event.getSender().getId())) {
            if (CAP_2.get(event.getSender().getId()).getId() == event.getGroup().getId()) {
                CapHandler.cap(event.getSender().getId(), EventTools.getStringFromGroupMessageEvent(event, true));
            }
        }
        if (event.getSender() instanceof AnonymousMember) {
            return;
        }
        String text = null;
        io.github.kloping.mirai0.commons.Group eGroup = null;
        Group group = null;
        long id = -1;
        try {
            id = event.getSender().getId();
            boolean inS = SessionController.INSTANCE.contains(id);
            group = event.getGroup();
            eGroup = io.github.kloping.mirai0.commons.Group.create(group.getId(), group.getName(), HIST_GROUP_MAP);
            io.github.kloping.mirai0.commons.User eUser = io.github.kloping.mirai0.commons.User.create(id, group.getId(), group.get(id).getNick(), group.get(id).getNameCard());
            text = EventTools.getStringFromGroupMessageEvent(event, !inS, id);
            if (!inS) {
                if (ControllerTool.canGroup(group.getId())) {
                    MessageBroadcast.INSTANCE.broadcast(id, group.getId(), text);
                }
                StarterApplication.executeMethod(id, text, id, eUser, eGroup, 0);
            } else {
                SessionController.INSTANCE.gotoSession(group, text, id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            MemberTools.RECENT_SPEECHES.put(id, group.getId());
            if (ControllerTool.canGroup(group.getId())) {
                ControllerSource.emojiCompositeListenerHost.onMessage(event);
            }
            eveEnd(text, id, eGroup, group, event.getSender(), event.getMessage());
            if (INSTANCE.getActionManager().mather(text) == null) {
                if (Client.CHContext != null && Client.INSTANCE.getGid() == event.getGroup().getId()) {
                    Client.CHContext.writeAndFlush(Unpooled.copiedBuffer(MemberTools.getNameFromGroup(id, eGroup) + ": " + text, StandardCharsets.UTF_8));
                }
            }
        }
    }

    @EventHandler
    public void onMessage(@NotNull FriendMessageEvent event) throws Exception {
        if (!Resource.Switch.AllK) {
            return;
        }
        if (event.getSender() instanceof AnonymousMember) {
            return;
        }
        String text = null;
        io.github.kloping.mirai0.commons.Group eGroup = null;
        Group group = null;
        MessageChain chain = null;
        long id = -1;
        try {
            chain = event.getMessage();
            id = event.getSender().getId();
            group = getCg(id);
            eGroup = io.github.kloping.mirai0.commons.Group.create(group.getId(), group.getName(), HIST_GROUP_MAP);
            io.github.kloping.mirai0.commons.User eUser = getUser(id);
            text = EventTools.getStringFromMessageChain(event.getMessage(), id);
            if (INSTANCE.getActionManager().mather(text) != null) {
                StarterApplication.executeMethod(id, text, id, eUser, eGroup, 1);
            } else {
                event.getSender().sendMessage(EntertainmentController.otherService.talk(text));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @EventHandler
    public void onMessage(@NotNull GroupTempMessageEvent event) throws Exception {
        if (!Resource.Switch.AllK) {
            return;
        }
        if (event.getSender() instanceof AnonymousMember) {
            return;
        }
        String text = null;
        io.github.kloping.mirai0.commons.Group eGroup = null;
        Group group = null;
        MessageChain chain = null;
        long id = -1;
        try {
            chain = event.getMessage();
            id = event.getSender().getId();
            group = event.getGroup();
            eGroup = io.github.kloping.mirai0.commons.Group.create(group.getId(), group.getName(), HIST_GROUP_MAP);
            io.github.kloping.mirai0.commons.User eUser = getUser(id);
            text = EventTools.getStringFromMessageChain(event.getMessage(), id);
            if (INSTANCE.getActionManager().mather(text) != null) {
                StarterApplication.executeMethod(id, text, id, eUser, eGroup, 1);
            } else {
                event.getSender().sendMessage(EntertainmentController.otherService.talk(text));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Group getCg(long id) {
        for (Group group : BOT.getGroups()) {
            if (group.contains(id)) {
                return group;
            }
        }
        return null;
    }

    @EventHandler
    public void onMemberRequest(@NotNull MemberJoinRequestEvent event) {
        try {
            if (!ControllerTool.canGroup(event.getGroup().getId())) {
                return;
            }
            MessageChainBuilder builder = new MessageChainBuilder();
            joinRequestEvent = event;
            builder.append("收到加群申请:").append("\r\n");
            builder.append(Contact.uploadImage(event.getGroup(), new URL(io.github.kloping.mirai0.unitls.Tools.Tool.tool.getTouUrl(event.getFromId())).openStream()))
                    .append("\r\n");
            builder.append("QQ号:").append(event.getFromId() + "")
                    .append("\r\n");
            builder.append("QQ昵称:").append(event.getFromNick())
                    .append("\r\n");
            builder.append("邀请者:").append(event.getInvitor() == null ? "无" :
                    String.format("%s(%s)", event.getInvitor().getNameCard(), event.getInvitorId())).append("\r\n");
            builder.append("请管理员回复(通过/不通过)");
            event.getGroup().sendMessage(builder.build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @EventHandler
    public void onMemberJoined(MemberJoinEvent event) {
        if (!ControllerTool.canGroup(event.getGroup().getId())) {
            return;
        }
        MessageChainBuilder builder = new MessageChainBuilder();
        builder.append("欢迎新人,芜湖~~").append(new Face(Face.DA_CALL)).append("\n");
        builder.append("QQ号:").append(event.getMember().getId() + "").append("\r\n");
        builder.append("QQ昵称:").append(event.getMember().getNick()).append("\r\n");
        builder.append("备注: ").append("您是本群的第").append(String.valueOf(event.getGroup().getMembers().size() + 1))
                .append("位成员哦").append(new Face(13));
        event.getGroup().sendMessage(builder.build());
        if (event.getGroup().get(event.getBot().getId()).getPermission().getLevel() >= 1) {
            join(event.getMember().getId(), event.getGroup());
        }
    }

    @EventHandler
    public void onMemberLeft(MemberLeaveEvent.Quit event) {
        if (!ControllerTool.canGroup(event.getGroup().getId())) {
            return;
        }
        MessageChainBuilder builder = new MessageChainBuilder();
        builder.append("有个人,从小道溜了,好难过").append(new Face(Face.NAN_GUO)).append("\n");
        builder.append("群里只剩").append(String.valueOf(event.getGroup().getMembers().size()))
                .append("位成员了\n");
        builder.append("(" + event.getUser().getId() + ")");
        event.getGroup().sendMessage(builder.build());
    }

    @EventHandler
    public void onMemberLeave(MemberLeaveEvent.Kick event) {
        if (!ControllerTool.canGroup(event.getGroup().getId())) {
            return;
        }
        MessageChainBuilder builder = new MessageChainBuilder();
        builder.append("有个人,被踢出群聊").append(new Face(Face.HAI_PA)).append("\n");
        Member member = event.getOperator();
        if (member == null) {
            return;
        }
        builder.append("\"操作者\":").append(member == null ? event.getBot().getNick() : member.getNameCard())
                .append("\r\n");
        builder.append("群里只剩").append(String.valueOf(event.getGroup().getMembers().size() + 1))
                .append("位成员了\n");
        builder.append("(" + event.getUser().getId() + ")");
        event.getGroup().sendMessage(builder.build());
    }

    /*@EventHandler
    public void onMemberNameCardModify(MemberCardChangeEvent event) {
        if (!ControllerTool.CanGroup(event.getGroup().getId())) return;
        MessageChainBuilder builder = new MessageChainBuilder();
        builder.append("有个靓仔偷偷改了群昵称,还以为我不知道").append(new Face(Face.XIE_YAN_XIAO)).append("\n");
        builder.append("谁: ").append(event.getMember().getId() + "").append("\n");
        builder.append("旧: ").append(event.getOrigin()).append("\n");
        builder.append("新: ").append(event.getNew()).append("");
        event.getGroup().sendMessage(builder.build());
    }*/

    /*
    private static final Map<GroupHonorType, String> gs = new ConcurrentHashMap<>();

    static {
        gs.put(GroupHonorType.TALKATIVE, "龙王");
        gs.put(GroupHonorType.PERFORMER, "群聊之火");
        gs.put(GroupHonorType.LEGEND, "群聊炽焰");
        gs.put(GroupHonorType.STRONG_NEWBIE, "冒尖小春笋");
        gs.put(GroupHonorType.EMOTION, "快乐源泉");
        gs.put(GroupHonorType.ACTIVE, "活跃头衔");
        gs.put(GroupHonorType.EXCLUSIVE, "特殊头衔");
        gs.put(GroupHonorType.MANAGE, "管理头衔");
    }

    @EventHandler
    public void onMemberHonorChangeEvent_Achieve(MemberHonorChangeEvent.Achieve event) {
        if (!ControllerTool.CanGroup(event.getGroup().getId())) return;
        MessageChainBuilder builder = new MessageChainBuilder();
        builder.append("恭喜恭喜\n");
        String nameCard = event.getMember().getId() == event.getBot().getId() ? "我"
                : "\"" + event.getMember().getNameCard() + "\"";
        builder.append("快来祝贺 ").append(nameCard).append("\n");
        builder.append("ta 获得了").append(gs.get(event.getHonorType())).append("\n");
        builder.append(new Face(Face.QING_ZHU));
        event.getGroup().sendMessage(builder.build());
    }*/

    /*
    @EventHandler
    public void onMemberHonorChangeEvent_Lose(MemberHonorChangeEvent.Lose event) {
        if (!ControllerTool.CanGroup(event.getGroup().getId())) return;
        MessageChainBuilder builder = new MessageChainBuilder();
        builder.append("哦吼..\n");
        String nameCard = event.getMember().getId() == event.getBot().getId() ? "我"
                : "\"" + event.getMember().getNameCard() + "\"";
        builder.append("ta 失去了").append(gs.get(event.getHonorType())).append("\n");
        builder.append(new Face(Face.SAO_RAO));
        event.getGroup().sendMessage(builder.build());
    }*/
}