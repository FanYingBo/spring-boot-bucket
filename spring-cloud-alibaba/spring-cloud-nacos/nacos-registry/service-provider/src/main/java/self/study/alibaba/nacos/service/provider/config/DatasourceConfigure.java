package self.study.alibaba.nacos.service.provider.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DatasourceConfigure {

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")  // 数据源1的配置前缀  数据源2这里 spring.datasource.database
    @RefreshScope // nacos 客户端更新配置后刷新
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }


}
