package Project.aSpring.mcs.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import Project.listeners.SaveHandler;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author github kloping
 * @version 1.0
 */
@Repository
public interface SaveMapper extends BaseMapper<SaveHandler.AllMessage> {
    /**
     * 获取某群中某人发送的消息
     *
     * @param gid
     * @param qid
     * @param num
     * @return
     */
    @Select("select time,id,internal_id,sender_id,bot_id,type,from_id,content from all_message where from_id=#{gid} and sender_id=#{qid} and recalled=0 order by time desc limit #{num};")
    List<SaveHandler.AllMessage> selectMessage(@Param("gid") Long gid, @Param("qid") Long qid, @Param("num") Integer num);
}
