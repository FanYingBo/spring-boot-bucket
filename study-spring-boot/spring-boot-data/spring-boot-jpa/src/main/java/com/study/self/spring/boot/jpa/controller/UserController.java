package com.study.self.spring.boot.jpa.controller;

import com.study.self.spring.boot.jpa.dto.User;
import com.study.self.spring.boot.jpa.entity.UserEntity;
import com.study.self.spring.boot.jpa.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class UserController {

    private UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @RequestMapping(value = "/user/get-all",method = RequestMethod.GET)
    @ResponseBody
    public List<UserEntity> getAllUsers(){
        return userRepository.findAll();
    }

    @RequestMapping(value = "/user/save", method = RequestMethod.POST)
    @ResponseBody
    public UserEntity save(@RequestBody User user){
        UserEntity userEntity = UserEntity.parseFrom(user);
        userRepository.save(userEntity);
        return userEntity;
    }
}
