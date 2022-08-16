package self.study.alibaba.nacos.service.provider.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CheckController {

    @Value(value = "${check.config.enable}")
    private boolean checkConfig;

    @GetMapping("/check")
    public boolean getCheckConfig(){
        return checkConfig;
    }
}
