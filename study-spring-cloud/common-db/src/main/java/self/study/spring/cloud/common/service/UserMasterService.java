package self.study.spring.cloud.common.service;


import self.study.spring.cloud.common.dto.resp.ResponseResult;
import self.study.spring.cloud.common.dto.resp.UserDTO;
import self.study.spring.cloud.common.model.UserMaster;

public interface UserMasterService {

    ResponseResult<UserDTO> addUser(UserMaster userMaster);

    ResponseResult<UserDTO> getUser(Long userId);
}
