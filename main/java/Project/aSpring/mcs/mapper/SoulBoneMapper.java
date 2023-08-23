package Project.aSpring.mcs.mapper;

import Project.aSpring.dao.SoulBone;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author github.kloping
 */
@Repository
public interface SoulBoneMapper extends BaseMapper<SoulBone> {
    /**
     * get bones
     *
     * @param qid
     * @return
     */
    @Select("SELECT * FROM `soul_bone` WHERE `qid`=#{qid}")
    List<SoulBone> selectBons(@Param("qid") Long qid);

    /**
     * delete a
     *
     * @param soulBone
     * @return
     */
    @Delete("DELETE FROM `soul_bone` WHERE  `qid`=#{qid} AND `type`=#{type} AND `value`=#{value} AND `oid`=#{oid} AND `time`=#{time} LIMIT 1;")
    Integer delete(SoulBone soulBone);
}
