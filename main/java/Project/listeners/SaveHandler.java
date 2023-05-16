package Project.listeners;

import Project.aSpring.SpringBootResource;
import kotlin.coroutines.CoroutineContext;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.*;
import net.mamoe.mirai.message.action.MemberNudge;
import net.mamoe.mirai.message.data.*;
import org.jetbrains.annotations.NotNull;

import static io.github.kloping.mirai0.Main.BootstarpResource.DEA_THREADS;

/**
 * 独立
 *
 * @author github-kloping
 * @version 1.0
 */
public class SaveHandler extends SimpleListenerHost {

    public SaveHandler(String[] args) {
    }

    @Override
    public void handleException(@NotNull CoroutineContext context, @NotNull Throwable exception) {

    }

    @EventHandler
    public void onMessage(@NotNull GroupMessageEvent event) throws Exception {
        AllMessage.factory(event).save();
    }

    @EventHandler
    public void onMessage(@NotNull MessagePostSendEvent event) throws Exception {
        AllMessage.factory(event).save();
    }

    @EventHandler
    public void onMessage(@NotNull GroupMessageSyncEvent event) throws Exception {
        AllMessage.factory(event).save();
    }

    @EventHandler
    public void onMessage(@NotNull FriendMessageEvent event) throws Exception {
        AllMessage.factory(event).save();
    }

    @EventHandler
    public void onMessage(@NotNull FriendMessageSyncEvent event) throws Exception {
        AllMessage.factory(event).save();
    }

    @EventHandler
    public void onMessage(@NotNull StrangerMessageEvent event) throws Exception {
        AllMessage.factory(event).save();
    }

    @EventHandler
    public void onMessage(@NotNull StrangerMessageSyncEvent event) throws Exception {
        AllMessage.factory(event).save();
    }

    /**
     * @author github-kloping
     */
    @Data
    @Accessors(chain = true)
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AllMessage {
        private Long time;
        private Integer id;
        private Integer internalId;
        private Long senderId;
        private Long botId;
        private String type;
        private Long fromId;
        private String content;
        private Integer recalled = 0;

        public static AllMessage factory(MessagePostSendEvent event) {
            OnlineMessageSource.Outgoing messageSource = event.getReceipt().getSource();
            if (messageSource instanceof OnlineMessageSource.Outgoing.ToGroup) {
                MessageChain mc = event.getMessage();
                return new AllMessage()
                        .setBotId(event.getBot().getId())
                        .setId(latest(0, messageSource.getIds()))
                        .setContent(getText(mc))
                        .setFromId(messageSource.getTargetId())
                        .setSenderId(event.getBot().getId())
                        .setInternalId(latest(0, messageSource.getInternalIds()))
                        .setType("groupSelf").setTime(System.currentTimeMillis());
            } else if (messageSource instanceof OnlineMessageSource.Outgoing.ToFriend) {
                MessageChain mc = event.getMessage();
                return new AllMessage()
                        .setBotId(event.getBot().getId())
                        .setId(latest(0, messageSource.getIds()))
                        .setContent(getText(mc))
                        .setFromId(messageSource.getTargetId())
                        .setSenderId(event.getBot().getId())
                        .setInternalId(latest(0, messageSource.getInternalIds()))
                        .setType("friendSelf").setTime(System.currentTimeMillis());
            }
            return null;
        }

        public static AllMessage factory(MessageEvent event) {
            MessageSource messageSource = (MessageSource) event.getMessage().get(0);
            if (event instanceof GroupMessageEvent) {
                GroupMessageEvent gme = (GroupMessageEvent) event;
                return new AllMessage()
                        .setBotId(event.getBot().getId())
                        .setId(latest(0, messageSource.getIds()))
                        .setContent(getText(gme.getMessage()))
                        .setFromId(gme.getSubject().getId())
                        .setSenderId(gme.getSender().getId())
                        .setInternalId(latest(0, messageSource.getInternalIds()))
                        .setType("group").setTime(System.currentTimeMillis());
            } else if (event instanceof GroupMessageSyncEvent) {
                GroupMessageSyncEvent gme = (GroupMessageSyncEvent) event;
                return new AllMessage()
                        .setBotId(event.getBot().getId())
                        .setId(latest(0, messageSource.getIds()))
                        .setContent(getText(gme.getMessage()))
                        .setFromId(gme.getSubject().getId())
                        .setSenderId(gme.getSender().getId())
                        .setInternalId(latest(0, messageSource.getInternalIds()))
                        .setType("groupSelfSync").setTime(System.currentTimeMillis());
            } else if (event instanceof FriendMessageEvent) {
                FriendMessageEvent gme = (FriendMessageEvent) event;
                return new AllMessage()
                        .setBotId(event.getBot().getId())
                        .setId(latest(0, messageSource.getIds()))
                        .setContent(getText(gme.getMessage()))
                        .setFromId(gme.getSubject().getId())
                        .setSenderId(gme.getSender().getId())
                        .setInternalId(latest(0, messageSource.getInternalIds()))
                        .setType("friend").setTime(System.currentTimeMillis());
            } else if (event instanceof FriendMessageSyncEvent) {
                FriendMessageSyncEvent gme = (FriendMessageSyncEvent) event;
                return new AllMessage()
                        .setBotId(event.getBot().getId())
                        .setId(latest(0, messageSource.getIds()))
                        .setContent(getText(gme.getMessage()))
                        .setFromId(gme.getSubject().getId())
                        .setSenderId(gme.getSender().getId())
                        .setInternalId(latest(0, messageSource.getInternalIds()))
                        .setType("friendSelfSync").setTime(System.currentTimeMillis());
            } else if (event instanceof StrangerMessageEvent) {
                FriendMessageEvent gme = (FriendMessageEvent) event;
                return new AllMessage()
                        .setBotId(event.getBot().getId())
                        .setId(latest(0, messageSource.getIds()))
                        .setContent(getText(gme.getMessage()))
                        .setFromId(gme.getSubject().getId())
                        .setSenderId(gme.getSender().getId())
                        .setInternalId(latest(0, messageSource.getInternalIds()))
                        .setType("stranger").setTime(System.currentTimeMillis());
            } else if (event instanceof StrangerMessageSyncEvent) {
                FriendMessageSyncEvent gme = (FriendMessageSyncEvent) event;
                return new AllMessage()
                        .setBotId(event.getBot().getId())
                        .setId(latest(0, messageSource.getIds()))
                        .setContent(getText(gme.getMessage()))
                        .setFromId(gme.getSubject().getId())
                        .setSenderId(gme.getSender().getId())
                        .setInternalId(latest(0, messageSource.getInternalIds()))
                        .setType("strangerSelf").setTime(System.currentTimeMillis());
            }
            return new AllMessage();
        }

        public static final int latest(int defaultValue, int... ts) {
            if (ts.length == 0 || ts[ts.length - 1] == 0) {
                return defaultValue;
            } else {
                return ts[ts.length - 1];
            }
        }

        public static final <T> T latest(T defaultValue, T... ts) {
            if (ts.length == 0 || ts[ts.length - 1] == null) {
                return defaultValue;
            } else {
                return ts[ts.length - 1];
            }
        }

        private static String getText(MessageChain chain) {
            if (chain.size() == 2) {
                if (chain.get(1) instanceof PlainText) {
                    return ((PlainText) chain.get(1)).getContent();
                } else if (chain.get(1) instanceof Audio) {
                    return MessageChain.serializeToJsonString(chain);
                } else {
                    return MessageChain.serializeToJsonString(chain);
                }
            } else {
                return getStringFromMessageChain(chain);
            }
        }

        public static String getStringFromMessageChain(MessageChain chain) {
            StringBuilder sb = new StringBuilder();
            for (Object o : chain) {
                if (o instanceof MessageSource) {
                    continue;
                }
                if (o instanceof PlainText) {
                    sb.append(((PlainText) o).getContent());
                } else if (o instanceof At) {
                    At at = (At) o;
                    sb.append("[@").append(at.getTarget()).append("]");
                } else if (o instanceof FlashImage) {
                    FlashImage flashImage = (FlashImage) o;
                    sb.append("[闪照:").append(Image.queryUrl(flashImage.getImage())).append(":]");
                } else if (o instanceof Face) {
                    Face face = (Face) o;
                    sb.append("[Face:").append(face.getId()).append("(").append(face.getName()).append(")").append("]");
                } else if (o instanceof Image) {
                    Image image = (Image) o;
                    sb.append("[Pic:").append(image.getImageId()).append("]");
                } else if (o instanceof MemberNudge) {
                    MemberNudge mn = (MemberNudge) o;
                    long qid = mn.getTarget().getId();
                    sb.append("[戳一戳:").append(qid).append("]");
                } else {
                    Message message = (Message) o;
                    sb.append(message.toString());
                }
            }
            return sb.toString();
        }

        public void save() {
            if (content == null || content.isEmpty()) {
                return;
            }
            DEA_THREADS.submit(() -> {
                SpringBootResource.getSaveMapper().insert(AllMessage.this);
            });
        }

        public int getIntTime() {
            Long t0 = time;
            return Integer.parseInt(t0.toString().substring(0, 10));
        }
    }
}
