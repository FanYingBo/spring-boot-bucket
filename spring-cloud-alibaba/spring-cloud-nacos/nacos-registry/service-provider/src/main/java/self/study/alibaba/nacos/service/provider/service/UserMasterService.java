package self.study.alibaba.nacos.service.provider.service;


import self.study.alibaba.nacos.service.provider.entity.UserMaster;

public interface UserMasterService {

    UserMaster addUser(UserMaster userMaster);

    UserMaster getUser(Long userId);
}
