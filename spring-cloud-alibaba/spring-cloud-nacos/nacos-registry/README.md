## Spring Cloud Nacos 服务注册发现
* Maven  依赖
````text
 <dependency>
        <groupId>com.alibaba.cloud</groupId>
        <artifactId>spring-cloud-starter-alibaba-nacos-config</artifactId>
</dependency>
<dependency>
        <groupId>com.alibaba.cloud</groupId>
        <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
</dependency>
````
* 基于Docker镜像构建
````text
 <plugin>
        <!-- docker-maven-plugin -->
        <!-- mvn ... docker:build -DpushImageTag -DdockerImageTags=latest,another-tag-->
        <groupId>com.spotify</groupId>
        <artifactId>docker-maven-plugin</artifactId>
        <version>1.2.0</version>
        <configuration>
            <baseImage>openjdk:8</baseImage>
            <imageName>192.168.0.196:5000/service-provider</imageName>
            <dockerDirectory>docker</dockerDirectory> <!--Dockerfile 相对路径-->
            <imageTags>
                <tag>1.2</tag>
                <tag>latest</tag>
            </imageTags>
        <buildArgs>
            <PROJECT_VERSION>${project.version}</PROJECT_VERSION>
            <PROJECT_NAME>${project.artifactId}</PROJECT_NAME>
            <!--                            <PROJECT_BASE_DIR>${project.basedir}</PROJECT_BASE_DIR>-->
        </buildArgs>
        <resources>
            <resource>
                <targetPath>/</targetPath>
                <directory>${project.build.directory}</directory>
                <include>${project.build.finalName}.jar</include>
            </resource>
        </resources>
        <dockerHost>http://192.168.0.196:2375</dockerHost>
    </configuration>
</plugin>
````
### Service Consumer
[Example](service-consumer) \
Boostrap 配置：
````
spring.application.name=service-consumer
spring.cloud.nacos.config.server-addr=192.168.0.196:8848

spring.cloud.nacos.config.enabled=true
spring.cloud.nacos.username=nacos
spring.cloud.nacos.password=nacos

spring.cloud.nacos.config.file-extension=properties
````
````java
@RestController
@RefreshScope
public class UserController {
    // nacos 配置中心的Service Provider Name
    @Value("http://${user.service.provider}")
    private String serviceProvider;

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/user/get")
    public UserMaster get(Long userId){
        return restTemplate.getForObject(serviceProvider+"/user/get?userId="+userId, UserMaster.class);
    }

}
````
````java
@Configuration
public class DatasourceConfigure {
    /**
     * RestTemplate 必须依赖 LoadBalanced 注解才能实现服务发现
     * @return
     */
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
````

### Service Provider
[Example](service-provider)

### LoadBalanced 原理分析
* Bean的配置 ：  
`org.springframework.cloud.client.loadbalancer.LoadBalancerAutoConfiguration`  
`org.springframework.cloud.netflix.ribbon.RibbonClientConfiguration`  
[Nacos Server中的配置](./service-consumer/README.md)
```java
public class PropertiesFactory {

  @Autowired
  private Environment environment;

  private Map<Class, String> classToProperty = new HashMap<>();
  // 通过配置化参数决定生效的是那种方式
  public PropertiesFactory() {
    classToProperty.put(ILoadBalancer.class, "NFLoadBalancerClassName");
    classToProperty.put(IPing.class, "NFLoadBalancerPingClassName");
    classToProperty.put(IRule.class, "NFLoadBalancerRuleClassName");
    classToProperty.put(ServerList.class, "NIWSServerListClassName");
    classToProperty.put(ServerListFilter.class, "NIWSServerListFilterClassName");
  }

  public boolean isSet(Class clazz, String name) {
    return StringUtils.hasText(getClassName(clazz, name));
  }

  public String getClassName(Class clazz, String name) {
    if (this.classToProperty.containsKey(clazz)) {
      String classNameProperty = this.classToProperty.get(clazz);
      String className = environment
              .getProperty(name + "." + NAMESPACE + "." + classNameProperty);
      return className;
    }
    return null;
  }

  @SuppressWarnings("unchecked")
  public <C> C get(Class<C> clazz, IClientConfig config, String name) {
    String className = getClassName(clazz, name);
    if (StringUtils.hasText(className)) {
      try {
        Class<?> toInstantiate = Class.forName(className);
        return (C) SpringClientFactory.instantiateWithConfig(toInstantiate,
                config);
      }
      catch (ClassNotFoundException e) {
        throw new IllegalArgumentException("Unknown class to load " + className
                + " for class " + clazz + " named " + name);
      }
    }
    return null;
  }
}
 
```
* RestTemplate 客户端 
  1. 初始化 `org.springframework.cloud.client.loadbalancer.LoadBalancerAutoConfiguration`
```java
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(RestTemplate.class)
@ConditionalOnBean(LoadBalancerClient.class)
@EnableConfigurationProperties(LoadBalancerRetryProperties.class)
public class LoadBalancerAutoConfiguration {

  @LoadBalanced
  @Autowired(required = false)
  private List<RestTemplate> restTemplates = Collections.emptyList();

  @Autowired(required = false)
  private List<LoadBalancerRequestTransformer> transformers = Collections.emptyList();

  @Bean
  public SmartInitializingSingleton loadBalancedRestTemplateInitializerDeprecated(
          final ObjectProvider<List<RestTemplateCustomizer>> restTemplateCustomizers) {
    return () -> restTemplateCustomizers.ifAvailable(customizers -> {
      for (RestTemplate restTemplate : LoadBalancerAutoConfiguration.this.restTemplates) {
        for (RestTemplateCustomizer customizer : customizers) {
          customizer.customize(restTemplate);
        }
      }
    });
  }
  //... ...
  @Configuration(proxyBeanMethods = false)
  @ConditionalOnMissingClass("org.springframework.retry.support.RetryTemplate")
  static class LoadBalancerInterceptorConfig {

    @Bean
    public LoadBalancerInterceptor loadBalancerInterceptor(
            LoadBalancerClient loadBalancerClient,
            LoadBalancerRequestFactory requestFactory) {
      return new LoadBalancerInterceptor(loadBalancerClient, requestFactory);
    }

    @Bean
    @ConditionalOnMissingBean
    public RestTemplateCustomizer restTemplateCustomizer(
            final LoadBalancerInterceptor loadBalancerInterceptor) {
      return restTemplate -> {
        List<ClientHttpRequestInterceptor> list = new ArrayList<>(
                restTemplate.getInterceptors());
        list.add(loadBalancerInterceptor);
        restTemplate.setInterceptors(list);
      };
    }

  }
}
```
  2. 通过 `InterceptingClientHttpRequestFactory#InterceptingClientHttpRequest` 创建`org.springframework.http.client.InterceptingClientHttpRequest`
```java
public class InterceptingClientHttpRequestFactory extends AbstractClientHttpRequestFactoryWrapper {

	private final List<ClientHttpRequestInterceptor> interceptors;

	public InterceptingClientHttpRequestFactory(ClientHttpRequestFactory requestFactory,
			@Nullable List<ClientHttpRequestInterceptor> interceptors) {

		super(requestFactory);
		this.interceptors = (interceptors != null ? interceptors : Collections.emptyList());
	}

	@Override
	protected ClientHttpRequest createRequest(URI uri, HttpMethod httpMethod, ClientHttpRequestFactory requestFactory) {
		return new InterceptingClientHttpRequest(requestFactory, this.interceptors, uri, httpMethod);
	}

}
```
  3. `org.springframework.http.client.InterceptingClientHttpRequest` 调用`executeInternal`方法
```java
class InterceptingClientHttpRequest extends AbstractBufferingClientHttpRequest {

  private final ClientHttpRequestFactory requestFactory;

  private final List<ClientHttpRequestInterceptor> interceptors;

  private HttpMethod method;

  private URI uri;


  protected InterceptingClientHttpRequest(ClientHttpRequestFactory requestFactory,
                                          List<ClientHttpRequestInterceptor> interceptors, URI uri, HttpMethod method) {

    this.requestFactory = requestFactory;
    this.interceptors = interceptors;
    this.method = method;
    this.uri = uri;
  }


  @Override
  public HttpMethod getMethod() {
    return this.method;
  }

  @Override
  public String getMethodValue() {
    return this.method.name();
  }

  @Override
  public URI getURI() {
    return this.uri;
  }

  @Override
  protected final ClientHttpResponse executeInternal(HttpHeaders headers, byte[] bufferedOutput) throws IOException {
    InterceptingRequestExecution requestExecution = new InterceptingRequestExecution();
    return requestExecution.execute(this, bufferedOutput);
  }
  // 执行拦截器
  private class InterceptingRequestExecution implements ClientHttpRequestExecution {

    private final Iterator<ClientHttpRequestInterceptor> iterator;

    public InterceptingRequestExecution() {
      this.iterator = interceptors.iterator();
    }

    @Override
    public ClientHttpResponse execute(HttpRequest request, byte[] body) throws IOException {
      if (this.iterator.hasNext()) {
          // 通过迭代执行
        ClientHttpRequestInterceptor nextInterceptor = this.iterator.next();
        return nextInterceptor.intercept(request, body, this);
      }
      else {
          //最终会调用该方法
        HttpMethod method = request.getMethod();
        Assert.state(method != null, "No standard HTTP method");
        // org.springframework.http.client.SimpleBufferingClientHttpRequest
        ClientHttpRequest delegate = requestFactory.createRequest(request.getURI(), method);
        request.getHeaders().forEach((key, value) -> delegate.getHeaders().addAll(key, value));
        if (body.length > 0) {
          if (delegate instanceof StreamingHttpOutputMessage) {
            StreamingHttpOutputMessage streamingOutputMessage = (StreamingHttpOutputMessage) delegate;
            streamingOutputMessage.setBody(outputStream -> StreamUtils.copy(body, outputStream));
          }
          else {
            StreamUtils.copy(body, delegate.getBody());
          }
        }
        return delegate.execute();
      }
    }
  }
}
```
  4. 通过`org.springframework.cloud.client.loadbalancer.LoadBalancerInterceptor` 拦截请求
```java
public class LoadBalancerInterceptor implements ClientHttpRequestInterceptor {

	private LoadBalancerClient loadBalancer;

	private LoadBalancerRequestFactory requestFactory;

	public LoadBalancerInterceptor(LoadBalancerClient loadBalancer,
			LoadBalancerRequestFactory requestFactory) {
		this.loadBalancer = loadBalancer;
		this.requestFactory = requestFactory;
	}

	public LoadBalancerInterceptor(LoadBalancerClient loadBalancer) {
		// for backwards compatibility
		this(loadBalancer, new LoadBalancerRequestFactory(loadBalancer));
	}

	@Override
	public ClientHttpResponse intercept(final HttpRequest request, final byte[] body,
			final ClientHttpRequestExecution execution) throws IOException {
		final URI originalUri = request.getURI();
		String serviceName = originalUri.getHost();
		Assert.state(serviceName != null,
				"Request URI does not contain a valid hostname: " + originalUri);
		return this.loadBalancer.execute(serviceName,
				this.requestFactory.createRequest(request, body, execution));
	}

}
```
  5. `org.springframework.cloud.netflix.ribbon.RibbonLoadBalancerClient` 执行
```java
public class RibbonLoadBalancerClient implements LoadBalancerClient {
    //... ...
    public <T> T execute(String serviceId, LoadBalancerRequest<T> request, Object hint)
            throws IOException {
        // 获取服务列表 默认是com.netflix.loadbalancer.ZoneAwareLoadBalancer
        ILoadBalancer loadBalancer = getLoadBalancer(serviceId);
        // 选择服务 默认 com.netflix.loadbalancer.ZoneAwareLoadBalancer.chooseServer 选择服务器
        Server server = getServer(loadBalancer, hint);
        if (server == null) {
            throw new IllegalStateException("No instances available for " + serviceId);
        }
        RibbonServer ribbonServer = new RibbonServer(serviceId, server,
                isSecure(server, serviceId),
                serverIntrospector(serviceId).getMetadata(server));

        return execute(serviceId, ribbonServer, request);
    }
    //... ...
    @Override
    public <T> T execute(String serviceId, ServiceInstance serviceInstance,
                         LoadBalancerRequest<T> request) throws IOException {
        Server server = null;
        if (serviceInstance instanceof RibbonServer) {
            server = ((RibbonServer) serviceInstance).getServer();
        }
        if (server == null) {
            throw new IllegalStateException("No instances available for " + serviceId);
        }

        RibbonLoadBalancerContext context = this.clientFactory
                .getLoadBalancerContext(serviceId);
        RibbonStatsRecorder statsRecorder = new RibbonStatsRecorder(context, server);

        try {
            // 执行请求
            T returnVal = request.apply(serviceInstance);
            statsRecorder.recordStats(returnVal);
            return returnVal;
        }
        // catch IOException and rethrow so RestTemplate behaves correctly
        catch (IOException ex) {
            statsRecorder.recordStats(ex);
            throw ex;
        }
        catch (Exception ex) {
            statsRecorder.recordStats(ex);
            ReflectionUtils.rethrowRuntimeException(ex);
        }
        return null;
    }
}
```
  6. `org.springframework.cloud.client.loadbalancer.LoadBalancerRequestFactory`  
```java
public class LoadBalancerRequestFactory {

	private LoadBalancerClient loadBalancer;

	private List<LoadBalancerRequestTransformer> transformers;

	public LoadBalancerRequestFactory(LoadBalancerClient loadBalancer,
			List<LoadBalancerRequestTransformer> transformers) {
		this.loadBalancer = loadBalancer;
		this.transformers = transformers;
	}

	public LoadBalancerRequestFactory(LoadBalancerClient loadBalancer) {
		this.loadBalancer = loadBalancer;
	}
    // 创建Request
	public LoadBalancerRequest<ClientHttpResponse> createRequest(
			final HttpRequest request, final byte[] body,
			final ClientHttpRequestExecution execution) {
		return instance -> {
			HttpRequest serviceRequest = new ServiceRequestWrapper(request, instance,
					this.loadBalancer);
			if (this.transformers != null) {
				for (LoadBalancerRequestTransformer transformer : this.transformers) {
					serviceRequest = transformer.transformRequest(serviceRequest,
							instance);
				}
			}
            //InterceptingRequestExecution#execute
            // org.springframework.http.client.SimpleBufferingClientHttpRequest#execute
			return execution.execute(serviceRequest, body);
		};
	}

}
```
## 集成Nacos服务发现
* `com.netflix.loadbalancer.ZoneAwareLoadBalancer`
```java
public class ZoneAwareLoadBalancer<T extends Server> extends DynamicServerListLoadBalancer<T> {
    // ... ...
  // 选择服务
  @Override
  public Server chooseServer(Object key) {
    if (!ENABLED.get() || getLoadBalancerStats().getAvailableZones().size() <= 1) {
      logger.debug("Zone aware logic disabled or there is only one zone");
      return super.chooseServer(key);
    }
    Server server = null;
    try {
      LoadBalancerStats lbStats = getLoadBalancerStats();
      Map<String, ZoneSnapshot> zoneSnapshot = ZoneAvoidanceRule.createSnapshot(lbStats);
      logger.debug("Zone snapshots: {}", zoneSnapshot);
      if (triggeringLoad == null) {
        triggeringLoad = DynamicPropertyFactory.getInstance().getDoubleProperty(
                "ZoneAwareNIWSDiscoveryLoadBalancer." + this.getName() + ".triggeringLoadPerServerThreshold", 0.2d);
      }

      if (triggeringBlackoutPercentage == null) {
        triggeringBlackoutPercentage = DynamicPropertyFactory.getInstance().getDoubleProperty(
                "ZoneAwareNIWSDiscoveryLoadBalancer." + this.getName() + ".avoidZoneWithBlackoutPercetage", 0.99999d);
      }
      Set<String> availableZones = ZoneAvoidanceRule.getAvailableZones(zoneSnapshot, triggeringLoad.get(), triggeringBlackoutPercentage.get());
      logger.debug("Available zones: {}", availableZones);
      if (availableZones != null &&  availableZones.size() < zoneSnapshot.keySet().size()) {
        String zone = ZoneAvoidanceRule.randomChooseZone(zoneSnapshot, availableZones);
        logger.debug("Zone chosen: {}", zone);
        if (zone != null) {
          BaseLoadBalancer zoneLoadBalancer = getLoadBalancer(zone);
          server = zoneLoadBalancer.chooseServer(key);
        }
      }
    } catch (Exception e) {
      logger.error("Error choosing server using zone aware logic for load balancer={}", name, e);
    }
    if (server != null) {
      return server;
    } else {
      logger.debug("Zone avoidance logic is not invoked.");
      return super.chooseServer(key);
    }
  }
}
```