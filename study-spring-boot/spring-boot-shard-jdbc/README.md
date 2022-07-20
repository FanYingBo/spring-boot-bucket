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
## 读写分离
配置：
````
sharding.jdbc.datasource.names=master,slave1,slave2

sharding.jdbc.datasource.master.type=com.alibaba.druid.pool.DruidDataSource
sharding.jdbc.datasource.master.driver-class-name=com.mysql.cj.jdbc.Driver
sharding.jdbc.datasource.master.url=jdbc:mysql://192.168.0.198:3306/userdb?characterEncoding=utf8
sharding.jdbc.datasource.master.username=root
sharding.jdbc.datasource.master.password=root123

sharding.jdbc.datasource.slave1.type=com.alibaba.druid.pool.DruidDataSource
sharding.jdbc.datasource.slave1.driver-class-name=com.mysql.cj.jdbc.Driver
sharding.jdbc.datasource.slave1.url=jdbc:mysql://192.168.0.186:3306/userdb?characterEncoding=utf8
sharding.jdbc.datasource.slave1.username=root
sharding.jdbc.datasource.slave1.password=root123

sharding.jdbc.datasource.slave2.type=com.alibaba.druid.pool.DruidDataSource
sharding.jdbc.datasource.slave2.driver-class-name=com.mysql.cj.jdbc.Driver
sharding.jdbc.datasource.slave2.url=jdbc:mysql://192.168.0.189:3306/userdb?characterEncoding=utf8
sharding.jdbc.datasource.slave2.username=root
sharding.jdbc.datasource.slave2.password=root123

sharding.jdbc.config.masterslave.name=ms
sharding.jdbc.config.masterslave.master-data-source-name=master
sharding.jdbc.config.masterslave.slave-data-source-names=slave2,slave1
````
查询日志：
``````
2022-07-20 15:38:07.078  INFO 1244 --- [nio-8080-exec-9] ShardingSphere-SQL                       : Rule Type: master-slave
2022-07-20 15:38:07.078  INFO 1244 --- [nio-8080-exec-9] ShardingSphere-SQL                       : SQL: SELECT * FROM user_master where user_id = ? ::: DataSources: slave2
2022-07-20 15:38:08.977  INFO 1244 --- [io-8080-exec-10] ShardingSphere-SQL                       : Rule Type: master-slave
2022-07-20 15:38:08.977  INFO 1244 --- [io-8080-exec-10] ShardingSphere-SQL                       : SQL: SELECT * FROM user_master where user_id = ? ::: DataSources: slave1
``````
插入日志：
````
2022-07-20 15:36:20.620  INFO 1244 --- [nio-8080-exec-4] ShardingSphere-SQL                       : Rule Type: master-slave
2022-07-20 15:36:20.621  INFO 1244 --- [nio-8080-exec-4] ShardingSphere-SQL                       : SQL: insert into user_master (user_id, user_name, email, address, age, job_type)
        values
        (?, ?, ?, ?, ?, ?) ::: DataSources: master
````