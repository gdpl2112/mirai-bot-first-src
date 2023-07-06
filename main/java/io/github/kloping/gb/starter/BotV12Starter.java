package io.github.kloping.gb.starter;

import io.github.kloping.gb.*;
import io.github.kloping.qqbot.impl.EventReceiver;

/**
 * @author github.kloping
 */
public class BotV12Starter {

    public static BotInterface botInterface;

    public static void main(String[] args) {
//        WebChatClientWithOneBotV12.registerListenerHost(new ListenerHost() {
//            @Override
//            public void handleException(Throwable e) {
//
//            }
//
//            @EventReceiver
//            public void onEvent(GroupMessageEvent event) {
//                MessageContext context = new MessageContext(event.getSender().getUserId(),
//                        event.getGroupId(), event.getSelf().getUserId());
//                context.setData(event);
//                for (Message e : event.getMessage().getMessages()) {
//                    if (e instanceof PlainText) {
//                        PlainText text = (PlainText) e;
//                        context.getMsgs().add(new DataText(text.getText()));
//                    } else if (e instanceof Image) {
//                        Image image = (Image) e;
//                        DataImage dataImage = new DataImage(image.getId());
//                        context.getMsgs().add(dataImage);
//                    } else if (e instanceof At) {
//                        At at = (At) e;
//                        context.getMsgs().add(new DataAt(at.getTarget()));
//                    }
//                    BootstrapResource.INSTANCE.starter.handler(botInterface, context);
//                }
//            }
//
//            @EventReceiver
//            public void onEvent(FriendMessageEvent event) {
//                if (event.getMessage().toString().trim().equals("测试")) {
//                    Friend friend = event.getFriend();
//                    event.sendMessage("测试成功");
//                }
//            }
//        });
//        WebChatClientWithOneBotV12.main(args);
//        BootstrapResource.INSTANCE.info("v12 bot started!");
    }
}
