package com.xincao9.prs.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 *
 * @author xincao9@gmail.com
 */
@SpringBootApplication
@EnableEurekaServer
public class Application {

    public static void main(String... args) {
        SpringApplication.run(Application.class, args);
    }
}
