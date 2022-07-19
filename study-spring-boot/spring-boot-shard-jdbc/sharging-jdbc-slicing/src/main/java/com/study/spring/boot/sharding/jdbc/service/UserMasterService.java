package com.study.spring.boot.sharding.jdbc.service;

import com.study.spring.boot.sharding.jdbc.entity.UserMaster;

public interface UserMasterService {

    UserMaster addUser(UserMaster userMaster);

    UserMaster getUser(Long userId);
}
