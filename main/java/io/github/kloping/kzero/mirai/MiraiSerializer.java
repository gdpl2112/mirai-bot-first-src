package io.github.kloping.kzero.mirai;

import io.github.kloping.arr.ArrDeSerializer;
import io.github.kloping.arr.ArrSerializer;
import io.github.kloping.io.ReadUtils;
import io.github.kloping.kzero.main.api.MessageSerializer;
import io.github.kloping.number.NumberUtils;
import io.github.kloping.url.UrlUtils;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.message.data.*;
import net.mamoe.mirai.utils.ExternalResource;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author github.kloping
 */
public class MiraiSerializer implements MessageSerializer<MessageChain> {
    private Bot bot;

    public MiraiSerializer(Bot bot) {
        this.bot = bot;
    }

    public final Map<Integer, MarketFace> MARKET_FACE_MAP = new HashMap<>();
    public final io.github.kloping.arr.ArrSerializer ARR_SERIALIZER = new ArrSerializer();

    {
        ARR_SERIALIZER.add(new ArrSerializer.Rule<Image>(Image.class) {
            @Override
            public String serializer(Image o) {
                return String.format("<pic:%s>", o.getImageId());
            }
        });
        ARR_SERIALIZER.add(new ArrSerializer.Rule<At>(At.class) {
            @Override
            public String serializer(At o) {
                return String.format("<at:%s>", o.getTarget());
            }
        });
        ARR_SERIALIZER.add(new ArrSerializer.Rule<Face>(Face.class) {
            @Override
            public String serializer(Face o) {
                return String.format("<face:%s>", o.getId());
            }
        });
        ARR_SERIALIZER.add(new ArrSerializer.Rule<PlainText>(PlainText.class) {
            @Override
            public String serializer(PlainText o) {
                String touch = o.getContent();
                String regx = "<.*?>";
                Pattern pattern = Pattern.compile(regx);
                Matcher matcher = pattern.matcher(touch);
                while (matcher.find()) {
                    touch = touch.replace(matcher.group(), "\\" + matcher.group());
                }
                return touch;
            }
        });
        ARR_SERIALIZER.add(new ArrSerializer.Rule<Audio>(Audio.class) {
            @Override
            public String serializer(Audio o) {
                return String.format("<audio:%s>", o.getFilename());
            }
        });
        ARR_SERIALIZER.add(new ArrSerializer.Rule<MusicShare>(MusicShare.class) {
            @Override
            public String serializer(MusicShare o) {
                return String.format("<music:%s>", o.getMusicUrl());
            }
        });
        ARR_SERIALIZER.add(new ArrSerializer.Rule<MarketFace>(MarketFace.class) {
            @Override
            public String serializer(MarketFace o) {
                MARKET_FACE_MAP.put(o.getId(), o);
                return String.format("<marketface:%s>", o.getId());
            }
        });
        ARR_SERIALIZER.setMode(1);
    }

    private final Pattern PATTER_FACE = Pattern.compile("<face:\\d+>");
    private final Pattern PATTER_PIC = Pattern.compile("<pic:[^>^]+?>");
    private final Pattern PATTER_AT = Pattern.compile("<at:[\\d+|?]+>");
    private final Pattern PATTER_MUSIC = Pattern.compile("<music:\\d+>");
    private final Pattern PATTER_VOICE = Pattern.compile("<audio:.+>");
    public final ArrDeSerializer<Message> ARR_DE_SERIALIZER = new ArrDeSerializer<>();

    {
        ARR_DE_SERIALIZER.add(PATTER_FACE, new ArrDeSerializer.Rule0<Message>() {
            @Override
            public Message deserializer(String s) {
                return new Face(NumberUtils.getIntegerFromString(s));
            }
        });
        ARR_DE_SERIALIZER.add(PATTER_AT, new ArrDeSerializer.Rule0<Message>() {
            @Override
            public Message deserializer(String s) {
                return new At(NumberUtils.getIntegerFromString(s));
            }
        });
        ARR_DE_SERIALIZER.add(PATTER_MUSIC, new ArrDeSerializer.Rule0<Message>() {
            @Override
            public Message deserializer(String s) {
                String[] ss = s.split(",");
                MusicKind kind = MusicKind.valueOf(ss[0]);
                MusicShare share = new MusicShare(kind, ss[1], ss[2], ss[3], ss[4], ss[5]);
                return share;
            }
        });
        ARR_DE_SERIALIZER.add(PATTER_VOICE, new ArrDeSerializer.Rule0<Message>() {
            @Override
            public Message deserializer(String s) {
                ExternalResource resource = null;
                try {
                    byte[] bytes = UrlUtils.getBytesFromHttpUrl(s);
                    resource = ExternalResource.create(bytes);
                    return bot.getAsFriend().uploadAudio(resource);
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                } finally {
                    if (resource != null) {
                        try {
                            resource.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
        ARR_DE_SERIALIZER.add(ArrDeSerializer.EMPTY_PATTERN, new ArrDeSerializer.Rule0<Message>() {
            @Override
            public Message deserializer(String s) {
                return new PlainText(s);
            }
        });
        ARR_DE_SERIALIZER.add(PATTER_PIC, new ArrDeSerializer.Rule0<Message>() {
            @Override
            public Message deserializer(String path) {
                Message image = null;
                try {
                    if (path.startsWith("http")) {
                        image = Contact.uploadImage(bot.getAsFriend(), new ByteArrayInputStream(ReadUtils.readAll(new URL(path).openStream())));
                    } else if (path.startsWith("{")) {
                        image = Image.fromId(path);
                    } else {
                        image = Contact.uploadImage(bot.getAsFriend(), new File(path));
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
    public String serialize(MessageChain msg) {
        return ARR_SERIALIZER.serializer(msg);
    }

    @Override
    public MessageChain deserialize(String msg) {
        MessageChainBuilder builder = new MessageChainBuilder();
        for (Message message : ARR_DE_SERIALIZER.deserializer(msg)) {
            if (message != null) builder.append(message);
        }
        return builder.build();
    }
}
