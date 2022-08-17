## Spring Cloud Nacos 服务注册发现
* Maven  依赖
````text
 <dependency>
        <groupId>com.alibaba.cloud</groupId>
        <artifactId>spring-cloud-starter-alibaba-nacos-config</artifactId>
</dependency>
<dependency>
        <groupId>com.alibaba.cloud</groupId>
        <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
</dependency>
````
* 基于Docker镜像构建
````text
 <plugin>
        <!-- docker-maven-plugin -->
        <!-- mvn ... docker:build -DpushImageTag -DdockerImageTags=latest,another-tag-->
        <groupId>com.spotify</groupId>
        <artifactId>docker-maven-plugin</artifactId>
        <version>1.2.0</version>
        <configuration>
            <baseImage>openjdk:8</baseImage>
            <imageName>192.168.0.196:5000/service-provider</imageName>
            <dockerDirectory>docker</dockerDirectory> <!--Dockerfile 相对路径-->
            <imageTags>
                <tag>1.2</tag>
                <tag>latest</tag>
            </imageTags>
        <buildArgs>
            <PROJECT_VERSION>${project.version}</PROJECT_VERSION>
            <PROJECT_NAME>${project.artifactId}</PROJECT_NAME>
            <!--                            <PROJECT_BASE_DIR>${project.basedir}</PROJECT_BASE_DIR>-->
        </buildArgs>
        <resources>
            <resource>
                <targetPath>/</targetPath>
                <directory>${project.build.directory}</directory>
                <include>${project.build.finalName}.jar</include>
            </resource>
        </resources>
        <dockerHost>http://192.168.0.196:2375</dockerHost>
    </configuration>
</plugin>
````
### Service Consumer
[Example](service-consumer) \
Boostrap 配置：
````
spring.application.name=service-consumer
spring.cloud.nacos.config.server-addr=192.168.0.196:8848

spring.cloud.nacos.config.enabled=true
spring.cloud.nacos.username=nacos
spring.cloud.nacos.password=nacos

spring.cloud.nacos.config.file-extension=properties
````
````java
@RestController
@RefreshScope
public class UserController {
    // nacos 配置中心的Service Provider Name
    @Value("http://${user.service.provider}")
    private String serviceProvider;

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/user/get")
    public UserMaster get(Long userId){
        return restTemplate.getForObject(serviceProvider+"/user/get?userId="+userId, UserMaster.class);
    }

}
````
````java
@Configuration
public class DatasourceConfigure {
    /**
     * RestTemplate 必须依赖 LoadBalanced 注解才能实现服务发现
     * @return
     */
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
````

### Service Provider
[Example](service-provider)
