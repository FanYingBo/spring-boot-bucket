package self.study.spring.boot.data.redis.controller;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
public class TestController {

    private static final Logger logger = LoggerFactory.getLogger(TestController.class);
    private static final String prefix = "TEST_LOCK_KEY_";
    @Autowired
    private RedissonClient redissonClient;

    @GetMapping("/lock")
    public Boolean lockTest(String key){
        RLock lock = null;
        try{
            lock = redissonClient.getLock(prefix + key);
        }catch (Exception e){
            return Boolean.FALSE;
        }
        try{
            // waitTime根据业务需求而定
            boolean lock1 = lock.tryLock(10, 20, TimeUnit.SECONDS);
            if(lock1){
                logger.info("TO DO Transaction");
            }
        }catch (Exception e){

        }finally {
            lock.unlock();
        }
        return Boolean.TRUE;
    }

}
