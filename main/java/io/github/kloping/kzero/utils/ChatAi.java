package io.github.kloping.kzero.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.stream.Collectors;

/**
 * @author github.kloping
 */
public class ChatAi {
    public static String chat(String sid, String msg) {
        AiData aiData = new AiData();
        aiData.setModel("gpt-4o-mini-2024-07-18");
        aiData.setTemperature(0.7);
        Queue<Message> messages = getMessage(sid);
        if (messages.size() == MAX) {
            messages.poll();
            messages.poll();
        }
        messages.offer(new Message("user", msg));
        aiData.setMessages(messages.stream().collect(Collectors.toList()));
        try {
            Document doc0 = Jsoup.connect("https://api.chatanywhere.tech/v1/chat/completions")
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer sk-Kz2MEINteHgVdYZU6Gw19VWLETj1AKWh9pyHPzukqLsArKcJ")
                    .ignoreContentType(true)
                    .requestBody(JSON.toJSONString(aiData)).post();
            Assistant assistant = JSONObject.parseObject(doc0.body().text()).toJavaObject(Assistant.class);
            String ass = assistant.getChoices().get(0).getMessage().getContent();
            messages.offer(new Message("assistant", ass));
            return ass;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    private static Map<String, Queue<Message>> sid2msgs = new HashMap<>();
    public static final Integer MAX = 20;

    private static Queue<Message> getMessage(String sid) {
        if (sid2msgs.containsKey(sid)) {
            return sid2msgs.get(sid);
        } else {
            Queue<Message> queue = new ArrayBlockingQueue<>(MAX);
            sid2msgs.put(sid, queue);
            return queue;
        }
    }

    @Data
    public static class AiData {
        private String model;
        private List<Message> messages;
        private double temperature;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Message {
        private String role;
        private String content;
    }
}
