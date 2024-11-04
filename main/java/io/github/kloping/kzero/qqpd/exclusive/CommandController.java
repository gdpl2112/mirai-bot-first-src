package io.github.kloping.kzero.qqpd.exclusive;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.github.kloping.kzero.bot.database.DataBase;
import io.github.kloping.kzero.main.api.KZeroBot;
import io.github.kloping.kzero.main.api.MessagePack;
import io.github.kloping.kzero.spring.dao.BindMap;
import io.github.kloping.kzero.spring.dao.Father;
import io.github.kloping.qqbot.entities.Bot;
import io.github.kloping.spt.annotations.*;
import io.github.kloping.spt.exceptions.NoRunException;
import io.github.kloping.spt.impls.AutomaticWiringValueImpl;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * @author github.kloping
 */
@Controller
public class CommandController {
    private RestTemplate template;

    @Constructor(value = 1)
    public CommandController(KZeroBot kZeroBot) {
        template = new RestTemplate();
        if (!(kZeroBot.getSelf() instanceof Bot)) throw new NoRunException("pd-group-bot专属扩展");
    }

    @AutoStand
    GroupController groupController;

    @AutoStand
    DataBase dataBase;

    @Action("/.+")
    public Object command(@AllMess String msg, KZeroBot bot, MessagePack pack) throws Exception {
        Father father = dataBase.getFather(pack.getSenderId());
        if (father == null || !father.permissionsList().contains(Father.SUPER_PER)) return "permission denied";
        String[] args = msg.split("\\s+");
        try {
            Object out;
            QueryWrapper<BindMap> qw;
            switch (args[0].substring(1)) {
                case "bind":
                    out = groupController.idMapping.put(args[1], args[2]);
                    groupController.bindMapper.insert(new BindMap().setBid(bot.getId()).setSid(args[1]).setTid(args[2]));
                    return "ok for out:" + out;
                case "update":
                    out = groupController.idMapping.remove(args[1]);
                    BindMap bindMap = new BindMap().setBid(bot.getId()).setSid(args[1]).setTid(args[2]);
                    qw = new QueryWrapper<>();
                    qw.eq("bid", bot.getId());
                    qw.eq("sid", args[1]);
                    groupController.bindMapper.update(bindMap, qw);
                    return "update for out:" + out;
                case "del":
                    out = groupController.idMapping.remove(args[1]);
                    qw = new QueryWrapper<>();
                    qw.eq("bid", bot.getId());
                    qw.eq("sid", args[1]);
                    groupController.bindMapper.delete(qw);
                    return String.format("deleted:%s-%s", out, args[1]);
                case "clear":
                    int i = groupController.idMapping.size();
                    groupController.idMapping.clear();
                    return "cleared " + i;
                default:
                    return "unknown command";
            }
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @Action("绑定QQ<.+=>qq>")
    public Object userBind(@Param("qq") String qq, MessagePack pack, KZeroBot bot) {
        qq = qq.trim();
        Object out = groupController.idMapping.put(pack.getSenderId(), qq);
        groupController.bindMapper.insert(new BindMap().setBid(bot.getId()).setSid(pack.getSenderId()).setTid(qq));
        return String.format("已经'%s'ID绑定至'%s'旧'%s'\n若绑定错误请到q群278681553反馈", pack.getSenderId(), qq, out);
    }
}
