package io.github.kloping.gb.spring.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author github.kloping
 */
@Mapper
public interface SingListMapper {
    /**
     * insert a sign
     *
     * @param qid
     * @param day
     * @param time
     * @return
     */
    @Insert("INSERT INTO `signlist` (`qid`, `day`, `time`) VALUES (#{qid}, #{day}, #{time});")
    Integer insert(@Param("qid") String qid, @Param("day") String day, @Param("time") Long time);

    /**
     * select day
     *
     * @param day
     * @return
     */
    @Select("SELECT `qid` FROM `signlist` WHERE `day`=#{day} ORDER BY `time`")
    List<String> selectDay(@Param("day") String day);


    /**
     * 查询今日签到的人数
     *
     * @param day
     * @return
     */
    @Select("SELECT count(*) FROM `signlist` WHERE day=#{day}")
    Integer selectCountByDay(@Param("day") String day);
}
