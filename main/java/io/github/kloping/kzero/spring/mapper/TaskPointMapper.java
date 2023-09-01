package io.github.kloping.kzero.spring.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.kloping.kzero.spring.dao.TaskPoint;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

/**
 * @author github.kloping
 */
@Repository
public interface TaskPointMapper extends BaseMapper<TaskPoint> {
    /**
     * 更新 normal_index
     *
     * @return
     */
    @Update("UPDATE `task_point` SET `normal_index`=0")
    Integer updateAll();
}
