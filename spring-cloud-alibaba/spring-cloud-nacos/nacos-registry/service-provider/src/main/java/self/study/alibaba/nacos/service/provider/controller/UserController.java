package self.study.alibaba.nacos.service.provider.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import self.study.alibaba.nacos.service.provider.entity.UserMaster;
import self.study.alibaba.nacos.service.provider.service.UserMasterService;

@RestController("/")
public class UserController {

    @Autowired
    private UserMasterService userMasterService;

    @PostMapping("/user/add")
    public UserMaster addUser(@RequestBody UserMaster userMaster){
        return userMasterService.addUser(userMaster);
    }


    @GetMapping("/user/get")
    public UserMaster getUser(Long userId){
        return userMasterService.getUser(userId);
    }
}
