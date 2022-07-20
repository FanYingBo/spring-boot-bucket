package com.study.spring.boot.sharding.jdbc.service;

import com.study.spring.boot.sharding.jdbc.entity.UserMaster;
import com.study.spring.boot.sharding.jdbc.mapper.UserMasterMapper;
import io.shardingsphere.core.keygen.KeyGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserMasterServiceImpl implements  UserMasterService{

    @Autowired
    private KeyGenerator keyGenerator;
    @Autowired
    private UserMasterMapper userMasterMapper;
    @Override
    public UserMaster addUser(UserMaster userMaster) {
        long longValue = keyGenerator.generateKey().longValue();
        userMaster.setUserId(longValue);
        userMasterMapper.addUserMaster(userMaster);
        return userMaster;
    }

    @Override
    public UserMaster getUser(Long userId) {
        return userMasterMapper.getUserMasterById(userId);
    }
}
