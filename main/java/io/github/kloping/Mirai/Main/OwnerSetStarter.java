package io.github.kloping.Mirai.Main;

import Project.Tools.Tool;
import io.github.kloping.Mirai.Main.ITools.EventTools;
import io.github.kloping.Mirai.Main.ITools.Saver;
import kotlin.coroutines.CoroutineContext;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.BotFactory;
import net.mamoe.mirai.contact.AnonymousMember;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.FriendMessageEvent;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.utils.BotConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class OwnerSetStarter {
    public static final List<Long> hased = new CopyOnWriteArrayList<>();
    public static final List<Long> hase = new CopyOnWriteArrayList<>();

    static {
        hase.add(291841860L);
        hase.add(2630059874L);
    }

    public static void main(String[] args) {
        BotConfiguration botConfiguration = new BotConfiguration();
        botConfiguration.setProtocol(BotConfiguration.MiraiProtocol.ANDROID_PAD);
        botConfiguration.setHeartbeatStrategy(BotConfiguration.HeartbeatStrategy.NONE);
        botConfiguration.setCacheDir(new File("./cache"));
        botConfiguration.fileBasedDeviceInfo("./devices/device2.json");
        Resource.ABot abot = Resource.get(4);
        Bot bot = BotFactory.INSTANCE.newBot(abot.getQq(), abot.getPassWord(), botConfiguration);
        bot.login();
        bot.getEventChannel().registerListenerHost(new SimpleListenerHost() {
            @Override
            public void handleException(@NotNull CoroutineContext context, @NotNull Throwable exception) {
                super.handleException(context, exception);
            }

            boolean init = false;

            @EventHandler
            public void onMessage(@NotNull GroupMessageEvent event) throws Exception {
                if (!init) {
                    init = true;
                    for (Group group : bot.getGroups()) {
                        for (long q : hase) {
                            if (group.contains(q)) {
                                hased.add(group.getId());
                            }
                        }
                    }
                }
                if (event.getSender() instanceof AnonymousMember) return;
                Group group = event.getGroup();
                long gid = group.getId();
                Resource.threads.execute(() -> {
                    if (hased.contains(gid)) return;
                    try {
                        String json = MessageChain.serializeToJsonString(event.getMessage());
                        Saver.saveMessage(json, gid, event.getSender().getId());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                if (gid != 278681553L) return;
                String text = EventTools.getStringFromGroupMessageEvent(event);
                Long q2 = event.getSender().getId();
                if (text.startsWith("我要头衔")) {
                    text = text.replace("我要头衔", "").replaceAll(" ", "");
                    if (!Tool.isIlleg(text) && !text.isEmpty()) {
                        group.get(q2).setSpecialTitle(text);
                        group.sendMessage("=>O了");
                    } else group.sendMessage("敏感字节!");
                }
            }

            @EventHandler
            public void onFriendMessage(FriendMessageEvent event) {
                Resource.threads.execute(() -> {
                    if (event.getSender().getId() == bot.getId()) {
                        String m1 = (event.getMessage().get(1).toString()).trim();
                        if (m1.startsWith("/get")) {
                            m1 = m1.substring(4);
                            String[] ss = m1.split(":");
                            Long qid = Long.parseLong(ss[0].trim());
                            int[] ints = Tool.StringToInts(ss[1].trim());
                            try {
                                String[] sss = Saver.getTexts2(bot.getId(), qid.longValue(), ints);
                                for (String s1 : sss) {
                                    MessageChain o = MessageChain.deserializeFromJsonString(s1);
                                    event.getSender().sendMessage(o);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        return;
                    }
                    try {
                        String json = MessageChain.serializeToJsonString(event.getMessage());
                        Saver.saveMessage2(json, bot.getId(), event.getSender().getId());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        });
    }
}
