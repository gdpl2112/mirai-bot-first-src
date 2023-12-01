package io.github.kloping.kzero.spring.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.kloping.kzero.spring.dao.BindMap;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @author github.kloping
 */
@Mapper
public interface BindMapper extends BaseMapper<BindMap> {
    @Select("SELECT tid FROM bind_map WHERE bid=#{bid} AND sid=#{sid}")
    String tid(@Param("bid") String bid, @Param("sid") String sid);
}
