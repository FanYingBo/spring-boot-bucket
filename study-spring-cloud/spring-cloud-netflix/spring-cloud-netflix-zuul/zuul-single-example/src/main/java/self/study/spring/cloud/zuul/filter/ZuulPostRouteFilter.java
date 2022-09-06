package self.study.spring.cloud.zuul.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.zuul.filters.Route;
import org.springframework.cloud.netflix.zuul.filters.RouteLocator;
import org.springframework.web.util.UrlPathHelper;

/**
 * filter of route
 */
public class ZuulPostRouteFilter extends ZuulFilter {

    private static Logger logger = LoggerFactory.getLogger(ZuulPostRouteFilter.class);

    private RouteLocator routeLocator;
    private UrlPathHelper urlPathHelper;

    public ZuulPostRouteFilter(RouteLocator routeLocator, UrlPathHelper urlPathHelper) {
        this.routeLocator = routeLocator;
        this.urlPathHelper = urlPathHelper;
    }

    @Override
    public String filterType() {
        return "post";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext ctx = RequestContext.getCurrentContext();
        String requestURI = urlPathHelper.getPathWithinApplication(ctx.getRequest());
        Route matchingRoute = routeLocator.getMatchingRoute(requestURI);
        logger.info("Route: location {}", matchingRoute.getLocation());
        return null;
    }
}
