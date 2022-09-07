package self.study.spring.cloud.zuul.config;

import com.netflix.zuul.ZuulFilter;
import org.springframework.cloud.netflix.zuul.filters.RouteLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.util.UrlPathHelper;
import self.study.spring.cloud.zuul.filter.ZuulPostRouteFilter;
import self.study.spring.cloud.zuul.filter.ZuulRateLimitFilter;

@Configuration
public class ZuulFilterConfiguration {

    @Bean
    public ZuulFilter ZuulPostRouteFilter(RouteLocator routeLocator){
        return new ZuulPostRouteFilter(routeLocator,new UrlPathHelper());
    }

    /**
     * 限流filter
     * @return
     */
    @Bean
    public ZuulFilter ZuulRateLimitFilter(){
        return new ZuulRateLimitFilter();
    }
}
