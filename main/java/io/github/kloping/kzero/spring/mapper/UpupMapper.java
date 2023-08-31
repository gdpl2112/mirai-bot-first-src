package io.github.kzero.spring.mapper;

import org.apache.ibatis.annotations.*;

/**
 * @author github.kloping
 */
@Mapper
public interface UpupMapper {
    /**
     * 查询是否在该等级升级过
     *
     * @param qid
     * @param level
     * @return
     */
    @Select("SELECT `state` FROM `upup` WHERE `qid`=#{qid} AND `level`=#{level} AND `p`=#{p}")
    Integer select(@Param("qid") Long qid, @Param("level") Integer level, @Param("p") Integer p);

    /**
     * 插入一记录
     *
     * @param qid
     * @param level
     * @return
     */
    @Insert("INSERT INTO `upup` (`qid`, `level` ,`p` ) VALUES (#{qid}, #{level} ,#{p});")
    Integer insert(@Param("qid") Long qid, @Param("level") Integer level, @Param("p") Integer p);

    /**
     * delete
     *
     * @param qid
     * @return
     */
    @Delete("DELETE FROM `upup` WHERE  `qid`=#{qid} AND `p`=#{p}")
    Integer deleteByQq(@Param("qid") Long qid, @Param("p") Integer p);
}
