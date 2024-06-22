package io.github.kloping.kzero.spring.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.kloping.kzero.spring.dao.FuncData;
import io.github.kloping.kzero.spring.dao.SweatherData;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author github.kloping
 */
@Mapper
public interface FuncDataMapper extends BaseMapper<FuncData> {
}
