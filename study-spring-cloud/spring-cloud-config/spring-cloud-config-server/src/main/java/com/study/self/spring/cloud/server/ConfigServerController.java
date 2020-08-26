package com.study.self.spring.cloud.server;

import org.springframework.cloud.config.server.environment.MultipleJGitEnvironmentRepository;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.net.URI;

@RestController
public class ConfigServerController {

    private MultipleJGitEnvironmentRepository gitEnvironmentRepository;

    public ConfigServerController(MultipleJGitEnvironmentRepository gitEnvironmentRepository) {
        this.gitEnvironmentRepository = gitEnvironmentRepository;
    }


    @GetMapping("/config/git/get")
    public String getConfig(@RequestParam String configKey){
        if(StringUtils.hasText(configKey)){
            ConfigurableEnvironment environment = gitEnvironmentRepository.getEnvironment();
            return environment.getProperty(configKey);
        }
        return "";
    }

    public static void main(String[] args) {
        File file = new File("D:\\springcloudgit");
        URI uri = file.toURI();
        System.out.println(uri.toString());
    }
}
