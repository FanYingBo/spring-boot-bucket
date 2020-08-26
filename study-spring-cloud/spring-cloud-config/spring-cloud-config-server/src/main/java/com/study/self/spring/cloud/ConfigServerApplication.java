package com.study.self.spring.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.environment.Environment;
import org.springframework.cloud.config.environment.PropertySource;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.cloud.config.server.environment.EnvironmentRepository;
import org.springframework.context.annotation.Bean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootApplication
@EnableConfigServer
public class ConfigServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConfigServerApplication.class);
    }

    /**
     * 若配置了该EnvironmentRepository 则会覆盖所有的environmentRepository
     * @return
     */
    public EnvironmentRepository environmentRepository(){
        return (application,profile,label)->{
            Environment environment = new Environment("default",profile);
            Map<String, Object> dataMap = new HashMap<>();
            dataMap.put("name","jack");
            PropertySource propertySource = new PropertySource("test", dataMap);
            List<PropertySource> propertySources = environment.getPropertySources();
            propertySources.add(propertySource);
            return environment;
        };
    }
}
