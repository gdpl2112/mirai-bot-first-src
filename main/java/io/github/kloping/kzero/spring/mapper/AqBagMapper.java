package io.github.kzero.spring.mapper;

import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

/**
 * @author github.kloping
 */
@Mapper
public interface AqBagMapper {
    /**
     * insert a
     *
     * @param oid
     * @param qid
     * @param num
     * @param time
     * @return
     */
    @Insert("INSERT INTO `aq_bag` (`oid`, `qid`, `num`, `time`) VALUES (#{oid}, #{qid}, #{num}, #{time});")
    Integer insert(@Param("oid") Integer oid, @Param("qid") Long qid, @Param("num") Integer num, @Param("time") Long time);

    /**
     * get aq
     *
     * @param qid
     * @return
     */
    @Select("SELECT id,oid,num FROM `aq_bag` WHERE `qid`=#{qid} and state=0")
    List<Map<String, Integer>> selectAq(@Param("qid") Long qid);

    /**
     * update
     *
     * @param num
     * @param state
     * @param id
     * @return
     */
    @Update("UPDATE `aq_bag` SET `num`=#{num},`state`=#{state} WHERE id=#{id}")
    Integer update(@Param("num") Integer num, @Param("state") Integer state, @Param("id") Integer id);

    /**
     * delete
     *
     * @param qid
     * @param id
     * @return
     */
    @Delete("DELETE FROM `aq_bag` WHERE id=#{id}")
    Integer delete(@Param("id") Integer id);
}
