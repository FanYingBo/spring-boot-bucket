package self.study.spring.cloud.zuul.config;

import com.netflix.zuul.ZuulFilter;
import org.springframework.cloud.netflix.zuul.filters.RouteLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.util.UrlPathHelper;
import self.study.spring.cloud.zuul.filter.ZuulPostRouteFilter;

@Configuration
public class ZuulFilterConfiguration {

    @Bean
    public ZuulFilter ZuulPostRouteFilter(RouteLocator routeLocator){
        return new ZuulPostRouteFilter(routeLocator,new UrlPathHelper());
    }

}
