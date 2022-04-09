package Project.aSpring.mcs.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.kloping.mirai0.commons.UserScore;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

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
     * 清除收益记录
     *
     * @return
     */
    @Update("UPDATE `user_score` SET `earnings`=0,`debuffs`=0")
    Integer updateEarnings();

    /**
     * to day
     *
     * @param num
     * @return
     */
    @Select("select * from user_score order by times desc limit #{num};")
    List<UserScore> toDay(@Param("num") Integer num);

    /**
     * ph
     *
     * @param num
     * @return
     */
    @Select("select * from user_score order by s_times desc limit #{num};")
    List<UserScore> ph(@Param("num") Integer num);
}
