package io.github.kloping.kzero.spring.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.kloping.kzero.spring.dao.UserScore;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
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

    /**
     * 获取未结束的
     *
     * @param t
     * @return
     */
    @Select("select * from user_score where k>#{t}")
    List<UserScore> selectA1(@Param("t") Long t);
}

