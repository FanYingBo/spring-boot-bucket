package self.study.spring.boot.data.redis.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@ConfigurationProperties(prefix = RedisConfigProperties.DEFAULT_PREFIX)
@Configuration
public class RedisConfigProperties {

    public final static String DEFAULT_PREFIX = "spring.redis";

    private int database;
    private List<String> nodeAddress;

    public int getDatabase() {
        return database;
    }

    public void setDatabase(int database) {
        this.database = database;
    }

    public List<String> getNodeAddress() {
        return nodeAddress;
    }

    public void setNodeAddress(List<String> nodeAddress) {
        this.nodeAddress = nodeAddress;
    }
}
