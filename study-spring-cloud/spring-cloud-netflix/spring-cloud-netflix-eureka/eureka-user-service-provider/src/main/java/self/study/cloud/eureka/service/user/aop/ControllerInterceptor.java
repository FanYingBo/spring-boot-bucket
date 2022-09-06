package self.study.cloud.eureka.service.user.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Aspect
@Component
public class ControllerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(ControllerInterceptor.class);

    @Value("${server.port}")
    private String serverPort;

    @Pointcut("execution(* self.study.cloud.eureka.service.user.controller.*.*(..))")
    public void controllerPointcut(){}

    @Before("controllerPointcut()")
    public void before(JoinPoint joinPoint){
//        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        logger.info("serverPort: {} method: {}", serverPort, joinPoint.getSignature().getName());
    }
}
