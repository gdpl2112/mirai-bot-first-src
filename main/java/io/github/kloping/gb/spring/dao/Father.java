package io.github.kloping.gb.spring.dao;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.HashSet;
import java.util.Set;

/**
 * @author github.kloping
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class Father {
    public static final String ALL = "0";
    public static final String SPLIT = ",";

    @TableId(type = IdType.ASSIGN_UUID)
    private Long id;
    private String permission = "";

    public Set<String> getGids() {
        Set<String> set = new HashSet<>();
        for (String s : permission.split(SPLIT)) {
            if (s.trim().isEmpty()) continue;
            set.add(s.trim());
        }
        return set;
    }

    public boolean hasPermission(long gid) {
        return getGids().contains(gid);
    }

    public String getPermission() {
        return permission;
    }

    public String addPermission(String gid) {
        Set<String> set = getGids();
        permission = "";
        set.add(gid);
        for (String aLong : set) {
            permission = permission + SPLIT + aLong;
        }
        if (permission.startsWith(SPLIT)) {
            permission = permission.substring(1);
        }
        if (permission.equals(SPLIT)) {
            permission = permission.substring(0, permission.length() - 1);
        }
        return permission;
    }
}
