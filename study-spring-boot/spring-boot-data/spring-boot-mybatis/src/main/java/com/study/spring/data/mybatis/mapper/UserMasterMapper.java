package com.study.spring.data.mybatis.mapper;


import com.study.spring.data.mybatis.entity.UserMaster;

public interface UserMasterMapper {

    UserMaster getUserMasterById(long userId);

    int addUserMaster(UserMaster userMaster);
}
