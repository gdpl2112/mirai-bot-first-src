package io.github.kloping.kzero.spring.dao;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.annotation.TableField;
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

    @TableField(exist = false)
    public static final String SUPER_PER = "super0";

    @TableId
    private String sid;
    private String permissions = "[]";

    public List<String> permissionsList() {
        if (Judge.isEmpty(permissions)) return new ArrayList<>();
        else return JSON.parseObject(permissions, new TypeReference<ArrayList<String>>() {
        });
    }

    public Father addPermission(String pid) {
        List<String> list = permissionsList();
        list.add(pid);
        permissions = JSON.toJSONString(list);
        return this;
    }

    public Father removePermission(String pid) {
        List<String> list = permissionsList();
        list.remove(pid);
        permissions = JSON.toJSONString(list);
        return this;
    }

    public boolean hasPerm(String subjectId) {
        return permissions.contains(SUPER_PER) || permissionsList().contains(subjectId);
    }
}
