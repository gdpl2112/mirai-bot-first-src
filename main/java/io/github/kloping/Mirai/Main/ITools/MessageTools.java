package io.github.kloping.Mirai.Main.ITools;

import Project.Tools.Tool;
import io.github.kloping.Mirai.Main.Resource;
import io.github.kloping.url.UrlUtils;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.message.data.*;
import net.mamoe.mirai.utils.ExternalResource;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static io.github.kloping.Mirai.Main.ITools.EventTools.getStringFromMessageChain;
import static io.github.kloping.Mirai.Main.Resource.bot;

/**
 * @author github-kloping
 */
public class MessageTools {
    public static long getAtFromString(String message) {
        int start = message.indexOf("[@");
        int end = message.indexOf("]");
        if (start == -1 || end == -1) return -1;
        String str = message.substring(start + 2, end);
        if (str.equals("me"))
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

    private static final Pattern patterFace = Pattern.compile("(<Face:\\d+>|\\[Face:\\d+])");
    private static final Pattern patterPic = Pattern.compile("(<Pic:[^>^]+?>|\\[Pic:[^>^]+?])");
    private static final Pattern patterUrl = Pattern.compile("<Url:[^>^]+>");
    private static final Pattern patterAt = Pattern.compile("\\[At:.+?]|<At:.+?>");

    private static List<Object> append(String sb, MessageChainBuilder builder, Contact group) {
        List<Object> lls = aStart(sb);
        for (Object o : lls) {
            String str = o.toString();
            if ((str.startsWith("<") || str.startsWith("[")) && !str.matches("\\[.+]请使用最新版手机QQ体验新功能")) {
                String ss = str.replace("<", "").replace(">", "").replace("[", "").replace("]", "");
                int i1 = ss.indexOf(":");
                String s1 = ss.substring(0, i1);
                String s2 = ss.substring(i1 + 1);
                switch (s1) {
                    case "Pic":
                        builder.append(createImage(group, s2));
                        break;
                    case "Face":
                        builder.append(new Face(Integer.parseInt(s2)));
                        break;
                    case "At":
                        builder.append(new At(Long.parseLong(s2)));
                }
            } else
                builder.append(str.trim());
        }
        return lls;
    }

    public static List<Object> aStart(String line) {
        List<String> list = new ArrayList<>();
        List<Object> olist = new ArrayList<>();
        a1b2c3(list, line);
        for (String s : list) {
            int i = line.indexOf(s);
            if (i > 0) {
                olist.add(line.substring(0, i));
            }
            olist.add(s);
            line = line.substring(i + s.length());
        }
        if (!line.isEmpty())
            olist.add(line);
        return olist;
    }

    public static void a1b2c3(List<String> list, String line) {
        if (list == null || line == null || line.isEmpty()) return;
        Map<Integer, String> nm = getNearestOne(line, patterPic, patterAt, patterFace, patterUrl);
        if (nm.isEmpty()) {
            list.add(line);
            return;
        }
        int n = nm.keySet().iterator().next();
        String v = nm.get(n);
        String[] ss = new String[2];
        ss[0] = line.substring(0, line.indexOf(v));
        ss[1] = line.substring(line.indexOf(v) + v.length(), line.length());
        if (!ss[0].isEmpty()) {
            list.add(ss[0]);
            line = line.replaceFirst(ss[0], "");
        }
        line = ss[1];
        list.add(v);
        a1b2c3(list, line);
        return;
    }

    public static Map<Integer, String> getNearestOne(final String line, Pattern... patterns) {
        try {
            Map<Integer, String> map = new LinkedHashMap<>();
            for (Pattern pattern : patterns) {
                Matcher matcher = pattern.matcher(line);
                if (matcher.find()) {
                    String l1 = matcher.group();
                    int i1 = line.indexOf(l1);
                    map.put(i1, l1);
                }
            }
            Map<Integer, String> result1 = new LinkedHashMap<>();
            map.entrySet().stream().sorted(Map.Entry.comparingByKey()).forEachOrdered(x -> result1.put(x.getKey(), x.getValue()));
            return result1;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
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

    private static final Map<String, Image> HIST_IMAGES = new ConcurrentHashMap<>();

    public static synchronized Image createImage(Contact group, String path) {
        Image image = null;
        try {
            if (HIST_IMAGES.containsKey(path)) {
                image = HIST_IMAGES.get(path);
            } else if (path.startsWith("http")) {
                image = Contact.uploadImage(group, new URL(path).openStream());
            } else if (path.startsWith("{")) {
                image = Image.fromId(path);
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
            Matcher matcher = patterPic.matcher(allmess);
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

    public static synchronized void sendMessageInGroup(String str, long id) {
        try {
            Group group = bot.getGroup(id);
            Message message = MessageTools.getMessageFromString(str, group);
            group.sendMessage(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static synchronized void sendStringInGroup(String str, long id) {
        try {
            Group group = bot.getGroup(id);
            group.sendMessage(new PlainText(str));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static synchronized void sendMessageInGroup(Object o, long id) {
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

    public static synchronized void sendVoiceMessageInGroup(String url, long id) {
        try {
            Group group = bot.getGroup(id);
            byte[] bytes = UrlUtils.getBytesFromHttpUrl(url);
//            bytes = mp32amr(bytes);
            ExternalResource resource = ExternalResource.create(bytes);
            group.sendMessage(new MessageChainBuilder().append(group.uploadAudio(resource)).build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
/*

    private static final EncodingAttributes attrs = new EncodingAttributes();

    static {
        //[ac3, adpcm_adx, adpcm_ima_wav, adpcm_ms, adpcm_swf, adpcm_yamaha, flac, g726,
        // libamr_nb, libamr_wb, libfaac, libgsm, libgsm_ms, libmp3lame, libvorbis, mp2,
        // pcm_alaw, pcm_mulaw, pcm_s16be, pcm_s16le, pcm_s24be, pcm_s24daud, pcm_s24le,
        // pcm_s32be, pcm_s32le, pcm_s8, pcm_u16be, pcm_u16le, pcm_u24be, pcm_u24le, pcm_u32be,
        // pcm_u32le, pcm_u8, pcm_zork, roq_dpcm, sonic, sonicls, vorbis, wmav1, wmav2]
        AudioAttributes audio = new AudioAttributes();
        audio.setCodec("libamr_nb");
        audio.setBitRate(12200);
        audio.setChannels(1);
        audio.setSamplingRate(8000);
        attrs.setFormat("amr");
        attrs.setAudioAttributes(audio);
    }

    public static byte[] mp32amr(byte[] bytes) throws Exception {
        File source = File.createTempFile("temp0", ".mp3");
        File target = File.createTempFile("temp1", ".amr");
        FileUtils.writeBytesToFile(bytes, source);
        FileUtils.writeBytesToFile(bytes, target);
        Encoder encoder = new Encoder();
        try {
            encoder.encode(source, target, attrs);
        } catch (Exception e) {
            e.printStackTrace();
        }
        bytes = FileUtils.getBytesFromFile(target.getAbsolutePath());
//        source.delete();
//        target.delete();
        return bytes;
    }
*/

    public static synchronized void sendMessageInGroupWithAt(String str, long gid, long qq) {
        try {
            if (str == null || gid == -1 || qq == -1) return;
            Group group = bot.getGroup(gid);
            Message message = MessageTools.getMessageFromString(str, group);
            group.sendMessage(new MessageChainBuilder().append(new At(qq)).append("\r\n").append(message).build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static synchronized void sendMessageInOneFromGroup(String str, long id, long gid) {
        try {
            Contact contact = bot.getGroup(gid).get(id);
            Message message = MessageTools.getMessageFromString(str, contact);
            contact.sendMessage(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static synchronized void sendMessageInOneFromGroup(String str, long id) {
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

    public static synchronized void sendMessageByForward(long gid, Object[] objects) {
        Group group = bot.getGroup(gid);
        ForwardMessageBuilder builder = new ForwardMessageBuilder(group);
        for (Object o : objects) {
            Message message = getMessageFromString(o.toString(), group);
            builder.add(bot.getId(), bot.getNick(), message);
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
