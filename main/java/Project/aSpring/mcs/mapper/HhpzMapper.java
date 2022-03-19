package Project.aSpring.mcs.mapper;

import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author github.kloping
 */
@Mapper
public interface HhpzMapper {
    /**
     * insert a
     *
     * @param qid
     * @param oid
     * @param time
     * @return
     */
    @Insert("INSERT INTO `hhpz` (`qid`, `oid`, `time`) VALUES (#{qid}, #{oid}, #{time});")
    Integer insert(@Param("qid") Long qid, @Param("oid") Integer oid, @Param("time") Long time);

    /**
     * select hh
     *
     * @param qid
     * @return
     */
    @Select("SELECT `oid` FROM `hhpz` WHERE `qid`=#{qid} AND `state`=0 ORDER BY `time`;")
    List<Integer> select(@Param("qid") Long qid);

    /**
     * delete
     *
     * @param id
     * @return
     */
    @Update("DELETE FROM `hhpz` WHERE `id`=#{id}")
    Integer delete(@Param("id") Integer id);

    /**
     * update oid
     *
     * @param id
     * @param oid
     * @return
     */
    @Update("UPDATE `hhpz` SET `oid`=#{oid} WHERE `id`=#{id}")
    Integer update(@Param("id") Integer id, @Param("oid") Integer oid);

    /**
     * select ids
     *
     * @param qid
     * @return
     */
    @Select("SELECT `id` FROM `hhpz` WHERE `qid`=#{qid} AND `state`=0 ORDER BY `time`;")
    List<Integer> selectIds(@Param("qid") Long qid);
}
