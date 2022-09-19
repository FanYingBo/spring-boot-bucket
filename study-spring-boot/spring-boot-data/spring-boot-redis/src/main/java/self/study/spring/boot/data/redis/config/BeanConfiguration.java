package self.study.spring.boot.data.redis.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    @Bean
    public RedissonClient redissonClient(RedisConfigProperties redisConfigProperties){
        Config config = new Config();
        config.useSingleServer().setAddress(redisConfigProperties.getNodeAddress().get(0));
        return Redisson.create(config);
    }
}
