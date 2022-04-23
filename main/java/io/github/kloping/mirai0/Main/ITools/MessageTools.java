package io.github.kloping.mirai0.Main.ITools;

import io.github.kloping.MySpringTool.StarterApplication;
import io.github.kloping.file.FileUtils;
import io.github.kloping.mirai0.Main.Resource;
import io.github.kloping.mirai0.unitls.Tools.Tool;
import io.github.kloping.url.UrlUtils;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.message.data.*;
import net.mamoe.mirai.utils.ExternalResource;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static Project.controllers.auto.ControllerSource.aiBaiduDetail;
import static io.github.kloping.mirai0.Main.ITools.EventTools.getStringFromMessageChain;
import static io.github.kloping.mirai0.Main.Parse.aStart;
import static io.github.kloping.mirai0.Main.Resource.bot;
import static io.github.kloping.mirai0.unitls.Tools.Tool.getBase64Data;
import static io.github.kloping.mirai0.unitls.Tools.Tool.print;

/**
 * @author github-kloping
 */
public class MessageTools {
    public static final String BASE_VOICE_URL = "https://tts.youdao.com/fanyivoice?word=%s&le=zh&keyfrom=speaker-target";
    private static final Pattern PATTER_FACE = Pattern.compile("(<Face:\\d+>|\\[Face:\\d+])");
    private static final Pattern PATTER_PIC = Pattern.compile("(<Pic:[^>^]+?>|\\[Pic:[^>^]+?])");
    private static final Pattern PATTER_URL = Pattern.compile("<Url:[^>^]+>");
    private static final Pattern PATTER_AT = Pattern.compile("\\[At:.+?]|<At:.+?>");
    private static final Pattern PATTER_VOICE = Pattern.compile("\\[Voice:.+?]|<Audio:.+?>");
    private static final Map<Integer, Face> faces = new ConcurrentHashMap<>();
    private static final Map<Long, At> ats = new ConcurrentHashMap<>();
    private static final Map<String, Image> HIST_IMAGES = new ConcurrentHashMap<>();

    public static long getAtFromString(String message) {
        int start = message.indexOf("[@");
        int end = message.indexOf("]");
        if (start == -1 || end == -1) return -1;
        String str = message.substring(start + 2, end);
        if ("me".equals(str))
            return Resource.qq.getQq();
        long l = Long.parseLong(str);
        return l;
    }

    public static MessageChain getMessageFromString(String str, Contact group) {
        if (str == null || str.isEmpty() || group == null) return null;
        MessageChainBuilder builder = new MessageChainBuilder();
        append(str, builder, group);
        MessageChain message = builder.build();
        return message;
    }

    private static List<Object> append(String sb, MessageChainBuilder builder, Contact contact) {
        List<Object> lls = aStart(sb);
        for (Object o : lls) {
            String str = o.toString();
            boolean k = (str.startsWith("<") || str.startsWith("[")) && !str.matches("\\[.+]请使用最新版手机QQ体验新功能");
            if (k) {
                String ss = str.replace("<", "").replace(">", "")
                        .replace("[", "").replace("]", "");
                int i1 = ss.indexOf(":");
                String s1 = ss.substring(0, i1);
                String s2 = ss.substring(i1 + 1);
                switch (s1) {
                    case "Pic":
                        builder.append(createImage(contact, s2));
                        break;
                    case "Face":
                        builder.append(getFace(Integer.parseInt(s2)));
                        break;
                    case "At":
                        builder.append(getAt(Long.parseLong(s2)));
                        break;
                    case "Voice":
                    case "Audio":
                        builder.append(createVoiceMessageInGroup(s2, contact.getId()));
                        break;
                    default:
                        break;
                }
            } else {
                builder.append(str.trim());
            }
        }
        return lls;
    }

    private static Face getFace(int parseInt) {
        if (faces.containsKey(parseInt)) {
            return faces.get(parseInt);
        } else {
            Face face = new Face(parseInt);
            faces.put(parseInt, face);
            return face;
        }
    }

    public static At getAt(long id) {
        if (ats.containsKey(id)) {
            return ats.get(id);
        } else {
            At at = new At(id);
            ats.put(id, at);
            return at;
        }
    }

    public static void speak(String line, io.github.kloping.mirai0.commons.Group group) {
        try {
            MessageTools.sendVoiceMessageInGroup(aiBaiduDetail.getBytes(line), group.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static long findNumberFromMessage(MessageChain message) {
        try {
            String str = getStringFromMessageChain(message);
            String at = getAtFromString(str) + "";
            if (str.contains(at + ""))
                str = str.replace(at, "");
            if (str.isEmpty()) return -1;
            long l = Long.parseLong(Tool.findNumberFromString(str));
            return l;
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public static Image createImage(Contact group, String path) {
        Image image = null;
        try {
            if (HIST_IMAGES.containsKey(path)) {
                image = HIST_IMAGES.get(path);
            } else if (path.startsWith("http")) {
                image = Contact.uploadImage(group, new URL(path).openStream());
            } else if (path.startsWith("{")) {
                image = Image.fromId(path);
            } else if (path.contains("base64,")) {
                image = Contact.uploadImage(group, new ByteArrayInputStream(getBase64Data(path)));
            } else {
                image = Contact.uploadImage(group, new File(path));
            }
        } catch (Exception e) {
            System.err.println(path + "加载失败");
            e.printStackTrace();
        }
        if (image != null) {
            HIST_IMAGES.put(path, image);
            if (HIST_IMAGES.size() >= 100) {
                HIST_IMAGES.clear();
            }
        }
        return image;
    }

    public static Image createImage(String path) {
        return createImage(bot.getAsFriend(), path);
    }

    public static String getFlashUrlFromMessageString(String mess) {
        int i1 = mess.indexOf(":");
        int i2 = mess.lastIndexOf(":");
        if (i1 > 0 && i2 > 0) {
            return mess.substring(i1 + 1, i2);
        }
        return null;
    }

    public static String getImageUrlFromMessageString(String allmess) {
        try {
            String url = "";
            Matcher matcher = PATTER_PIC.matcher(allmess);
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

    public static String getImageIDFromMessageString(String allmess) {
        try {
            Matcher matcher = PATTER_PIC.matcher(allmess);
            if (matcher.find()) {
                String p1 = matcher.group();
                int i1 = p1.indexOf("{");
                int i2 = p1.indexOf("]");
                return p1.substring(i1, i2);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void sendMessageInGroup(String str, long id) {
        try {
            Group group = bot.getGroup(id);
            Message message = MessageTools.getMessageFromString(str, group);
            group.sendMessage(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendStringInGroup(String str, long id) {
        try {
            Group group = bot.getGroup(id);
            group.sendMessage(new PlainText(str));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendMessageInGroup(Object o, long id) {
        try {
            Group group = bot.getGroup(id);
            if (o instanceof Message) {
                group.sendMessage((Message) o);
            } else {
                Message message = MessageTools.getMessageFromString(o.toString(), group);
                group.sendMessage(message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendVoiceMessageInGroup(String url, long id) {
        try {
            Group group = bot.getGroup(id);
            group.sendMessage(createVoiceMessageInGroup(url, id));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendVoiceMessageInGroup(byte[] bytes, long id) {
        try {
            Group group = bot.getGroup(id);
            group.sendMessage(createVoiceMessageInGroup(bytes, id));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Message createVoiceMessageInGroup(String url, long id) {
        ExternalResource resource = null;
        try {
            Group group = bot.getGroup(id);
            byte[] bytes = UrlUtils.getBytesFromHttpUrl(url);
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

    public static Message createVoiceMessageInGroup(byte[] bytes, long id) {
        ExternalResource resource = null;
        try {
            Group group = bot.getGroup(id);
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

    public static byte[] mp32amr(byte[] bytes) throws Exception {
        File source = File.createTempFile("temp0", ".mp3");
        File target = File.createTempFile("temp1", ".amr");
        FileUtils.writeBytesToFile(bytes, source);
        FileUtils.writeBytesToFile(bytes, target);
        try {
            String[] args = {
                    "ffmpeg", "-i", source.getAbsolutePath(),
                    "-ac", "1",
                    "-ar", "8000",
                    "-f", "amr",
                    "-y", target.getAbsolutePath()
            };
            StarterApplication.logger.info("exec(" + Arrays.toString(args) + ")");
            String[] ss = print(Runtime.getRuntime().exec(args));
            StarterApplication.logger.info("exec out:" + ss[0]);
            StarterApplication.logger.error("exec err:" + ss[1]);
        } catch (Exception e) {
            e.printStackTrace();
        }
        bytes = FileUtils.getBytesFromFile(target.getAbsolutePath());
        source.delete();
        target.delete();
        return bytes;
    }

    public static void sendImageByBytesOnGroupWithAt(byte[] bytes, long gid, long qid) {
        Group group = bot.getGroup(gid);
        ExternalResource resource = ExternalResource.create(bytes);
        Image image = group.uploadImage(resource);
        MessageChainBuilder mcb = new MessageChainBuilder().append(getAt(qid)).append("\n").append(image);
        group.sendMessage(mcb.build());
    }

    public static Image toImage(byte[] bytes, long gid) {
        if (bytes == null) {
            return null;
        }
        Group group = bot.getGroup(gid);
        ExternalResource resource = ExternalResource.create(bytes);
        Image image = group.uploadImage(resource);
        return image;
    }

    public static void sendMessageInGroupWithAt(String str, long gid, long qq) {
        try {
            if (str == null || gid == -1 || qq == -1) return;
            Group group = bot.getGroup(gid);
            Message message = MessageTools.getMessageFromString(str, group);
            group.sendMessage(new MessageChainBuilder().append(new At(qq)).append("\r\n").append(message).build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendMessageInOneFromGroup(String str, long id, long gid) {
        try {
            Contact contact = bot.getGroup(gid).get(id);
            Message message = MessageTools.getMessageFromString(str, contact);
            contact.sendMessage(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendMessageInOneFromGroup(String str, long id) {
        try {
            for (Group group : bot.getGroups()) {
                if (group.contains(id)) {
                    Contact contact = group.get(id);
                    Message message = MessageTools.getMessageFromString(str, contact);
                    contact.sendMessage(message);
                    return;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendMessageByForward(long gid, Object[] objects) {
        Group group = bot.getGroup(gid);
        ForwardMessageBuilder builder = new ForwardMessageBuilder(group);
        for (Object o : objects) {
            Message message;
            if (o instanceof Message) {
                message = (Message) o;
            } else {
                message = getMessageFromString(o.toString(), group);
            }
            builder.add(bot.getId(), bot.getNick(), message);
        }
        group.sendMessage(builder.build());
    }

    public static void sendMessageByForward(long gid, String[] strings) {
        Group group = bot.getGroup(gid);
        ForwardMessageBuilder builder = new ForwardMessageBuilder(group);
        for (String string : strings) {
            builder.add(bot.getId(), bot.getNick(), new PlainText(string));
        }
        group.sendMessage(builder.build());
    }

    public static boolean containsOneInGroup(Long qq, long id) {
        try {
            return bot.getGroup(id).contains(qq);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isJoinGroup(long qq) {
        for (net.mamoe.mirai.contact.Group group : bot.getGroups())
            if (qq == group.getId()) return true;
        return false;
    }
}
