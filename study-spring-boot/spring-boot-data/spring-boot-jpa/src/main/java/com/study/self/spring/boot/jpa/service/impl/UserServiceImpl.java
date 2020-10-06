package com.study.self.spring.boot.jpa.service.impl;

import com.study.self.spring.boot.jpa.dto.User;
import com.study.self.spring.boot.jpa.entity.UserEntity;
import com.study.self.spring.boot.jpa.exception.DataBaseException;
import com.study.self.spring.boot.jpa.repository.UserRepository;
import com.study.self.spring.boot.jpa.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl  implements IUserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public void saveUser(User user) {
        try {
            UserEntity userEntity = UserEntity.parseFrom(user);
            userRepository.save(userEntity);
        }catch (Exception e){
            throw new DataBaseException("save user error ", e);
        }
    }

    @Override
    public List<User> getAllUser() {
        try {
            List<UserEntity> entityList = userRepository.findAll();
            List<User> users = entityList.stream()
                    .map(userEntity ->
                            User.Builder.newBuilder()
                                    .mobile(userEntity.getMobile())
                                    .username(userEntity.getUsername())
                                    .password(userEntity.getPassword())
                                    .realname(userEntity.getRealname())
                                    .build())
                    .collect(Collectors.toList());
            return users;
        }catch (Exception e){
            throw new DataBaseException("query all user error ", e);
        }
    }

    @Override
    public User getUserByUsername(String username) {
        try{
            UserEntity byUsername = userRepository.findByUsername(username);
            return User.Builder.newBuilder().realname(byUsername.getRealname()).username(byUsername.getUsername()).mobile(byUsername.getMobile()).build();
        }catch (Exception e){
            throw new DataBaseException("query all user error , where username = "+username, e);
        }
    }
}
