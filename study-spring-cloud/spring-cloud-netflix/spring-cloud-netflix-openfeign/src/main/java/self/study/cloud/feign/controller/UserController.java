package self.study.cloud.feign.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import self.study.cloud.feign.client.UserClient;
import self.study.spring.cloud.common.dto.resp.ResponseResult;
import self.study.spring.cloud.common.dto.resp.UserDTO;
import self.study.spring.cloud.common.edm.ResponseCode;

@RestController
public class UserController {

    @Autowired
    private UserClient userClient;

    @GetMapping("/remote/get/user")
    public UserDTO getUser(Long userId){
        ResponseResult<UserDTO> responseResult = userClient.getUserByUserId(userId);
        if(responseResult.getCode().equals(ResponseCode.OK.getCode())){
            return responseResult.getData();
        }
        return null;
    }

}
