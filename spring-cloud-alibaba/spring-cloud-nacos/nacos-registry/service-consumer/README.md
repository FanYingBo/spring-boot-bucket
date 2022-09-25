Nacos 中的配置：
```properties
user.service.provider=service-provider
# 配置Ribbon的Rule实现 以 Nacos 配置为主 {serviceProviderName}.ribbon.NFLoadBalancerRuleClassName
service-provider.ribbon.NFLoadBalancerRuleClassName=com.alibaba.cloud.nacos.ribbon.NacosRule
```
