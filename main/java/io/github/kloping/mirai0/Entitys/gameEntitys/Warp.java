package io.github.kloping.mirai0.Entitys.gameEntitys;

import Project.aSpring.SpringBootResource;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import static Project.dataBases.GameDataBase.getWarp;
import static Project.dataBases.GameDataBase.setWarp;

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
    private Long prentice = -1L;

    public Warp setId(String id) {
        try {
            this.id = Long.parseLong(id.trim());
        } catch (Exception e) {
        }
        return this;
    }

    public static Warp getInstance(long q) {
        return SpringBootResource.getWarpMapper().selectById(q);
    }

    public static Warp getInstanceFromFile(long q) {
        return getWarp(q);
    }

    public void apply() {
        UpdateWrapper<Warp> qw = new UpdateWrapper<>();
        qw.eq("id", id);
        SpringBootResource.getWarpMapper().update(this, qw);
    }
}
