package self.study.cloud.hystrix;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;

@SpringBootApplication
@EnableEurekaClient
@EnableHystrix
@MapperScan("self.study.spring.cloud.common.repository")
public class HystrixApplication {

    public static void main(String[] args) {
        SpringApplication.run(HystrixApplication.class);
    }
}
