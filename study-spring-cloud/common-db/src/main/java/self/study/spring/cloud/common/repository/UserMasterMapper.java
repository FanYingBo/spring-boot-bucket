package self.study.spring.cloud.common.repository;


import org.apache.ibatis.annotations.Param;
import self.study.spring.cloud.common.model.UserMaster;

import java.util.List;

public interface UserMasterMapper {

    UserMaster getUserMasterById(long userId);

    List<UserMaster> getUserMasterByIds(@Param("userIds") List<Long> userIds);

    int addUserMaster(UserMaster userMaster);
}
