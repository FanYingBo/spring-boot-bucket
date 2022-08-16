package self.study.alibaba.nacos.service.provider.mapper;


import self.study.alibaba.nacos.service.provider.entity.UserMaster;

public interface UserMasterMapper {

    UserMaster getUserMasterById(long userId);

    int addUserMaster(UserMaster userMaster);
}
