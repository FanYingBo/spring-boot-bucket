package com.study.spring.boot.sharding.jdbc.bean;

import io.shardingsphere.core.keygen.DefaultKeyGenerator;
import io.shardingsphere.core.keygen.KeyGenerator;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



@Configuration
@MapperScan(value = "com.study.spring.boot.sharding.jdbc.mapper")
public class BeanConfiguration {

    @Bean
    public KeyGenerator keyGenerator(){
        return new DefaultKeyGenerator();
    }
}
