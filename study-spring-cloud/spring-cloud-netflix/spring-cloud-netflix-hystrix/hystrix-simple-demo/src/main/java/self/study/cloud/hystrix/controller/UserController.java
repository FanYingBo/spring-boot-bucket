package self.study.cloud.hystrix.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import self.study.spring.cloud.common.dto.resp.ResponseResult;
import self.study.spring.cloud.common.dto.resp.UserDTO;
import self.study.spring.cloud.common.model.UserMaster;
import self.study.spring.cloud.common.service.UserMasterService;

@RestController
public class UserController {

    @Autowired
    private UserMasterService userMasterService;

    @GetMapping("/user/get/v1")
    public ResponseResult<UserDTO> getUser(Long userId){
        return userMasterService.getUser(userId);
    }

    @GetMapping("/user/add")
    public ResponseResult<UserDTO> addUserTimeOut(UserDTO userDTO){
        return userMasterService.addUser(new UserMaster().fromDTO(userDTO));
    }
}
