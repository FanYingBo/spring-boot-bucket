package self.study.cloud.eureka.service.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import self.study.spring.cloud.common.dto.resp.ResponseResult;
import self.study.spring.cloud.common.dto.resp.UserDTO;
import self.study.spring.cloud.common.service.UserMasterService;

@RestController
public class UserMasterController {

    @Autowired
    private UserMasterService userService;

    @GetMapping("/user/get/v1")
    public ResponseResult<UserDTO> getUser(Long userId){
        return userService.getUser(userId);
    }
}
