package Entitys;

import java.util.Map;

public class Group {
    private Long id;
    private String nickName;

    private Group(Long id, String nickName) {
        this.id = id;
        this.nickName = nickName;
    }

    public static Group create(long id, String name, Map<Long, Group> histGroupMap) {
        if (histGroupMap.containsKey(id)) return histGroupMap.get(id);
        Group group = new Group(id, name);
        histGroupMap.put(
                id, group
        );
        return group;
    }

    public long getId() {
        return id.longValue();
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}
