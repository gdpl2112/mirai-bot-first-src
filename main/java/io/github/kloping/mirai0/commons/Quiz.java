package io.github.kloping.mirai0.commons;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 竞猜
 *
 * @author github.kloping
 */
public class Quiz {
    public static Quiz quiz = null;
    private boolean processing = true;
    private Map<Integer, String> quizData = new HashMap<>();
    private List<QuizSon> quizSons = new ArrayList<>();
    private String title = "";

    public static class QuizSon {
        public Long qid;
        public Integer index;
        public Long sc;

        public QuizSon(Long qid, Integer index, Long sc) {
            this.qid = qid;
            this.index = index;
            this.sc = sc;
        }

        public Long getQid() {
            return qid;
        }

        public Integer getIndex() {
            return index;
        }

        public Long getSc() {
            return sc;
        }
    }

    public static Quiz getQuiz() {
        return quiz;
    }

    public static void setQuiz(Quiz quiz) {
        Quiz.quiz = quiz;
    }

    public boolean append(long qid, int index, long sc) {
        int i = 0;
        for (QuizSon quizSon : quizSons) {
            if (quizSon.getQid() == qid)
                i++;
        }
        if (i == 3) return false;
        else {
            QuizSon son = new QuizSon(qid, index, sc);
            quizSons.add(son);
            return true;
        }
    }

    public int getAll() {
        int all = 0;
        for (QuizSon quizSon : quizSons) {
            all += quizSon.getSc();
        }
        return all;
    }

    public int getAll(int index) {
        int all = 0;
        for (QuizSon quizSon : quizSons) {
            if (quizSon.getIndex() == index)
                all += quizSon.getSc();
        }
        return all;
    }

    public List<QuizSon> getQuizSons() {
        return quizSons;
    }

    public void setQuizSons(List<QuizSon> quizSons) {
        this.quizSons = quizSons;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Map<Integer, String> getQuizData() {
        return quizData;
    }

    public void setQuizData(Map<Integer, String> quizData) {
        this.quizData = quizData;
    }

    public boolean isProcessing() {
        return processing;
    }

    public void setProcessing(boolean processing) {
        this.processing = processing;
    }
}
