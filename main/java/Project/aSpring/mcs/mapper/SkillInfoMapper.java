package Project.aSpring.mcs.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.kloping.mirai0.commons.gameEntitys.SkillInfo;
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
