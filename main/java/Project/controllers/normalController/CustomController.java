package Project.controllers.normalController;

import Project.broadcast.normal.MessageBroadcast;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.google.gson.internal.LinkedHashTreeMap;
import io.github.kloping.MySpringTool.annotations.*;
import io.github.kloping.MySpringTool.exceptions.NoRunException;
import io.github.kloping.map.MapUtils;
import io.github.kloping.mirai0.Main.ITools.MessageTools;
import io.github.kloping.mirai0.Main.Resource;
import io.github.kloping.mirai0.commons.Group;
import io.github.kloping.mirai0.commons.eEntitys.AutoReply;
import io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet;
import io.github.kloping.mirai0.unitls.Tools.Tool;
import net.mamoe.mirai.message.data.MessageChainBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static Project.aSpring.SpringBootResource.getAutoReplyMapper;
import static Project.controllers.auto.ControllerTool.canGroup;
import static Project.controllers.auto.ControllerTool.opened;
import static Project.dataBases.DataBase.isFather;
import static Project.services.impl.GameServiceImpl.threads;
import static io.github.kloping.mirai0.Main.Resource.Switch.AllK;
import static io.github.kloping.mirai0.Main.Resource.println;
import static io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet.FinalString.CUSTOM_MENU_STR;
import static io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet.FinalString.NO_PERMISSION_STR;
import static io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet.FinalValue.NOT_OPEN_NO_RUN_EXCEPTION;
import static io.github.kloping.mirai0.unitls.Tools.Tool.getRandT;

/**
 * @author github-kloping
 */
@Controller
public class CustomController {
    public static final String LINE = "1.添加 问_答_..(用*号代表占位符)\r\n" +
            "2.查询词 _...\r\n" +
            "3.查询id _...\r\n" +
            "4.删除词 _...\r\n" +
            "5.删除id _...";
    private static final int FINAL_INDEX = 10;
    private static final long CD = 15 * 1000;
    public static Map<String, List<Reply>> MAP = new HashMap<>();
    public static Map<Long, String> QLIST = new ConcurrentHashMap<>();
    private static int index = 1;
    private static long cd = -1;

    public CustomController() {
        println(this.getClass().getSimpleName() + "构建");
        MessageBroadcast.INSTANCE.add(new MessageBroadcast.MessageReceiver() {
            @Override
            public void onReceive(long qid, long gid, String context) {
                action(qid, context, Group.get(gid));
            }
        });
    }

    public static final String action(long qq, String s, Group group) {
        if (MAP.isEmpty()) {
            MAP = getAllAutoReply();
        }
        try {
            if ((index++ % FINAL_INDEX) == 0) tryUpdateMap();
            if (!AllK || !canGroup(group.getId())) {
                return null;
            }
            if (cd > System.currentTimeMillis()) return null;
            if (MAP.containsKey(s)) {
                Reply reply = getRandT(MAP.get(s));
                MessageChainBuilder builder = new MessageChainBuilder();
                if (reply.content.startsWith("at")) {
                    reply.content = reply.content.replaceFirst("at", "");
                    MessageTools.sendMessageInGroupWithAt(reply.content, group.getId(), qq);
                } else {
                    MessageTools.sendMessageInGroup(reply.content, group.getId());
                }
                cd = System.currentTimeMillis() + CD;
                return reply.content;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void tryUpdateMap() {
        int size = getAllAutoReplyCount();
        if (size != MAP.size()) {
            MAP.clear();
            MAP.putAll(getAllAutoReply());
        }
    }

    public static boolean builderAndAdd(String str, long q) {
        int i1 = str.indexOf("问");
        int i2 = str.indexOf("答");
        String k = str.substring(i1 + 1, i2);
        String v = str.substring(i2 + 1, str.length());
        Reply reply = new Reply(q, v, System.currentTimeMillis(), k);
        if (MAP.containsKey(k)) return false;
        if (!addReply(reply)) return false;
        MapUtils.append(MAP, k, reply);
        return true;
    }

    public final static Map<String, List<Reply>> getAllAutoReply() {
        QueryWrapper<AutoReply> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("delete_stat", 0);
        Map<String, List<Reply>> map = new LinkedHashTreeMap<>();
        for (AutoReply autoReply : getAutoReplyMapper().selectList(queryWrapper)) {
            Reply reply = new Reply(autoReply.getId().longValue(), autoReply.getV(), Long.parseLong(autoReply.getTime()), autoReply.getK());
            reply.id = autoReply.getId();
            MapUtils.append(map, reply.key, reply);
        }
        return map;
    }

    public final static int getAllAutoReplyCount() {
        QueryWrapper<AutoReply> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("delete_stat", 0);
        return getAutoReplyMapper().selectList(queryWrapper).size();
    }

    public final static boolean deleteReply(Reply reply) {
        AutoReply autoReply = getAutoReplyMapper().selectById(reply.id);
        autoReply.setDeleteStat(1);
        UpdateWrapper<AutoReply> wrapper = new UpdateWrapper<>();
        wrapper.eq("id", reply.id);
        return getAutoReplyMapper().update(autoReply, wrapper) > 0;
    }

    public final static boolean addReply(Reply reply) {
        AutoReply autoReply = new AutoReply()
                .setK(reply.key)
                .setV(reply.content)
                .setDeleteStat(0)
                .setWho(String.valueOf(reply.who))
                .setTime(String.valueOf(reply.time));
        return getAutoReplyMapper().insert(autoReply) > 0;
    }

    @Before
    public void before(Group group, long qq, @AllMess String s) throws NoRunException {
        if (!opened(group.getId(), this.getClass())) {
            throw NOT_OPEN_NO_RUN_EXCEPTION;
        }
        if (s.equals(CUSTOM_MENU_STR)) {
            return;
        }
        if (!isFather(qq)) {
            throw new NoRunException(NO_PERMISSION_STR);
        }
    }

    @Action("回话菜单")
    public String menu() {
        return LINE;
    }

    @Action("添加<.+=>str>")
    public String add(@Param("str") String str, long qq) {
        if (qq != Resource.superQL) {
            if (Tool.isIlleg(str))
                return ResourceSet.FinalString.IS_ILLEGAL_TIPS_1;
        }
        int i1 = str.indexOf("问");
        int i2 = str.indexOf("答");
        if (i1 >= 0 && i2 > 0) {
            String k = str.substring(i1 + 1, i2);
            String v = str.substring(i2 + 1, str.length());
            if (k.contains("*") || v.contains("*")) {
                QLIST.put(qq, str);
                threads.execute(new Runnable() {
                    private long q1 = qq;

                    @Override
                    public void run() {
                        try {
                            Thread.sleep(60 * 1000);
                            if (QLIST.containsKey(q1)) {
                                QLIST.remove(q1);
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
                return "已添加到处理队列\r\n请在1分钟内完成内容填充\r\n发送emoji\\图片\\表情即可";
            } else {
                if (builderAndAdd(str, qq)) {
                    return "OK";
                } else {
                    return ResourceSet.FinalString.ADD_TO_AUTO_REPLY_ERROR;
                }
            }
        } else {
            return "添加问_答_";
        }
    }

    @Action("\\[.+]请使用最新版手机QQ体验新功能")
    public String onEmoji(@AllMess String mess, @Param("id") String id, long qq) {
        if (QLIST.containsKey(qq)) {
            String str = QLIST.get(qq);
            str = str.replaceFirst("\\*", mess);
            if (str.contains("*")) {
                QLIST.remove(qq);
                QLIST.put(qq, str);
                return "已填充1个";
            } else {
                QLIST.remove(qq);
                if (builderAndAdd(str, qq)) {
                    return "填充完成\r\n添加完成";
                } else
                    return ResourceSet.FinalString.ADD_TO_AUTO_REPLY_ERROR;
            }
        } else throw new NoRunException("没有在添加");
    }

    @Action("查询词<.+=>name>")
    public String select(@Param("name") String name) {
        if (MAP.containsKey(name)) {
            StringBuilder sb = new StringBuilder();
            for (Reply reply : MAP.get(name)) {
                sb.append(String.format("触发词:%s\r\n回复词:%s\r\n添加时间:%s\r\n添加者:%s\r\n", reply.key, reply.content, Tool.getTimeYMdhms(reply.time), reply.who));
            }
            return sb.toString();
        } else {
            return "未查询到该词";
        }
    }

    @Action("删除词<.+=>name>")
    public String delete(@Param("name") String name) {
        if (MAP.containsKey(name)) {
            Reply reply = MAP.get(name).get(0);
            if (deleteReply(reply)) {
                MAP.get(name).remove(reply);
                return "删除成功";
            } else return "删除失败";
        } else {
            return "未查询到该词";
        }
    }

    public final static class Reply {
        private Long who;
        private String content;
        private Long time;
        private String key;
        private Integer id;

        public Reply(Long who, String content, Long time, String key) {
            this.who = who;
            this.content = content;
            this.time = time;
            this.key = key;
        }

        @Override
        public String toString() {
            return "Reply{" +
                    "who=" + who +
                    ", content='" + content + '\'' +
                    ", time=" + time +
                    ", key='" + key + '\'' +
                    '}';
        }
    }
}


