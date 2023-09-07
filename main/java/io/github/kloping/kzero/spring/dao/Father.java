package io.github.kloping.kzero.spring.dao;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.annotation.TableId;
import io.github.kloping.judge.Judge;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

/**
 * @author github.kloping
 */
@Data
@Accessors(chain = true)
public class Father {
    @TableId
    private String sid;
    private String permissions;

    private List<String> permissionsList() {
        if (Judge.isEmpty(permissions)) return new ArrayList<>();
        else return JSON.parseObject(permissions, ArrayList.class);
    }

    private Father addPermission(String pid) {
        permissions = JSON.toJSONString(permissionsList().add(pid));
        return this;
    }

    private Father removePermission(String pid) {
        permissions = JSON.toJSONString(permissionsList().remove(pid));
        return this;
    }
}
