package io.github.kloping.mirai0.commons;

import Project.aSpring.SpringBootResource;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

import static Project.dataBases.GameDataBase.getWarp;

/**
 * @author github-kloping
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class Warp {
    @TableId
    private Long id = -1L;
    private Long bindQ = -1L;
    private Long master = -1L;
    private String prentice = "";

    public static Warp getInstance(long q) {
        return getWarp(q);
    }

    public static Warp getInstanceFromFile(long q) {
        return getWarp(q);
    }

    public Warp setId(String id) {
        try {
            this.id = Long.parseLong(id.trim());
        } catch (Exception e) {
        }
        return this;
    }

    public void addP(long q) {
        if (!prentice.startsWith("[")) {
            prentice = "[" + prentice + "]";
        }
        JSONArray array = JSON.parseArray(prentice);
        List<Long> list = array.toJavaList(Long.class);
        list.add(q);
        list.remove(-1L);
        prentice = JSON.toJSONString(list);
    }

    public List<Long> allP() {
        if (!prentice.startsWith("[")) {
            prentice = "[" + prentice + "]";
        }
        JSONArray array = JSON.parseArray(prentice);
        List<Long> list = array.toJavaList(Long.class);
        list.remove(-1L);
        return list;
    }

    public void removeP(long q) {
        if (!prentice.startsWith("[")) {
            prentice = "[" + prentice + "]";
        }
        JSONArray array = JSON.parseArray(prentice);
        List<Long> list = array.toJavaList(Long.class);
        list.remove(q);
        list.remove(-1L);
        prentice = JSON.toJSONString(list);
    }

    public void apply() {
        UpdateWrapper<Warp> qw = new UpdateWrapper<>();
        qw.eq("id", id);
        SpringBootResource.getWarpMapper().update(this, qw);
    }

    public boolean isEmpty0() {
        return allP().isEmpty() || (allP().size() == 1 && allP().contains(-1L));
    }
}
