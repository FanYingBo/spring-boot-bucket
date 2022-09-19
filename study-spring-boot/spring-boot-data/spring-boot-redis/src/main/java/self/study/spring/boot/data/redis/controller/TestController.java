package self.study.spring.boot.data.redis.controller;

import org.redisson.api.*;
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

    public void transaction(String key){
        RTransaction transaction = null;
        try{
            transaction = redissonClient.createTransaction(TransactionOptions.defaults());
//        RSetCache<Object> setCache = transaction.getSetCache(key);
//        RCountDownLatch countDownLatch = setCache.getCountDownLatch(key);
//        分布式闭锁
//        countDownLatch.countDown();
//        分布式信号量
//        setCache.getSemaphore(key);
//        分布式读写锁
//        setCache.getReadWriteLock();
            RMapCache<Object, Object> mapCache = transaction.getMapCache(key);
            RBucket<Object> bucket = transaction.getBucket(key);
            bucket.getAndSet(1);
            mapCache.put("name", "tom");
            transaction.commit();
        }catch (Exception e){
            transaction.rollback();
        }

    }
}
