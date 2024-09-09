package io.github.kloping.kzero.spring.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.kloping.kzero.spring.dao.FuncData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author github.kloping
 */
@Mapper
public interface FuncDataMapper extends BaseMapper<FuncData> {
    @Select("select count(*) from func_data where ftype=#{ftype}")
    Integer selectCountByType(Integer ftype);

    @Select("select * from func_data where ftype=#{ftype}")
    List<FuncData> selectList(Integer ftype);
}
