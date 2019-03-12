package com.xincao9.prs.logminer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.kafka.annotation.EnableKafka;

/**
 *
 * @author xincao9@gmail.com
 */
@SpringBootApplication
@EnableEurekaClient
@EnableKafka
public class Application {

    public static void main(String... args) {
        SpringApplication.run(Application.class, args);
    }
}
