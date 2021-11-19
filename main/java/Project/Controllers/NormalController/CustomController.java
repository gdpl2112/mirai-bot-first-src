package Project.Controllers.NormalController;

import Entitys.Group;
import Project.StringSet;
import Project.Tools.Tool;
import Project.Utils.DBUtils;
import io.github.kloping.Mirai.Main.ITools.MessageTools;
import io.github.kloping.Mirai.Main.Resource;
import io.github.kloping.MySpringTool.annotations.*;
import io.github.kloping.MySpringTool.exceptions.NoRunException;
import net.mamoe.mirai.message.data.MessageChainBuilder;

import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import static Project.Controllers.ControllerTool.CanGroup;
import static Project.DataBases.DataBase.isFather;
import static Project.Services.impl.GameServiceImpl.threads;
import static io.github.kloping.Mirai.Main.Resource.Switch.AllK;
import static io.github.kloping.Mirai.Main.Resource.println;

@Controller
public class CustomController {
    public CustomController() {
        println(this.getClass().getSimpleName() + "构建");
    }

    @Before
    public void before(Group group, long qq, @AllMess String s) throws NoRunException {
        if (!AllK)
            throw new NoRunException();
        if (!CanGroup(group.getId())) {
            throw new NoRunException();
        }
        if (s.equals("回话菜单")) return;
        if (!isFather(qq))
            throw new NoRunException("无权限");
    }

    private static final int finalIndex = 10;
    private static int index = 1;

    public static final synchronized String action(long qq, String s, Group group) {
        try {
            if ((index++ % finalIndex) == 0) tryUpdateMap();
            if (!AllK || !CanGroup(group.getId())) {
                return null;
            }
            if (map.containsKey(s)) {
                Reply reply = map.get(s);
                MessageChainBuilder builder = new MessageChainBuilder();
                if (reply.content.startsWith("at")) {
                    reply.content = reply.content.replaceFirst("at", "");
                    MessageTools.sendMessageInGroupWithAt(reply.content, group.getId(), qq);
                } else {
                    MessageTools.sendMessageInGroup(reply.content, group.getId());
                }
                return reply.content;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static synchronized void tryUpdateMap() {
        int size = getAllAutoReplyCount();
        if (size != map.size()) {
            map.clear();
            map.putAll(getAllAutoReply());
            collections.clear();
            collections.addAll(map.values());
        }
    }

    public static final String line = "1.添加 问_答_..(用*号代表占位符)\r\n" +
            "2.查询词 _...\r\n" +
            "3.查询id _...\r\n" +
            "4.删除词 _...\r\n" +
            "5.删除id _...";

    @Action("回话菜单")
    public String menu() {
        return line;
    }

    public static final Map<String, Reply> map = getAllAutoReply();
    public static final Map<Long, String> qlist = new ConcurrentHashMap<>();
    public static List<Reply> collections = new CopyOnWriteArrayList<>();

    @Action("添加<.+=>str>")
    public String add(@Param("str") String str, long qq) {
        if (qq != Resource.superQL) {
            if (Tool.isIlleg_(str))
                return StringSet.Final.isIllegalTips1;
        }
        int i1 = str.indexOf("问");
        int i2 = str.indexOf("答");
        if (i1 >= 0 && i2 > 0) {
            String k = str.substring(i1 + 1, i2);
            String v = str.substring(i2 + 1, str.length());
            if (k.contains("*") || v.contains("*")) {
                qlist.put(qq, str);
                threads.execute(new Runnable() {
                    private long q1 = qq;

                    @Override
                    public void run() {
                        try {
                            Thread.sleep(60 * 1000);
                            if (map.containsKey(q1)) {
                                map.remove(q1);
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
                return "已添加到处理队列\r\n请在1分钟内完成内容填充\r\n发送emoji\\图片\\表情即可";
            } else {
                if (BuilderAndAdd(str, qq))
                    return "OK";
                else
                    return StringSet.Final.addToAutoReplyError;
            }
        } else {
            return "添加问_答_";
        }
    }

    @Action("[Pic:<.+=>id>")
    public String onPic(@AllMess String mess, @Param("id") String id, long qq) {
        if (qlist.containsKey(qq)) {
            String str = qlist.get(qq);
            str = str.replaceFirst("\\*", mess);
            if (str.contains("*")) {
                qlist.remove(qq);
                qlist.put(qq, str);
                return "已填充1个";
            } else {
                qlist.remove(qq);
                if (BuilderAndAdd(str, qq)) {
                    return "填充完成\r\n添加完成";
                } else return StringSet.Final.addToAutoReplyError;
            }
        } else throw new NoRunException("没有在添加");
    }

    @Action("\\[.+]请使用最新版手机QQ体验新功能")
    public String onEmoji(@AllMess String mess, @Param("id") String id, long qq) {
        if (qlist.containsKey(qq)) {
            String str = qlist.get(qq);
            str = str.replaceFirst("\\*", mess);
            if (str.contains("*")) {
                qlist.remove(qq);
                qlist.put(qq, str);
                return "已填充1个";
            } else {
                qlist.remove(qq);
                if (BuilderAndAdd(str, qq)) {
                    return "填充完成\r\n添加完成";
                } else
                    return StringSet.Final.addToAutoReplyError;
            }
        } else throw new NoRunException("没有在添加");
    }

    @Action("查询词<.+=>name>")
    public String select(@Param("name") String name) {
        if (map.containsKey(name)) {
            Reply reply = map.get(name);
            return String.format("触发词:%s\r\n回复词:%s\r\n添加时间:%s\r\n添加者:%s\r\n", reply.key, reply.content, Tool.getTimeYMdhms(reply.time), reply.who);
        } else {
            return "未查询到该词";
        }
    }

    @Action("查询id<.+=>id>")
    public String selectById(@Param("id") String ids, Group group) {
        if (collections.isEmpty()) {
            collections.clear();
            collections.addAll(map.values());
        }
        int id = 0;
        try {
            id = Integer.parseInt(ids);
        } catch (NumberFormatException e) {
            return "Id应为一个正整数";
        }
        if (collections.size() > id) {
            Reply reply = collections.get(id);
            return String.format("触发词:%s\r\n回复词:%s\r\n添加时间:%s\r\n添加者:%s\r\n", reply.key, reply.content, Tool.getTimeYMdhms(reply.time), reply.who);
        } else {
            return "未查询到该ID";
        }
    }

    @Action("删除词<.+=>name>")
    public String delete(@Param("name") String name) {
        if (map.containsKey(name)) {
            Reply reply = map.get(name);
            if (deleteReply(reply)) {
                map.clear();
                map.putAll(getAllAutoReply());
                collections.clear();
                collections.addAll(map.values());
                return "删除成功";
            } else return "删除失败";
        } else {
            return "未查询到该词";
        }
    }

    @Action("删除id<.+=>id>")
    public String deleteById(@Param("id") String ids, Group group) {
        if (collections.isEmpty()) {
            collections.clear();
            collections.addAll(map.values());
        }
        int id = 0;
        try {
            id = Integer.parseInt(ids);
        } catch (NumberFormatException e) {
            return "Id应为一个正整数";
        }
        if (collections.size() > id) {
            Reply reply = collections.get(id);
            if (deleteReply(reply)) {
                map.clear();
                map.putAll(getAllAutoReply());
                collections.clear();
                collections.addAll(map.values());
                return "删除成功";
            } else return "删除失败";
        } else {
            return "未查询到该ID";
        }
    }

    public static synchronized boolean BuilderAndAdd(String str, long q) {
        int i1 = str.indexOf("问");
        int i2 = str.indexOf("答");
        String k = str.substring(i1 + 1, i2);
        String v = str.substring(i2 + 1, str.length());
        Reply reply = new Reply(q, v, System.currentTimeMillis(), k);
        if (map.containsKey(k)) return false;
        if (!addReply(reply)) return false;
        map.clear();
        map.putAll(getAllAutoReply());
        collections.clear();
        collections.addAll(map.values());
        return true;
    }

    public final static synchronized Map<String, Reply> getAllAutoReply() {
        try {
            Connection connection = DBUtils.getConnection();
            PreparedStatement statement = connection.prepareStatement("select * from auto_reply where delete_stat=0;");
            Map<String, Reply> map = new ConcurrentHashMap<>();
            ResultSet set = statement.executeQuery();
            while (set.next()) {
                int id = set.getInt(1);
                String k = new String(set.getBytes(2), "utf-8");
                String v = new String(set.getBytes(3), "utf-8");
                String w = set.getString(4);
                String t = set.getString(5);
                Reply reply = new Reply(Long.valueOf(w), v, Long.valueOf(t), k);
                reply.id = id;
                map.put(k, reply);
            }
            connection.close();
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    public final static synchronized int getAllAutoReplyCount() {
        Connection connection = null;
        try {
            connection = DBUtils.getConnection();
            PreparedStatement statement = connection.prepareStatement("select count(*) from auto_reply where delete_stat=0;");
            Map<String, Reply> map = new ConcurrentHashMap<>();
            ResultSet set = statement.executeQuery();
            set.next();
            int num = set.getInt(1);
            return num;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public final static synchronized boolean deleteReply(Reply reply) {
        Connection connection = null;
        try {
            connection = DBUtils.getConnection();
            PreparedStatement statement = connection.prepareStatement("update auto_reply set delete_stat=1 where id=?;");
            statement.setInt(1, reply.id);
            return statement.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public final static synchronized boolean addReply(Reply reply) {
        Connection connection = null;
        try {
            connection = DBUtils.getConnection();
            PreparedStatement statement = connection.prepareStatement("insert into auto_reply (k,v,who,time,delete_stat) VALUES (?,?,?,?,0);");
            statement.setBytes(1, reply.key.getBytes(StandardCharsets.UTF_8));
            statement.setBytes(2, reply.content.getBytes(StandardCharsets.UTF_8));
            statement.setString(3, reply.who + "");
            statement.setString(4, reply.time + "");
            return statement.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public final static class Reply {
        private Long who;
        private String content;
        private Long time;
        private String key;
        private Integer id;

        @Override
        public String toString() {
            return "Reply{" +
                    "who=" + who +
                    ", content='" + content + '\'' +
                    ", time=" + time +
                    ", key='" + key + '\'' +
                    '}';
        }

        public Reply(Long who, String content, Long time, String key) {
            this.who = who;
            this.content = content;
            this.time = time;
            this.key = key;
        }
    }
}


