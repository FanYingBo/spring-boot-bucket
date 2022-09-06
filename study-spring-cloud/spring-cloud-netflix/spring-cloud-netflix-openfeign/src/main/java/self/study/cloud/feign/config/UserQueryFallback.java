package self.study.cloud.feign.config;

import org.springframework.stereotype.Service;
import self.study.cloud.feign.client.UserClient;
import self.study.spring.cloud.common.dto.resp.ResponseResult;
import self.study.spring.cloud.common.edm.ResponseCode;
import self.study.spring.cloud.common.dto.resp.UserDTO;

@Service
public class UserQueryFallback implements UserClient {
    @Override
    public ResponseResult<UserDTO> getUserByUserId(Long userId) {
        return ResponseResult.commonResult(ResponseCode.GATEWAY_ERROR, "Query User error");
    }
}
