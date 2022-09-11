package self.study.cloud.alibaba.sentinel.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import self.study.spring.cloud.common.dto.resp.ResponseResult;
import self.study.spring.cloud.common.dto.resp.UserDTO;
import self.study.spring.cloud.common.edm.ResponseCode;
import self.study.spring.cloud.common.model.UserMaster;
import self.study.spring.cloud.common.repository.UserMasterMapper;
import self.study.spring.cloud.common.service.UserMasterService;

import java.util.List;
import java.util.Random;

@Service
public class UserService implements UserMasterService {

    @Autowired
    private UserMasterMapper userMasterMapper;
    @Override
    public ResponseResult<UserDTO> addUser(UserMaster userMaster) {
        return null;
    }

    @Override
    public ResponseResult<UserDTO> getUser(Long userId) {
        try{
            UserMaster userMaster = userMasterMapper.getUserMasterById(userId);
            return ResponseCode.OK.responseResult(new UserDTO().fromModel(userMaster));
        }catch (Exception e){
            return ResponseCode.DB_ERROR.responseResult(e.getMessage(), null);
        }
    }

    @Override
    public ResponseResult<List<UserDTO>> getUserByUserIds(List<Long> userIds) {
        return null;
    }

    @Override
    public ResponseResult<UserDTO> getUserByUserId(Long userId) {
            int nextInt = new Random().nextInt(10);
            if(nextInt < 3){
               throw new RuntimeException("execution exception");
            }
            UserMaster userMaster = userMasterMapper.getUserMasterById(userId);
            return ResponseCode.OK.responseResult(new UserDTO().fromModel(userMaster));
    }
}
