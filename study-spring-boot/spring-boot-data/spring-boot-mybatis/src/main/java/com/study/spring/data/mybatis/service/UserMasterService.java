package com.study.spring.data.mybatis.service;

import com.study.spring.data.mybatis.entity.UserMaster;

public interface UserMasterService {

    UserMaster addUser(UserMaster userMaster);

    UserMaster getUser(Long userId);
}
