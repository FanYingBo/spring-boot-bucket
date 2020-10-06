package com.study.self.spring.boot.jpa.controller;

import com.study.self.spring.boot.jpa.dto.User;
import com.study.self.spring.boot.jpa.entity.UserEntity;
import com.study.self.spring.boot.jpa.repository.UserRepository;
import com.study.self.spring.boot.jpa.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class UserController {
    @Autowired
    private IUserService userService;

    @RequestMapping(value = "/user/get-all",method = RequestMethod.GET)
    @ResponseBody
    public List<User> getAllUsers(){
        return userService.getAllUser();
    }

    @RequestMapping(value = "/user/save", method = RequestMethod.POST)
    @ResponseBody
    public User save(@RequestBody User user){
        userService.saveUser(user);
        return user;
    }
    @RequestMapping(value = "/user/get/{username}",method = RequestMethod.GET)
    @ResponseBody
    public User queryByUsername(@PathVariable("username") String username){
        return userService.getUserByUsername(username);
    }
}
