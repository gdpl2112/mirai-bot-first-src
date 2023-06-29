package io.github.kloping.gb.starter;

import io.github.kloping.gb.*;
import io.github.kloping.qqbot.Starter;
import io.github.kloping.qqbot.api.Intents;
import io.github.kloping.qqbot.api.message.MessageChannelReceiveEvent;
import io.github.kloping.qqbot.entities.ex.At;
import io.github.kloping.qqbot.entities.ex.Image;
import io.github.kloping.qqbot.entities.ex.PlainText;
import io.github.kloping.qqbot.impl.EventReceiver;
import io.github.kloping.qqbot.impl.ListenerHost;

/**
 * @author github.kloping
 */
public class GuildStarter {

    public static BotInterface botInterface;

    public static void main(String[] args) {
        Starter starter = new Starter("102057448", "v0uQvq74AZtFGTCCWcDnEpsOLNoszA2H");
        starter.setReconnect(true);
        starter.getConfig().setCode(Intents.GUILD_MESSAGES.getCode());
        starter.run();
        starter.APPLICATION.logger.setLogLevel(0);
        botInterface = new GuildBotInterface(starter.getBot());
        starter.registerListenerHost(DefaultListenerHost.INSTANCE);
        BootstrapResource.INSTANCE.info("bot from guild started!");
    }

    public static class DefaultListenerHost extends ListenerHost {
        public static final DefaultListenerHost INSTANCE = new DefaultListenerHost();

        @EventReceiver
        public void onEvent(MessageChannelReceiveEvent event) {
            MessageContext context = new MessageContext(event.getSender().getUser().getId(),
                    event.getChannelId(), event.getGuild().getId());
            context.setData(event.getRawMessage());
            event.getMessage().forEach((e) -> {
                if (e instanceof PlainText) {
                    PlainText text = (PlainText) e;
                    context.getMsgs().add(new DataText(text.getText()));
                } else if (e instanceof Image) {
                    Image image = (Image) e;
                    DataImage dataImage = new DataImage(image.getUrl());
                    context.getMsgs().add(dataImage);
                } else if (e instanceof At) {
                    At at = (At) e;
                    context.getMsgs().add(new DataAt(at.getTargetId()));
                }
            });
            BootstrapResource.INSTANCE.starter.handler(botInterface, context);
        }

        @Override
        public void handleException(Throwable e) {

        }
    }
}
