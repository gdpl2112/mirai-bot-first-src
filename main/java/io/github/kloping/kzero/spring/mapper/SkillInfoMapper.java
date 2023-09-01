package io.github.kloping.kzero.spring.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.kloping.kzero.spring.dao.SkillInfo;
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
