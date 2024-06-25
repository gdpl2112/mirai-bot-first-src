package io.github.kloping.kzero.mirai.listeners;

import io.github.kloping.spt.annotations.Action;
import io.github.kloping.spt.annotations.AllMess;
import io.github.kloping.spt.annotations.Before;
import io.github.kloping.spt.annotations.Controller;
import io.github.kloping.spt.exceptions.NoRunException;
import io.github.kloping.kzero.main.ResourceSet;
import io.github.kloping.kzero.main.api.KZeroBot;
import io.github.kloping.kzero.main.api.MessagePack;
import io.github.kloping.kzero.main.api.MessageType;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.Member;
import net.mamoe.mirai.event.events.MemberJoinEvent;
import net.mamoe.mirai.event.events.MemberLeaveEvent;
import net.mamoe.mirai.message.data.Face;
import net.mamoe.mirai.message.data.MessageChainBuilder;

/**
 * @author github.kloping
 */
@Controller
public class Exc0Controller {
    @Before
    public void before(@AllMess String msg, KZeroBot kZeroBot, MessagePack pack) {
        if (!(kZeroBot.getSelf() instanceof Bot)) throw new NoRunException("mirai-bot专属扩展");
    }

    @Action("MemberJoinEvent")
    public void onMemberJoined(MessagePack pack, KZeroBot bot) {
        if (!(pack.getRaw() instanceof MemberJoinEvent)) return;
        MemberJoinEvent event = (MemberJoinEvent) pack.getRaw();
        MessageChainBuilder builder = new MessageChainBuilder();
        builder.append("欢迎新人,芜湖~~").append(new Face(Face.DA_CALL)).append("\n");
        builder.append("QQ号:").append(event.getMember().getId() + "").append("\r\n");
        builder.append("QQ昵称:").append(event.getMember().getNick()).append("\r\n");
        builder.append("备注: ").append("您是本群的第").append(String.valueOf(event.getGroup().getMembers().size() + 1)).append("位成员哦").append(new Face(13));
        event.getGroup().sendMessage(builder.build());
        bot.getAdapter().sendMessage(MessageType.GROUP, String.valueOf(event.getGroupId()), String.format(ResourceSet.FinalFormat.PIC_FORMAT0, "https://api.andeer.top/API/welcome.php?qq=" + event.getMember().getId() + "&exit=off"));
    }

    @Action("MemberLeaveEvent")
    public void onMemberLeft(MessagePack pack, KZeroBot bot) {
        if (pack.getRaw() instanceof MemberLeaveEvent.Quit) {
            MemberLeaveEvent.Quit event = (MemberLeaveEvent.Quit) pack.getRaw();
            MessageChainBuilder builder = new MessageChainBuilder();
            builder.append("有个人,从小道溜了,好难过").append(new Face(Face.NAN_GUO)).append("\n");
            builder.append("群里只剩").append(String.valueOf(event.getGroup().getMembers().size())).append("位成员了\n");
            builder.append("(" + event.getUser().getId() + ")");
            event.getGroup().sendMessage(builder.build());
            bot.getAdapter().sendMessage(MessageType.GROUP, String.valueOf(event.getGroupId()), String.format(ResourceSet.FinalFormat.PIC_FORMAT0, "https://api.andeer.top/API/welcome.php?qq=" + event.getMember().getId() + "&exit=on"));
        } else if (pack.getRaw() instanceof MemberLeaveEvent.Kick) {
            MemberLeaveEvent.Kick event = (MemberLeaveEvent.Kick) pack.getRaw();
            MessageChainBuilder builder = new MessageChainBuilder();
            builder.append("有个人,被踢出群聊").append(new Face(Face.HAI_PA)).append("\n");
            Member member = event.getOperator();
            if (member == null) return;
            builder.append("\"操作者\":").append(member == null ? event.getBot().getNick() : member.getNameCard()).append("\r\n");
            builder.append("群里只剩").append(String.valueOf(event.getGroup().getMembers().size() + 1)).append("位成员了\n");
            builder.append("(" + event.getUser().getId() + ")");
            event.getGroup().sendMessage(builder.build());
            bot.getAdapter().sendMessage(MessageType.GROUP, String.valueOf(event.getGroupId()), String.format(ResourceSet.FinalFormat.PIC_FORMAT0, "https://api.andeer.top/API/welcome.php?qq=" + event.getMember().getId() + "&exit=on"));
        }
    }
}
