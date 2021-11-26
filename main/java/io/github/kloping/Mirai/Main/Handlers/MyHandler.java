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
import net.mamoe.mirai.data.GroupHonorType;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.*;
import net.mamoe.mirai.message.action.Nudge;
import net.mamoe.mirai.message.data.Face;
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

    @EventHandler
    public void onMemberJoined(MemberJoinEvent event) {
        MessageChainBuilder builder = new MessageChainBuilder();
        builder.append("欢迎新人,芜湖~~").append(new Face(Face.DA_CALL)).append("\n");
        builder.append("QQ号:").append(event.getMember().getId() + "").append("\r\n");
        builder.append("QQ昵称:").append(event.getMember().getNick()).append("\r\n");
        builder.append("备注: ").append("您是本群的第").append(String.valueOf(event.getGroup().getMembers().size() + 1))
                .append("位成员哦").append(new Face(13));
        event.getGroup().sendMessage(builder.build());
    }

    @EventHandler
    public void onMemberLeft(MemberLeaveEvent.Quit event) {
        MessageChainBuilder builder = new MessageChainBuilder();
        builder.append("有个人,从小道溜了,好难过").append(new Face(Face.NAN_GUO)).append("\n");
        builder.append("群里只剩").append(String.valueOf(event.getGroup().getMembers().size() + 1))
                .append("位成员了\n");
        builder.append("(" + event.getUser().getId() + ")");
        event.getGroup().sendMessage(builder.build());
    }

    @EventHandler
    public void onMemberLeave(MemberLeaveEvent.Kick event) {
        MessageChainBuilder builder = new MessageChainBuilder();
        builder.append("有个人,被凶狠踢出群聊,好吓机器人").append(new Face(Face.HAI_PA)).append("\n");
        Member member = event.getOperator();
        builder.append("\"凶狠\":").append(member == null ? "原来是我自己" : member.getNameCard())
                .append("\r\n");
        builder.append("群里只剩").append(String.valueOf(event.getGroup().getMembers().size() + 1))
                .append("位成员了\n");
        builder.append("(" + event.getUser().getId() + ")");
        event.getGroup().sendMessage(builder.build());
    }

    @EventHandler
    public void onMemberNameCardModify(MemberCardChangeEvent event) {
        MessageChainBuilder builder = new MessageChainBuilder();
        builder.append("有个靓仔偷偷改了群昵称,还以为我不知道").append(new Face(Face.XIE_YAN_XIAO)).append("\n");
        builder.append("谁: ").append(event.getMember().getId() + "").append("\n");
        builder.append("旧: ").append(event.getOrigin()).append("\n");
        builder.append("新: ").append(event.getNew()).append("");
        event.getGroup().sendMessage(builder.build());
    }

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
        MessageChainBuilder builder = new MessageChainBuilder();
        builder.append("恭喜恭喜\n");
        String nameCard = event.getMember().getId() == event.getBot().getId() ? "我"
                : "\"" + event.getMember().getNameCard() + "\"";
        builder.append("快来祝贺 ").append(nameCard).append("\n");
        builder.append("ta 获得了").append(gs.get(event.getHonorType())).append("\n");
        builder.append(new Face(Face.QING_ZHU));
        event.getGroup().sendMessage(builder.build());
    }

    @EventHandler
    public void onMemberHonorChangeEvent_Lose(MemberHonorChangeEvent.Lose event) {
        MessageChainBuilder builder = new MessageChainBuilder();
        builder.append("哦吼..\n");
        String nameCard = event.getMember().getId() == event.getBot().getId() ? "我"
                : "\"" + event.getMember().getNameCard() + "\"";
        builder.append("ta 失去了").append(gs.get(event.getHonorType())).append("\n");
        builder.append(new Face(Face.SAO_RAO));
        event.getGroup().sendMessage(builder.build());
    }
}