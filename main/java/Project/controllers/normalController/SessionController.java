package Project.controllers.normalController;

import Project.plugins.RunAll;
import io.github.kloping.MySpringTool.annotations.Action;
import io.github.kloping.MySpringTool.annotations.AutoStand;
import io.github.kloping.MySpringTool.annotations.Controller;
import io.github.kloping.mirai0.commons.apiEntitys.runcode.CodeContent;
import io.github.kloping.mirai0.commons.apiEntitys.runcode.CodeEntity;
import io.github.kloping.mirai0.commons.apiEntitys.runcode.CodeResponse;
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


/**
 * @author github-kloping
 */
@Controller
public class SessionController {

    public static final Map<String, String> SUPPORTED_LANGUAGE = new ConcurrentHashMap<>();
    private static final ExecutorService RUN_CODE_DAEMONS = Executors.newFixedThreadPool(10, new DefaultThreadFactory("runCodeDaemons"));
    @AutoStand
    public static SessionController INSTANCE;

    static {
        SUPPORTED_LANGUAGE.put("java", "java");
        SUPPORTED_LANGUAGE.put("c", "c");
        SUPPORTED_LANGUAGE.put("py", "python");
        SUPPORTED_LANGUAGE.put("cpp", "cpp");
        SUPPORTED_LANGUAGE.put("kt", "kotlin");
        SUPPORTED_LANGUAGE.put("go", "go");
        SUPPORTED_LANGUAGE.put("sh", "bash");
        SUPPORTED_LANGUAGE.put("lua", "lua");
        SUPPORTED_LANGUAGE.put("js", "javascript");
    }

    public final List<Long> InTheSession = new CopyOnWriteArrayList<>();
    public final Map<Long, String> q2Filename = new ConcurrentHashMap<>();
    public final Map<Long, String> q2CodeContent = new ConcurrentHashMap<>();
    public final Map<Long, String> q2CodeRunInput = new ConcurrentHashMap<>();
    private final String HELP_STR = "已知命令:\n" +
            "创建文件<FileName>\n" +
            "\t#仅可创建c,py,java 文件\n" +
            "\t例如:创建文件Main.java\n" +
            "文件内容<Text>\n" +
            "文件内容追加<Text>\n" +
            "执行时输入<Text>\n" +
            "开始执行\n" +
            "结束会话\n" +
            "==============\n" +
            "目前可运行 java c py c++ kotlin lua go bash javascript更多语言开发中...";
    @AutoStand
    private RunAll runAll;

    public boolean contains(long id) {
        return InTheSession.contains(id);
    }

    public void gotoSession(Group group, String text, long q) {
        String result = i(text, q, group);
        if (result != null) {
            if (result.startsWith("&")) {
                group.sendMessage(new MessageChainBuilder().append(new At(q)).append(result.substring(1)).build());
            } else {
                group.sendMessage(result);
            }
        }
    }

    public String i(String m1, long q, Group group) {
        if (m1.startsWith("帮助") || m1.startsWith("help")) {
            return HELP_STR;
        } else if (m1.startsWith("创建文件")) {
            return createFile(m1.substring(4).trim(), q);
        } else if (m1.startsWith("结束会话")) {
            q2Filename.remove(q);
            q2CodeContent.remove(q);
            q2CodeRunInput.remove(q);
            return InTheSession.remove(q) ? "&\n结束会话" : "&\n您没有在会话";
        } else if (m1.startsWith("文件内容追加")) {
            return "当前文件内容:\n" + writeFile(m1.substring(6).trim(), q, true);
        } else if (m1.startsWith("文件内容")) {
            return "当前文件内容:\n" + writeFile(m1.substring(4).trim(), q, false);
        } else if (m1.startsWith("执行时输入")) {
            q2CodeRunInput.put(q, m1.substring(5));
            return "执行时输入:\n" + q2CodeRunInput.get(q);
        } else if (m1.startsWith("开始执行")) {
            return run(q, group);
        } else {
            return "未知命令:" + m1 + "\nUnknown Command:" + m1;
        }
    }

    @Action("开始会话")
    public String m1(long q) {
        return InTheSession.add(q) ? "开始会话\n说出\"help\"显示更多" : "您已经在会话";
    }

    public String run(long q, Group group) {
        CodeEntity entity = new CodeEntity();
        CodeContent content = new CodeContent();
        if (!q2Filename.containsKey(q)) return "未设置文件名";
        String fileName = q2Filename.get(q);
        content.setName(fileName);
        if (!q2CodeContent.containsKey(q)) return "文件文本为空";
        content.setContent(q2CodeContent.get(q));
        entity.setFiles(new CodeContent[]{content});
        entity.setStdin("");
        if (q2CodeRunInput.containsKey(q)) entity.setStdin(q2CodeRunInput.get(q));
        entity.setCommand("");
        try {
            return "运行中...";
        } finally {
            RUN_CODE_DAEMONS.execute(() -> {
                try {
                    CodeResponse response = new CodeResponse();
                    if (fileName.endsWith("java"))
                        response = runAll.runJava(entity);
                    else if (fileName.endsWith("c"))
                        response = runAll.runC(entity);
                    else if (fileName.endsWith("py"))
                        response = runAll.runPython(entity);
                    else
                        response = runAll.runAny(entity, SUPPORTED_LANGUAGE.get(fileName.substring(fileName.indexOf(".") + 1)));
                    String result = String.format("运行结果:\n%s\n\n运行警告:\n%s\n\n运行错误:\n%s\n\n", response.getStdout(), response.getStderr(), response.getError());
                    group.sendMessage(result);
                } catch (Exception e) {
                    e.printStackTrace();
                    group.sendMessage("运行失败,可能因为输出过多");
                }
            });
        }
    }

    public String createFile(String fileName, long q) {
        if (!(fileName.indexOf(".") > 0 && SUPPORTED_LANGUAGE.containsKey(fileName.substring(fileName.indexOf(".") + 1))))
            return "未知文件类型:" + fileName + "\nUnknown File Type:" + fileName;
        String f1 = q2Filename.put(q, fileName);
        return f1 == null ? String.format("创建文件:%s成功", fileName) : String.format("覆盖文件:%s,并创建文件:%s", f1, fileName);
    }

    public String writeFile(String content, long q, boolean k) {
        String oldContent = "";
        if (k && q2CodeContent.containsKey(q))
            oldContent = q2CodeContent.get(q);
        oldContent = oldContent + content;
        q2CodeContent.put(q, oldContent);
        return q2CodeContent.get(q);
    }
}
