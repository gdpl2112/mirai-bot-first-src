package io.github.kloping.kzero.spring.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.kloping.kzero.spring.dao.PersonInfo;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author github.kloping
 */
@Mapper
public interface PersonInfoMapper extends BaseMapper<PersonInfo> {
}
