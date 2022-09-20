package self.study.spring.boot.web.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
//@CrossOrigin  该controller所有请求支持跨域
public class HelloController {

    @GetMapping("/hello")
    // 该方法允许跨域
//    @CrossOrigin
    public String hello(String name){
        return "hello, "+name;
    }
}
