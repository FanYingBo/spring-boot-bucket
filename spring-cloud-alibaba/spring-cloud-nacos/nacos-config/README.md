# Spring-cloud-nacos 配置中心

## 配置文件
沿用Spring Cloud 配置文件bootstrap.properties \
Nacos 通过 Group + DataId 定位配置信息， dataId 由 ${spring.application.name}.${file-extension} 组成
````
#应用名称
spring.application.name=nacos-config-example
#nacos 配置中心地址
spring.cloud.nacos.config.server-addr=192.168.0.196:8848
spring.cloud.nacos.config.enabled=true

spring.cloud.nacos.username=nacos
spring.cloud.nacos.password=nacos
#当前应用的配置类型
spring.cloud.nacos.config.file-extension=properties

spring.cloud.nacos.config.refresh-enabled=true
#引用其他配置
spring.cloud.nacos.config.shared-configs[0].data-id=config.yaml
spring.cloud.nacos.config.shared-configs[0].refresh=true
````
## 获取和更新配置
* Spring 注解 @Value  获取配置
* Spring Boot 注解 @ConfigurationProperties 获取配置
* @RefreshScope 动态刷新配置
