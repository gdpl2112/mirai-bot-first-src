package Project.aSpring.mcs.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.kloping.mirai0.Entitys.UserScore;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

/**
 * @author github-kloping
 */
@Repository
public interface UserScoreMapper extends BaseMapper<UserScore> {
    /**
     * 清除所有犯罪
     *
     * @return
     */
    @Update("UPDATE `user_score` SET `fz`=0")
    Integer updateAll();

    /**
     * 查询今日签到的人数
     * @param day
     * @return
     */
    @Select("SELECT count(*) FROM user_score WHERE day=#{day}")
    Integer selectCountByDay(@Param("day") Integer day);
}
