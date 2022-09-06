###Spring Cloud 服务架构示意图
![img.png](img.png)

Eureka：负责服务的注册与发现；  
Ribbon：负责客户端调用的负载均衡；  
Hystrix：负责服务之间远程调用时的容错保护；  
Feign：可以让我们通过定义接口的方式直接调用其他服务的API；  
Zuul：API网关，是客户端请求的入口，负责鉴权，路由等功能； 
Spring Cloud Config：用于统一的配置管理；   
Sleuth：用于请求的链路跟踪。 
