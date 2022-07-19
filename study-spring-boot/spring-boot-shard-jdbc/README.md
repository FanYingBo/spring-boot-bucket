# ShardingSphere
## 简介
ShardingSphere是一套开源的分布式数据库中间件解决方案组成的生态圈，
它由Sharding-JDBC、Sharding-Proxy和Sharding-Sidecar（计划中）
这3款相互独立的产品组成。 他们均提供标准化的数据分片、分布式事务和
数据库治理功能 \
[官方文档](https://shardingsphere.apache.org/document/legacy/3.x/document/cn/features/sharding/concept/sharding/)

## 分片 
SpringBoot + MyBatis + Sharging-JDBC + MySQL 实现分库分表 \
案例 ： sharging-jdbc-slicing 
* 依赖:
```
<dependency>
    <groupId>io.shardingsphere</groupId>
    <artifactId>sharding-jdbc-spring-boot-starter</artifactId>
    <version>${spring.boot.sharding.jdbc.ver}</version>
</dependency>
<dependency>
    <groupId>io.shardingsphere</groupId>
    <artifactId>sharding-jdbc-spring-namespace</artifactId>
    <version>${spring.boot.sharding.jdbc.ver}</version>
</dependency>
```
* 配置
  SpringBootShardingRuleConfigurationProperties
```
sharding.jdbc.datasource.ds0.type=com.alibaba.druid.pool.DruidDataSource
sharding.jdbc.datasource.ds0.driver-class-name=com.mysql.cj.jdbc.Driver
sharding.jdbc.datasource.ds0.url=jdbc:mysql://192.168.0.196:6226/userdb?characterEncoding=utf8
sharding.jdbc.datasource.ds0.username=root
sharding.jdbc.datasource.ds0.password=root123

sharding.jdbc.datasource.ds1.type=com.alibaba.druid.pool.DruidDataSource
sharding.jdbc.datasource.ds1.driver-class-name=com.mysql.cj.jdbc.Driver
sharding.jdbc.datasource.ds1.url=jdbc:mysql://192.168.0.197:6226/userdb?characterEncoding=utf8
sharding.jdbc.datasource.ds1.username=root
sharding.jdbc.datasource.ds1.password=root123

# 分片的表
sharding.jdbc.config.sharding.binding-tables=user_master
sharding.jdbc.config.sharding.default-database-strategy.inline.sharding-column=user_id
sharding.jdbc.config.sharding.default-database-strategy.inline.algorithm-expression=ds$->{user_id % 2}

sharding.jdbc.config.sharding.tables.user_master.actual-data-nodes=ds$->{0..1}.user_master
sharding.jdbc.config.sharding.tables.user_master.table-strategy.inline.sharding-column=user_id
#分片表名相同的配置
sharding.jdbc.config.sharding.tables.user_master.table-strategy.inline.algorithm-expression=user_master
```
POST添加user：http://localhost:8080/user/add  
请求:
````
{
    "userName": "cindy",
    "email": "cindy@qq.com",
    "address": "shanghai",
    "age": 25,
}
````
响应：
```
{
    "userId": 756282348521127937,
    "userName": "cindy",
    "email": "cindy@qq.com",
    "address": "shanghai",
    "age": 25,
    "jobType": null
}
```