package self.study.spring.cloud.zuul.filter;

import com.google.common.util.concurrent.RateLimiter;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_TYPE;

public class ZuulRateLimitFilter extends ZuulFilter {

    private RateLimiter rateLimiter = RateLimiter.create(5);// 每秒超过5次
    @Override
    public String filterType() {
        return PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    /**
     * 未开启
     * @return
     */
    @Override
    public boolean shouldFilter() {
        return false;
    }

    @Override
    public Object run() throws ZuulException {
        boolean tryAcquire = rateLimiter.tryAcquire();
        if(!tryAcquire){
            RequestContext currentContext = RequestContext.getCurrentContext();
            currentContext.setSendZuulResponse(Boolean.FALSE);
            currentContext.setResponseStatusCode(HttpStatus.TOO_MANY_REQUESTS.value());
            HttpServletResponse response = currentContext.getResponse();
//        response.sendRedirect(); // 重定向
            try {
                response.setContentType("text/html;charset=UTF-8");
                response.getWriter().write("访问上限,限流啦");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
        return null;
    }
}
