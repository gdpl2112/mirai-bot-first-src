package io.github.kloping.kzero.awxb;

import io.github.gdpl2112.onebot.v12.data.*;
import io.github.gdpl2112.onebot.v12.event.MetaEvent;
import io.github.gdpl2112.onebot.v12.utils.ConfigurationUtils;
import io.github.kloping.arr.ArrDeSerializer;
import io.github.kloping.file.FileUtils;
import io.github.kloping.kzero.main.api.MessageSerializer;

/**
 * @author github.kloping
 */
public class WxSerializer implements MessageSerializer<MessageChain> {

    private MetaEvent metaEvent;

    public WxSerializer(MetaEvent metaEvent) {
        this.metaEvent = metaEvent;
    }

    @Override
    public String serialize(MessageChain msg) {
        StringBuilder sb = new StringBuilder();
        for (Message message : msg.getMessages()) {
            if (message instanceof PlainText) {
                sb.append(((PlainText) message).getText());
            } else if (message instanceof Image) {
                sb.append("<pic:" + ((Image) message).getId() + ">");
            } else if (message instanceof At) {
                sb.append("<at:" + ((At) message).getTarget() + ">");
            }
        }
        return sb.toString();
    }

    protected final ArrDeSerializer<Message> ARR_DE_SERIALIZER = new ArrDeSerializer<>();

    {
        ARR_DE_SERIALIZER.add(ArrDeSerializer.EMPTY_PATTERN, new ArrDeSerializer.Rule0<Message>() {
            @Override
            public Message deserializer(String s) {
                return new PlainText(s);
            }
        });
        ARR_DE_SERIALIZER.add(PATTER_AT, new ArrDeSerializer.Rule0<Message>() {
            @Override
            public Message deserializer(String s) {
                return new At(s.substring(s.indexOf(":") + 1, s.length() - 1));
            }
        });
        ARR_DE_SERIALIZER.add(PATTER_PIC, new ArrDeSerializer.Rule0<Message>() {
            @Override
            public Message deserializer(String data) {
                String path = data.substring(data.indexOf(":") + 1, data.length() - 1);
                Image image = null;
                try {
                    if (path.startsWith("http")) {
                        image = ConfigurationUtils.INSTANCE.uploadImage(path, metaEvent);
                    } else {
                        image = ConfigurationUtils.INSTANCE.uploadImage(FileUtils.getBytesFromFile(path), metaEvent);
                    }
                } catch (Exception e) {
                    System.err.println(path + "加载失败");
                    e.printStackTrace();
                }
                if (image != null) return image;
                else return null;
            }
        });
    }

    @Override
    public MessageChain deserialize(String msg) {
        MessageChainBuilder builder = new MessageChainBuilder();
        for (Message message : ARR_DE_SERIALIZER.deserializer(msg)) {
            builder.append(message);
        }
        return builder.build();
    }
}
