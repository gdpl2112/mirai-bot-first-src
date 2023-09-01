package io.github.kloping.kzero.spring.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.kloping.kzero.spring.dao.PersonInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author github-kloping
 */
@Repository
public interface PersonInfoMapper extends BaseMapper<PersonInfo> {

    /**
     * get排名
     *
     * @param num
     * @return
     */
    @Select("SELECT * FROM person_info ORDER BY win_c DESC LIMIT #{num}")
    List<PersonInfo> getOrderByStar(@Param("num") Integer num);


    /**
     * 更新全部 0点
     *
     * @return
     */
    @Update("UPDATE `person_info` SET `my_tag`='',`help_c`=0,`help_toc`=0,`buy_help_c`=0,`died`=0,`downed`=0,`buy_help_c`=0,`buy_help_to_c`=0")
    Integer updateAll();
}
