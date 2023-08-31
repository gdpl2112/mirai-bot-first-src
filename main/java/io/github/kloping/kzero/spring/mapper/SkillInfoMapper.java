package io.github.kzero.spring.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.kzero.spring.dao.SkillInfo;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author github.kloping
 */
@Repository
public interface SkillInfoMapper extends BaseMapper<SkillInfo> {
    /**
     * select *
     *
     * @return
     */
    @Select("SELECT * FROM `skill_info`")
    List<SkillInfo> selectAll();
}
