package Project.ASpring.mapper;

import Entitys.gameEntitys.GInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

@Repository
public interface GInfoMapper extends BaseMapper<GInfo> {
}
