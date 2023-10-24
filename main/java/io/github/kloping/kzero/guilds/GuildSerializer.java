package io.github.kloping.kzero.guilds;

import io.github.kloping.arr.ArrDeSerializer;
import io.github.kloping.file.FileUtils;
import io.github.kloping.kzero.main.api.MessageSerializer;
import io.github.kloping.number.NumberUtils;
import io.github.kloping.qqbot.api.SendAble;
import io.github.kloping.qqbot.entities.Bot;
import io.github.kloping.qqbot.entities.ex.At;
import io.github.kloping.qqbot.entities.ex.Image;
import io.github.kloping.qqbot.entities.ex.MessageAsyncBuilder;
import io.github.kloping.qqbot.entities.ex.PlainText;
import io.github.kloping.qqbot.entities.ex.msg.MessageChain;
import io.github.kloping.qqbot.entities.qqpd.data.Emoji;

/**
 * @author github.kloping
 */
public class GuildSerializer implements MessageSerializer<SendAble> {

    private Bot bot;

    public GuildSerializer(Bot bot) {
        this.bot = bot;
    }

    @Override
    public String serialize(SendAble msg0) {
        if (msg0 instanceof MessageChain) {
            MessageChain msg = (MessageChain) msg0;
            StringBuilder sb = new StringBuilder();
            msg.forEach((e) -> {
                if (e instanceof Image) {
                    Image image = (Image) e;
                    sb.append("<pic:" + image.getUrl() + ">");
                } else if (e instanceof PlainText) {
                    sb.append(e.toString());
                } else if (e instanceof At) {
                    At at = ((At) e);
                    String aid = at.getTargetId();
                    if (aid.equals(bot.getInfo().getId())) return;
                    sb.append("<at:" + at.getTargetId() + ">");
                }
            });
            return sb.toString().trim();
        } else return msg0.toString().trim();
    }

    protected final ArrDeSerializer<SendAble> ARR_DE_SERIALIZER = new ArrDeSerializer<>();

    {
        ARR_DE_SERIALIZER.add(PATTER_FACE, new ArrDeSerializer.Rule0<SendAble>() {
            @Override
            public SendAble deserializer(String s) {
                return Emoji.valueOf(NumberUtils.getIntegerFromString(s, 0));
            }
        });
        ARR_DE_SERIALIZER.add(PATTER_AT, new ArrDeSerializer.Rule0<SendAble>() {
            @Override
            public SendAble deserializer(String s) {
                return new At(At.MEMBER_TYPE, s.substring(s.indexOf(":") + 1, s.length() - 1));
            }
        });
        ARR_DE_SERIALIZER.add(PATTER_MUSIC, new ArrDeSerializer.Rule0<SendAble>() {
            @Override
            public SendAble deserializer(String s) {
                return new PlainText(String.format("[音乐:%s]", s));
            }
        });
        ARR_DE_SERIALIZER.add(ArrDeSerializer.EMPTY_PATTERN, new ArrDeSerializer.Rule0<SendAble>() {
            @Override
            public SendAble deserializer(String s) {
                return new PlainText(s);
            }
        });
        ARR_DE_SERIALIZER.add(PATTER_PIC, new ArrDeSerializer.Rule0<SendAble>() {
            @Override
            public SendAble deserializer(String data) {
                String path = data.substring(data.indexOf(":") + 1, data.length() - 1);
                Image image = null;
                try {
                    if (path.startsWith("http")) {
                        image = new Image(path);
                    } else {
                        image = new Image(FileUtils.getBytesFromFile(path));
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
    public SendAble deserialize(String msg) {
        MessageAsyncBuilder builder = new MessageAsyncBuilder();
        for (SendAble sendAble : ARR_DE_SERIALIZER.deserializer(msg)) {
            if (sendAble != null) builder.append(sendAble);
        }
        return builder.build();
    }
}
