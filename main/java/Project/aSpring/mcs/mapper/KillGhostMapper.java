package Project.aSpring.mcs.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * @author github.kloping
 */
@Mapper
public interface KillGhostMapper {
    /**
     * select
     *
     * @param num
     * @return
     */
    @Select("select id,num from killc order by num desc limit #{num}")
    List<Map<String, Number>> select(Integer num);

    /**
     * update
     *
     * @param num
     * @param id
     * @return
     */
    @Select("update killc set num=#{num} where id=#{id}")
    Integer update(@Param("num") Integer num, @Param("id") Long id);

    /**
     * insert
     *
     * @param num
     * @param id
     * @return
     */
    @Insert("insert into killc (id,num) VALUES(#{id},#{num})")
    Integer insert(@Param("num") Integer num, @Param("id") Long id);

    /**
     * get one num
     *
     * @param qid
     * @return
     */
    @Select("select num from killc where id=#{qid}")
    Integer getNum(Long qid);
}
