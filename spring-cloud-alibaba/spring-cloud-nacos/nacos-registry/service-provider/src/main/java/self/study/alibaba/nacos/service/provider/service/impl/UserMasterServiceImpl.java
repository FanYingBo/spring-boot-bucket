package self.study.alibaba.nacos.service.provider.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import self.study.alibaba.nacos.service.provider.entity.UserMaster;
import self.study.alibaba.nacos.service.provider.mapper.UserMasterMapper;
import self.study.alibaba.nacos.service.provider.service.UserMasterService;

import java.util.Random;

@Service
public class UserMasterServiceImpl implements UserMasterService {

    @Autowired
    private UserMasterMapper userMasterMapper;
    @Override
    public UserMaster addUser(UserMaster userMaster) {
        int random = new Random().nextInt(100);
        long userId = System.currentTimeMillis()+ random;
        userMaster.setUserId(userId);
        return userMaster;
    }

    @Override
    public UserMaster getUser(Long userId) {
        return userMasterMapper.getUserMasterById(userId);
    }
}
