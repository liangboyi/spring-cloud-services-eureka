# spring-cloud-services-eureka

1.eureka registory

   eureka-service

2.eureka customer service

   eureka-customer-service

3.eureka customer client

   eureka-customer-client

---

#### eureka-service

创建eureka注册server，所有的service都将通过该server注册和被发现

Spring boot中使用@EnableEurekaServer即可

```
@EnableEurekaServer
@SpringBootApplication
public class EurekaServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(EurekaServiceApplication.class, args);
    }
}
```

#### eureka customer service

1.创建service名称为customer-service，将service注册到eureka中

 Spring boot使用@EnableEurekaClient

```
@EnableEurekaClient
@SpringBootApplication
public class CustomerClientApplication {
    public static void main(String[] args) {
        SpringApplication.run(CustomerClientApplication.class,args);
    }

```

 bootstrap.yml

```
spring:
  application:
    name: customer-service
  cloud:
    config:
      discovery:
        enabled: true
        service-id: spring-cloud-config-server

eureka:
  client:
    serviceUrl:
      defaultZone: http://spring-cloud-eureka:8761/eureka/
  instance:
    preferIpAddress: true
```

bootstrap.yml中eureka.client.serviceUrl.defaultZone配置eureka server的地址，其中 http://spring-cloud-eureka:8761/eureka 中的spring-cloud-eureka为eureka server的ip\(kube中暴露出service之后，起的名字为spring-cloud-eureka，本地启动可以在/etc/hosts中加上相应的映射即可，如 spring-cloud-eureka 127.0.0.1 \)

```
eureka:
  client:
    serviceUrl:
      defaultZone: http://spring-cloud-eureka:8761/eureka/
  instance:
    preferIpAddress: true

```

2.service中使用spring-cloud-config

bootstrap.yml中配置spring-cloud-config server的serviceId，在eureka server中通过serviceId名称\(spring-cloud-config-server\)查找对应的config-server

```
spring:
  application:
    name: customer-service
  cloud:
    config:
      discovery:
        enabled: true
        service-id: spring-cloud-config-server
```

3.通过curl -X POST http://xxxx:8080/refresh 刷新git的值到spring-cloud-config中

通过curl -X POST http://xxxx:8080/refresh 命令，刷新github的值到config-server，如果github中有值修改过，自动刷新

配置@RefreshScope后，可通过curl -X POST http://ip:port/refresh 更新环境变量的值

```
@RefreshScope
@RestController
class CustomerServiceRestController {
    @Value("${customer.name1:this is default name1}")
    private String name1;
    @Value("${customer.name2:this is default name2}")
    private String name2;
    @Value("${customer.invalid:this is default invalid}")
    private String invalidName;
    
    @RequestMapping(value="/customer/{id}",method = RequestMethod.GET ,produces = "application/json")
    public String getCustomerName(@PathVariable("id") int id) {
        String resultName = "";
        if(id == 1)
            resultName = name1;
        else if (id == 2)
            resultName = name2;
        else
            resultName = invalidName;
        return resultName;
    }
}
```

application.yml中配置management.security.enabled.为false，如不配置，curl -X POST http://client:8080 时会报401错误

```
server:
  port: 8080
management:
  security:
    enabled: false
```

#### eureka customer client

1.创建service名称为customer-client，将service注册到eureka中

Spring boot中使用@EnableEurekaClient

```
EnableEurekaClient
@SpringBootApplication
public class CustomerClientApplication {
    public static void main(String[] args) {
        SpringApplication.run(CustomerClientApplication.class,args);
    }

    @RestController
    class CustomerClientRestController {

        @Autowired
        RestTemplate restTemplate;

        @RequestMapping(value="/customer/{id}",method = RequestMethod.GET ,produces = "application/json")
        public String getCustomerName(@PathVariable("id") int id) {
            return restTemplate.getForEntity("http://customer-service/customer/"+id, String.class).getBody();
        }
    }

}
```

2.service中使用restTemplate，在eureka server中查找serviceId为customer-service的service，调用并返回对应的值

```
@RequestMapping(value="/customer/{id}",method = RequestMethod.GET ,produces = "application/json")
public String getCustomerName(@PathVariable("id") int id) {
  return restTemplate.getForEntity("http://customer-service/customer/"+id, String.class).getBody();
}

```





