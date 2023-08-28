package io.github.kloping.mirai0.Main.iutils;

import Project.utils.SSLSocketClientUtil;
import Project.utils.Tools.Tool;
import io.github.kloping.MySpringTool.StarterApplication;
import io.github.kloping.file.FileUtils;
import io.github.kloping.url.UrlUtils;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.message.data.*;
import net.mamoe.mirai.utils.ExternalResource;
import org.jsoup.Jsoup;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

import static io.github.kloping.mirai0.Main.BootstarpResource.BOT;
import static io.github.kloping.mirai0.Main.Parse.PATTER_PIC;
import static io.github.kloping.mirai0.Main.Parse.aStart;


/**
 * @author github-kloping
 */
public class MessageUtils {
    public static final MessageUtils INSTANCE = new MessageUtils();
    public final Map<String, Image> HIST_IMAGES = new HashMap<>();

    public MessageChain getMessageFromString(String str, Contact group) {
        if (str == null || str.isEmpty() || group == null) return null;
        MessageChainBuilder builder = new MessageChainBuilder();
        append(str, builder, group);
        MessageChain message = builder.build();
        return message;
    }

    private List<Object> append(String sb, MessageChainBuilder builder, Contact contact) {
        List<Object> lls = aStart(sb);
        for (Object o : lls) {
            final String str = o.toString();
            boolean k = (str.startsWith("<") || str.startsWith("[")) && !str.matches("\\[.+]请使用最新版手机QQ体验新功能");
            if (k) {
                String ss = str.replace("<", "").replace(">", "").replace("[", "").replace("]", "");
                int i1 = ss.indexOf(":");
                String s1 = ss.substring(0, i1);
                String s2 = ss.substring(i1 + 1);
                Message msg = null;
                switch (s1) {
                    case "Pic":
                        msg = (createImage(contact, s2));
                        break;
                    case "Face":
                        msg = (new Face(Integer.parseInt(s2)));
                        break;
                    case "At":
                        msg = (new At(Long.parseLong(s2)));
                        break;
                    case "Voice":
                    case "Audio":
                        msg = (createVoiceMessageInGroup(s2, contact.getId()));
                        break;
                    default:
                        msg = (new PlainText(str));
                        break;
                }
                if (msg != null)
                    builder.append(msg);
            } else {
                builder.append(str.trim());
            }
        }
        return lls;
    }

    public Image createImage(Contact group, String path) {
        Image image = null;
        try {
            if (HIST_IMAGES.containsKey(path)) {
                image = HIST_IMAGES.get(path);
            } else if (path.startsWith("https")) {
                image = Contact.uploadImage(group, getStreamSsl(path));
            } else if (path.startsWith("http")) {
                image = Contact.uploadImage(group, new URL(path).openStream());
            } else if (path.startsWith("{")) {
                image = Image.fromId(path);
            } else if (path.contains("base64,")) {
                image = Contact.uploadImage(group, new ByteArrayInputStream(Tool.INSTANCE.getBase64Data(path)));
            } else {
                image = Contact.uploadImage(group, new File(path));
            }
        } catch (Throwable e) {
            System.err.println(path + "加载失败");
            e.printStackTrace();
            return null;
        }
        if (image != null) {
            HIST_IMAGES.put(path, image);
            if (HIST_IMAGES.size() >= 100) {
                HIST_IMAGES.clear();
            }
        } else {
            try {
                image = Contact.uploadImage(group, new URL(Tool.INSTANCE.getTouUrl(1)).openStream());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return image;
    }

    private InputStream getStreamSsl(String path) {
        try {
            byte[] bytes = Jsoup.connect(path).sslSocketFactory(
                    SSLSocketClientUtil.getSocketFactory(SSLSocketClientUtil.getX509TrustManager())
            ).ignoreContentType(true).execute().bodyAsBytes();
            return new ByteArrayInputStream(bytes);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getImageUrlFromMessageString(String allMess) {
        try {
            String url = "";
            Matcher matcher = PATTER_PIC.matcher(allMess);
            if (matcher.find()) {
                String p1 = matcher.group();
                int i1 = p1.indexOf("{");
                int i2 = p1.indexOf("]");
                Image image = Image.fromId(p1.substring(i1, i2));
                return Image.queryUrl(image);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void sendMessageInGroup(String str, long id) {
        try {
            Group group = BOT.getGroup(id);
            Message message = MessageUtils.INSTANCE.getMessageFromString(str, group);
            group.sendMessage(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 直接发送字符串
     *
     * @param str
     * @param id
     */
    public void sendMessageInGroupI(String str, long id) {
        try {
            Group group = BOT.getGroup(id);
            group.sendMessage(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendVoiceMessageInGroup(String url, long id) {
        try {
            Group group = BOT.getGroup(id);
            group.sendMessage(createVoiceMessageInGroup(url, id));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Message createVoiceMessageInGroup(String url, long id) {
        return createVoiceMessageInGroup(UrlUtils.getBytesFromHttpUrl(url), id);
    }

    public Message createVoiceMessageInGroup(byte[] bytes, long id) {
        ExternalResource resource = null;
        try {
            Group group = BOT.getGroup(id);
            bytes = mp32amr(bytes);
            resource = ExternalResource.create(bytes);
            Audio audio = group.uploadAudio(resource);
            return audio;
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

    public byte[] mp32amr(byte[] bytes) throws Exception {
        try {
            File source = File.createTempFile("temp0", ".mp3");
            File target = File.createTempFile("temp1", ".amr");
            FileUtils.writeBytesToFile(bytes, source);
            FileUtils.writeBytesToFile(bytes, target);
            try {
                String[] args = {"ffmpeg", "-i", source.getAbsolutePath(), "-ac", "1", "-ar", "8000", "-f", "amr", "-y", target.getAbsolutePath()};
                StarterApplication.logger.info("exec(" + Arrays.toString(args) + ")");
                String[] ss = Tool.INSTANCE.print(Runtime.getRuntime().exec(args));
                StarterApplication.logger.info("exec out:" + ss[0]);
                StarterApplication.logger.error("exec err:" + ss[1]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            bytes = FileUtils.getBytesFromFile(target.getAbsolutePath());
            source.delete();
            target.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytes;
    }

    public void sendMessageInGroupWithAt(String str, long gid, long qq) {
        try {
            if (str == null || gid == -1 || qq == -1) return;
            Group group = BOT.getGroup(gid);
            Message message = MessageUtils.INSTANCE.getMessageFromString(str, group);
            group.sendMessage(new MessageChainBuilder().append(new At(qq)).append("\r\n").append(message).build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendMessageInOneFromGroup(String str, long id) {
        try {
            for (Group group : BOT.getGroups()) {
                if (group.contains(id)) {
                    Contact contact = group.get(id);
                    Message message = MessageUtils.INSTANCE.getMessageFromString(str, contact);
                    contact.sendMessage(message);
                    return;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendMessageByForward(long gid, Object[] objects) {
        Group group = BOT.getGroup(gid);
        ForwardMessageBuilder builder = new ForwardMessageBuilder(group);
        for (Object o : objects) {
            Message message;
            if (o instanceof Message) {
                message = (Message) o;
            } else {
                message = getMessageFromString(o.toString(), group);
            }
            builder.add(BOT.getId(), BOT.getNick(), message);
        }
        group.sendMessage(builder.build());
    }

    /**
     * forward小心
     *
     * @param gid
     * @param strings
     */
    public void sendMessageByForward(long gid, String[] strings) {
        Group group = BOT.getGroup(gid);
        ForwardMessageBuilder builder = new ForwardMessageBuilder(group);
        for (String string : strings) {
            builder.add(BOT.getId(), BOT.getNick(), new PlainText(string));
        }
        group.sendMessage(builder.build());
    }
}
