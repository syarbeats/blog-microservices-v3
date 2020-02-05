package com.mitrais.cdc.blogmicroservices;

import com.mitrais.cdc.blogmicroservices.utility.KafkaCustomChannel;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.stream.annotation.EnableBinding;

@SpringBootApplication
@RefreshScope
@EnableEurekaClient
@EnableBinding(KafkaCustomChannel.class)
public class BlogMicroservicesApplication {

    public static void main(String[] args) {
        SpringApplication.run(BlogMicroservicesApplication.class, args);
    }

}
