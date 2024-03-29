## Hystrix
* 线程隔离模式
* 信号量模式
### 功能
* 对网络延迟及故障进行容错
* 阻断分布式系统雪崩
* 快速失败并平缓恢复
* 服务降级
* 实时监控、警报
### 方案
* 隔离
* 降级
* 熔断
* 合并（缓存）
### 配置类
HystrixCommand 通过切面获取需要被断路隔离的方法  
`com.netflix.hystrix.contrib.javanica.aop.aspectj.HystrixCommandAspect`  
参数管理：
`com.netflix.hystrix.contrib.javanica.conf.HystrixPropertiesManager` 参数名管理
`com.netflix.hystrix.HystrixThreadPoolProperties` 线程隔离参数管理
`com.netflix.hystrix.HystrixCommandProperties` 限流断路参数管理
`com.netflix.hystrix.HystrixTimerThreadPoolProperties` 定时线程池参数管理  
`com.netflix.hystrix.HystrixCollapserProperties` 请求合并参数管理
### 案例
``````java
@Service
public class UserService implements UserMasterService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserMasterMapper userMasterMapper;

    @Override
    @HystrixCommand(fallbackMethod = "addUserTimeOutFailBack",
            commandProperties = {
                    @HystrixProperty(name="execution.isolation.strategy",value = "THREAD"), // 线程隔离模式 
                    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds",value = "3000"),
            })
    public ResponseResult<UserDTO> addUser(UserMaster userMaster) {
        try {
            logger.info("addUser timeout example, thread: {}", Thread.currentThread().getName());
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    @HystrixCommand(fallbackMethod = "userGetFailBack",
            commandProperties = {
                    @HystrixProperty(name = "circuitBreaker.enabled",value = "true"),
                    @HystrixProperty(name="execution.isolation.strategy",value = "SEMAPHORE"), // 信号量模式
                    @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold",value = "10"),
                    @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "10000"),
                    @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage",value = "60"),
            })
    public ResponseResult<UserDTO> getUser(Long userId) {
        logger.info("getUser example, thread: {}", Thread.currentThread().getName());
        if(userId < 0){
            throw new RuntimeException("userId should be greater than zero");
        }
        UserMaster userMaster = userMasterMapper.getUserMasterById(userId);
        return ResponseResult.commonResult(ResponseCode.OK, new UserDTO().fromModel(userMaster));
    }

    public ResponseResult<UserDTO> userGetFailBack(Long userId) {
        logger.info("userGetFailBack, thread: {}", Thread.currentThread().getName());
        return ResponseResult.commonResult(ResponseCode.REQUEST_PARAM_ERROR,
                MessageFormat.format("userId {0} should be greater than zero", userId));
    }

    public ResponseResult<UserDTO> addUserTimeOutFailBack(UserMaster userMaster) {
        logger.info("addUserTimeOutFailBack, thread: {}", Thread.currentThread().getName());
        return ResponseResult.commonResult(ResponseCode.REQUEST_TIME_OUT, "Add user time out ");
    }
``````

## 问题
Hystrix 通过 线程池隔离 的方式，来对依赖（在 Sentinel 的概念中对应 资源）进行了隔离。这样做的好处是资源和资源之间做到了最彻底的隔离。缺点是除了增加了线程切换的成本（过多的线程池导致线程数目过多），还需要预先给各个资源做线程池大小的分配，并且对于一些使用了 ThreadLocal 的场景来说会有问题（如 Spring 事务）。