package io.pivotal.boot;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by bliang on 17/05/2017.
 */
@EnableDiscoveryClient
@SpringBootApplication
public class CustomerServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(CustomerServiceApplication.class, args);
    }

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
}
