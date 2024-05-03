package io.github.kloping.kzero.hwxb.dto;

import lombok.Data;

/**
 * @author github.kloping
 */
@Data
public class Group extends User {
    private User[] adminIdList;
    private User[] memberList;
    private String topic;

    @Override
    public String getName() {
        return getTopic();
    }

    @Override
    public String getAlias() {
        return getTopic();
    }

    public void setTopic(String topic) {
        this.topic = topic;
        setName(topic);
        setAlias(topic);
    }
}
