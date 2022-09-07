package self.study.cloud.hystrix.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import self.study.spring.cloud.common.dto.resp.ResponseResult;
import self.study.spring.cloud.common.dto.resp.UserDTO;
import self.study.spring.cloud.common.edm.ResponseCode;
import self.study.spring.cloud.common.model.UserMaster;
import self.study.spring.cloud.common.repository.UserMasterMapper;
import self.study.spring.cloud.common.service.UserMasterService;

import java.text.MessageFormat;
import java.util.concurrent.TimeUnit;

@Service
public class UserService implements UserMasterService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserMasterMapper userMasterMapper;

    @Override
    @HystrixCommand(fallbackMethod = "addUserTimeOutFailBack",
            commandProperties = {
                    @HystrixProperty(name="execution.isolation.strategy",value = "THREAD"),
                    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds",value = "3000"),
            })
    public ResponseResult<UserDTO> addUser(UserMaster userMaster) {
        try {
            logger.info("addUser timeout example, thread: {}", Thread.currentThread().getName());
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    @HystrixCommand(fallbackMethod = "userGetFailBack",
            commandProperties = {
                    @HystrixProperty(name = "circuitBreaker.enabled",value = "true"),
                    @HystrixProperty(name="execution.isolation.strategy",value = "SEMAPHORE"),
                    @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold",value = "10"),
                    @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "10000"),
                    @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage",value = "60"),
            })
    public ResponseResult<UserDTO> getUser(Long userId) {
        logger.info("getUser example, thread: {}", Thread.currentThread().getName());
        if(userId < 0){
            throw new RuntimeException("userId should be greater than zero");
        }
        UserMaster userMaster = userMasterMapper.getUserMasterById(userId);
        return ResponseResult.commonResult(ResponseCode.OK, new UserDTO().fromModel(userMaster));
    }

    public ResponseResult<UserDTO> userGetFailBack(Long userId) {
        logger.info("userGetFailBack, thread: {}", Thread.currentThread().getName());
        return ResponseResult.commonResult(ResponseCode.REQUEST_PARAM_ERROR,
                MessageFormat.format("userId {0} should be greater than zero", userId));
    }

    public ResponseResult<UserDTO> addUserTimeOutFailBack(UserMaster userMaster) {
        logger.info("addUserTimeOutFailBack, thread: {}", Thread.currentThread().getName());
        return ResponseResult.commonResult(ResponseCode.REQUEST_TIME_OUT, "Add user time out ");
    }

}
