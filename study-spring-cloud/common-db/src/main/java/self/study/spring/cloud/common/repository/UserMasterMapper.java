package self.study.spring.cloud.common.repository;


import self.study.spring.cloud.common.model.UserMaster;

public interface UserMasterMapper {

    UserMaster getUserMasterById(long userId);

    int addUserMaster(UserMaster userMaster);
}
