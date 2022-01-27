package io.github.kloping.mirai0.Entitys;


public class Mess {
    /**
     * 消息 类型
     * 101:群聊
     */
    private final Integer type;//1.类型

    /**
     * 群名
     */
    private final String groupName;
    /**
     * 此人在此群的名片
     */
    private final String card;
    /**
     * 发送人QQ
     */
    private final Long qq;
    /**
     * 群Id
     */
    private final Long groupId;
    /**
     * 发送时间
     */
    private final Long time;
    /**
     * 消息Id
     */
    private final Long id;
    /**
     * 消息序号
     */
    private final Long idst;
    /**
     * 内容
     */
    private String msg;

    public Mess(Integer type, String groupName, String card, Long qq, Long groupId, Long time, Long id, Long idst, String msg) {
        this.type = type;
        this.groupName = groupName;
        this.card = card;
        this.qq = qq;
        this.groupId = groupId;
        this.time = time;
        this.id = id;
        this.idst = idst;
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public Integer getType() {
        return type;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getCard() {
        return card;
    }

    public Long getQq() {
        return qq;
    }

    public Long getGroupId() {
        return groupId;
    }

    public Long getTime() {
        return time;
    }

    public Long getId() {
        return id;
    }

    public Long getIdst() {
        return idst;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static final String s = "β";

    @Override
    public String toString() {
        if (idst == 0 && id == 0)
            return qq + s + groupId + s + msg + s + type;
        else
            return qq + s + groupId + s + msg + s + type + s + idst + s + id;
    }
}