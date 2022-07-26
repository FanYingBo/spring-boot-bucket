package com.study.spring.data.mybatis.service;


import com.study.spring.data.mybatis.entity.UserMaster;
import com.study.spring.data.mybatis.mapper.UserMasterMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
