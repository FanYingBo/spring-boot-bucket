package com.study.spring.boot.sharding.jdbc.mapper;

import com.study.spring.boot.sharding.jdbc.entity.UserMaster;

public interface UserMasterMapper {

    UserMaster getUserMasterById(long userId);

    int addUserMaster(UserMaster userMaster);
}
