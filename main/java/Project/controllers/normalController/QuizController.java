package Project.controllers.normalController;

import Project.aSpring.dao.UserScore;
import Project.dataBases.DataBase;
import Project.utils.Tools.Tool;
import io.github.kloping.MySpringTool.annotations.Action;
import io.github.kloping.MySpringTool.annotations.AllMess;
import io.github.kloping.mirai0.commons.Quiz;
import io.github.kloping.number.NumberUtils;

import static Project.commons.rt.ResourceSet.FinalString.NEWLINE;

/**
 * @author github.kloping
 */
public class QuizController {

    @Action("竞猜.+")
    public String s3(long qid, @AllMess String mess) {
        if (!Quiz.quiz.isProcessing())
            return "竞猜已停止";
        StringBuilder sb = new StringBuilder();
        String[] sss = mess.substring(2).split(",|，");
        Integer index = Tool.INSTANCE.getInteagerFromStr(sss[0]);
        Integer sc = Tool.INSTANCE.getInteagerFromStr(sss[1]);
        sc = sc == null ? 0 : sc;
        if (sc > 10000) {
            sc = 10000;
            sb.append("最大竞猜1000自动转换\n");
        } else if (sc < 10) {
            sc = 10;
            sb.append("最小竞猜10自动转换\n");
        }
        if (Quiz.quiz.append(qid, index, sc)) {
            UserScore userScore = DataBase.getUserInfo(qid);
            if (userScore.getScore() < sc) return "积分不足";
            userScore.addScore(-sc);
            int all = Quiz.quiz.getAll();
            sb.append(Quiz.quiz.getTitle()).append(NEWLINE);
            Quiz.quiz.getQuizData().forEach((k, v) -> {
                int a0 = Quiz.quiz.getAll(k);
                int b = NumberUtils.toPercent(a0, all);
                sb.append(k).append(v).append("==>>").append(b).append("%").append("(").append(a0).append(")\n");
            });
            return sb.toString();
        } else {
            return "竞猜失败,最大三次竞猜";
        }
    }
}
