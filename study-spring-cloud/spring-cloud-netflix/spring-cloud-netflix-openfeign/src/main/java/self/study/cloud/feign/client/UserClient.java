package self.study.cloud.feign.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import self.study.cloud.feign.config.UserQueryFallback;
import self.study.spring.cloud.common.dto.resp.ResponseResult;
import self.study.spring.cloud.common.dto.resp.UserDTO;

@FeignClient(value = "user-service", path = "/user", fallback = UserQueryFallback.class)
public interface UserClient {

    @GetMapping("/get/v1")
    ResponseResult<UserDTO> getUserByUserId(@RequestParam("userId") Long userId);
}
