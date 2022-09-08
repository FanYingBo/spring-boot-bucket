package self.study.cloud.hystrix.controller;


import com.netflix.hystrix.contrib.javanica.annotation.HystrixCollapser;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.netflix.hystrix.contrib.javanica.conf.HystrixPropertiesManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import self.study.spring.cloud.common.dto.resp.ResponseResult;
import self.study.spring.cloud.common.dto.resp.UserDTO;
import self.study.spring.cloud.common.edm.ResponseCode;
import self.study.spring.cloud.common.model.UserMaster;
import self.study.spring.cloud.common.service.UserMasterService;

import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.stream.Collectors;

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

    @GetMapping("/user/get/v2")
    @HystrixCollapser(scope = com.netflix.hystrix.HystrixCollapser.Scope.GLOBAL,
            batchMethod = "getUserByUserIds",
            collapserProperties={
                    @HystrixProperty(name = HystrixPropertiesManager.MAX_REQUESTS_IN_BATCH , value = "3"),
                    @HystrixProperty(name = HystrixPropertiesManager.TIMER_DELAY_IN_MILLISECONDS, value = "100")
            })
    public ResponseResult<UserDTO> getUserById(Long userId){
        return userMasterService.getUserByUserId(userId);
    }

    @HystrixCommand
    public List<ResponseResult<UserDTO>> getUserByUserIds(List<Long> userId){
        return userMasterService.getUserByUserIds(userId).getData().stream().map(data-> ResponseCode.OK.responseResult(data)).collect(Collectors.toList());
    }

}
