package Project.aSpring.mcs.mapper;

import Project.commons.gameEntitys.SkillInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
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
