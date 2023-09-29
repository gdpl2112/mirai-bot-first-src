package io.github.kloping.kzero.spring.mapper;

import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author github.kloping
 */
@Mapper
public interface BagMapper {
    /**
     * 插入一条记录
     *
     * @param oid  物品
     * @param sid  玩家id
     * @param time 时间
     * @return
     */
    @Insert("INSERT INTO `bag` (`oid`, `qid`, `time`) VALUES (#{oid},#{qid},#{time});")
    Integer insert(@Param("oid") Integer oid, @Param("qid") String sid, @Param("time") Long time);

    /**
     * 插入一条记录
     *
     * @param oid  物品
     * @param sid  玩家id
     * @param time 时间
     * @param desc 描述
     * @return
     */
    @Insert("INSERT INTO `bag` (`oid`, `qid`, `time`,`desc`) VALUES (#{oid},#{qid},#{time},#{desc});")
    Integer insertWithDesc(@Param("oid") Integer oid, @Param("qid") String sid, @Param("time") Long time, @Param("desc") String desc);

    /**
     * 获取玩家背包中指定的物品id
     *
     * @param sid
     * @param oid
     * @param num
     * @return
     */
    @Select("SELECT `id` FROM `bag` WHERE `qid`=#{qid} and `state`=0 and oid=#{oid}  ORDER BY `time` LIMIT #{num}")
    List<Integer> selectIds(@Param("qid") String sid, @Param("oid") Integer oid, @Param("num") Integer num);

    /**
     * 获取玩家背包中指定的物品id
     *
     * @param sid
     * @param oid
     * @return
     */
    @Select("SELECT `id` FROM `bag` WHERE `qid`=#{qid} and `state`=0 and oid=#{oid}  ORDER BY `time` LIMIT 1")
    Integer selectId(@Param("qid") String sid, @Param("oid") Integer oid);

    /**
     * 获取玩家背包
     *
     * @param sid
     * @return
     */
    @Select("SELECT `oid` FROM `bag` WHERE `qid`=#{qid} and `state`=0")
    List<Integer> selectAll(@Param("qid") String sid);

    /**
     * 获取 ids
     *
     * @param sid
     * @return
     */
    @Select("SELECT `id` FROM `bag` WHERE `qid`=#{qid} and `state`=0")
    List<Integer> selectAllIds(@Param("qid") String sid);

    /**
     * 更新玩家背包物品状态
     *
     * @param id
     * @return
     */
    @Update("UPDATE `bag` SET `state`=1 WHERE `id`=#{id}")
    Integer update(@Param("id") Integer id);
}