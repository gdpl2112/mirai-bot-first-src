package io.github.kloping.kzero.spring.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.kloping.kzero.spring.dao.TradingRecord;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author github.kloping
 */
@Repository
public interface TradingRecordMapper extends BaseMapper<TradingRecord> {
    /**
     * get list
     *
     * @param qid
     * @return
     */
    @Select("SELECT * FROM `trading_record` WHERE main=#{qid} ORDER BY `time` DESC LIMIT 1000;")
    List<TradingRecord> getList(Long qid);
}
