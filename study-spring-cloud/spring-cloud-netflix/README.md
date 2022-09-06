#Spring Cloud Zuul
Spring Cloud Zuul 是 Spring Cloud Netflix 子项目的核心组件之一，可以作为微服务架构中的 API 网关使用，支持动态路由与过滤功能
API 网关为微服务架构中的服务提供了统一的访问入口，客户端通过 API 网关访问相关服务。
API 网关的定义类似于设计模式中的门面模式，它相当于整个微服务架构中的门面，所有客户端的访问都通过它来进行路由及过滤。
它实现了请求路由、负载均衡、校验过滤、服务容错、服务聚合等功能。
## 微服务网关
Zuul实质是API Gateway， 微服务网关的作用：
* 统一入口：未全部为服务提供一个唯一的入口，网关起到外部和内部隔离的作用，保障了后台服务的安全性。
* 鉴权校验：识别每个请求的权限，拒绝不符合要求的请求。
* 动态路由：动态的将请求路由到不同的后端集群中。
* 减少客户端与服务端的耦合：服务可以独立发展，通过网关层来做映射。
## 工作原理
* zuul的核心是一系列的filters, 其作用可以类比Servlet框架的Filter，或者AOP。
* zuul把Request route到 用户处理逻辑 的过程中，这些filter参与一些过滤处理，比如Authentication，Load Shedding等。
* Zuul提供了一个框架，可以对过滤器进行动态的加载，编译，运行。
* Zuul的过滤器之间没有直接的相互通信，他们之间通过一个RequestContext的静态类来进行数据传递的。RequestContext类中有ThreadLocal变量来记录每个Request所需要传递的数据。
* Zuul的过滤器是由Groovy写成，这些过滤器文件被放在Zuul Server上的特定目录下面，Zuul会定期轮询这些目录，修改过的过滤器会动态的加载到Zuul Server中以便过滤请求使用。
## 下面有几种标准的过滤器类型
Zuul大部分功能都是通过过滤器来实现的。Zuul中定义了四种标准过滤器类型，这些过滤器类型对应于请求的典型生命周期
* PRE：这种过滤器在请求被路由之前调用。我们可利用这种过滤器实现身份验证、在集群中选择请求的微服务、记录调试信息等。
* ROUTING：这种过滤器用于构建发送给微服务的请求，并使用Apache HttpClient或Netfilx Ribbon请求微服务。
* POST：这种过滤器在路由到微服务以后执行。这种过滤器可用来为响应添加标准的HTTP Header、收集统计信息和指标、将响应从微服务发送给客户端等。
* ERROR：在其他阶段发生错误时执行该过滤器。
## 内置的特殊过滤器
zuul还提供了一类特殊的过滤器，分别为：StaticResponseFilter和SurgicalDebugFilter

StaticResponseFilter：StaticResponseFilter允许从Zuul本身生成响应，而不是将请求转发到源。
SurgicalDebugFilter：SurgicalDebugFilter允许将特定请求路由到分隔的调试集群或主机。
## 自定义的过滤器
可以定制一种STATIC类型的过滤器，直接在Zuul中生成响应，而不将请求转发到后端的微服务。

## ZuulServlet
``````java
public class ZuulServlet extends HttpServlet {

    private static final long serialVersionUID = -3374242278843351500L;
    private ZuulRunner zuulRunner;


    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        String bufferReqsStr = config.getInitParameter("buffer-requests");
        boolean bufferReqs = bufferReqsStr != null && bufferReqsStr.equals("true") ? true : false;

        zuulRunner = new ZuulRunner(bufferReqs);
    }

    public void service(javax.servlet.ServletRequest servletRequest, javax.servlet.ServletResponse servletResponse) throws ServletException, IOException {
        try {
            init((HttpServletRequest) servletRequest, (HttpServletResponse) servletResponse);

            // Marks this request as having passed through the "Zuul engine", as opposed to servlets
            // explicitly bound in web.xml, for which requests will not have the same data attached
            RequestContext context = RequestContext.getCurrentContext();
            context.setZuulEngineRan();

            try {
                preRoute();
            } catch (ZuulException e) {
                error(e);
                postRoute();
                return;
            }
            try {
                route();
            } catch (ZuulException e) {
                error(e);
                postRoute();
                return;
            }
            try {
                postRoute();
            } catch (ZuulException e) {
                error(e);
                return;
            }

        } catch (Throwable e) {
            error(new ZuulException(e, 500, "UNHANDLED_EXCEPTION_" + e.getClass().getName()));
        } finally {
            RequestContext.getCurrentContext().unset();
        }
    }

    /**
     * executes "post" ZuulFilters
     *
     * @throws ZuulException
     */
    void postRoute() throws ZuulException {
        zuulRunner.postRoute();
    }

    /**
     * executes "route" filters
     *
     * @throws ZuulException
     */
    void route() throws ZuulException {
        zuulRunner.route();
    }

    /**
     * executes "pre" filters
     *
     * @throws ZuulException
     */
    void preRoute() throws ZuulException {
        zuulRunner.preRoute();
    }

    /**
     * initializes request
     *
     * @param servletRequest
     * @param servletResponse
     */
    void init(HttpServletRequest servletRequest, HttpServletResponse servletResponse) {
        zuulRunner.init(servletRequest, servletResponse);
    }
    //...........//
}
``````

## Zuul 能做什么
Zuul可以通过加载动态过滤机制，从而实现以下各项功能：

1. 验证与安全保障: 识别面向各类资源的验证要求并拒绝那些与要求不符的请求。
2. 审查与监控: 在边缘位置追踪有意义数据及统计结果，从而为我们带来准确的生产状态结论。
3. 动态路由: 以动态方式根据需要将请求路由至不同后端集群处。
4. 压力测试: 逐渐增加指向集群的负载流量，从而计算性能水平。
5. 负载分配: 为每一种负载类型分配对应容量，并弃用超出限定值的请求。
6. 静态响应处理: 在边缘位置直接建立部分响应，从而避免其流入内部集群。
7. 多区域弹性: 跨越AWS区域进行请求路由，旨在实现ELB使用多样化并保证边缘位置与使用者尽可能接近。
8. 除此之外，Netflix公司还利用Zuul的功能通过金丝雀版本实现精确路由与压力测试。

## 处理请求 
调度不同阶段的filters，处理异常等， ZuulServlet类似SpringMvc的DispatcherServlet，所有的Request都要经过ZuulServlet的处理
三个核心的方法preRoute(),route(), postRoute()，zuul对request处理逻辑都在这三个方法里
ZuulServlet交给ZuulRunner去执行。由于ZuulServlet是单例，因此ZuulRunner也仅有一个实例。
ZuulRunner直接将执行逻辑交由FilterProcessor处理，FilterProcessor也是单例，其功能就是依据filterType执行filter的处理逻辑
FilterProcessor对filter的处理逻辑。
1. 首先根据Type获取所有输入该Type的filter，Listlist。

2. 遍历该list，执行每个filter的处理逻辑，processZuulFilter(ZuulFilter filter)

3. RequestContext对每个filter的执行状况进行记录，应该留意，此处的执行状态主要包括其执行时间、以及执行成功或者失败，如果执行失败则对异常封装后抛出。

4. 到目前为止，zuul框架对每个filter的执行结果都没有太多的处理，它没有把上一filter的执行结果交由下一个将要执行的filter，仅仅是记录执行状态，如果执行失败抛出异常并终止执行。