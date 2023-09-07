package io.github.kloping.kzero.mirai;

import io.github.kloping.MySpringTool.annotations.Action;
import io.github.kloping.MySpringTool.annotations.Before;
import io.github.kloping.MySpringTool.annotations.Controller;
import io.github.kloping.MySpringTool.annotations.Param;
import io.github.kloping.MySpringTool.exceptions.NoRunException;
import io.github.kloping.kzero.main.api.KZeroBot;
import io.github.kloping.kzero.main.api.MessagePack;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.MemberPermission;
import net.mamoe.mirai.message.data.MessageChain;

/**
 * @author github.kloping
 */
@Controller
public class PluginController {
    @Before
    public void before(KZeroBot kZeroBot) {
        if (!(kZeroBot.getSelf() instanceof Bot)) {
            throw new NoRunException("mirai-bot专属扩展");
        }
    }

    @Action("我要头衔<.+=>name>")
    public String wanner(@Param("name") String name, MessagePack pack, KZeroBot<MessageChain, Bot> bot) {
        Long gid = Long.valueOf(pack.getSubjectId());
        Long sid = Long.valueOf(pack.getSenderId());
        Group group = bot.getSelf().getGroup(gid);
        MemberPermission p0 = group.getBotAsMember().getPermission();
        if (p0 != MemberPermission.OWNER) return null;
        group.get(sid).setSpecialTitle(name);
        return "OK!";
    }
}
