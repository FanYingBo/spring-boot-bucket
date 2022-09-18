##Sleuth 术语:
* span（跨度）：基本工作单元。例如，在一个新建的 span 中发送一个 RPC 等同于发送一个回应请求给 RPC，span 通过一个 64 位 ID 唯一标识，trace 以另一个 64 位 ID 表示，span 还有其他数据信息，比如摘要、时间戳事件、关键值注释（tags）、span 的 ID，以及进度 ID（通常是 IP 地址)。span 在不断的启动和停止，同时记录了时间信息，当你创建了一个 span，你必须在未来的某个时刻停止它。
* trace（追踪）：一组共享“root span”的 span 组成的树状结构成为 trace。trace 也用一个 64 位的 ID 唯一标识，trace 中的所有 span 都共享该 trace 的 ID。
* annotation（标注）：用来及时记录一个事件的存在，一些核心 annotations 用来定义一个请求的开始和结束。  
  1. CS，即 Client Sent，客户端发起一个请求，这个 annotion 描述了这个 span 的开始。 
  2. SR，即 Server Received，服务端获得请求并准备开始处理它，如果将其 SR 减去 CS 时间戳便可得到网络延迟。
  3. SS，即 Server Sent，注解表明请求处理的完成（当请求返回客户端），如果 SS 减去 SR 时间戳便可得到服务端需要的处理请求时间。
  4. CR，即 Client Received，表明 span 的结束，客户端成功接收到服务端的回复，如果 CR 减去 CS 时间戳便可得到客户端从服务端获取回复的所有所需时间。

如果时HTTP接口，HTTP Headers 会添加如下信息：    
1. x-b3-spanid：一个工作单元(rpc 调用)的唯一标识。
2. x-b3-parentspanid：当前工作单元的上一个工作单元，Root Span(请求链路的第一个工作单元)的值为空。
3. x-b3-traceid：一条请求链条(trace) 的唯一标识。
4. x-b3-sampled：是否被抽样为输出的标志，1 为需要被输出，0 为不需要被输出。
日志信息：  
   `2022-09-15 22:53:31.476  INFO [user-service,243f2458bc4157d2,dc948235ca018d89,true] 16936 --- [nio-9092-exec-1] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Starting...
   2022-09-15 22:53:31.710  INFO [user-service,243f2458bc4157d2,dc948235ca018d89,true] 16936 --- [nio-9092-exec-1] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Start completed.`
MDC（Mapped Diagnostic Context，映射调试上下文）：  
MDC 中的信息：[appname,traceId,spanId,exportable]  
* appname：应用名称，即 spring.application.name 的值。  
* tranceId：整个请求链路的唯一ID。  
* spanId：基本的工作单元，一个 RPC 调用就是一个新的 span。启动跟踪的初始 span 称为 root span ，此 spanId 的值与 traceId 的值相同。
* exportable：是否将数据导入到 Zipkin 中，true 表示导入成功，false 表示导入失败。

## HTTP客户端调用
brave.spring.web.TracingClientHttpRequestInterceptor