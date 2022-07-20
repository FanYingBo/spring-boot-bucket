package com.study.spring.boot.sharding.jdbc.bean;

import io.shardingsphere.core.keygen.DefaultKeyGenerator;
import io.shardingsphere.core.keygen.KeyGenerator;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



@Configuration
@MapperScan(value = "com.study.spring.boot.sharding.jdbc.mapper")
public class BeanConfiguration {
    /**
     * 自定义分片key生成器
     * @return
     */
    @Bean
    public KeyGenerator keyGenerator(){
        return new DefaultKeyGenerator();
    }
}
