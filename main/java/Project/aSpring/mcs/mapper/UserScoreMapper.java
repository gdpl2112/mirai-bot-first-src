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
     * all
     *
     * @return
     */
    @Select("SELECT * FROM `user_score`")
    List<UserScore> selectAll();

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
     * @param d
     * @return
     */
    @Select("select * from user_score where times_day=#{d} order by times desc limit #{num};")
    List<UserScore> toDay(@Param("d") Integer d, @Param("num") Integer num);

    /**
     * ph
     *
     * @param num
     * @return
     */
    @Select("select * from user_score order by s_times desc limit #{num};")
    List<UserScore> ph(@Param("num") Integer num);

    /**
     * score order desc
     *
     * @param num
     * @return
     */
    @Select("select * from user_score order by `score` desc limit #{num};")
    List<UserScore> phScore(@Param("num") Integer num);
}

