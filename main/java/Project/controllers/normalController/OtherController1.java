package Project.controllers.normalController;


import Project.interfaces.http_api.JiaKaoBaoDian;
import com.alibaba.fastjson.JSON;
import io.github.kloping.MySpringTool.annotations.*;
import io.github.kloping.MySpringTool.exceptions.NoRunException;
import io.github.kloping.initialize.FileInitializeValue;
import io.github.kloping.mirai0.Main.ITools.MessageTools;
import io.github.kloping.mirai0.commons.Group;
import io.github.kloping.mirai0.commons.apiEntitys.jkbd.Data;
import io.github.kloping.mirai0.commons.apiEntitys.jkbd.QuestionData;
import io.github.kloping.mirai0.commons.apiEntitys.jkbd.Record;
import io.github.kloping.mirai0.commons.apiEntitys.jkbd.pre.QuestionIdData;
import io.github.kloping.mirai0.unitls.Tools.JsUtils;

import java.util.HashMap;
import java.util.Map;

import static Project.controllers.auto.ControllerTool.opened;
import static io.github.kloping.judge.Judge.isNotEmpty;
import static io.github.kloping.mirai0.Main.Resource.DEA_THREADS;
import static io.github.kloping.mirai0.Main.Resource.println;
import static io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet.FinalFormat.CE_CA;
import static io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet.FinalString.*;
import static io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet.FinalValue.NOT_OPEN_NO_RUN_EXCEPTION;
import static io.github.kloping.mirai0.unitls.Tools.Tool.getInteagerFromStr;
import static io.github.kloping.mirai0.unitls.Tools.Tool.pathToImg;

/**
 * @author github kloping
 */
@Controller
public class OtherController1 {

    private static final String PATH = "./records/jkbd/record.json";
    private static final int BASE = 16;
    private static final Map<Integer, Character> ANSWERS2ID = new HashMap<>();
    private static final Map<Character, Integer> ID2ANSWERS = new HashMap<>();
    private static Record record = new Record();

    static {
        for (int i = 1; i <= 8; i++) {
            int n = BASE;
            char c0 = (char) ('A' + i - 1);
            for (int i1 = 1; i1 < i; i1++) {
                n *= 2;
            }
            ANSWERS2ID.put(n, c0);
            ID2ANSWERS.put(c0, n);
        }
        System.out.println();
    }

    @AutoStand
    JiaKaoBaoDian dian;
    private QuestionData questionData;

    public OtherController1() {
        println(this.getClass().getSimpleName() + "构建");
        load();
    }

    private void load() {
        record = FileInitializeValue.getValue(PATH, record, true);
    }

    @Before
    public void before(@AllMess String mess, Group group) throws NoRunException {
        if (mess.contains(OPEN_STR) || mess.contains(CLOSE_STR)) {
            return;
        }
        if (!opened(group.getId(), this.getClass())) {
            throw NOT_OPEN_NO_RUN_EXCEPTION;
        }
    }

    @Action("驾校考题.+")
    public String s0(@AllMess String ss) {
        int s = getInteagerFromStr(ss);
        if (record.getUpData() == null || record.getUpData().isEmpty()) {
            QuestionIdData idData = dian.ids(JsUtils.f(1), null, null, null, s, s);
            record.setUpData(JSON.toJSONString(idData));
        }
        apply();
        return ret();
    }

    private String ret() {
        questionData = dian.data(JsUtils.f(1), record.questionIdData().getData().getQuestionList()[record.getUpSt()].toString());
        return toView(questionData);
    }

    private String toView(QuestionData questionData) {
        StringBuilder sb = new StringBuilder();
        Data data = questionData.getData()[0];
        sb.append("题目:\n");
        sb.append(data.getQuestion()).append(NEWLINE);
        if (isNotEmpty(data.getMediaContent()))
            sb.append(pathToImg(data.getMediaContent())).append("\n");
        sb.append("选项:\n");
        if (isNotEmpty(data.getOptionA()))
            sb.append("A:").append((data.getOptionA())).append(NEWLINE);
        if (isNotEmpty(data.getOptionB()))
            sb.append("B:").append((data.getOptionB())).append(NEWLINE);
        if (isNotEmpty(data.getOptionC()))
            sb.append("C:").append((data.getOptionC())).append(NEWLINE);
        if (isNotEmpty(data.getOptionD()))
            sb.append("D:").append((data.getOptionD())).append(NEWLINE);
        if (isNotEmpty(data.getOptionE()))
            sb.append("E:").append((data.getOptionE())).append(NEWLINE);
        if (isNotEmpty(data.getOptionF()))
            sb.append("F:").append((data.getOptionF())).append(NEWLINE);
        if (isNotEmpty(data.getOptionG()))
            sb.append("G:").append((data.getOptionG())).append(NEWLINE);
        if (isNotEmpty(data.getOptionH()))
            sb.append("H:").append((data.getOptionH())).append(NEWLINE);
        sb.append("错误率:").append(data.getWrongRate()).append(NEWLINE);
        return sb.toString();
    }

    @Action("选<.+=>a>")
    public String s1(@Param("a") String a, Group group) {
        a = a.toUpperCase();
        int i = findAnswer(a);
        boolean k = false;
        try {
            if (questionData.getData()[0].getAnswer().intValue() == i) {
                k = true;
                return "太棒了,回答正确";
            }
            return "回答错误";
        } catch (Exception e) {
            return null;
        } finally {
            sendNext(k, group.getId());
        }
    }

    private void sendNext(final boolean k, long gid) {
        DEA_THREADS.submit(() -> {
            Data data = questionData.getData()[0];
            if (!k) {
                StringBuilder sb = new StringBuilder();
                sb.append("说明: ").append(data.getExplain()).append(NEWLINE);
                sb.append("").append(data.getConciseExplain()).append(NEWLINE);
                sb.append("提示: ").append(data.getKnackDetail()).append(NEWLINE);
                sb.append("正确答案: ").append(toAnswer(data.getAnswer().intValue()));
                MessageTools.sendMessageInGroup(sb.toString(), gid);
                record.addError();
            } else {
                record.addCorrect();
            }
            record.add();
            apply();
            MessageTools.sendMessageInGroup(ret(), gid);
            MessageTools.sendMessageInGroup(String.format(CE_CA, record.getCorrect(), record.getError(), record.getUpSt(), record.questionIdData().getData().getQuestionList().length), gid);
        });
    }

    private char toAnswer(int i) {
        return ANSWERS2ID.get(i);
    }

    private int findAnswer(String a) {
        for (char c : a.toCharArray()) {
            if (c >= 'A' && c <= 'Z') {
                return ID2ANSWERS.get(c);
            }
        }
        return -1;
    }

    @Action("上一题")
    public String s2() {
        record.setUpSt(record.getUpSt() - 1);
        apply();
        return ret();
    }

    private void apply() {
        record = FileInitializeValue.putValues(PATH, record, true);
    }
}