package io.github.kloping.Mirai.Main.ITools;

import Project.Tools.Tool;
import io.github.kloping.Mirai.Main.Resource;
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

    public static final Map<String, MessageChain> historyShortMess = new ConcurrentHashMap<>();

    public static MessageChain getMessageFromString(String str, Contact group) {
        if (str == null || str.isEmpty() || group == null) return null;
        if (str.length() < 8 && historyShortMess.containsKey(str))
            return historyShortMess.get(str);
        MessageChainBuilder builder = new MessageChainBuilder();
        append(str, builder, group);
        MessageChain message = builder.build();
        if (str.length() < 8 && !historyShortMess.containsKey(str))
            historyShortMess.put(str, message);
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
                        builder.append(createImageInGroup(group, s2));
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

    public static final Map<Long, Map<String, Image>> ImageResourceMap = new ConcurrentHashMap<>();

    public static synchronized Image createImageInGroup(Contact group, String path) {
        try {
            if (path.startsWith("http")) {
                return Contact.uploadImage(group, new URL(path).openStream());
            } else if (path.startsWith("{")) {
                return Image.fromId(path);
            } else {
                Image image = null;
                if ((image = getImageResource(group.getId(), path)) != null)
                    return image;
                image = Contact.uploadImage(group, new File(path));
                appendToImageResourceMap(group.getId(), path, image);
                return image;
            }
        } catch (IOException e) {
            System.err.println(path + "加载重试");
            try {
                return Contact.uploadImage(group, new URL(path).openStream());
            } catch (IOException ioException) {
                ioException.printStackTrace();
                return null;
            }
        }
    }

    private static void appendToImageResourceMap(Long groupId, String id, Image image) {
        if (id.contains("temp")) return;
        Map<String, Image> map = ImageResourceMap.get(groupId);
        if (map == null) map = new ConcurrentHashMap<>();
        map.put(id, image);
        ImageResourceMap.put(groupId, map);
    }

    private static Image getImageResource(Long groupId, String id) {
        Map<String, Image> map = ImageResourceMap.get(groupId);
        if (map == null) map = new ConcurrentHashMap<>();
        if (map.containsKey(id)) return map.get(id);
        else return null;
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
            byte[] bytes = Tool.getBytesFromHttpUrl(url);
            ExternalResource resource = ExternalResource.create(bytes);
            Voice audio = Voice.fromAudio(group.uploadAudio(resource));
            String s = new MessageChainBuilder().append(audio).build().serializeToMiraiCode();
            System.out.println(s);
            group.sendMessage(audio);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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
