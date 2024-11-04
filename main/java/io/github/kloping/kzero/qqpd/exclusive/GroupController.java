package io.github.kloping.kzero.qqpd.exclusive;

import io.github.kloping.kzero.bot.database.DataBase;
import io.github.kloping.kzero.main.KlopZeroMainThreads;
import io.github.kloping.kzero.main.api.BotMessageHandler;
import io.github.kloping.kzero.main.api.KZeroBot;
import io.github.kloping.kzero.main.api.MessagePack;
import io.github.kloping.kzero.main.api.MessageType;
import io.github.kloping.kzero.spring.dao.GroupConf;
import io.github.kloping.kzero.spring.mapper.BindMapper;
import io.github.kloping.qqbot.api.SendAble;
import io.github.kloping.qqbot.api.event.InterActionEvent;
import io.github.kloping.qqbot.api.v2.GroupMessageEvent;
import io.github.kloping.qqbot.entities.Bot;
import io.github.kloping.qqbot.entities.ex.msg.MessageChain;
import io.github.kloping.qqbot.impl.ListenerHost;
import io.github.kloping.spt.annotations.AutoStand;
import io.github.kloping.spt.annotations.Constructor;
import io.github.kloping.spt.annotations.Controller;
import io.github.kloping.spt.exceptions.NoRunException;
import io.github.kloping.spt.interfaces.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * @author github.kloping
 */
@Controller
public class GroupController extends ListenerHost {

    private BotMessageHandler handler;
    private KZeroBot zeroBot;


    @Constructor(value = 1)
    public GroupController(KZeroBot kZeroBot) {
        if (!(kZeroBot.getSelf() instanceof Bot)) throw new NoRunException("pd-group-bot专属扩展");
        this.zeroBot = kZeroBot;
        Bot bot = (Bot) kZeroBot.getSelf();
        bot.getConfig().getListenerHosts().add(this);
    }

    @AutoStand
    BindMapper bindMapper;

    private void initHandler() {
        if (handler == null) {
            handler = KlopZeroMainThreads.APPLICATION_MAP.get(zeroBot.getId());
        }
    }

    public Map<String, String> idMapping = new HashMap<>();

    /*
    @Override
    public Object intercept(MessagePack pack, KZeroBot bot) {
       if (!(pack.getRaw() instanceof GroupMessageEvent)) return null;
        MessageEvent event = (MessageEvent) pack.getRaw();
        StringBuilder sb = new StringBuilder();
        for (SendAble sendAble : event.getMessage()) {
            if (sendAble instanceof Image) {
                Image image = (Image) sendAble;
                if (!idMapping.containsKey(pack.getSubjectId())) {
                    int i0 = image.getUrl().substring(36).indexOf("/") + 36;
                    String gid = image.getUrl().substring(35, i0);
                    bindMapper.insert(new BindMap().setBid(bot.getId()).setSid(pack.getSubjectId()).setTid(gid));
                    idMapping.put(pack.getSubjectId(), gid);
                    sb.append("群ID绑定成功!");
                }
                if (!idMapping.containsKey(pack.getSenderId())) {
                    int i1 = image.getUrl().indexOf("?");
                    int i2 = image.getUrl().substring(i1).indexOf("&") + i1;
                    String sid = image.getUrl().substring(i1 + 6, i2);
                    bindMapper.insert(new BindMap().setBid(bot.getId()).setSid(pack.getSenderId()).setTid(sid));
                    idMapping.put(pack.getSenderId(), sid);
                    sb.append("发送者ID绑定成功!");
                }
            }
        }
        return sb.length() == 0 ? null : sb.toString();
    }*/

    @AutoStand
    DataBase dataBase;
    @AutoStand
    Logger logger;

    @EventReceiver
    public void onEvent(InterActionEvent event) {

    }

    @EventReceiver
    public void onEvent(GroupMessageEvent event) {
        MessageChain chain = event.getMessage();
        Guild2Gsuid.INSTANCE.offer(event);
        initHandler();
        String gid = getGid(event);
        GroupConf groupConf = dataBase.getConf(gid);
        if (groupConf != null) {
            if (!groupConf.getOpen()) {
                logger.waring("未开启 group");
                return;
            }
        }
        if (handler != null) {
            String sid = getSid(event);
            KZeroBot<SendAble, Bot> kZeroBot = KlopZeroMainThreads.BOT_MAP.get(String.valueOf(event.getBot().getId()));
            String outMsg = kZeroBot.getSerializer().serialize(chain);
            if (outMsg.startsWith("/") && outMsg.length() > 1) outMsg = outMsg.substring(1);
            MessagePack pack = new MessagePack(MessageType.GROUP, sid, gid, outMsg);
            pack.setRaw(event);
            handler.onMessage(pack);
            Guild2Gsuid.INSTANCE.sendToGsuid(pack, event);
            MihdpConnect2.INSTANCE.sendToMihdp(pack, event, kZeroBot);
        }
    }

    private String getGid(GroupMessageEvent event) {
        String sid = event.getSubject().getId();
        if (idMapping.containsKey(sid)) return idMapping.get(sid);
        String bid = event.getBot().getId();
        String tid = bindMapper.tid(bid, sid);
        if (tid != null) {
            idMapping.put(sid, tid);
            return tid;
        }
        return sid;
    }

    private String getSid(GroupMessageEvent event) {
        String sid = event.getSender().getId();
        if (idMapping.containsKey(sid)) return idMapping.get(sid);
        String bid = event.getBot().getId();
        String tid = bindMapper.tid(bid, sid);
        if (tid != null) {
            idMapping.put(sid, tid);
            return tid;
        }
        return sid;
    }
}
