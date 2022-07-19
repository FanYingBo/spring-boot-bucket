package com.study.spring.boot.sharding.jdbc.route;

import com.study.spring.boot.sharding.jdbc.entity.UserMaster;
import com.study.spring.boot.sharding.jdbc.service.UserMasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/")
public class UserController {

    @Autowired
    private UserMasterService userMasterService;

    @PostMapping("/user/add")
    public UserMaster addUser(@RequestBody UserMaster userMaster){
        return userMasterService.addUser(userMaster);
    }


    @GetMapping("/user/get")
    public UserMaster addUser(Long userId){
        return userMasterService.getUser(userId);
    }
}
