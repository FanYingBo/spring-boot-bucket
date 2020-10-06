package com.study.self.spring.boot.jpa.service;

import com.study.self.spring.boot.jpa.dto.User;
import com.study.self.spring.boot.jpa.entity.UserEntity;

import java.util.List;

public interface IUserService {

    void saveUser(User user);

    List<User> getAllUser();

    User getUserByUsername(String username);
}
