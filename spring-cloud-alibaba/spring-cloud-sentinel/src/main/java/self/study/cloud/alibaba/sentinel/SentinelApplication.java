package self.study.cloud.alibaba.sentinel;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("self.study.spring.cloud.common.repository")
public class SentinelApplication {

    public static void main(String[] args) {
        SpringApplication.run(SentinelApplication.class);
    }
}
