package io.github.kloping.kzero.mirai.listeners;

import io.github.kloping.judge.Judge;
import io.github.kloping.spt.annotations.Entity;
import net.mamoe.mirai.event.events.MessageEvent;

import java.util.Deque;
import java.util.LinkedList;

/**
 * @author github.kloping
 */
@Entity
public class MessageMer {

    //=============消息记录start
    public static final Integer MAX_E = 50;
    private MessageEvent temp0 = null;
    private Deque<MessageEvent> QUEUE = new LinkedList<>();


    public void offer(MessageEvent msg) {
        if (QUEUE.contains(msg)) return;
        if (QUEUE.size() >= MAX_E) QUEUE.pollLast();
        QUEUE.offerFirst(msg);
    }

    public MessageEvent getMessage(String id) {
        if (Judge.isEmpty(id)) return null;
        if (temp0 != null && getMessageEventId(temp0).equals(id)) return temp0;
        for (MessageEvent event : QUEUE) {
            if (getMessageEventId(event).equals(id)) return temp0 = event;
        }
        return null;
    }

    public String getMessageEventId(MessageEvent event) {
        if (event.getSource().getIds().length == 0) return "";
        else return String.valueOf(event.getSource().getIds()[0]);
    }
    //=============消息记录end
}
