package io.pivotal.boot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * Created by bliang on 17/05/2017.
 */
@EnableEurekaClient
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
