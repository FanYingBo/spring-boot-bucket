package self.study.cloud.eureka.service.user.repository;

import javafx.util.Pair;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class UserRepository {

    private Map<Long, String> userMap = new ConcurrentHashMap<>();

    public Pair<Long, String> save(String name){
        Long id = System.currentTimeMillis();
        String s = userMap.putIfAbsent(id, name);
        return new Pair<>(id, name);
    }

    public String get(Long id){
        String s = userMap.get(id);
        return s;
    }

}
