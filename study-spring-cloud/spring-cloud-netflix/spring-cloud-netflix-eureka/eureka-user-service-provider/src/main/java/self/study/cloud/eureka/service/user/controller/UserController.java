package self.study.cloud.eureka.service.user.controller;


import javafx.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import self.study.cloud.eureka.service.user.repository.UserRepository;

@RestController
public class UserController {

    private Logger logger = LoggerFactory.getLogger(UserController.class);

    @Value("${server.port}")
    private String serverPort;
    @Autowired
    private UserRepository userRepository;
    @PostMapping("/user/save")
    public Pair<Long, String>  save(String name){
        return userRepository.save(name);
    }


    @GetMapping("/user/get")
    public String  getUser(Long id){
        logger.info("user get from server: "+ serverPort);
        return userRepository.get(id);
    }
}
