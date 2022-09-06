package self.study.cloud.eureka.service.user.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import self.study.spring.cloud.common.dto.resp.ResponseResult;
import self.study.spring.cloud.common.edm.ResponseCode;
import self.study.spring.cloud.common.dto.resp.UserDTO;
import self.study.spring.cloud.common.model.UserMaster;
import self.study.spring.cloud.common.repository.UserMasterMapper;
import self.study.spring.cloud.common.service.UserMasterService;

import java.util.Random;

@Service("userService")
public class UserServiceImpl implements UserMasterService {

    private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserMasterMapper userMasterMapper;

    @Override
    public ResponseResult<UserDTO> addUser(UserMaster userMaster) {
        int random = new Random().nextInt(100);
        long userId = System.currentTimeMillis()+ random;
        userMaster.setUserId(userId);
        try {
            userMasterMapper.addUserMaster(userMaster);
            return ResponseResult.commonResult(ResponseCode.OK, new UserDTO().fromModel(userMaster));
        }catch (Exception e){
            logger.error("add user master error ,",e);
            return ResponseResult.commonResult(ResponseCode.DB_ERROR, e);
        }
    }

    @Override
    public ResponseResult<UserDTO> getUser(Long userId) {
        try{
            UserMaster userMaster = userMasterMapper.getUserMasterById(userId);
            return ResponseResult.commonResult(ResponseCode.OK, new UserDTO().fromModel(userMaster));
        }catch (Exception e){
            logger.error("get user master error ,",e);
            return ResponseResult.commonResult(ResponseCode.DB_ERROR, e);
        }
    }
}
