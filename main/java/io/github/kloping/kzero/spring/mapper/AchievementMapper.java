package io.github.kloping.kzero.spring.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.kloping.kzero.spring.dao.Achievement;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * @author github.kloping
 */

@Repository
public interface AchievementMapper extends BaseMapper<Achievement> {
    /**
     * 查询
     *
     * @param aid
     * @param qid
     * @return
     */
    @Select("SELECT * FROM `achievement` WHERE `aid`=#{aid} AND `qid`=#{qid};")
    Achievement selectByAidAndQid(@Param("aid") Integer aid, @Param("qid") Long qid);
}
