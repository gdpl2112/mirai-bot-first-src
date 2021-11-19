package Project.Controllers;

import Project.Plugins.pluginsDetail.entitys.CodeContent;
import Project.Plugins.pluginsDetail.entitys.CodeEntity;
import Project.Plugins.pluginsDetail.entitys.CodeResponse;
import io.github.kloping.MySpringTool.annotations.Action;
import io.github.kloping.MySpringTool.annotations.Controller;
import io.netty.util.concurrent.DefaultThreadFactory;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.MessageChainBuilder;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static Project.Plugins.pluginsDetail.RunAll.*;

@Controller
public class SessionController {

    public static boolean contains(long id) {
        return InTheSession.contains(id);
    }

    @Action("开始会话")
    public String m1(long q) {
        return InTheSession.add(q) ? "开始会话" : "您已经在会话";
    }

//    @Action("结束会话")
//    public String m2(long q) {
//        q2Filename.remove(q);
//        q2CodeContent.remove(q);
//        q2CodeRunInput.remove(q);
//        return InTheSession.remove(q) ? "结束会话" : "您没有在会话";
//    }

    public static void gotoSession(Group group, String text, long q) {
        String result = i(text, q, group);
        if (result != null)
            if (result.startsWith("&"))
                group.sendMessage(new MessageChainBuilder().append(new At(q)).append(result.substring(1)).build());
            else
                group.sendMessage(result);

    }

    public static final List<Long> InTheSession = new CopyOnWriteArrayList<>();

    public static final Map<Long, String> q2Filename = new ConcurrentHashMap<>();

    public static final Map<Long, String> q2CodeContent = new ConcurrentHashMap<>();

    public static final Map<Long, String> q2CodeRunInput = new ConcurrentHashMap<>();
    private static final String helpStr = "已知命令:\n" +
            "创建文件(文件名)\n" +
            "\t#仅可创建c,py,java 文件\n" +
            "\t例如:创建文件Main.java\n" +
            "文件内容(内容)\n" +
            "文件内容追加(内容)\n" +
            "执行时输入(内容)\n" +
            "开始执行\n" +
            "==============\n" +
            "目前可运行 java c py c++ kotlin lua go bash javascript更多语言开发中...";

    public static String i(String m1, long q, Group group) {
        if (m1.startsWith("帮助") || m1.startsWith("help")) {
            return helpStr;
        } else if (m1.startsWith("创建文件")) {
            return createFile(m1.substring(4).trim(), q);
        } else if (m1.startsWith("结束会话")) {
            q2Filename.remove(q);
            q2CodeContent.remove(q);
            q2CodeRunInput.remove(q);
            return InTheSession.remove(q) ? "&\n结束会话" : "&\n您没有在会话";
        } else if (m1.startsWith("文件内容追加")) {
            return "当前文件内容:\n" + WriteFile(m1.substring(6).trim(), q, true);
        } else if (m1.startsWith("文件内容")) {
            return "当前文件内容:\n" + WriteFile(m1.substring(4).trim(), q, false);
        } else if (m1.startsWith("执行时输入")) {
            q2CodeRunInput.put(q, m1.substring(5));
            return "执行时输入:\n" + q2CodeRunInput.get(q);
        } else if (m1.startsWith("开始执行")) {
            return run(q, group);
        } else return null;
        //return "未知命令:" + m1 + "\nUnknown Command:" + m1;
    }

    public static String run(long q, Group group) {
        CodeEntity entity = new CodeEntity();
        CodeContent content = new CodeContent();
        if (!q2Filename.containsKey(q)) return "未设置文件名";
        String fileName = q2Filename.get(q);
        content.setName(fileName);
        if (!q2CodeContent.containsKey(q)) return "文件文本为空";
        content.setContent(q2CodeContent.get(q));
        // content 准备完成
        entity.setFiles(new CodeContent[]{content});
        entity.setStdin("");
        if (q2CodeRunInput.containsKey(q)) entity.setStdin(q2CodeRunInput.get(q));
        entity.setCommand("");
        try {
            return "运行中...";
        } finally {
            runCodeDaeThreads.execute(() -> {
                try {
                    CodeResponse response = new CodeResponse();
                    if (fileName.endsWith("java"))
                        response = runJava(entity);
                    else if (fileName.endsWith("c"))
                        response = runC(entity);
                    else if (fileName.endsWith("py"))
                        response = runPython(entity);
                    else response = runAny(entity, supportLanguage.get(fileName.substring(fileName.indexOf(".") + 1)));
                    String result = String.format("运行结果:\n%s\n\n运行警告:\n%s\n\n运行错误:\n%s\n\n", response.getStdout(), response.getStderr(), response.getError());
                    group.sendMessage(result);
                } catch (Exception e) {
                    e.printStackTrace();
                    group.sendMessage("运行失败,可能因为输出过多");
                }
            });
        }
    }

    public static final Map<String, String> supportLanguage = new ConcurrentHashMap<>();

    static {
        supportLanguage.put("java", "java");
        supportLanguage.put("c", "c");
        supportLanguage.put("py", "python");
        supportLanguage.put("cpp", "cpp");
        supportLanguage.put("kt", "kotlin");
        supportLanguage.put("go", "go");
        supportLanguage.put("sh", "bash");
        supportLanguage.put("lua", "lua");
        supportLanguage.put("js", "javascript");
    }

    private static final ExecutorService runCodeDaeThreads = Executors.newFixedThreadPool(10, new DefaultThreadFactory("runCodeDaemons"));

    public static String createFile(String fileName, long q) {
        if (!(fileName.indexOf(".") > 0 && supportLanguage.containsKey(fileName.substring(fileName.indexOf(".") + 1))))
            return "未知文件类型:" + fileName + "\nUnknown File Type:" + fileName;
        String f1 = q2Filename.put(q, fileName);
        return f1 == null ? String.format("创建文件:%s成功", fileName) : String.format("覆盖文件:%s,并创建文件:%s", f1, fileName);
    }

    public static String WriteFile(String content, long q, boolean k) {
        String old_content = "";
        if (k && q2CodeContent.containsKey(q))
            old_content = q2CodeContent.get(q);
        old_content = old_content + content;
        q2CodeContent.put(q, old_content);
        return q2CodeContent.get(q);
    }
}
