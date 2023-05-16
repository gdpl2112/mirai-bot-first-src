package Project.listeners;

import Project.broadcast.game.GroupMessageBroadcast;
import Project.broadcast.normal.MessageBroadcast;
import Project.commons.SpGroup;
import Project.commons.SpUser;
import Project.controllers.auto.ControllerSource;
import Project.controllers.auto.ControllerTool;
import Project.controllers.normalController.SessionController;
import Project.dataBases.DataBase;
import io.github.kloping.MySpringTool.StarterApplication;
import io.github.kloping.mirai.BotInstance;
import io.github.kloping.mirai0.Main.BootstarpResource;
import io.github.kloping.mirai0.Main.iutils.EventUtils;
import io.github.kloping.mirai0.Main.iutils.MemberUtils;
import io.github.kloping.mirai0.Main.iutils.MessageUtils;
import io.github.kloping.mirai0.Main.iutils.MinecraftServerClient;
import io.github.kloping.mirai0.unitls.Tools.Tool;
import io.netty.buffer.Unpooled;
import kotlin.coroutines.CoroutineContext;
import net.mamoe.mirai.contact.*;
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

import static Project.listeners.CapHandler.CAP_TWO;
import static Project.listeners.CapHandler.join;
import static io.github.kloping.MySpringTool.StarterApplication.Setting.INSTANCE;
import static io.github.kloping.mirai0.Main.BootstarpResource.println;
import static io.github.kloping.mirai0.Main.iutils.MemberUtils.getUser;

/**
 * @author github-kloping
 */
public class DefaultHandler extends SimpleListenerHost {
    public static final Map<Long, SpGroup> HIST_GROUP_MAP = new ConcurrentHashMap<>();
    private static final ExecutorService DAE_THREADS = Executors.newFixedThreadPool(10);
    public static MemberJoinRequestEvent joinRequestEvent;

    public DefaultHandler() {
        super();
    }

    public DefaultHandler(@NotNull CoroutineContext coroutineContext) {
        super(coroutineContext);
    }

    private static void eveEnd(String text, long id, SpGroup eGroup, Group group, Member member, MessageChain message) {
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
            if (CAP_TWO.get(event.getSender().getId()).getId() == event.getGroup().getId()) {
                CapHandler.cap(event.getSender().getId(), EventUtils.messageEvent2String(event, true));
            }
        }
        if (event.getSender() instanceof AnonymousMember) {
            return;
        }
        String text = null;
        SpGroup eGroup = null;
        Group group = null;
        long id = -1;
        try {
            id = event.getSender().getId();
            boolean inS = ControllerSource.sessionController.contains(id);
            group = event.getGroup();
            eGroup = SpGroup.create(group.getId(), group.getName(), HIST_GROUP_MAP);
            SpUser eUser = SpUser.create(id, group.getId(), group.get(id).getNick(), group.get(id).getNameCard());
            text = EventUtils.messageEvent2String(event, !inS, id);
            if (!inS) {
                if (ControllerTool.canGroup(group.getId())) {
                    MessageBroadcast.INSTANCE.broadcast(id, group.getId(), text);
                }
                StarterApplication.executeMethod(id, text, id, eUser, eGroup, 0, event);
            } else {
                ControllerSource.sessionController.gotoSession(group, text, id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            MemberUtils.RECENT_SPEECHES.put(id, group.getId());
            if (ControllerTool.canGroup(group.getId())) {
                ControllerSource.emojiCompositeListenerHost.onMessage(event);
            }
            eveEnd(text, id, eGroup, group, event.getSender(), event.getMessage());
            if (INSTANCE.getActionManager().mather(text) == null) {
                if (MinecraftServerClient.CHContext != null && MinecraftServerClient.INSTANCE.getGid() == event.getGroup().getId()) {
                    MinecraftServerClient.CHContext.writeAndFlush(Unpooled.copiedBuffer(MemberUtils.getNameFromGroup(id, eGroup) + ": " + text, StandardCharsets.UTF_8));
                }
            }
        }
    }

    @EventHandler
    public void onEvent(@NotNull SignEvent event) {
        NormalMember member = (NormalMember) event.getUser();
        long id = member.getId();
        Group group = member.getGroup();
        StarterApplication.executeMethod(id, "签到", id, getUser(id), SpGroup.create(group.getId(), group.getName(), HIST_GROUP_MAP), 0, event);
        StarterApplication.executeMethod(id, "魂师签到", id, getUser(id), SpGroup.create(group.getId(), group.getName(), HIST_GROUP_MAP), 0, event);
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
            builder.append(Contact.uploadImage(event.getGroup(), new URL(io.github.kloping.mirai0.unitls.Tools.Tool.INSTANCE.getTouUrl(event.getFromId())).openStream())).append("\r\n");
            builder.append("QQ号:").append(event.getFromId() + "").append("\r\n");
            builder.append("QQ昵称:").append(event.getFromNick()).append("\r\n");
            builder.append("邀请者:").append(event.getInvitor() == null ? "无" : String.format("%s(%s)", event.getInvitor().getNameCard(), event.getInvitorId())).append("\r\n");
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
        builder.append("备注: ").append("您是本群的第").append(String.valueOf(event.getGroup().getMembers().size() + 1)).append("位成员哦").append(new Face(13));
        event.getGroup().sendMessage(builder.build());
        if (event.getGroup().get(event.getBot().getId()).getPermission().getLevel() >= 1) {
            long iq = -1;
            if (event instanceof MemberJoinEvent.Invite) {
                iq = ((MemberJoinEvent.Invite) event).getInvitor().getId();
            }
            join(event.getMember().getId(), event.getGroup(), iq);
        }
    }

    @EventHandler
    public void onMemberLeft(MemberLeaveEvent.Quit event) {
        if (!ControllerTool.canGroup(event.getGroup().getId())) {
            return;
        }
        MessageChainBuilder builder = new MessageChainBuilder();
        builder.append("有个人,从小道溜了,好难过").append(new Face(Face.NAN_GUO)).append("\n");
        builder.append("群里只剩").append(String.valueOf(event.getGroup().getMembers().size())).append("位成员了\n");
        builder.append("(" + event.getUser().getId() + ")");
        event.getGroup().sendMessage(builder.build());
        MessageUtils.INSTANCE.sendMessageInGroup(Tool.INSTANCE.pathToImg("https://api.andeer.top/API/welcome.php?qq=" + event.getUser().getId() + "&exit=on"), event.getGroupId());
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
        builder.append("\"操作者\":").append(member == null ? event.getBot().getNick() : member.getNameCard()).append("\r\n");
        builder.append("群里只剩").append(String.valueOf(event.getGroup().getMembers().size() + 1)).append("位成员了\n");
        builder.append("(" + event.getUser().getId() + ")");
        event.getGroup().sendMessage(builder.build());
        MessageUtils.INSTANCE.sendMessageInGroup(Tool.INSTANCE.pathToImg("https://api.andeer.top/API/welcome.php?qq=" + event.getUser().getId() + "&exit=on"), event.getGroupId());
    }

    @EventHandler
    public void onEvent(BotOnlineEvent event) {
        BootstarpResource.BOT = event.getBot();
        println(String.format("BOT(%s)上线了!!", event.getBot().getId()));
        BotInstance.instance = new BotInstance(event.getBot());
    }
}