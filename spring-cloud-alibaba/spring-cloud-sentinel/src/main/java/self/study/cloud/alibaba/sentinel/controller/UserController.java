package self.study.cloud.alibaba.sentinel.controller;


import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import self.study.spring.cloud.common.dto.resp.ResponseResult;
import self.study.spring.cloud.common.dto.resp.UserDTO;
import self.study.spring.cloud.common.edm.ResponseCode;
import self.study.spring.cloud.common.service.UserMasterService;

@RestController
public class UserController {

    private Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserMasterService userService;

    @GetMapping("/user/get")
    @SentinelResource(value = "/getUser", fallback = "quickFallback", blockHandler = "blockHandler")
    public ResponseResult<UserDTO> getUser(Long userId){
        return userService.getUserByUserId(userId);
    }

    public ResponseResult<UserDTO> quickFallback(Long userId){
        logger.warn("quickFallback , userId: userId {} ", userId);
        return ResponseCode.DB_ERROR.responseResult("Quick Fallback",null);
    }
    public ResponseResult<UserDTO> blockHandler(Long userId, BlockException blockException){
        logger.warn("blockHandler current-limiting, userId: userId {} ", userId);
        return ResponseCode.TOO_MANY_REQUEST.responseResult(null);
    }
}
