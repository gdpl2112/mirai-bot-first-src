package Project.aSpring.mcs.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

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
    @Select("SELECT `state` FROM `upup` WHERE `qid`=#{qid} AND `level`=#{level}")
    Integer select(@Param("qid") Long qid, @Param("level") Integer level);

    /**
     * 插入一记录
     *
     * @param qid
     * @param level
     * @return
     */
    @Insert("INSERT INTO `upup` (`qid`, `level`) VALUES (#{qid}, #{level});")
    Integer insert(@Param("qid") Long qid, @Param("level") Integer level);
}
