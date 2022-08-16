package self.study.alibaba.nacos.service.consumer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import self.study.alibaba.nacos.service.consumer.dto.UserMaster;

@RestController
@RefreshScope
public class UserController {

    @Value("http://${user.service.provider}")
    private String serviceProvider;

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/user/get")
    public UserMaster get(Long userId){
        return restTemplate.getForObject(serviceProvider+"/user/get?userId="+userId, UserMaster.class);
    }

}
